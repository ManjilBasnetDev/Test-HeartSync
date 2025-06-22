package heartsync.dao;

import heartsync.model.User;
import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.IOException;
import java.net.URLEncoder;

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
                if (user != null && user.getUsername() != null && user.getUsername().equals(username)) {
                    return true;
                }
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
                    if (user != null && user.getUsername() != null && user.getUsername().equals(username)) {
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
                    if (user != null && user.getUsername() != null && user.getPassword() != null &&
                        user.getUsername().equals(username) && user.getPassword().equals(password)) {
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
                    // Only include users that have valid usernames and are not null
                    if (user != null && user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
                        user.setUserId(entry.getKey());
                        usersList.add(user);
                    }
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
            // First, find the user to get their ID
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            String userId = null;
            
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user != null && user.getUsername() != null && user.getUsername().equals(username)) {
                        userId = entry.getKey();
                        break;
                    }
                }
            }
            
            if (userId == null) {
                lastError = "User '" + username + "' not found in database";
                return false;
            }
            
            // URL encode the username for Firebase paths
            String encodedUsername = java.net.URLEncoder.encode(username, "UTF-8");
            
            // 1. Delete user profile from user_details
            try {
                FirebaseConfig.set("user_details/" + encodedUsername, null);
            } catch (Exception e) {
                System.err.println("Warning: Could not delete user profile for " + username + ": " + e.getMessage());
            }
            
            // 2. Delete user's likes
            try {
                FirebaseConfig.set("likes/" + encodedUsername, null);
            } catch (Exception e) {
                System.err.println("Warning: Could not delete likes for " + username + ": " + e.getMessage());
            }
            
            // 3. Delete user's matches
            try {
                FirebaseConfig.set("matches/" + encodedUsername, null);
            } catch (Exception e) {
                System.err.println("Warning: Could not delete matches for " + username + ": " + e.getMessage());
            }
            
            // 4. Delete user's notifications
            try {
                FirebaseConfig.set("notifications/" + encodedUsername, null);
            } catch (Exception e) {
                System.err.println("Warning: Could not delete notifications for " + username + ": " + e.getMessage());
            }
            
            // 5. Remove likes TO this user from other users
            try {
                Map<String, Map<String, Boolean>> allLikes = FirebaseConfig.get("likes", 
                    new TypeToken<Map<String, Map<String, Boolean>>>(){}.getType());
                if (allLikes != null) {
                    for (String likerUsername : allLikes.keySet()) {
                        try {
                            String likePath = "likes/" + likerUsername + "/" + encodedUsername;
                            FirebaseConfig.set(likePath, null);
                        } catch (Exception e) {
                            System.err.println("Warning: Could not remove like from " + likerUsername + " to " + username);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Warning: Could not clean up likes to user " + username + ": " + e.getMessage());
            }
            
            // 6. Remove matches TO this user from other users
            try {
                Map<String, Map<String, Boolean>> allMatches = FirebaseConfig.get("matches", 
                    new TypeToken<Map<String, Map<String, Boolean>>>(){}.getType());
                if (allMatches != null) {
                    for (String matcherUsername : allMatches.keySet()) {
                        try {
                            String matchPath = "matches/" + matcherUsername + "/" + encodedUsername;
                            FirebaseConfig.set(matchPath, null);
                        } catch (Exception e) {
                            System.err.println("Warning: Could not remove match from " + matcherUsername + " to " + username);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Warning: Could not clean up matches to user " + username + ": " + e.getMessage());
            }
            
            // 7. Finally, delete the user from the main users collection
            FirebaseConfig.delete(FirebaseConfig.getUserPath(userId));
            
            System.out.println("Successfully deleted user: " + username + " and all associated data");
            return true;
            
        } catch (IOException e) {
            lastError = "Error deleting user: " + e.getMessage();
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            lastError = "Unexpected error deleting user: " + e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyUser(String userId) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null && users.containsKey(userId)) {
                User user = users.get(userId);
                user.setUserType("verified");
                FirebaseConfig.put(FirebaseConfig.getUserPath(userId), user);
                return true;
            }
        } catch (IOException e) {
            lastError = "Error verifying user: " + e.getMessage();
            e.printStackTrace();
        }
        return false;
    }

    public boolean rejectUser(String userId) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null && users.containsKey(userId)) {
                User user = users.get(userId);
                user.setUserType("rejected");
                FirebaseConfig.put(FirebaseConfig.getUserPath(userId), user);
                return true;
            }
        } catch (IOException e) {
            lastError = "Error rejecting user: " + e.getMessage();
            e.printStackTrace();
        }
        return false;
    }
} 