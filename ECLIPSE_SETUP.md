# Eclipse IDE Setup Guide

## ✅ Quick Start (Project Already Configured!)

Your project is **ready to run** in Eclipse! All configuration files have been set up.

### Step 1: Open/Import the Project

**If the project is not showing in Eclipse:**

1. In Eclipse, go to **File → Open Projects from File System...**
2. Click **Directory...** and select:
   ```
   /Users/khantkoko1999/eclipse-workspace/PBL Project Ver3
   ```
3. Click **Finish**

**OR, if you see it but it needs refresh:**

1. Right-click on "PBL Project Ver3" in Package Explorer
2. Select **Refresh** (or press F5)

---

### Step 2: Run the Application

**Option A: Right-click method**

1. In Package Explorer, expand **src**
2. Right-click on **POSApplication.java**
3. Select **Run As → Java Application**

**Option B: Toolbar button**

1. Open **POSApplication.java** in the editor
2. Click the green **▶️ Run** button in the toolbar

**Option C: Use the pre-configured launcher**

1. Go to **Run → Run Configurations...**
2. You should see "POSApplication" already configured
3. Click **Run**

---

## ✅ What's Already Configured

Your Eclipse project includes:

### 1. `.classpath` file

- ✅ Source folder: `src`
- ✅ Output folder: `bin`
- ✅ **MySQL JDBC Driver** (`lib/mysql-connector-j-9.4.0.jar`) is included
- ✅ JRE System Library configured

### 2. `.project` file

- ✅ Project name: "PBL Project Ver3"
- ✅ Java nature enabled
- ✅ Java builder configured

### 3. `.settings/` directory

- ✅ **Java Compiler**: Java 11
- ✅ **Encoding**: UTF-8 (no more encoding issues!)

### 4. `POSApplication.launch`

- ✅ Pre-configured run configuration
- ✅ Main class: POSApplication
- ✅ Classpath includes MySQL driver

---

## 🎯 Expected Output

When you run the application, you should see:

```
✅ Database connected successfully!
✅ Loaded 6 menu items from database (all set to available)
===========================================
  Cafe POS System Started Successfully!
===========================================
MVC Architecture Components:
✓ Models: MenuItem, Order, Payment, SalesData
✓ Views: OrderView, MenuManagementView, SalesView
✓ Controllers: MenuController, OrderController, SalesController
===========================================
```

And the GUI window will open!

---

## 🔧 Troubleshooting

### Problem: "MySQL JDBC Driver not found"

**This should NOT happen** with the current setup, but if it does:

1. Right-click on project → **Properties**
2. Go to **Java Build Path → Libraries** tab
3. Look for `mysql-connector-j-9.4.0.jar`
4. If it's missing or shows an error:
   - Click **Remove** (if present)
   - Click **Add JARs...** (NOT "Add External JARs")
   - Navigate to `lib` folder in your project
   - Select `mysql-connector-j-9.4.0.jar`
   - Click **OK**

### Problem: "Access denied for user 'root'@'localhost'"

Your MySQL credentials might be incorrect. Check:

- File: `src/database/DatabaseManager.java`
- Line 12: Password should be `Khantkoko18$`
- Line 11: Username should be `root`
- Line 10: Database should be `kkkDB`

### Problem: Build errors or red X marks

1. **Refresh the project**:

   - Right-click project → **Refresh** (F5)

2. **Clean and rebuild**:

   - **Project → Clean...**
   - Select "PBL Project Ver3"
   - Check "Start a build immediately"
   - Click **Clean**

3. **Check Java version**:
   - Right-click project → **Properties**
   - **Java Compiler**
   - Should be set to **Java 11**

### Problem: Korean characters show as ???

This should be fixed with UTF-8 encoding, but if not:

1. Right-click project → **Properties**
2. **Resource → Text file encoding**
3. Select **Other: UTF-8**
4. Click **Apply and Close**

---

## 🎨 Eclipse Tips

### View Console Output

- **Window → Show View → Console**
- The database connection messages and debug output appear here

### Enable Auto-Build

- **Project → Build Automatically** (should be checked)
- Changes to code will automatically recompile

### Format Code

- Select code → **Source → Format** (Ctrl+Shift+F / Cmd+Shift+F)

### Organize Imports

- In any Java file → **Source → Organize Imports** (Ctrl+Shift+O / Cmd+Shift+O)

---

## 📦 What's in the MySQL Driver JAR?

The `lib/mysql-connector-j-9.4.0.jar` file contains:

- MySQL JDBC driver classes
- Connection pooling support
- SSL/TLS security features
- Compatibility with MySQL 5.7+

**You don't need to download or install anything else!**

---

## 🚀 Next Steps

1. **Run the application** using one of the methods above
2. **Test database features**:
   - Add a new menu item in "Menu Management"
   - Check console for: `✅ Menu item saved to database`
3. **Try the BUY button** in the Order view
4. **Switch languages** using the 🇺🇸/🇰🇷 button

---

## 📚 Additional Resources

- Main documentation: `HOW_TO_RUN.md`
- Database setup: `DATABASE_SETUP.md`
- Project summary: Check the main README files in the project root

---

**Happy coding! If you have any issues, check the troubleshooting sections or re-run the setup.**
