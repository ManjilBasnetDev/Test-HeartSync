package heartsync.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import heartsync.database.FirebaseConfig;

public class LikeDAO {
    private static final String LIKES_PATH = "user_likes";
    private static final String PASSES_PATH = "user_passes";
    
    public boolean addLike(String userId, String likedUserId) {
        try {
            FirebaseConfig.put(LIKES_PATH + "/" + userId + "/" + likedUserId, true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean addPass(String userId, String passedUserId) {
        try {
            FirebaseConfig.put(PASSES_PATH + "/" + userId + "/" + passedUserId, true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<String> getLikedUsers(String userId) {
        List<String> likedUsers = new ArrayList<>();
        try {
            Map<String, Boolean> likes = FirebaseConfig.get(LIKES_PATH + "/" + userId, 
                new TypeToken<Map<String, Boolean>>(){}.getType());
            if (likes != null) {
                likedUsers.addAll(likes.keySet());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return likedUsers;
    }
    
    public List<String> getPassedUsers(String userId) {
        List<String> passedUsers = new ArrayList<>();
        try {
            Map<String, Boolean> passes = FirebaseConfig.get(PASSES_PATH + "/" + userId, 
                new TypeToken<Map<String, Boolean>>(){}.getType());
            if (passes != null) {
                passedUsers.addAll(passes.keySet());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return passedUsers;
    }
    
    public boolean hasInteractedWith(String userId, String otherUserId) {
        try {
            // Check if liked
            Boolean liked = FirebaseConfig.get(LIKES_PATH + "/" + userId + "/" + otherUserId, Boolean.class);
            if (liked != null && liked) {
                return true;
            }
            
            // Check if passed
            Boolean passed = FirebaseConfig.get(PASSES_PATH + "/" + userId + "/" + otherUserId, Boolean.class);
            return passed != null && passed;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
} 