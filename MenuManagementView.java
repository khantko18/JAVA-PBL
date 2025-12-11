package view;

import model.MenuItem;
import util.LanguageManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * View for managing menu items (add, edit, delete)
 * 이미지 선택(파일 대화상자) + 이미지 미리보기 추가
 *
 * 주의: table의 기존 컬럼 인덱스(특히 available 컬럼 인덱스 5)를 유지하기 위해
 * 'ImagePath' 컬럼은 마지막(인덱스 6)으로 추가함.
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

    // 이미지 관련 UI
    private JTextField imagePathField;
    private JButton chooseImageButton;
    private JLabel imagePreview;

    private TitledBorder topPanelBorder;
    private TitledBorder bottomPanelBorder;
    private JLabel nameLabel;
    private JLabel categoryLabel;
    private JLabel priceLabel;
    private JLabel descriptionLabel;
    private JLabel imageLabel;

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
        menuTable.setRowHeight(40); // 조금 높여서 미리보기가 보이기 쉬움

        // 테이블 컬럼 최소 너비 설정으로 작은 화면에서 각 칼럼이 지나치게 좁아지지 않게 함
        menuTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        JScrollPane tableScroll = new JScrollPane(menuTable);
        // 테이블 스크롤에 선호 크기 지정 (상단 영역을 넓게 보이게 함)
        tableScroll.setPreferredSize(new Dimension(800, 360));
        topPanel.add(tableScroll, BorderLayout.CENTER);

        // Bottom panel - Form for adding/editing
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanelBorder = BorderFactory.createTitledBorder(langManager.getText("add_edit_item"));
        bottomPanel.setBorder(bottomPanelBorder);

        // Form panel: use GridBagLayout for better size control
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        nameLabel = new JLabel(langManager.getText("name_label"));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        formPanel.add(nameLabel, gbc);
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(240, 28));
        nameField.setMaximumSize(new Dimension(400, 28));
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(nameField, gbc);

        categoryLabel = new JLabel(langManager.getText("category_label"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(categoryLabel, gbc);
        updateCategoryCombo();
        categoryCombo.setPreferredSize(new Dimension(200, 28));
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        formPanel.add(categoryCombo, gbc);

        priceLabel = new JLabel(langManager.getText("price_label"));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(priceLabel, gbc);
        priceField = new JTextField();
        priceField.setPreferredSize(new Dimension(160, 28));
        priceField.setMaximumSize(new Dimension(220, 28));
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(priceField, gbc);

        descriptionLabel = new JLabel(langManager.getText("description_label"));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        formPanel.add(descriptionLabel, gbc);
        descriptionField = new JTextField();
        descriptionField.setPreferredSize(new Dimension(240, 28));
        descriptionField.setMaximumSize(new Dimension(400, 28));
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(descriptionField, gbc);

        // Image selection row: label + button+path+preview (use subpanel)
        imageLabel = new JLabel(langManager.getText("image") != null ? langManager.getText("image") : "Image:");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        formPanel.add(imageLabel, gbc);

        JPanel imageRowPanel = new JPanel(new BorderLayout(8, 8));
        JPanel imgSelectPanel = new JPanel(new BorderLayout(5, 0));
        imagePathField = new JTextField();
        imagePathField.setPreferredSize(new Dimension(240, 28));
        chooseImageButton = new JButton("Choose...");
        chooseImageButton.setFont(new Font("Arial", Font.PLAIN, 12));
        imgSelectPanel.add(imagePathField, BorderLayout.CENTER);
        imgSelectPanel.add(chooseImageButton, BorderLayout.EAST);

        imagePreview = new JLabel();
        // preview 크기를 실제 스케일에 맞춰 작게 설정하여 폼이 과도하게 커지지 않게 함
        imagePreview.setPreferredSize(new Dimension(120, 80));
        imagePreview.setMinimumSize(new Dimension(120, 80));
        imagePreview.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        imageRowPanel.add(imgSelectPanel, BorderLayout.CENTER);
        imageRowPanel.add(imagePreview, BorderLayout.EAST);

        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(imageRowPanel, gbc);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addButton = new JButton(langManager.getText("add_new_item"));
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.setFocusPainted(false);

        updateButton = new JButton(langManager.getText("update_item"));
        updateButton.setBackground(new Color(255, 193, 7));
        updateButton.setForeground(Color.BLACK);
        updateButton.setFont(new Font("Arial", Font.BOLD, 12));
        updateButton.setEnabled(false);
        updateButton.setFocusPainted(false);

        deleteButton = new JButton(langManager.getText("delete_item"));
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.BLACK);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
        deleteButton.setEnabled(false);
        deleteButton.setFocusPainted(false);

        clearButton = new JButton(langManager.getText("clear_form"));
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearButton.setFocusPainted(false);

        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(clearButton);

        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Use JSplitPane to control top/bottom proportions so top (table) is larger
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        split.setResizeWeight(0.65); // 상단 65% 비율로 크게
        split.setOneTouchExpandable(true);
        split.setDividerSize(6);
        add(split, BorderLayout.CENTER);

        // Image chooser action
        chooseImageButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                imagePathField.setText(f.getAbsolutePath());
                // load preview safely and scale to the preview size
                try {
                    ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                    Image img = icon.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                    imagePreview.setIcon(new ImageIcon(img));
                } catch (Exception ex) {
                    imagePreview.setIcon(null);
                }
            }
        });

        // Optionally: set some sensible minimum sizes so UI doesn't collapse
        topPanel.setMinimumSize(new Dimension(400, 200));
        bottomPanel.setMinimumSize(new Dimension(400, 150));
    }

    private void updateTableModel() {
        String[] columns = {
            langManager.getText("id"),
            langManager.getText("name"),
            langManager.getText("category"),
            langManager.getText("price"),
            langManager.getText("description"),
            langManager.getText("available"),
            "image" // 추가: 이미지 경로(마지막 칼럼으로 추가, 내부용)
        };

        if (tableModel == null) {
            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Make only the "available" column (column 5) editable
                    return column == 5;
                }

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    // Set column 5 as Boolean for checkbox
                    if (columnIndex == 5) {
                        return Boolean.class;
                    }
                    return String.class;
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
        // imageLabel는 resources에 없을 경우 "image"로 표시됨
        imageLabel.setText(langManager.getText("image") != null ? langManager.getText("image") : "Image");
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
                !item.isAvailable(),  // Checked = Sold Out (not available)
                item.getImagePath()   // 마지막 칼럼: 이미지 경로(내부용)
            };
            tableModel.addRow(row);
        }
    }

    public void clearForm() {
        nameField.setText("");
        priceField.setText("");
        descriptionField.setText("");
        imagePathField.setText("");
        imagePreview.setIcon(null);
        categoryCombo.setSelectedIndex(0);
        selectedItemId = null;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        addButton.setEnabled(true);
    }

    public void loadItemToForm(MenuItem item) {
        nameField.setText(item.getName());

        // Display price in KRW if Korean mode, otherwise in USD
        double displayPrice = item.getPrice();
        if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
            displayPrice = displayPrice * 1200; // Convert USD to KRW for editing
        }
        priceField.setText(String.format("%.0f", displayPrice));

        descriptionField.setText(item.getDescription());
        categoryCombo.setSelectedItem(langManager.translateCategory(item.getCategory()));
        selectedItemId = item.getId();
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);
        addButton.setEnabled(true); // Keep Add button enabled

        // Image path & preview
        if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
            imagePathField.setText(item.getImagePath());
            try {
                ImageIcon icon = new ImageIcon(item.getImagePath());
                Image img = icon.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                imagePreview.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                imagePreview.setIcon(null);
            }
        } else {
            imagePathField.setText("");
            imagePreview.setIcon(null);
        }
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

    // Image getters
    public JTextField getImagePathField() { return imagePathField; }
    public JLabel getImagePreviewLabel() { return imagePreview; }
}
