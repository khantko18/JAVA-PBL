# Database Setup Guide 🗄️

## Overview

This guide will help you set up MySQL database integration for the Cafe POS System.

---

## 📋 Prerequisites

### 1. MySQL Server Installation

**For macOS:**

```bash
# Install MySQL using Homebrew
brew install mysql

# Start MySQL service
brew services start mysql

# Secure MySQL installation (optional but recommended)
mysql_secure_installation
```

**For Windows:**

- Download MySQL Installer from [MySQL Official Website](https://dev.mysql.com/downloads/installer/)
- Run the installer and follow the setup wizard
- Choose "Developer Default" installation type

**For Linux (Ubuntu/Debian):**

```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
```

### 2. Verify MySQL Installation

```bash
mysql --version
# Should output: mysql  Ver X.X.X for ...
```

---

## 🔧 Database Setup

### Step 1: Login to MySQL

```bash
mysql -u root -p
# Enter your MySQL root password (press Enter if no password)
```

### Step 2: Create Database

From the project root directory, run the SQL script:

```bash
# Option 1: From terminal
mysql -u root -p < database_schema.sql

# Option 2: From MySQL shell
mysql> source /path/to/project/database_schema.sql
```

Or manually execute:

```sql
CREATE DATABASE IF NOT EXISTS khantkoko;
USE khantkoko;
-- Then run all commands from database_schema.sql
```

### Step 3: Verify Database Creation

```sql
SHOW DATABASES;
USE khantkoko;
SHOW TABLES;
```

You should see these tables:

- menu_items
- orders
- order_items
- payments

### Step 4: Check Sample Data

```sql
SELECT * FROM menu_items;
```

You should see 6 sample menu items (Americano, Cappuccino, Latte, etc.)

---

## 📦 JDBC Driver Installation

### Download MySQL Connector/J

1. **Download the MySQL JDBC Driver:**

   - Go to: https://dev.mysql.com/downloads/connector/j/
   - Download "Platform Independent" ZIP archive
   - Or use direct link: [MySQL Connector/J 8.0.33](https://dev.mysql.com/downloads/connector/j/)

2. **Extract the JAR file:**
   - Extract `mysql-connector-java-8.0.x.jar` from the ZIP
   - Note the file location

### Add to Project Classpath

#### Option 1: Eclipse IDE

1. Right-click on project → **Build Path** → **Configure Build Path**
2. Click **Libraries** tab → **Add External JARs**
3. Browse and select `mysql-connector-java-8.0.x.jar`
4. Click **Apply and Close**

#### Option 2: Command Line Compilation

```bash
# Compile with JDBC driver in classpath
javac -cp ".:mysql-connector-java-8.0.33.jar" -d bin src/**/*.java

# Run with JDBC driver in classpath
java -cp "bin:mysql-connector-java-8.0.33.jar" POSApplication
```

#### Option 3: Place JAR in lib directory

```bash
# Create lib directory in project root
mkdir lib
cp mysql-connector-java-8.0.x.jar lib/

# Compile and run
javac -cp ".:lib/*" -d bin src/**/*.java
java -cp "bin:lib/*" POSApplication
```

---

## ⚙️ Database Configuration

### Update Database Credentials

Edit `src/database/DatabaseManager.java`:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/khantkoko";
private static final String DB_USER = "root"; // Change to your MySQL username
private static final String DB_PASSWORD = ""; // Add your MySQL password here
```

**Common MySQL Users:**

- Default user: `root`
- Default password: (empty) or the password you set during installation

### Connection Properties

The application uses these MySQL connection properties:

- `useSSL=false` - Disables SSL for local development
- `serverTimezone=UTC` - Sets timezone to UTC
- `allowPublicKeyRetrieval=true` - Allows public key retrieval for authentication

---

## 🚀 Running the Application

### Compile the Project

```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project"

# Compile all Java files
javac -cp ".:lib/mysql-connector-java-8.0.33.jar" -d bin \
    src/database/*.java \
    src/util/*.java \
    src/model/*.java \
    src/view/*.java \
    src/controller/*.java \
    src/POSApplication.java
```

### Run the Application

```bash
java -cp "bin:lib/mysql-connector-java-8.0.33.jar" POSApplication
```

### Expected Console Output

```
✅ Database connected successfully!
✅ Loaded 6 menu items from database
==========================================
   Cafe POS System Started Successfully!
==========================================
MVC Architecture Components:
✓ Models: MenuItem, Order, Payment, SalesData
✓ Views: OrderView, MenuManagementView, SalesView
✓ Controllers: MenuController, OrderController, SalesController
==========================================
```

---

## 📊 CSV Export Feature

### Using CSV Export

1. **Open Sales Statistics Tab**
2. **Click "Export Sales to CSV" button**

   - Exports last 30 days of sales data
   - Includes: Date, Time, Order ID, Payment Method, Amount
   - Summary statistics included

3. **Click "Export Popular Items" button**
   - Exports item sales by quantity
   - Shows most to least popular items

### CSV File Location

- Files are saved to your chosen location via file dialog
- Default filename format:
  - `sales_report_YYYY-MM-DD.csv`
  - `popular_items_YYYY-MM-DD.csv`

### CSV Format Example

**sales_report_2025-10-24.csv:**

```csv
Date,Time,Order ID,Payment Method,Subtotal,Discount,Total Amount
2025-10-24,14:30:25,ORD001,CASH,15.50,1.55,13.95
2025-10-24,15:45:10,ORD002,CARD,8.00,0.00,8.00

SUMMARY
Total Orders,2
Total Revenue,21.95
Total Discount,1.55
Average Order Value,10.98

Exported on: 2025-10-24
```

---

## 🔍 Troubleshooting

### Issue: "Database connection failed"

**Solution 1: Check MySQL is running**

```bash
# macOS
brew services list
brew services start mysql

# Linux
sudo systemctl status mysql
sudo systemctl start mysql

# Windows
# Services → MySQL → Start
```

**Solution 2: Verify database exists**

```bash
mysql -u root -p
mysql> SHOW DATABASES;
mysql> USE khantkoko;
```

**Solution 3: Check credentials**

- Verify username and password in `DatabaseManager.java`
- Try connecting manually: `mysql -u root -p`

### Issue: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"

**Solution:**

- JDBC driver not in classpath
- Download and add `mysql-connector-java-8.0.x.jar`
- Verify with: `java -cp "bin:lib/*" POSApplication`

### Issue: "Access denied for user 'root'@'localhost'"

**Solution:**

```bash
# Reset MySQL root password
mysql -u root -p
mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY 'your_new_password';
mysql> FLUSH PRIVILEGES;
```

### Issue: "No menu items in database"

**Solution:**

```bash
# Re-run the database schema script
mysql -u root -p khantkoko < database_schema.sql
```

### Issue: "CSV export shows no data"

**Possible Causes:**

- No sales have been recorded yet
- Database connection failed
- No orders in the selected date range

**Solution:**

- Make at least one sale in the application
- Check console for database errors
- Verify data exists: `SELECT * FROM payments;`

---

## 🗃️ Database Schema Overview

### Tables Structure

#### 1. **menu_items**

Stores all cafe menu items

```sql
id VARCHAR(10) PRIMARY KEY
name VARCHAR(100)
category VARCHAR(50)
price DECIMAL(10, 2)
description TEXT
available BOOLEAN
created_at TIMESTAMP
updated_at TIMESTAMP
```

#### 2. **orders**

Stores order information

```sql
order_id VARCHAR(20) PRIMARY KEY
order_date DATE
order_time TIME
subtotal DECIMAL(10, 2)
discount_percent DECIMAL(5, 2)
discount_amount DECIMAL(10, 2)
total_amount DECIMAL(10, 2)
status VARCHAR(20)
created_at TIMESTAMP
```

#### 3. **order_items**

Stores items in each order

```sql
id INT AUTO_INCREMENT PRIMARY KEY
order_id VARCHAR(20) FK → orders(order_id)
menu_item_id VARCHAR(10) FK → menu_items(id)
menu_item_name VARCHAR(100)
quantity INT
unit_price DECIMAL(10, 2)
subtotal DECIMAL(10, 2)
```

#### 4. **payments**

Stores payment information

```sql
payment_id VARCHAR(20) PRIMARY KEY
order_id VARCHAR(20) FK → orders(order_id)
payment_date DATE
payment_time TIME
payment_method VARCHAR(20)
amount DECIMAL(10, 2)
received_amount DECIMAL(10, 2)
change_amount DECIMAL(10, 2)
created_at TIMESTAMP
```

### Views

#### **sales_summary**

Aggregated daily sales data

```sql
sale_date, total_orders, total_revenue,
average_order_value, cash_payments, card_payments
```

#### **popular_items**

Items ranked by sales quantity

```sql
menu_item_id, menu_item_name, category,
total_quantity_sold, total_revenue, order_count
```

---

## 📈 Database Queries (Useful for Testing)

### View Today's Sales

```sql
SELECT * FROM payments WHERE payment_date = CURDATE();
```

### View Total Revenue

```sql
SELECT SUM(amount) as total_revenue FROM payments;
```

### View Popular Items

```sql
SELECT * FROM popular_items;
```

### View Recent Orders

```sql
SELECT o.order_id, o.order_date, o.total_amount, p.payment_method
FROM orders o
JOIN payments p ON o.order_id = p.order_id
ORDER BY o.order_date DESC, o.order_time DESC
LIMIT 10;
```

### View Order Details

```sql
SELECT o.order_id, oi.menu_item_name, oi.quantity, oi.unit_price, oi.subtotal
FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id
WHERE o.order_id = 'ORD001';
```

---

## 🔒 Security Best Practices

1. **Never commit database passwords to Git**

   - Use environment variables
   - Create `.gitignore` entry for config files

2. **Use strong MySQL passwords**

   ```sql
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'StrongPassword123!';
   ```

3. **Create dedicated database user**

   ```sql
   CREATE USER 'pos_user'@'localhost' IDENTIFIED BY 'pos_password';
   GRANT ALL PRIVILEGES ON khantkoko.* TO 'pos_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

4. **Enable SSL for production**
   - Set `useSSL=true` in connection string
   - Configure SSL certificates

---

## 💾 Backup and Restore

### Backup Database

```bash
# Backup entire database
mysqldump -u root -p khantkoko > backup_$(date +%Y%m%d).sql

# Backup specific tables
mysqldump -u root -p khantkoko menu_items orders > menu_orders_backup.sql
```

### Restore Database

```bash
mysql -u root -p khantkoko < backup_20251024.sql
```

---

## 📞 Support

If you encounter any issues:

1. Check the console output for error messages
2. Verify MySQL service is running
3. Confirm JDBC driver is in classpath
4. Check database credentials
5. Review the troubleshooting section above

For more help, check:

- MySQL Documentation: https://dev.mysql.com/doc/
- JDBC Documentation: https://docs.oracle.com/javase/tutorial/jdbc/

---

## ✅ Success Checklist

- [ ] MySQL installed and running
- [ ] Database `khantkoko` created
- [ ] Tables created successfully
- [ ] Sample data loaded
- [ ] JDBC driver downloaded
- [ ] JDBC driver added to classpath
- [ ] Database credentials configured
- [ ] Application compiles without errors
- [ ] Application connects to database
- [ ] Menu items load from database
- [ ] Orders save to database
- [ ] CSV export works

---

**Congratulations! Your POS system is now fully integrated with MySQL database! 🎉**
