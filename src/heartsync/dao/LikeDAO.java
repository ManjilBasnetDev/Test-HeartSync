package heartsync.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.google.gson.reflect.TypeToken;

import heartsync.database.FirebaseConfig;
import heartsync.model.Chat;

public class LikeDAO {
    private static final String LIKES_PATH = "likes";
    private static final String PASSES_PATH = "user_passes";
    private static final String MATCHES_PATH = "matches";
    private static final String MESSAGES_PATH = "messages";
    
    private final MatchDAO matchDAO;

    public LikeDAO() {
        this.matchDAO = new MatchDAO();
    }

    public void saveLike(String currentUserId, String likedUserId) {
        try {
            System.out.println("Attempting to save like from " + currentUserId + " to " + likedUserId);
            
            // Save the like using username
            String likePath = LIKES_PATH + "/" + currentUserId + "/" + likedUserId;
            FirebaseConfig.set(likePath, true);
            System.out.println("Like saved successfully at path: " + likePath);

            // Check for mutual like directly
            String mutualLikePath = LIKES_PATH + "/" + likedUserId + "/" + currentUserId;
            Boolean mutualLike = FirebaseConfig.get(mutualLikePath, Boolean.class);
            System.out.println("Checking mutual like at path: " + mutualLikePath + ", result: " + mutualLike);

            if (Boolean.TRUE.equals(mutualLike)) {
                System.out.println("Mutual like detected! Creating match...");
                
                // Create match entries for both users
                FirebaseConfig.set(MATCHES_PATH + "/" + currentUserId + "/" + likedUserId, true);
                FirebaseConfig.set(MATCHES_PATH + "/" + likedUserId + "/" + currentUserId, true);
                System.out.println("Match entries created in database");

                // Initialize chat
                String chatId = currentUserId.compareTo(likedUserId) < 0 
                    ? currentUserId + "_" + likedUserId 
                    : likedUserId + "_" + currentUserId;
                
                // Create chat metadata
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("user1", currentUserId);
                metadata.put("user2", likedUserId);
                metadata.put("lastMessage", "");
                metadata.put("timestamp", System.currentTimeMillis());
                
                Map<String, Object> messageData = new HashMap<>();
                messageData.put("meta", metadata);
                messageData.put("messages", new HashMap<>());
                
                FirebaseConfig.set(MESSAGES_PATH + "/" + chatId, messageData);
                System.out.println("Chat initialized with ID: " + chatId);

                // Create welcome message
                Chat welcomeChat = new Chat();
                welcomeChat.setMessageId(UUID.randomUUID().toString());
                welcomeChat.setChatId(chatId);
                welcomeChat.setSenderId("system");
                welcomeChat.setMessage("You are now matched! Say hello! 👋");
                welcomeChat.setTimestamp(System.currentTimeMillis());
                
                FirebaseConfig.set(MESSAGES_PATH + "/" + chatId + "/messages/" + welcomeChat.getMessageId(), welcomeChat);
                System.out.println("Welcome message created");

                // Update metadata with welcome message
                Map<String, Object> updatedMeta = new HashMap<>();
                updatedMeta.put("lastMessage", welcomeChat.getMessage());
                updatedMeta.put("timestamp", welcomeChat.getTimestamp());
                FirebaseConfig.patch(MESSAGES_PATH + "/" + chatId + "/meta", updatedMeta);

                // Show match notification
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null,
                        "🎉 It's a Match! 🎉\nYou and the other person liked each other!",
                        "New Match!",
                        JOptionPane.INFORMATION_MESSAGE);
                });
            }
        } catch (Exception e) {
            System.err.println("Error saving like: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save like: " + e.getMessage());
        }
    }

    public boolean hasLiked(String userId, String otherUserId) {
        try {
            String likePath = LIKES_PATH + "/" + userId + "/" + otherUserId;
            Boolean liked = FirebaseConfig.get(likePath, Boolean.class);
            return Boolean.TRUE.equals(liked);
        } catch (Exception e) {
            System.err.println("Error checking like status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Boolean> getLikes(String userId) {
        try {
            String likesPath = LIKES_PATH + "/" + userId;
            Map<String, Boolean> likes = FirebaseConfig.get(likesPath, new TypeToken<Map<String, Boolean>>(){}.getType());
            return likes != null ? likes : new HashMap<>();
        } catch (Exception e) {
            System.err.println("Error getting likes: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void removeLike(String userId, String otherUserId) {
        try {
            String likePath = LIKES_PATH + "/" + userId + "/" + otherUserId;
            FirebaseConfig.delete(likePath);
        } catch (Exception e) {
            System.err.println("Error removing like: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to remove like: " + e.getMessage());
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
        System.out.println("Starting match creation between " + userId1 + " and " + userId2);
        
        // Create match entries for both users
        FirebaseConfig.put(MATCHES_PATH + "/" + userId1 + "/" + userId2, true);
        System.out.println("Added match entry for " + userId1);
        
        FirebaseConfig.put(MATCHES_PATH + "/" + userId2 + "/" + userId1, true);
        System.out.println("Added match entry for " + userId2);
        
        // Initialize chat
        // Sort user IDs alphabetically to create a consistent chat ID
        String chatId = userId1.compareTo(userId2) < 0 ? userId1 + "_" + userId2 : userId2 + "_" + userId1;
        System.out.println("Created chat ID: " + chatId);
        
        // Create the messages node structure
        Map<String, Object> messageData = new HashMap<>();
        
        // Create metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("user1", userId1);
        metadata.put("user2", userId2);
        metadata.put("lastMessage", "");
        metadata.put("timestamp", System.currentTimeMillis());
        messageData.put("meta", metadata);
        
        // Create empty messages map
        Map<String, Object> messages = new HashMap<>();
        messageData.put("messages", messages);
        
        // Save the entire message structure
        FirebaseConfig.put(MESSAGES_PATH + "/" + chatId, messageData);
        System.out.println("Created chat room structure");
        
        // Create a welcome message
        Chat welcomeChat = new Chat();
        welcomeChat.setMessageId(UUID.randomUUID().toString());
        welcomeChat.setChatId(chatId);
        welcomeChat.setSenderId("system");
        welcomeChat.setMessage("You are now matched! Say hello! 👋");
        welcomeChat.setTimestamp(System.currentTimeMillis());
        
        // Save welcome message
        FirebaseConfig.put(MESSAGES_PATH + "/" + chatId + "/messages/" + welcomeChat.getMessageId(), welcomeChat);
        System.out.println("Added welcome message to chat");
        
        // Update metadata with welcome message
        Map<String, Object> updatedMeta = new HashMap<>();
        updatedMeta.put("lastMessage", welcomeChat.getMessage());
        updatedMeta.put("timestamp", welcomeChat.getTimestamp());
        FirebaseConfig.patch(MESSAGES_PATH + "/" + chatId + "/meta", updatedMeta);
        
        // Add notification for both users
        String matchNotification = "💘 Match: You have a new match!";
        FirebaseConfig.put("notifications/" + userId1 + "/" + System.currentTimeMillis(), matchNotification);
        FirebaseConfig.put("notifications/" + userId2 + "/" + System.currentTimeMillis(), matchNotification);
        System.out.println("Added match notifications for both users");
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

    // Get all users who liked a specific user
    public List<String> getLikersOfUser(String userId) {
        List<String> likers = new ArrayList<>();
        try {
            // Get directly from user_likers path
            Map<String, Boolean> userLikers = FirebaseConfig.get("user_likers/" + userId,
                new TypeToken<Map<String, Boolean>>(){}.getType());
            
            if (userLikers != null) {
                likers.addAll(userLikers.keySet());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return likers;
    }

    private void unmatch(String userId1, String userId2) throws IOException {
        FirebaseConfig.delete(MATCHES_PATH + "/" + userId1 + "/" + userId2);
        FirebaseConfig.delete(MATCHES_PATH + "/" + userId2 + "/" + userId1);
    }
} 