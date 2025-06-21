package heartsync.dao;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.google.gson.reflect.TypeToken;

import heartsync.database.FirebaseConfig;

public class MatchDAO {
    private static final String LIKES_PATH = "likes";
    private static final String MATCHES_PATH = "matches";
    private static final String MESSAGES_PATH = "messages";
    private static final String NOTIFICATIONS_PATH = "notifications";

    public void checkAndCreateMatch(String currentUserId, String likedUserId) {
        try {
            System.out.println("Checking for match between " + currentUserId + " and " + likedUserId);
            
            // Check if the other user has already liked the current user
            String otherUserLikePath = LIKES_PATH + "/" + likedUserId + "/" + currentUserId;
            Boolean hasLiked = FirebaseConfig.get(otherUserLikePath, Boolean.class);
            System.out.println("Has " + likedUserId + " liked " + currentUserId + "? " + hasLiked);

            // Check if current user has liked the other user (should be true at this point)
            String currentUserLikePath = LIKES_PATH + "/" + currentUserId + "/" + likedUserId;
            Boolean currentUserLiked = FirebaseConfig.get(currentUserLikePath, Boolean.class);
            System.out.println("Has " + currentUserId + " liked " + likedUserId + "? " + currentUserLiked);

            // Check if match already exists
            String matchPath = MATCHES_PATH + "/" + currentUserId + "/" + likedUserId;
            Boolean matchExists = FirebaseConfig.get(matchPath, Boolean.class);
            System.out.println("Does match already exist? " + matchExists);

            // Both users must have liked each other and match shouldn't already exist
            if (Boolean.TRUE.equals(hasLiked) && Boolean.TRUE.equals(currentUserLiked) && !Boolean.TRUE.equals(matchExists)) {
                System.out.println("Creating new match!");
                // It's a match! Create match records for both users
                createMatchRecord(currentUserId, likedUserId);
                
                // Initialize chat
                initializeChat(currentUserId, likedUserId);
                
                // Create notification for both users
                createMatchNotification(currentUserId, likedUserId);
                createMatchNotification(likedUserId, currentUserId);
                
                // Show match notification to current user
                JOptionPane.showMessageDialog(null, 
                    "🎉 It's a Match! 🎉\nYou and the other person liked each other!", 
                    "New Match!", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("No match created. Either one user hasn't liked the other, or match already exists.");
            }
        } catch (Exception e) {
            System.err.println("Error checking for match: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createMatchRecord(String user1Id, String user2Id) {
        try {
            System.out.println("Creating match record between " + user1Id + " and " + user2Id);
            
            // Create match records for both users
            FirebaseConfig.set(MATCHES_PATH + "/" + user1Id + "/" + user2Id, true);
            FirebaseConfig.set(MATCHES_PATH + "/" + user2Id + "/" + user1Id, true);
            
            System.out.println("Match records created successfully");
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