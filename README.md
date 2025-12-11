# ☕ Cafe POS System - Java Project

A comprehensive Point of Sale (POS) system for cafes built with Java Swing and MySQL database.

## 🎯 Features

- ✅ **Complete MVC Architecture** - Model-View-Controller pattern
- ✅ **MySQL Database Integration** - Persistent data storage
- ✅ **Menu Management** - Add, edit, delete menu items with images
- ✅ **Order Processing** - Customizable order system with BUY button
- ✅ **Payment Handling** - Cash and card payment support
- ✅ **Sales Statistics** - Revenue tracking and analytics
- ✅ **Multi-language Support** - English and Korean (🇺🇸/🇰🇷)
- ✅ **Image Support** - Menu items with images
- ✅ **Auto-refresh** - Real-time UI updates
- ✅ **Order Customization** - Temperature, quantity, order type, extras

## 🏗️ Architecture

```
PBL Project Ver3/
├── src/
│   ├── POSApplication.java      # Main entry point
│   ├── controller/              # MVC Controllers
│   ├── model/                   # Data models
│   ├── view/                    # UI components
│   ├── database/                # Database DAOs
│   └── util/                    # Utilities
├── lib/
│   └── mysql-connector-j-9.4.0.jar  # MySQL JDBC driver
├── images/
│   └── menu_items/              # Menu item images
├── bin/                         # Compiled classes
└── database_schema.sql          # Database schema
```

## 🚀 Quick Start

### Prerequisites

- Java 11 or higher
- MySQL Server
- MySQL JDBC Driver (included in `lib/`)

### Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/khantko18/JAVA-PBL.git
   cd JAVA-PBL
   ```

2. **Set up the database:**
   ```bash
   mysql -u root -p < database_schema.sql
   ```

3. **Configure database connection:**
   Edit `src/database/DatabaseManager.java` with your MySQL credentials:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/kkkDB";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "your_password";
   ```

4. **Compile:**
   ```bash
   ./compile.sh
   ```

5. **Run:**
   ```bash
   ./run.sh
   ```

## 📖 Documentation

- [HOW_TO_RUN.md](HOW_TO_RUN.md) - Complete running guide
- [DATABASE_SETUP.md](DATABASE_SETUP.md) - Database setup instructions
- [IMAGE_FEATURE_GUIDE.md](IMAGE_FEATURE_GUIDE.md) - Image feature documentation
- [PERFORMANCE_OPTIMIZATION.md](PERFORMANCE_OPTIMIZATION.md) - Performance improvements

## 🗄️ Database Schema

The system uses MySQL with the following main tables:
- `menu_items` - Menu items with images
- `orders` - Order records
- `order_items` - Order details
- `payments` - Payment transactions

See `database_schema.sql` for complete schema.

## 🎨 Features in Detail

### Menu Management
- Add/Edit/Delete menu items
- Category management (Coffee, Beverage, Dessert)
- Image support for menu items
- Availability toggle (sold out)

### Order System
- Category filtering
- BUY button with customization dialog
- Temperature selection (ICE/HOT)
- Quantity selection
- Order type (Take Out/In Store)
- Extras for Coffee/Beverage (Add Shot, Whipping Cream)

### Payment System
- Cash payment with change calculation
- Card payment support
- Order completion and receipt

### Sales Analytics
- Total revenue tracking
- Today's sales
- Popular items
- CSV export functionality

## 🛠️ Development

### Compile
```bash
./compile.sh
```

### Run
```bash
./run.sh
```

### Run from Eclipse
- Right-click `POSApplication.java` → Run As → Java Application

### Run from VS Code
- Click ▶️ Run Code button

## 📦 Dependencies

- **MySQL Connector/J** 9.4.0 - Database connectivity
- **Java Swing** - UI framework (included in JDK)

## 🔧 Configuration

### Database
- Database: `kkkDB`
- Default user: `root`
- Configure in: `src/database/DatabaseManager.java`

### Images
- Image directory: `images/menu_items/`
- Supported formats: JPG, PNG, GIF
- Link images using: `./link_image.sh <item_id> <image_file>`

## 📝 Project Structure

```
src/
├── POSApplication.java          # Main application
├── controller/                  # Business logic
│   ├── MenuController.java
│   ├── OrderController.java
│   └── SalesController.java
├── model/                       # Data models
│   ├── MenuItem.java
│   ├── Order.java
│   └── Payment.java
├── view/                        # UI components
│   ├── MainView.java
│   ├── OrderView.java
│   └── MenuManagementView.java
├── database/                    # Data access
│   ├── DatabaseManager.java
│   ├── MenuItemDAO.java
│   ├── OrderDAO.java
│   └── PaymentDAO.java
└── util/                        # Utilities
    ├── LanguageManager.java
    └── LanguageResources.java
```

## 🎯 Key Technologies

- **Java 11** - Programming language
- **Java Swing** - GUI framework
- **MySQL** - Database management
- **JDBC** - Database connectivity
- **MVC Pattern** - Architecture pattern

## 📸 Screenshots

*Add screenshots of your application here*

## 🤝 Contributing

This is a PBL (Project-Based Learning) project. Contributions and suggestions are welcome!

## 📄 License

This project is for educational purposes.

## 👤 Author

**khantko18**
- GitHub: [@khantko18](https://github.com/khantko18)

## 🙏 Acknowledgments

- MySQL Community
- Java Swing Documentation
- All contributors and testers

---

**Built with ❤️ for Cafe Management**
