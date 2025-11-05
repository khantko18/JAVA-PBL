# Multilingual Implementation Guide

## Overview

The Cafe POS System now supports **English** and **Korean (한국어)** languages with seamless switching capability using toggle buttons in all sections.

## Features Added

### 1. Language Toggle Buttons

- **Location**: Top-right corner of the main window
- **Buttons**:
  - "English" button
  - "한국어" (Korean) button
- **Visual Feedback**: Active language button is highlighted in blue
- **Accessible**: Available from all tabs (Order, Menu Management, Sales Statistics)

### 2. Dynamic Language Switching

All UI elements update instantly when language is changed:

- Tab titles
- Button labels
- Table headers
- Form labels
- Dialog messages
- Error messages
- Success notifications

## Implementation Architecture

### Core Components

#### 1. LanguageManager (`util/LanguageManager.java`)

- **Singleton Pattern**: Ensures single instance across the application
- **Observer Pattern**: Notifies all views when language changes
- **Methods**:
  - `getInstance()`: Get the singleton instance
  - `setLanguage(Language)`: Change current language
  - `getText(String key)`: Get translated text for a key
  - `addLanguageChangeListener(LanguageChangeListener)`: Register for language updates

#### 2. LanguageResources (`util/LanguageResources.java`)

- **Static Resource Maps**: Stores all translations
- **200+ Translation Keys**: Covers all UI text
- **Methods**:
  - `getEnglish(String key)`: Get English translation
  - `getKorean(String key)`: Get Korean translation

### Translation Coverage

#### Main Application

- Window title
- Tab names
- Language selector

#### Order View

- Menu items section
- Category filter (All, Coffee, Beverage, Dessert)
- Current order table (Item, Qty, Price, Subtotal)
- Discount controls
- Total calculation labels
- Buttons (Add, Apply, Clear Order, Proceed to Payment)
- All dialogs (quantity input, clear confirmation, payment)

#### Menu Management View

- Table headers (ID, Name, Category, Price, Description, Available)
- Form labels
- Buttons (Add New Item, Update Item, Delete Item, Clear Form)
- Success/error messages
- Validation messages

#### Sales Statistics View

- Stat cards (Total Revenue, Today's Sales, Today's Orders)
- Table headers (Time, Order ID, Amount, Payment)
- Popular items section

#### Payment Dialog

- Payment method selection (Cash/Card)
- Field labels (Total Amount, Amount Received, Change)
- Buttons (Confirm Payment, Cancel)
- Success/error messages

## How to Use

### For Users

1. **Launch the Application**

   ```bash
   cd "/Users/khantkoko1999/eclipse-workspace/PBL Project"
   java -cp bin POSApplication
   ```

2. **Switch Language**

   - Click "English" button for English
   - Click "한국어" button for Korean
   - All UI elements update instantly

3. **Language Persistence**
   - Language selection persists during the current session
   - Default language is English on startup

### For Developers

#### Adding New Translations

1. **Add to LanguageResources.java**:

```java
// In initEnglish()
english.put("new_key", "English Text");

// In initKorean()
korean.put("new_key", "한국어 텍스트");
```

2. **Use in Views**:

```java
langManager.getText("new_key")
```

#### Adding New Language Support

1. **Extend Language Enum** in `LanguageManager.java`:

```java
public enum Language {
    ENGLISH, KOREAN, JAPANESE  // Add new language
}
```

2. **Add Resource Method** in `LanguageResources.java`:

```java
private static final Map<String, String> japanese = new HashMap<>();

static {
    initEnglish();
    initKorean();
    initJapanese();  // Add initialization
}

private static void initJapanese() {
    japanese.put("app_title", "カフェPOSシステム");
    // Add all translations...
}

public static String getJapanese(String key) {
    return japanese.getOrDefault(key, key);
}
```

3. **Update getText()** in `LanguageManager.java`:

```java
public String getText(String key) {
    if (currentLanguage == Language.KOREAN) {
        return LanguageResources.getKorean(key);
    } else if (currentLanguage == Language.JAPANESE) {
        return LanguageResources.getJapanese(key);
    } else {
        return LanguageResources.getEnglish(key);
    }
}
```

4. **Add Language Button** in `MainView.java`:

```java
JButton japaneseButton = new JButton("日本語");
japaneseButton.addActionListener(e -> {
    langManager.setLanguage(Language.JAPANESE);
    updateLanguageButtons();
});
```

## Technical Details

### Observer Pattern Implementation

```java
// LanguageManager notifies all listeners
private void notifyListeners() {
    for (LanguageChangeListener listener : listeners) {
        listener.onLanguageChanged(currentLanguage);
    }
}

// Views implement the listener
langManager.addLanguageChangeListener(newLanguage -> {
    refreshLanguage();
});
```

### View Refresh Strategy

Each view implements a `refreshLanguage()` method that:

1. Updates all text labels and buttons
2. Refreshes table column headers
3. Updates combo box options
4. Repaints the component

```java
public void refreshLanguage() {
    // Update labels
    titleLabel.setText(langManager.getText("title"));

    // Update table headers
    updateTableModel();

    // Force UI refresh
    repaint();
    revalidate();
}
```

## Translation Keys Reference

### Common Keys

- `app_title`: Application window title
- `success`: Success message title
- `error`: Error message title
- `cancel`: Cancel button
- `yes`: Yes option
- `no`: No option

### Order Processing

- `add`: Add button
- `quantity`: Quantity label
- `subtotal`: Subtotal label
- `discount`: Discount label
- `total`: Total label
- `proceed_payment`: Proceed to payment button
- `clear_order`: Clear order button

### Menu Management

- `add_new_item`: Add new item button
- `update_item`: Update item button
- `delete_item`: Delete item button
- `name_label`: Name field label
- `price_label`: Price field label
- `category_label`: Category field label

### Categories

- `all`: All categories
- `coffee`: Coffee category
- `beverage`: Beverage category
- `dessert`: Dessert category
- `food`: Food category

## Testing

### Manual Testing Checklist

- [x] Switch language from English to Korean
- [x] Switch language from Korean to English
- [x] Verify all tabs update correctly
- [x] Test order processing in both languages
- [x] Test menu management in both languages
- [x] Test sales view in both languages
- [x] Test payment dialog in both languages
- [x] Test all error messages
- [x] Test all success messages
- [x] Test all confirmation dialogs

### Known Limitations

1. Language preference does not persist between sessions (resets to English on restart)
2. Menu item names and descriptions are not translated (only UI elements)
3. Date/time formatting is not localized

## Future Enhancements

1. **Persistent Language Settings**

   - Save language preference to configuration file
   - Load preference on startup

2. **Additional Languages**

   - Japanese (日本語)
   - Chinese (中文)
   - Spanish (Español)

3. **Localized Data**

   - Translate menu item names
   - Translate menu item descriptions
   - Localized date/time formats
   - Localized currency display

4. **Right-to-Left Support**

   - Support for Arabic, Hebrew, etc.
   - Automatic layout mirroring

5. **Dynamic Resource Loading**
   - Load translations from external files (JSON/Properties)
   - Hot-reload translations without restart

## Conclusion

The multilingual implementation provides a solid foundation for international users. The architecture is clean, maintainable, and easily extensible for additional languages. All UI elements respond dynamically to language changes, providing a seamless user experience.

---

**Last Updated**: October 24, 2025  
**Version**: 1.0  
**Languages Supported**: English, Korean (한국어)
