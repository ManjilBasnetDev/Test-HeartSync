package heartsync.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
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
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

import heartsync.model.User;
import heartsync.model.Chat;
import heartsync.dao.ChatDAO;
import heartsync.dao.ReportDAO;

public class MessageBox extends JFrame {
    private static final int WINDOW_RADIUS = 20;
    private static final Color BACKGROUND_COLOR = new Color(255, 192, 203); // Baby pink #FFC0CB
    private static final Color HEADER_COLOR = new Color(219, 112, 147); // PaleVioletRed - complementary to baby pink
    private static final Color MESSAGE_SENT_COLOR = new Color(219, 112, 147); // Matching header color
    private static final Color MESSAGE_RECEIVED_COLOR = new Color(255, 228, 225); // MistyRose - lighter pink
    
    private final JPanel mainPanel;
    private final JPanel chatPanel;
    private JTextField messageInput;
    private final ArrayList<Message> messages;
    private final String userName;
    private final String userImage;
    private final String chatId;
    private final ChatDAO chatDAO;
    private final String currentUserId;
    private final String otherUserId;
    
    private static class Message {
        String content;
        boolean isSent;
        long timestamp;
        
        Message(String content, boolean isSent, long timestamp) {
            this.content = content;
            this.isSent = isSent;
            this.timestamp = timestamp;
        }
    }
    
    public MessageBox(String userName, String userImage, String otherUserId) {
        this.userName = userName;
        this.userImage = userImage;
        this.otherUserId = otherUserId;
        this.currentUserId = User.getCurrentUser().getUserId();
        this.chatId = getChatId(currentUserId, otherUserId);
        this.messages = new ArrayList<>();
        this.chatDAO = new ChatDAO();
        
        setTitle("Chat with " + userName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);
        setUndecorated(true);
        
        // Main panel with rounded corners
        mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BACKGROUND_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS);
            }
        };
        mainPanel.setOpaque(false);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Chat area
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Input area
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS));
        
        // Load messages
        loadMessages();
        
        // Make window draggable
        setupWindowDragging(headerPanel);
        
        // Set up message refresh timer
        Timer refreshTimer = new Timer(5000, e -> loadMessages());
        refreshTimer.start();
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(getWidth(), 60));
        
        // Left panel with back button and user info
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);

        // Back button with updated style
        JButton backButton = new JButton("←");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(HEADER_COLOR);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());
        leftPanel.add(backButton);
        
        // User name only
        JLabel nameLabel = new JLabel(userName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        leftPanel.add(nameLabel);
        
        header.add(leftPanel, BorderLayout.WEST);
        
        // Menu button and popup menu
        JButton menuButton = new JButton("⋮");
        menuButton.setForeground(Color.WHITE);
        menuButton.setBackground(HEADER_COLOR);
        menuButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        menuButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        menuButton.setFocusPainted(false);
        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Create popup menu
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Color.WHITE);
        
        // Block option
        JMenuItem blockItem = new JMenuItem("Block User");
        blockItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        blockItem.setBackground(Color.WHITE);
        blockItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to block " + userName + "?",
                "Block User",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                // TODO: Implement block functionality
                JOptionPane.showMessageDialog(
                    this,
                    userName + " has been blocked.",
                    "User Blocked",
                    JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
            }
        });
        
        // Report option with ReportDAO integration
        JMenuItem reportItem = new JMenuItem("Report User");
        reportItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reportItem.setBackground(Color.WHITE);
        reportItem.addActionListener(e -> {
            String reason = JOptionPane.showInputDialog(
                this,
                "Please provide a reason for reporting " + userName + ":",
                "Report User",
                JOptionPane.QUESTION_MESSAGE
            );
            if (reason != null && !reason.trim().isEmpty()) {
                ReportDAO reportDAO = new ReportDAO();
                if (reportDAO.reportUser(otherUserId, currentUserId, reason)) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Thank you for your report. We will review it shortly.",
                        "Report Submitted",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Failed to submit report. Please try again later.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        popupMenu.add(blockItem);
        popupMenu.addSeparator();
        popupMenu.add(reportItem);
        
        menuButton.addActionListener(e -> {
            popupMenu.show(menuButton, 0, menuButton.getHeight());
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(menuButton);
        header.add(buttonPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(BACKGROUND_COLOR); // Match main background
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create a wrapper panel for the input field to handle rounded corners
        JPanel inputWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(248, 248, 248)); // #f8f8f8
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); // 20px radius * 2 for both sides
            }
        };
        inputWrapper.setOpaque(false);
        inputWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Space for the send button
        
        // Modern text field
        messageInput = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(248, 248, 248)); // #f8f8f8
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                super.paintComponent(g);
            }
        };
        messageInput.setOpaque(false);
        messageInput.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        messageInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageInput.setBackground(new Color(248, 248, 248));
        
        // Circular send button
        JButton sendButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(219, 64, 112)); // Darker pink when pressed
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(239, 84, 132)); // Lighter pink on hover
                } else {
                    g2.setColor(new Color(231, 84, 128)); // #e75480
                }
                
                g2.fillOval(0, 0, 40, 40);
                
                // Draw send arrow
                g2.setColor(Color.WHITE);
                int[] xPoints = {12, 28, 12};
                int[] yPoints = {12, 20, 28};
                g2.setStroke(new java.awt.BasicStroke(2));
                g2.fillPolygon(xPoints, yPoints, 3);
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(40, 40);
            }
            
            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
            
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        sendButton.setContentAreaFilled(false);
        sendButton.setBorderPainted(false);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> sendMessage());
        
        // Add components
        inputWrapper.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(inputWrapper, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        return inputPanel;
    }
    
    private void sendMessage() {
        String messageText = messageInput.getText().trim();
        if (messageText.isEmpty()) {
            return;
        }

        try {
            Chat chat = new Chat();
            chat.setSenderId(currentUserId);
            chat.setMessage(messageText);
            
            chatDAO.sendMessage(chat);
            boolean success = true; // Always true unless exception
            
            if (success) {
                messageInput.setText("");
                loadMessages(); // Refresh messages
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to send message. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error sending message: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadMessages() {
        try {
            List<Chat> chatMessages = chatDAO.getConversation(currentUserId, otherUserId);
            
            // Clear existing messages
            chatPanel.removeAll();
            
            // Add each message
            for (Chat chat : chatMessages) {
                boolean isSent = chat.getSenderId().equals(currentUserId);
                addMessageBubble(chat.getMessage(), isSent, chat.getTimestamp());
            }
            
            // Scroll to bottom
            SwingUtilities.invokeLater(() -> {
                JScrollPane scrollPane = (JScrollPane) chatPanel.getParent().getParent();
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
            
            chatPanel.revalidate();
            chatPanel.repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading messages: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addMessageBubble(String message, boolean isSent, long timestamp) {
        JPanel bubblePanel = new JPanel();
        bubblePanel.setLayout(new BorderLayout());
        bubblePanel.setOpaque(false);
        
        // Message text
        JLabel messageLabel = new JLabel("<html><div style='width: 200px;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        messageLabel.setOpaque(true);
        
        if (isSent) {
            messageLabel.setBackground(MESSAGE_SENT_COLOR);
            messageLabel.setForeground(Color.WHITE);
            bubblePanel.add(messageLabel, BorderLayout.EAST);
        } else {
            messageLabel.setBackground(MESSAGE_RECEIVED_COLOR);
            messageLabel.setForeground(Color.BLACK);
            bubblePanel.add(messageLabel, BorderLayout.WEST);
        }
        
        // Add some padding
        bubblePanel.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        chatPanel.add(bubblePanel);
    }
    
    private void setupWindowDragging(JPanel dragPanel) {
        MouseAdapter dragListener = new MouseAdapter() {
            private Point mouseDownCompCoords;
            
            @Override
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseDownCompCoords != null) {
                    Point currCoords = e.getLocationOnScreen();
                    setLocation(currCoords.x - mouseDownCompCoords.x, 
                              currCoords.y - mouseDownCompCoords.y);
                }
            }
        };
        
        dragPanel.addMouseListener(dragListener);
        dragPanel.addMouseMotionListener(dragListener);
    }
    
    private String getChatId(String userId1, String userId2) {
        return userId1.compareTo(userId2) < 0 ? userId1 + "_" + userId2 : userId2 + "_" + userId1;
    }
} 