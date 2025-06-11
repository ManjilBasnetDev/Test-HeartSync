package com.heartsync.gui;

import com.heartsync.chat.ChatManager;
import com.heartsync.model.Message;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ChatWindow extends JFrame implements ChatManager.ChatUpdateListener {
    private final ChatManager chatManager;
    private final JTextArea chatArea;
    private final JTextField messageField;
    private final JButton sendButton;
    private final SimpleDateFormat timeFormat;
    private final int currentUserId;
    private final int otherUserId;
    
    public ChatWindow(int currentUserId, int otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
        this.chatManager = new ChatManager(currentUserId, otherUserId);
        this.timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        // Set up the window
        setTitle("Chat with User " + otherUserId);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Create components
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        
        messageField = new JTextField();
        sendButton = new JButton("Send");
        
        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Add listeners
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chatManager.stopMessagePolling();
            }
        });
        
        // Start real-time updates
        chatManager.addListener(this);
        chatManager.startMessagePolling();
    }
    
    private void sendMessage() {
        String text = messageField.getText().trim();
        if (!text.isEmpty()) {
            chatManager.sendMessage(text);
            messageField.setText("");
        }
    }
    
    @Override
    public void onNewMessages(List<Message> newMessages) {
        SwingUtilities.invokeLater(() -> {
            for (Message message : newMessages) {
                String sender = (message.getSenderId() == currentUserId) ? "You" : "Other";
                String time = timeFormat.format(message.getTimestamp());
                chatArea.append(String.format("[%s] %s: %s%n", 
                    time, sender, message.getMessageText()));
            }
            // Scroll to bottom
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
    
    @Override
    public void onError(String errorMessage) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, 
                errorMessage, 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        });
    }
} 