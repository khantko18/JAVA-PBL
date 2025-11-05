package util;

import database.PaymentDAO;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Utility class for exporting sales data to CSV format
 */
public class CSVExporter {
    
    private static final String CSV_SEPARATOR = ",";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Export sales data to CSV file
     */
    public static boolean exportSalesToCSV(String filePath, LocalDate startDate, LocalDate endDate) {
        PaymentDAO paymentDAO = new PaymentDAO();
        List<Map<String, Object>> salesData = paymentDAO.getSalesSummary(startDate, endDate);
        
        if (salesData.isEmpty()) {
            System.out.println("No sales data to export");
            return false;
        }
        
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write CSV Header
            writer.append("Date");
            writer.append(CSV_SEPARATOR);
            writer.append("Time");
            writer.append(CSV_SEPARATOR);
            writer.append("Order ID");
            writer.append(CSV_SEPARATOR);
            writer.append("Payment Method");
            writer.append(CSV_SEPARATOR);
            writer.append("Subtotal");
            writer.append(CSV_SEPARATOR);
            writer.append("Discount");
            writer.append(CSV_SEPARATOR);
            writer.append("Total Amount");
            writer.append("\n");
            
            // Write Data Rows
            for (Map<String, Object> row : salesData) {
                writer.append(row.get("date").toString());
                writer.append(CSV_SEPARATOR);
                writer.append(row.get("time").toString());
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(row.get("order_id").toString()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(row.get("payment_method").toString()));
                writer.append(CSV_SEPARATOR);
                writer.append(String.format("%.2f", row.get("subtotal")));
                writer.append(CSV_SEPARATOR);
                writer.append(String.format("%.2f", row.get("discount")));
                writer.append(CSV_SEPARATOR);
                writer.append(String.format("%.2f", row.get("total")));
                writer.append("\n");
            }
            
            // Write Summary
            writer.append("\n");
            writer.append("SUMMARY");
            writer.append("\n");
            
            double totalRevenue = salesData.stream()
                .mapToDouble(row -> (Double) row.get("total"))
                .sum();
            
            double totalDiscount = salesData.stream()
                .mapToDouble(row -> (Double) row.get("discount"))
                .sum();
            
            int totalOrders = salesData.size();
            
            writer.append("Total Orders");
            writer.append(CSV_SEPARATOR);
            writer.append(String.valueOf(totalOrders));
            writer.append("\n");
            
            writer.append("Total Revenue");
            writer.append(CSV_SEPARATOR);
            writer.append(String.format("%.2f", totalRevenue));
            writer.append("\n");
            
            writer.append("Total Discount");
            writer.append(CSV_SEPARATOR);
            writer.append(String.format("%.2f", totalDiscount));
            writer.append("\n");
            
            writer.append("Average Order Value");
            writer.append(CSV_SEPARATOR);
            writer.append(String.format("%.2f", totalRevenue / totalOrders));
            writer.append("\n");
            
            writer.append("\n");
            writer.append("Exported on: " + LocalDate.now().format(DATE_FORMATTER));
            writer.append("\n");
            
            writer.flush();
            System.out.println("✅ CSV export successful: " + filePath);
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ CSV export failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Export popular items to CSV
     */
    public static boolean exportPopularItemsToCSV(String filePath) {
        PaymentDAO paymentDAO = new PaymentDAO();
        Map<String, Integer> popularItems = paymentDAO.getPopularItems();
        
        if (popularItems.isEmpty()) {
            System.out.println("No items data to export");
            return false;
        }
        
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write CSV Header
            writer.append("Item Name");
            writer.append(CSV_SEPARATOR);
            writer.append("Total Quantity Sold");
            writer.append("\n");
            
            // Write Data Rows (sorted by quantity)
            popularItems.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> {
                    try {
                        writer.append(escapeCSV(entry.getKey()));
                        writer.append(CSV_SEPARATOR);
                        writer.append(entry.getValue().toString());
                        writer.append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            
            writer.append("\n");
            writer.append("Exported on: " + LocalDate.now().format(DATE_FORMATTER));
            writer.append("\n");
            
            writer.flush();
            System.out.println("✅ Popular items CSV export successful: " + filePath);
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ CSV export failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Escape special characters in CSV
     */
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        
        // If value contains comma, quote, or newline, wrap in quotes
        if (value.contains(CSV_SEPARATOR) || value.contains("\"") || value.contains("\n")) {
            value = "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
}

