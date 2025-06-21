package heartsync.dao;

import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import java.util.HashMap;
import javax.swing.JOptionPane;
import java.time.Instant;

public class MatchDAO {
    private static final String LIKES_PATH = "likes";
    private static final String MATCHES_PATH = "matches";
    private static final String MESSAGES_PATH = "messages";
    private static final String NOTIFICATIONS_PATH = "notifications";

    public void checkAndCreateMatch(String currentUserId, String likedUserId) {
        try {
            // Check if the other user has already liked the current user
            String otherUserLikePath = LIKES_PATH + "/" + likedUserId + "/" + currentUserId;
            Boolean hasLiked = FirebaseConfig.get(otherUserLikePath, Boolean.class);

            if (Boolean.TRUE.equals(hasLiked)) {
                // It's a match! Create match records for both users
                createMatchRecord(currentUserId, likedUserId);
                
                // Initialize chat
                initializeChat(currentUserId, likedUserId);
                
                // Show match notification to current user
                JOptionPane.showMessageDialog(null, "It's a Match!");
                
                // Create notification for the other user
                createMatchNotification(currentUserId, likedUserId);
            }
        } catch (Exception e) {
            System.err.println("Error checking for match: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createMatchRecord(String user1Id, String user2Id) {
        try {
            // Check if match already exists
            String matchPath = MATCHES_PATH + "/" + user1Id + "/" + user2Id;
            Boolean exists = FirebaseConfig.get(matchPath, Boolean.class);
            
            if (!Boolean.TRUE.equals(exists)) {
                // Create match records for both users
                Map<String, Boolean> matchData = new HashMap<>();
                matchData.put(user2Id, true);
                FirebaseConfig.set(MATCHES_PATH + "/" + user1Id + "/" + user2Id, true);
                FirebaseConfig.set(MATCHES_PATH + "/" + user2Id + "/" + user1Id, true);
            }
        } catch (Exception e) {
            System.err.println("Error creating match record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeChat(String user1Id, String user2Id) {
        try {
            // Create chat ID by sorting user IDs alphabetically
            String chatId = user1Id.compareTo(user2Id) < 0 
                ? user1Id + "_" + user2Id 
                : user2Id + "_" + user1Id;

            // Check if chat metadata already exists
            String metaPath = MESSAGES_PATH + "/" + chatId + "/meta";
            Map<String, Object> existingMeta = FirebaseConfig.get(metaPath, new TypeToken<Map<String, Object>>(){}.getType());

            if (existingMeta == null) {
                // Create chat metadata
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("user1", user1Id.compareTo(user2Id) < 0 ? user1Id : user2Id);
                metadata.put("user2", user1Id.compareTo(user2Id) < 0 ? user2Id : user1Id);
                metadata.put("lastMessage", "");
                metadata.put("timestamp", Instant.now().getEpochSecond());

                FirebaseConfig.set(metaPath, metadata);
            }
        } catch (Exception e) {
            System.err.println("Error initializing chat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createMatchNotification(String currentUserId, String likedUserId) {
        try {
            // Create notification for the other user
            FirebaseConfig.set(NOTIFICATIONS_PATH + "/" + likedUserId + "/" + currentUserId, "match");
        } catch (Exception e) {
            System.err.println("Error creating match notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<String, Boolean> getMatches(String userId) {
        try {
            String matchPath = MATCHES_PATH + "/" + userId;
            return FirebaseConfig.get(matchPath, new TypeToken<Map<String, Boolean>>(){}.getType());
        } catch (Exception e) {
            System.err.println("Error getting matches: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }
} 