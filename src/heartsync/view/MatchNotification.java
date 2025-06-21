package heartsync.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import heartsync.model.UserProfile;
import heartsync.model.User;
import heartsync.database.DatabaseManagerProfile;

public class MatchNotification extends JDialog {
    private static final Color PINK_COLOR = new Color(255, 182, 193);
    private static final Color DARK_PINK = new Color(219, 112, 147);
    
    public MatchNotification(JFrame parent, String userId1, String userId2) {
        super(parent, "New Match! 💘", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        // Get user profiles
        UserProfile user1 = DatabaseManagerProfile.getInstance().getUserProfile(userId1);
        UserProfile user2 = DatabaseManagerProfile.getInstance().getUserProfile(userId2);
        
        // Create main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PINK_COLOR, 0, h, DARK_PINK);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Add match icon
        JLabel iconLabel = new JLabel("💘", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add match text
        String matchText = String.format("<html><center>Congratulations!<br><br>%s and %s<br>have matched!</center></html>",
            user1.getFullName(), user2.getFullName());
        JLabel matchLabel = new JLabel(matchText);
        matchLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        matchLabel.setForeground(Color.WHITE);
        matchLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add description
        JLabel descLabel = new JLabel("<html><center>You can now chat with each other!<br>Say hello!</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(Color.WHITE);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        
        // Start Chat button
        JButton chatButton = new JButton("Start Chatting");
        chatButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        chatButton.setBackground(Color.WHITE);
        chatButton.setForeground(DARK_PINK);
        chatButton.addActionListener(e -> {
            dispose();
            // Open chat window
            User matchedUser = new User();
            matchedUser.setUsername(userId2);
            new ChatSystem(matchedUser).setVisible(true);
        });
        
        // Maybe Later button
        JButton laterButton = new JButton("Maybe Later");
        laterButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        laterButton.setBackground(new Color(255, 255, 255, 180));
        laterButton.setForeground(DARK_PINK);
        laterButton.addActionListener(e -> dispose());
        
        // Add components
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(matchLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(descLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        buttonPanel.add(chatButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(laterButton);
        
        contentPanel.add(buttonPanel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
} 