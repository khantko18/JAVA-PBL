import controller.MenuController;
import controller.OrderController;
import controller.SalesController;
import model.MenuManager;
import model.MenuItem;
import model.SalesData;
import view.MainView;
import database.MenuItemDAO;
import javax.swing.*;
import java.util.List;

/**
 * Main application entry point for the Cafe POS System
 * This class initializes the MVC components and starts the application
 */
public class POSApplication {
    private MainView mainView;
    private MenuManager menuManager;
    private SalesData salesData;	
    
    // Controllers - kept as fields for potential future use (cleanup, state management)
    @SuppressWarnings("unused")
    private MenuController menuController;
    private OrderController orderController;
    @SuppressWarnings("unused")
    private SalesController salesController;
    
    public POSApplication() {
        // Initialize Models
        menuManager = new MenuManager();
        salesData = new SalesData();
        
        // Load menu items from database BEFORE creating views (fast startup)
        loadMenuFromDatabase();
        
        // Initialize View
        mainView = new MainView();
        
        // Initialize Controllers
        orderController = new OrderController(
            menuManager,
            salesData,
            mainView.getOrderView(),
            mainView.getMembershipView()
        );
        
        menuController = new MenuController(
            menuManager, 
            mainView.getMenuManagementView(),
            orderController
        );
        
        salesController = new SalesController(
            salesData,
            mainView.getSalesView()
        );
        
        // Setup tab change listener to refresh data
        setupTabChangeListener();
        
        // Show the main window
        mainView.setVisible(true);
    }
    
    /**
     * Pre-load menu items from database before UI initialization
     * This ensures the UI shows database items immediately on startup
     */
    private void loadMenuFromDatabase() {
        try {
            MenuItemDAO menuItemDAO = new MenuItemDAO();
            List<MenuItem> dbItems = menuItemDAO.getAllMenuItems();
            
            if (!dbItems.isEmpty()) {
                // Clear default sample items
                menuManager.getAllMenuItems().clear();
                
                // Add database items
                for (MenuItem item : dbItems) {
                    menuManager.addMenuItem(item);
                }
                
                System.out.println("✅ Pre-loaded " + dbItems.size() + " menu items from database");
            } else {
                System.out.println("ℹ️ No items in database, using default menu");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Could not pre-load from database: " + e.getMessage());
            System.err.println("⚠️ Using default menu items");
            // Continue with default menu items
        }
    }
    
    private void setupTabChangeListener() {
        JTabbedPane tabbedPane = mainView.getTabbedPane();
        
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            
            // Refresh order view when switching to it (to reflect sold out changes)
            if (selectedIndex == 0) { // Order tab
                orderController.refreshMenu();
            }
            
            // Refresh sales view when switching to it
            if (selectedIndex == 2) { // Sales tab
                salesController.refreshStatistics();
            }
        });
    }
    
    public static void main(String[] args) {
        // Set system look and feel for better UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Run the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new POSApplication();
            System.out.println("===========================================");
            System.out.println("  Cafe POS System Started Successfully!");
            System.out.println("===========================================");
            System.out.println("MVC Architecture Components:");
            System.out.println("✓ Models: MenuItem, Order, Payment, SalesData");
            System.out.println("✓ Views: OrderView, MenuManagementView, SalesView");
            System.out.println("✓ Controllers: MenuController, OrderController, SalesController");
            System.out.println("===========================================");
        });
    }
}

