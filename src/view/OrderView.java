package view;

import model.MenuItem;
import model.OrderItem;
import util.LanguageManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class OrderView extends JPanel {
    private JPanel menuPanel;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JLabel subtotalLabel, discountLabel, totalLabel;
    private JTextField discountField;
    private JButton applyDiscountButton, payButton, clearButton;
    private JComboBox<String> categoryFilter;
    private LanguageManager langManager;

    private TitledBorder leftPanelBorder, rightPanelBorder;
    private JLabel categoryLabel, discountPercentLabel, subtotalTextLabel, discountTextLabel, totalTextLabel;

    private static final int IMG_WIDTH = 180;
    private static final int IMG_HEIGHT = 130;
    private static final int CARD_WIDTH = (int) (240 * 1.5);
    private static final int CARD_HEIGHT = 320; // 높이 확보

    public OrderView() {
        langManager = LanguageManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initializeComponents();
        setupLanguageListener();
        updateFonts(); // 초기화 시 폰트 적용
    }

    private void initializeComponents() {
        // Left Side
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanelBorder = BorderFactory.createTitledBorder(langManager.getText("menu_items"));
        leftPanel.setBorder(leftPanelBorder);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryLabel = new JLabel(langManager.getText("category"));
        filterPanel.add(categoryLabel);
        updateCategoryFilter();
        filterPanel.add(categoryFilter);
        leftPanel.add(filterPanel, BorderLayout.NORTH);

        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 2, 20, 20));
        JScrollPane menuScroll = new JScrollPane(menuPanel);
        menuScroll.getVerticalScrollBar().setUnitIncrement(16);
        leftPanel.add(menuScroll, BorderLayout.CENTER);

        // Right Side
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanelBorder = BorderFactory.createTitledBorder(langManager.getText("current_order"));
        rightPanel.setBorder(rightPanelBorder);
        rightPanel.setPreferredSize(new Dimension(440, 0));

        updateTableModel();
        orderTable = new JTable(tableModel);
        orderTable.setRowHeight(25);
        if (orderTable.getColumnModel().getColumnCount() >= 4) {
            orderTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            orderTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            orderTable.getColumnModel().getColumn(1).setMaxWidth(70);
            orderTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            orderTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        }
        
        JScrollPane tableScroll = new JScrollPane(orderTable);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        // Bottom Panel (Totals with GridBagLayout)
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        JPanel totalsPanel = new JPanel(new GridBagLayout());
        totalsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        discountPercentLabel = new JLabel(langManager.getText("discount_percent"));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0; totalsPanel.add(discountPercentLabel, gbc);

        JPanel discountInputPanel = new JPanel(new BorderLayout(5, 0));
        discountField = new JTextField("0");
        applyDiscountButton = new JButton(langManager.getText("apply"));
        discountInputPanel.add(discountField, BorderLayout.CENTER);
        discountInputPanel.add(applyDiscountButton, BorderLayout.EAST);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; totalsPanel.add(discountInputPanel, gbc);

        subtotalTextLabel = new JLabel(langManager.getText("subtotal"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; totalsPanel.add(subtotalTextLabel, gbc);
        subtotalLabel = new JLabel("$0.00", SwingConstants.RIGHT);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; totalsPanel.add(subtotalLabel, gbc);

        discountTextLabel = new JLabel(langManager.getText("discount"));
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; totalsPanel.add(discountTextLabel, gbc);
        discountLabel = new JLabel("$0.00", SwingConstants.RIGHT);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; totalsPanel.add(discountLabel, gbc);

        totalTextLabel = new JLabel(langManager.getText("total"));
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0; totalsPanel.add(totalTextLabel, gbc);
        totalLabel = new JLabel("$0.00", SwingConstants.RIGHT);
        totalLabel.setForeground(new Color(0, 128, 0));
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0; totalsPanel.add(totalLabel, gbc);

        bottomPanel.add(totalsPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        clearButton = new JButton(langManager.getText("clear_order"));
        clearButton.setBackground(new Color(220, 53, 69)); clearButton.setForeground(Color.BLACK);
        payButton = new JButton(langManager.getText("proceed_payment"));
        payButton.setBackground(new Color(40, 167, 69)); payButton.setForeground(Color.BLACK);
        
        buttonsPanel.add(clearButton); buttonsPanel.add(payButton);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    // [폰트 수정]
    private void updateFonts() {
        String fontName = (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) ? "Malgun Gothic" : "Arial";
        Font plainFont = new Font(fontName, Font.PLAIN, 12);
        Font valueFont = new Font(fontName, Font.BOLD, 14);
        Font totalFont = new Font(fontName, Font.BOLD, 16);
        Font btnFont = new Font(fontName, Font.BOLD, 14);

        leftPanelBorder.setTitleFont(plainFont); rightPanelBorder.setTitleFont(plainFont);
        categoryLabel.setFont(plainFont); categoryFilter.setFont(plainFont); orderTable.setFont(plainFont);
        
        discountPercentLabel.setFont(plainFont); discountField.setFont(plainFont); applyDiscountButton.setFont(plainFont);
        subtotalTextLabel.setFont(plainFont); subtotalLabel.setFont(valueFont);
        discountTextLabel.setFont(plainFont); discountLabel.setFont(valueFont);
        totalTextLabel.setFont(plainFont); totalLabel.setFont(totalFont);
        
        clearButton.setFont(btnFont); payButton.setFont(btnFont);
        
        // 동적으로 생성된 카드들도 업데이트 필요
        updateMenuCardsFont(fontName);
        repaint();
    }
    
    // 메뉴 카드 내부 폰트 업데이트 헬퍼
    private void updateMenuCardsFont(String fontName) {
        Component[] cards = menuPanel.getComponents();
        for (Component card : cards) {
            if (card instanceof JPanel) {
                updateComponentTreeFont((JPanel)card, fontName);
            }
        }
    }
    
    private void updateComponentTreeFont(Container container, String fontName) {
        for (Component c : container.getComponents()) {
            if (c instanceof JLabel) {
                Font f = c.getFont();
                c.setFont(new Font(fontName, f.getStyle(), f.getSize()));
            } else if (c instanceof JButton) {
                Font f = c.getFont();
                c.setFont(new Font(fontName, f.getStyle(), f.getSize()));
            } else if (c instanceof Container) {
                updateComponentTreeFont((Container)c, fontName);
            }
        }
    }

    public void displayMenuItems(List<MenuItem> menuItems) {
        menuPanel.removeAll();
        for (MenuItem item : menuItems) {
            menuPanel.add(createMenuCard(item));
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private JPanel createMenuCard(MenuItem item) {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

        boolean isSoldOut = !item.isAvailable();
        card.setBackground(isSoldOut ? new Color(240, 240, 240) : Color.WHITE);
        String fontName = (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) ? "Malgun Gothic" : "Arial";

        // Image
        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imgLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        String path = item.getImagePath();
        boolean imgLoaded = false;
        
        if (path != null && !path.trim().isEmpty()) {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                try {
                    BufferedImage img = ImageIO.read(imgFile);
                    if (img != null) {
                        Image scaled = img.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH);
                        imgLabel.setIcon(new ImageIcon(scaled));
                        imgLoaded = true;
                    }
                } catch (Exception e) {}
            }
        }
        
        if (!imgLoaded) {
            imgLabel.setText("No Image");
            imgLabel.setFont(new Font(fontName, Font.PLAIN, 12));
            imgLabel.setForeground(Color.LIGHT_GRAY);
            imgLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
        card.add(imgLabel, BorderLayout.NORTH);

        // Text Info (GridBagLayout)
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(card.getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 0, 2, 0);

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font(fontName, Font.BOLD, 15));
        if (isSoldOut) nameLabel.setForeground(Color.GRAY);
        detailsPanel.add(nameLabel, gbc);

        JLabel categoryLabel = new JLabel(langManager.translateCategory(item.getCategory()));
        categoryLabel.setFont(new Font(fontName, Font.PLAIN, 12));
        categoryLabel.setForeground(Color.GRAY);
        detailsPanel.add(categoryLabel, gbc);

        JLabel priceLabel = new JLabel(langManager.formatPrice(item.getPrice()));
        priceLabel.setFont(new Font(fontName, Font.BOLD, 14));
        priceLabel.setForeground(isSoldOut ? Color.GRAY : new Color(0, 128, 0));
        detailsPanel.add(priceLabel, gbc);

        if (isSoldOut) {
            JLabel soldOutLabel = new JLabel(langManager.getText("sold_out"));
            soldOutLabel.setFont(new Font(fontName, Font.BOLD, 13));
            soldOutLabel.setForeground(new Color(220, 53, 69));
            gbc.insets = new Insets(5, 0, 0, 0);
            detailsPanel.add(soldOutLabel, gbc);
        }
        card.add(detailsPanel, BorderLayout.CENTER);

        // Button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(card.getBackground());
        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        JButton buyButton = new JButton(langManager.getText("buy"));
        buyButton.setFont(new Font(fontName, Font.BOLD, 16));
        buyButton.setPreferredSize(new Dimension(160, 40));
        buyButton.setBackground(isSoldOut ? new Color(230, 230, 230) : Color.WHITE);
        buyButton.setForeground(isSoldOut ? Color.GRAY : Color.BLACK);
        buyButton.setFocusPainted(false);
        buyButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        if (!isSoldOut) {
            buyButton.putClientProperty("menuItem", item);
            buyButton.putClientProperty("action", "buy");
            buyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            buyButton.setEnabled(false);
        }
        
        btnPanel.add(buyButton);
        card.add(btnPanel, BorderLayout.SOUTH);

        return card;
    }

    private void updateCategoryFilter() {
        String[] categories = {
            langManager.getText("all"), langManager.translateCategory("Coffee"),
            langManager.translateCategory("Beverage"), langManager.translateCategory("Dessert")
        };
        
        if (categoryFilter == null) {
            categoryFilter = new JComboBox<>(categories);
        } else {
            categoryFilter.removeAllItems();
            for (String cat : categories) categoryFilter.addItem(cat);
        }
    }

    private void updateTableModel() {
        String[] columns = {
            langManager.getText("item"), langManager.getText("qty"),
            langManager.getText("price"), langManager.getText("subtotal")
        };
        if (tableModel == null) {
            tableModel = new DefaultTableModel(columns, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
        } else {
            tableModel.setColumnIdentifiers(columns);
        }
    }

    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> { refreshLanguage(); updateFonts(); });
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
                item.getMenuItem().getName(), item.getQuantity(),
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

    public JPanel getMenuPanel() { return menuPanel; }
    public JTable getOrderTable() { return orderTable; }
    public JTextField getDiscountField() { return discountField; }
    public JButton getApplyDiscountButton() { return applyDiscountButton; }
    public JButton getPayButton() { return payButton; }
    public JButton getClearButton() { return clearButton; }
    public JComboBox<String> getCategoryFilter() { return categoryFilter; }
}