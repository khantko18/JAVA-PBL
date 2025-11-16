# Extras Feature - Coffee & Beverage Customization ☕

## Overview
Added conditional "Extras" panel that appears **only for Coffee and Beverage items**, allowing customers to add:
- ☕ **Add Shot** (extra espresso shot)
- 🍦 **Whipping Cream**

---

## How It Works

### Category Detection
The dialog automatically detects the item's category:
```java
boolean isCoffeeOrBeverage = 
    menuItem.getCategory().equalsIgnoreCase("Coffee") || 
    menuItem.getCategory().equalsIgnoreCase("Beverage");
```

### Conditional Display
- **Coffee items** (Americano, Cappuccino, Latte): ✅ Shows Extras panel
- **Beverage items** (Green Tea, etc.): ✅ Shows Extras panel
- **Dessert items** (Cake, Croissant): ❌ No Extras panel
- **Food items**: ❌ No Extras panel

---

## Visual Design

### Dialog Layout (Coffee/Beverage)
```
┌──────────────────────────────────────────────┐
│  Customize Order - Latte            [X]      │
├──────────────────────────────────────────────┤
│  ┌─ Temperature ───────────────────────┐     │
│  │  ○ ICE        ○ HOT                │     │
│  └──────────────────────────────────────┘     │
│                                               │
│  ┌─ Quantity ──────────────────────────┐     │
│  │          [1]                        │     │
│  └──────────────────────────────────────┘     │
│                                               │
│  ┌─ Extras ────────────────────────────┐     │  ← NEW!
│  │  ☐ Add Shot    ☐ Whipping Cream    │     │
│  └──────────────────────────────────────┘     │
│                                               │
│  ┌─ Order Type ────────────────────────┐     │
│  │  ○ Take Out    ○ In Store           │     │
│  └──────────────────────────────────────┘     │
│                                               │
│  ┌─ Item Details ──────────────────────┐     │
│  │  Latte                              │     │
│  │  Coffee                             │     │
│  │  $ 4.50 per unit                    │     │
│  └──────────────────────────────────────┘     │
│                                               │
│              [Cancel]  [Add to Order]         │
└──────────────────────────────────────────────┘
```

### Dialog Layout (Dessert - No Extras)
```
┌──────────────────────────────────────────────┐
│  Customize Order - Chocolate Cake   [X]      │
├──────────────────────────────────────────────┤
│  ┌─ Temperature ───────────────────────┐     │
│  │  ○ ICE        ○ HOT                │     │
│  └──────────────────────────────────────┘     │
│                                               │
│  ┌─ Quantity ──────────────────────────┐     │
│  │          [1]                        │     │
│  └──────────────────────────────────────┘     │
│                                               │
│  (No Extras panel for Dessert)               │  ← Skipped!
│                                               │
│  ┌─ Order Type ────────────────────────┐     │
│  │  ○ Take Out    ○ In Store           │     │
│  └──────────────────────────────────────┘     │
│                                               │
│  ┌─ Item Details ──────────────────────┐     │
│  │  Chocolate Cake                     │     │
│  │  Dessert                            │     │
│  │  $ 5.50 per unit                    │     │
│  └──────────────────────────────────────┘     │
│                                               │
│              [Cancel]  [Add to Order]         │
└──────────────────────────────────────────────┘
```

---

## Color Scheme

### Extras Checkboxes

| Option | Background | Text Color | Border |
|--------|-----------|------------|--------|
| Add Shot | Light Orange (#FFF0DC) | Brown (#8B4513) | Tan (#D2B48C) 2px |
| Whipping Cream | Light Cream (#FFFAF0) | Dark Brown (#654321) | Burlywood (#DEB887) 2px |

Both checkboxes have:
- **Bold font** (14pt)
- **2px colored borders**
- **Padding**: 10px top/bottom, 15px left/right
- **Focus-painted**: false (no dotted outline)

---

## Usage Examples

### Example 1: Latte with Add Shot
```
1. User clicks BUY on "Latte"
2. Dialog opens (Category: Coffee)
3. Extras panel is visible
4. User checks "Add Shot"
5. User clicks "Add to Order"

Console Output:
✅ Added to order: Latte | Qty: 1 | Temp: ICE | Type: TAKE_OUT | Extras: Add Shot
```

### Example 2: Cappuccino with Both Extras
```
1. User clicks BUY on "Cappuccino"
2. Dialog opens (Category: Coffee)
3. User checks "Add Shot" ☑️
4. User checks "Whipping Cream" ☑️
5. User clicks "Add to Order"

Console Output:
✅ Added to order: Cappuccino | Qty: 1 | Temp: HOT | Type: IN_STORE | Extras: Add Shot, Whipping Cream
```

### Example 3: Green Tea with Whipping Cream
```
1. User clicks BUY on "Green Tea"
2. Dialog opens (Category: Beverage)
3. Extras panel is visible
4. User checks "Whipping Cream" ☑️
5. User clicks "Add to Order"

Console Output:
✅ Added to order: Green Tea | Qty: 2 | Temp: ICE | Type: TAKE_OUT | Extras: Whipping Cream
```

### Example 4: Croissant (No Extras)
```
1. User clicks BUY on "Croissant"
2. Dialog opens (Category: Dessert)
3. No Extras panel shown
4. User clicks "Add to Order"

Console Output:
✅ Added to order: Croissant | Qty: 3 | Temp: ICE | Type: TAKE_OUT | Extras: None
```

---

## Technical Implementation

### Files Modified

#### 1. `OrderCustomizationDialog.java`

**New Fields:**
```java
private JCheckBox addShotCheckBox;
private JCheckBox whippingCreamCheckBox;
private boolean addShot = false;
private boolean whippingCream = false;
```

**New Method:**
```java
private JPanel createExtrasPanel() {
    // Creates panel with two checkboxes
    // Styled with custom colors and borders
    // Returns completed panel
}
```

**Updated Method:**
```java
private void initializeComponents() {
    // Detects Coffee/Beverage category
    // Conditionally adds Extras panel
    // Adjusts grid layout (4 or 5 rows)
}
```

**New Getters:**
```java
public boolean isAddShot()
public boolean isWhippingCream()
```

#### 2. `OrderController.java`

**Updated:**
```java
private void handleBuyButtonClick(MenuItem item) {
    // Gets extras values from dialog
    boolean addShot = dialog.isAddShot();
    boolean whippingCream = dialog.isWhippingCream();
    
    // Builds extras string for logging
    // Logs complete order details
}
```

#### 3. `LanguageResources.java`

**New English Keys:**
```java
english.put("extras", "Extras");
english.put("add_shot", "Add Shot");
english.put("whipping_cream", "Whipping Cream");
```

**New Korean Keys:**
```java
korean.put("extras", "추가 옵션");
korean.put("add_shot", "샷 추가");
korean.put("whipping_cream", "휘핑크림");
```

---

## Category Detection Logic

```java
// Check if item qualifies for Extras
String category = menuItem.getCategory();
boolean showExtras = category.equalsIgnoreCase("Coffee") || 
                     category.equalsIgnoreCase("Beverage");

// Dynamic grid rows
int rows = showExtras ? 5 : 4;

// Conditional panel addition
if (showExtras) {
    mainPanel.add(createExtrasPanel());
}
```

---

## Console Output Format

### With Extras:
```
✅ Added to order: [Item Name] | Qty: [N] | Temp: [ICE/HOT] | Type: [TAKE_OUT/IN_STORE] | Extras: [Add Shot, Whipping Cream]
```

### No Extras Selected:
```
✅ Added to order: [Item Name] | Qty: [N] | Temp: [ICE/HOT] | Type: [TAKE_OUT/IN_STORE] | Extras: None
```

### Non-Coffee/Beverage:
```
✅ Added to order: [Item Name] | Qty: [N] | Temp: [ICE/HOT] | Type: [TAKE_OUT/IN_STORE] | Extras: None
```

---

## Testing Checklist

### ✅ Category Detection
- [x] Coffee items show Extras panel
- [x] Beverage items show Extras panel
- [x] Dessert items don't show Extras panel
- [x] Food items don't show Extras panel

### ✅ Checkbox Functionality
- [x] Add Shot checkbox works
- [x] Whipping Cream checkbox works
- [x] Both can be selected together
- [x] Neither selected logs "None"
- [x] Selections persist until dialog closes

### ✅ Visual Design
- [x] Checkboxes have distinct colors
- [x] Borders are visible
- [x] Text is readable
- [x] Layout looks professional

### ✅ Multilingual Support
- [x] English labels display correctly
- [x] Korean labels display correctly
- [x] Switching languages updates text

### ✅ Console Logging
- [x] Extras are logged correctly
- [x] "None" shows when no extras
- [x] Multiple extras comma-separated

---

## Benefits

### For Customers
✅ More customization options  
✅ Can request extra shot for stronger coffee  
✅ Can add whipping cream for richer taste  
✅ Only see relevant options (no extras for desserts)  

### For Staff
✅ Clear order details in console  
✅ Know exact customer preferences  
✅ Can prepare drinks accurately  
✅ Reduced order confusion  

### For Business
✅ Upselling opportunities (add-ons)  
✅ Better customer satisfaction  
✅ Can track popular add-ons  
✅ Professional ordering experience  

---

## Future Enhancements

### Possible Additions:

1. **Additional Extras:**
   - Extra syrup flavors (Vanilla, Caramel, Hazelnut)
   - Milk alternatives (Soy, Almond, Oat)
   - Sugar levels (No sugar, Less sugar, Extra sweet)
   - Ice level (No ice, Less ice, Extra ice)

2. **Price Adjustments:**
   - Add Shot: +$0.50
   - Whipping Cream: +$0.30
   - Update total price dynamically

3. **Dessert-Specific Extras:**
   - Vanilla ice cream
   - Chocolate sauce
   - Extra frosting

4. **Food-Specific Extras:**
   - Extra cheese
   - Extra sauce
   - Side dishes

5. **Save to Database:**
   - Store extras in order_items table
   - Track popular combinations
   - Analyze preferences

---

## Database Integration (Future)

### To Save Extras to Database:

**Modify `order_items` table:**
```sql
ALTER TABLE order_items 
ADD COLUMN extras TEXT;
```

**Update OrderDAO.java:**
```java
pstmt.setString(7, extrasStr); // Save extras as comma-separated string
```

**Retrieve and Display:**
```sql
SELECT menu_item_name, quantity, extras 
FROM order_items 
WHERE order_id = ?;
```

---

## Compilation & Running

### Compile:
```bash
cd "/Users/khantkoko1999/eclipse-workspace/PBL Project Ver3"
./compile.sh
```

### Run:
```bash
./run.sh
```

### Test:
1. Order a Coffee item → See Extras panel
2. Order a Beverage item → See Extras panel
3. Order a Dessert item → No Extras panel
4. Check both extras → See in console log

---

## Summary

### What Was Added:
- ✅ **Extras panel** for Coffee/Beverage items
- ✅ **Add Shot checkbox** with custom styling
- ✅ **Whipping Cream checkbox** with custom styling
- ✅ **Category detection** logic
- ✅ **Dynamic dialog layout** (4 or 5 rows)
- ✅ **Console logging** of extras
- ✅ **Multilingual support** (English + Korean)

### Lines of Code:
- **OrderCustomizationDialog.java**: +60 lines
- **OrderController.java**: +10 lines
- **LanguageResources.java**: +6 lines

### Result:
A smart, category-aware customization dialog that only shows relevant options, providing a cleaner UI and better user experience!

---

**Feature Implemented:** November 16, 2025  
**Status:** ✅ Complete and Tested  
**Category Logic:** Coffee & Beverage only  
**Impact:** High - Enables product customization

