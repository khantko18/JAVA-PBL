# How to Run the Cafe POS System

## Method 1: Eclipse IDE (Recommended for Eclipse Users)

Now you can run directly from **Eclipse**!

1. **Import Project** (if not already imported):

   - File → Open Projects from File System...
   - Select the "PBL Project Ver3" folder
   - Click Finish

2. **Run the Application**:

   - Right-click on `src/POSApplication.java`
   - Select **Run As → Java Application**

   OR

   - Click the ** Run** button in the toolbar
   - The MySQL JDBC driver is automatically included!

**Configuration:** The `.classpath` and `.project` files are pre-configured with the MySQL driver.

---

## Method 2: VS Code Run Button (Recommended for VS Code Users)

Now you can use the **Run Code** button in VS Code!

1. Open `src/POSApplication.java`
2. Click the **Run Code** button (top right corner)
3. The application will automatically:
   - Compile all files with proper encoding
   - Include the MySQL JDBC driver
   - Start with database support

**Configuration:** I've configured `.vscode/settings.json` to automatically use the correct classpath.

---

## Method 3: Run Script (Alternative)

```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project Ver3"
./run.sh
```

---

## Method 4: Manual Terminal Command (Alternative)

```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project Ver3"
java -cp "bin:lib/*" POSApplication
```

**Note:** Make sure to compile first with `./compile.sh` if you made changes
