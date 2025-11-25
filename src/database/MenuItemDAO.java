package database;

import model.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for MenuItem
 */
public class MenuItemDAO {
    
    public MenuItemDAO() {
        // No longer caching connection - will get fresh connection each time
    }
    
    // Helper method to get fresh connection
    private Connection getConnection() {
        return DatabaseManager.getInstance().getConnection();
    }
    
    // Create
    public boolean insertMenuItem(MenuItem item) {
        Connection connection = getConnection();
        if (connection == null) {
            System.err.println("⚠️ Database connection is null! Cannot insert menu item.");
            return false;
        }
        
        // First check if item with this ID already exists
        if (getMenuItemById(item.getId()) != null) {
            System.err.println("⚠️ Menu item with ID " + item.getId() + " already exists. Cannot insert.");
            return false;
        }
        
<<<<<<< HEAD
        String sql = "INSERT INTO menu_items (id, name, category, price, description, available) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
=======
        String sql = "INSERT INTO menu_items (id, name, category, price, description, image_path, available) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
>>>>>>> 786efbaa53d6a74a8cd9f12bcdb0bbda532c7371
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, item.getId());
            pstmt.setString(2, item.getName());
            pstmt.setString(3, item.getCategory());
            pstmt.setDouble(4, item.getPrice());
            pstmt.setString(5, item.getDescription());
            pstmt.setString(6, item.getImagePath());
            pstmt.setBoolean(7, item.isAvailable());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("⚠️ SQL Error inserting menu item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Read All
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        Connection connection = getConnection();
        if (connection == null) {
            return items;
        }
        
        String sql = "SELECT * FROM menu_items ORDER BY id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                MenuItem item = new MenuItem(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_path")
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
        Connection connection = getConnection();
        if (connection == null) {
            return null;
        }
        
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
                        rs.getString("description"),
                        rs.getString("image_path")
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
        Connection connection = getConnection();
        if (connection == null) {
            return false;
        }
        
<<<<<<< HEAD
        String sql = "UPDATE menu_items SET name=?, category=?, price=?, description=?, available=? " +
=======
        String sql = "UPDATE menu_items SET name=?, category=?, price=?, description=?, image_path=?, available=? " +
>>>>>>> 786efbaa53d6a74a8cd9f12bcdb0bbda532c7371
                    "WHERE id=?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getCategory());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setString(4, item.getDescription());
            pstmt.setString(5, item.getImagePath());
            pstmt.setBoolean(6, item.isAvailable());
            pstmt.setString(7, item.getId());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete
    public boolean deleteMenuItem(String id) {
        Connection connection = getConnection();
        if (connection == null) {
            return false;
        }
        
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
        Connection connection = getConnection();
        if (connection == null) {
            return items;
        }
        
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
                        rs.getString("description"),
                        rs.getString("image_path")
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

