package view;

import model.MenuItem;
import util.LanguageManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * View for managing menu items (add, edit, delete)
 */
public class MenuManagementView extends JPanel {
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField descriptionField;
    private JComboBox<String> categoryCombo;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private String selectedItemId;
    private LanguageManager langManager;
    
    private TitledBorder topPanelBorder;
    private TitledBorder bottomPanelBorder;
    private JLabel nameLabel;
    private JLabel categoryLabel;
    private JLabel priceLabel;
    private JLabel descriptionLabel;
    
    public MenuManagementView() {
        langManager = LanguageManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
        setupLanguageListener();
    }
    
    private void initializeComponents() {
        // Top panel - Menu table
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanelBorder = BorderFactory.createTitledBorder(langManager.getText("menu_items_list"));
        topPanel.setBorder(topPanelBorder);
        
        updateTableModel();
        
        menuTable = new JTable(tableModel);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuTable.setFont(new Font("Arial", Font.PLAIN, 12));
        menuTable.setRowHeight(25);
        
        JScrollPane tableScroll = new JScrollPane(menuTable);
        topPanel.add(tableScroll, BorderLayout.CENTER);
        
        // Bottom panel - Form for adding/editing
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanelBorder = BorderFactory.createTitledBorder(langManager.getText("add_edit_item"));
        bottomPanel.setBorder(bottomPanelBorder);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        nameLabel = new JLabel(langManager.getText("name_label"));
        formPanel.add(nameLabel);
        nameField = new JTextField();
        formPanel.add(nameField);
        
        categoryLabel = new JLabel(langManager.getText("category_label"));
        formPanel.add(categoryLabel);
        updateCategoryCombo();
        formPanel.add(categoryCombo);
        
        priceLabel = new JLabel(langManager.getText("price_label"));
        formPanel.add(priceLabel);
        priceField = new JTextField();
        formPanel.add(priceField);
        
        descriptionLabel = new JLabel(langManager.getText("description_label"));
        formPanel.add(descriptionLabel);
        descriptionField = new JTextField();
        formPanel.add(descriptionField);
        
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = new JButton(langManager.getText("add_new_item"));
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        updateButton = new JButton(langManager.getText("update_item"));
        updateButton.setBackground(new Color(255, 193, 7));
        updateButton.setForeground(Color.BLACK);
        updateButton.setFont(new Font("Arial", Font.BOLD, 12));
        updateButton.setEnabled(false);
        
        deleteButton = new JButton(langManager.getText("delete_item"));
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.BLACK);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
        deleteButton.setEnabled(false);
        
        clearButton = new JButton(langManager.getText("clear_form"));
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(clearButton);
        
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        // Add panels to main view
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void updateTableModel() {
        String[] columns = {
            langManager.getText("id"),
            langManager.getText("name"),
            langManager.getText("category"),
            langManager.getText("price"),
            langManager.getText("description"),
            langManager.getText("available")
        };
        
        if (tableModel == null) {
            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        } else {
            tableModel.setColumnIdentifiers(columns);
        }
    }
    
    private void updateCategoryCombo() {
        String[] categories = {
            langManager.translateCategory("Coffee"),
            langManager.translateCategory("Beverage"),
            langManager.translateCategory("Dessert"),
            langManager.translateCategory("Food")
        };
        
        if (categoryCombo == null) {
            categoryCombo = new JComboBox<>(categories);
        } else {
            int selectedIndex = categoryCombo.getSelectedIndex();
            categoryCombo.removeAllItems();
            for (String cat : categories) {
                categoryCombo.addItem(cat);
            }
            if (selectedIndex >= 0 && selectedIndex < categories.length) {
                categoryCombo.setSelectedIndex(selectedIndex);
            }
        }
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> {
            refreshLanguage();
        });
    }
    
    public void refreshLanguage() {
        topPanelBorder.setTitle(langManager.getText("menu_items_list"));
        bottomPanelBorder.setTitle(langManager.getText("add_edit_item"));
        nameLabel.setText(langManager.getText("name_label"));
        categoryLabel.setText(langManager.getText("category_label"));
        priceLabel.setText(langManager.getText("price_label"));
        descriptionLabel.setText(langManager.getText("description_label"));
        addButton.setText(langManager.getText("add_new_item"));
        updateButton.setText(langManager.getText("update_item"));
        deleteButton.setText(langManager.getText("delete_item"));
        clearButton.setText(langManager.getText("clear_form"));
        
        updateTableModel();
        updateCategoryCombo();
        
        repaint();
        revalidate();
    }
    
    public void displayMenuItems(List<MenuItem> items) {
        tableModel.setRowCount(0);
        
        for (MenuItem item : items) {
            Object[] row = {
                item.getId(),
                item.getName(),
                langManager.translateCategory(item.getCategory()),
                langManager.formatPrice(item.getPrice()),
                item.getDescription(),
                item.isAvailable() ? langManager.getText("yes") : langManager.getText("no")
            };
            tableModel.addRow(row);
        }
    }
    
    public void clearForm() {
        nameField.setText("");
        priceField.setText("");
        descriptionField.setText("");
        categoryCombo.setSelectedIndex(0);
        selectedItemId = null;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        addButton.setEnabled(true);
    }
    
    public void loadItemToForm(MenuItem item) {
        nameField.setText(item.getName());
        priceField.setText(String.valueOf(item.getPrice()));
        descriptionField.setText(item.getDescription());
        categoryCombo.setSelectedItem(langManager.translateCategory(item.getCategory()));
        selectedItemId = item.getId();
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);
        addButton.setEnabled(false);
    }
    
    // Getters
    public JTable getMenuTable() { return menuTable; }
    public JTextField getNameField() { return nameField; }
    public JTextField getPriceField() { return priceField; }
    public JTextField getDescriptionField() { return descriptionField; }
    public JComboBox<String> getCategoryCombo() { return categoryCombo; }
    public JButton getAddButton() { return addButton; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getClearButton() { return clearButton; }
    public String getSelectedItemId() { return selectedItemId; }
}

