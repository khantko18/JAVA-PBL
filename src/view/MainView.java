package view;

import util.LanguageManager;
import util.LanguageManager.Language;
import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private JTabbedPane tabbedPane;
    private OrderView orderView;
    private MenuManagementView menuManagementView;
    private MembershipView membershipView;
    private SalesView salesView;
    private JButton englishButton;
    private JButton koreanButton;
    private JLabel langLabel;
    private JPanel topPanel;
    private LanguageManager langManager;
    
    public MainView() {
        langManager = LanguageManager.getInstance();
        
        setTitle(langManager.getText("app_title"));
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeComponents();
        applyModernStyling();
        setupLanguageListener();
        
        updateFonts();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Top panel
        topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        topPanel.setBackground(new Color(240, 240, 240));
        
        langLabel = new JLabel("Language:");
        
        englishButton = new JButton("English");
        englishButton.setBackground(new Color(0, 123, 255));
        englishButton.setForeground(Color.BLACK);
        englishButton.setFocusPainted(false);
        englishButton.setPreferredSize(new Dimension(80, 30));
        
        koreanButton = new JButton("한국어");
        koreanButton.setBackground(Color.WHITE);
        koreanButton.setForeground(Color.BLACK);
        koreanButton.setFocusPainted(false);
        koreanButton.setPreferredSize(new Dimension(80, 30));
        
        englishButton.addActionListener(e -> {
            langManager.setLanguage(Language.ENGLISH);
            updateLanguageButtons();
        });
        
        koreanButton.addActionListener(e -> {
            langManager.setLanguage(Language.KOREAN);
            updateLanguageButtons();
        });
        
        topPanel.add(langLabel);
        topPanel.add(englishButton);
        topPanel.add(koreanButton);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create views
        orderView = new OrderView();
        menuManagementView = new MenuManagementView();
        membershipView = new MembershipView();
        salesView = new SalesView();
        
        // Add tabs
        updateTabTitles();
        
        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        
        initializeLanguageButtons();
    }
    
    // [핵심] 언어 버튼 폰트 설정 수정
    private void updateFonts() {
        String fontName = (langManager.getCurrentLanguage() == Language.KOREAN) ? "Malgun Gothic" : "Arial";
        
        Font labelFont = new Font(fontName, Font.BOLD, 12);
        Font btnFont = new Font(fontName, Font.BOLD, 11);
        Font tabFont = new Font(fontName, Font.BOLD, 14);
        
        // "한국어" 버튼은 언어 설정과 상관없이 항상 한글 폰트를 사용해야 깨지지 않음
        Font koreanFont = new Font("Malgun Gothic", Font.BOLD, 11);
        
        langLabel.setFont(labelFont);
        englishButton.setFont(btnFont); // English는 Arial로도 잘 보임
        koreanButton.setFont(koreanFont); // [수정] 항상 맑은 고딕 고정
        
        tabbedPane.setFont(tabFont);
        
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    private void updateLanguageButtons() {
        Language current = langManager.getCurrentLanguage();
        if (current == Language.ENGLISH) {
            englishButton.setBackground(new Color(0, 123, 255));
            koreanButton.setBackground(Color.WHITE);
        } else {
            englishButton.setBackground(Color.WHITE);
            koreanButton.setBackground(new Color(0, 123, 255));
        }
    }
    
    private void initializeLanguageButtons() {
        updateLanguageButtons();
    }
    
    private void updateTabTitles() {
        if (tabbedPane.getTabCount() == 4) {
            tabbedPane.setTitleAt(0, langManager.getText("tab_order"));
            tabbedPane.setTitleAt(1, langManager.getText("tab_menu"));
            tabbedPane.setTitleAt(2, langManager.getText("tab_membership"));
            tabbedPane.setTitleAt(3, langManager.getText("tab_sales"));
        } else {
            tabbedPane.removeAll();
            tabbedPane.addTab(langManager.getText("tab_order"), orderView);
            tabbedPane.addTab(langManager.getText("tab_menu"), menuManagementView);
            tabbedPane.addTab(langManager.getText("tab_membership"), membershipView);
            tabbedPane.addTab(langManager.getText("tab_sales"), salesView);
        }
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> {
            setTitle(langManager.getText("app_title"));
            updateTabTitles();
            updateFonts();
        });
    }
    
    private void applyModernStyling() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public OrderView getOrderView() { return orderView; }
    public MenuManagementView getMenuManagementView() { return menuManagementView; }
    public MembershipView getMembershipView() { return membershipView; }
    public SalesView getSalesView() { return salesView; }
    public JTabbedPane getTabbedPane() { return tabbedPane; }
}