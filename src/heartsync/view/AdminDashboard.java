package heartsync.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import heartsync.controller.LoginController;
import heartsync.navigation.WindowManager;

public class AdminDashboard extends JFrame {
    // Managed by WindowManager – no internal singleton needed
    
    public AdminDashboard() {
        setTitle("Admin Dashboard - Coming Soon");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        // Add window listener to handle window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // When admin closes the dashboard, show the login screen again
                dispose();
                heartsync.controller.LoginController.createAndShowLoginView();
            }
        });
        
        // Create main panel with coming soon message
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        
        JLabel comingSoonLabel = new JLabel("Admin Dashboard - Coming Soon", JLabel.CENTER);
        comingSoonLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" +
                "This section is currently under development.<br>" +
                "Please check back later for admin features." +
                "</div></html>", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Add logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            heartsync.controller.LoginController.createAndShowLoginView();
        });
        
        // Add components to panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(logoutButton);
        
        mainPanel.add(comingSoonLabel, BorderLayout.NORTH);
        mainPanel.add(messageLabel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    public static void showAdminDashboard() {
        WindowManager.show(AdminDashboard.class, AdminDashboard::new, null);
    }
}
