package view;

import model.Member;
import controller.MembershipController;
import util.LanguageManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class MembershipView extends JPanel {
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private JTextField phoneField, nameField, totalSpentField, searchField;
    private JButton addButton, updateButton, deleteButton, clearButton, searchButton, refreshButton;
    private JLabel totalMembersLabel;
    private String selectedPhone;
    private LanguageManager langManager;
    private MembershipController membershipController;
    
    private TitledBorder topPanelBorder, bottomPanelBorder;
    private JLabel phoneLabel, nameLabel, totalSpentLabel, searchLabel, infoLabel, currencyHintLabel;
    
    public MembershipView() {
        langManager = LanguageManager.getInstance();
        membershipController = new MembershipController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
        setupListeners();
        setupLanguageListener();
        updateFonts(); // 초기 폰트 적용
        loadMembers();
    }
    
    private void initializeComponents() {
        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanelBorder = BorderFactory.createTitledBorder(langManager.getText("members_list"));
        topPanel.setBorder(topPanelBorder);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchLabel = new JLabel(langManager.getText("search") + ":");
        searchField = new JTextField(20);
        searchButton = new JButton(langManager.getText("search"));
        refreshButton = new JButton(langManager.getText("refresh"));
        totalMembersLabel = new JLabel(langManager.getText("total_members") + ": 0");
        
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
        memberTable.setRowHeight(25);
        
        // Column widths
        memberTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        memberTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        memberTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        memberTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        memberTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        memberTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        topPanel.add(new JScrollPane(memberTable), BorderLayout.CENTER);
        
        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanelBorder = BorderFactory.createTitledBorder(langManager.getText("add_edit_member"));
        bottomPanel.setBorder(bottomPanelBorder);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        phoneLabel = new JLabel(langManager.getText("phone_number") + ":");
        formPanel.add(phoneLabel);
        phoneField = new JTextField();
        formPanel.add(phoneField);
        
        nameLabel = new JLabel(langManager.getText("name") + ":");
        formPanel.add(nameLabel);
        nameField = new JTextField();
        formPanel.add(nameField);
        
        totalSpentLabel = new JLabel(langManager.getText("total_spent") + ":");
        formPanel.add(totalSpentLabel);
        
        JPanel totalSpentPanel = new JPanel(new BorderLayout());
        totalSpentField = new JTextField("0");
        totalSpentPanel.add(totalSpentField, BorderLayout.CENTER);
        currencyHintLabel = new JLabel();
        currencyHintLabel.setForeground(Color.GRAY);
        updateCurrencyLabel();
        totalSpentPanel.add(currencyHintLabel, BorderLayout.EAST);
        formPanel.add(totalSpentPanel);
        
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton(langManager.getText("add_member"));
        updateButton = new JButton(langManager.getText("update"));
        deleteButton = new JButton(langManager.getText("delete"));
        clearButton = new JButton(langManager.getText("clear_form"));
        
        // Style buttons
        addButton.setBackground(new Color(40, 167, 69)); addButton.setForeground(Color.BLACK);
        updateButton.setBackground(new Color(255, 193, 7)); updateButton.setForeground(Color.BLACK);
        deleteButton.setBackground(new Color(220, 53, 69)); deleteButton.setForeground(Color.BLACK);
        clearButton.setBackground(new Color(108, 117, 125)); clearButton.setForeground(Color.BLACK);
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(clearButton);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoLabel = new JLabel();
        infoLabel.setForeground(Color.DARK_GRAY);
        infoLabel.setText(langManager.getText("membership_info")); // 리소스 사용
        infoPanel.add(infoLabel);
        bottomPanel.add(infoPanel, BorderLayout.NORTH);
        
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    // [중요] 폰트 업데이트 메서드
    private void updateFonts() {
        String fontName = (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) ? "Malgun Gothic" : "Arial";
        Font plainFont = new Font(fontName, Font.PLAIN, 12);
        Font boldFont = new Font(fontName, Font.BOLD, 12);
        Font titleFont = new Font(fontName, Font.BOLD, 14);
        
        // Borders
        topPanelBorder.setTitleFont(titleFont);
        bottomPanelBorder.setTitleFont(titleFont);
        
        // Labels
        searchLabel.setFont(plainFont);
        totalMembersLabel.setFont(boldFont);
        phoneLabel.setFont(plainFont);
        nameLabel.setFont(plainFont);
        totalSpentLabel.setFont(plainFont);
        infoLabel.setFont(new Font(fontName, Font.ITALIC, 11));
        currencyHintLabel.setFont(new Font(fontName, Font.PLAIN, 11));
        
        // Fields & Table
        searchField.setFont(plainFont);
        phoneField.setFont(plainFont);
        nameField.setFont(plainFont);
        totalSpentField.setFont(plainFont);
        memberTable.setFont(plainFont);
        
        // Buttons
        searchButton.setFont(boldFont);
        refreshButton.setFont(boldFont);
        addButton.setFont(boldFont);
        updateButton.setFont(boldFont);
        deleteButton.setFont(boldFont);
        clearButton.setFont(boldFont);
        
        repaint();
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
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            memberTable.setModel(tableModel);
        } else {
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
        }
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(lang -> {
            updateLanguage();
            updateFonts();
        });
    }
    
    private void updateLanguage() {
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
        infoLabel.setText(langManager.getText("membership_info")); // 리소스 사용
        
        updateTableModel();
        updateMemberCount();
        updateCurrencyLabel();
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
    
    public void loadMembers() {
        List<Member> members = membershipController.getAllMembers();
        displayMembers(members);
        updateMemberCount();
    }
    
    private void displayMembers(List<Member> members) {
        tableModel.setRowCount(0);
        for (Member member : members) {
            String totalSpentDisplay;
            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                double wonAmount = Math.round(member.getTotalSpent() * 1200);
                totalSpentDisplay = "₩ " + String.format("%,d", (long)wonAmount);
            } else {
                totalSpentDisplay = langManager.formatPrice(member.getTotalSpent());
            }
            
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
    
    private void setupListeners() {
        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && memberTable.getSelectedRow() != -1) {
                int selectedRow = memberTable.getSelectedRow();
                selectedPhone = (String) tableModel.getValueAt(selectedRow, 0);
                phoneField.setText(selectedPhone);
                nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                
                Member member = membershipController.getMemberByPhone(selectedPhone);
                if (member != null) {
                    if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                        double wonAmount = Math.round(member.getTotalSpent() * 1200);
                        totalSpentField.setText(String.format("%.0f", wonAmount));
                    } else {
                        totalSpentField.setText(String.format("%.2f", member.getTotalSpent()));
                    }
                }
            }
        });
        
        addButton.addActionListener(e -> addMember());
        updateButton.addActionListener(e -> updateMember());
        deleteButton.addActionListener(e -> deleteMember());
        clearButton.addActionListener(e -> clearForm());
        searchButton.addActionListener(e -> searchMembers());
        searchField.addActionListener(e -> searchMembers());
        refreshButton.addActionListener(e -> loadMembers());
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
        
        if (phone.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone and Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Remove hyphens for storage
        String formattedPhone = phone.replace("-", "");
        if(formattedPhone.length() == 10) formattedPhone = formattedPhone.substring(0,3)+"-"+formattedPhone.substring(3,6)+"-"+formattedPhone.substring(6);
        else if(formattedPhone.length() == 11) formattedPhone = formattedPhone.substring(0,3)+"-"+formattedPhone.substring(3,7)+"-"+formattedPhone.substring(7);
        
        double totalSpent = 0;
        try {
            if (!totalSpentStr.isEmpty()) {
                double inputAmount = Double.parseDouble(totalSpentStr);
                if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                    totalSpent = inputAmount / 1200.0;
                } else {
                    totalSpent = inputAmount;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (membershipController.addMember(formattedPhone, name, totalSpent)) {
            loadMembers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add member.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateMember() {
        if (selectedPhone == null) return;
        
        String newPhone = phoneField.getText().trim();
        String name = nameField.getText().trim();
        String totalSpentStr = totalSpentField.getText().trim();
        
        double totalSpent = 0;
        try {
            double inputAmount = Double.parseDouble(totalSpentStr);
            if (langManager.getCurrentLanguage() == LanguageManager.Language.KOREAN) {
                totalSpent = inputAmount / 1200.0;
            } else {
                totalSpent = inputAmount;
            }
        } catch (NumberFormatException e) { return; }
        
        if (membershipController.updateMember(selectedPhone, newPhone, name, totalSpent)) {
            loadMembers();
            clearForm();
        }
    }
    
    private void deleteMember() {
        if (selectedPhone == null) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete member?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            membershipController.deleteMember(selectedPhone);
            loadMembers();
            clearForm();
        }
    }
    
    private void clearForm() {
        phoneField.setText("");
        nameField.setText("");
        totalSpentField.setText("0");
        selectedPhone = null;
        memberTable.clearSelection();
    }
    
    public MembershipController getMembershipController() { return membershipController; }
}