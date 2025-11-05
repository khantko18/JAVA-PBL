package util;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains text resources for English and Korean languages
 */
public class LanguageResources {
    private static final Map<String, String> english = new HashMap<>();
    private static final Map<String, String> korean = new HashMap<>();
    
    static {
        initEnglish();
        initKorean();
    }
    
    private static void initEnglish() {
        // Main View
        english.put("app_title", "Cafe POS System");
        english.put("tab_order", "📋 Order");
        english.put("tab_menu", "☕ Menu Management");
        english.put("tab_sales", "📊 Sales Statistics");
        
        // Order View
        english.put("menu_items", "Menu Items");
        english.put("category", "Category:");
        english.put("all", "All");
        english.put("coffee", "Coffee");
        english.put("beverage", "Beverage");
        english.put("dessert", "Dessert");
        english.put("food", "Food");
        english.put("current_order", "Current Order");
        english.put("item", "Item");
        english.put("qty", "Qty");
        english.put("price", "Price");
        english.put("subtotal", "Subtotal");
        english.put("discount_percent", "Discount (%):");
        english.put("apply", "Apply");
        english.put("discount", "Discount:");
        english.put("total", "Total:");
        english.put("clear_order", "Clear Order");
        english.put("proceed_payment", "Proceed to Payment");
        english.put("add", "Add");
        english.put("enter_quantity", "Enter quantity for ");
        english.put("quantity", "Quantity");
        english.put("invalid_quantity", "Quantity must be positive!");
        english.put("invalid_quantity_format", "Invalid quantity format!");
        english.put("invalid_discount", "Discount must be between 0 and 100!");
        english.put("invalid_discount_format", "Invalid discount format!");
        english.put("empty_order", "Cannot process payment for an empty order!");
        english.put("empty_order_title", "Empty Order");
        english.put("confirm_clear", "Are you sure you want to clear the current order?");
        english.put("confirm_clear_title", "Confirm Clear");
        
        // Menu Management View
        english.put("menu_items_list", "Menu Items");
        english.put("id", "ID");
        english.put("name", "Name");
        english.put("description", "Description");
        english.put("available", "Available");
        english.put("yes", "Yes");
        english.put("no", "No");
        english.put("add_edit_item", "Add/Edit Menu Item");
        english.put("name_label", "Name:");
        english.put("category_label", "Category:");
        english.put("price_label", "Price:");
        english.put("description_label", "Description:");
        english.put("add_new_item", "Add New Item");
        english.put("update_item", "Update Item");
        english.put("delete_item", "Delete Item");
        english.put("clear_form", "Clear Form");
        english.put("name_empty", "Name cannot be empty");
        english.put("price_positive", "Price must be positive");
        english.put("invalid_price_format", "Invalid price format. Please enter a valid number.");
        english.put("item_added", "Menu item added successfully!");
        english.put("item_updated", "Menu item updated successfully!");
        english.put("item_deleted", "Menu item deleted successfully!");
        english.put("confirm_delete", "Are you sure you want to delete this item?");
        english.put("confirm_delete_title", "Confirm Delete");
        english.put("success", "Success");
        english.put("error", "Error");
        english.put("validation_error", "Validation Error");
        
        // Payment Dialog
        english.put("process_payment", "Process Payment");
        english.put("total_amount", "Total Amount:");
        english.put("payment_method", "Payment Method:");
        english.put("cash", "Cash");
        english.put("card", "Card");
        english.put("amount_received", "Amount Received:");
        english.put("change", "Change:");
        english.put("confirm_payment", "Confirm Payment");
        english.put("cancel", "Cancel");
        english.put("insufficient_payment", "Insufficient payment amount!");
        english.put("payment_error", "Payment Error");
        english.put("payment_success", "Payment successful!\n\nOrder ID: ");
        english.put("payment_complete", "Payment Complete");
        english.put("received", "Received: ");
        
        // Sales View
        english.put("total_revenue", "Total Revenue");
        english.put("today_sales", "Today's Sales");
        english.put("today_orders", "Today's Orders");
        english.put("recent_transactions", "Recent Transactions");
        english.put("popular_items", "Popular Items");
        english.put("time", "Time");
        english.put("order_id", "Order ID");
        english.put("amount", "Amount");
        english.put("payment", "Payment");
        english.put("item_name", "Item Name");
        english.put("quantity_sold", "Quantity Sold");
        
        // Sample menu items
        english.put("americano", "Americano");
        english.put("americano_desc", "Classic espresso with hot water");
        english.put("cappuccino", "Cappuccino");
        english.put("cappuccino_desc", "Espresso with steamed milk foam");
        english.put("latte", "Latte");
        english.put("latte_desc", "Espresso with steamed milk");
        english.put("green_tea", "Green Tea");
        english.put("green_tea_desc", "Fresh brewed green tea");
        english.put("chocolate_cake", "Chocolate Cake");
        english.put("chocolate_cake_desc", "Rich chocolate cake slice");
        english.put("croissant", "Croissant");
        english.put("croissant_desc", "Butter croissant");
    }
    
    private static void initKorean() {
        // Main View
        korean.put("app_title", "카페 POS 시스템");
        korean.put("tab_order", "📋 주문");
        korean.put("tab_menu", "☕ 메뉴 관리");
        korean.put("tab_sales", "📊 매출 통계");
        
        // Order View
        korean.put("menu_items", "메뉴 항목");
        korean.put("category", "카테고리:");
        korean.put("all", "전체");
        korean.put("coffee", "커피");
        korean.put("beverage", "음료");
        korean.put("dessert", "디저트");
        korean.put("food", "음식");
        korean.put("current_order", "현재 주문");
        korean.put("item", "항목");
        korean.put("qty", "수량");
        korean.put("price", "가격");
        korean.put("subtotal", "소계");
        korean.put("discount_percent", "할인 (%):");
        korean.put("apply", "적용");
        korean.put("discount", "할인:");
        korean.put("total", "합계:");
        korean.put("clear_order", "주문 취소");
        korean.put("proceed_payment", "결제 진행");
        korean.put("add", "추가");
        korean.put("enter_quantity", "수량 입력 ");
        korean.put("quantity", "수량");
        korean.put("invalid_quantity", "수량은 양수여야 합니다!");
        korean.put("invalid_quantity_format", "잘못된 수량 형식입니다!");
        korean.put("invalid_discount", "할인은 0에서 100 사이여야 합니다!");
        korean.put("invalid_discount_format", "잘못된 할인 형식입니다!");
        korean.put("empty_order", "빈 주문은 결제할 수 없습니다!");
        korean.put("empty_order_title", "빈 주문");
        korean.put("confirm_clear", "현재 주문을 취소하시겠습니까?");
        korean.put("confirm_clear_title", "취소 확인");
        
        // Menu Management View
        korean.put("menu_items_list", "메뉴 항목");
        korean.put("id", "ID");
        korean.put("name", "이름");
        korean.put("description", "설명");
        korean.put("available", "이용 가능");
        korean.put("yes", "예");
        korean.put("no", "아니오");
        korean.put("add_edit_item", "메뉴 항목 추가/수정");
        korean.put("name_label", "이름:");
        korean.put("category_label", "카테고리:");
        korean.put("price_label", "가격:");
        korean.put("description_label", "설명:");
        korean.put("add_new_item", "새 항목 추가");
        korean.put("update_item", "항목 수정");
        korean.put("delete_item", "항목 삭제");
        korean.put("clear_form", "양식 지우기");
        korean.put("name_empty", "이름은 비워둘 수 없습니다");
        korean.put("price_positive", "가격은 양수여야 합니다");
        korean.put("invalid_price_format", "잘못된 가격 형식입니다. 유효한 숫자를 입력하세요.");
        korean.put("item_added", "메뉴 항목이 성공적으로 추가되었습니다!");
        korean.put("item_updated", "메뉴 항목이 성공적으로 수정되었습니다!");
        korean.put("item_deleted", "메뉴 항목이 성공적으로 삭제되었습니다!");
        korean.put("confirm_delete", "이 항목을 삭제하시겠습니까?");
        korean.put("confirm_delete_title", "삭제 확인");
        korean.put("success", "성공");
        korean.put("error", "오류");
        korean.put("validation_error", "유효성 검사 오류");
        
        // Payment Dialog
        korean.put("process_payment", "결제 처리");
        korean.put("total_amount", "총 금액:");
        korean.put("payment_method", "결제 방법:");
        korean.put("cash", "현금");
        korean.put("card", "카드");
        korean.put("amount_received", "받은 금액:");
        korean.put("change", "거스름돈:");
        korean.put("confirm_payment", "결제 확인");
        korean.put("cancel", "취소");
        korean.put("insufficient_payment", "결제 금액이 부족합니다!");
        korean.put("payment_error", "결제 오류");
        korean.put("payment_success", "결제 성공!\n\n주문 ID: ");
        korean.put("payment_complete", "결제 완료");
        korean.put("received", "받은 금액: ");
        
        // Sales View
        korean.put("total_revenue", "총 수익");
        korean.put("today_sales", "오늘의 매출");
        korean.put("today_orders", "오늘의 주문");
        korean.put("recent_transactions", "최근 거래");
        korean.put("popular_items", "인기 항목");
        korean.put("time", "시간");
        korean.put("order_id", "주문 ID");
        korean.put("amount", "금액");
        korean.put("payment", "결제");
        korean.put("item_name", "항목 이름");
        korean.put("quantity_sold", "판매 수량");
        
        // Sample menu items
        korean.put("americano", "아메리카노");
        korean.put("americano_desc", "뜨거운 물과 에스프레소");
        korean.put("cappuccino", "카푸치노");
        korean.put("cappuccino_desc", "스팀 우유 거품과 에스프레소");
        korean.put("latte", "라떼");
        korean.put("latte_desc", "스팀 우유와 에스프레소");
        korean.put("green_tea", "녹차");
        korean.put("green_tea_desc", "신선한 녹차");
        korean.put("chocolate_cake", "초콜릿 케이크");
        korean.put("chocolate_cake_desc", "진한 초콜릿 케이크 한 조각");
        korean.put("croissant", "크루아상");
        korean.put("croissant_desc", "버터 크루아상");
    }
    
    public static String getEnglish(String key) {
        return english.getOrDefault(key, key);
    }
    
    public static String getKorean(String key) {
        return korean.getOrDefault(key, key);
    }
}

