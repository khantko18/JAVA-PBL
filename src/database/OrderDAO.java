package database;

import model.Order;
import model.OrderItem;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Order
 */
public class OrderDAO {
    
    public OrderDAO() {
        // No longer caching connection - will get fresh connection each time
    }
    
    // Helper method to get fresh connection
    private Connection getConnection() {
        return DatabaseManager.getInstance().getConnection();
    }
    
    // Insert Order with Items
    public boolean insertOrder(Order order) {
        Connection connection = getConnection();
        if (connection == null) {
            return false;
        }
        
        String orderSql = "INSERT INTO orders (order_id, order_date, order_time, subtotal, " +
                         "discount_percent, discount_amount, total_amount, status) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        String itemSql = "INSERT INTO order_items (order_id, menu_item_id, menu_item_name, " +
                        "quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            connection.setAutoCommit(false); // Start transaction
            
            // Insert order
            try (PreparedStatement pstmt = connection.prepareStatement(orderSql)) {
                pstmt.setString(1, order.getOrderId());
                pstmt.setDate(2, Date.valueOf(LocalDate.now()));
                pstmt.setTime(3, Time.valueOf(order.getOrderTime().toLocalTime()));
                pstmt.setDouble(4, order.getSubtotal());
                pstmt.setDouble(5, order.getDiscountPercent());
                pstmt.setDouble(6, order.getDiscountAmount());
                pstmt.setDouble(7, order.getTotal());
                pstmt.setString(8, order.getStatus());
                
                pstmt.executeUpdate();
            }
            
            // Insert order items
            try (PreparedStatement pstmt = connection.prepareStatement(itemSql)) {
                for (OrderItem item : order.getItems()) {
                    pstmt.setString(1, order.getOrderId());
                    pstmt.setString(2, item.getMenuItem().getId());
                    pstmt.setString(3, item.getMenuItem().getName());
                    pstmt.setInt(4, item.getQuantity());
                    pstmt.setDouble(5, item.getMenuItem().getPrice());
                    pstmt.setDouble(6, item.getSubtotal());
                    
                    pstmt.executeUpdate();
                }
            }
            
            connection.commit(); // Commit transaction
            return true;
            
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Get Orders by Date
    public List<Order> getOrdersByDate(LocalDate date) {
        List<Order> orders = new ArrayList<>();
        Connection connection = getConnection();
        if (connection == null) {
            return orders;
        }
        
        String sql = "SELECT * FROM orders WHERE order_date = ? ORDER BY order_time DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Note: We're creating a basic order without items for now
                    // You can extend this to load items if needed
                    Order order = new Order(rs.getString("order_id"));
                    order.setDiscountPercent(rs.getDouble("discount_percent"));
                    order.setStatus(rs.getString("status"));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orders;
    }
    
    // Get Total Orders Count
    public int getTotalOrdersCount() {
        Connection connection = getConnection();
        if (connection == null) {
            return 0;
        }
        
        String sql = "SELECT COUNT(*) as count FROM orders";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    // Get Today's Orders Count
    public int getTodayOrdersCount() {
        Connection connection = getConnection();
        if (connection == null) {
            return 0;
        }
        
        String sql = "SELECT COUNT(*) as count FROM orders WHERE order_date = CURDATE()";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
}

