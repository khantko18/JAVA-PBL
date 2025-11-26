package database;

import java.sql.*;
import java.util.Properties;

/**
 * Database connection manager for MySQL
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/kkkdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "jsj884603#";
    
    private static DatabaseManager instance;
    private Connection connection;
    
    private DatabaseManager() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connection properties
            Properties props = new Properties();
            props.setProperty("user", DB_USER);
            props.setProperty("password", DB_PASSWORD);
            props.setProperty("useSSL", "false");
            props.setProperty("serverTimezone", "UTC");
            props.setProperty("allowPublicKeyRetrieval", "true");
            
            // Establish connection
            connection = DriverManager.getConnection(DB_URL, props);
            System.out.println("✅ Database connected successfully!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found!");
            System.err.println("⚠️ Application will run without database support");
            connection = null;
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed!");
            System.err.println("⚠️ Application will run without database support");
            System.err.println("Error: " + e.getMessage());
            connection = null;
        }
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            // Check if connection is closed or invalid
            if (connection == null || connection.isClosed()) {
                System.out.println("⚠️ Connection lost, attempting to reconnect...");
                
                // Reconnect with proper properties
                Properties props = new Properties();
                props.setProperty("user", DB_USER);
                props.setProperty("password", DB_PASSWORD);
                props.setProperty("useSSL", "false");
                props.setProperty("serverTimezone", "UTC");
                props.setProperty("allowPublicKeyRetrieval", "true");
                
                connection = DriverManager.getConnection(DB_URL, props);
                System.out.println("✅ Reconnected to database successfully!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to reconnect to database: " + e.getMessage());
            e.printStackTrace();
            connection = null;
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Test database connection
     */
    public boolean testConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}

