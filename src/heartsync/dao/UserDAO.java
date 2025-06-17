package heartsync.dao;

import heartsync.model.User;
import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import java.util.UUID;
import java.io.IOException;

public class UserDAO {
    public UserDAO() {}

    // Authenticate user login
    public User authenticateUser(String username, String password) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
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

    // Create new user
    public boolean createUser(User user) {
        try {
            if (usernameExists(user.getUsername())) return false;
            String userId = UUID.randomUUID().toString();
            user.setUserId(userId);
            FirebaseConfig.put(FirebaseConfig.getUserPath(userId), user);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if username exists
    public boolean usernameExists(String username) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (User user : users.values()) {
                    if (user.getUsername().equals(username)) return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get user by username
    public User getUserByUsername(String username) {
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

    // Update user password
    public boolean updatePassword(String username, String newPassword) {
        try {
            User user = getUserByUsername(username);
            if (user != null) {
                user.setPassword(newPassword);
                FirebaseConfig.put(FirebaseConfig.getUserPath(user.getUserId()), user);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get user by username (alias)
    public User getUser(String username) {
        return getUserByUsername(username);
    }
}
