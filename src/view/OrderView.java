package view;

import model.MenuItem;
import model.OrderItem;
import util.LanguageManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * View for processing customer orders
 */
public class OrderView extends JPanel {
    private JPanel menuPanel;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel totalLabel;
    private JTextField discountField;
    private JButton applyDiscountButton;
    private JButton payButton;
    private JButton clearButton;
    private JComboBox<String> categoryFilter;
    private LanguageManager langManager;
    
    private TitledBorder leftPanelBorder;
    private TitledBorder rightPanelBorder;
    private JLabel categoryLabel;
    private JLabel discountPercentLabel;
    private JLabel subtotalTextLabel;
    private JLabel discountTextLabel;
    private JLabel totalTextLabel;
    
    public OrderView() {
        langManager = LanguageManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
        setupLanguageListener();
    }
    
    private void initializeComponents() {
        // Left side - Menu items in card format
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanelBorder = BorderFactory.createTitledBorder(langManager.getText("menu_items"));
        leftPanel.setBorder(leftPanelBorder);
        
        // Category filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryLabel = new JLabel(langManager.getText("category"));
        filterPanel.add(categoryLabel);
        updateCategoryFilter();
        filterPanel.add(categoryFilter);
        leftPanel.add(filterPanel, BorderLayout.NORTH);
        
        // Menu items panel with scroll
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 2, 10, 10));
        JScrollPane menuScroll = new JScrollPane(menuPanel);
        menuScroll.getVerticalScrollBar().setUnitIncrement(16);
        leftPanel.add(menuScroll, BorderLayout.CENTER);
        
        // Right side - Order details
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanelBorder = BorderFactory.createTitledBorder(langManager.getText("current_order"));
        rightPanel.setBorder(rightPanelBorder);
        rightPanel.setPreferredSize(new Dimension(400, 0));
        
        // Order table
        updateTableModel();
        orderTable = new JTable(tableModel);
        orderTable.setFont(new Font("Arial", Font.PLAIN, 12));
        orderTable.setRowHeight(25);
        
        // Set column widths
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Item name
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(50);  // Qty
        orderTable.getColumnModel().getColumn(1).setMaxWidth(70);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Price
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Subtotal
        
        JScrollPane tableScroll = new JScrollPane(orderTable);
        rightPanel.add(tableScroll, BorderLayout.CENTER);
        
        // Bottom panel with totals and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        
        // Totals panel
        JPanel totalsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        totalsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        discountPercentLabel = new JLabel(langManager.getText("discount_percent"));
        totalsPanel.add(discountPercentLabel);
        JPanel discountInputPanel = new JPanel(new BorderLayout(5, 0));
        discountField = new JTextField("0");
        applyDiscountButton = new JButton(langManager.getText("apply"));
        discountInputPanel.add(discountField, BorderLayout.CENTER);
        discountInputPanel.add(applyDiscountButton, BorderLayout.EAST);
        totalsPanel.add(discountInputPanel);
        
        subtotalTextLabel = new JLabel(langManager.getText("subtotal"));
        totalsPanel.add(subtotalTextLabel);
        subtotalLabel = new JLabel("$0.00");
        subtotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalsPanel.add(subtotalLabel);
        
        discountTextLabel = new JLabel(langManager.getText("discount"));
        totalsPanel.add(discountTextLabel);
        discountLabel = new JLabel("$0.00");
        discountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalsPanel.add(discountLabel);
        
        totalTextLabel = new JLabel(langManager.getText("total"));
        totalsPanel.add(totalTextLabel);
        totalLabel = new JLabel("$0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(0, 128, 0));
        totalsPanel.add(totalLabel);
        
        bottomPanel.add(totalsPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        clearButton = new JButton(langManager.getText("clear_order"));
        clearButton.setBackground(new Color(220, 53, 69));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        payButton = new JButton(langManager.getText("proceed_payment"));
        payButton.setBackground(new Color(40, 167, 69));
        payButton.setForeground(Color.BLACK);
        payButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        buttonsPanel.add(clearButton);
        buttonsPanel.add(payButton);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Add panels to main view
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
    
    public void displayMenuItems(List<MenuItem> menuItems) {
        menuPanel.removeAll();
        
        for (MenuItem item : menuItems) {
            JPanel card = createMenuCard(item);
            menuPanel.add(card);
        }
        
        menuPanel.revalidate();
        menuPanel.repaint();
    }
    
    private JPanel createMenuCard(MenuItem item) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Check if item is sold out
        boolean isSoldOut = !item.isAvailable();
        
        // Gray out if sold out
        if (isSoldOut) {
            card.setBackground(new Color(240, 240, 240));
        } else {
            card.setBackground(Color.WHITE);
        }
        
        // Item details
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 0, 2));
        detailsPanel.setBackground(card.getBackground());
        
        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        if (isSoldOut) {
            nameLabel.setForeground(Color.GRAY);
        }
        
        JLabel categoryLabel = new JLabel(langManager.translateCategory(item.getCategory()));
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        categoryLabel.setForeground(Color.GRAY);
        
        JLabel priceLabel = new JLabel(langManager.formatPrice(item.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 13));
        if (isSoldOut) {
            priceLabel.setForeground(Color.GRAY);
        } else {
            priceLabel.setForeground(new Color(0, 128, 0));
        }
        
        detailsPanel.add(nameLabel);
        detailsPanel.add(categoryLabel);
        detailsPanel.add(priceLabel);
        
        // Add sold out label if item is not available
        if (isSoldOut) {
            JLabel soldOutLabel = new JLabel(langManager.getText("sold_out"));
            soldOutLabel.setFont(new Font("Arial", Font.BOLD, 12));
            soldOutLabel.setForeground(new Color(220, 53, 69));
            soldOutLabel.setHorizontalAlignment(SwingConstants.CENTER);
            detailsPanel.add(soldOutLabel);
        }
        
        // BUY button panel
        JPanel buyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buyPanel.setBackground(card.getBackground());
        
        JButton buyButton = new JButton(langManager.getText("buy"));
        buyButton.setFont(new Font("Arial", Font.BOLD, 16));
        buyButton.setPreferredSize(new Dimension(160, 45));
        buyButton.setBackground(Color.WHITE);
        buyButton.setForeground(Color.BLACK);
        buyButton.setFocusPainted(false);
        buyButton.setOpaque(true);
        buyButton.setBorderPainted(true);
        buyButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(8, 28, 8, 28)
        ));
        buyButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buyButton.putClientProperty("menuItem", item);
        buyButton.putClientProperty("action", "buy");
        buyButton.setEnabled(!isSoldOut); // Disable if sold out
        
        if (isSoldOut) {
            buyButton.setBackground(new Color(240, 240, 240));
            buyButton.setForeground(new Color(150, 150, 150));
            buyButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 2),
                BorderFactory.createEmptyBorder(8, 28, 8, 28)
            ));
        }
        
        buyPanel.add(buyButton);
        
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(buyPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void updateCategoryFilter() {
        String[] categories = {
            langManager.getText("all"),
            langManager.translateCategory("Coffee"),
            langManager.translateCategory("Beverage"),
            langManager.translateCategory("Dessert")
        };
        
        if (categoryFilter == null) {
            categoryFilter = new JComboBox<>(categories);
        } else {
            int selectedIndex = categoryFilter.getSelectedIndex();
            categoryFilter.removeAllItems();
            for (String cat : categories) {
                categoryFilter.addItem(cat);
            }
            if (selectedIndex >= 0 && selectedIndex < categories.length) {
                categoryFilter.setSelectedIndex(selectedIndex);
            }
        }
    }
    
    private void updateTableModel() {
        String[] columns = {
            langManager.getText("item"),
            langManager.getText("qty"),
            langManager.getText("price"),
            langManager.getText("subtotal")
        };
        
        if (tableModel == null) {
            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        } else {
            tableModel.setColumnIdentifiers(columns);
        }
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> {
            refreshLanguage();
        });
    }
    
    public void refreshLanguage() {
        leftPanelBorder.setTitle(langManager.getText("menu_items"));
        rightPanelBorder.setTitle(langManager.getText("current_order"));
        categoryLabel.setText(langManager.getText("category"));
        discountPercentLabel.setText(langManager.getText("discount_percent"));
        subtotalTextLabel.setText(langManager.getText("subtotal"));
        discountTextLabel.setText(langManager.getText("discount"));
        totalTextLabel.setText(langManager.getText("total"));
        applyDiscountButton.setText(langManager.getText("apply"));
        clearButton.setText(langManager.getText("clear_order"));
        payButton.setText(langManager.getText("proceed_payment"));
        
        updateCategoryFilter();
        updateTableModel();
        
        repaint();
        revalidate();
    }
    
    public void updateOrderTable(List<OrderItem> items) {
        tableModel.setRowCount(0);
        
        for (OrderItem item : items) {
            Object[] row = {
                item.getMenuItem().getName(),
                item.getQuantity(),
                langManager.formatPrice(item.getMenuItem().getPrice()),
                langManager.formatPrice(item.getSubtotal())
            };
            tableModel.addRow(row);
        }
    }
    
    public void updateTotals(double subtotal, double discount, double total) {
        subtotalLabel.setText(langManager.formatPrice(subtotal));
        discountLabel.setText(langManager.formatPrice(discount));
        totalLabel.setText(langManager.formatPrice(total));
    }
    
    // No longer needed - removed updateMenuCardQuantities since we use BUY button now
    
    // Getters for UI components
    public JPanel getMenuPanel() { return menuPanel; }
    public JTable getOrderTable() { return orderTable; }
    public JTextField getDiscountField() { return discountField; }
    public JButton getApplyDiscountButton() { return applyDiscountButton; }
    public JButton getPayButton() { return payButton; }
    public JButton getClearButton() { return clearButton; }
    public JComboBox<String> getCategoryFilter() { return categoryFilter; }
}

