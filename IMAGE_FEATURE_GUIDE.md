# 📸 Menu Item Image Feature Guide

## ✅ What's Been Added

Your Cafe POS System now supports **images for menu items**!

---

## 🗄️ Database Changes

### New Column Added:
```sql
ALTER TABLE menu_items 
ADD COLUMN image_path VARCHAR(255) DEFAULT NULL
AFTER description;
```

**Column Details:**
- **Name:** `image_path`
- **Type:** `VARCHAR(255)`
- **Default:** `NULL` (no image)
- **Purpose:** Stores the path to the menu item image file

---

## 📊 Database Schema (Updated)

```sql
CREATE TABLE menu_items (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    image_path VARCHAR(255) DEFAULT NULL,        -- NEW!
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

## 💻 Code Changes

### 1. MenuItem Model (`src/model/MenuItem.java`)

**Added Field:**
```java
private String imagePath;  // Path to menu item image
```

**New Constructor:**
```java
public MenuItem(String id, String name, String category, 
                double price, String description, String imagePath) {
    // ... includes image path
}
```

**New Methods:**
```java
public String getImagePath() { return imagePath; }
public void setImagePath(String imagePath) { this.imagePath = imagePath; }
```

### 2. MenuItemDAO (`src/database/MenuItemDAO.java`)

**Updated SQL Statements:**
- ✅ `INSERT` - Now includes image_path
- ✅ `UPDATE` - Now includes image_path
- ✅ `SELECT` - Now reads image_path from database

**All menu items loaded from database now include their image paths!**

---

## 📁 Directory Structure

```
PBL Project Ver3/
├── images/
│   └── menu_items/          # Store menu item images here
│       ├── americano.jpg
│       ├── cappuccino.jpg
│       ├── latte.jpg
│       └── ...
├── src/
│   ├── model/MenuItem.java  # Updated with imagePath field
│   └── database/MenuItemDAO.java  # Updated to handle images
└── database_schema.sql      # Updated schema
```

---

## 🎯 How to Add Images to Menu Items

### Method 1: Manual Database Update (Quick Test)

```sql
-- Add image to a specific menu item
UPDATE menu_items 
SET image_path = 'images/menu_items/americano.jpg'
WHERE id = 'M001';

-- Verify
SELECT id, name, image_path FROM menu_items WHERE id = 'M001';
```

### Method 2: Add Images When Creating New Items

**In your application code** (future UI enhancement):
```java
MenuItem item = new MenuItem(
    "M009",                              // id
    "Espresso",                          // name
    "Coffee",                            // category
    2.50,                                // price
    "Strong espresso shot",              // description
    "images/menu_items/espresso.jpg"     // image path
);

menuItemDAO.insertMenuItem(item);
```

### Method 3: Update Existing Items

```java
MenuItem item = menuItemDAO.getMenuItemById("M001");
item.setImagePath("images/menu_items/americano.jpg");
menuItemDAO.updateMenuItem(item);
```

---

## 📷 Image Best Practices

### Recommended Image Specifications:
- **Format:** JPG or PNG
- **Size:** 300x300 to 500x500 pixels (square)
- **File Size:** < 500 KB per image
- **Quality:** Medium to High
- **Background:** Transparent (PNG) or white background

### Naming Convention:
```
images/menu_items/[item-name-lowercase].jpg

Examples:
- americano.jpg
- cappuccino.jpg
- chocolate-cake.jpg
- green-tea.png
```

---

## 🔧 Example: Adding Images to All Current Items

```sql
USE kkkDB;

-- Update existing items with images
UPDATE menu_items SET image_path = 'images/menu_items/americano.jpg' WHERE id = 'M001';
UPDATE menu_items SET image_path = 'images/menu_items/cappuccino.jpg' WHERE id = 'M002';
UPDATE menu_items SET image_path = 'images/menu_items/latte.jpg' WHERE id = 'M003';
UPDATE menu_items SET image_path = 'images/menu_items/green-tea.jpg' WHERE id = 'M004';
UPDATE menu_items SET image_path = 'images/menu_items/chocolate-cake.jpg' WHERE id = 'M005';
UPDATE menu_items SET image_path = 'images/menu_items/croissant.jpg' WHERE id = 'M006';

-- Verify all images
SELECT id, name, image_path FROM menu_items ORDER BY id;
```

---

## 🎨 Next Steps: UI Integration

### To Display Images in the UI (Future Enhancement):

**In OrderView.java:**
```java
// Load and display image
if (item.getImagePath() != null) {
    ImageIcon icon = new ImageIcon(item.getImagePath());
    Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
    card.add(imageLabel, BorderLayout.NORTH);
} else {
    // Show placeholder or text
    JLabel placeholder = new JLabel("No Image");
    card.add(placeholder, BorderLayout.NORTH);
}
```

**In MenuManagementView.java:**
```java
// Add image selection button
JButton selectImageBtn = new JButton("Select Image");
selectImageBtn.addActionListener(e -> {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File("images/menu_items"));
    int result = fileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        imagePathField.setText(selectedFile.getPath());
    }
});
```

---

## 🧪 Testing the Feature

### 1. Verify Database Column

```bash
mysql -h 127.0.0.1 -u root -p'Khantkoko18$' kkkDB -e "DESCRIBE menu_items;"
```

**Expected Output:**
```
| image_path | varchar(255) | YES  |     | NULL    |       |
```

### 2. Check Current Items

```bash
mysql -h 127.0.0.1 -u root -p'Khantkoko18$' kkkDB -e "SELECT id, name, image_path FROM menu_items;"
```

### 3. Add Test Image

```sql
-- Add image to Americano
UPDATE menu_items 
SET image_path = 'images/menu_items/americano.jpg'
WHERE id = 'M001';
```

### 4. Run Application

```bash
./run.sh
```

**Check Console Output:**
```
✅ Pre-loaded 8 menu items from database
```

The items will now have their image paths loaded!

---

## 📦 Sample Images

### Where to Get Menu Item Images:

1. **Free Stock Photos:**
   - [Unsplash](https://unsplash.com) - Search "coffee", "cake", "tea"
   - [Pexels](https://pexels.com) - High quality food photos
   - [Pixabay](https://pixabay.com) - Free images

2. **Icon Libraries:**
   - [Flaticon](https://flaticon.com) - Food and beverage icons
   - [Icons8](https://icons8.com) - Menu item illustrations

3. **Custom Photos:**
   - Take photos of actual menu items
   - Use a white background
   - Ensure good lighting

---

## 🔍 Troubleshooting

### Problem: Images Not Loading

**Check:**
1. Image file exists at the specified path
2. Path is correct (relative to project root)
3. File permissions are readable
4. Image format is supported (JPG/PNG)

**Solution:**
```bash
# Check if file exists
ls -la "images/menu_items/americano.jpg"

# Fix permissions if needed
chmod 644 images/menu_items/*.jpg
```

### Problem: Database Shows NULL for image_path

**This is normal!** Items without images will have `NULL` as `image_path`.

**To add an image:**
```sql
UPDATE menu_items 
SET image_path = 'images/menu_items/[item-name].jpg'
WHERE id = '[item-id]';
```

---

## 📋 Summary

### What's Working Now:

✅ Database column `image_path` added to `menu_items` table  
✅ `MenuItem` model includes `imagePath` field  
✅ `MenuItemDAO` handles image paths in all operations  
✅ Images can be stored and retrieved from database  
✅ Directory structure created (`images/menu_items/`)  

### Next Steps (Optional Enhancements):

🔜 Add image selection in Menu Management UI  
🔜 Display images in Order View menu cards  
🔜 Add image preview when editing items  
🔜 Implement image upload feature  
🔜 Add default placeholder image  

---

## 🎉 You're Ready!

Your database now supports menu item images! The infrastructure is in place. When you're ready to display images in the UI, the image paths will already be available in the `MenuItem` objects.

**To start using images:**
1. Place images in `images/menu_items/` directory
2. Update database with image paths
3. Run the application - items will load with image paths!

---

*Last Updated: November 16, 2024*  
*Feature: Menu Item Images Support*

