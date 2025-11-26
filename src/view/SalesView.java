package view;

import util.LanguageManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * View for displaying sales statistics and managing orders
 */
public class SalesView extends JPanel {
    private JLabel monthlyRevenueLabel;
    private JLabel todaySalesLabel;
    private JLabel todayOrdersLabel;
    
    private JTabbedPane leftTabbedPane;
    private JTable salesTable;
    private DefaultTableModel tableModel;
    private JTable cancelledTable;
    private DefaultTableModel cancelledTableModel;
    
    private JTable searchTable;
    private DefaultTableModel searchTableModel;
    
    private LanguageManager langManager;
    private TitledBorder searchPanelBorder;
    private JLabel monthlyRevenueTextLabel;
    private JLabel todaySalesTextLabel;
    private JLabel todayOrdersTextLabel;
    
    private JComboBox<Integer> yearCombo;
    private JComboBox<Integer> monthCombo;
    private JComboBox<Integer> dayCombo;
    private JTextField amountField;
    private JButton searchButton;
    
    private JButton printReceiptButton;
    private JButton cancelOrderButton;
    private JButton exportCSVButton;

    private boolean isUpdatingSelection = false;
    
    private JLabel yearLabel;
    private JLabel monthLabel;
    private JLabel dayLabel;
    private JLabel priceLabel;
    
    public SalesView() {
        langManager = LanguageManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
        setupLanguageListener();
        
        // [추가] 초기 실행 시 폰트 적용
        updateFonts();
    }
    
    private void initializeComponents() {
        // Top panel - Statistics cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        
        JPanel revenueCard = createStatCard(langManager.getText("monthly_revenue"), "$0.00", new Color(40, 167, 69));
        monthlyRevenueTextLabel = (JLabel) revenueCard.getComponent(0);
        monthlyRevenueLabel = (JLabel) revenueCard.getComponent(1);
        statsPanel.add(revenueCard);
        
        JPanel todaySalesCard = createStatCard(langManager.getText("today_sales"), "$0.00", new Color(0, 123, 255));
        todaySalesTextLabel = (JLabel) todaySalesCard.getComponent(0);
        todaySalesLabel = (JLabel) todaySalesCard.getComponent(1);
        statsPanel.add(todaySalesCard);
        
        JPanel todayOrdersCard = createStatCard(langManager.getText("today_orders"), "0", new Color(255, 193, 7));
        todayOrdersTextLabel = (JLabel) todayOrdersCard.getComponent(0);
        todayOrdersLabel = (JLabel) todayOrdersCard.getComponent(1);
        statsPanel.add(todayOrdersCard);
        
        // Center panel
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Left: Tabbed Pane
        leftTabbedPane = new JTabbedPane();
        
        updateSalesTableModel();
        salesTable = new JTable(tableModel);
        salesTable.setRowHeight(25);
        JScrollPane salesScroll = new JScrollPane(salesTable);
        leftTabbedPane.addTab(langManager.getText("recent_transactions"), salesScroll);
        
        updateCancelledTableModel();
        cancelledTable = new JTable(cancelledTableModel);
        cancelledTable.setRowHeight(25);
        cancelledTable.setForeground(Color.RED);
        JScrollPane cancelledScroll = new JScrollPane(cancelledTable);
        leftTabbedPane.addTab(langManager.getText("cancelled_history"), cancelledScroll);
        
        centerPanel.add(leftTabbedPane);
        
        // Right: Order Search
        JPanel searchPanel = new JPanel(new BorderLayout(0, 10));
        searchPanelBorder = BorderFactory.createTitledBorder(langManager.getText("title_order_search"));
        searchPanel.setBorder(searchPanelBorder);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        LocalDate now = LocalDate.now();
        yearCombo = new JComboBox<>();
        for(int i = now.getYear(); i >= 2020; i--) yearCombo.addItem(i);
        
        monthCombo = new JComboBox<>();
        for(int i = 1; i <= 12; i++) monthCombo.addItem(i);
        monthCombo.setSelectedItem(now.getMonthValue());
        
        dayCombo = new JComboBox<>();
        for(int i = 1; i <= 31; i++) dayCombo.addItem(i);
        dayCombo.setSelectedItem(now.getDayOfMonth());
        
        amountField = new JTextField(6);
        searchButton = new JButton(langManager.getText("btn_search"));
        
        yearLabel = new JLabel(langManager.getText("label_year"));
        monthLabel = new JLabel(langManager.getText("label_month"));
        dayLabel = new JLabel(langManager.getText("label_day"));
        priceLabel = new JLabel(langManager.getText("label_price"));
        
        filterPanel.add(yearLabel); filterPanel.add(yearCombo);
        filterPanel.add(monthLabel); filterPanel.add(monthCombo);
        filterPanel.add(dayLabel); filterPanel.add(dayCombo);
        filterPanel.add(priceLabel); filterPanel.add(amountField);
        filterPanel.add(searchButton);
        
        searchPanel.add(filterPanel, BorderLayout.NORTH);
        
        updateSearchTableModel();
        searchTable = new JTable(searchTableModel);
        searchTable.setRowHeight(25);
        JScrollPane searchScroll = new JScrollPane(searchTable);
        searchPanel.add(searchScroll, BorderLayout.CENTER);
        
        centerPanel.add(searchPanel);
        
        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        printReceiptButton = new JButton(langManager.getText("btn_print_receipt"));
        printReceiptButton.setEnabled(false);
        
        cancelOrderButton = new JButton(langManager.getText("btn_cancel_order"));
        cancelOrderButton.setFocusPainted(false);
        cancelOrderButton.setBorderPainted(true);
        cancelOrderButton.setOpaque(true);
        cancelOrderButton.setContentAreaFilled(true);
        cancelOrderButton.setEnabled(false);
        cancelOrderButton.setBackground(Color.LIGHT_GRAY);
        cancelOrderButton.setForeground(Color.DARK_GRAY);
        
        exportCSVButton = new JButton(langManager.getText("btn_export_csv"));
        exportCSVButton.setBackground(new Color(40, 167, 69));
        exportCSVButton.setForeground(Color.BLACK);
        
        bottomPanel.add(printReceiptButton);
        bottomPanel.add(cancelOrderButton);
        bottomPanel.add(exportCSVButton);
        
        add(statsPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setupSelectionListeners();
    }
    
    // [추가] 현재 언어에 따라 폰트를 일괄 변경하는 메서드
    private void updateFonts() {
        // 언어에 따른 폰트 패밀리 선택
        String fontName;
        if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
            fontName = "Malgun Gothic"; // 한글: 맑은 고딕
        } else {
            fontName = "Arial";         // 영어: Arial
        }

        // 폰트 스타일 정의
        Font plainFont = new Font(fontName, Font.PLAIN, 12);
        Font boldFont = new Font(fontName, Font.BOLD, 12);
        Font titleFont = new Font(fontName, Font.BOLD, 14);
        Font valueFont = new Font(fontName, Font.BOLD, 24);
        Font buttonFont = new Font(fontName, Font.BOLD, 13);
        Font headerFont = new Font(fontName, Font.BOLD, 12); // TitledBorder용

        // 1. 상단 통계 카드
        monthlyRevenueTextLabel.setFont(titleFont);
        monthlyRevenueLabel.setFont(valueFont);
        todaySalesTextLabel.setFont(titleFont);
        todaySalesLabel.setFont(valueFont);
        todayOrdersTextLabel.setFont(titleFont);
        todayOrdersLabel.setFont(valueFont);

        // 2. 탭 및 테이블
        leftTabbedPane.setFont(plainFont);
        salesTable.setFont(plainFont);
        cancelledTable.setFont(plainFont);
        searchTable.setFont(plainFont);

        // 3. 검색 패널
        searchPanelBorder.setTitleFont(headerFont);
        yearLabel.setFont(plainFont);
        monthLabel.setFont(plainFont);
        dayLabel.setFont(plainFont);
        priceLabel.setFont(plainFont);
        yearCombo.setFont(plainFont);
        monthCombo.setFont(plainFont);
        dayCombo.setFont(plainFont);
        amountField.setFont(plainFont);
        searchButton.setFont(boldFont);

        // 4. 하단 버튼
        printReceiptButton.setFont(buttonFont);
        cancelOrderButton.setFont(buttonFont);
        exportCSVButton.setFont(buttonFont);

        this.repaint();
    }

    private void setupSelectionListeners() {
        salesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) checkSelectionsAndSetButtons(salesTable);
        });

        searchTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) checkSelectionsAndSetButtons(searchTable);
        });
        
        cancelledTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) checkSelectionsAndSetButtons(cancelledTable);
        });
    }

    private void checkSelectionsAndSetButtons(JTable sourceTable) {
        if (isUpdatingSelection) return;

        if (sourceTable.getSelectedRow() != -1) {
            isUpdatingSelection = true;

            if (sourceTable != salesTable) salesTable.clearSelection();
            if (sourceTable != searchTable) searchTable.clearSelection();
            if (sourceTable != cancelledTable) cancelledTable.clearSelection();

            isUpdatingSelection = false;

            printReceiptButton.setEnabled(true);
            
            if (sourceTable == cancelledTable) {
                cancelOrderButton.setEnabled(false);
                styleCancelButton(false);
            } else {
                cancelOrderButton.setEnabled(true);
                styleCancelButton(true);
            }
        } else {
            if (salesTable.getSelectedRow() == -1 && 
                searchTable.getSelectedRow() == -1 && 
                cancelledTable.getSelectedRow() == -1) {
                
                printReceiptButton.setEnabled(false);
                cancelOrderButton.setEnabled(false);
                styleCancelButton(false);
            }
        }
    }

    private void styleCancelButton(boolean active) {
        if (active) {
            cancelOrderButton.setBackground(new Color(255, 100, 100));
            cancelOrderButton.setForeground(Color.BLACK);
        } else {
            cancelOrderButton.setBackground(Color.LIGHT_GRAY);
            cancelOrderButton.setForeground(Color.DARK_GRAY);
        }
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        
        JLabel titleLabel = new JLabel(title);
        // 초기 폰트 설정 (이후 updateFonts로 덮어씌워짐)
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.DARK_GRAY);
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    public void updateStatistics(double monthlyRevenue, double todaySales, int todayOrders) {
        monthlyRevenueLabel.setText(langManager.formatPrice(monthlyRevenue));
        todaySalesLabel.setText(langManager.formatPrice(todaySales));
        todayOrdersLabel.setText(String.valueOf(todayOrders));
        this.repaint();
    }
    
    public void updateSalesTable(java.util.List<String[]> salesData) {
        tableModel.setRowCount(0);
        for (String[] row : salesData) {
            tableModel.addRow(row);
        }
    }
    
    public void updateCancelledTable(java.util.List<String[]> cancelledData) {
        cancelledTableModel.setRowCount(0);
        for (String[] row : cancelledData) {
            cancelledTableModel.addRow(row);
        }
    }
    
    public void updateSearchTable(java.util.List<String[]> searchData) {
        searchTableModel.setRowCount(0);
        for (String[] row : searchData) {
            searchTableModel.addRow(row);
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
                public boolean isCellEditable(int row, int column) { return false; }
            };
        } else {
            tableModel.setColumnIdentifiers(salesColumns);
        }
    }
    
    private void updateCancelledTableModel() {
        String[] columns = { 
            langManager.getText("time"), 
            langManager.getText("order_id"), 
            langManager.getText("amount"), 
            langManager.getText("payment") 
        };
        if (cancelledTableModel == null) {
            cancelledTableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
        } else {
            cancelledTableModel.setColumnIdentifiers(columns);
        }
    }
    
    private void updateSearchTableModel() {
        String[] searchColumns = { 
            langManager.getText("time"), 
            langManager.getText("order_id"), 
            langManager.getText("amount"), 
            langManager.getText("payment") 
        };
        if (searchTableModel == null) {
            searchTableModel = new DefaultTableModel(searchColumns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
        } else {
            searchTableModel.setColumnIdentifiers(searchColumns);
        }
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> refreshLanguage());
    }
    
    public void refreshLanguage() {
        monthlyRevenueTextLabel.setText(langManager.getText("monthly_revenue"));
        todaySalesTextLabel.setText(langManager.getText("today_sales"));
        todayOrdersTextLabel.setText(langManager.getText("today_orders"));
        
        leftTabbedPane.setTitleAt(0, langManager.getText("recent_transactions"));
        leftTabbedPane.setTitleAt(1, langManager.getText("cancelled_history"));
        
        searchPanelBorder.setTitle(langManager.getText("title_order_search"));
        yearLabel.setText(langManager.getText("label_year"));
        monthLabel.setText(langManager.getText("label_month"));
        dayLabel.setText(langManager.getText("label_day"));
        priceLabel.setText(langManager.getText("label_price"));
        searchButton.setText(langManager.getText("btn_search"));
        
        printReceiptButton.setText(langManager.getText("btn_print_receipt"));
        cancelOrderButton.setText(langManager.getText("btn_cancel_order"));
        exportCSVButton.setText(langManager.getText("btn_export_csv"));
        
        updateSalesTableModel();
        updateSearchTableModel();
        updateCancelledTableModel();
        
        // [추가] 언어 변경 시 폰트도 함께 업데이트
        updateFonts();
        
        this.repaint();
        this.revalidate();
    }
    
    public JButton getExportCSVButton() { return exportCSVButton; }
    public JButton getSearchButton() { return searchButton; }
    public JButton getPrintReceiptButton() { return printReceiptButton; }
    public JButton getCancelOrderButton() { return cancelOrderButton; }
    
    public int getSelectedYear() { return (Integer) yearCombo.getSelectedItem(); }
    public int getSelectedMonth() { return (Integer) monthCombo.getSelectedItem(); }
    public int getSelectedDay() { return (Integer) dayCombo.getSelectedItem(); }
    public String getSearchAmount() { return amountField.getText(); }
    
    public String getSelectedOrderId() {
        JTable targetTable = null;
        if (salesTable.getSelectedRow() != -1) targetTable = salesTable;
        else if (searchTable.getSelectedRow() != -1) targetTable = searchTable;
        else if (cancelledTable.getSelectedRow() != -1) targetTable = cancelledTable;
        
        if (targetTable != null) {
            int row = targetTable.getSelectedRow();
            return (String) targetTable.getValueAt(row, 1);
        }
        return null;
    }
}