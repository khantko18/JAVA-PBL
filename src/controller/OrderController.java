package controller;

import model.*;
import view.OrderView;
import view.OrderCustomizationDialog;
import view.PaymentDialog;
import util.LanguageManager;
import database.OrderDAO;
import database.PaymentDAO;
import database.MemberDAO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderController {
    private MenuManager menuManager;
    private Order currentOrder;
    private SalesData salesData;
    private OrderView view;
    private int orderCounter;
    private LanguageManager langManager;
    private OrderDAO orderDAO;
    private PaymentDAO paymentDAO;
    private MemberDAO memberDAO;
    
    public OrderController(MenuManager menuManager, SalesData salesData, OrderView view) {
        this.menuManager = menuManager;
        this.salesData = salesData;
        this.view = view;
        this.orderCounter = 1;
        this.langManager = LanguageManager.getInstance();
        this.orderDAO = new OrderDAO();
        this.paymentDAO = new PaymentDAO();
        this.memberDAO = new MemberDAO();
        
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
        LocalDate now = LocalDate.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String orderId = String.format("%s%04d", dateStr, orderCounter++);
        currentOrder = new Order(orderId);
    }
    
    private void initializeListeners() {
        view.getCategoryFilter().addActionListener(e -> refreshMenuDisplay());
        view.getApplyDiscountButton().addActionListener(e -> handleApplyDiscount());
        view.getClearButton().addActionListener(e -> handleClearOrder());
        view.getPayButton().addActionListener(e -> handlePayment());
    }
    
    private void refreshMenuDisplay() {
        String selectedCategory = (String) view.getCategoryFilter().getSelectedItem();
        if (selectedCategory == null) return;
        List<model.MenuItem> items;
        if (langManager.getText("all").equals(selectedCategory) || "All".equals(selectedCategory)) {
            items = menuManager.getAllMenuItems();
        } else {
            String categoryKey = langManager.getCategoryKey(selectedCategory);
            items = menuManager.getMenuItemsByCategory(categoryKey);
        }
        view.displayMenuItems(items);
        attachMenuCardListeners();
    }
    
    private void attachMenuCardListeners() {
        JPanel menuPanel = view.getMenuPanel();
        for (Component card : menuPanel.getComponents()) {
            if (card instanceof JPanel) attachButtonListener((JPanel) card);
        }
    }
    
    private void attachButtonListener(JPanel card) {
        for (Component comp : card.getComponents()) {
            if (comp instanceof JPanel) {
                for (Component inner : ((JPanel) comp).getComponents()) {
                    if (inner instanceof JButton) {
                        JButton btn = (JButton) inner;
                        for (java.awt.event.ActionListener al : btn.getActionListeners()) btn.removeActionListener(al);
                        btn.addActionListener(e -> {
                            model.MenuItem item = (model.MenuItem) btn.getClientProperty("menuItem");
                            String action = (String) btn.getClientProperty("action");
                            if ("buy".equals(action)) handleBuyButtonClick(item);
                        });
                    }
                }
            }
        }
    }
    
    private void handleBuyButtonClick(model.MenuItem item) {
        if (item == null) return;
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(view);
        OrderCustomizationDialog dialog = new OrderCustomizationDialog(parentFrame, item);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            currentOrder.addItem(item, dialog.getQuantity());
            refreshOrderDisplay();
        }
    }
    
    private void handleApplyDiscount() {
        try {
            String discountStr = view.getDiscountField().getText().trim();
            double discount = Double.parseDouble(discountStr);
            if (discount < 0 || discount > 100) throw new NumberFormatException();
            currentOrder.setDiscountPercent(discount);
            refreshOrderDisplay();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, langManager.getText("invalid_discount"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleClearOrder() {
        int confirm = JOptionPane.showConfirmDialog(view, langManager.getText("confirm_clear"), langManager.getText("confirm_clear_title"), JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            currentOrder.clear();
            currentOrder.setDiscountPercent(0);
            view.getDiscountField().setText("0");
            refreshOrderDisplay();
        }
    }
    
    private void handlePayment() {
        if (currentOrder.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(view, langManager.getText("empty_order"), langManager.getText("empty_order_title"), JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        PaymentDialog paymentDialog = new PaymentDialog((Frame) SwingUtilities.getWindowAncestor(view), currentOrder.getTotal());
        paymentDialog.setVisible(true);
        
        if (paymentDialog.isConfirmed()) {
            try {
                double finalAmount = paymentDialog.getFinalAmount();
                Payment.PaymentMethod method = paymentDialog.isCashPayment() ? Payment.PaymentMethod.CASH : Payment.PaymentMethod.CARD;
                String paymentId = String.format("PAY%s", currentOrder.getOrderId());
                Payment payment = new Payment(paymentId, currentOrder.getOrderId(), finalAmount, method);
                
                if (method == Payment.PaymentMethod.CASH) {
                    payment.processCashPayment(paymentDialog.getAmountReceived());
                }
                
                Member member = paymentDialog.getCurrentMember();
                if (member != null) {
                    member.addSpending(finalAmount);
                    memberDAO.updateMember(member);
                }
                
                currentOrder.setStatus("Completed");
                salesData.recordSale(payment, currentOrder);
                orderDAO.insertOrder(currentOrder);
                paymentDAO.insertPayment(payment);
                
                // [수정] 결제 완료 팝업 내용 개선 (받은 금액, 거스름돈 추가)
                StringBuilder msg = new StringBuilder();
                msg.append(langManager.getText("payment_success")).append("\n");
                msg.append("--------------------------------\n");
                msg.append(langManager.getText("order_id")).append(": ").append(currentOrder.getOrderId()).append("\n");
                msg.append(langManager.getText("total")).append(" ").append(langManager.formatPrice(finalAmount)).append("\n");
                
                if (method == Payment.PaymentMethod.CASH) {
                    // LanguageResources에 있는 "received"와 "change" 키 사용 (공백 제거)
                    String receivedLabel = langManager.getText("received").replace(":", "").trim();
                    String changeLabel = langManager.getText("change").replace(":", "").trim();
                    
                    msg.append(receivedLabel).append(": ").append(langManager.formatPrice(payment.getReceivedAmount())).append("\n");
                    msg.append(changeLabel).append(": ").append(langManager.formatPrice(payment.getChangeAmount())).append("\n");
                }
                
                if (member != null) {
                    msg.append("--------------------------------\n");
                    msg.append(langManager.getText("label_member")).append(": ").append(member.getName());
                    msg.append(" (").append(member.getLevelName()).append(")");
                }
                
                JOptionPane.showMessageDialog(view, msg.toString(), langManager.getText("payment_complete"), JOptionPane.INFORMATION_MESSAGE);
                
                createNewOrder();
                view.getDiscountField().setText("0");
                refreshOrderDisplay();
                
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Payment Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void refreshOrderDisplay() {
        view.updateOrderTable(currentOrder.getItems());
        view.updateTotals(currentOrder.getSubtotal(), currentOrder.getDiscountAmount(), currentOrder.getTotal());
    }
    
    public Order getCurrentOrder() { return currentOrder; }
    public SalesData getSalesData() { return salesData; }
    public void refreshMenu() { refreshMenuDisplay(); }
}