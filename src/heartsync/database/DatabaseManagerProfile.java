package heartsync.database;

import com.google.gson.reflect.TypeToken;
import heartsync.model.UserProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DatabaseManagerProfile {
    private static DatabaseManagerProfile instance;
    private static final String PROFILES_PATH = "profiles";

    private DatabaseManagerProfile() {}

    public static DatabaseManagerProfile getInstance() {
        if (instance == null) {
            instance = new DatabaseManagerProfile();
        }
        return instance;
    }

    public void saveUserProfile(UserProfile profile) {
        try {
            Map<String, UserProfile> profiles = FirebaseConfig.get(PROFILES_PATH, 
                new TypeToken<Map<String, UserProfile>>(){}.getType());
            
            if (profiles == null) {
                profiles = new java.util.HashMap<>();
            }
            
            profiles.put(profile.getUsername(), profile);
            FirebaseConfig.set(PROFILES_PATH, profiles);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save user profile: " + e.getMessage());
        }
    }

    public UserProfile getUserProfile(String username) {
        try {
            Map<String, UserProfile> profiles = FirebaseConfig.get(PROFILES_PATH, 
                new TypeToken<Map<String, UserProfile>>(){}.getType());
            
            if (profiles != null && profiles.containsKey(username)) {
                return profiles.get(username);
            }
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
            Map<String, UserProfile> profiles = FirebaseConfig.get(PROFILES_PATH, 
                new TypeToken<Map<String, UserProfile>>(){}.getType());
            
            if (profiles != null && profiles.containsKey(username)) {
                profiles.remove(username);
                FirebaseConfig.set(PROFILES_PATH, profiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete user profile: " + e.getMessage());
        }
    }
} 