package database;

import model.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for MenuItem
 */
public class MenuItemDAO {
    private Connection connection;
    
    public MenuItemDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }
    
    // Create
    public boolean insertMenuItem(MenuItem item) {
        String sql = "INSERT INTO menu_items (id, name, category, price, description, available) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, item.getId());
            pstmt.setString(2, item.getName());
            pstmt.setString(3, item.getCategory());
            pstmt.setDouble(4, item.getPrice());
            pstmt.setString(5, item.getDescription());
            pstmt.setBoolean(6, item.isAvailable());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Read All
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_items ORDER BY id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                MenuItem item = new MenuItem(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("description")
                );
                item.setAvailable(rs.getBoolean("available"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return items;
    }
    
    // Read by ID
    public MenuItem getMenuItemById(String id) {
        String sql = "SELECT * FROM menu_items WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    MenuItem item = new MenuItem(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("description")
                    );
                    item.setAvailable(rs.getBoolean("available"));
                    return item;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Update
    public boolean updateMenuItem(MenuItem item) {
        String sql = "UPDATE menu_items SET name=?, category=?, price=?, description=?, available=? " +
                    "WHERE id=?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getCategory());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setString(4, item.getDescription());
            pstmt.setBoolean(5, item.isAvailable());
            pstmt.setString(6, item.getId());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete
    public boolean deleteMenuItem(String id) {
        String sql = "DELETE FROM menu_items WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get by Category
    public List<MenuItem> getMenuItemsByCategory(String category) {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_items WHERE category = ? ORDER BY name";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, category);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MenuItem item = new MenuItem(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("description")
                    );
                    item.setAvailable(rs.getBoolean("available"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return items;
    }
}

