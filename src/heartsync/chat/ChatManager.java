package heartsync.chat;

import heartsync.dao.MessageDAO;
import heartsync.model.Message;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatManager {
    private final MessageDAO messageDAO;
    private final int currentUserId;
    private final int otherUserId;
    private Timer messagePollingTimer;
    private List<ChatUpdateListener> listeners;
    private long lastMessageTimestamp;

    public interface ChatUpdateListener {
        void onNewMessages(List<Message> messages);
        void onError(String errorMessage);
    }

    public ChatManager(int currentUserId, int otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
        this.messageDAO = new MessageDAO();
        this.listeners = new ArrayList<>();
        this.lastMessageTimestamp = System.currentTimeMillis();
    }

    public void addListener(ChatUpdateListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ChatUpdateListener listener) {
        listeners.remove(listener);
    }

    public void startMessagePolling() {
        messagePollingTimer = new Timer();
        messagePollingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForNewMessages();
            }
        }, 0, 2000); // Check every 2 seconds
    }

    public void stopMessagePolling() {
        if (messagePollingTimer != null) {
            messagePollingTimer.cancel();
            messagePollingTimer = null;
        }
    }

    public void sendMessage(String messageText) {
        try {
            Message message = new Message();
            message.setSenderId(currentUserId);
            message.setReceiverId(otherUserId);
            message.setMessageText(messageText);
            message.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
            
            messageDAO.sendMessage(message);
            notifyListeners(List.of(message));
        } catch (SQLException e) {
            notifyError("Failed to send message: " + e.getMessage());
        }
    }

    private void checkForNewMessages() {
        try {
            List<Message> messages = messageDAO.getLatestMessages(currentUserId, otherUserId, 10);
            if (!messages.isEmpty()) {
                notifyListeners(messages);
            }
        } catch (SQLException e) {
            notifyError("Failed to fetch messages: " + e.getMessage());
        }
    }

    private void notifyListeners(List<Message> messages) {
        for (ChatUpdateListener listener : listeners) {
            listener.onNewMessages(messages);
        }
    }

    private void notifyError(String errorMessage) {
        for (ChatUpdateListener listener : listeners) {
            listener.onError(errorMessage);
        }
    }
} 