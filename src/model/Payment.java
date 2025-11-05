package model;

import java.time.LocalDateTime;

/**
 * Model class representing a payment transaction
 */
public class Payment {
    public enum PaymentMethod {
        CASH, CARD
    }
    
    private String paymentId;
    private String orderId;
    private double amount;
    private PaymentMethod method;
    private LocalDateTime paymentTime;
    private double receivedAmount; // For cash payments
    private double changeAmount;   // For cash payments
    
    public Payment(String paymentId, String orderId, double amount, PaymentMethod method) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.paymentTime = LocalDateTime.now();
        this.receivedAmount = 0.0;
        this.changeAmount = 0.0;
    }
    
    public void processCashPayment(double receivedAmount) {
        if (receivedAmount >= amount) {
            this.receivedAmount = receivedAmount;
            this.changeAmount = receivedAmount - amount;
        } else {
            throw new IllegalArgumentException("Insufficient payment amount");
        }
    }
    
    // Getters
    public String getPaymentId() { return paymentId; }
    public String getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public PaymentMethod getMethod() { return method; }
    public LocalDateTime getPaymentTime() { return paymentTime; }
    public double getReceivedAmount() { return receivedAmount; }
    public double getChangeAmount() { return changeAmount; }
    
    @Override
    public String toString() {
        return "Payment: " + paymentId + " - " + method + " - $" + String.format("%.2f", amount);
    }
}

