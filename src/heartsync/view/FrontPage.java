package heartsync.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import heartsync.view.Swipe;
import heartsync.view.ChatWindow;

public class FrontPage extends JFrame {
    private JPanel mainPanel;
    private JPanel matchedUsersPanel;
    private JScrollPane scrollPane;
    private int currentUserId = 1; // Placeholder for logged-in user

    public FrontPage() {
        setTitle("HeartSync - Messages");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250, 245, 250));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(128, 0, 64));
        header.setPreferredSize(new Dimension(900, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(233, 54, 128)));
        
        JLabel appName = new JLabel("HEARTSYNC");
        appName.setFont(new Font("SansSerif", Font.BOLD, 36));
        appName.setForeground(Color.PINK);
        appName.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 28));
        backBtn.setBackground(new Color(128, 0, 64));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setOpaque(true);
        backBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            Swipe swipeView = new Swipe();
            swipeView.setVisible(true);
            dispose();
        });
        
        header.add(appName, BorderLayout.WEST);
        header.add(backBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Center: Matched users list
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(250, 245, 250));
        
        JLabel matchedLabel = new JLabel("Messages");
        matchedLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        matchedLabel.setForeground(new Color(128, 0, 64));
        matchedLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        matchedUsersPanel = new JPanel();
        matchedUsersPanel.setLayout(new BoxLayout(matchedUsersPanel, BoxLayout.Y_AXIS));
        matchedUsersPanel.setBackground(new Color(250, 245, 250));
        
        // Add some dummy matched users
        addMatchedUser("John Doe", "Hey, how are you?", "10:30 AM");
        addMatchedUser("Jane Smith", "Would you like to meet for coffee?", "Yesterday");
        addMatchedUser("Mike Johnson", "Thanks for the match!", "2 days ago");
        
        scrollPane = new JScrollPane(matchedUsersPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        
        centerPanel.add(matchedLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void addMatchedUser(String name, String lastMessage, String time) {
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(Color.WHITE);
        userPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(233, 54, 128)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        userPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // User info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setForeground(new Color(128, 0, 64));
        
        JLabel messageLabel = new JLabel(lastMessage);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(102, 102, 102));
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        timeLabel.setForeground(new Color(153, 153, 153));
        
        infoPanel.add(nameLabel, BorderLayout.NORTH);
        infoPanel.add(messageLabel, BorderLayout.CENTER);
        
        userPanel.add(infoPanel, BorderLayout.CENTER);
        userPanel.add(timeLabel, BorderLayout.EAST);
        
        // Add click listener to open chat
        userPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ChatWindow chatWindow = new ChatWindow(currentUserId, 2); // 2 is a placeholder for the other user's ID
                chatWindow.setVisible(true);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                userPanel.setBackground(new Color(255, 230, 245));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                userPanel.setBackground(Color.WHITE);
            }
        });
        
        matchedUsersPanel.add(userPanel);
        matchedUsersPanel.add(Box.createVerticalStrut(1));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrontPage().setVisible(true);
        });
    }
} 