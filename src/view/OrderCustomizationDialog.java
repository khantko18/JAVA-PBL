package view;

import model.MenuItem;
import util.LanguageManager;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog for customizing order items (temperature, quantity, order type)
 */
public class OrderCustomizationDialog extends JDialog {
    private MenuItem menuItem;
    private LanguageManager langManager;
    
    // UI Components
    private JRadioButton iceButton;
    private JRadioButton hotButton;
    private JSpinner quantitySpinner;
    private JRadioButton takeOutButton;
    private JRadioButton inStoreButton;
    private JCheckBox addShotCheckBox;
    private JCheckBox whippingCreamCheckBox;
    private JButton confirmButton;
    private JButton cancelButton;
    
    // Result values
    private boolean confirmed = false;
    private String temperature;
    private int quantity;
    private String orderType;
    private boolean addShot = false;
    private boolean whippingCream = false;
    
    public OrderCustomizationDialog(Frame parent, MenuItem item) {
        super(parent, true); // Modal dialog
        this.menuItem = item;
        this.langManager = LanguageManager.getInstance();
        
        initializeComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initializeComponents() {
        setTitle(langManager.getText("customize_order") + " - " + menuItem.getName());
        setLayout(new BorderLayout(10, 10));
        
        // Check if item is Coffee or Beverage
        boolean isCoffeeOrBeverage = menuItem.getCategory().equalsIgnoreCase("Coffee") || 
                                     menuItem.getCategory().equalsIgnoreCase("Beverage");
        
        // Main panel - dynamic grid based on category
        int rows = isCoffeeOrBeverage ? 5 : 4;
        JPanel mainPanel = new JPanel(new GridLayout(rows, 1, 10, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 1. Temperature Selection
        JPanel tempPanel = createTemperaturePanel();
        mainPanel.add(tempPanel);
        
        // 2. Quantity Selection
        JPanel quantityPanel = createQuantityPanel();
        mainPanel.add(quantityPanel);
        
        // 3. Extras (only for Coffee/Beverage)
        if (isCoffeeOrBeverage) {
            JPanel extrasPanel = createExtrasPanel();
            mainPanel.add(extrasPanel);
        }
        
        // 4. Order Type Selection
        JPanel orderTypePanel = createOrderTypePanel();
        mainPanel.add(orderTypePanel);
        
        // 5. Item Details
        JPanel detailsPanel = createDetailsPanel();
        mainPanel.add(detailsPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        add(buttonsPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTemperaturePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            langManager.getText("temperature"),
            0,
            0,
            new Font("Arial", Font.BOLD, 13)
        ));
        panel.setBackground(Color.WHITE);
        
        JPanel radioPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        radioPanel.setBackground(Color.WHITE);
        radioPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Temperature buttons with custom styling
        iceButton = new JRadioButton(langManager.getText("ice"), true);
        iceButton.setFont(new Font("Arial", Font.BOLD, 15));
        iceButton.setBackground(new Color(200, 230, 255)); // Light blue
        iceButton.setForeground(new Color(0, 100, 200)); // Dark blue text
        iceButton.setOpaque(true);
        iceButton.setFocusPainted(false);
        iceButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 180, 255), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        hotButton = new JRadioButton(langManager.getText("hot"));
        hotButton.setFont(new Font("Arial", Font.BOLD, 15));
        hotButton.setBackground(new Color(255, 220, 220)); // Light red
        hotButton.setForeground(new Color(200, 50, 50)); // Dark red text
        hotButton.setOpaque(true);
        hotButton.setFocusPainted(false);
        hotButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 150, 150), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        ButtonGroup tempGroup = new ButtonGroup();
        tempGroup.add(iceButton);
        tempGroup.add(hotButton);
        
        radioPanel.add(iceButton);
        radioPanel.add(hotButton);
        
        panel.add(radioPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createQuantityPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            langManager.getText("quantity"),
            0,
            0,
            new Font("Arial", Font.BOLD, 13)
        ));
        panel.setBackground(Color.WHITE);
        
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        spinnerPanel.setBackground(Color.WHITE);
        spinnerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 99, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.setFont(new Font("Arial", Font.BOLD, 20));
        ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        quantitySpinner.setPreferredSize(new Dimension(100, 45));
        quantitySpinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 180, 255), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        spinnerPanel.add(quantitySpinner);
        panel.add(spinnerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createExtrasPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            langManager.getText("extras"),
            0,
            0,
            new Font("Arial", Font.BOLD, 13)
        ));
        panel.setBackground(Color.WHITE);
        
        JPanel checkBoxPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        checkBoxPanel.setBackground(Color.WHITE);
        checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Add Shot checkbox
        addShotCheckBox = new JCheckBox(langManager.getText("add_shot"));
        addShotCheckBox.setFont(new Font("Arial", Font.BOLD, 14));
        addShotCheckBox.setBackground(new Color(255, 240, 220)); // Light orange
        addShotCheckBox.setForeground(new Color(139, 69, 19)); // Brown text
        addShotCheckBox.setOpaque(true);
        addShotCheckBox.setFocusPainted(false);
        addShotCheckBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 180, 140), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // Whipping Cream checkbox
        whippingCreamCheckBox = new JCheckBox(langManager.getText("whipping_cream"));
        whippingCreamCheckBox.setFont(new Font("Arial", Font.BOLD, 14));
        whippingCreamCheckBox.setBackground(new Color(255, 250, 240)); // Light cream
        whippingCreamCheckBox.setForeground(new Color(101, 67, 33)); // Dark brown text
        whippingCreamCheckBox.setOpaque(true);
        whippingCreamCheckBox.setFocusPainted(false);
        whippingCreamCheckBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 184, 135), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        checkBoxPanel.add(addShotCheckBox);
        checkBoxPanel.add(whippingCreamCheckBox);
        
        panel.add(checkBoxPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createOrderTypePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            langManager.getText("order_type"),
            0,
            0,
            new Font("Arial", Font.BOLD, 13)
        ));
        panel.setBackground(Color.WHITE);
        
        JPanel radioPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        radioPanel.setBackground(Color.WHITE);
        radioPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Order type buttons with custom styling
        takeOutButton = new JRadioButton(langManager.getText("take_out"), true);
        takeOutButton.setFont(new Font("Arial", Font.BOLD, 15));
        takeOutButton.setBackground(new Color(255, 245, 200)); // Light yellow
        takeOutButton.setForeground(new Color(180, 130, 0)); // Dark yellow text
        takeOutButton.setOpaque(true);
        takeOutButton.setFocusPainted(false);
        takeOutButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 200, 100), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        inStoreButton = new JRadioButton(langManager.getText("in_store"));
        inStoreButton.setFont(new Font("Arial", Font.BOLD, 15));
        inStoreButton.setBackground(new Color(220, 255, 220)); // Light green
        inStoreButton.setForeground(new Color(50, 150, 50)); // Dark green text
        inStoreButton.setOpaque(true);
        inStoreButton.setFocusPainted(false);
        inStoreButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 220, 150), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        ButtonGroup orderTypeGroup = new ButtonGroup();
        orderTypeGroup.add(takeOutButton);
        orderTypeGroup.add(inStoreButton);
        
        radioPanel.add(takeOutButton);
        radioPanel.add(inStoreButton);
        
        panel.add(radioPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            langManager.getText("item_details"),
            0,
            0,
            new Font("Arial", Font.BOLD, 13)
        ));
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                langManager.getText("item_details"),
                0,
                0,
                new Font("Arial", Font.BOLD, 13)
            ),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel nameLabel = new JLabel(menuItem.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(new Color(50, 50, 50));
        
        JLabel categoryLabel = new JLabel(langManager.translateCategory(menuItem.getCategory()));
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        categoryLabel.setForeground(new Color(100, 100, 100));
        
        JLabel priceLabel = new JLabel(langManager.formatPrice(menuItem.getPrice()) + " " + langManager.getText("per_unit"));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(0, 150, 0));
        
        panel.add(nameLabel);
        panel.add(categoryLabel);
        panel.add(priceLabel);
        
        return panel;
    }
    
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));
        panel.setBackground(Color.WHITE);
        
        cancelButton = new JButton(langManager.getText("cancel"));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 15));
        cancelButton.setPreferredSize(new Dimension(140, 50));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(new Color(80, 80, 80));
        cancelButton.setFocusPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.setBorderPainted(true);
        cancelButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        cancelButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        confirmButton = new JButton(langManager.getText("add_to_order"));
        confirmButton.setFont(new Font("Arial", Font.BOLD, 15));
        confirmButton.setPreferredSize(new Dimension(180, 50));
        confirmButton.setBackground(new Color(40, 167, 69));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setOpaque(true);
        confirmButton.setBorderPainted(false);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        confirmButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        confirmButton.addActionListener(e -> {
            temperature = iceButton.isSelected() ? "ICE" : "HOT";
            quantity = (Integer) quantitySpinner.getValue();
            orderType = takeOutButton.isSelected() ? "TAKE_OUT" : "IN_STORE";
            
            // Get extras if checkboxes exist (Coffee/Beverage only)
            if (addShotCheckBox != null) {
                addShot = addShotCheckBox.isSelected();
            }
            if (whippingCreamCheckBox != null) {
                whippingCream = whippingCreamCheckBox.isSelected();
            }
            
            confirmed = true;
            dispose();
        });
        
        panel.add(cancelButton);
        panel.add(confirmButton);
        
        return panel;
    }
    
    // Getters
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public String getTemperature() {
        return temperature;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public String getOrderType() {
        return orderType;
    }
    
    public boolean isAddShot() {
        return addShot;
    }
    
    public boolean isWhippingCream() {
        return whippingCream;
    }
}

