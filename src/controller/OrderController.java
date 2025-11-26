package controller;

import model.*;
import view.OrderView;
import view.OrderCustomizationDialog;
import view.PaymentDialog;
import util.LanguageManager;
import database.OrderDAO;
import database.PaymentDAO;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for managing orders
 */
public class OrderController {
    private MenuManager menuManager;
    private Order currentOrder;
    private SalesData salesData;
    private OrderView view;
    private int orderCounter;
    private LanguageManager langManager;
    private OrderDAO orderDAO;
    private PaymentDAO paymentDAO;
    
    public OrderController(MenuManager menuManager, SalesData salesData, OrderView view) {
        this.menuManager = menuManager;
        this.salesData = salesData;
        this.view = view;
        this.orderCounter = 1;
        this.langManager = LanguageManager.getInstance();
        this.orderDAO = new OrderDAO();
        this.paymentDAO = new PaymentDAO();
        
        createNewOrder();
        initializeListeners();
        refreshMenuDisplay();
        setupLanguageListener();
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> {
            refreshMenuDisplay();
            refreshOrderDisplay();
        });
    }
    
    private void createNewOrder() {
        // Modified: Generate Order ID as YYYYMMDD + 4 digit sequence
        // Example: 202511240001
        LocalDate now = LocalDate.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String orderId = String.format("%s%04d", dateStr, orderCounter++);
        
        currentOrder = new Order(orderId);
    }
    
    private void initializeListeners() {
        // Category filter
        view.getCategoryFilter().addActionListener(e -> refreshMenuDisplay());
        
        // Add item to order (from menu cards)
        Component[] menuCards = view.getMenuPanel().getComponents();
        for (Component card : menuCards) {
            if (card instanceof JPanel) {
                attachAddButtonListener((JPanel) card);
            }
        }
        
        // Apply discount button
        view.getApplyDiscountButton().addActionListener(e -> handleApplyDiscount());
        
        // Clear order button
        view.getClearButton().addActionListener(e -> handleClearOrder());
        
        // Payment button
        view.getPayButton().addActionListener(e -> handlePayment());
    }
    
    private void refreshMenuDisplay() {
        String selectedCategory = (String) view.getCategoryFilter().getSelectedItem();
        
        // If no category is selected (e.g., during language switch), show all items
        if (selectedCategory == null) {
            view.displayMenuItems(menuManager.getAllMenuItems());
            return;
        }
        
        List<model.MenuItem> items;
        
        if (langManager.getText("all").equals(selectedCategory) || "All".equals(selectedCategory)) {
            items = menuManager.getAllMenuItems();
        } else {
            // Convert display category to English key
            String categoryKey = langManager.getCategoryKey(selectedCategory);
            if (categoryKey == null) {
                items = menuManager.getAllMenuItems();
            } else {
                items = menuManager.getMenuItemsByCategory(categoryKey);
            }
        }
        
        view.displayMenuItems(items);
        
        // Re-attach listeners to new menu cards
        Component[] menuCards = view.getMenuPanel().getComponents();
        for (Component card : menuCards) {
            if (card instanceof JPanel) {
                attachAddButtonListener((JPanel) card);
            }
        }
    }
    
    private void attachAddButtonListener(JPanel card) {
        Component[] components = card.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                // Look for BUY button in the buy panel
                Component[] panelComponents = ((JPanel) comp).getComponents();
                for (Component innerComp : panelComponents) {
                    if (innerComp instanceof JButton) {
                        JButton button = (JButton) innerComp;
                        // Remove existing listeners
                        for (java.awt.event.ActionListener al : button.getActionListeners()) {
                            button.removeActionListener(al);
                        }
                        // Add new listener
                        button.addActionListener(e -> {
                            model.MenuItem item = (model.MenuItem) button.getClientProperty("menuItem");
                            String action = (String) button.getClientProperty("action");
                            
                            if (item != null && "buy".equals(action)) {
                                handleBuyButtonClick(item);
                            }
                        });
                    }
                }
            }
        }
    }
    
    private void handleBuyButtonClick(model.MenuItem item) {
        if (item == null) {
            return;
        }
        
        // Show customization dialog
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(view);
        OrderCustomizationDialog dialog = new OrderCustomizationDialog(parentFrame, item);
        dialog.setVisible(true);
        
        // If user confirmed, add to order with selected quantity
        if (dialog.isConfirmed()) {
            int quantity = dialog.getQuantity();
            String temperature = dialog.getTemperature();
            String orderType = dialog.getOrderType();
            boolean addShot = dialog.isAddShot();
            boolean whippingCream = dialog.isWhippingCream();
            
            // Add item with quantity
            currentOrder.addItem(item, quantity);
            
            // Build extras string
            StringBuilder extras = new StringBuilder();
            if (addShot) extras.append("Add Shot, ");
            if (whippingCream) extras.append("Whipping Cream, ");
            String extrasStr = extras.length() > 0 ? extras.substring(0, extras.length() - 2) : "None";
            
            // Log the order details
            System.out.println("✅ Added to order: " + item.getName() + 
                             " | Qty: " + quantity + 
                             " | Temp: " + temperature + 
                             " | Type: " + orderType +
                             " | Extras: " + extrasStr);
            
            refreshOrderDisplay();
        }
    }
    
    private void handleApplyDiscount() {
        try {
            String discountStr = view.getDiscountField().getText().trim();
            double discount = Double.parseDouble(discountStr);
            
            if (discount < 0 || discount > 100) {
                JOptionPane.showMessageDialog(view,
                    langManager.getText("invalid_discount"),
                    langManager.getText("error"),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            currentOrder.setDiscountPercent(discount);
            refreshOrderDisplay();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view,
                langManager.getText("invalid_discount_format"),
                langManager.getText("error"),
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleClearOrder() {
        int confirm = JOptionPane.showConfirmDialog(view,
            langManager.getText("confirm_clear"),
            langManager.getText("confirm_clear_title"),
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            currentOrder.clear();
            currentOrder.setDiscountPercent(0);
            view.getDiscountField().setText("0");
            refreshOrderDisplay();
        }
    }
    
    private void handlePayment() {
        if (currentOrder.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                langManager.getText("empty_order"),
                langManager.getText("empty_order_title"),
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double totalAmount = currentOrder.getTotal();
        
        // Show payment dialog
        PaymentDialog paymentDialog = new PaymentDialog(
            (Frame) SwingUtilities.getWindowAncestor(view), 
            totalAmount
        );
        
        paymentDialog.getConfirmButton().addActionListener(e -> {
            try {
                if (paymentDialog.isCashPayment()) {
                    double received = paymentDialog.getAmountReceived();
                    if (received < totalAmount) {
                        JOptionPane.showMessageDialog(paymentDialog,
                            langManager.getText("insufficient_payment"),
                            langManager.getText("payment_error"),
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                // Process payment
                String paymentId = String.format("PAY%04d", orderCounter);
                Payment.PaymentMethod method = paymentDialog.isCashPayment() ? 
                    Payment.PaymentMethod.CASH : Payment.PaymentMethod.CARD;
                
                Payment payment = new Payment(paymentId, currentOrder.getOrderId(), totalAmount, method);
                
                if (paymentDialog.isCashPayment()) {
                    payment.processCashPayment(paymentDialog.getAmountReceived());
                }
                
                // Record sale
                currentOrder.setStatus("Completed");
                salesData.recordSale(payment, currentOrder);
                
                // Save to database
                try {
                    boolean orderSaved = orderDAO.insertOrder(currentOrder);
                    boolean paymentSaved = paymentDAO.insertPayment(payment);
                    
                    if (orderSaved && paymentSaved) {
                        System.out.println("✅ Order and payment saved to database");
                    } else {
                        System.err.println("⚠️ Failed to save to database");
                    }
                } catch (Exception dbEx) {
                    System.err.println("⚠️ Database error: " + dbEx.getMessage());
                    // Continue even if database save fails
                }
                
                // Show success message
                String message = langManager.getText("payment_success") + currentOrder.getOrderId() + "\n";
                message += langManager.getText("total") + " " + langManager.formatPrice(totalAmount) + "\n";
                
                if (paymentDialog.isCashPayment()) {
                    message += langManager.getText("received") + langManager.formatPrice(payment.getReceivedAmount()) + "\n";
                    message += langManager.getText("change") + " " + langManager.formatPrice(payment.getChangeAmount());
                }
                
                JOptionPane.showMessageDialog(view,
                    message,
                    langManager.getText("payment_complete"),
                    JOptionPane.INFORMATION_MESSAGE);
                
                paymentDialog.setConfirmed(true);
                paymentDialog.dispose();
                
                // Create new order
                createNewOrder();
                view.getDiscountField().setText("0");
                refreshOrderDisplay();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(paymentDialog,
                    "Payment error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        paymentDialog.setVisible(true);
    }
    
    private void refreshOrderDisplay() {
        view.updateOrderTable(currentOrder.getItems());
        view.updateTotals(
            currentOrder.getSubtotal(),
            currentOrder.getDiscountAmount(),
            currentOrder.getTotal()
        );
    }
    
    public Order getCurrentOrder() {
        return currentOrder;
    }
    
    public SalesData getSalesData() {
        return salesData;
    }
    
    // Public method to refresh menu display (called when availability changes)
    public void refreshMenu() {
        refreshMenuDisplay();
    }
}