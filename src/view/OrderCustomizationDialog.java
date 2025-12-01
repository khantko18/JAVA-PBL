package view;

import model.MenuItem;
import util.LanguageManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class OrderCustomizationDialog extends JDialog {
    private MenuItem menuItem;
    private LanguageManager langManager;
    
    // UI Components
    private JRadioButton iceButton, hotButton, takeOutButton, inStoreButton;
    private JCheckBox addShotCheckBox, whippingCreamCheckBox;
    private JButton confirmButton, cancelButton;
    private JSpinner quantitySpinner;
    private JPanel tempPanel, quantityPanel, extrasPanel, orderTypePanel, detailsPanel;
    private JLabel nameLabel, categoryLabel, priceLabel;
    
    // Result values
    private boolean confirmed = false;
    private String temperature;
    private int quantity;
    private String orderType;
    private boolean addShot = false;
    private boolean whippingCream = false;
    
    public OrderCustomizationDialog(Frame parent, MenuItem item) {
        super(parent, true);
        this.menuItem = item;
        this.langManager = LanguageManager.getInstance();
        
        initializeComponents();
        updateFonts();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initializeComponents() {
        setTitle(langManager.getText("customize_order") + " - " + menuItem.getName());
        setLayout(new BorderLayout(10, 10));
        
        boolean isCoffeeOrBeverage = menuItem.getCategory().equalsIgnoreCase("Coffee") || 
                                     menuItem.getCategory().equalsIgnoreCase("Beverage");
        
        int rows = isCoffeeOrBeverage ? 5 : 4;
        JPanel mainPanel = new JPanel(new GridLayout(rows, 1, 10, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 1. Temperature
        tempPanel = createTitledPanel("temperature");
        JPanel tempRadioPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        tempRadioPanel.setBackground(Color.WHITE);
        tempRadioPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        iceButton = createStyledRadioButton("ice", new Color(200, 230, 255), new Color(0, 100, 200));
        hotButton = createStyledRadioButton("hot", new Color(255, 220, 220), new Color(200, 50, 50));
        iceButton.setSelected(true);
        
        ButtonGroup tempGroup = new ButtonGroup();
        tempGroup.add(iceButton); tempGroup.add(hotButton);
        tempRadioPanel.add(iceButton); tempRadioPanel.add(hotButton);
        tempPanel.add(tempRadioPanel, BorderLayout.CENTER);
        mainPanel.add(tempPanel);
        
        // 2. Quantity
        quantityPanel = createTitledPanel("quantity");
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        spinnerPanel.setBackground(Color.WHITE);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        quantitySpinner.setPreferredSize(new Dimension(100, 45));
        spinnerPanel.add(quantitySpinner);
        quantityPanel.add(spinnerPanel, BorderLayout.CENTER);
        mainPanel.add(quantityPanel);
        
        // 3. Extras
        if (isCoffeeOrBeverage) {
            extrasPanel = createTitledPanel("extras");
            JPanel checkBoxPanel = new JPanel(new GridLayout(1, 2, 15, 0));
            checkBoxPanel.setBackground(Color.WHITE);
            checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            addShotCheckBox = createStyledCheckBox("add_shot", new Color(255, 240, 220), new Color(139, 69, 19));
            whippingCreamCheckBox = createStyledCheckBox("whipping_cream", new Color(255, 250, 240), new Color(101, 67, 33));
            
            checkBoxPanel.add(addShotCheckBox); checkBoxPanel.add(whippingCreamCheckBox);
            extrasPanel.add(checkBoxPanel, BorderLayout.CENTER);
            mainPanel.add(extrasPanel);
        }
        
        // 4. Order Type
        orderTypePanel = createTitledPanel("order_type");
        JPanel typeRadioPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        typeRadioPanel.setBackground(Color.WHITE);
        typeRadioPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        takeOutButton = createStyledRadioButton("take_out", new Color(255, 245, 200), new Color(180, 130, 0));
        inStoreButton = createStyledRadioButton("in_store", new Color(220, 255, 220), new Color(50, 150, 50));
        takeOutButton.setSelected(true);
        
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(takeOutButton); typeGroup.add(inStoreButton);
        typeRadioPanel.add(takeOutButton); typeRadioPanel.add(inStoreButton);
        orderTypePanel.add(typeRadioPanel, BorderLayout.CENTER);
        mainPanel.add(orderTypePanel);
        
        // 5. Details
        detailsPanel = createTitledPanel("item_details");
        detailsPanel.setBackground(new Color(250, 250, 250));
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        nameLabel = new JLabel(menuItem.getName());
        nameLabel.setForeground(new Color(50, 50, 50));
        categoryLabel = new JLabel(langManager.translateCategory(menuItem.getCategory()));
        categoryLabel.setForeground(Color.GRAY);
        priceLabel = new JLabel(langManager.formatPrice(menuItem.getPrice()) + " " + langManager.getText("per_unit"));
        priceLabel.setForeground(new Color(0, 150, 0));
        
        infoPanel.add(nameLabel); infoPanel.add(categoryLabel); infoPanel.add(priceLabel);
        detailsPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(detailsPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonsPanel.setBackground(Color.WHITE);
        
        cancelButton = new JButton(langManager.getText("cancel"));
        cancelButton.setPreferredSize(new Dimension(100, 45));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());
        
        confirmButton = new JButton(langManager.getText("add_to_order"));
        confirmButton.setPreferredSize(new Dimension(140, 45));
        
        // [수정] 배경색은 유지하되, 글자색을 검은색으로 변경하여 밝은 배경에서도 잘 보이게 함
        confirmButton.setBackground(new Color(40, 167, 69)); // 원래 초록색
        confirmButton.setForeground(Color.BLACK); // [수정] WHITE -> BLACK
        
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(e -> confirmOrder());
        
        buttonsPanel.add(cancelButton); buttonsPanel.add(confirmButton);
        add(buttonsPanel, BorderLayout.SOUTH);
    }
    
    private void confirmOrder() {
        temperature = iceButton.isSelected() ? "ICE" : "HOT";
        quantity = (Integer) quantitySpinner.getValue();
        orderType = takeOutButton.isSelected() ? "TAKE_OUT" : "IN_STORE";
        if (addShotCheckBox != null) addShot = addShotCheckBox.isSelected();
        if (whippingCreamCheckBox != null) whippingCream = whippingCreamCheckBox.isSelected();
        confirmed = true;
        dispose();
    }
    
    private void updateFonts() {
        String fontName = (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) ? "Malgun Gothic" : "Arial";
        Font titleFont = new Font(fontName, Font.BOLD, 13);
        Font optionFont = new Font(fontName, Font.BOLD, 14);
        Font detailFont = new Font(fontName, Font.BOLD, 16);
        Font subFont = new Font(fontName, Font.PLAIN, 12);
        Font btnFont = new Font(fontName, Font.BOLD, 14);
        
        updateBorderTitle(tempPanel, "temperature", titleFont);
        updateBorderTitle(quantityPanel, "quantity", titleFont);
        if(extrasPanel != null) updateBorderTitle(extrasPanel, "extras", titleFont);
        updateBorderTitle(orderTypePanel, "order_type", titleFont);
        updateBorderTitle(detailsPanel, "item_details", titleFont);
        
        iceButton.setFont(optionFont); iceButton.setText(langManager.getText("ice"));
        hotButton.setFont(optionFont); hotButton.setText(langManager.getText("hot"));
        quantitySpinner.setFont(new Font(fontName, Font.BOLD, 18));
        
        if(addShotCheckBox != null) {
            addShotCheckBox.setFont(optionFont); addShotCheckBox.setText(langManager.getText("add_shot"));
            whippingCreamCheckBox.setFont(optionFont); whippingCreamCheckBox.setText(langManager.getText("whipping_cream"));
        }
        
        takeOutButton.setFont(optionFont); takeOutButton.setText(langManager.getText("take_out"));
        inStoreButton.setFont(optionFont); inStoreButton.setText(langManager.getText("in_store"));
        
        nameLabel.setFont(detailFont);
        categoryLabel.setFont(subFont); categoryLabel.setText(langManager.translateCategory(menuItem.getCategory()));
        priceLabel.setFont(optionFont); priceLabel.setText(langManager.formatPrice(menuItem.getPrice()) + " " + langManager.getText("per_unit"));
        
        cancelButton.setFont(btnFont); cancelButton.setText(langManager.getText("cancel"));
        confirmButton.setFont(btnFont); confirmButton.setText(langManager.getText("add_to_order"));
    }
    
    private void updateBorderTitle(JPanel panel, String key, Font font) {
        if(panel.getBorder() instanceof TitledBorder) {
            TitledBorder border = (TitledBorder) panel.getBorder();
            border.setTitle(langManager.getText(key));
            border.setTitleFont(font);
            panel.repaint();
        }
    }
    
    private JPanel createTitledPanel(String key) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), langManager.getText(key)));
        panel.setBackground(Color.WHITE);
        return panel;
    }
    
    private JRadioButton createStyledRadioButton(String key, Color bg, Color fg) {
        JRadioButton btn = new JRadioButton(langManager.getText(key));
        btn.setBackground(bg); btn.setForeground(fg);
        btn.setOpaque(true); btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return btn;
    }
    
    private JCheckBox createStyledCheckBox(String key, Color bg, Color fg) {
        JCheckBox box = new JCheckBox(langManager.getText(key));
        box.setBackground(bg); box.setForeground(fg);
        box.setOpaque(true); box.setFocusPainted(false);
        box.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return box;
    }
    
    public boolean isConfirmed() { return confirmed; }
    public String getTemperature() { return temperature; }
    public int getQuantity() { return quantity; }
    public String getOrderType() { return orderType; }
    public boolean isAddShot() { return addShot; }
    public boolean isWhippingCream() { return whippingCream; }
}