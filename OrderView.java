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
 * - 메뉴 카드 3열 -> 2열 변경
 * - 메뉴카드 이미지 크기: 180x130
 * - 메뉴카드 전체 크기: 대략 1.5배 (preferredSize 사용)
 * (기존 로직과 호환되도록 나머지 동작 유지)
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

    // constants for image and card sizes (as requested)
    private static final int IMG_WIDTH = 180;
    private static final int IMG_HEIGHT = 130;
    private static final int CARD_WIDTH = (int) (240 * 1.5); // base approx 240 -> 1.5x => 360
    private static final int CARD_HEIGHT = (int) (180 * 1.5); // base approx 180 -> 1.5x => 270

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
        // 변경: 2열로 표시, 간격 약간 증가
        menuPanel.setLayout(new GridLayout(0, 2, 18, 18));
        JScrollPane menuScroll = new JScrollPane(menuPanel);
        menuScroll.getVerticalScrollBar().setUnitIncrement(16);
        leftPanel.add(menuScroll, BorderLayout.CENTER);

        // Right side - Order details
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanelBorder = BorderFactory.createTitledBorder(langManager.getText("current_order"));
        rightPanel.setBorder(rightPanelBorder);
        rightPanel.setPreferredSize(new Dimension(440, 0)); // 약간 넓힘

        // Order table
        updateTableModel();
        orderTable = new JTable(tableModel);
        orderTable.setFont(new Font("Arial", Font.PLAIN, 12));
        orderTable.setRowHeight(25);

        // Set column widths (defensive: check column count)
        if (orderTable.getColumnModel().getColumnCount() >= 4) {
            orderTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            orderTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            orderTable.getColumnModel().getColumn(1).setMaxWidth(70);
            orderTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            orderTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        JScrollPane tableScroll = new JScrollPane(orderTable);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        // Bottom panel with totals and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

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
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // Set preferred size to make card visually larger (approx 1.5x)
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

        boolean isSoldOut = !item.isAvailable();

        card.setBackground(isSoldOut ? new Color(240, 240, 240) : Color.WHITE);

        // Top: image (if exists) - scaled to 180x130
        if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(item.getImagePath());
                Image scaled = icon.getImage().getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(scaled));
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imgLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
                imgLabel.setBackground(card.getBackground());
                card.add(imgLabel, BorderLayout.NORTH);
            } catch (Exception ex) {
                // 이미지 로드 실패 시 무시하고 진행
            }
        } else {
            // optional: add an empty placeholder to maintain size consistency
            JLabel placeholder = new JLabel();
            placeholder.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
            placeholder.setOpaque(false);
            card.add(placeholder, BorderLayout.NORTH);
        }

        // Item details
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 0, 4));
        detailsPanel.setBackground(card.getBackground());

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 15)); // slightly larger for bigger card
        if (isSoldOut) {
            nameLabel.setForeground(Color.GRAY);
        }

        JLabel categoryLabel = new JLabel(langManager.translateCategory(item.getCategory()));
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        categoryLabel.setForeground(Color.GRAY);

        JLabel priceLabel = new JLabel(langManager.formatPrice(item.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(isSoldOut ? Color.GRAY : new Color(0, 128, 0));

        detailsPanel.add(nameLabel);
        detailsPanel.add(categoryLabel);
        detailsPanel.add(priceLabel);

        // Sold-out label (if applicable)
        if (isSoldOut) {
            JLabel soldOutLabel = new JLabel(langManager.getText("sold_out"));
            soldOutLabel.setFont(new Font("Arial", Font.BOLD, 13));
            soldOutLabel.setForeground(new Color(220, 53, 69));
            soldOutLabel.setHorizontalAlignment(SwingConstants.CENTER);
            detailsPanel.add(soldOutLabel);
        }

        // Quantity control panel with +/- buttons
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        quantityPanel.setBackground(card.getBackground());

        JButton minusButton = new JButton("-");
        minusButton.setFont(new Font("Arial", Font.BOLD, 18));
        minusButton.setPreferredSize(new Dimension(48, 38));
        minusButton.setBackground(new Color(220, 53, 69));
        minusButton.setForeground(Color.BLACK);
        minusButton.setFocusPainted(false);
        minusButton.putClientProperty("menuItem", item);
        minusButton.putClientProperty("action", "decrease");
        minusButton.setEnabled(!isSoldOut);

        JLabel quantityLabel = new JLabel("0");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        quantityLabel.setPreferredSize(new Dimension(46, 36));
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quantityLabel.putClientProperty("itemId", item.getId());
        if (isSoldOut) {
            quantityLabel.setForeground(Color.GRAY);
        }

        JButton plusButton = new JButton("+");
        plusButton.setFont(new Font("Arial", Font.BOLD, 18));
        plusButton.setPreferredSize(new Dimension(48, 38));
        plusButton.setBackground(new Color(40, 167, 69));
        plusButton.setForeground(Color.BLACK);
        plusButton.setFocusPainted(false);
        plusButton.putClientProperty("menuItem", item);
        plusButton.putClientProperty("action", "increase");
        plusButton.setEnabled(!isSoldOut);

        quantityPanel.add(minusButton);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(plusButton);

        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(quantityPanel, BorderLayout.SOUTH);

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

    // Update menu card quantities based on current order
    public void updateMenuCardQuantities(List<OrderItem> orderItems) {
        Component[] menuCards = menuPanel.getComponents();

        for (Component card : menuCards) {
            if (card instanceof JPanel) {
                Component[] cardComponents = ((JPanel) card).getComponents();
                for (Component comp : cardComponents) {
                    if (comp instanceof JPanel) {
                        Component[] panelComponents = ((JPanel) comp).getComponents();
                        for (Component innerComp : panelComponents) {
                            if (innerComp instanceof JLabel) {
                                JLabel label = (JLabel) innerComp;
                                Object itemIdObj = label.getClientProperty("itemId");
                                if (itemIdObj instanceof String) {
                                    String itemId = (String) itemIdObj;
                                    int quantity = 0;
                                    for (OrderItem orderItem : orderItems) {
                                        if (orderItem.getMenuItem().getId().equals(itemId)) {
                                            quantity = orderItem.getQuantity();
                                            break;
                                        }
                                    }
                                    label.setText(String.valueOf(quantity));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Getters for UI components
    public JPanel getMenuPanel() { return menuPanel; }
    public JTable getOrderTable() { return orderTable; }
    public JTextField getDiscountField() { return discountField; }
    public JButton getApplyDiscountButton() { return applyDiscountButton; }
    public JButton getPayButton() { return payButton; }
    public JButton getClearButton() { return clearButton; }
    public JComboBox<String> getCategoryFilter() { return categoryFilter; }
}
