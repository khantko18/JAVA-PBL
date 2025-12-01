import controller.MenuController;
import controller.OrderController;
import controller.SalesController;
import model.MenuManager;
import model.MenuItem;
import model.SalesData;
import view.MainView;
import view.LoginDialog;
import database.MenuItemDAO;
import javax.swing.*;
import java.util.List;

/**
 * Main application entry point for the Cafe POS System
 */
public class POSApplication {
    private MainView mainView;
    private MenuManager menuManager;
    private SalesData salesData;    
    
    // Controllers
    @SuppressWarnings("unused")
    private MenuController menuController;
    private OrderController orderController;
    private SalesController salesController;
    
    public POSApplication() {
        // Initialize Models
        menuManager = new MenuManager();
        salesData = new SalesData();
        
        // Load menu items from database
        loadMenuFromDatabase();
        
        // Initialize View
        mainView = new MainView();
        
        // Initialize Controllers
        orderController = new OrderController(
            menuManager,
            salesData,
            mainView.getOrderView()
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
        
        // Setup tab change listener
        setupTabChangeListener();
        
        // Show the main window
        mainView.setVisible(true);
    }
    
    private void loadMenuFromDatabase() {
        try {
            MenuItemDAO menuItemDAO = new MenuItemDAO();
            List<MenuItem> dbItems = menuItemDAO.getAllMenuItems();
            
            if (!dbItems.isEmpty()) {
                menuManager.getAllMenuItems().clear();
                for (MenuItem item : dbItems) {
                    menuManager.addMenuItem(item);
                }
                System.out.println("✅ Pre-loaded " + dbItems.size() + " menu items from database");
            } else {
                System.out.println("ℹ️ No items in database, using default menu");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Could not pre-load from database: " + e.getMessage());
        }
    }
    
    private void setupTabChangeListener() {
        JTabbedPane tabbedPane = mainView.getTabbedPane();
        
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            
            // [수정] 탭 인덱스 매핑 수정 (0:주문, 1:메뉴, 2:멤버십, 3:매출)
            if (selectedIndex == 0) { // Order tab
                orderController.refreshMenu();
            }
            
            // [수정] 매출 통계 탭이 2번에서 3번으로 변경됨
            if (selectedIndex == 3) { // Sales tab
                salesController.refreshStatistics();
            }
        });
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginDialog loginDialog = new LoginDialog(null);
            loginDialog.setVisible(true);

            if (loginDialog.isSucceeded()) {
                new POSApplication();
                System.out.println("System Started.");
            } else {
                System.exit(0);
            }
        });
    }
}