# How to Run the Cafe POS System

## ✅ Method 1: Eclipse IDE (Recommended for Eclipse Users)

Now you can run directly from **Eclipse**!

1. **Import Project** (if not already imported):

   - File → Open Projects from File System...
   - Select the "PBL Project Ver3" folder
   - Click Finish

2. **Run the Application**:

   - Right-click on `src/POSApplication.java`
   - Select **Run As → Java Application**

   OR

   - Click the **▶️ Run** button in the toolbar
   - The MySQL JDBC driver is automatically included!

**Configuration:** The `.classpath` and `.project` files are pre-configured with the MySQL driver.

---

## ✅ Method 2: VS Code Run Button (Recommended for VS Code Users)

Now you can use the **▶️ Run Code** button in VS Code!

1. Open `src/POSApplication.java`
2. Click the **▶️ Run Code** button (top right corner)
3. The application will automatically:
   - Compile all files with proper encoding
   - Include the MySQL JDBC driver
   - Start with database support

**Configuration:** I've configured `.vscode/settings.json` to automatically use the correct classpath.

---

## ✅ Method 3: Run Script (Alternative)

```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project Ver3"
./run.sh
```

---

## ✅ Method 4: Manual Terminal Command (Alternative)

```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project Ver3"
java -cp "bin:lib/*" POSApplication
```

**Note:** Make sure to compile first with `./compile.sh` if you made changes

---

## ⚠️ Important Notes

### Database Connection

- **Database Name:** `kkkDB`
- **Username:** `root`
- **Password:** `Khantkoko18$`
- **Port:** `3306` (default MySQL)

### If you see "MySQL JDBC Driver not found!"

This means the `lib/*` directory is not in the classpath. Make sure you're using one of the methods above.

### Compilation

If you make changes to the code, compile first:

```bash
./compile.sh
```

Or the VS Code Run button will compile automatically!

---

## 🎯 Features Working

✅ **Database Integration**

- Menu items load from MySQL database
- New items save to database
- Updates persist across restarts

✅ **Auto-Refresh**

- Order view updates when switching tabs
- Sold out items reflect immediately

✅ **BUY Button Dialog**

- Select Temperature (ICE/HOT)
- Choose Quantity (spinner)
- Pick Order Type (Take Out/In Store)
- Add extras for Coffee/Beverage (Add Shot, Whipping Cream)

✅ **Multi-language Support**

- English and Korean (🇺🇸/🇰🇷 button)

---

## 🔧 Troubleshooting

### Problem: "Access denied for user 'root'@'localhost'"

**Solution:** Check your MySQL password in `src/database/DatabaseManager.java`

### Problem: "No suitable driver found"

**Solution:** Make sure you're running from the workspace root with `lib/*` in classpath

### Problem: Encoding errors during compilation

**Solution:** The compile script now uses `-encoding UTF-8` automatically

### Problem: Eclipse doesn't recognize the project

**Solution:**

1. Right-click on the project in Eclipse
2. Select **Refresh** (F5)
3. Select **Project → Clean...** to rebuild

### Problem: Eclipse can't find the MySQL driver

**Solution:**

1. Right-click on project → **Properties**
2. Go to **Java Build Path → Libraries**
3. Verify `mysql-connector-j-9.4.0.jar` is listed
4. If not, click **Add JARs...** and select it from the `lib` folder

---

## 📁 Project Structure

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
├── bin/                         # Compiled classes
├── .classpath                   # Eclipse classpath config
├── .project                     # Eclipse project config
├── .settings/                   # Eclipse settings
│   ├── org.eclipse.jdt.core.prefs        # Java compiler settings
│   └── org.eclipse.core.resources.prefs  # Encoding settings (UTF-8)
├── .vscode/                     # VS Code configuration
│   ├── settings.json            # Code Runner config
│   └── launch.json              # Debug config
├── POSApplication.launch        # Eclipse run configuration
├── compile.sh                   # Compilation script
├── run.sh                       # Run script
└── HOW_TO_RUN.md               # This file!
```
