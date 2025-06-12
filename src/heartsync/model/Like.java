package heartsyncdatingapp.model;

public class Like {
    private int id;
    private int likerId;
    private int likedUserId;
    private String timestamp;

    public Like(int id, int likerId, int likedUserId, String timestamp) {
        this.id = id;
        this.likerId = likerId;
        this.likedUserId = likedUserId;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikerId() {
        return likerId;
    }

    public void setLikerId(int likerId) {
        this.likerId = likerId;
    }

    public int getLikedUserId() {
        return likedUserId;
    }

    public void setLikedUserId(int likedUserId) {
        this.likedUserId = likedUserId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
