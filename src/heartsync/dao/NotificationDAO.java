package heartsync.dao;

import heartsync.model.Notification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationDAO {
    private static final List<Notification> notifications = new ArrayList<>();

    public static void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public static List<Notification> getAllNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    public static void markAllAsRead() {
        for (Notification n : notifications) {
            n.markAsRead();
        }
    }
}
