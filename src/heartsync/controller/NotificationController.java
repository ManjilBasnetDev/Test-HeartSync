package heartsync.controller;

import heartsync.dao.NotificationDAO;
import java.util.List;
import heartsync.model.Notification;

public class NotificationController {
    public void notifyMatch(String matchName) {
        Notification notif = new Notification(
            "You have a new match: " + matchName + "!",
            Notification.Type.MATCH
        );
        NotificationDAO.addNotification(notif);
    }

    public void notifyMessage(String fromUser) {
        Notification notif = new Notification(
            "New message from: " + fromUser + "!",
            Notification.Type.MESSAGE
        );
        NotificationDAO.addNotification(notif);
    }

    public List<Notification> getNotifications() {
        return NotificationDAO.getAllNotifications();
    }

    public void markAllAsRead() {
        NotificationDAO.markAllAsRead();
    }
}
