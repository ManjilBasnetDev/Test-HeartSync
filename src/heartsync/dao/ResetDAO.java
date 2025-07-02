package heartsync.dao;

import com.google.gson.reflect.TypeToken;
import heartsync.database.FirebaseConfig;
import heartsync.model.User;
import java.util.Map;

public class ResetDAO {
    private static final String USERS_PATH = "users";

    public boolean resetPassword(String username, String newPassword) {
        try {
            // Get all users from Firebase
            Map<String, User> users = FirebaseConfig.get(USERS_PATH, 
                new TypeToken<Map<String, User>>(){}.getType());
            
            if (users != null) {
                // Find user by username in the values
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user != null && user.getUsername() != null && 
                        user.getUsername().equals(username)) {
                        // Update password and save back to Firebase
                        user.setPassword(newPassword);
                        FirebaseConfig.put(FirebaseConfig.getUserPath(entry.getKey()), user);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateSecurityAnswer(String username, String answer) {
        try {
            // Get all users from Firebase
            Map<String, User> users = FirebaseConfig.get(USERS_PATH, 
                new TypeToken<Map<String, User>>(){}.getType());
            
            if (users != null) {
                // Find user by username and validate security answer
                for (User user : users.values()) {
                    if (user != null && user.getUsername() != null && 
                        user.getUsername().equals(username)) {
                        // Check if the answer matches any of the security answers
                        return (user.getSecurityAnswer() != null && user.getSecurityAnswer().equalsIgnoreCase(answer)) ||
                               (user.getSecurityAnswer2() != null && user.getSecurityAnswer2().equalsIgnoreCase(answer)) ||
                               (user.getFavoriteColor() != null && user.getFavoriteColor().equalsIgnoreCase(answer)) ||
                               (user.getFirstSchool() != null && user.getFirstSchool().equalsIgnoreCase(answer));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean validateSecurityQuestions(String username, String favoriteColor, String firstSchool) {
        try {
            // Get all users from Firebase
            Map<String, User> users = FirebaseConfig.get(USERS_PATH, 
                new TypeToken<Map<String, User>>(){}.getType());
            
            if (users != null) {
                // Find user by username and validate both security questions
                for (User user : users.values()) {
                    if (user != null && user.getUsername() != null && 
                        user.getUsername().equals(username)) {
                        boolean colorMatch = (user.getFavoriteColor() != null && 
                                            user.getFavoriteColor().equalsIgnoreCase(favoriteColor));
                        boolean schoolMatch = (user.getFirstSchool() != null && 
                                             user.getFirstSchool().equalsIgnoreCase(firstSchool));
                        return colorMatch && schoolMatch;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public User findUserByUsername(String username) {
        try {
            // Get all users from Firebase
            Map<String, User> users = FirebaseConfig.get(USERS_PATH, 
                new TypeToken<Map<String, User>>(){}.getType());
            
            if (users != null) {
                // Find user by username
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user != null && user.getUsername() != null && 
                        user.getUsername().equals(username)) {
                        user.setUserId(entry.getKey()); // Set the Firebase key as userId
                        return user;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
} 