package view;

import util.LanguageManager;
import util.LanguageManager.Language;
import javax.swing.*;
import java.awt.*;

/**
 * Main application window with tabbed interface
 */
public class MainView extends JFrame {
    private JTabbedPane tabbedPane;
    private OrderView orderView;
    private MenuManagementView menuManagementView;
    private SalesView salesView;
    private JButton englishButton;
    private JButton koreanButton;
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
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Top panel with language buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        topPanel.setBackground(new Color(240, 240, 240));
        
        JLabel langLabel = new JLabel("Language:");
        langLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        englishButton = new JButton("English");
        englishButton.setFont(new Font("Arial", Font.BOLD, 11));
        englishButton.setBackground(new Color(0, 123, 255));
        englishButton.setForeground(Color.BLACK);
        englishButton.setFocusPainted(false);
        englishButton.setPreferredSize(new Dimension(80, 30));
        
        koreanButton = new JButton("한국어");
        koreanButton.setFont(new Font("Arial", Font.BOLD, 11));
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
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Create views
        orderView = new OrderView();
        menuManagementView = new MenuManagementView();
        salesView = new SalesView();
        
        // Add tabs
        updateTabTitles();
        
        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
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
    
    private void updateTabTitles() {
        tabbedPane.removeAll();
        tabbedPane.addTab(langManager.getText("tab_order"), orderView);
        tabbedPane.addTab(langManager.getText("tab_menu"), menuManagementView);
        tabbedPane.addTab(langManager.getText("tab_sales"), salesView);
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> {
            setTitle(langManager.getText("app_title"));
            updateTabTitles();
        });
    }
    
    private void applyModernStyling() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public OrderView getOrderView() {
        return orderView;
    }
    
    public MenuManagementView getMenuManagementView() {
        return menuManagementView;
    }
    
    public SalesView getSalesView() {
        return salesView;
    }
    
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}

