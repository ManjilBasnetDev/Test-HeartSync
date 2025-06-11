package com.heartsync.chat;

import com.heartsync.dao.MessageDAO;
import com.heartsync.model.Message;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class ChatManager {
    private final MessageDAO messageDAO;
    private final int currentUserId;
    private final int otherUserId;
    private final List<ChatUpdateListener> listeners;
    private Timer messageCheckTimer;
    private long lastMessageTimestamp;
    private int refreshInterval;
    
    public interface ChatUpdateListener {
        void onNewMessages(List<Message> newMessages);
        void onError(String errorMessage);
    }
    
    public ChatManager(int currentUserId, int otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
        this.messageDAO = new MessageDAO();
        this.listeners = new CopyOnWriteArrayList<>();
        this.lastMessageTimestamp = System.currentTimeMillis();
        loadConfig();
    }
    
    private void loadConfig() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
            refreshInterval = Integer.parseInt(props.getProperty("chat.refresh.interval", "1000"));
        } catch (IOException e) {
            System.err.println("Warning: Could not load config.properties. Using default refresh interval.");
            refreshInterval = 1000;
        }
    }
    
    public void addListener(ChatUpdateListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(ChatUpdateListener listener) {
        listeners.remove(listener);
    }
    
    public void startMessagePolling() {
        if (messageCheckTimer != null) {
            return;
        }
        
        messageCheckTimer = new Timer(true);
        messageCheckTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForNewMessages();
            }
        }, 0, refreshInterval);
    }
    
    public void stopMessagePolling() {
        if (messageCheckTimer != null) {
            messageCheckTimer.cancel();
            messageCheckTimer = null;
        }
    }
    
    private void checkForNewMessages() {
        try {
            List<Message> messages = messageDAO.getMessagesBetween(currentUserId, otherUserId);
            if (!messages.isEmpty()) {
                // Filter messages newer than last check
                List<Message> newMessages = messages.stream()
                    .filter(msg -> msg.getTimestamp().getTime() > lastMessageTimestamp)
                    .toList();
                
                if (!newMessages.isEmpty()) {
                    lastMessageTimestamp = newMessages.get(newMessages.size() - 1)
                        .getTimestamp().getTime();
                    notifyListeners(newMessages);
                }
            }
        } catch (SQLException e) {
            notifyError("Error checking for new messages: " + e.getMessage());
        }
    }
    
    public void sendMessage(String messageText) {
        try {
            Message message = new Message(currentUserId, otherUserId, messageText);
            messageDAO.sendMessage(message);
        } catch (SQLException e) {
            notifyError("Error sending message: " + e.getMessage());
        }
    }
    
    private void notifyListeners(List<Message> newMessages) {
        for (ChatUpdateListener listener : listeners) {
            listener.onNewMessages(newMessages);
        }
    }
    
    private void notifyError(String errorMessage) {
        for (ChatUpdateListener listener : listeners) {
            listener.onError(errorMessage);
        }
    }
} 