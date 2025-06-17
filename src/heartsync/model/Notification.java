package heartsync.model;

import java.time.LocalDateTime;

public class Notification {
    public enum Type {
        MATCH, MESSAGE
    }

    private final String content;
    private final Type type;
    private final LocalDateTime timestamp;
    private boolean read;

    public Notification(String content, Type type) {
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }

    public String getContent() {
        return content;
    }

    public Type getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        this.read = true;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
