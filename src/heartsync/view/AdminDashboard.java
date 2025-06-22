package heartsync.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import heartsync.controller.LoginController;
import heartsync.navigation.WindowManager;
import heartsync.database.TestUserCleanup;
import javax.swing.JOptionPane;

public class AdminDashboard extends JFrame {
    private ReviewAndApprove reviewPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JPanel navigationPanel;
    private JLabel currentNavItem;
    private static HomePage homePageInstance = null;
    
    // Color scheme matching HomePage
    private static final Color BACKGROUND_COLOR = new Color(255, 218, 225); // Light pink background
    private static final Color NAV_BACKGROUND = Color.WHITE; // White navigation background
    private static final Color ACTIVE_NAV_COLOR = new Color(229, 89, 36); // Orange for active items
    private static final Color NAV_TEXT_COLOR = new Color(51, 51, 51); // Dark gray for nav text
    private static final Color CONTENT_BACKGROUND = new Color(242, 242, 242); // Light gray for content area
    
    public AdminDashboard() {
        setTitle("HeartSync Admin Dashboard");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 900);
        setResizable(false);
        setLocationRelativeTo(null);
        
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
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BACKGROUND_COLOR);
        
        // Create navigation panel
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        navigationPanel.setBackground(NAV_BACKGROUND);
        
        // Add logo
        JPanel logoPanel = createLogoPanel();
        navigationPanel.add(logoPanel);
        
        // Add spacing after logo
        navigationPanel.add(Box.createHorizontalStrut(50));
        
        // Add navigation labels
        JLabel reviewLabel = createNavLabel("Review and Approve");
        JLabel deleteLabel = createNavLabel("Delete User Account");
        JLabel reportedLabel = createNavLabel("View Reported Users");
        
        navigationPanel.add(reviewLabel);
        navigationPanel.add(deleteLabel);
        navigationPanel.add(reportedLabel);
        
        // Add cleanup button
        JButton cleanupButton = new JButton("🧹 Clean Test Users");
        cleanupButton.setOpaque(true);
        cleanupButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        cleanupButton.setForeground(Color.WHITE);
        cleanupButton.setBackground(new Color(255, 140, 0)); // Orange background
        cleanupButton.setBorderPainted(false);
        cleanupButton.setFocusPainted(false);
        cleanupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cleanupButton.setPreferredSize(new Dimension(150, 35));
        
        // Add hover effect
        cleanupButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cleanupButton.setBackground(new Color(255, 140, 0).darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                cleanupButton.setBackground(new Color(255, 140, 0));
            }
        });
        
        // Add cleanup functionality
        cleanupButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                this,
                "This will permanently remove ALL test/demo users from the system.\n" +
                "This includes:\n" +
                "• All test user profiles and data\n" +
                "• All likes, matches, and notifications\n" +
                "• All authentication records\n\n" +
                "Are you sure you want to continue?",
                "Confirm Test User Cleanup",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                // Show progress dialog
                JOptionPane.showMessageDialog(
                    this,
                    "Cleanup started. Check console for progress.\nThis may take a few moments...",
                    "Cleanup In Progress",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Run cleanup in background thread
                new Thread(() -> {
                    try {
                        TestUserCleanup.runCleanup();
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(
                                this,
                                "Test user cleanup completed successfully!\n" +
                                "All test/demo users have been removed from the system.",
                                "Cleanup Complete",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        });
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(
                                this,
                                "Error during cleanup: " + ex.getMessage(),
                                "Cleanup Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                        });
                    }
                }).start();
            }
        });
        
        navigationPanel.add(cleanupButton);
        
        // Add spacing before logout button
        navigationPanel.add(Box.createHorizontalStrut(50));
        
        // Create a wrapper panel for the logout button
        JPanel logoutWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutWrapper.setBackground(NAV_BACKGROUND);
        
        // Add logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setOpaque(true);
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Color.RED);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        
        // Add hover effect
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(Color.RED.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(Color.RED);
            }
        });
        
        // Add logout functionality
        logoutButton.addActionListener(e -> {
            dispose(); // Close AdminDashboard
            // Create and show a fresh HomePage
            HomePage homePage = new HomePage();
            homePage.setLocationRelativeTo(null);
            homePage.setVisible(true);
        });
        
        logoutWrapper.add(logoutButton);
        navigationPanel.add(logoutWrapper);
        
        // Create card layout for main content
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(CONTENT_BACKGROUND);
        
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
        
        setContentPane(mainContainer);
        
        // Set initial navigation item
        setCurrentNavItem(reviewLabel);
    }
    
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        logoPanel.setOpaque(false);
        
        JLabel heartIcon = new JLabel("❤");
        heartIcon.setFont(new Font("SansSerif", Font.PLAIN, 20));
        heartIcon.setForeground(new Color(255, 89, 89));
        
        JLabel heartLabel = new JLabel("Heart");
        heartLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        heartLabel.setForeground(new Color(255, 89, 89));
        
        JLabel syncLabel = new JLabel("Sync");
        syncLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        syncLabel.setForeground(new Color(128, 128, 128));
        
        logoPanel.add(heartIcon);
        logoPanel.add(heartLabel);
        logoPanel.add(syncLabel);
        
        return logoPanel;
    }
    
    private JLabel createNavLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setForeground(NAV_TEXT_COLOR);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setCurrentNavItem(label);
                cardLayout.show(mainContentPanel, text);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (label != currentNavItem) {
                    label.setForeground(ACTIVE_NAV_COLOR);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (label != currentNavItem) {
                    label.setForeground(NAV_TEXT_COLOR);
                }
            }
        });
        
        return label;
    }
    
    private void setCurrentNavItem(JLabel navItem) {
        if (currentNavItem != null) {
            currentNavItem.setForeground(NAV_TEXT_COLOR);
            currentNavItem.setBorder(null);
        }
        
        currentNavItem = navItem;
        currentNavItem.setForeground(ACTIVE_NAV_COLOR);
        currentNavItem.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACTIVE_NAV_COLOR));
    }
    
    public static void showAdminDashboard() {
        WindowManager.show(AdminDashboard.class, AdminDashboard::new, null);
    }
}
