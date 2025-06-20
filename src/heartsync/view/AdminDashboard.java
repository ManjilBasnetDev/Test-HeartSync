package heartsync.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import heartsync.controller.LoginController;
import heartsync.navigation.WindowManager;

public class AdminDashboard extends JFrame {
    private ReviewAndApprove reviewPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JPanel navigationPanel;
    private static final Color PRIMARY_COLOR = new Color(255, 192, 203); // Light pink color
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color SELECTED_NAV_COLOR = new Color(255, 182, 193); // Slightly darker pink for selected state
    
    public AdminDashboard() {
        setTitle("HeartSync Admin Dashboard");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 900); // Match HomePage size
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Add window listener to handle window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                heartsync.controller.LoginController.createAndShowLoginView();
            }
        });
        
        setupUI();
    }
    
    private void setupUI() {
        // Main container with background color
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BACKGROUND_COLOR);
        
        // Create navigation panel
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navigationPanel.setBackground(PRIMARY_COLOR);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Create navigation buttons
        String[] navItems = {"Review and Approve", "Delete User Account", "View Reported Users"};
        ButtonGroup navGroup = new ButtonGroup();
        
        for (String item : navItems) {
            JToggleButton navButton = createNavButton(item);
            navGroup.add(navButton);
            navigationPanel.add(navButton);
            
            // Add action listener
            navButton.addActionListener(e -> switchPanel(item));
        }
        
        // Add logout button
        JButton logoutButton = new JButton("Logout");
        styleLogoutButton(logoutButton);
        navigationPanel.add(logoutButton);
        
        // Create card layout for main content
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(BACKGROUND_COLOR);
        
        // Initialize panels
        reviewPanel = new ReviewAndApprove();
        JPanel deleteUserPanel = new DeleteUserPanel();
        JPanel reportedUsersPanel = new ReportedUsersPanel();
        
        // Add panels to card layout
        mainContentPanel.add(reviewPanel, "Review and Approve");
        mainContentPanel.add(deleteUserPanel, "Delete User Account");
        mainContentPanel.add(reportedUsersPanel, "View Reported Users");
        
        // Add components to main container
        mainContainer.add(navigationPanel, BorderLayout.NORTH);
        mainContainer.add(mainContentPanel, BorderLayout.CENTER);
        
        // Set the content pane
        setContentPane(mainContainer);
        
        // Select the first button by default
        ((JToggleButton)navigationPanel.getComponent(0)).setSelected(true);
    }
    
    private JToggleButton createNavButton(String text) {
        JToggleButton button = new JToggleButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.BLACK); // Changed to black for better contrast on pink
        button.setBackground(PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addChangeListener(e -> {
            if (button.isSelected()) {
                button.setBackground(SELECTED_NAV_COLOR);
                button.setContentAreaFilled(true);
            } else {
                button.setContentAreaFilled(false);
            }
        });
        
        return button;
    }
    
    private void styleLogoutButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(231, 76, 60));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> {
            dispose();
            heartsync.controller.LoginController.createAndShowLoginView();
        });
    }
    
    private void switchPanel(String panelName) {
        cardLayout.show(mainContentPanel, panelName);
    }
    
    public static void showAdminDashboard() {
        WindowManager.show(AdminDashboard.class, AdminDashboard::new, null);
    }
}
