package heartsync.dao;

import com.google.gson.reflect.TypeToken;
import heartsync.database.FirebaseConfig;
import heartsync.model.Notification;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationDAO {
    private static final String NOTIFICATIONS_PATH = "notifications";

    public static void addNotification(Notification notification) {
        try {
            Map<String, Notification> notifications = FirebaseConfig.get(NOTIFICATIONS_PATH, 
                new TypeToken<Map<String, Notification>>(){}.getType());
            
            if (notifications == null) {
                notifications = new java.util.HashMap<>();
            }
            
            String notifId = String.valueOf(System.currentTimeMillis());
            notifications.put(notifId, notification);
            
            FirebaseConfig.set(NOTIFICATIONS_PATH, notifications);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Notification> getAllNotifications() {
        try {
            Map<String, Notification> notifications = FirebaseConfig.get(NOTIFICATIONS_PATH, 
                new TypeToken<Map<String, Notification>>(){}.getType());
            
            if (notifications != null) {
                return new ArrayList<>(notifications.values());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void markAllAsRead() {
        try {
            Map<String, Notification> notifications = FirebaseConfig.get(NOTIFICATIONS_PATH, 
                new TypeToken<Map<String, Notification>>(){}.getType());
            
            if (notifications != null) {
                notifications.values().forEach(notification -> notification.setRead(true));
                FirebaseConfig.set(NOTIFICATIONS_PATH, notifications);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 