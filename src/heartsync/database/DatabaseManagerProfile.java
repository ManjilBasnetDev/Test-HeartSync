package heartsync.database;

import heartsync.model.UserProfile;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.io.IOException;

public class DatabaseManagerProfile {
    private static DatabaseManagerProfile instance;

    private DatabaseManagerProfile() {}

    public static synchronized DatabaseManagerProfile getInstance() {
        if (instance == null) {
            instance = new DatabaseManagerProfile();
        }
        return instance;
    }

    /**
     * Save or update a user profile under path user_details/{username}
     */
    public void saveUserProfile(UserProfile profile) throws IOException {
        if (profile == null || profile.getUsername() == null) {
            throw new IllegalArgumentException("Profile or username cannot be null");
        }
        String path = "user_details/" + profile.getUsername();
        Map<String, Object> data = new HashMap<>();
        data.put("username", profile.getUsername());
        data.put("fullName", profile.getFullName());
        data.put("height", profile.getHeight());
        data.put("weight", profile.getWeight());
        data.put("country", profile.getCountry());
        data.put("address", profile.getAddress());
        data.put("phoneNumber", profile.getPhoneNumber());
        data.put("education", profile.getEducation());
        data.put("gender", profile.getGender());
        data.put("preferences", profile.getPreferences());
        data.put("aboutMe", profile.getAboutMe());
        data.put("profilePicPath", profile.getProfilePicPath());
        data.put("hobbies", profile.getHobbies());
        data.put("relationshipGoal", profile.getRelationshipGoal());
        data.put("occupation", profile.getOccupation());
        data.put("religion", profile.getReligion());
        data.put("ethnicity", profile.getEthnicity());
        data.put("languages", profile.getLanguages());
        data.put("dateOfBirth", profile.getDateOfBirth());
        data.put("email", profile.getEmail());

        FirebaseConfig.set(path, data);
    }

    /**
     * Retrieve a user profile from Firebase.
     */
    public UserProfile getUserProfile(String username) {
        try {
            String path = "user_details/" + username;
            Map<String, Object> data = FirebaseConfig.get(path, new TypeToken<Map<String, Object>>(){}.getType());
            if (data == null) {
                return null;
            }
            UserProfile p = new UserProfile();
            p.setUsername((String) data.getOrDefault("username", username));
            p.setFullName((String) data.getOrDefault("fullName", ""));
            Object height = data.get("height");
            if (height instanceof Number) p.setHeight(((Number) height).intValue());
            Object weight = data.get("weight");
            if (weight instanceof Number) p.setWeight(((Number) weight).intValue());
            p.setCountry((String) data.getOrDefault("country", ""));
            p.setAddress((String) data.getOrDefault("address", ""));
            p.setPhoneNumber((String) data.getOrDefault("phoneNumber", ""));
            p.setEducation((String) data.getOrDefault("education", ""));
            p.setGender((String) data.getOrDefault("gender", ""));
            p.setPreferences((String) data.getOrDefault("preferences", ""));
            p.setAboutMe((String) data.getOrDefault("aboutMe", ""));
            p.setProfilePicPath((String) data.getOrDefault("profilePicPath", ""));
            Object hobbies = data.get("hobbies");
            if (hobbies instanceof List) {
                p.setHobbies((List<String>) hobbies);
            }
            p.setRelationshipGoal((String) data.getOrDefault("relationshipGoal", ""));
            p.setOccupation((String) data.getOrDefault("occupation", ""));
            p.setReligion((String) data.getOrDefault("religion", ""));
            p.setEthnicity((String) data.getOrDefault("ethnicity", ""));
            Object languages = data.get("languages");
            if (languages instanceof List) {
                p.setLanguages((List<String>) languages);
            }
            p.setDateOfBirth((String) data.getOrDefault("dateOfBirth", ""));
            p.setEmail((String) data.getOrDefault("email", ""));
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Delete a user profile from Firebase.
     */
    public boolean deleteUserProfile(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return false;
            }
            String path = "user_details/" + username;
            FirebaseConfig.set(path, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 