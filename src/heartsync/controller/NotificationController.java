package heartsync.controller;

import heartsync.model.Notification;
import java.util.ArrayList;
import java.util.List;

public class NotificationController {
    private List<Notification> notifications = new ArrayList<>();

    public void addMatchNotification(String message) {
        Notification notif = new Notification(
            message,
            Notification.Type.MATCH
        );
        notifications.add(notif);
    }

    public void addMessageNotification(String message) {
        Notification notif = new Notification(
            message,
            Notification.Type.MESSAGE
        );
        notifications.add(notif);
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public void markAllAsRead() {
        notifications.clear();
    }
}
