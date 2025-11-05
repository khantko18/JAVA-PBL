# 🚀 Quick Start Guide - Your Setup

## ✅ What's Already Done

✔️ JDBC driver connected: `mysql-connector-j-9.4.0.jar`  
✔️ Database name set: `kkkdb`  
✔️ MySQL password configured: `123abcABC%`  
✔️ Project compiled successfully  
✔️ All scripts created

---

## 📋 3 Simple Steps to Run

### Step 1: Create Database (First Time Only)

Open your terminal and run:

```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project"
./setup-database.sh
```

This will:

- Create database `kkkdb`
- Create 4 tables (menu_items, orders, order_items, payments)
- Load 6 sample menu items

**OR** if the script doesn't work, run manually:

```bash
mysql -u root -p123abcABC% < database_schema.sql
```

---

### Step 2: Verify Database (Optional)

```bash
mysql -u root -p123abcABC% kkkdb -e "SHOW TABLES;"
mysql -u root -p123abcABC% kkkdb -e "SELECT * FROM menu_items;"
```

You should see 4 tables and 6 menu items!

---

### Step 3: Run the Application

```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project"
./run.sh
```

**OR** run directly:

```bash
java -cp "bin:lib/mysql-connector-j-9.4.0.jar" POSApplication
```

---

## ✅ Expected Output

When working correctly, you'll see:

```
✅ Database connected successfully!
✅ Loaded 6 menu items from database
===========================================
   Cafe POS System Started Successfully!
===========================================
MVC Architecture Components:
✓ Models: MenuItem, Order, Payment, SalesData
✓ Views: OrderView, MenuManagementView, SalesView
✓ Controllers: MenuController, OrderController, SalesController
===========================================
```

---

## 🎯 Using the Application

### Make a Test Order

1. **Order Tab** - Add items (Americano, Latte, etc.)
2. Click **"Proceed to Payment"**
3. Choose Cash/Card
4. Click **"Confirm Payment"**
5. ✅ Order saved to database!

### Export Sales Report

1. Go to **"Sales Statistics"** tab
2. Click **"📄 Export Sales to CSV"**
3. Choose save location
4. Open CSV in Excel/Numbers

### Manage Menu

1. Go to **"Menu Management"** tab
2. Add/Edit/Delete items
3. ✅ Changes saved to database!

---

## 🔧 Useful Scripts

```bash
# Compile project
./compile.sh

# Run application
./run.sh

# Setup database
./setup-database.sh
```

---

## 📊 Check Your Data in MySQL

```bash
# Connect to database
mysql -u root -p123abcABC% kkkdb

# View all tables
SHOW TABLES;

# View menu items
SELECT * FROM menu_items;

# View today's sales
SELECT * FROM payments WHERE payment_date = CURDATE();

# View total revenue
SELECT SUM(amount) as total FROM payments;

# Exit
exit
```

---

## 🐛 Troubleshooting

### Problem: "Database connection failed"

**Solution:**

```bash
# Check MySQL is running
brew services list

# Start MySQL if not running
brew services start mysql
```

### Problem: "Access denied"

**Solution:**

- Password might be wrong
- Try: `mysql -u root -p` (enter password when prompted)

### Problem: "Unknown database 'kkkdb'"

**Solution:**

```bash
# Run database setup
./setup-database.sh
```

### Problem: Application won't start

**Solution:**

```bash
# Recompile
./compile.sh

# Try running again
./run.sh
```

---

## 📁 Your Project Structure

```
PBL Project/
├── 🚀 run.sh                    ← Run this!
├── 🔨 compile.sh                ← Compile project
├── 🗄️  setup-database.sh        ← Setup database
├── START_HERE.md               ← This file
├── database_schema.sql         ← Database structure
├── lib/
│   └── mysql-connector-j-9.4.0.jar  ← JDBC driver ✓
├── bin/                        ← Compiled classes
└── src/                        ← Source code
    ├── database/               ← Database classes
    ├── model/
    ├── view/
    ├── controller/
    └── util/
```

---

## 🎉 You're All Set!

**Configuration:**

- ✅ Database: `kkkdb`
- ✅ User: `root`
- ✅ Password: `123abcABC%`
- ✅ JDBC: Connected
- ✅ Compiled: Ready

**Just run:**

```bash
./setup-database.sh    # First time only
./run.sh              # Every time
```

---

## 📚 More Help

- **Complete Guide:** `DATABASE_SETUP_GUIDE.md`
- **Quick Start:** `DATABASE_QUICK_START.md`
- **Features:** `DATABASE_FEATURES_SUMMARY.md`
- **Setup:** `SETUP_INSTRUCTIONS.txt`

---

**Have fun with your POS system! 🎊**
