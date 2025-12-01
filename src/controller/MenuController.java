package controller;

import model.MenuItem;
import model.MenuManager;
import view.MenuManagementView;
import util.LanguageManager;
import database.MenuItemDAO;

import javax.swing.*;
import java.util.List;

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

        loadMenuFromDatabase();
        initializeListeners();
        refreshMenuTable();
        setupLanguageListener();
    }

    private void loadMenuFromDatabase() {
        try {
            List<MenuItem> dbItems = menuItemDAO.getAllMenuItems();
            if (!dbItems.isEmpty()) {
                menuManager.getAllMenuItems().clear();
                for (MenuItem item : dbItems) {
                    item.setAvailable(true);
                    menuManager.addMenuItem(item);
                    menuItemDAO.updateMenuItem(item);
                }
                System.out.println("✅ Loaded " + dbItems.size() + " menu items from database.");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to load menu: " + e.getMessage());
        }
    }

    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> refreshMenuTable());
    }

    private void initializeListeners() {
        view.getMenuTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) handleTableSelection();
        });

        view.getMenuTable().getModel().addTableModelListener(e -> {
            if (e.getColumn() == 5) { // Available column
                int row = e.getFirstRow();
                if (row >= 0) handleAvailabilityChange(row);
            }
        });

        view.getAddButton().addActionListener(e -> handleAddItem());
        view.getUpdateButton().addActionListener(e -> handleUpdateItem());
        view.getDeleteButton().addActionListener(e -> handleDeleteItem());
        view.getClearButton().addActionListener(e -> view.clearForm());
    }

    private void handleTableSelection() {
        int selectedRow = view.getMenuTable().getSelectedRow();
        if (selectedRow >= 0) {
            String itemId = (String) view.getMenuTable().getValueAt(selectedRow, 0);
            MenuItem item = menuManager.getMenuItem(itemId);
            if (item != null) view.loadItemToForm(item);
        }
    }

    private void handleAvailabilityChange(int row) {
        try {
            String itemId = (String) view.getMenuTable().getValueAt(row, 0);
            Boolean isSoldOut = (Boolean) view.getMenuTable().getValueAt(row, 5);
            MenuItem item = menuManager.getMenuItem(itemId);
            
            if (item != null) {
                item.setAvailable(!isSoldOut);
                menuItemDAO.updateMenuItem(item);
                if (orderController != null) orderController.refreshMenu();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleAddItem() {
        try {
            String name = view.getNameField().getText().trim();
            String displayCategory = (String) view.getCategoryCombo().getSelectedItem();
            String category = langManager.getCategoryKey(displayCategory);
            String priceText = view.getPriceField().getText().trim();
            String description = view.getDescriptionField().getText().trim();
            // [최신 기능] 이미지 경로 가져오기
            String imagePath = view.getImagePathField().getText().trim();

            if (name.isEmpty()) throw new IllegalArgumentException(langManager.getText("name_empty"));

            double price = Double.parseDouble(priceText);
            if (price <= 0) throw new IllegalArgumentException(langManager.getText("price_positive"));

            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                price = price / 1200;
            }

            String newId = menuManager.generateNewId();
            // 이미지 포함 생성자 사용
            MenuItem newItem = new MenuItem(newId, name, category, price, description, imagePath);
            menuManager.addMenuItem(newItem);
            menuItemDAO.insertMenuItem(newItem);

            refreshMenuTable();
            view.clearForm();
            JOptionPane.showMessageDialog(view, langManager.getText("item_added"), "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, langManager.getText("invalid_price_format"), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateItem() {
        try {
            String itemId = view.getSelectedItemId();
            if (itemId == null) return;

            MenuItem item = menuManager.getMenuItem(itemId);
            if (item == null) return;

            String name = view.getNameField().getText().trim();
            String displayCategory = (String) view.getCategoryCombo().getSelectedItem();
            String category = langManager.getCategoryKey(displayCategory);
            String priceText = view.getPriceField().getText().trim();
            String description = view.getDescriptionField().getText().trim();
            // [최신 기능] 이미지 경로 가져오기
            String imagePath = view.getImagePathField().getText().trim();

            if (name.isEmpty()) throw new IllegalArgumentException(langManager.getText("name_empty"));

            double price = Double.parseDouble(priceText);
            if (price <= 0) throw new IllegalArgumentException(langManager.getText("price_positive"));

            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                price = price / 1200;
            }

            item.setName(name);
            item.setCategory(category);
            item.setPrice(price);
            item.setDescription(description);
            item.setImagePath(imagePath); // [Logic] 업데이트

            menuItemDAO.updateMenuItem(item);

            refreshMenuTable();
            view.clearForm();
            JOptionPane.showMessageDialog(view, langManager.getText("item_updated"), "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteItem() {
        String itemId = view.getSelectedItemId();
        if (itemId == null) return;

        int confirm = JOptionPane.showConfirmDialog(view, langManager.getText("confirm_delete"), "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            menuManager.removeMenuItem(itemId);
            menuItemDAO.deleteMenuItem(itemId);
            refreshMenuTable();
            view.clearForm();
            JOptionPane.showMessageDialog(view, langManager.getText("item_deleted"), "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refreshMenuTable() {
        List<MenuItem> items = menuManager.getAllMenuItems();
        items.sort((a, b) -> a.getId().compareTo(b.getId()));
        view.displayMenuItems(items);
        if (orderController != null) orderController.refreshMenu();
    }
}