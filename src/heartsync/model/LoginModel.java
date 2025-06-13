package heartsync.model;

public class LoginModel {
    private String username;
    private String password;

    public LoginModel() {}
    public LoginModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Validation logic
    public String validate() {
        if (username == null || username.trim().isEmpty()) {
            return "Username/email is required.";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Password is required.";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters.";
        }
        return null; // Valid
    }
}
