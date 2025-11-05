package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a customer order
 */
public class Order {
    private String orderId;
    private List<OrderItem> items;
    private LocalDateTime orderTime;
    private double discountPercent;
    private String status; // "Pending", "Completed", "Cancelled"
    
    public Order(String orderId) {
        this.orderId = orderId;
        this.items = new ArrayList<>();
        this.orderTime = LocalDateTime.now();
        this.discountPercent = 0.0;
        this.status = "Pending";
    }
    
    public void addItem(MenuItem menuItem, int quantity) {
        // Check if item already exists in order
        for (OrderItem item : items) {
            if (item.getMenuItem().getId().equals(menuItem.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // Add new item if not exists
        items.add(new OrderItem(menuItem, quantity));
    }
    
    public void removeItem(MenuItem menuItem) {
        items.removeIf(item -> item.getMenuItem().getId().equals(menuItem.getId()));
    }
    
    public double getSubtotal() {
        return items.stream()
                   .mapToDouble(OrderItem::getSubtotal)
                   .sum();
    }
    
    public double getDiscountAmount() {
        return getSubtotal() * (discountPercent / 100.0);
    }
    
    public double getTotal() {
        return getSubtotal() - getDiscountAmount();
    }
    
    // Getters
    public String getOrderId() { return orderId; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public LocalDateTime getOrderTime() { return orderTime; }
    public double getDiscountPercent() { return discountPercent; }
    public String getStatus() { return status; }
    
    // Setters
    public void setDiscountPercent(double discountPercent) {
        if (discountPercent >= 0 && discountPercent <= 100) {
            this.discountPercent = discountPercent;
        }
    }
    
    public void setStatus(String status) { this.status = status; }
    
    public void clear() {
        items.clear();
    }
}

