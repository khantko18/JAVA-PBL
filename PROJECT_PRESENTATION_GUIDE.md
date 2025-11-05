# Cafe POS System - Project Presentation Guide

## 프로젝트 주요 기능 / Project Main Functions

---

## 1. 📋 주문 (Order) Module

| 주요기능<br>Main Function            | 설명<br>Description                                                                     | 비고<br>Remarks                                          |
| ------------------------------------ | --------------------------------------------------------------------------------------- | -------------------------------------------------------- |
| **메뉴 불러오기**<br>Load Menu Items | 카테고리별로 메뉴 항목을 불러와 화면에 표시<br>Display menu items by category on screen | 카드 형식으로 메뉴 표시<br>Menu displayed in card format |
| **카테고리 필터**<br>Category Filter | 전체, 커피, 음료, 디저트로 메뉴 필터링<br>Filter menu by All, Coffee, Beverage, Dessert | 실시간 필터링 지원<br>Real-time filtering supported      |
| **항목 추가**<br>Add to Order        | 선택한 메뉴를 주문 테이블에 추가<br>Add selected menu items to order table              | 수량 입력 다이얼로그<br>Quantity input dialog            |
| **수량 조정**<br>Adjust Quantity     | 주문 항목의 수량을 조정<br>Adjust quantity of order items                               | 동일 항목 자동 합산<br>Auto-sum same items               |
| **할인 적용**<br>Apply Discount      | 주문에 할인율(%) 적용<br>Apply discount percentage to order                             | 0~100% 범위 검증<br>Validates 0-100% range               |
| **가격 계산**<br>Calculate Price     | 소계, 할인, 합계 자동 계산<br>Auto-calculate subtotal, discount, total                  | 실시간 업데이트<br>Real-time updates                     |
| **통화 표시**<br>Currency Display    | 언어에 따라 $ 또는 ₩ 표시<br>Display $ or ₩ based on language                           | 환율 자동 변환 (1:1200)<br>Auto exchange rate (1:1200)   |
| **주문 취소**<br>Clear Order         | 현재 주문 전체 초기화<br>Clear entire current order                                     | 확인 다이얼로그 표시<br>Shows confirmation dialog        |
| **결제 진행**<br>Proceed to Payment  | 결제 창으로 이동<br>Navigate to payment window                                          | 빈 주문 검증<br>Validates non-empty order                |

---

## 2. ☕ 메뉴 관리 (Menu Management) Module

| 주요기능<br>Main Function                | 설명<br>Description                                                             | 비고<br>Remarks                                                                             |
| ---------------------------------------- | ------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------- |
| **메뉴 목록 표시**<br>Display Menu List  | 전체 메뉴 항목을 테이블에 표시<br>Display all menu items in table               | ID, 이름, 카테고리, 가격, 설명, 가용성<br>ID, Name, Category, Price, Description, Available |
| **항목 선택**<br>Select Item             | 테이블에서 항목 선택 시 폼에 로드<br>Load item to form when selected from table | 단일 선택 모드<br>Single selection mode                                                     |
| **새 항목 추가**<br>Add New Item         | 새로운 메뉴 항목 생성<br>Create new menu item                                   | 자동 ID 생성 (M001, M002...)<br>Auto ID generation (M001, M002...)                          |
| **항목 수정**<br>Update Item             | 선택한 항목의 정보 수정<br>Update selected item information                     | 선택 시 활성화<br>Enabled when item selected                                                |
| **항목 삭제**<br>Delete Item             | 선택한 항목 삭제<br>Delete selected item                                        | 삭제 확인 메시지<br>Deletion confirmation message                                           |
| **폼 초기화**<br>Clear Form              | 입력 폼의 모든 필드 초기화<br>Clear all fields in input form                    | 버튼 상태 리셋<br>Reset button states                                                       |
| **유효성 검사**<br>Validation            | 이름 필수, 가격 양수 검증<br>Validate required name, positive price             | 에러 메시지 표시<br>Display error messages                                                  |
| **카테고리 관리**<br>Category Management | 커피, 음료, 디저트, 음식 분류<br>Categorize as Coffee, Beverage, Dessert, Food  | 드롭다운 선택<br>Dropdown selection                                                         |
| **가격 형식**<br>Price Format            | 언어별 통화 형식 적용<br>Apply currency format per language                     | $ / ₩ 자동 변환<br>Auto $ / ₩ conversion                                                    |

---

## 3. 📊 매출 통계 (Sales Statistics) Module

| 주요기능<br>Main Function            | 설명<br>Description                                                       | 비고<br>Remarks                                                 |
| ------------------------------------ | ------------------------------------------------------------------------- | --------------------------------------------------------------- |
| **총 매출**<br>Total Revenue         | 전체 기간 누적 매출액 표시<br>Display cumulative revenue for all time     | 초록색 카드로 강조<br>Highlighted in green card                 |
| **오늘 매출**<br>Today's Sales       | 당일 매출액 실시간 표시<br>Display today's sales in real-time             | 파란색 카드로 표시<br>Displayed in blue card                    |
| **오늘 주문 수**<br>Today's Orders   | 당일 처리된 주문 건수<br>Number of orders processed today                 | 노란색 카드로 표시<br>Displayed in yellow card                  |
| **거래 내역**<br>Transaction History | 최근 거래 목록 시간순 표시<br>Display recent transactions chronologically | 시간, 주문ID, 금액, 결제방식<br>Time, Order ID, Amount, Payment |
| **인기 메뉴**<br>Popular Items       | 판매량 기준 인기 항목 순위<br>Rank popular items by sales quantity        | 내림차순 정렬<br>Sorted in descending order                     |
| **자동 새로고침**<br>Auto Refresh    | 탭 전환 시 자동 데이터 갱신<br>Auto refresh data when tab changes         | 실시간 데이터<br>Real-time data                                 |
| **통화 변환**<br>Currency Conversion | 모든 금액을 선택 언어로 표시<br>Display all amounts in selected language  | $ ↔ ₩ 자동 변환<br>Auto $ ↔ ₩ conversion                        |
| **일별 집계**<br>Daily Summary       | 날짜별 매출 데이터 집계<br>Aggregate sales data by date                   | 데이터베이스 구조 활용<br>Utilizes data structure               |

---

## 4. 💳 결제 (Payment) Module

| 주요기능<br>Main Function               | 설명<br>Description                                                      | 비고<br>Remarks                                                  |
| --------------------------------------- | ------------------------------------------------------------------------ | ---------------------------------------------------------------- |
| **결제 다이얼로그**<br>Payment Dialog   | 모달 창으로 결제 화면 표시<br>Display payment screen as modal window     | 주문 확정 전 마지막 단계<br>Final step before order confirmation |
| **결제 방식 선택**<br>Payment Method    | 현금/카드 선택<br>Select Cash/Card                                       | 라디오 버튼으로 선택<br>Radio button selection                   |
| **받은 금액**<br>Amount Received        | 현금 결제 시 받은 금액 입력<br>Enter amount received for cash payment    | 현금 선택 시만 활성화<br>Enabled only for cash                   |
| **거스름돈 계산**<br>Change Calculation | 거스름돈 자동 계산 및 표시<br>Auto-calculate and display change          | 실시간 계산<br>Real-time calculation                             |
| **금액 검증**<br>Amount Validation      | 받은 금액이 합계보다 많은지 검증<br>Validate received ≥ total amount     | 부족 시 에러 메시지<br>Error if insufficient                     |
| **결제 완료**<br>Complete Payment       | 결제 처리 및 영수증 정보 표시<br>Process payment and show receipt info   | 주문ID, 금액, 거스름돈<br>Order ID, amount, change               |
| **주문 기록**<br>Record Order           | 매출 데이터에 주문 저장<br>Save order to sales data                      | 통계에 자동 반영<br>Auto-reflected in statistics                 |
| **새 주문 시작**<br>Start New Order     | 결제 후 새 주문 화면 초기화<br>Initialize new order screen after payment | 자동 주문ID 생성<br>Auto order ID generation                     |

---

## 5. 🌐 다국어 지원 (Multilingual Support)

| 주요기능<br>Main Function                 | 설명<br>Description                                                        | 비고<br>Remarks                                  |
| ----------------------------------------- | -------------------------------------------------------------------------- | ------------------------------------------------ |
| **언어 전환**<br>Language Toggle          | English ↔ 한국어 즉시 전환<br>Instant switch between English ↔ Korean      | 화면 상단 버튼<br>Buttons on top of screen       |
| **UI 번역**<br>UI Translation             | 모든 레이블, 버튼, 메시지 번역<br>Translate all labels, buttons, messages  | 200+ 번역 키<br>200+ translation keys            |
| **통화 변환**<br>Currency Conversion      | $ (달러) ↔ ₩ (원) 자동 변환<br>Auto $ (Dollar) ↔ ₩ (Won) conversion        | 환율 1:1200 적용<br>Exchange rate 1:1200         |
| **카테고리 번역**<br>Category Translation | Coffee↔커피, Beverage↔음료 등<br>Coffee↔커피, Beverage↔음료, etc.          | 내부는 영어 저장<br>Internally stored in English |
| **옵저버 패턴**<br>Observer Pattern       | 언어 변경 시 모든 뷰 자동 갱신<br>Auto-update all views on language change | 리스너 패턴 사용<br>Uses listener pattern        |

---

## 6. 🏗️ MVC Architecture Implementation

| 구성요소<br>Component      | 역할<br>Role                                            | 비고<br>Remarks                                                         |
| -------------------------- | ------------------------------------------------------- | ----------------------------------------------------------------------- |
| **Model**<br>모델          | MenuItem, Order, Payment, SalesData                     | 비즈니스 로직 및 데이터<br>Business logic and data                      |
| **View**<br>뷰             | OrderView, MenuManagementView, SalesView, PaymentDialog | Swing GUI 컴포넌트<br>Swing GUI components                              |
| **Controller**<br>컨트롤러 | MenuController, OrderController, SalesController        | 모델-뷰 연결 및 이벤트 처리<br>Model-View connection and event handling |
| **Utility**<br>유틸리티    | LanguageManager, LanguageResources                      | 다국어 지원 시스템<br>Multilingual support system                       |

---

## 7. 🎯 Key Technologies & Features

### Core Technologies

- **Programming Language**: Java 11
- **GUI Framework**: Swing
- **Design Pattern**: MVC (Model-View-Controller)
- **Architecture Pattern**: Observer Pattern for language switching

### Key Features

1. ✅ **Real-time Updates**: All calculations and displays update instantly
2. ✅ **Input Validation**: Comprehensive validation for all user inputs
3. ✅ **Error Handling**: User-friendly error messages in both languages
4. ✅ **Responsive UI**: Modern card-based layout with proper spacing
5. ✅ **Data Persistence**: In-memory data storage during session
6. ✅ **Bilingual Support**: Complete English/Korean translation
7. ✅ **Currency Localization**: Automatic currency conversion and formatting

### User Experience

- 🎨 **Modern UI**: Clean, professional interface with color-coded elements
- 🌐 **Bilingual**: Seamless language switching without restart
- 💰 **Currency Support**: Localized currency display ($ / ₩)
- ⚡ **Fast**: Real-time calculations and instant updates
- 🔒 **Validation**: Prevents invalid inputs and operations
- 📊 **Visual Feedback**: Color-coded cards and status indicators

---

## 8. 💡 Technical Highlights

### Code Quality

```
✓ Clean Code: Well-organized package structure
✓ Documentation: Comprehensive inline comments
✓ Naming: Clear, descriptive variable and method names
✓ Error Handling: Try-catch blocks throughout
✓ Validation: Input validation at multiple levels
```

### Design Patterns Used

1. **MVC Pattern**: Separation of concerns
2. **Singleton Pattern**: LanguageManager instance
3. **Observer Pattern**: Language change notifications
4. **Factory Method**: MenuItem and Order creation

### Data Flow

```
User Input → View → Controller → Model → Controller → View → Display
```

---

## 9. 📝 Demo Scenario

### Typical Use Case:

1. **Customer arrives**: Staff opens Order tab
2. **Browse menu**: Filter by category (Coffee)
3. **Add items**: Click "Add" on Cappuccino, enter quantity 2
4. **Add more**: Switch to Dessert, add Chocolate Cake
5. **Apply discount**: Enter 10% discount
6. **View total**: Check calculated total with discount
7. **Process payment**: Click "Proceed to Payment"
8. **Select method**: Choose Cash payment
9. **Enter amount**: Input received amount
10. **Complete**: Confirm payment, print receipt info
11. **Check stats**: Switch to Sales Statistics to see updated data

---

## 10. 🌟 Project Strengths

| Strength                | Description                                          |
| ----------------------- | ---------------------------------------------------- |
| **Professional Design** | Modern, intuitive UI suitable for real business use  |
| **Complete MVC**        | Proper separation of Model, View, and Controller     |
| **Bilingual**           | Full English/Korean support with instant switching   |
| **Robust Validation**   | Comprehensive input validation and error handling    |
| **Real-time Updates**   | All data updates instantly across the application    |
| **Scalable**            | Easy to add new features, languages, or menu items   |
| **User-Friendly**       | Clear feedback, confirmation dialogs, error messages |
| **Well-Documented**     | Code comments, README, and presentation guides       |

---

## 📌 Presentation Tips

### For Demonstration:

1. **Start with English**: Show the default interface
2. **Switch to Korean**: Demonstrate language toggle feature
3. **Process an order**: Walk through complete order flow
4. **Show all tabs**: Demonstrate Order → Menu Management → Sales
5. **Highlight MVC**: Explain how components interact
6. **Show error handling**: Demonstrate validation (negative price, etc.)
7. **Explain currency**: Show $ to ₩ conversion

### Key Points to Emphasize:

- ✨ **Complete MVC implementation**
- ✨ **Professional bilingual support**
- ✨ **Real-world applicable features**
- ✨ **Clean, maintainable code**
- ✨ **User-friendly interface**
- ✨ **Comprehensive error handling**

---

## 📊 Project Statistics

- **Total Classes**: 15+ classes
- **Lines of Code**: 2,500+ lines
- **Translation Keys**: 200+ bilingual entries
- **Features**: 30+ main features
- **Design Patterns**: 4 patterns implemented
- **Languages Supported**: 2 (English, Korean)
- **Modules**: 4 main modules (Order, Menu, Sales, Payment)

---

**Developed with**: Java 11, Swing, MVC Architecture  
**Target**: Cafe/Restaurant POS System  
**Status**: Fully Functional ✅
