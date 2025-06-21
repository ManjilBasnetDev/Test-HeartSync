package heartsync.controller;

import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class NotificationController {
    private static final String NOTIFICATIONS_PATH = "notifications";
    private String currentUserId;
    private Thread notificationThread;
    private volatile boolean running;

    public NotificationController(String userId) {
        this.currentUserId = userId;
    }

    public void startNotificationListener() {
        if (notificationThread != null && notificationThread.isAlive()) {
            return;
        }

        running = true;
        notificationThread = new Thread(() -> {
            while (running) {
                try {
                    checkNotifications();
                    Thread.sleep(5000); // Check every 5 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Error checking notifications: " + e.getMessage());
                }
            }
        });
        notificationThread.setDaemon(true);
        notificationThread.start();
    }

    public void stopNotificationListener() {
        running = false;
        if (notificationThread != null) {
            notificationThread.interrupt();
        }
    }

    private void checkNotifications() {
        try {
            String path = NOTIFICATIONS_PATH + "/" + currentUserId;
            Map<String, String> notifications = FirebaseConfig.get(path, new TypeToken<Map<String, String>>(){}.getType());

            if (notifications != null && !notifications.isEmpty()) {
                notifications.forEach((userId, type) -> {
                    if ("match".equals(type)) {
                        showMatchNotification();
                        // Remove the notification after showing it
                        try {
                            FirebaseConfig.delete(path + "/" + userId);
                        } catch (Exception e) {
                            System.err.println("Error deleting notification: " + e.getMessage());
                        }
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Error checking notifications: " + e.getMessage());
        }
    }

    private void showMatchNotification() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, 
                "You have a new match!", 
                "New Match", 
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
