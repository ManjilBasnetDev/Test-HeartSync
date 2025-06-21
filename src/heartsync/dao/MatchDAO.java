package heartsync.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import heartsync.database.FirebaseConfig;

public class MatchDAO {
    private static final String LIKES_PATH = "likes";
    private static final String MATCHES_PATH = "matches";

    public void createMatch(String user1Id, String user2Id) {
        try {
            System.out.println("Creating match between " + user1Id + " and " + user2Id);
            
            // Create match entries for both users directly in Firebase
            FirebaseConfig.set(MATCHES_PATH + "/" + user1Id + "/" + user2Id, true);
            FirebaseConfig.set(MATCHES_PATH + "/" + user2Id + "/" + user1Id, true);
            
            System.out.println("Match created successfully in Firebase between " + user1Id + " and " + user2Id);
        } catch (Exception e) {
            System.err.println("Error creating match: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create match: " + e.getMessage());
        }
    }

    public boolean checkForMutualLike(String user1Id, String user2Id) {
        try {
            // Check if user1 has liked user2
            Boolean user1LikedUser2 = FirebaseConfig.get(LIKES_PATH + "/" + user1Id + "/" + user2Id, Boolean.class);
            if (!Boolean.TRUE.equals(user1LikedUser2)) {
                // Try array format for user1's likes
                List<Boolean> user1Likes = FirebaseConfig.get(LIKES_PATH + "/" + user1Id, new TypeToken<List<Boolean>>(){}.getType());
                if (user1Likes != null && user2Id.matches("\\d+")) {
                    int idx = Integer.parseInt(user2Id);
                    user1LikedUser2 = idx < user1Likes.size() && Boolean.TRUE.equals(user1Likes.get(idx));
                }
            }
            
            // Check if user2 has liked user1
            Boolean user2LikedUser1 = FirebaseConfig.get(LIKES_PATH + "/" + user2Id + "/" + user1Id, Boolean.class);
            if (!Boolean.TRUE.equals(user2LikedUser1)) {
                // Try array format for user2's likes
                List<Boolean> user2Likes = FirebaseConfig.get(LIKES_PATH + "/" + user2Id, new TypeToken<List<Boolean>>(){}.getType());
                if (user2Likes != null && user1Id.matches("\\d+")) {
                    int idx = Integer.parseInt(user1Id);
                    user2LikedUser1 = idx < user2Likes.size() && Boolean.TRUE.equals(user2Likes.get(idx));
                }
            }

            boolean mutualLike = Boolean.TRUE.equals(user1LikedUser2) && Boolean.TRUE.equals(user2LikedUser1);
            System.out.println("Mutual like check result: " + mutualLike + 
                             " (User1 liked User2: " + user1LikedUser2 + 
                             ", User2 liked User1: " + user2LikedUser1 + ")");
            return mutualLike;
        } catch (Exception e) {
            System.err.println("Error checking for mutual like: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean isMatched(String user1Id, String user2Id) {
        try {
            Boolean matchExists = FirebaseConfig.get(MATCHES_PATH + "/" + user1Id + "/" + user2Id, Boolean.class);
            return Boolean.TRUE.equals(matchExists);
        } catch (Exception e) {
            System.err.println("Error checking if matched: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Boolean> getMatches(String userId) {
        try {
            String matchPath = MATCHES_PATH + "/" + userId;
            Map<String, Boolean> matches = FirebaseConfig.get(matchPath, new TypeToken<Map<String, Boolean>>(){}.getType());
            return matches != null ? matches : new HashMap<>();
        } catch (Exception e) {
            System.err.println("Error getting matches: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }
} 