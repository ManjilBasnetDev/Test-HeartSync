/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.view;

import heartsync.dao.ChatDAO;
import heartsync.dao.ReportDAO;
import heartsync.model.Chat;
import heartsync.model.User;
import heartsync.model.UserProfile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.Timer;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder;

/**
 * A JPanel that displays the chat conversation with a selected participant.
 *
 * @author Manjil
 */
public class ConversationView extends JPanel {

    private final User currentUser;
    private UserProfile participant;
    private final ChatDAO chatDAO;
    private final ReportDAO reportDAO;

    private JPanel messagesPanel;
    private JScrollPane scrollPane;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel participantNameLabel;
    private Timer refreshTimer;

    private static final Color BUBBLE_SENT_COLOR = new Color(0, 132, 255);
    private static final Color BUBBLE_RECEIVED_COLOR = new Color(229, 229, 229);
    private static final Color TEXT_SENT_COLOR = Color.WHITE;
    private static final Color TEXT_RECEIVED_COLOR = Color.BLACK;
    private static final Color CHAT_BACKGROUND_COLOR = new Color(244, 247, 252);

    public ConversationView(User currentUser, UserProfile initialParticipant) {
        this.currentUser = currentUser;
        this.participant = initialParticipant;
        this.chatDAO = new ChatDAO();
        this.reportDAO = new ReportDAO();

        initComponents();
        if (this.participant != null) {
            loadConversation();
        } else {
            showPlaceholder();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(CHAT_BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Messages Panel
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(CHAT_BACKGROUND_COLOR);
        messagesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.SOUTH);

        // Timer to refresh messages every 5 seconds
        refreshTimer = new Timer(5000, e -> {
            if (participant != null) {
                loadConversation();
            }
        });
        refreshTimer.start();
    }
    
    private void showPlaceholder() {
        messagesPanel.removeAll();
        participantNameLabel.setText("Select a conversation");
        
        JPanel placeholder = new JPanel(new GridBagLayout());
        placeholder.setOpaque(false);
        JLabel placeholderLabel = new JLabel("Select a contact from the left to start chatting.");
        placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        placeholderLabel.setForeground(Color.GRAY);
        placeholder.add(placeholderLabel);
        
        messagesPanel.add(placeholder);
        
        messageField.setEnabled(false);
        sendButton.setEnabled(false);
        
        messagesPanel.revalidate();
        messagesPanel.repaint();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(247, 247, 247));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        participantNameLabel = new JLabel();
        participantNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        participantNameLabel.setBorder(new EmptyBorder(0, 20, 0, 0));
        headerPanel.add(participantNameLabel, BorderLayout.CENTER);

        // More options button (e.g., for report/block)
        JButton optionsButton = new JButton("⋮");
        optionsButton.setFont(new Font("Arial", Font.BOLD, 20));
        optionsButton.setBorderPainted(false);
        optionsButton.setContentAreaFilled(false);
        optionsButton.setFocusPainted(false);
        optionsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPopupMenu optionsMenu = new JPopupMenu();
        JMenuItem reportItem = new JMenuItem("Report User");
        JMenuItem blockItem = new JMenuItem("Block User");
        
        reportItem.addActionListener(e -> reportUser());
        blockItem.addActionListener(e -> blockUser());

        optionsMenu.add(reportItem);
        optionsMenu.add(blockItem);

        optionsButton.addActionListener(e -> {
             if (participant != null) {
                optionsMenu.show(optionsButton, 0, optionsButton.getHeight());
            }
        });
        
        headerPanel.add(optionsButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(new Color(247, 247, 247));
        inputPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        messageField = new JTextField("Type a message...");
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        messageField.setForeground(Color.GRAY);
        messageField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        messageField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (messageField.getText().equals("Type a message...")) {
                    messageField.setText("");
                    messageField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (messageField.getText().isEmpty()) {
                    messageField.setForeground(Color.GRAY);
                    messageField.setText("Type a message...");
                }
            }
        });

        messageField.addActionListener(e -> sendMessage());
        inputPanel.add(messageField, BorderLayout.CENTER);

        // Modern, circular send button
        sendButton = new JButton("➤") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(BUBBLE_SENT_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(BUBBLE_SENT_COLOR.brighter());
                } else {
                    g2.setColor(BUBBLE_SENT_COLOR);
                }
                g2.fillOval(0, 0, getWidth(), getHeight());
                
                super.paintComponent(g);
            }
        };
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setContentAreaFilled(false);
        sendButton.setBorder(null);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.setPreferredSize(new Dimension(45, 45));
        sendButton.addActionListener(e -> sendMessage());
        inputPanel.add(sendButton, BorderLayout.EAST);

        return inputPanel;
    }

    public void setParticipant(UserProfile participant) {
        this.participant = participant;
        if (this.participant != null) {
            messageField.setEnabled(true);
            sendButton.setEnabled(true);
            loadConversation();
        } else {
            showPlaceholder();
        }
    }

    private void loadConversation() {
        if (participant == null) return;

        participantNameLabel.setText(participant.getFullName());
        messagesPanel.removeAll();
        
        List<Chat> conversation = chatDAO.getConversation(currentUser.getUsername(), participant.getUsername());
        for (Chat chat : conversation) {
            boolean isSentByMe = chat.getSenderId().equals(currentUser.getUsername());
            JPanel messageBubble = createMessageBubble(chat, isSentByMe);
            messagesPanel.add(messageBubble);
            messagesPanel.add(Box.createVerticalStrut(5));
        }

        messagesPanel.revalidate();
        messagesPanel.repaint();

        // Scroll to the bottom
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    private void sendMessage() {
        String messageText = messageField.getText().trim();
        if (messageText.isEmpty() || participant == null || messageText.equals("Type a message...")) {
            return;
        }

        Chat chat = new Chat();
        chat.setSenderId(currentUser.getUsername());
        chat.setReceiverId(participant.getUsername());
        chat.setMessage(messageText);
        chat.setTimestamp(System.currentTimeMillis());

        try {
            chatDAO.sendMessage(chat);
            messageField.setText("");
            loadConversation(); // Refresh conversation to show the new message
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send message.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createMessageBubble(Chat chat, boolean isSentByMe) {
        // 1. The JTextArea for the message
        JTextArea messageArea = new JTextArea(chat.getMessage());
        messageArea.setOpaque(false);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        messageArea.setForeground(isSentByMe ? TEXT_SENT_COLOR : TEXT_RECEIVED_COLOR);

        // 2. The bubble panel with a custom border for the shape
        JPanel bubble = new JPanel(new BorderLayout());
        bubble.add(messageArea, BorderLayout.CENTER);
        bubble.setOpaque(false);
        bubble.setBorder(new BubbleBorder(isSentByMe ? BUBBLE_SENT_COLOR : BUBBLE_RECEIVED_COLOR, 2, 4, 16, isSentByMe));
        
        // Constrain the bubble's max width
        bubble.setMaximumSize(new Dimension(350, 1000));
        bubble.setPreferredSize(bubble.getPreferredSize());


        // 3. The timestamp label
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");
        JLabel timestampLabel = new JLabel(sdf.format(new Date(chat.getTimestamp())));
        timestampLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timestampLabel.setForeground(Color.GRAY);
        timestampLabel.setBorder(new EmptyBorder(2, isSentByMe ? 0 : 12, 0, isSentByMe ? 12 : 0));


        // 4. Combine bubble and timestamp
        JPanel bubbleWithTimestamp = new JPanel(new BorderLayout());
        bubbleWithTimestamp.setOpaque(false);
        bubbleWithTimestamp.add(bubble, BorderLayout.CENTER);
        bubbleWithTimestamp.add(timestampLabel, BorderLayout.SOUTH);
        if (isSentByMe) {
            timestampLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        // 5. Use a wrapper with FlowLayout to prevent stretching and align left/right
        JPanel wrapper = new JPanel(new FlowLayout(isSentByMe ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        wrapper.setOpaque(false);
        wrapper.add(bubbleWithTimestamp);
        wrapper.setBorder(new EmptyBorder(4, 0, 4, 0));

        return wrapper;
    }

    private void reportUser() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to report " + participant.getFullName() + "?",
                "Report User",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            boolean success = reportDAO.reportUser(participant.getUsername(), currentUser.getUsername(), "Inappropriate behavior.");
            if (success) {
                JOptionPane.showMessageDialog(this, "User reported successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to report user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void blockUser() {
       int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to block " + participant.getFullName() + "?\nThis will unmatch you and delete the conversation.",
                "Block User",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            // Here you would call a DAO method to block the user.
            // For example: likeDAO.removeMatch(currentUser.getUsername(), participant.getUsername());
            JOptionPane.showMessageDialog(this, participant.getFullName() + " has been blocked.", "Blocked", JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh the main chat system or close this window
            // This is a bit tricky without a direct reference to the parent.
            // A better approach would be to fire an event that ChatSystem listens to.
            SwingUtilities.getWindowAncestor(this).dispose();
            // Don't create a new ChatSystem here - just close the current one

        }
    }
    
    @Override
    public void removeNotify() {
        super.removeNotify();
        // Stop the timer when the component is removed
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }

    // Custom Border to create the bubble shape with a pointer
    class BubbleBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radii;
        private final int pointerSize;
        private final boolean sentByMe;

        BubbleBorder(Color color, int thickness, int radii, int pointerSize, boolean sentByMe) {
            this.color = color;
            this.thickness = thickness;
            this.radii = radii;
            this.pointerSize = pointerSize;
            this.sentByMe = sentByMe;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, new Insets(thickness, thickness, thickness, thickness));
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = thickness + radii;
            if (sentByMe) {
                insets.right += pointerSize;
            } else {
                insets.left += pointerSize;
            }
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);

            int r = radii;
            RoundRectangle2D.Double bubble;
            Polygon pointer = new Polygon();

            if (sentByMe) {
                bubble = new RoundRectangle2D.Double(x, y, width - pointerSize - 1, height - 1, r, r);
                int pX = x + width - pointerSize - 1;
                int pY = y + height / 2;
                pointer.addPoint(pX, pY - pointerSize);
                pointer.addPoint(pX, pY + pointerSize);
                pointer.addPoint(x + width - 1, pY);
            } else {
                bubble = new RoundRectangle2D.Double(x + pointerSize, y, width - pointerSize - 1, height - 1, r, r);
                int pX = x + pointerSize;
                int pY = y + height / 2;
                pointer.addPoint(pX, pY - pointerSize);
                pointer.addPoint(pX, pY + pointerSize);
                pointer.addPoint(x, pY);
            }
            
            Area area = new Area(bubble);
            area.add(new Area(pointer));

            g2.fill(area);
            g2.dispose();
        }
    }
}