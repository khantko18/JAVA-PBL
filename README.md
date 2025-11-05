## testing git

# Cafe POS System

A comprehensive Point of Sale (POS) system for cafes built with Java Swing using **MVC Architecture**.

## Project Overview

This POS system provides a complete solution for managing cafe operations including menu management, order processing, payment handling, and sales analytics.

## Features

### 🛠️ Core Features

1. **Menu Management**

   - Add, edit, and delete menu items
   - Categorize items (Coffee, Beverage, Dessert, Food)
   - Set prices and descriptions
   - Card-based display format

2. **Order Processing**

   - Browse menu items by category
   - Add items to cart with quantities
   - Apply discounts (percentage-based)
   - View real-time order totals
   - Clear orders

3. **Payment System**

   - Cash payment with change calculation
   - Card payment
   - Payment confirmation dialog
   - Receipt information

4. **Sales Analytics**
   - Total revenue tracking
   - Daily sales statistics
   - Order count tracking
   - Popular items analysis
   - Recent transaction history

## MVC Architecture

### 📦 Model (Data & Business Logic)

- `MenuItem.java` - Represents menu items
- `MenuManager.java` - Manages menu item collection
- `Order.java` - Represents customer orders
- `OrderItem.java` - Individual items in an order
- `Payment.java` - Payment transactions
- `SalesData.java` - Sales statistics and tracking

### 🎨 View (User Interface)

- `MainView.java` - Main application window with tabs
- `OrderView.java` - Order processing interface
- `MenuManagementView.java` - Menu management interface
- `SalesView.java` - Sales statistics dashboard
- `PaymentDialog.java` - Payment processing dialog

### 🎮 Controller (Business Logic)

- `MenuController.java` - Handles menu management operations
- `OrderController.java` - Handles order processing and payment
- `SalesController.java` - Handles sales statistics updates

## Technical Implementation

### Java Features Used

✅ **Object-Oriented Programming**

- Encapsulation (private fields, public getters/setters)
- Inheritance and Polymorphism
- Access modifiers (public, private)

✅ **Collections Framework**

- `ArrayList` for dynamic lists
- `HashMap` for key-value storage
- `List` interface for flexibility
- `Map` interface for data structures

✅ **Exception Handling**

- Try-catch blocks for input validation
- NumberFormatException handling
- IllegalArgumentException for business rules

✅ **Java 8+ Features**

- Streams API for data processing
- Lambda expressions for event handling
- LocalDateTime for timestamps
- Enums for payment methods

✅ **GUI Programming**

- Swing components (JFrame, JPanel, JTable, etc.)
- Layout managers (BorderLayout, GridLayout, FlowLayout)
- Event listeners
- Custom component styling

## Project Structure

```
PBL Project/
├── src/
│   ├── model/                  # Model layer
│   │   ├── MenuItem.java
│   │   ├── MenuManager.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── Payment.java
│   │   └── SalesData.java
│   ├── view/                   # View layer
│   │   ├── MainView.java
│   │   ├── OrderView.java
│   │   ├── MenuManagementView.java
│   │   ├── SalesView.java
│   │   └── PaymentDialog.java
│   ├── controller/             # Controller layer
│   │   ├── MenuController.java
│   │   ├── OrderController.java
│   │   └── SalesController.java
│   └── POSApplication.java     # Main entry point
└── bin/                        # Compiled classes
```

## How to Run

### Compile the Project

```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project"
javac -d bin src/model/*.java src/view/*.java src/controller/*.java src/POSApplication.java
```

### Run the Application

```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project"
java -cp bin POSApplication
```

### Or Run in Eclipse

1. Right-click on `POSApplication.java`
2. Select "Run As" → "Java Application"

## Usage Guide

### 1. Processing Orders

1. Navigate to the "📋 Order" tab
2. Browse menu items or filter by category
3. Click "Add" on items to add to cart
4. Enter quantity when prompted
5. Apply discounts if needed
6. Click "Proceed to Payment"
7. Select payment method (Cash/Card)
8. For cash, enter amount received
9. Click "Confirm Payment"

### 2. Managing Menu

1. Navigate to the "☕ Menu Management" tab
2. View all menu items in the table
3. To add: Fill form and click "Add New Item"
4. To edit: Select item in table, modify form, click "Update Item"
5. To delete: Select item, click "Delete Item"

### 3. Viewing Sales

1. Navigate to the "📊 Sales Statistics" tab
2. View total revenue, today's sales, and order count
3. Check recent transactions
4. View popular items ranking

## Sample Menu Items

The application comes pre-loaded with sample items:

- **Coffee**: Americano ($3.50), Cappuccino ($4.00), Latte ($4.50)
- **Beverage**: Green Tea ($3.00)
- **Dessert**: Chocolate Cake ($5.50), Croissant ($3.50)

## Development Notes

### Design Patterns

- **MVC Pattern**: Separation of concerns
- **Observer Pattern**: Event-driven UI updates
- **Singleton-like**: Single MenuManager and SalesData instances

### Best Practices

- Consistent naming conventions
- Comprehensive comments and documentation
- Input validation and error handling
- User-friendly error messages
- Modern, clean UI design

## Requirements Met

✅ GUI design with Swing  
✅ MVC architecture  
✅ Exception handling (try-catch)  
✅ Collections (ArrayList, HashMap)  
✅ Access modifiers (public, private)  
✅ Control structures (if, for, switch)  
✅ Menu card display format  
✅ CRUD operations  
✅ Payment processing  
✅ Sales statistics

## Future Enhancements

- Database integration for persistent storage
- Print receipt functionality
- Employee management
- Inventory tracking
- Multi-language support
- Advanced reporting with charts

## Author

Created for PBL Project - Java GUI Programming Course

## License

Educational Project - Free to use and modify
