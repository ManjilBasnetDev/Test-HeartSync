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
    public UserRegisterDAO() {}

    public boolean createUser(User user) {
        try {
            if (usernameExists(user.getUsername())) throw new IOException("Username already exists");
            String userId = UUID.randomUUID().toString();
            user.setUserId(userId);
            FirebaseConfig.put(FirebaseConfig.getUserPath(userId), user);
            return true;
        } catch (IOException e) {
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