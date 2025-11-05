package controller;

import model.Payment;
import model.SalesData;
import view.SalesView;
import util.LanguageManager;
import util.CSVExporter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing sales statistics
 */
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
        setupLanguageListener();
        setupExportListeners();
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> {
            refreshStatistics();
        });
    }
    
    private void setupExportListeners() {
        // Export Sales CSV
        view.getExportCSVButton().addActionListener(e -> handleExportSales());
        
        // Export Popular Items CSV
        view.getExportItemsButton().addActionListener(e -> handleExportItems());
    }
    
    private void handleExportSales() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Sales to CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        
        // Set default file name with current date
        String defaultFileName = "sales_report_" + LocalDate.now() + ".csv";
        fileChooser.setSelectedFile(new File(defaultFileName));
        
        int userSelection = fileChooser.showSaveDialog(view);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            
            // Ensure .csv extension
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }
            
            // Export data (last 30 days)
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(30);
            
            boolean success = CSVExporter.exportSalesToCSV(filePath, startDate, endDate);
            
            if (success) {
                JOptionPane.showMessageDialog(view,
                    "Sales data exported successfully!\n" +
                    "File: " + filePath,
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view,
                    "Failed to export sales data.\n" +
                    "Please check console for details.",
                    "Export Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleExportItems() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Popular Items to CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        
        // Set default file name
        String defaultFileName = "popular_items_" + LocalDate.now() + ".csv";
        fileChooser.setSelectedFile(new File(defaultFileName));
        
        int userSelection = fileChooser.showSaveDialog(view);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            
            // Ensure .csv extension
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }
            
            boolean success = CSVExporter.exportPopularItemsToCSV(filePath);
            
            if (success) {
                JOptionPane.showMessageDialog(view,
                    "Popular items exported successfully!\n" +
                    "File: " + filePath,
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view,
                    "Failed to export popular items.\n" +
                    "Please check console for details.",
                    "Export Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshStatistics() {
        LocalDate today = LocalDate.now();
        
        // Calculate statistics
        double totalRevenue = salesData.getTotalRevenue();
        double todaySales = salesData.getTotalSales(today);
        int todayOrders = salesData.getTotalOrders(today);
        
        // Update view
        view.updateStatistics(totalRevenue, todaySales, todayOrders);
        
        // Update sales table
        List<Payment> todayPayments = salesData.getSalesByDate(today);
        List<String[]> salesTableData = new ArrayList<>();
        
        for (Payment payment : todayPayments) {
            String[] row = {
                payment.getPaymentTime().format(timeFormatter),
                payment.getOrderId(),
                langManager.formatPrice(payment.getAmount()),
                payment.getMethod().toString()
            };
            salesTableData.add(row);
        }
        
        view.updateSalesTable(salesTableData);
        
        // Update item statistics
        Map<String, Integer> itemStats = salesData.getItemSalesCount();
        view.updateItemStats(itemStats);
    }
}

