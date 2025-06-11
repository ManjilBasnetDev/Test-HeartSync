package messagesystem.model;

public class User {
    private String username;
    private String profilePicturePath;

    public User(String username, String profilePicturePath) {
        this.username = username;
        this.profilePicturePath = profilePicturePath;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }
} 