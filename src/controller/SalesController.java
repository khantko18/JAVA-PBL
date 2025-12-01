package controller;

import model.Order;
import model.OrderItem;
import model.Payment;
import model.SalesData;
import view.SalesView;
import util.LanguageManager;
import util.CSVExporter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Dimension;
import java.io.File;
import java.time.LocalDate; // [확인] import 필수
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SalesController {
    private SalesData salesData;
    private SalesView view;
    private DateTimeFormatter timeFormatter;
    private LanguageManager langManager;
    
    public SalesController(SalesData salesData, SalesView view) {
        this.salesData = salesData;
        this.view = view;
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.langManager = LanguageManager.getInstance();
        
        refreshStatistics();
        setupActionListeners();
        setupLanguageListener();
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(l -> refreshStatistics());
    }
    
    private void setupActionListeners() {
        view.getExportCSVButton().addActionListener(e -> handleExportSales());
        view.getSearchButton().addActionListener(e -> handleSearch());
        view.getPrintReceiptButton().addActionListener(e -> handlePrintReceipt());
        view.getCancelOrderButton().addActionListener(e -> handleCancelOrder());
    }
    
    private void handleSearch() {
        try {
            int year = view.getSelectedYear();
            int month = view.getSelectedMonth();
            int day = view.getSelectedDay();
            String amtStr = view.getSearchAmount();
            Double amount = null;
            if (!amtStr.trim().isEmpty()) amount = Double.parseDouble(amtStr);
            
            List<Payment> results = salesData.searchOrders(year, month, day, amount);
            updateSearchTable(results);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, langManager.getText("error"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateSearchTable(List<Payment> payments) {
        List<String[]> tableData = new ArrayList<>();
        for (Payment payment : payments) {
            String[] row = {
                payment.getPaymentTime().format(timeFormatter),
                payment.getOrderId(),
                langManager.formatPrice(payment.getAmount()),
                payment.getMethod().toString()
            };
            tableData.add(row);
        }
        view.updateSearchTable(tableData);
    }
    
    private void handleCancelOrder() {
        String orderId = view.getSelectedOrderId();
        if (orderId == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(view, 
            langManager.getText("btn_cancel_order") + "?",
            langManager.getText("confirm_delete_title"), 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = salesData.cancelOrder(orderId);
            if (success) {
                JOptionPane.showMessageDialog(view, 
                    langManager.getText("msg_order_cancelled"), 
                    langManager.getText("title_notice"), 
                    JOptionPane.INFORMATION_MESSAGE);
                refreshStatistics(); 
                handleSearch(); 
            } else {
                JOptionPane.showMessageDialog(view, "Failed to cancel order.", langManager.getText("error"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // [수정] 영수증 출력 디자인 개선 (키 값 문제 해결)
    private void handlePrintReceipt() {
        String orderId = view.getSelectedOrderId();
        if (orderId == null) return;
        
        Order order = salesData.getOrder(orderId);
        Payment payment = salesData.getPayment(orderId);
        
        if (order != null && payment != null) {
            StringBuilder sb = new StringBuilder();
            
            String line = "========================================\n";
            String dash = "----------------------------------------\n";
            
            sb.append(line);
            // 중앙 정렬 느낌의 헤더
            String header = langManager.getText("receipt_header");
            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                sb.append(String.format("             %s             \n", header));
            } else {
                sb.append(String.format("           %s           \n", header));
            }
            sb.append(line);
            
            if ("Cancelled".equals(order.getStatus())) {
                sb.append(String.format("      %s      \n", langManager.getText("receipt_cancelled")));
                sb.append(dash);
            }
            
            // Info Section
            sb.append(String.format("%-12s : %s\n", langManager.getText("receipt_order_id"), order.getOrderId()));
            sb.append(String.format("%-12s : %s\n", langManager.getText("receipt_date"), 
                payment.getPaymentTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            sb.append(dash);
            
            // Column Header
            sb.append(String.format("%-20s %5s %13s\n", 
                langManager.getText("receipt_col_item"), 
                langManager.getText("receipt_col_qty"), 
                langManager.getText("receipt_col_price")));
            sb.append(dash);
            
            // Items
            for (OrderItem item : order.getItems()) {
                String name = item.getMenuItem().getName();
                if (name.length() > 18) name = name.substring(0, 16) + "..";
                
                sb.append(String.format("%-20s %5d %13s\n", 
                    name,
                    item.getQuantity(), 
                    langManager.formatPrice(item.getSubtotal())));
            }
            
            sb.append(dash);
            
            // Totals
            sb.append(String.format("%-20s : %18s\n", langManager.getText("receipt_total_amount"), langManager.formatPrice(order.getTotal())));
            sb.append(String.format("%-20s : %18s\n", langManager.getText("receipt_payment_method"), payment.getMethod()));
            
            if (payment.getMethod() == Payment.PaymentMethod.CASH) {
                sb.append(String.format("%-20s : %18s\n", langManager.getText("receipt_received"), langManager.formatPrice(payment.getReceivedAmount())));
                sb.append(String.format("%-20s : %18s\n", langManager.getText("receipt_change"), langManager.formatPrice(payment.getChangeAmount())));
            }
            
            sb.append(line);
            
            // Footer
            String footer = langManager.getText("receipt_footer");
            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                sb.append(String.format("       %s       \n", footer));
            } else {
                sb.append(String.format("      %s      \n", footer));
            }
            sb.append(line);
            
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setFont(new Font("Malgun Gothic", Font.PLAIN, 12)); 
            textArea.setEditable(false);
            textArea.setMargin(new java.awt.Insets(15, 15, 15, 15));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(380, 500));
            
            JOptionPane.showMessageDialog(view, scrollPane, langManager.getText("btn_print_receipt"), JOptionPane.PLAIN_MESSAGE);
            
        } else {
            JOptionPane.showMessageDialog(view, "Order details not found.", langManager.getText("error"), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleExportSales() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("sales_" + LocalDate.now() + ".csv"));
        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            CSVExporter.exportSalesToCSV(fileChooser.getSelectedFile().getAbsolutePath(), LocalDate.now().minusDays(30), LocalDate.now());
        }
    }
    
    public void refreshStatistics() {
        LocalDate today = LocalDate.now();
        double monthlyRevenue = salesData.getMonthlyRevenue(today.getYear(), today.getMonthValue());
        double todaySales = salesData.getTotalSales(today);
        int todayOrders = salesData.getTotalOrders(today);
        
        view.updateStatistics(monthlyRevenue, todaySales, todayOrders);
        
        List<Payment> todayPayments = salesData.getSalesByDate(today);
        List<String[]> activeRows = new ArrayList<>();
        for (Payment p : todayPayments) {
            activeRows.add(new String[]{
                p.getPaymentTime().format(timeFormatter),
                p.getOrderId(),
                langManager.formatPrice(p.getAmount()),
                p.getMethod().toString()
            });
        }
        view.updateSalesTable(activeRows);
        
        List<Payment> cancelledPayments = salesData.getCancelledPayments();
        List<String[]> cancelledRows = new ArrayList<>();
        cancelledPayments.sort((p1, p2) -> p2.getPaymentTime().compareTo(p1.getPaymentTime()));
        
        for (Payment p : cancelledPayments) {
            cancelledRows.add(new String[]{
                p.getPaymentTime().format(DateTimeFormatter.ofPattern("MM-dd HH:mm")), 
                p.getOrderId(),
                langManager.formatPrice(p.getAmount()),
                p.getMethod().toString()
            });
        }
        view.updateCancelledTable(cancelledRows);
    }
}