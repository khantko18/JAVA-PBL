package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model class for tracking sales statistics
 */
public class SalesData {
    private Map<LocalDate, List<Payment>> dailySales;
    private Map<String, Integer> itemSalesCount;
    
    public SalesData() {
        this.dailySales = new HashMap<>();
        this.itemSalesCount = new HashMap<>();
    }
    
    public void recordSale(Payment payment, Order order) {
        // Record payment by date
        LocalDate date = payment.getPaymentTime().toLocalDate();
        dailySales.computeIfAbsent(date, k -> new ArrayList<>()).add(payment);
        
        // Record item sales count
        for (OrderItem item : order.getItems()) {
            String itemName = item.getMenuItem().getName();
            itemSalesCount.put(itemName, 
                itemSalesCount.getOrDefault(itemName, 0) + item.getQuantity());
        }
    }
    
    public double getTotalSales(LocalDate date) {
        return dailySales.getOrDefault(date, new ArrayList<>())
                        .stream()
                        .mapToDouble(Payment::getAmount)
                        .sum();
    }
    
    public int getTotalOrders(LocalDate date) {
        return dailySales.getOrDefault(date, new ArrayList<>()).size();
    }
    
    public Map<String, Integer> getItemSalesCount() {
        return new HashMap<>(itemSalesCount);
    }
    
    public List<Payment> getSalesByDate(LocalDate date) {
        return new ArrayList<>(dailySales.getOrDefault(date, new ArrayList<>()));
    }
    
    public double getTotalRevenue() {
        return dailySales.values().stream()
                        .flatMap(List::stream)
                        .mapToDouble(Payment::getAmount)
                        .sum();
    }
}

