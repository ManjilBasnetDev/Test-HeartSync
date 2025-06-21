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
        setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Messages Panel
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(Color.WHITE);
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

        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        messageField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        messageField.addActionListener(e -> sendMessage());
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setBackground(new Color(0, 132, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        if (messageText.isEmpty() || participant == null) {
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
        // Wrapper panel to align the bubble
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        // The actual bubble with text
        JTextArea messageArea = new JTextArea(chat.getMessage());
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        messageArea.setMargin(new Insets(8, 12, 8, 12));

        if (isSentByMe) {
            messageArea.setBackground(BUBBLE_SENT_COLOR);
            messageArea.setForeground(TEXT_SENT_COLOR);
            wrapper.add(messageArea, BorderLayout.EAST);
        } else {
            messageArea.setBackground(BUBBLE_RECEIVED_COLOR);
            messageArea.setForeground(TEXT_RECEIVED_COLOR);
            wrapper.add(messageArea, BorderLayout.WEST);
        }
        
        // Add timestamp below the bubble
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");
        JLabel timestampLabel = new JLabel(sdf.format(new Date(chat.getTimestamp())));
        timestampLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timestampLabel.setForeground(Color.GRAY);
        timestampLabel.setBorder(new EmptyBorder(2, 12, 2, 12));

        if(isSentByMe) {
            timestampLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
            timestampLabel.setHorizontalAlignment(SwingConstants.LEFT);
        }

        JPanel finalBubble = new JPanel(new BorderLayout());
        finalBubble.setOpaque(false);
        finalBubble.add(wrapper, BorderLayout.CENTER);
        finalBubble.add(timestampLabel, BorderLayout.SOUTH);

        return finalBubble;
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
             new ChatSystem(currentUser).setVisible(true);

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
}