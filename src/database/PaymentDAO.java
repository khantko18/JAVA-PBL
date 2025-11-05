package database;

import model.Payment;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Payment
 */
public class PaymentDAO {
    private Connection connection;
    
    public PaymentDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }
    
    // Insert Payment
    public boolean insertPayment(Payment payment) {
        String sql = "INSERT INTO payments (payment_id, order_id, payment_date, payment_time, " +
                    "payment_method, amount, received_amount, change_amount) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, payment.getPaymentId());
            pstmt.setString(2, payment.getOrderId());
            pstmt.setDate(3, Date.valueOf(LocalDate.now()));
            pstmt.setTime(4, Time.valueOf(payment.getPaymentTime().toLocalTime()));
            pstmt.setString(5, payment.getMethod().toString());
            pstmt.setDouble(6, payment.getAmount());
            pstmt.setDouble(7, payment.getReceivedAmount());
            pstmt.setDouble(8, payment.getChangeAmount());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get Total Revenue
    public double getTotalRevenue() {
        String sql = "SELECT SUM(amount) as total FROM payments";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    // Get Today's Sales
    public double getTodaySales() {
        String sql = "SELECT SUM(amount) as total FROM payments WHERE payment_date = CURDATE()";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    // Get Payments by Date
    public List<Payment> getPaymentsByDate(LocalDate date) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE payment_date = ? ORDER BY payment_time DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment(
                        rs.getString("payment_id"),
                        rs.getString("order_id"),
                        rs.getDouble("amount"),
                        Payment.PaymentMethod.valueOf(rs.getString("payment_method"))
                    );
                    
                    if (payment.getMethod() == Payment.PaymentMethod.CASH) {
                        double received = rs.getDouble("received_amount");
                        if (received > 0) {
                            payment.processCashPayment(received);
                        }
                    }
                    
                    payments.add(payment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return payments;
    }
    
    // Get Popular Items
    public Map<String, Integer> getPopularItems() {
        Map<String, Integer> items = new HashMap<>();
        String sql = "SELECT menu_item_name, SUM(quantity) as total_qty " +
                    "FROM order_items " +
                    "GROUP BY menu_item_name " +
                    "ORDER BY total_qty DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                items.put(rs.getString("menu_item_name"), rs.getInt("total_qty"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return items;
    }
    
    // Get Sales Summary for CSV Export
    public List<Map<String, Object>> getSalesSummary(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> summary = new ArrayList<>();
        
        String sql = "SELECT " +
                    "p.payment_date, " +
                    "p.payment_time, " +
                    "p.order_id, " +
                    "p.payment_method, " +
                    "p.amount, " +
                    "o.subtotal, " +
                    "o.discount_amount " +
                    "FROM payments p " +
                    "JOIN orders o ON p.order_id = o.order_id " +
                    "WHERE p.payment_date BETWEEN ? AND ? " +
                    "ORDER BY p.payment_date DESC, p.payment_time DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("date", rs.getDate("payment_date"));
                    row.put("time", rs.getTime("payment_time"));
                    row.put("order_id", rs.getString("order_id"));
                    row.put("payment_method", rs.getString("payment_method"));
                    row.put("subtotal", rs.getDouble("subtotal"));
                    row.put("discount", rs.getDouble("discount_amount"));
                    row.put("total", rs.getDouble("amount"));
                    
                    summary.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return summary;
    }
}

