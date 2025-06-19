package heartsync.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.reflect.TypeToken;

import heartsync.database.FirebaseConfig;

public class LikeDAO {
    private static final String LIKES_PATH = "user_likes";
    private static final String PASSES_PATH = "user_passes";
    private static final String MATCHES_PATH = "matches";
    private static final String MESSAGES_PATH = "messages";
    
    public boolean addLike(String userId, String likedUserId) {
        try {
            // Add the like
            FirebaseConfig.put(LIKES_PATH + "/" + userId + "/" + likedUserId, true);
            
            // Check for mutual like
            if (checkMutualLike(userId, likedUserId)) {
                // Create match entries for both users
                createMatch(userId, likedUserId);
                return true;
            }
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

    // Check if there's a mutual like
    private boolean checkMutualLike(String userId, String likedUserId) throws IOException {
        Boolean mutualLike = FirebaseConfig.get(LIKES_PATH + "/" + likedUserId + "/" + userId, Boolean.class);
        return mutualLike != null && mutualLike;
    }

    // Create match entries for both users
    private void createMatch(String userId1, String userId2) throws IOException {
        // Check if match already exists to prevent duplicates
        Boolean existingMatch = FirebaseConfig.get(MATCHES_PATH + "/" + userId1 + "/" + userId2, Boolean.class);
        if (existingMatch == null || !existingMatch) {
            // Create match entries for both users
            FirebaseConfig.put(MATCHES_PATH + "/" + userId1 + "/" + userId2, true);
            FirebaseConfig.put(MATCHES_PATH + "/" + userId2 + "/" + userId1, true);
            
            // Initialize chat
            // Sort user IDs alphabetically to create a consistent chat ID
            String chatId = userId1.compareTo(userId2) < 0 ? userId1 + "_" + userId2 : userId2 + "_" + userId1;
            
            // Check if chat already exists
            Map<String, Object> existingChat = FirebaseConfig.get(MESSAGES_PATH + "/" + chatId + "/meta", 
                new TypeToken<Map<String, Object>>(){}.getType());
                
            if (existingChat == null) {
                // Create chat metadata
                Map<String, Object> chatMeta = new HashMap<>();
                chatMeta.put("user1", userId1);
                chatMeta.put("user2", userId2);
                chatMeta.put("lastMessage", "");
                chatMeta.put("timestamp", System.currentTimeMillis());
                
                // Save chat metadata
                FirebaseConfig.put(MESSAGES_PATH + "/" + chatId + "/meta", chatMeta);
                
                // Initialize empty messages map
                Map<String, Object> emptyMessages = new HashMap<>();
                FirebaseConfig.put(MESSAGES_PATH + "/" + chatId + "/messages", emptyMessages);
            }
        }
    }

    // Check if users are matched
    public boolean isMatched(String userId1, String userId2) {
        try {
            Boolean match = FirebaseConfig.get(MATCHES_PATH + "/" + userId1 + "/" + userId2, Boolean.class);
            return match != null && match;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all matches for a user
    public List<String> getMatches(String userId) {
        List<String> matches = new ArrayList<>();
        try {
            Map<String, Boolean> userMatches = FirebaseConfig.get(MATCHES_PATH + "/" + userId, 
                new TypeToken<Map<String, Boolean>>(){}.getType());
            if (userMatches != null) {
                matches.addAll(userMatches.keySet());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }
} 