# Common Errors & Solutions 🔧

## ❌ Error: UnsupportedClassVersionError

### Error Message:

```
Error: LinkageError occurred while loading main class POSApplication
java.lang.UnsupportedClassVersionError: POSApplication has been compiled
by a more recent version of the Java Runtime (class file version 65.0),
this version of the Java Runtime only recognizes class file versions up to 55.0
```

### What This Means:

- Your class files were compiled with Java 21 (version 65.0)
- But you're trying to run with Java 11 (which only supports up to version 55.0)

### ✅ Solution:

**Option 1: Use the provided scripts (EASIEST)**

```bash
./compile.sh   # Recompile with Java 11
./run.sh       # Run with correct classpath
```

**Option 2: Clean and recompile manually**

```bash
# 1. Clean old class files
rm -rf bin/*

# 2. Recompile with Java 11 compatibility
javac -source 11 -target 11 -cp ".:lib/mysql-connector-j-9.4.0.jar" -d bin \
  src/database/*.java src/util/*.java src/model/*.java \
  src/view/*.java src/controller/*.java src/POSApplication.java

# 3. Run with JDBC driver in classpath
java -cp "bin:lib/mysql-connector-j-9.4.0.jar" POSApplication
```

---

## ❌ Error: Missing JDBC Driver

### Error Message:

```
ClassNotFoundException: com.mysql.cj.jdbc.Driver
```

### What This Means:

- The MySQL JDBC driver is not in your classpath

### ✅ Solution:

**Always include the JDBC driver when running:**

```bash
# ❌ WRONG (missing JDBC driver)
java -cp bin POSApplication

# ✅ CORRECT (includes JDBC driver)
java -cp "bin:lib/mysql-connector-j-9.4.0.jar" POSApplication

# ✅ OR use the script
./run.sh
```

---

## ❌ Error: Database Connection Failed

### Error Message:

```
Database connection failed!
Access denied for user 'root'@'localhost'
```

### What This Means:

- MySQL password is incorrect
- MySQL server is not running
- Database doesn't exist

### ✅ Solution:

**1. Check if MySQL is running:**

```bash
brew services list
brew services start mysql
```

**2. Verify password:**

```bash
# Try connecting manually
mysql -u root -p123abcABC%

# If it doesn't work, update the password in:
# src/database/DatabaseManager.java line 12
```

**3. Create database if missing:**

```bash
./setup-database.sh
# OR
mysql -u root -p123abcABC% < database_schema.sql
```

---

## ❌ Error: Unknown Database 'kkkdb'

### Error Message:

```
Unknown database 'kkkdb'
```

### What This Means:

- Database hasn't been created yet

### ✅ Solution:

```bash
# Run the setup script
./setup-database.sh

# OR manually
mysql -u root -p123abcABC% < database_schema.sql

# Verify it was created
mysql -u root -p123abcABC% -e "SHOW DATABASES;"
```

---

## ❌ Error: Table Doesn't Exist

### Error Message:

```
Table 'kkkdb.menu_items' doesn't exist
```

### What This Means:

- Database exists but tables weren't created

### ✅ Solution:

```bash
# Re-run the database schema
mysql -u root -p123abcABC% kkkdb < database_schema.sql

# Verify tables exist
mysql -u root -p123abcABC% kkkdb -e "SHOW TABLES;"
```

---

## ❌ GUI Window Not Opening

### Symptoms:

- Program runs but no window appears
- No error messages

### ✅ Solution:

**1. Check if it's already running:**

```bash
# Close any existing instances
ps aux | grep POSApplication
kill <process_id>
```

**2. Check for errors:**

```bash
# Run in foreground to see errors
java -cp "bin:lib/mysql-connector-j-9.4.0.jar" POSApplication
```

**3. Try recompiling:**

```bash
./compile.sh
./run.sh
```

---

## 🔍 How to Debug

### 1. Check Console Output

Look for these success messages:

```
✅ Database connected successfully!
✅ Loaded 6 menu items from database
```

### 2. Check Java Version

```bash
java -version
# Should show Java 11 or later
```

### 3. Check MySQL Status

```bash
brew services list
# MySQL should show "started"
```

### 4. Verify Database

```bash
mysql -u root -p123abcABC% kkkdb -e "SELECT COUNT(*) FROM menu_items;"
# Should show 6 items
```

---

## ✅ Quick Fix Checklist

When something goes wrong, try these steps:

1. **Clean and recompile:**

   ```bash
   ./compile.sh
   ```

2. **Setup database (if needed):**

   ```bash
   ./setup-database.sh
   ```

3. **Run application:**

   ```bash
   ./run.sh
   ```

4. **If still not working:**

   ```bash
   # Check MySQL
   brew services restart mysql

   # Verify database
   mysql -u root -p123abcABC% -e "SHOW DATABASES;"

   # Try manual run with debug output
   java -cp "bin:lib/mysql-connector-j-9.4.0.jar" POSApplication
   ```

---

## 📝 Important Notes

### Always Use These Commands:

✅ **Compile:**

```bash
javac -source 11 -target 11 -cp ".:lib/mysql-connector-j-9.4.0.jar" -d bin src/**/*.java
```

✅ **Run:**

```bash
java -cp "bin:lib/mysql-connector-j-9.4.0.jar" POSApplication
```

### Or Simply Use Scripts:

```bash
./compile.sh   # To compile
./run.sh       # To run
```

---

## 🆘 Still Having Issues?

1. **Read the startup guide:** `START_HERE.md`
2. **Check database setup:** `DATABASE_SETUP_GUIDE.md`
3. **Verify configuration:**

   - Database: `kkkdb`
   - User: `root`
   - Password: `123abcABC%`
   - JDBC: `lib/mysql-connector-j-9.4.0.jar`

4. **Test each component:**

   ```bash
   # Test MySQL connection
   mysql -u root -p123abcABC% -e "SELECT 1;"

   # Test database
   mysql -u root -p123abcABC% kkkdb -e "SHOW TABLES;"

   # Test JDBC driver
   ls -lh lib/mysql-connector-j-9.4.0.jar

   # Test compilation
   javac -version
   ```

---

**Remember: When in doubt, use the scripts!**

- `./compile.sh` - Always compiles correctly
- `./run.sh` - Always runs with proper classpath
- `./setup-database.sh` - Sets up database properly
