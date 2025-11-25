# Database Setup Guide 🗄️

## Your Database Configuration

```
Database Name: kkkDB
Host: localhost:3306
Username: root
Password: Khantkoko181$
```

---

## Option 1: Automatic Setup (Recommended)

### Run the setup script:

```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project Ver3"
./setup-database-manual.sh
```

This will:

- ✅ Test MySQL connection
- ✅ Create database `kkkDB`
- ✅ Create all tables
- ✅ Load sample menu items
- ✅ Verify setup

---

## Option 2: Manual Setup

### Step 1: Check MySQL is Running

```bash
# Check if MySQL is running
ps aux | grep mysql | grep -v grep

# Or if installed via Homebrew
brew services list | grep mysql

# Start MySQL if not running
brew services start mysql
```

### Step 2: Connect to MySQL

```bash
mysql -u root -p
# Enter password: Khantkoko181$
```

### Step 3: Create Database

```sql
CREATE DATABASE IF NOT EXISTS kkkDB;
USE kkkDB;
```

### Step 4: Run Schema Script

From your terminal (not in MySQL):

```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project Ver3"
mysql -u root -p kkkDB < database_schema.sql
# Enter password: Khantkoko181$
```

### Step 5: Verify Setup

```bash
mysql -u root -p kkkDB
# Enter password: Khantkoko181$
```

Then in MySQL:

```sql
-- Check tables
SHOW TABLES;

-- Should see:
-- +------------------+
-- | Tables_in_kkkDB  |
-- +------------------+
-- | menu_items       |
-- | order_items      |
-- | orders           |
-- | payments         |
-- +------------------+

-- Check sample data
SELECT * FROM menu_items;

-- Should see 6 items:
-- M001 - Americano
-- M002 - Cappuccino
-- M003 - Latte
-- M004 - Green Tea
-- M005 - Chocolate Cake
-- M006 - Croissant

EXIT;
```

---

## Option 3: Reset Password (if needed)

If you can't connect with your password:

### Method 1: Using MySQL Shell

```bash
sudo mysql -u root

# In MySQL:
ALTER USER 'root'@'localhost' IDENTIFIED BY 'Khantkoko181$';
FLUSH PRIVILEGES;
EXIT;
```

### Method 2: Using mysqladmin

```bash
mysqladmin -u root -p password 'Khantkoko181$'
# Enter old password
```

---

## Troubleshooting

### Issue 1: "Access denied for user 'root'@'localhost'"

**Solution:**

```bash
# Try connecting without password first
mysql -u root

# If that works, set the password:
ALTER USER 'root'@'localhost' IDENTIFIED BY 'Khantkoko181$';
FLUSH PRIVILEGES;
```

### Issue 2: "Can't connect to MySQL server"

**Solution:**

```bash
# Check if MySQL is running
brew services list

# Start MySQL
brew services start mysql

# Or if installed differently
sudo /usr/local/mysql/support-files/mysql.server start
```

### Issue 3: "Unknown database 'kkkDB'"

**Solution:**

```bash
mysql -u root -p
# In MySQL:
CREATE DATABASE kkkDB;
```

### Issue 4: Special characters in password

**Solution:**

- In terminal: Use single quotes `'Khantkoko181$'`
- In MySQL: Use double quotes `"Khantkoko181$"`
- In Java: Keep as is `Khantkoko181$`

---

## Quick Commands Reference

### Check MySQL Status

```bash
# macOS
brew services list | grep mysql
ps aux | grep mysql

# Start MySQL
brew services start mysql
```

### Connect to Database

```bash
mysql -u root -p'Khantkoko181$' kkkDB
```

### Show Databases

```sql
SHOW DATABASES;
```

### Show Tables

```sql
USE kkkDB;
SHOW TABLES;
```

### Count Records

```sql
SELECT COUNT(*) FROM menu_items;
SELECT COUNT(*) FROM orders;
SELECT COUNT(*) FROM payments;
```

### View Data

```sql
SELECT * FROM menu_items;
SELECT * FROM orders ORDER BY order_date DESC LIMIT 10;
SELECT * FROM payments ORDER BY payment_date DESC LIMIT 10;
```

---

## After Setup Complete

### Compile and Run Application

```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project Ver3"

# Compile
./compile.sh

# Run
./run.sh
```

### Expected Console Output

```
✅ Database connected successfully!
✅ Loaded 6 menu items from database (all set to available)
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

## Database Schema

### Tables

1. **menu_items** - Store all menu items

   - id (VARCHAR 10) - Primary key
   - name (VARCHAR 100)
   - category (VARCHAR 50)
   - price (DECIMAL 10,2)
   - description (TEXT)
   - available (BOOLEAN)

2. **orders** - Store order information

   - order_id (VARCHAR 20) - Primary key
   - order_date (DATE)
   - order_time (TIME)
   - subtotal (DECIMAL 10,2)
   - discount_percent (DECIMAL 5,2)
   - discount_amount (DECIMAL 10,2)
   - total_amount (DECIMAL 10,2)
   - status (VARCHAR 20)

3. **order_items** - Store items in each order

   - id (INT) - Auto increment
   - order_id (VARCHAR 20) - Foreign key
   - menu_item_id (VARCHAR 10) - Foreign key
   - menu_item_name (VARCHAR 100)
   - quantity (INT)
   - unit_price (DECIMAL 10,2)
   - subtotal (DECIMAL 10,2)

4. **payments** - Store payment transactions
   - payment_id (VARCHAR 20) - Primary key
   - order_id (VARCHAR 20) - Foreign key
   - payment_date (DATE)
   - payment_time (TIME)
   - payment_method (VARCHAR 20) - CASH or CARD
   - amount (DECIMAL 10,2)
   - received_amount (DECIMAL 10,2)
   - change_amount (DECIMAL 10,2)

### Sample Data (Auto-loaded)

| ID   | Name           | Category | Price |
| ---- | -------------- | -------- | ----- |
| M001 | Americano      | Coffee   | $3.50 |
| M002 | Cappuccino     | Coffee   | $4.00 |
| M003 | Latte          | Coffee   | $4.50 |
| M004 | Green Tea      | Beverage | $3.00 |
| M005 | Chocolate Cake | Dessert  | $5.50 |
| M006 | Croissant      | Dessert  | $3.50 |

---

## Security Note

⚠️ **Important:** Your database password is stored in the code:

- File: `src/database/DatabaseManager.java`
- Line 12: `private static final String DB_PASSWORD = "Khantkoko181$";`

For production, consider:

1. Using environment variables
2. Using a properties file (add to .gitignore)
3. Using a secrets manager

---

## Backup Your Database

### Create Backup

```bash
mysqldump -u root -p'Khantkoko181$' kkkDB > backup_$(date +%Y%m%d_%H%M%S).sql
```

### Restore Backup

```bash
mysql -u root -p'Khantkoko181$' kkkDB < backup_20231116_143022.sql
```

---

## Need Help?

If you encounter any issues:

1. Check MySQL is running
2. Verify password is correct
3. Try connecting manually: `mysql -u root -p`
4. Check the console output for error messages
5. Review the error logs: `/usr/local/mysql/data/mysqld.local.err`

---

**Setup complete! Your POS system is now connected to MySQL database! 🎉**
