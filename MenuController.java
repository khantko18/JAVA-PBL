package controller;

import model.MenuItem;
import model.MenuManager;
import view.MenuManagementView;
import util.LanguageManager;
import database.MenuItemDAO;

import javax.swing.*;
import java.util.List;

/**
 * Controller for managing menu items
 * 기존 로직 그대로 유지 + imagePath 처리 추가
 */
public class MenuController {
    private MenuManager menuManager;
    private MenuManagementView view;
    private LanguageManager langManager;
    private MenuItemDAO menuItemDAO;
    private OrderController orderController;

    public MenuController(MenuManager menuManager, MenuManagementView view, OrderController orderController) {
        this.menuManager = menuManager;
        this.view = view;
        this.orderController = orderController;
        this.langManager = LanguageManager.getInstance();
        this.menuItemDAO = new MenuItemDAO();

        // Load menu items from database (existing code elsewhere handles this)
        loadMenuFromDatabase();

        initializeListeners();
        refreshMenuTable();
        setupLanguageListener();
    }

    private void loadMenuFromDatabase() {
        try {
            List<MenuItem> dbItems = menuItemDAO.getAllMenuItems();
            if (!dbItems.isEmpty()) {
                // Clear existing items
                menuManager.getAllMenuItems().clear();
                // Add items from database (DB items already include available flag)
                for (MenuItem item : dbItems) {
                    // Reset available true on startup? original code set available true; keep original behavior
                    item.setAvailable(true);
                    menuManager.addMenuItem(item);
                    // Update DB (attempt) - keep original behavior
                    menuItemDAO.updateMenuItem(item);
                }
                System.out.println("✅ Loaded " + dbItems.size() + " menu items from database (all set to available)");
            } else {
                System.out.println("ℹ️ No menu items in database, using default items");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to load menu from database: " + e.getMessage());
            // Continue with default menu items
        }
    }

    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> {
            refreshMenuTable();
        });
    }

    private void initializeListeners() {
        // Table selection listener
        view.getMenuTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTableSelection();
            }
        });

        // Table model listener for checkbox changes
        view.getMenuTable().getModel().addTableModelListener(e -> {
            if (e.getColumn() == 5) { // Available column (unchanged)
                int row = e.getFirstRow();
                if (row >= 0) {
                    handleAvailabilityChange(row);
                }
            }
        });

        // Add button
        view.getAddButton().addActionListener(e -> handleAddItem());

        // Update button
        view.getUpdateButton().addActionListener(e -> handleUpdateItem());

        // Delete button
        view.getDeleteButton().addActionListener(e -> handleDeleteItem());

        // Clear button
        view.getClearButton().addActionListener(e -> view.clearForm());
    }

    private void handleTableSelection() {
        int selectedRow = view.getMenuTable().getSelectedRow();
        if (selectedRow >= 0) {
            String itemId = (String) view.getMenuTable().getValueAt(selectedRow, 0);
            MenuItem item = menuManager.getMenuItem(itemId);
            if (item != null) {
                view.loadItemToForm(item);
            }
        }
    }

    private void handleAvailabilityChange(int row) {
        try {
            String itemId = (String) view.getMenuTable().getValueAt(row, 0);
            Boolean isSoldOut = (Boolean) view.getMenuTable().getValueAt(row, 5);

            MenuItem item = menuManager.getMenuItem(itemId);
            if (item != null) {
                // Reverse logic: checked = sold out = not available
                item.setAvailable(!isSoldOut);

                // Update in database
                try {
                    boolean updated = menuItemDAO.updateMenuItem(item);
                    if (updated) {
                        String status = isSoldOut ? langManager.getText("sold_out") : langManager.getText("yes");
                        System.out.println("✅ Menu item status updated: " + itemId + " - " + status);

                        // Refresh order view menu display
                        if (orderController != null) {
                            orderController.refreshMenu();
                        }
                    } else {
                        System.err.println("⚠️ Failed to update menu item status in database");
                    }
                } catch (Exception dbEx) {
                    System.err.println("⚠️ Database error: " + dbEx.getMessage());
                }
            }
        } catch (Exception ex) {
            System.err.println("⚠️ Error handling availability change: " + ex.getMessage());
        }
    }

    private void handleAddItem() {
        try {
            String name = view.getNameField().getText().trim();
            String displayCategory = (String) view.getCategoryCombo().getSelectedItem();
            String category = langManager.getCategoryKey(displayCategory);
            String priceText = view.getPriceField().getText().trim();
            String description = view.getDescriptionField().getText().trim();
            String imagePath = view.getImagePathField() != null ? view.getImagePathField().getText().trim() : null;

            // Validation
            if (name.isEmpty()) {
                throw new IllegalArgumentException(langManager.getText("name_empty"));
            }

            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                throw new IllegalArgumentException(langManager.getText("price_positive"));
            }

            // Convert price from KRW to USD if in Korean mode
            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                price = price / 1200; // Convert KRW to USD
            }

            // Create and add new item (use constructor with imagePath)
            String newId = menuManager.generateNewId();
            MenuItem newItem = new MenuItem(newId, name, category, price, description, imagePath);
            menuManager.addMenuItem(newItem);

            // Save to database
            try {
                boolean saved = menuItemDAO.insertMenuItem(newItem);
                if (saved) {
                    System.out.println("✅ Menu item saved to database: " + newId);
                } else {
                    System.err.println("⚠️ Failed to save menu item to database");
                }
            } catch (Exception dbEx) {
                System.err.println("⚠️ Database error: " + dbEx.getMessage());
            }

            // Refresh and clear
            refreshMenuTable();
            view.clearForm();

            JOptionPane.showMessageDialog(view,
                langManager.getText("item_added"),
                langManager.getText("success"),
                JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view,
                langManager.getText("invalid_price_format"),
                langManager.getText("error"),
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view,
                ex.getMessage(),
                langManager.getText("validation_error"),
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateItem() {
        try {
            String itemId = view.getSelectedItemId();
            if (itemId == null) {
                return;
            }

            MenuItem item = menuManager.getMenuItem(itemId);
            if (item == null) {
                JOptionPane.showMessageDialog(view,
                    langManager.getText("error"),
                    langManager.getText("error"),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            String name = view.getNameField().getText().trim();
            String displayCategory = (String) view.getCategoryCombo().getSelectedItem();
            String category = langManager.getCategoryKey(displayCategory);
            String priceText = view.getPriceField().getText().trim();
            String description = view.getDescriptionField().getText().trim();
            String imagePath = view.getImagePathField() != null ? view.getImagePathField().getText().trim() : null;

            // Validation
            if (name.isEmpty()) {
                throw new IllegalArgumentException(langManager.getText("name_empty"));
            }

            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                throw new IllegalArgumentException(langManager.getText("price_positive"));
            }

            // Convert price from KRW to USD if in Korean mode
            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                price = price / 1200; // Convert KRW to USD
            }

            // Update item
            item.setName(name);
            item.setCategory(category);
            item.setPrice(price);
            item.setDescription(description);
            item.setImagePath(imagePath);

            // Update in database
            try {
                boolean updated = menuItemDAO.updateMenuItem(item);
                if (updated) {
                    System.out.println("✅ Menu item updated in database: " + itemId);
                } else {
                    System.err.println("⚠️ Failed to update menu item in database");
                }
            } catch (Exception dbEx) {
                System.err.println("⚠️ Database error: " + dbEx.getMessage());
            }

            // Refresh and clear
            refreshMenuTable();
            view.clearForm();

            JOptionPane.showMessageDialog(view,
                langManager.getText("item_updated"),
                langManager.getText("success"),
                JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view,
                langManager.getText("invalid_price_format"),
                langManager.getText("error"),
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view,
                ex.getMessage(),
                langManager.getText("validation_error"),
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteItem() {
        String itemId = view.getSelectedItemId();
        if (itemId == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
            langManager.getText("confirm_delete"),
            langManager.getText("confirm_delete_title"),
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            menuManager.removeMenuItem(itemId);

            // Delete from database
            try {
                boolean deleted = menuItemDAO.deleteMenuItem(itemId);
                if (deleted) {
                    System.out.println("✅ Menu item deleted from database: " + itemId);
                } else {
                    System.err.println("⚠️ Failed to delete menu item from database");
                }
            } catch (Exception dbEx) {
                System.err.println("⚠️ Database error: " + dbEx.getMessage());
            }

            refreshMenuTable();
            view.clearForm();

            JOptionPane.showMessageDialog(view,
                langManager.getText("item_deleted"),
                langManager.getText("success"),
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refreshMenuTable() {
        List<MenuItem> items = menuManager.getAllMenuItems();
        // Sort items by ID (M001, M002, M003, etc.)
        items.sort((a, b) -> a.getId().compareTo(b.getId()));
        view.displayMenuItems(items);

        // Also refresh order view menu display to show images
        if (orderController != null) {
            orderController.refreshMenu();
        }
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLang -> refreshMenuTable());
    }
}
