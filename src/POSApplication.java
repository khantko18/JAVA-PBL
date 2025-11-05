import controller.MenuController;
import controller.OrderController;
import controller.SalesController;
import model.MenuManager;
import model.SalesData;
import view.MainView;
import javax.swing.*;

/**
 * Main application entry point for the Cafe POS System
 * This class initializes the MVC components and starts the application
 */
public class POSApplication {
    private MainView mainView;
    private MenuManager menuManager;
    private SalesData salesData;
    
    @SuppressWarnings("unused")
    private MenuController menuController;
    @SuppressWarnings("unused")
    private OrderController orderController;
    private SalesController salesController;
    
    public POSApplication() {
        // Initialize Models
        menuManager = new MenuManager();
        salesData = new SalesData();
        
        // Initialize View
        mainView = new MainView();
        
        // Initialize Controllers
        menuController = new MenuController(
            menuManager, 
            mainView.getMenuManagementView()
        );
        
        orderController = new OrderController(
            menuManager,
            salesData,
            mainView.getOrderView()
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
    
    private void setupTabChangeListener() {
        JTabbedPane tabbedPane = mainView.getTabbedPane();
        
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            
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

