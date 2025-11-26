package controller;

import model.Order;
import model.OrderItem;
import model.Payment;
import model.SalesData;
import view.SalesView;
import util.LanguageManager;
import util.CSVExporter;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
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
            JOptionPane.showMessageDialog(view, "Invalid search parameters.", "Error", JOptionPane.ERROR_MESSAGE);
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
            "Cancel Order ID: " + orderId + "?\nThis will move the order to history and remove revenue.",
            "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = salesData.cancelOrder(orderId);
            if (success) {
                JOptionPane.showMessageDialog(view, "Order Cancelled.");
                refreshStatistics(); // 통계 및 테이블 갱신
                handleSearch(); // 검색 결과 갱신
            } else {
                JOptionPane.showMessageDialog(view, "Failed to cancel order.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handlePrintReceipt() {
        String orderId = view.getSelectedOrderId();
        if (orderId == null) return;
        
        Order order = salesData.getOrder(orderId);
        Payment payment = salesData.getPayment(orderId);
        
        if (order != null && payment != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("================================\n");
            sb.append("          SALES RECEIPT         \n");
            sb.append("================================\n");
            if ("Cancelled".equals(order.getStatus())) {
                sb.append("       [ ORDER CANCELLED ]      \n");
                sb.append("--------------------------------\n");
            }
            sb.append(String.format("Order ID : %s\n", order.getOrderId()));
            sb.append(String.format("Date     : %s\n", payment.getPaymentTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            sb.append("--------------------------------\n");
            sb.append(String.format("%-18s %3s %8s\n", "Item", "Qty", "Price"));
            sb.append("--------------------------------\n");
            
            for (OrderItem item : order.getItems()) {
                String name = item.getMenuItem().getName();
                if (name.length() > 18) name = name.substring(0, 15) + "...";
                sb.append(String.format("%-18s %3d %8s\n", 
                    name, item.getQuantity(), langManager.formatPrice(item.getSubtotal())));
            }
            
            sb.append("--------------------------------\n");
            sb.append(String.format("Total Amount     : %10s\n", langManager.formatPrice(order.getTotal())));
            sb.append(String.format("Payment Method   : %10s\n", payment.getMethod()));
            
            if (payment.getMethod() == Payment.PaymentMethod.CASH) {
                sb.append(String.format("Received Amount  : %10s\n", langManager.formatPrice(payment.getReceivedAmount())));
                sb.append(String.format("Change           : %10s\n", langManager.formatPrice(payment.getChangeAmount())));
            }
            sb.append("================================\n");
            sb.append("      Thank you for visiting    \n");
            sb.append("================================");
            
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(350, 400));
            JOptionPane.showMessageDialog(view, scrollPane, "Receipt View", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "Order details not found.", "Error", JOptionPane.ERROR_MESSAGE);
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
        
        // [확인] 취소된 내역 갱신
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