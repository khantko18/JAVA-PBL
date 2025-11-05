package view;

import util.LanguageManager;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog for processing payments
 */
public class PaymentDialog extends JDialog {
    private JRadioButton cashRadio;
    private JRadioButton cardRadio;
    private JTextField amountReceivedField;
    private JLabel changeLabel;
    private JButton confirmButton;
    private JButton cancelButton;
    private double totalAmount;
    private boolean confirmed;
    private LanguageManager langManager;
    
    private JLabel totalAmountLabel;
    private JLabel paymentMethodLabel;
    private JLabel amountReceivedLabel;
    private JLabel changeTextLabel;
    
    public PaymentDialog(Frame parent, double totalAmount) {
        super(parent);
        this.langManager = LanguageManager.getInstance();
        this.totalAmount = totalAmount;
        this.confirmed = false;
        
        setTitle(langManager.getText("process_payment"));
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Center panel - Payment details
        JPanel centerPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Total amount
        totalAmountLabel = new JLabel(langManager.getText("total_amount"));
        centerPanel.add(totalAmountLabel);
        JLabel totalLabel = new JLabel(langManager.formatPrice(totalAmount));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(0, 128, 0));
        centerPanel.add(totalLabel);
        
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
        cashRadio.addActionListener(e -> amountReceivedField.setEnabled(true));
        cardRadio.addActionListener(e -> {
            amountReceivedField.setEnabled(false);
            amountReceivedField.setText("");
            changeLabel.setText("$0.00");
        });
        
        amountReceivedField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                calculateChange();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void calculateChange() {
        try {
            double received = Double.parseDouble(amountReceivedField.getText());
            double change = received - totalAmount;
            changeLabel.setText(langManager.formatPrice(Math.max(0, change)));
        } catch (NumberFormatException ex) {
            changeLabel.setText(langManager.formatPrice(0));
        }
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
            return Double.parseDouble(amountReceivedField.getText());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    public JButton getConfirmButton() {
        return confirmButton;
    }
    
    public JButton getCancelButton() {
        return cancelButton;
    }
}

