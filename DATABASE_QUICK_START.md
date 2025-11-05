# Database Quick Start Guide 🚀

## Fast Setup (5 Minutes)

### Step 1: Install MySQL

```bash
# macOS
brew install mysql
brew services start mysql

# Windows: Download from https://dev.mysql.com/downloads/installer/
# Linux
sudo apt install mysql-server
```

### Step 2: Create Database

```bash
mysql -u root -p < database_schema.sql
```

### Step 3: Download JDBC Driver

1. Download: https://dev.mysql.com/downloads/connector/j/
2. Extract `mysql-connector-java-8.0.x.jar`
3. Place in `lib/` directory

### Step 4: Update Database Password

Edit `src/database/DatabaseManager.java`:

```java
private static final String DB_PASSWORD = "your_password"; // Line 11
```

### Step 5: Compile & Run

```bash
# Compile
javac -cp ".:lib/*" -d bin src/**/*.java

# Run
java -cp "bin:lib/*" POSApplication
```

## ✅ Success Indicators

Console should show:

```
✅ Database connected successfully!
✅ Loaded 6 menu items from database
```

## 📊 Features Added

1. **Database Storage**

   - All menu items saved to MySQL
   - Orders and payments persisted
   - Data survives application restart

2. **CSV Export**
   - Export sales reports (last 30 days)
   - Export popular items ranking
   - Files include summary statistics

## 🔧 Troubleshooting

### Can't connect to database?

```bash
# Check MySQL is running
brew services list

# Verify database exists
mysql -u root -p
> SHOW DATABASES;
> USE khantkoko;
```

### JDBC Driver not found?

- Ensure JAR is in `lib/` directory
- Check compile command includes `-cp ".:lib/*"`

### No data showing?

- Make some sales first!
- Check console for database errors

## 📖 Full Documentation

See `DATABASE_SETUP_GUIDE.md` for complete setup instructions.

---

**That's it! Your POS system now has full database integration! 🎉**
