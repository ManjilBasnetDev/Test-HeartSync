package heartsync.dao;

import heartsync.model.User;
import heartsync.model.UserProfile;
import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import java.util.Map;
import java.util.UUID;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public UserDAO() {}

    // Get user by ID
    public User getUserById(String userId) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null && users.containsKey(userId)) {
                User user = users.get(userId);
                user.setUserId(userId);
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Authenticate user login
    public User authenticateUser(String username, String password) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user != null && user.getUsername() != null &&
                        user.getUsername().equals(username) &&
                        password != null && password.equals(user.getPassword())) {
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
                    if (user != null && user.getUsername() != null && user.getUsername().equals(username)) {
                        return true;
                    }
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

    public User findByEmail(String email) {
        // This is a placeholder. You would implement the database logic here.
        // For now, it returns null as it's not the source of the current error.
        return null;
    }

    public UserProfile findByUsername(String username) {
        try {
            String path = "user_details/" + username;
            Map<String, Object> profileData = FirebaseConfig.get(path, new TypeToken<Map<String, Object>>(){}.getType());
            if (profileData != null) {
                UserProfile profile = new UserProfile();
                profile.setUsername((String) profileData.getOrDefault("username", ""));
                profile.setFullName((String) profileData.getOrDefault("fullName", ""));
                profile.setCountry((String) profileData.getOrDefault("country", ""));
                profile.setAddress((String) profileData.getOrDefault("address", ""));
                profile.setPhoneNumber((String) profileData.getOrDefault("phoneNumber", ""));
                profile.setGender((String) profileData.getOrDefault("gender", ""));
                profile.setEducation((String) profileData.getOrDefault("education", ""));
                profile.setPreferences((String) profileData.getOrDefault("preferences", ""));
                Object hobbiesObj = profileData.get("hobbies");
                if (hobbiesObj instanceof List) {
                    profile.setHobbies((List<String>) hobbiesObj);
                }
                profile.setRelationshipGoal((String) profileData.getOrDefault("relationshipGoal", ""));
                profile.setAboutMe((String) profileData.getOrDefault("aboutMe", ""));
                profile.setProfilePicPath((String) profileData.getOrDefault("profilePicPath", ""));
                return profile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUserProfile(String username, UserProfile profile) {
        try {
            String path = "user_details/" + username;
            Map<String, Object> profileData = new java.util.HashMap<>();
            profileData.put("username", profile.getUsername());
            profileData.put("fullName", profile.getFullName());
            profileData.put("country", profile.getCountry());
            profileData.put("address", profile.getAddress());
            profileData.put("phoneNumber", profile.getPhoneNumber());
            profileData.put("gender", profile.getGender());
            profileData.put("education", profile.getEducation());
            profileData.put("preferences", profile.getPreferences());
            profileData.put("hobbies", profile.getHobbies());
            profileData.put("relationshipGoal", profile.getRelationshipGoal());
            profileData.put("aboutMe", profile.getAboutMe());
            profileData.put("profilePicPath", profile.getProfilePicPath());
            FirebaseConfig.set(path, profileData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update user
    public boolean updateUser(User user) {
        try {
            if (user != null && user.getUserId() != null) {
                FirebaseConfig.put(FirebaseConfig.getUserPath(user.getUserId()), user);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete user by username
    public boolean deleteUser(String username) {
        try {
            User user = getUserByUsername(username);
            if (user != null && user.getUserId() != null) {
                // Delete from users collection
                FirebaseConfig.set(FirebaseConfig.getUserPath(user.getUserId()), null);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
