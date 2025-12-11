package view;

import util.LanguageManager;
import controller.MembershipController;
import model.Member;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

<<<<<<< HEAD
=======
/**
 * Dialog for processing payments with membership support
 */
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
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
    
    // Labels for font updates
    private JLabel totalAmountLabel;
    private JLabel paymentMethodLabel;
    private JLabel amountReceivedLabel;
    private JLabel changeTextLabel;
    private JLabel phoneLabel;
    private JLabel membershipLabel;
<<<<<<< HEAD
    private JLabel finalAmountTitleLabel;
    private TitledBorder topPanelBorder;
    private JPanel topPanel;
=======
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
    
    public PaymentDialog(Frame parent, double totalAmount) {
        super(parent);
        this.langManager = LanguageManager.getInstance();
        this.membershipController = new MembershipController();
        this.originalAmount = totalAmount;
        this.finalAmount = totalAmount;
        this.confirmed = false;
        this.currentMember = null;
        
        setTitle(langManager.getText("process_payment"));
<<<<<<< HEAD
        setSize(500, 550);
=======
        setSize(500, 500);
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        
        initializeComponents();
        updateFonts();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel - Membership
<<<<<<< HEAD
        topPanel = new JPanel(new BorderLayout(5, 5));
        topPanelBorder = BorderFactory.createTitledBorder(langManager.getText("membership_discount_title"));
        topPanel.setBorder(topPanelBorder);
        topPanel.setPreferredSize(new Dimension(480, 120));
        
        JPanel memberInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        phoneLabel = new JLabel(langManager.getText("phone_number") + ":");
        phoneField = new JTextField(15);
        
        checkMemberButton = new JButton(langManager.getText("check_member"));
        checkMemberButton.setBackground(new Color(0, 123, 255)); // Blue background
        // [수정] 글자색을 흰색으로 하되, 폰트 설정을 확실히 하여 보이게 함
        checkMemberButton.setForeground(Color.WHITE); 
        checkMemberButton.setFocusPainted(false);
        checkMemberButton.setOpaque(true); // Mac/L&F 이슈 방지
        checkMemberButton.setBorderPainted(false); // 깔끔하게
        
        memberInputPanel.add(phoneLabel);
        memberInputPanel.add(phoneField);
        memberInputPanel.add(checkMemberButton);
        
        memberInfoLabel = new JLabel(langManager.getText("no_member_selected"));
        memberInfoLabel.setForeground(Color.GRAY);
        
        JPanel memberDisplayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        memberDisplayPanel.add(memberInfoLabel);
        
        topPanel.add(memberInputPanel, BorderLayout.NORTH);
        topPanel.add(memberDisplayPanel, BorderLayout.CENTER);
        
        // Center panel - Payment details
        JPanel centerPanel = new JPanel(new GridLayout(7, 2, 10, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        totalAmountLabel = new JLabel(langManager.getText("original_amount"));
        centerPanel.add(totalAmountLabel);
        JLabel totalLabel = new JLabel(langManager.formatPrice(originalAmount));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(Color.BLACK);
        centerPanel.add(totalLabel);
        
        membershipLabel = new JLabel(langManager.getText("membership_discount_label"));
        centerPanel.add(membershipLabel);
        discountLabel = new JLabel(langManager.formatPrice(0) + " (0%)");
        discountLabel.setForeground(new Color(220, 53, 69));
        centerPanel.add(discountLabel);
        
        finalAmountTitleLabel = new JLabel(langManager.getText("final_amount"));
        centerPanel.add(finalAmountTitleLabel);
        finalAmountLabel = new JLabel(langManager.formatPrice(finalAmount));
        finalAmountLabel.setForeground(new Color(0, 128, 0));
        centerPanel.add(finalAmountLabel);
        
        centerPanel.add(new JLabel("")); centerPanel.add(new JLabel(""));
        
=======
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
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
        paymentMethodLabel = new JLabel(langManager.getText("payment_method"));
        centerPanel.add(paymentMethodLabel);
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cashRadio = new JRadioButton(langManager.getText("cash"), true);
        cardRadio = new JRadioButton(langManager.getText("card"));
        ButtonGroup methodGroup = new ButtonGroup();
        methodGroup.add(cashRadio); methodGroup.add(cardRadio);
        methodPanel.add(cashRadio); methodPanel.add(Box.createHorizontalStrut(15)); methodPanel.add(cardRadio);
        centerPanel.add(methodPanel);
        
        amountReceivedLabel = new JLabel(langManager.getText("amount_received"));
        centerPanel.add(amountReceivedLabel);
        amountReceivedField = new JTextField();
        centerPanel.add(amountReceivedField);
        
        changeTextLabel = new JLabel(langManager.getText("change"));
        centerPanel.add(changeTextLabel);
        changeLabel = new JLabel(langManager.formatPrice(0));
        centerPanel.add(changeLabel);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        confirmButton = new JButton(langManager.getText("confirm_payment"));
        confirmButton.setBackground(new Color(40, 167, 69));
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setPreferredSize(new Dimension(160, 45));
        confirmButton.setFocusPainted(false);
        
        cancelButton = new JButton(langManager.getText("cancel"));
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setPreferredSize(new Dimension(120, 45));
        cancelButton.setFocusPainted(false);
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
<<<<<<< HEAD
        // Listeners
        checkMemberButton.addActionListener(e -> checkMembership());
        phoneField.addActionListener(e -> checkMembership());
=======
        // Add listeners
        checkMemberButton.addActionListener(e -> checkMembership());
        phoneField.addActionListener(e -> checkMembership());
        
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
        cashRadio.addActionListener(e -> amountReceivedField.setEnabled(true));
        cardRadio.addActionListener(e -> {
            amountReceivedField.setEnabled(false);
            amountReceivedField.setText("");
            changeLabel.setText(langManager.formatPrice(0));
        });
        amountReceivedField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { calculateChange(); }
        });
        confirmButton.addActionListener(e -> {
            if (isCashPayment()) {
                double received = getAmountReceived();
                if (received < finalAmount) {
                    JOptionPane.showMessageDialog(this, langManager.getText("insufficient_payment"), langManager.getText("payment_error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            confirmed = true;
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());
    }
    
<<<<<<< HEAD
    private void updateFonts() {
        String fontName = (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) ? "Malgun Gothic" : "Arial";
        Font labelFont = new Font(fontName, Font.BOLD, 12);
        Font valFont = new Font(fontName, Font.BOLD, 15);
        Font btnFont = new Font(fontName, Font.BOLD, 14);
        Font fieldFont = new Font(fontName, Font.PLAIN, 14);
        Font plainFont = new Font(fontName, Font.PLAIN, 12);
        
        topPanelBorder.setTitle(langManager.getText("membership_discount_title"));
        topPanelBorder.setTitleFont(labelFont);
        topPanel.repaint();
        
        setTitle(langManager.getText("process_payment"));
        
        phoneLabel.setFont(labelFont);
        phoneLabel.setText(langManager.getText("phone_number") + ":");
        
        // [수정] 회원 조회 버튼 폰트 적용
        checkMemberButton.setFont(new Font(fontName, Font.BOLD, 12));
        checkMemberButton.setText(langManager.getText("check_member"));
        
        if (currentMember == null) {
            memberInfoLabel.setText(langManager.getText("no_member_selected"));
        }
        memberInfoLabel.setFont(plainFont);
        
        totalAmountLabel.setFont(labelFont);
        totalAmountLabel.setText(langManager.getText("original_amount"));
        
        membershipLabel.setFont(labelFont);
        membershipLabel.setText(langManager.getText("membership_discount_label"));
        
        finalAmountTitleLabel.setFont(labelFont);
        finalAmountTitleLabel.setText(langManager.getText("final_amount"));
        
        paymentMethodLabel.setFont(labelFont);
        paymentMethodLabel.setText(langManager.getText("payment_method"));
        
        cashRadio.setFont(plainFont);
        cashRadio.setText(langManager.getText("cash"));
        
        cardRadio.setFont(plainFont);
        cardRadio.setText(langManager.getText("card"));
        
        amountReceivedLabel.setFont(labelFont);
        amountReceivedLabel.setText(langManager.getText("amount_received"));
        
        changeTextLabel.setFont(labelFont);
        changeTextLabel.setText(langManager.getText("change"));
        
        phoneField.setFont(fieldFont);
        amountReceivedField.setFont(fieldFont);
        
        confirmButton.setFont(btnFont);
        confirmButton.setText(langManager.getText("confirm_payment"));
        
        cancelButton.setFont(btnFont);
        cancelButton.setText(langManager.getText("cancel"));
        
        changeLabel.setFont(valFont);
        discountLabel.setFont(new Font(fontName, Font.BOLD, 14));
        finalAmountLabel.setFont(new Font(fontName, Font.BOLD, 18));
    }
    
    // ... (나머지 로직 동일: checkMembership, calculateChange, getters)
    private void checkMembership() {
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) { resetMembership(); return; }
        Member member = membershipController.getMemberByPhone(phone);
=======
    private void checkMembership() {
        String phone = phoneField.getText().trim();
        
        if (phone.isEmpty()) {
            resetMembership();
            return;
        }
        
        Member member = membershipController.getMemberByPhone(phone);
        
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
        if (member != null) {
            currentMember = member;
            double discountAmount = member.calculateDiscount(originalAmount);
            finalAmount = originalAmount - discountAmount;
<<<<<<< HEAD
            memberInfoLabel.setText("✓ " + member.getName() + " (" + member.getLevelName() + ")");
            memberInfoLabel.setForeground(new Color(0, 128, 0));
            discountLabel.setText(langManager.formatPrice(discountAmount) + String.format(" (%.0f%%)", member.getDiscountPercent()));
            finalAmountLabel.setText(langManager.formatPrice(finalAmount));
            calculateChange(); 
        } else {
            currentMember = null;
            memberInfoLabel.setText("✗ " + langManager.getText("member_not_found"));
=======
            
            memberInfoLabel.setText(String.format("✓ %s - %s (%.0f%% discount)", 
                member.getName(), member.getLevelDescription(), member.getDiscountPercent()));
            memberInfoLabel.setForeground(new Color(0, 128, 0));
            
            discountLabel.setText(String.format("%s (%.0f%%)", 
                langManager.formatPrice(discountAmount), member.getDiscountPercent()));
            finalAmountLabel.setText(langManager.formatPrice(finalAmount));
        } else {
            currentMember = null;
            memberInfoLabel.setText("✗ Phone number not found. No discount applied.");
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
            memberInfoLabel.setForeground(new Color(220, 53, 69));
            resetMembership();
        }
    }
<<<<<<< HEAD
=======
    
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
    private void resetMembership() {
        currentMember = null;
        finalAmount = originalAmount;
        discountLabel.setText(langManager.formatPrice(0) + " (0%)");
        finalAmountLabel.setText(langManager.formatPrice(finalAmount));
<<<<<<< HEAD
        calculateChange();
    }
=======
        if (phoneField.getText().trim().isEmpty()) {
            memberInfoLabel.setText("No member selected");
            memberInfoLabel.setForeground(Color.GRAY);
        }
    }
    
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
    private void calculateChange() {
        try {
            String text = amountReceivedField.getText().trim();
            if (text.isEmpty()) { changeLabel.setText(langManager.formatPrice(0)); return; }
            double received = Double.parseDouble(text);
            double change;
            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
<<<<<<< HEAD
                change = received - (finalAmount * 1200); 
                change = change / 1200.0;
=======
                // Convert Won to USD (divide by exchange rate)
                double receivedInUSD = received / 1200;
                change = receivedInUSD - finalAmount;
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
            } else {
                change = received - finalAmount;
            }
            if (change < 0) change = 0;
            changeLabel.setText(langManager.formatPrice(change));
        } catch (NumberFormatException ex) { changeLabel.setText(langManager.formatPrice(0)); }
    }
<<<<<<< HEAD
    public boolean isConfirmed() { return confirmed; }
    public boolean isCashPayment() { return cashRadio.isSelected(); }
=======
    
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
    
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
    public double getAmountReceived() {
        try {
            double val = Double.parseDouble(amountReceivedField.getText().trim());
            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) return val / 1200.0;
            return val;
        } catch (Exception e) { return 0.0; }
    }
<<<<<<< HEAD
    public Member getCurrentMember() { return currentMember; }
    public double getFinalAmount() { return finalAmount; }
}
=======
    
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

>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
