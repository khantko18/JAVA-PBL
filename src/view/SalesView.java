package view;

import util.LanguageManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;

/**
 * View for displaying sales statistics
 */
public class SalesView extends JPanel {
    private JLabel totalRevenueLabel;
    private JLabel todaySalesLabel;
    private JLabel todayOrdersLabel;
    private JTable salesTable;
    private DefaultTableModel tableModel;
    private JTable itemStatsTable;
    private DefaultTableModel itemStatsModel;
    private LanguageManager langManager;
    
    private TitledBorder salesPanelBorder;
    private TitledBorder itemStatsPanelBorder;
    private JLabel totalRevenueTextLabel;
    private JLabel todaySalesTextLabel;
    private JLabel todayOrdersTextLabel;
    private JButton exportCSVButton;
    private JButton exportItemsButton;
    
    public SalesView() {
        langManager = LanguageManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
        setupLanguageListener();
    }
    
    private void initializeComponents() {
        // Top panel - Statistics cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        
        // Total Revenue Card
        JPanel revenueCard = createStatCard(langManager.getText("total_revenue"), "$0.00", new Color(40, 167, 69));
        totalRevenueTextLabel = (JLabel) revenueCard.getComponent(0);
        totalRevenueLabel = (JLabel) ((JPanel) revenueCard.getComponent(1)).getComponent(0);
        statsPanel.add(revenueCard);
        
        // Today's Sales Card
        JPanel todaySalesCard = createStatCard(langManager.getText("today_sales"), "$0.00", new Color(0, 123, 255));
        todaySalesTextLabel = (JLabel) todaySalesCard.getComponent(0);
        todaySalesLabel = (JLabel) ((JPanel) todaySalesCard.getComponent(1)).getComponent(0);
        statsPanel.add(todaySalesCard);
        
        // Today's Orders Card
        JPanel todayOrdersCard = createStatCard(langManager.getText("today_orders"), "0", new Color(255, 193, 7));
        todayOrdersTextLabel = (JLabel) todayOrdersCard.getComponent(0);
        todayOrdersLabel = (JLabel) ((JPanel) todayOrdersCard.getComponent(1)).getComponent(0);
        statsPanel.add(todayOrdersCard);
        
        // Center panel - Tables split
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Recent sales table
        JPanel salesPanel = new JPanel(new BorderLayout());
        salesPanelBorder = BorderFactory.createTitledBorder(langManager.getText("recent_transactions"));
        salesPanel.setBorder(salesPanelBorder);
        
        updateSalesTableModel();
        salesTable = new JTable(tableModel);
        salesTable.setFont(new Font("Arial", Font.PLAIN, 12));
        salesTable.setRowHeight(25);
        JScrollPane salesScroll = new JScrollPane(salesTable);
        salesPanel.add(salesScroll, BorderLayout.CENTER);
        
        // Item statistics table
        JPanel itemStatsPanel = new JPanel(new BorderLayout());
        itemStatsPanelBorder = BorderFactory.createTitledBorder(langManager.getText("popular_items"));
        itemStatsPanel.setBorder(itemStatsPanelBorder);
        
        updateItemStatsTableModel();
        itemStatsTable = new JTable(itemStatsModel);
        itemStatsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        itemStatsTable.setRowHeight(25);
        JScrollPane itemStatsScroll = new JScrollPane(itemStatsTable);
        itemStatsPanel.add(itemStatsScroll, BorderLayout.CENTER);
        
        centerPanel.add(salesPanel);
        centerPanel.add(itemStatsPanel);
        
        // Bottom panel - Export buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        exportCSVButton = new JButton("📄 Export Sales to CSV");
        exportCSVButton.setBackground(new Color(40, 167, 69));
        exportCSVButton.setForeground(Color.BLACK);
        exportCSVButton.setFont(new Font("Arial", Font.BOLD, 13));
        exportCSVButton.setPreferredSize(new Dimension(200, 40));
        exportCSVButton.setFocusPainted(false);
        
        exportItemsButton = new JButton("📊 Export Popular Items");
        exportItemsButton.setBackground(new Color(0, 123, 255));
        exportItemsButton.setForeground(Color.BLACK);
        exportItemsButton.setFont(new Font("Arial", Font.BOLD, 13));
        exportItemsButton.setPreferredSize(new Dimension(200, 40));
        exportItemsButton.setFocusPainted(false);
        
        bottomPanel.add(exportCSVButton);
        bottomPanel.add(exportItemsButton);
        
        // Add panels to main view
        add(statsPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.DARK_GRAY);
        
        JPanel valuePanel = new JPanel();
        valuePanel.setOpaque(false);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valuePanel.add(valueLabel);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);
        
        return card;
    }
    
    public void updateStatistics(double totalRevenue, double todaySales, int todayOrders) {
        totalRevenueLabel.setText(langManager.formatPrice(totalRevenue));
        todaySalesLabel.setText(langManager.formatPrice(todaySales));
        todayOrdersLabel.setText(String.valueOf(todayOrders));
    }
    
    public void updateSalesTable(java.util.List<String[]> salesData) {
        tableModel.setRowCount(0);
        for (String[] row : salesData) {
            tableModel.addRow(row);
        }
    }
    
    private void updateSalesTableModel() {
        String[] salesColumns = {
            langManager.getText("time"),
            langManager.getText("order_id"),
            langManager.getText("amount"),
            langManager.getText("payment")
        };
        
        if (tableModel == null) {
            tableModel = new DefaultTableModel(salesColumns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        } else {
            tableModel.setColumnIdentifiers(salesColumns);
        }
    }
    
    private void updateItemStatsTableModel() {
        String[] itemColumns = {
            langManager.getText("item_name"),
            langManager.getText("quantity_sold")
        };
        
        if (itemStatsModel == null) {
            itemStatsModel = new DefaultTableModel(itemColumns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        } else {
            itemStatsModel.setColumnIdentifiers(itemColumns);
        }
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> {
            refreshLanguage();
        });
    }
    
    public void refreshLanguage() {
        totalRevenueTextLabel.setText(langManager.getText("total_revenue"));
        todaySalesTextLabel.setText(langManager.getText("today_sales"));
        todayOrdersTextLabel.setText(langManager.getText("today_orders"));
        salesPanelBorder.setTitle(langManager.getText("recent_transactions"));
        itemStatsPanelBorder.setTitle(langManager.getText("popular_items"));
        
        updateSalesTableModel();
        updateItemStatsTableModel();
        
        repaint();
        revalidate();
    }
    
    public void updateItemStats(Map<String, Integer> itemStats) {
        itemStatsModel.setRowCount(0);
        
        // Sort by quantity descending
        itemStats.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(entry -> {
                Object[] row = {entry.getKey(), entry.getValue()};
                itemStatsModel.addRow(row);
            });
    }
    
    public JButton getExportCSVButton() {
        return exportCSVButton;
    }
    
    public JButton getExportItemsButton() {
        return exportItemsButton;
    }
}

