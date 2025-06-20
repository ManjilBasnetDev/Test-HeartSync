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

public class MessageBox extends JFrame {
    private static final int WINDOW_RADIUS = 20;
    private static final Color BACKGROUND_COLOR = new Color(255, 216, 227);
    private static final Color HEADER_COLOR = new Color(102, 0, 51);
    private static final Color MESSAGE_SENT_COLOR = new Color(64, 158, 255);
    private static final Color MESSAGE_RECEIVED_COLOR = new Color(240, 240, 240);
    
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
        setUndecorated(false);
        
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
        header.setPreferredSize(new Dimension(getWidth(), 70));
        
        // Left panel with back button and user info
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);

        // Back button
        JButton backButton = new JButton("← Back");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(HEADER_COLOR);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
        });
        leftPanel.add(backButton);
        
        // User info
        try {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource(userImage));
            Image image = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            leftPanel.add(imageLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JLabel nameLabel = new JLabel(userName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        leftPanel.add(nameLabel);
        
        header.add(leftPanel, BorderLayout.WEST);
        
        // Close button
        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(231, 76, 60));
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);
        header.add(buttonPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        messageInput = new JTextField();
        messageInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        JButton sendButton = new JButton("Send");
        sendButton.setBackground(MESSAGE_SENT_COLOR);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBorder(new EmptyBorder(8, 20, 8, 20));
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        ActionListener sendAction = e -> sendMessage();
        sendButton.addActionListener(sendAction);
        messageInput.addActionListener(sendAction);
        
        inputPanel.add(messageInput, BorderLayout.CENTER);
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
            
            boolean success = chatDAO.sendMessage(chatId, chat);
            
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
            List<Chat> chatMessages = chatDAO.getMessages(chatId);
            
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
            messageLabel.setBackground(new Color(0, 132, 255));
            messageLabel.setForeground(Color.WHITE);
            bubblePanel.add(messageLabel, BorderLayout.EAST);
        } else {
            messageLabel.setBackground(new Color(240, 240, 240));
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