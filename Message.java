import java.time.LocalDateTime;

public class Message {
    private String content;
    private User sender;
    private LocalDateTime timestamp;
    private boolean isFromCurrentUser;

    public Message(String content, User sender, boolean isFromCurrentUser) {
        this.content = content;
        this.sender = sender;
        this.timestamp = LocalDateTime.now();
        this.isFromCurrentUser = isFromCurrentUser;
    }

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isFromCurrentUser() {
        return isFromCurrentUser;
    }
} 