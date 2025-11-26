package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesData {
    private Map<LocalDate, List<Payment>> dailySales;
    private List<Payment> cancelledPayments;
    private Map<String, Order> allOrders; 
    private Map<String, Integer> itemSalesCount;
    
    public SalesData() {
        this.dailySales = new HashMap<>();
        this.cancelledPayments = new ArrayList<>();
        this.allOrders = new HashMap<>();
        this.itemSalesCount = new HashMap<>();
    }
    
    public void recordSale(Payment payment, Order order) {
        LocalDate date = payment.getPaymentTime().toLocalDate();
        dailySales.computeIfAbsent(date, k -> new ArrayList<>()).add(payment);
        
        allOrders.put(order.getOrderId(), order); 
        
        for (OrderItem item : order.getItems()) { 
            String itemName = item.getMenuItem().getName(); 
            itemSalesCount.put(itemName, 
                itemSalesCount.getOrDefault(itemName, 0) + item.getQuantity()); 
        }
    }
    
    /**
     * [수정] 주문 취소 로직 개선
     * ID 기반으로 전체 dailySales를 순회하여 해당 Payment 객체를 확실하게 찾아서 이동시킴
     */
    public boolean cancelOrder(String orderId) {
        if (!allOrders.containsKey(orderId)) return false;
        
        Order orderToRemove = allOrders.get(orderId);
        orderToRemove.setStatus("Cancelled");
        
        // 1. Payment 찾기 및 이동 (전체 검색으로 변경하여 안정성 확보)
        Payment paymentToRemove = null;
        LocalDate foundDate = null;
        
        // 모든 날짜의 리스트를 검색
        for (Map.Entry<LocalDate, List<Payment>> entry : dailySales.entrySet()) {
            List<Payment> dailyList = entry.getValue();
            for (Payment p : dailyList) {
                if (p.getOrderId().equals(orderId)) {
                    paymentToRemove = p;
                    foundDate = entry.getKey();
                    break;
                }
            }
            if (paymentToRemove != null) break;
        }
        
        // 찾았으면 이동
        if (paymentToRemove != null && foundDate != null) {
            // 활성 목록에서 제거
            dailySales.get(foundDate).remove(paymentToRemove);
            // 취소 목록에 추가
            cancelledPayments.add(paymentToRemove);
        } else {
            // Payment를 못 찾았지만 Order는 존재한다면? (예외 케이스)
            // 로직상 발생하기 어렵지만, 여기서는 false 리턴하지 않고 아이템 복구는 진행
        }
        
        // 2. 아이템 판매 수량 차감 (인기 항목 통계 갱신)
        for (OrderItem item : orderToRemove.getItems()) {
            String itemName = item.getMenuItem().getName();
            if (itemSalesCount.containsKey(itemName)) {
                int current = itemSalesCount.get(itemName);
                int newVal = current - item.getQuantity();
                if (newVal <= 0) itemSalesCount.remove(itemName);
                else itemSalesCount.put(itemName, newVal);
            }
        }
        
        return true;
    }
    
    public Order getOrder(String orderId) {
        return allOrders.get(orderId);
    }
    
    public Payment getPayment(String orderId) {
         for (List<Payment> list : dailySales.values()) {
             for (Payment p : list) {
                 if (p.getOrderId().equals(orderId)) return p;
             }
         }
         for (Payment p : cancelledPayments) {
             if (p.getOrderId().equals(orderId)) return p;
         }
         return null;
    }
    
    public double getTotalSales(LocalDate date) {
        return dailySales.getOrDefault(date, new ArrayList<>())
                        .stream()
                        .mapToDouble(Payment::getAmount)
                        .sum();
    }
    
    public int getTotalOrders(LocalDate date) {
        return dailySales.getOrDefault(date, new ArrayList<>()).size();
    }
    
    public double getMonthlyRevenue(int year, int month) {
        return dailySales.entrySet().stream()
                .filter(entry -> {
                    LocalDate d = entry.getKey();
                    return d.getYear() == year && d.getMonthValue() == month;
                })
                .flatMap(entry -> entry.getValue().stream())
                .mapToDouble(Payment::getAmount)
                .sum();
    }
    
    public List<Payment> getSalesByDate(LocalDate date) {
        return new ArrayList<>(dailySales.getOrDefault(date, new ArrayList<>()));
    }
    
    // [확인] Controller에서 호출하는 메서드
    public List<Payment> getCancelledPayments() {
        return new ArrayList<>(cancelledPayments);
    }
    
    public List<Payment> searchOrders(int year, int month, int day, Double amount) {
        LocalDate targetDate = LocalDate.of(year, month, day);
        List<Payment> results = new ArrayList<>(dailySales.getOrDefault(targetDate, new ArrayList<>()));
        
        if (amount != null) {
            return results.stream()
                .filter(p -> Math.abs(p.getAmount() - amount) < 0.01)
                .collect(Collectors.toList());
        }
        return results;
    }
}