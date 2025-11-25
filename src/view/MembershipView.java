package view;

import model.Member;
import controller.MembershipController;
import util.LanguageManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * View for managing member registrations and viewing membership information
 */
public class MembershipView extends JPanel {
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private JTextField phoneField;
    private JTextField nameField;
    private JTextField totalSpentField;
    private JTextField searchField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton searchButton;
    private JButton refreshButton;
    private JLabel totalMembersLabel;
    private String selectedPhone;
    private LanguageManager langManager;
    private MembershipController membershipController;
    
    private TitledBorder topPanelBorder;
    private TitledBorder bottomPanelBorder;
    private JLabel phoneLabel;
    private JLabel nameLabel;
    private JLabel totalSpentLabel;
    private JLabel searchLabel;
    private JLabel infoLabel;
    private JLabel currencyHintLabel;
    
    public MembershipView() {
        langManager = LanguageManager.getInstance();
        membershipController = new MembershipController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
        setupListeners();
        setupLanguageListener();
        loadMembers();
    }
    
    private void initializeComponents() {
        // Top panel - Member table with search
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanelBorder = BorderFactory.createTitledBorder(langManager.getText("members_list"));
        topPanel.setBorder(topPanelBorder);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchLabel = new JLabel(langManager.getText("search") + ":");
        searchField = new JTextField(20);
        searchButton = new JButton(langManager.getText("search"));
        searchButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        refreshButton = new JButton(langManager.getText("refresh"));
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        totalMembersLabel = new JLabel(langManager.getText("total_members") + ": 0");
        totalMembersLabel.setFont(new Font("Arial", Font.BOLD, 13));
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(totalMembersLabel);
        
        topPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Table
        updateTableModel();
        
        memberTable = new JTable(tableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.setFont(new Font("Arial", Font.PLAIN, 12));
        memberTable.setRowHeight(25);
        
        // Set column widths
        memberTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Phone
        memberTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        memberTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Total Spent
        memberTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Level
        memberTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Discount
        memberTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Next Level
        
        JScrollPane tableScroll = new JScrollPane(memberTable);
        topPanel.add(tableScroll, BorderLayout.CENTER);
        
        // Bottom panel - Form for adding/editing
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanelBorder = BorderFactory.createTitledBorder(langManager.getText("add_edit_member"));
        bottomPanel.setBorder(bottomPanelBorder);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        phoneLabel = new JLabel(langManager.getText("phone_number") + ":");
        formPanel.add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(phoneField);
        
        nameLabel = new JLabel(langManager.getText("name") + ":");
        formPanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(nameField);
        
        totalSpentLabel = new JLabel(langManager.getText("total_spent") + ":");
        formPanel.add(totalSpentLabel);
        JPanel totalSpentPanel = new JPanel(new BorderLayout());
        totalSpentField = new JTextField("0");
        totalSpentField.setFont(new Font("Arial", Font.PLAIN, 14));
        totalSpentPanel.add(totalSpentField, BorderLayout.CENTER);
        currencyHintLabel = new JLabel();
        currencyHintLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        currencyHintLabel.setForeground(Color.GRAY);
        updateCurrencyLabel();
        totalSpentPanel.add(currencyHintLabel, BorderLayout.EAST);
        formPanel.add(totalSpentPanel);
        
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = new JButton(langManager.getText("add_member"));
        addButton.setFont(new Font("Arial", Font.BOLD, 13));
        addButton.setPreferredSize(new Dimension(130, 35));
        
        updateButton = new JButton(langManager.getText("update"));
        updateButton.setFont(new Font("Arial", Font.BOLD, 13));
        updateButton.setPreferredSize(new Dimension(130, 35));
        
        deleteButton = new JButton(langManager.getText("delete"));
        deleteButton.setFont(new Font("Arial", Font.BOLD, 13));
        deleteButton.setPreferredSize(new Dimension(130, 35));
        
        clearButton = new JButton(langManager.getText("clear_form"));
        clearButton.setFont(new Font("Arial", Font.BOLD, 13));
        clearButton.setPreferredSize(new Dimension(130, 35));
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(clearButton);
        
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        // Info panel - will be updated based on language
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoLabel = new JLabel();
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        infoLabel.setForeground(Color.DARK_GRAY);
        updateInfoLabel();
        infoPanel.add(infoLabel);
        bottomPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Add panels to main view
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void updateTableModel() {
        String[] columnNames = {
            langManager.getText("phone_number"), 
            langManager.getText("name"), 
            langManager.getText("total_spent"), 
            langManager.getText("level"), 
            langManager.getText("discount"), 
            langManager.getText("to_next_level")
        };
        
        if (memberTable != null) {
            // Update existing table model
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            memberTable.setModel(tableModel);
        } else {
            // Initial creation
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        }
    }
    
    private void setupListeners() {
        // Table selection listener
        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && memberTable.getSelectedRow() != -1) {
                int selectedRow = memberTable.getSelectedRow();
                selectedPhone = (String) tableModel.getValueAt(selectedRow, 0);
                phoneField.setText(selectedPhone);
                nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                
                // Get the actual member to get totalSpent value
                Member member = membershipController.getMemberByPhone(selectedPhone);
                if (member != null) {
                    // Show amount in current language format
                    if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                        // Show in Won (multiply by 1200) - use precise value
                        double wonAmount = Math.round(member.getTotalSpent() * 1200);
                        totalSpentField.setText(String.format("%.0f", wonAmount));
                    } else {
                        // Show in dollars
                        totalSpentField.setText(String.format("%.2f", member.getTotalSpent()));
                    }
                }
            }
        });
        
        // Add button
        addButton.addActionListener(e -> addMember());
        
        // Update button
        updateButton.addActionListener(e -> updateMember());
        
        // Delete button
        deleteButton.addActionListener(e -> deleteMember());
        
        // Clear button
        clearButton.addActionListener(e -> clearForm());
        
        // Search button
        searchButton.addActionListener(e -> searchMembers());
        
        // Search field enter key
        searchField.addActionListener(e -> searchMembers());
        
        // Refresh button
        refreshButton.addActionListener(e -> loadMembers());
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(lang -> updateLanguage());
    }
    
    private void updateLanguage() {
        // Update UI text based on language
        topPanelBorder.setTitle(langManager.getText("members_list"));
        bottomPanelBorder.setTitle(langManager.getText("add_edit_member"));
        searchLabel.setText(langManager.getText("search") + ":");
        phoneLabel.setText(langManager.getText("phone_number") + ":");
        nameLabel.setText(langManager.getText("name") + ":");
        totalSpentLabel.setText(langManager.getText("total_spent") + ":");
        addButton.setText(langManager.getText("add_member"));
        updateButton.setText(langManager.getText("update"));
        deleteButton.setText(langManager.getText("delete"));
        clearButton.setText(langManager.getText("clear_form"));
        searchButton.setText(langManager.getText("search"));
        refreshButton.setText(langManager.getText("refresh"));
        
        // Update table column headers
        updateTableModel();
        
        // Update total members label
        updateMemberCount();
        
        // Update info label with correct currency
        updateInfoLabel();
        updateCurrencyLabel();
        
        // Refresh the member display to show currency in new format
        loadMembers();
        repaint();
    }
    
    private void updateCurrencyLabel() {
        if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
            currencyHintLabel.setText(" (Won)");
        } else {
            currencyHintLabel.setText(" ($)");
        }
    }
    
    private void updateInfoLabel() {
        if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
            infoLabel.setText("<html><b>회원 등급:</b> Level 5 (0%), Level 4 (5% ≥₩500,000), Level 3 (10% ≥₩1,000,000), Level 2 (15% ≥₩2,000,000), Level 1 (20% ≥₩3,000,000)</html>");
        } else {
            infoLabel.setText("<html><b>Membership Levels:</b> Level 5 (0%), Level 4 (5% ≥$417), Level 3 (10% ≥$833), Level 2 (15% ≥$1,667), Level 1 (20% ≥$2,500)</html>");
        }
    }
    
    public void loadMembers() {
        List<Member> members = membershipController.getAllMembers();
        displayMembers(members);
        updateMemberCount();
    }
    
    private void displayMembers(List<Member> members) {
        tableModel.setRowCount(0);
        
        for (Member member : members) {
            // Format total spent with proper rounding
            String totalSpentDisplay;
            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                double wonAmount = Math.round(member.getTotalSpent() * 1200);
                totalSpentDisplay = "₩ " + String.format("%,d", (long)wonAmount);
            } else {
                totalSpentDisplay = langManager.formatPrice(member.getTotalSpent());
            }
            
            // Format amount to next level with proper rounding
            String toNextLevelDisplay;
            if (member.getMembershipLevel() == 1) {
                toNextLevelDisplay = langManager.getText("max_level");
            } else {
                if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                    double wonAmount = Math.round(member.getAmountToNextLevel() * 1200);
                    toNextLevelDisplay = "₩ " + String.format("%,d", (long)wonAmount);
                } else {
                    toNextLevelDisplay = langManager.formatPrice(member.getAmountToNextLevel());
                }
            }
            
            Object[] row = {
                member.getPhoneNumber(),
                member.getName(),
                totalSpentDisplay,
                "Level " + member.getMembershipLevel(),
                String.format("%.0f%%", member.getDiscountPercent()),
                toNextLevelDisplay
            };
            tableModel.addRow(row);
        }
    }
    
    private void updateMemberCount() {
        int count = membershipController.getTotalMemberCount();
        totalMembersLabel.setText(langManager.getText("total_members") + ": " + count);
    }
    
    private void searchMembers() {
        String searchTerm = searchField.getText().trim();
        List<Member> members = membershipController.searchMembers(searchTerm);
        displayMembers(members);
    }
    
    private void addMember() {
        String phone = phoneField.getText().trim();
        String name = nameField.getText().trim();
        String totalSpentStr = totalSpentField.getText().trim();
        
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone number cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Format phone number with hyphens
        String formattedPhone = formatPhoneNumber(phone);
        
        // Get total spent amount
        double totalSpent = 0;
        try {
            if (!totalSpentStr.isEmpty()) {
                double inputAmount = Double.parseDouble(totalSpentStr);
                if (inputAmount < 0) {
                    JOptionPane.showMessageDialog(this, "Total spent cannot be negative!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // If Korean language, convert Won to base currency (dollars)
                if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                    // Store precise value: round to nearest won, then convert
                    double roundedWon = Math.round(inputAmount);
                    totalSpent = roundedWon / 1200.0;
                } else {
                    totalSpent = inputAmount;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid total spent format!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (membershipController.addMember(formattedPhone, name, totalSpent)) {
            JOptionPane.showMessageDialog(this, "Member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadMembers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add member. Phone number may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String formatPhoneNumber(String phone) {
        // Remove all hyphens first
        String cleanPhone = phone.replace("-", "");
        
        // Format as XXX-XXXX-XXXX if it's 10-11 digits
        if (cleanPhone.length() == 10) {
            return cleanPhone.substring(0, 3) + "-" + cleanPhone.substring(3, 6) + "-" + cleanPhone.substring(6);
        } else if (cleanPhone.length() == 11) {
            return cleanPhone.substring(0, 3) + "-" + cleanPhone.substring(3, 7) + "-" + cleanPhone.substring(7);
        }
        
        // Return as-is if not standard format
        return phone;
    }
    
    private void updateMember() {
        if (selectedPhone == null || selectedPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a member from the table first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String newPhone = phoneField.getText().trim();
        String name = nameField.getText().trim();
        String totalSpentStr = totalSpentField.getText().trim();
        
        if (newPhone.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone and name cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Format phone number with hyphens
        String formattedPhone = formatPhoneNumber(newPhone);
        
        double totalSpent;
        try {
            double inputAmount = Double.parseDouble(totalSpentStr);
            if (inputAmount < 0) {
                JOptionPane.showMessageDialog(this, "Total spent cannot be negative!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // If Korean language, convert Won to base currency (dollars)
            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                // Store precise value: round to nearest won, then convert
                double roundedWon = Math.round(inputAmount);
                totalSpent = roundedWon / 1200.0; // Convert Won to dollars with precision
            } else {
                totalSpent = inputAmount;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid total spent format!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (membershipController.updateMember(selectedPhone, formattedPhone, name, totalSpent)) {
            JOptionPane.showMessageDialog(this, "Member updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadMembers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update member!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteMember() {
        if (selectedPhone == null || selectedPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a member from the table first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this member?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (membershipController.deleteMember(selectedPhone)) {
                JOptionPane.showMessageDialog(this, "Member deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMembers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete member!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearForm() {
        phoneField.setText("");
        nameField.setText("");
        totalSpentField.setText("0.0");
        selectedPhone = null;
        memberTable.clearSelection();
    }
    
    public MembershipController getMembershipController() {
        return membershipController;
    }
}

