package view;

import model.MenuItem;
import util.LanguageManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class MenuManagementView extends JPanel {
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, priceField, descriptionField;
    private JComboBox<String> categoryCombo;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private String selectedItemId;
    private LanguageManager langManager;

    private JTextField imagePathField;
    private JButton chooseImageButton;
    private JLabel imagePreview;

    private TitledBorder topPanelBorder, bottomPanelBorder;
    private JLabel nameLabel, categoryLabel, priceLabel, descriptionLabel, imageLabel;

    public MenuManagementView() {
        langManager = LanguageManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initializeComponents();
        setupLanguageListener();
        updateFonts();
    }

    private void initializeComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanelBorder = BorderFactory.createTitledBorder(langManager.getText("menu_items_list"));
        topPanel.setBorder(topPanelBorder);

        updateTableModel();
        menuTable = new JTable(tableModel);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuTable.setRowHeight(30);

        JScrollPane tableScroll = new JScrollPane(menuTable);
        tableScroll.setPreferredSize(new Dimension(800, 300));
        topPanel.add(tableScroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanelBorder = BorderFactory.createTitledBorder(langManager.getText("add_edit_item"));
        bottomPanel.setBorder(bottomPanelBorder);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        nameLabel = new JLabel(langManager.getText("name_label"));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE; formPanel.add(nameLabel, gbc);
        nameField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; formPanel.add(nameField, gbc);

        categoryLabel = new JLabel(langManager.getText("category_label"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE; formPanel.add(categoryLabel, gbc);
        updateCategoryCombo();
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; formPanel.add(categoryCombo, gbc);

        priceLabel = new JLabel(langManager.getText("price_label"));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE; formPanel.add(priceLabel, gbc);
        priceField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; formPanel.add(priceField, gbc);

        descriptionLabel = new JLabel(langManager.getText("description_label"));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE; formPanel.add(descriptionLabel, gbc);
        descriptionField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; formPanel.add(descriptionField, gbc);

        imageLabel = new JLabel("Image:");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE; formPanel.add(imageLabel, gbc);

        JPanel imageRowPanel = new JPanel(new BorderLayout(5, 0));
        imagePathField = new JTextField(); imagePathField.setEditable(false);
        chooseImageButton = new JButton("Choose...");
        imageRowPanel.add(imagePathField, BorderLayout.CENTER);
        imageRowPanel.add(chooseImageButton, BorderLayout.EAST);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; formPanel.add(imageRowPanel, gbc);

        imagePreview = new JLabel("No Image", SwingConstants.CENTER);
        imagePreview.setPreferredSize(new Dimension(100, 70));
        imagePreview.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        imagePreview.setOpaque(true);
        imagePreview.setBackground(new Color(245, 245, 245));
        gbc.gridx = 2; gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE; formPanel.add(imagePreview, gbc);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton(langManager.getText("add_new_item"));
        addButton.setBackground(new Color(40, 167, 69)); addButton.setForeground(Color.BLACK); addButton.setFocusPainted(false);
        updateButton = new JButton(langManager.getText("update_item"));
        updateButton.setBackground(new Color(255, 193, 7)); updateButton.setForeground(Color.BLACK); updateButton.setEnabled(false); updateButton.setFocusPainted(false);
        deleteButton = new JButton(langManager.getText("delete_item"));
        deleteButton.setBackground(new Color(220, 53, 69)); deleteButton.setForeground(Color.BLACK); deleteButton.setEnabled(false); deleteButton.setFocusPainted(false);
        clearButton = new JButton(langManager.getText("clear_form"));
        clearButton.setBackground(new Color(108, 117, 125)); clearButton.setForeground(Color.BLACK); clearButton.setFocusPainted(false);
        
        buttonsPanel.add(addButton); buttonsPanel.add(updateButton); buttonsPanel.add(deleteButton); buttonsPanel.add(clearButton);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setResizeWeight(0.6);
        add(splitPane, BorderLayout.CENTER);

        chooseImageButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "gif", "jpeg"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                imagePathField.setText(f.getAbsolutePath());
                updateImagePreview(f.getAbsolutePath());
            }
        });
    }
    
    private void updateImagePreview(String path) {
        if (path == null || path.trim().isEmpty()) {
            imagePreview.setIcon(null); imagePreview.setText("No Image"); return;
        }
        File imgFile = new File(path);
        if (!imgFile.exists()) {
            imagePreview.setIcon(null); imagePreview.setText("File Not Found"); return;
        }
        try {
            BufferedImage img = ImageIO.read(imgFile);
            if (img != null) {
                Image scaled = img.getScaledInstance(100, 70, Image.SCALE_SMOOTH);
                imagePreview.setIcon(new ImageIcon(scaled));
                imagePreview.setText("");
            } else {
                imagePreview.setIcon(null); imagePreview.setText("Invalid Image");
            }
        } catch (Exception ex) {
            imagePreview.setIcon(null); imagePreview.setText("Read Error");
        }
        imagePreview.repaint();
    }

    // [폰트 수정]
    private void updateFonts() {
        String fontName = (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) ? "Malgun Gothic" : "Arial";
        Font plainFont = new Font(fontName, Font.PLAIN, 12);
        Font boldFont = new Font(fontName, Font.BOLD, 12);
        
        topPanelBorder.setTitleFont(plainFont); bottomPanelBorder.setTitleFont(plainFont);
        menuTable.setFont(plainFont);
        
        nameLabel.setFont(plainFont); nameField.setFont(plainFont);
        categoryLabel.setFont(plainFont); categoryCombo.setFont(plainFont);
        priceLabel.setFont(plainFont); priceField.setFont(plainFont);
        descriptionLabel.setFont(plainFont); descriptionField.setFont(plainFont);
        imageLabel.setFont(plainFont); imagePathField.setFont(plainFont);
        chooseImageButton.setFont(plainFont);
        
        addButton.setFont(boldFont); updateButton.setFont(boldFont);
        deleteButton.setFont(boldFont); clearButton.setFont(boldFont);
    }
    
    private void updateTableModel() {
        String[] columns = {
            langManager.getText("id"), langManager.getText("name"), langManager.getText("category"),
            langManager.getText("price"), langManager.getText("description"), langManager.getText("available"), "image"
        };
        if (tableModel == null) {
            tableModel = new DefaultTableModel(columns, 0) {
                @Override public boolean isCellEditable(int row, int column) { return column == 5; }
                @Override public Class<?> getColumnClass(int columnIndex) { return (columnIndex == 5) ? Boolean.class : String.class; }
            };
        } else {
            tableModel.setColumnIdentifiers(columns);
        }
    }
    
    private void updateCategoryCombo() {
        String[] categories = {
            langManager.translateCategory("Coffee"), langManager.translateCategory("Beverage"),
            langManager.translateCategory("Dessert"), langManager.translateCategory("Food")
        };
        if (categoryCombo == null) {
            categoryCombo = new JComboBox<>(categories);
        } else {
            categoryCombo.removeAllItems();
            for (String cat : categories) categoryCombo.addItem(cat);
        }
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> { refreshLanguage(); updateFonts(); });
    }
    
    public void refreshLanguage() {
        topPanelBorder.setTitle(langManager.getText("menu_items_list"));
        bottomPanelBorder.setTitle(langManager.getText("add_edit_item"));
        nameLabel.setText(langManager.getText("name_label"));
        categoryLabel.setText(langManager.getText("category_label"));
        priceLabel.setText(langManager.getText("price_label"));
        descriptionLabel.setText(langManager.getText("description_label"));
        String imgText = langManager.getText("image");
        imageLabel.setText(imgText != null && !imgText.equals("image") ? imgText : "Image:");
        addButton.setText(langManager.getText("add_new_item"));
        updateButton.setText(langManager.getText("update_item"));
        deleteButton.setText(langManager.getText("delete_item"));
        clearButton.setText(langManager.getText("clear_form"));
        updateTableModel(); updateCategoryCombo();
        repaint(); revalidate();
    }
    
    public void displayMenuItems(List<MenuItem> items) {
        tableModel.setRowCount(0);
        for (MenuItem item : items) {
            Object[] row = {
                item.getId(), item.getName(), langManager.translateCategory(item.getCategory()),
                langManager.formatPrice(item.getPrice()), item.getDescription(), !item.isAvailable(), item.getImagePath()
            };
            tableModel.addRow(row);
        }
    }
    
    public void clearForm() {
        nameField.setText(""); priceField.setText(""); descriptionField.setText("");
        imagePathField.setText(""); imagePreview.setIcon(null); imagePreview.setText("No Image");
        categoryCombo.setSelectedIndex(0); selectedItemId = null;
        updateButton.setEnabled(false); deleteButton.setEnabled(false); addButton.setEnabled(true);
    }
    
    public void loadItemToForm(MenuItem item) {
        nameField.setText(item.getName());
        double displayPrice = item.getPrice();
        if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) displayPrice = displayPrice * 1200;
        priceField.setText(String.format("%.0f", displayPrice));
        descriptionField.setText(item.getDescription());
        categoryCombo.setSelectedItem(langManager.translateCategory(item.getCategory()));
        selectedItemId = item.getId();
        String path = item.getImagePath();
        if (path != null && !path.trim().isEmpty()) {
            imagePathField.setText(path); updateImagePreview(path);
        } else {
            imagePathField.setText(""); imagePreview.setIcon(null); imagePreview.setText("No Image");
        }
        updateButton.setEnabled(true); deleteButton.setEnabled(true); addButton.setEnabled(true);
    }
    
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
    public JTextField getImagePathField() { return imagePathField; }
}