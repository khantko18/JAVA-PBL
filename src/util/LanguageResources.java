package util;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains text resources for English and Korean languages.
 * - Fixed: 영수증 관련 키 이름 불일치 문제 해결 (English/Korean 키 통일)
 */
public class LanguageResources {
    private static final Map<String, String> english = new HashMap<>();
    private static final Map<String, String> korean = new HashMap<>();
    
    static {
        initEnglish();
        initKorean();
    }
    
    private static void initEnglish() {
        // --- 1. Main View ---
        english.put("app_title", "Cafe POS System");
        english.put("tab_order", "📋 Order");
        english.put("tab_menu", "☕ Menu Management");
        english.put("tab_membership", "👥 Membership");
        english.put("tab_sales", "📊 Sales Statistics");
        
        // Membership View
        english.put("members_list", "Members List");
        english.put("search", "Search");
        english.put("refresh", "Refresh");
        english.put("total_members", "Total Members");
        english.put("phone_number", "Phone Number");
        english.put("name", "Name");
        english.put("total_spent", "Total Spent");
        english.put("level", "Level");
        english.put("discount", "Discount");
        english.put("to_next_level", "To Next Level");
        english.put("add_edit_member", "Add/Edit Member");
        english.put("add_member", "Add Member");
        english.put("update", "Update");
        english.put("delete", "Delete");
        english.put("clear_form", "Clear Form");
        english.put("max_level", "Max Level");
        
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
        english.put("buy", "BUY");
        english.put("sold_out", "Sold Out");
        english.put("per_unit", "per unit");
        
        // --- 3. Order Customization Dialog ---
        english.put("customize_order", "Customize Order");
        english.put("temperature", "Temperature");
        english.put("ice", "ICE");
        english.put("hot", "HOT");
        english.put("quantity", "Quantity");
        english.put("extras", "Extras");
        english.put("add_shot", "Add Shot");
        english.put("whipping_cream", "Whipping Cream");
        english.put("order_type", "Order Type");
        english.put("take_out", "Take Out");
        english.put("in_store", "In Store");
        english.put("item_details", "Item Details");
        english.put("cancel", "Cancel");
        english.put("add_to_order", "Add to Order");
        
        // --- 4. Menu Management View ---
        english.put("menu_items_list", "Menu Items");
        english.put("add_edit_item", "Add/Edit Menu Item");
        english.put("id", "ID");
        english.put("name", "Name");
        english.put("description", "Description");
        english.put("available", "Available");
        english.put("name_label", "Name:");
        english.put("category_label", "Category:");
        english.put("price_label", "Price:");
        english.put("description_label", "Description:");
        english.put("image", "Image:");
        english.put("add_new_item", "Add New Item");
        english.put("update_item", "Update Item");
        english.put("delete_item", "Delete Item");
        english.put("clear_form", "Clear Form");
        english.put("confirm_delete", "Are you sure to delete?");
        english.put("confirm_delete_title", "Confirm Delete");
        english.put("name_empty", "Name cannot be empty");
        english.put("price_positive", "Price must be positive");
        english.put("invalid_price_format", "Invalid price format");
        english.put("item_added", "Item added successfully");
        english.put("item_updated", "Item updated successfully");
        english.put("item_deleted", "Item deleted successfully");
        english.put("success", "Success");
        english.put("error", "Error");
        english.put("validation_error", "Validation Error");
        
        // --- 5. Membership View ---
        english.put("members_list", "Members List");
        english.put("add_edit_member", "Add/Edit Member");
        english.put("search", "Search");
        english.put("refresh", "Refresh");
        english.put("total_members", "Total Members");
        english.put("phone_number", "Phone Number");
        english.put("total_spent", "Total Spent");
        english.put("level", "Level");
        english.put("to_next_level", "To Next Level");
        english.put("add_member", "Add Member");
        english.put("update", "Update");
        english.put("delete", "Delete");
        english.put("max_level", "Max Level");
        english.put("membership_info", "<html><b>Levels:</b> L5(0%), L4(5% >$417), L3(10% >$833), L2(15% >$1,667), L1(20% >$2,500)</html>");
        
        // --- 6. Sales View ---
        english.put("total_revenue", "Total Revenue");
        english.put("monthly_revenue", "Monthly Revenue");
        english.put("today_sales", "Today's Sales");
        english.put("today_orders", "Today's Orders");
        english.put("recent_transactions", "Recent Transactions");
        english.put("cancelled_history", "Cancelled History");
        english.put("title_order_search", "Order Search");
        english.put("label_year", "Y:");
        english.put("label_month", "M:");
        english.put("label_day", "D:");
        english.put("label_price", "Price:");
        english.put("btn_search", "Search");
        english.put("time", "Time");
        english.put("order_id", "Order ID");
        english.put("amount", "Amount");
        english.put("payment", "Payment");
        
        // Bottom Buttons
        english.put("btn_print_receipt", "Print Receipt");
        english.put("btn_cancel_order", "Cancel Order");
        english.put("btn_export_csv", "Export Sales to CSV");
        
        // --- 7. Payment Dialog ---
        english.put("process_payment", "Process Payment");
        english.put("total_amount", "Total Amount:");
        english.put("payment_method", "Payment Method:");
        english.put("cash", "Cash");
        english.put("card", "Card");
        english.put("amount_received", "Amount Received:");
        english.put("change", "Change:");
        english.put("confirm_payment", "Confirm Payment");
        english.put("insufficient_payment", "Insufficient payment amount!");
        english.put("payment_error", "Payment Error");
        english.put("payment_success", "Payment successful!");
        english.put("payment_complete", "Payment Complete");
        english.put("membership_discount_title", "Membership Discount");
        english.put("check_member", "Check Member");
        english.put("no_member_selected", "No member selected");
        english.put("member_not_found", "Member not found");
        english.put("original_amount", "Original Amount:");
        english.put("membership_discount_label", "Membership Discount:");
        english.put("final_amount", "Final Amount:");
        english.put("label_member", "Member");
        
        // --- 8. Receipt Content (Standardized Keys) ---
        english.put("receipt_header", "SALES RECEIPT");
        english.put("receipt_cancelled", "[ ORDER CANCELLED ]");
        english.put("receipt_order_id", "Order ID");
        english.put("receipt_date", "Date");
        english.put("receipt_col_item", "Item");
        english.put("receipt_col_qty", "Qty");
        english.put("receipt_col_price", "Price");
        english.put("receipt_total_amount", "Total Amount");
        english.put("receipt_payment_method", "Payment Method");
        english.put("receipt_received", "Received");
        english.put("receipt_change", "Change");
        english.put("receipt_footer", "Thank you for visiting!");
        
        // Messages
        english.put("msg_order_cancelled", "Order Cancelled.");
        english.put("title_notice", "Notice");
    }
    
    private static void initKorean() {
        // --- Main & Tabs ---
        korean.put("app_title", "카페 POS 시스템");
        korean.put("tab_order", "📋 주문");
        korean.put("tab_menu", "☕ 메뉴 관리");
        korean.put("tab_membership", "👥 회원 관리");
        korean.put("tab_sales", "📊 매출 통계");
        
        // Membership View
        korean.put("members_list", "회원 목록");
        korean.put("search", "검색");
        korean.put("refresh", "새로고침");
        korean.put("total_members", "총 회원 수");
        korean.put("phone_number", "전화번호");
        korean.put("name", "이름");
        korean.put("total_spent", "총 사용 금액");
        korean.put("level", "등급");
        korean.put("discount", "할인율");
        korean.put("to_next_level", "다음 등급까지");
        korean.put("add_edit_member", "회원 추가/수정");
        korean.put("add_member", "회원 추가");
        korean.put("update", "수정");
        korean.put("delete", "삭제");
        korean.put("clear_form", "입력 초기화");
        korean.put("max_level", "최고 등급");
        
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
        korean.put("buy", "구매");
        korean.put("sold_out", "품절");
        korean.put("per_unit", "개당");
        
        // --- Order Customization ---
        korean.put("customize_order", "주문 옵션");
        korean.put("temperature", "온도");
        korean.put("ice", "아이스");
        korean.put("hot", "따뜻하게");
        korean.put("quantity", "수량");
        korean.put("extras", "추가 옵션");
        korean.put("add_shot", "샷 추가");
        korean.put("whipping_cream", "휘핑 크림");
        korean.put("order_type", "주문 포장");
        korean.put("take_out", "테이크아웃");
        korean.put("in_store", "매장");
        korean.put("item_details", "상품 정보");
        korean.put("cancel", "취소");
        korean.put("add_to_order", "주문 담기");
        
        // --- Menu Management ---
        korean.put("menu_items_list", "메뉴 목록");
        korean.put("add_edit_item", "메뉴 추가/수정");
        korean.put("id", "ID");
        korean.put("name", "이름");
        korean.put("description", "설명");
        korean.put("available", "판매 가능");
        korean.put("name_label", "이름:");
        korean.put("category_label", "카테고리:");
        korean.put("price_label", "가격:");
        korean.put("description_label", "설명:");
        korean.put("image", "이미지:");
        korean.put("add_new_item", "새 항목 추가");
        korean.put("update_item", "항목 수정");
        korean.put("delete_item", "항목 삭제");
        korean.put("clear_form", "초기화");
        korean.put("confirm_delete", "정말 삭제하시겠습니까?");
        korean.put("confirm_delete_title", "삭제 확인");
        korean.put("name_empty", "이름을 입력해주세요");
        korean.put("price_positive", "가격은 양수여야 합니다");
        korean.put("invalid_price_format", "잘못된 가격 형식입니다");
        korean.put("item_added", "메뉴가 추가되었습니다");
        korean.put("item_updated", "메뉴가 수정되었습니다");
        korean.put("item_deleted", "메뉴가 삭제되었습니다");
        korean.put("success", "성공");
        korean.put("error", "오류");
        korean.put("validation_error", "입력 오류");
        
        // --- Membership ---
        korean.put("members_list", "회원 목록");
        korean.put("add_edit_member", "회원 추가/수정");
        korean.put("search", "검색");
        korean.put("refresh", "새로고침");
        korean.put("total_members", "총 회원 수");
        korean.put("phone_number", "전화번호");
        korean.put("total_spent", "총 구매액");
        korean.put("level", "등급");
        korean.put("to_next_level", "다음 등급까지");
        korean.put("add_member", "회원 추가");
        korean.put("update", "수정");
        korean.put("delete", "삭제");
        korean.put("max_level", "최고 등급");
        korean.put("membership_info", "<html><b>회원 등급:</b> L5(0%), L4(5%>50만), L3(10%>100만), L2(15%>200만), L1(20%>300만)</html>");
        
        // --- Sales View ---
        korean.put("total_revenue", "총 수익");
        korean.put("monthly_revenue", "월간 수익");
        korean.put("today_sales", "오늘의 매출");
        korean.put("today_orders", "오늘의 주문");
        korean.put("recent_transactions", "오늘의 거래");
        korean.put("cancelled_history", "취소 내역");
        korean.put("title_order_search", "주문 검색");
        korean.put("label_year", "년:");
        korean.put("label_month", "월:");
        korean.put("label_day", "일:");
        korean.put("label_price", "금액:");
        korean.put("btn_search", "검색");
        korean.put("time", "시간");
        korean.put("order_id", "주문 ID");
        korean.put("amount", "금액");
        korean.put("payment", "결제");
        korean.put("btn_print_receipt", "영수증 출력");
        korean.put("btn_cancel_order", "주문 취소");
        korean.put("btn_export_csv", "매출 CSV 내보내기");
        
        // --- Payment Dialog ---
        korean.put("process_payment", "결제 처리");
        korean.put("total_amount", "총 금액:");
        korean.put("payment_method", "결제 수단:");
        korean.put("cash", "현금");
        korean.put("card", "카드");
        korean.put("amount_received", "받은 금액:");
        korean.put("change", "거스름돈:");
        korean.put("confirm_payment", "결제 확인");
        korean.put("insufficient_payment", "결제 금액이 부족합니다!");
        korean.put("payment_error", "결제 오류");
        korean.put("payment_success", "결제 성공!");
        korean.put("payment_complete", "결제 완료");
        korean.put("membership_discount_title", "멤버십 할인");
        korean.put("check_member", "회원 조회");
        korean.put("no_member_selected", "선택된 회원 없음");
        korean.put("member_not_found", "회원을 찾을 수 없습니다");
        korean.put("original_amount", "주문 금액:");
        korean.put("membership_discount_label", "멤버십 할인:");
        korean.put("final_amount", "최종 결제 금액:");
        korean.put("label_member", "회원");
        
        // --- Receipt Content (Standardized Keys) ---
        korean.put("receipt_header", "매출 영수증");
        korean.put("receipt_cancelled", "[ 주문 취소됨 ]");
        korean.put("receipt_order_id", "주문 ID");
        korean.put("receipt_date", "주문일시");
        korean.put("receipt_col_item", "상품명");
        korean.put("receipt_col_qty", "수량");
        korean.put("receipt_col_price", "금액");
        korean.put("receipt_total_amount", "총 결제 금액");
        korean.put("receipt_payment_method", "결제 수단");
        korean.put("receipt_received", "받은 금액");
        korean.put("receipt_change", "거스름돈");
        korean.put("receipt_footer", "방문해 주셔서 감사합니다");
        
        // Messages
        korean.put("msg_order_cancelled", "주문이 취소되었습니다.");
        korean.put("title_notice", "알림");
    }
    
    public static String getEnglish(String key) {
        return english.getOrDefault(key, key);
    }
    
    public static String getKorean(String key) {
        return korean.getOrDefault(key, key);
    }
}