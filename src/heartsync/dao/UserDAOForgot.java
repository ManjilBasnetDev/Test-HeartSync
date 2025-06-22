package heartsync.dao;

import heartsync.model.UserForgot;
import heartsync.model.User;
import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import java.io.IOException;

/**
 * DAO dedicated to Forgot-Password flow.
 * Moved into its own file so the public class name matches the filename.
 */
public class UserDAOForgot {
    public UserForgot findByUsername(String username) {
        try {
            System.out.println("=== DEBUG: Finding user by username ===");
            System.out.println("Looking for username: '" + username + "'");
            
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                System.out.println("Found " + users.size() + " users in database");
                
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user != null && user.getUsername() != null && user.getUsername().equals(username)) {
                        System.out.println("Found user: " + username);
                        System.out.println("User favorite color: '" + user.getFavoriteColor() + "'");
                        System.out.println("User first school: '" + user.getFirstSchool() + "'");
                        
                        UserForgot uf = new UserForgot();
                        uf.setId(0); // Not used in Firebase
                        uf.setUsername(user.getUsername());
                        uf.setPassword(user.getPassword());
                        // Map security questions properly with better null handling
                        String favoriteColor = user.getFavoriteColor();
                        String firstSchool = user.getFirstSchool();
                        
                        uf.setFavoriteColor(favoriteColor != null ? favoriteColor.trim() : "");
                        uf.setFirstSchool(firstSchool != null ? firstSchool.trim() : "");
                        
                        System.out.println("Returning UserForgot with color: '" + uf.getFavoriteColor() + "' and school: '" + uf.getFirstSchool() + "'");
                        System.out.println("==========================================");
                        return uf;
                    }
                }
                System.out.println("User not found: " + username);
            } else {
                System.out.println("No users found in database");
            }
            System.out.println("==========================================");
        } catch (IOException e) {
            System.err.println("Error finding user by username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateSecurityQuestions(String username, String favoriteColor, String firstSchool) {
        try {
            System.out.println("=== DEBUG: Validating security questions ===");
            System.out.println("Username: '" + username + "'");
            System.out.println("Input favorite color: '" + favoriteColor + "'");
            System.out.println("Input first school: '" + firstSchool + "'");
            
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (User user : users.values()) {
                    if (user != null && user.getUsername() != null && user.getUsername().equals(username)) {
                        System.out.println("Found user, checking security questions...");
                        System.out.println("Database favorite color: '" + user.getFavoriteColor() + "'");
                        System.out.println("Database first school: '" + user.getFirstSchool() + "'");
                        
                        // Check if both security questions match with better null handling
                        boolean colorMatch = false;
                        boolean schoolMatch = false;
                        
                        if (favoriteColor != null && user.getFavoriteColor() != null) {
                            colorMatch = favoriteColor.trim().equalsIgnoreCase(user.getFavoriteColor().trim());
                        }
                        
                        if (firstSchool != null && user.getFirstSchool() != null) {
                            schoolMatch = firstSchool.trim().equalsIgnoreCase(user.getFirstSchool().trim());
                        }
                        
                        System.out.println("Color match: " + colorMatch);
                        System.out.println("School match: " + schoolMatch);
                        System.out.println("Overall validation result: " + (colorMatch && schoolMatch));
                        System.out.println("===========================================");
                        
                        return colorMatch && schoolMatch;
                    }
                }
            }
            System.out.println("User not found for validation");
            System.out.println("===========================================");
        } catch (IOException e) {
            System.err.println("Error validating security questions: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveSecurityQuestions(String username, String favoriteColor, String firstSchool) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user != null && user.getUsername() != null && user.getUsername().equals(username)) {
                        user.setFavoriteColor(favoriteColor);
                        user.setFirstSchool(firstSchool);
                        FirebaseConfig.put(FirebaseConfig.getUserPath(entry.getKey()), user);
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(String username, String newPlainPassword) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user != null && user.getUsername() != null && user.getUsername().equals(username)) {
                        // Update password and save back to Firebase
                        user.setPassword(newPlainPassword);
                        FirebaseConfig.put(FirebaseConfig.getUserPath(entry.getKey()), user);
                        System.out.println("Password updated successfully for user: " + username);
                        return true;
                    }
                }
            }
            System.out.println("User not found: " + username);
        } catch (IOException e) {
            System.err.println("Error updating password for user " + username + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean initializeSecurityQuestionsForUser(String username, String favoriteColor, String firstSchool) {
        try {
            System.out.println("=== DEBUG: Initializing security questions ===");
            System.out.println("Username: '" + username + "'");
            System.out.println("Setting favorite color to: '" + favoriteColor + "'");
            System.out.println("Setting first school to: '" + firstSchool + "'");
            
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user != null && user.getUsername() != null && user.getUsername().equals(username)) {
                        // Set the security questions
                        user.setFavoriteColor(favoriteColor);
                        user.setFirstSchool(firstSchool);
                        
                        // Save back to Firebase
                        FirebaseConfig.put(FirebaseConfig.getUserPath(entry.getKey()), user);
                        System.out.println("Successfully initialized security questions for user: " + username);
                        System.out.println("===============================================");
                        return true;
                    }
                }
            }
            System.out.println("User not found: " + username);
            System.out.println("===============================================");
        } catch (IOException e) {
            System.err.println("Error initializing security questions: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean ensureSecurityQuestionsExist(String username) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user != null && user.getUsername() != null && user.getUsername().equals(username)) {
                        // Check if security questions are missing
                        if (user.getFavoriteColor() == null || user.getFirstSchool() == null) {
                            // For the demo user "Forgot", set default values
                            if (username.equals("Forgot")) {
                                user.setFavoriteColor("red");
                                user.setFirstSchool("red");
                                FirebaseConfig.put(FirebaseConfig.getUserPath(entry.getKey()), user);
                                System.out.println("Set default security questions for user: " + username);
                                return true;
                            }
                        }
                        return true; // Security questions exist
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking security questions: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
