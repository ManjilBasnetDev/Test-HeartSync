package heartsync.controller;

import heartsync.model.Notification;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationController {
    private static final Logger LOGGER = Logger.getLogger(NotificationController.class.getName());
    private List<Notification> notifications = new ArrayList<>();

    public void notifyMatch(String matchName) {
        String message = "You have a new match: " + matchName + "!";
        addMatchNotification(message);
        LOGGER.log(Level.INFO, "Match notification created for: {0}", matchName);
    }

    public void notifyMessage(String fromUser) {
        String message = "New message from: " + fromUser + "!";
        addMessageNotification(message);
        LOGGER.log(Level.INFO, "Message notification created from: {0}", fromUser);
    }

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
        LOGGER.log(Level.INFO, "All notifications marked as read");
    }
}
