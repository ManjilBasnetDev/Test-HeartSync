package heartsync.database;

import com.google.gson.reflect.TypeToken;
import heartsync.model.UserProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DatabaseManagerProfile {
    private static DatabaseManagerProfile instance;
    private static final String PROFILES_PATH = "user_details";

    private DatabaseManagerProfile() {}

    public static DatabaseManagerProfile getInstance() {
        if (instance == null) {
            instance = new DatabaseManagerProfile();
        }
        return instance;
    }

    public void saveUserProfile(UserProfile profile) {
        try {
            String username = profile.getUsername();
            if (username == null || username.trim().isEmpty()) {
                throw new RuntimeException("Username cannot be null or empty");
            }
            
            System.out.println("Attempting to save profile for user: " + username);
            
            // First, try to create the user_details node if it doesn't exist
            try {
                Map<String, Object> testMap = FirebaseConfig.get(PROFILES_PATH, new TypeToken<Map<String, Object>>(){}.getType());
                if (testMap == null) {
                    System.out.println("Creating user_details node in Firebase");
                    FirebaseConfig.set(PROFILES_PATH, new java.util.HashMap<>());
                }
            } catch (Exception e) {
                System.out.println("Creating user_details node in Firebase");
                FirebaseConfig.set(PROFILES_PATH, new java.util.HashMap<>());
            }
            
            // Save directly to user_details/{username}
            String path = PROFILES_PATH + "/" + username;
            System.out.println("Saving to path: " + path);
            
            // Create a map of the profile data to ensure proper JSON structure
            Map<String, Object> profileData = new java.util.HashMap<>();
            profileData.put("username", profile.getUsername());
            profileData.put("fullName", profile.getFullName());
            profileData.put("height", profile.getHeight());
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
            System.out.println("Profile data saved successfully to Firebase");
            
            // Update the current user's profile in memory
            UserProfile.setCurrentUser(profile);
        } catch (Exception e) {
            System.err.println("Error saving profile: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save user profile: " + e.getMessage());
        }
    }

    public UserProfile getUserProfile(String username) {
        try {
            String path = PROFILES_PATH + "/" + username;
            return FirebaseConfig.get(path, UserProfile.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserProfile> getAllUserProfilesExcept(String username) {
        try {
            Map<String, UserProfile> profiles = FirebaseConfig.get(PROFILES_PATH, 
                new TypeToken<Map<String, UserProfile>>(){}.getType());
            
            if (profiles != null) {
                return profiles.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals(username))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void updateUserProfile(UserProfile profile) {
        saveUserProfile(profile);
    }

    public void deleteUserProfile(String username) {
        try {
            String path = PROFILES_PATH + "/" + username;
            FirebaseConfig.delete(path);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete user profile: " + e.getMessage());
        }
    }
} 