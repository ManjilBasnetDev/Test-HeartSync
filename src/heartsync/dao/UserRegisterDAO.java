package heartsync.dao;

import heartsync.model.User;
import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.IOException;

public class UserRegisterDAO {
    private String lastError;

    public UserRegisterDAO() {
        this.lastError = "";
    }

    public String getLastError() {
        return lastError;
    }

    public boolean createUser(User user) {
        try {
            // Validate required fields
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                lastError = "Username is required";
                return false;
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                lastError = "Password is required";
                return false;
            }
            if (user.getSecurityQuestion() == null || user.getSecurityQuestion().trim().isEmpty()) {
                lastError = "First security question is required";
                return false;
            }
            if (user.getSecurityAnswer() == null || user.getSecurityAnswer().trim().isEmpty()) {
                lastError = "First security answer is required";
                return false;
            }
            if (user.getSecurityQuestion2() == null || user.getSecurityQuestion2().trim().isEmpty()) {
                lastError = "Second security question is required";
                return false;
            }
            if (user.getSecurityAnswer2() == null || user.getSecurityAnswer2().trim().isEmpty()) {
                lastError = "Second security answer is required";
                return false;
            }

            // Check if username exists
            if (usernameExists(user.getUsername())) {
                lastError = "Username already exists";
                return false;
            }

            // Generate unique user ID
            String userId = UUID.randomUUID().toString();
            user.setUserId(userId);

            // Save user to Firebase
            FirebaseConfig.put(FirebaseConfig.getUserPath(userId), user);
            return true;
        } catch (IOException e) {
            lastError = "Error creating account: " + e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    private boolean usernameExists(String username) throws IOException {
        Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
        if (users != null) {
            for (User user : users.values()) {
                if (user.getUsername().equals(username)) return true;
            }
        }
        return false;
    }

    public User getUser(String username) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user.getUsername().equals(username)) {
                        user.setUserId(entry.getKey());
                        return user;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateUser(String username, String password) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (User user : users.values()) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    user.setUserId(entry.getKey());
                    usersList.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    public boolean updateUser(User user) {
        try {
            if (user.getUserId() == null) return false;
            FirebaseConfig.put(FirebaseConfig.getUserPath(user.getUserId()), user);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String username) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user.getUsername().equals(username)) {
                        FirebaseConfig.delete(FirebaseConfig.getUserPath(entry.getKey()));
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
} 