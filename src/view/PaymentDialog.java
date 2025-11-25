package view;

import util.LanguageManager;
import controller.MembershipController;
import model.Member;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog for processing payments with membership support
 */
public class PaymentDialog extends JDialog {
    private JRadioButton cashRadio;
    private JRadioButton cardRadio;
    private JTextField amountReceivedField;
    private JTextField phoneField;
    private JButton checkMemberButton;
    private JLabel changeLabel;
    private JLabel memberInfoLabel;
    private JLabel discountLabel;
    private JLabel finalAmountLabel;
    private JButton confirmButton;
    private JButton cancelButton;
    private double originalAmount;
    private double finalAmount;
    private boolean confirmed;
    private LanguageManager langManager;
    private MembershipController membershipController;
    private Member currentMember;
    
    private JLabel totalAmountLabel;
    private JLabel paymentMethodLabel;
    private JLabel amountReceivedLabel;
    private JLabel changeTextLabel;
    private JLabel phoneLabel;
    private JLabel membershipLabel;
    
    public PaymentDialog(Frame parent, double totalAmount) {
        super(parent);
        this.langManager = LanguageManager.getInstance();
        this.membershipController = new MembershipController();
        this.originalAmount = totalAmount;
        this.finalAmount = totalAmount;
        this.confirmed = false;
        this.currentMember = null;
        
        setTitle(langManager.getText("process_payment"));
        setSize(500, 500);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel - Membership
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Membership Discount"));
        topPanel.setPreferredSize(new Dimension(480, 120));
        
        JPanel memberInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        phoneLabel = new JLabel("Phone Number:");
        phoneField = new JTextField(15);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
        checkMemberButton = new JButton("Check Member");
        checkMemberButton.setBackground(new Color(0, 123, 255));
        checkMemberButton.setForeground(Color.WHITE);
        checkMemberButton.setFont(new Font("Arial", Font.BOLD, 12));
        checkMemberButton.setOpaque(true);
        checkMemberButton.setBorderPainted(false);
        
        memberInputPanel.add(phoneLabel);
        memberInputPanel.add(phoneField);
        memberInputPanel.add(checkMemberButton);
        
        memberInfoLabel = new JLabel("No member selected");
        memberInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        memberInfoLabel.setForeground(Color.GRAY);
        
        JPanel memberDisplayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        memberDisplayPanel.add(memberInfoLabel);
        
        topPanel.add(memberInputPanel, BorderLayout.NORTH);
        topPanel.add(memberDisplayPanel, BorderLayout.CENTER);
        
        // Center panel - Payment details
        JPanel centerPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Original amount
        totalAmountLabel = new JLabel("Original Amount:");
        centerPanel.add(totalAmountLabel);
        JLabel totalLabel = new JLabel(langManager.formatPrice(originalAmount));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(0, 0, 0));
        centerPanel.add(totalLabel);
        
        // Discount
        membershipLabel = new JLabel("Membership Discount:");
        centerPanel.add(membershipLabel);
        discountLabel = new JLabel(langManager.formatPrice(0) + " (0%)");
        discountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        discountLabel.setForeground(new Color(220, 53, 69));
        centerPanel.add(discountLabel);
        
        // Final amount
        centerPanel.add(new JLabel("Final Amount:"));
        finalAmountLabel = new JLabel(langManager.formatPrice(finalAmount));
        finalAmountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        finalAmountLabel.setForeground(new Color(0, 128, 0));
        centerPanel.add(finalAmountLabel);
        
        // Separator
        centerPanel.add(new JSeparator());
        centerPanel.add(new JSeparator());
        
        // Payment method
        paymentMethodLabel = new JLabel(langManager.getText("payment_method"));
        centerPanel.add(paymentMethodLabel);
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cashRadio = new JRadioButton(langManager.getText("cash"), true);
        cardRadio = new JRadioButton(langManager.getText("card"));
        ButtonGroup methodGroup = new ButtonGroup();
        methodGroup.add(cashRadio);
        methodGroup.add(cardRadio);
        methodPanel.add(cashRadio);
        methodPanel.add(cardRadio);
        centerPanel.add(methodPanel);
        
        // Amount received (for cash)
        amountReceivedLabel = new JLabel(langManager.getText("amount_received"));
        centerPanel.add(amountReceivedLabel);
        amountReceivedField = new JTextField();
        amountReceivedField.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(amountReceivedField);
        
        // Change
        changeTextLabel = new JLabel(langManager.getText("change"));
        centerPanel.add(changeTextLabel);
        changeLabel = new JLabel(langManager.formatPrice(0));
        changeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        centerPanel.add(changeLabel);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel - Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        confirmButton = new JButton(langManager.getText("confirm_payment"));
        confirmButton.setBackground(new Color(40, 167, 69));
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setPreferredSize(new Dimension(160, 40));
        
        cancelButton = new JButton(langManager.getText("cancel"));
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Add listeners
        checkMemberButton.addActionListener(e -> checkMembership());
        phoneField.addActionListener(e -> checkMembership());
        
        cashRadio.addActionListener(e -> amountReceivedField.setEnabled(true));
        cardRadio.addActionListener(e -> {
            amountReceivedField.setEnabled(false);
            amountReceivedField.setText("");
            changeLabel.setText(langManager.formatPrice(0));
        });
        
        amountReceivedField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                calculateChange();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void checkMembership() {
        String phone = phoneField.getText().trim();
        
        if (phone.isEmpty()) {
            resetMembership();
            return;
        }
        
        Member member = membershipController.getMemberByPhone(phone);
        
        if (member != null) {
            currentMember = member;
            double discountAmount = member.calculateDiscount(originalAmount);
            finalAmount = originalAmount - discountAmount;
            
            memberInfoLabel.setText(String.format("✓ %s - %s (%.0f%% discount)", 
                member.getName(), member.getLevelDescription(), member.getDiscountPercent()));
            memberInfoLabel.setForeground(new Color(0, 128, 0));
            
            discountLabel.setText(String.format("%s (%.0f%%)", 
                langManager.formatPrice(discountAmount), member.getDiscountPercent()));
            finalAmountLabel.setText(langManager.formatPrice(finalAmount));
        } else {
            currentMember = null;
            memberInfoLabel.setText("✗ Phone number not found. No discount applied.");
            memberInfoLabel.setForeground(new Color(220, 53, 69));
            resetMembership();
        }
    }
    
    private void resetMembership() {
        currentMember = null;
        finalAmount = originalAmount;
        discountLabel.setText(langManager.formatPrice(0) + " (0%)");
        finalAmountLabel.setText(langManager.formatPrice(finalAmount));
        if (phoneField.getText().trim().isEmpty()) {
            memberInfoLabel.setText("No member selected");
            memberInfoLabel.setForeground(Color.GRAY);
        }
    }
    
    private void calculateChange() {
        try {
            double received = Double.parseDouble(amountReceivedField.getText());
            double change;
            
            // If Korean language, user inputs in Won, so convert to base currency (USD)
            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                // Convert Won to USD (divide by exchange rate)
                double receivedInUSD = received / 1200;
                change = receivedInUSD - finalAmount;
            } else {
                change = received - finalAmount;
            }
            
            changeLabel.setText(langManager.formatPrice(Math.max(0, change)));
        } catch (NumberFormatException ex) {
            changeLabel.setText(langManager.formatPrice(0));
        }
    }
    
    public double getFinalAmount() {
        return finalAmount;
    }
    
    public Member getCurrentMember() {
        return currentMember;
    }
    
    public MembershipController getMembershipController() {
        return membershipController;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
    
    public boolean isCashPayment() {
        return cashRadio.isSelected();
    }
    
    public double getAmountReceived() {
        try {
            double received = Double.parseDouble(amountReceivedField.getText());
            // If Korean language, convert Won to USD
            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                return received / 1200;
            }
            return received;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    public double getOriginalAmount() {
        return originalAmount;
    }
    
    public JButton getConfirmButton() {
        return confirmButton;
    }
    
    public JButton getCancelButton() {
        return cancelButton;
    }
}

