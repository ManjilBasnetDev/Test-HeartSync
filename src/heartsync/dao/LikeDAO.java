package heartsync.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import heartsync.database.FirebaseConfig;
import heartsync.model.Chat;

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
            messageData.put("messages", new HashMap<>());
            
            // Save the entire message structure
            FirebaseConfig.put(MESSAGES_PATH + "/" + chatId, messageData);
            
            // Create a welcome message
            Chat welcomeChat = new Chat();
            welcomeChat.setMessageId(UUID.randomUUID().toString());
            welcomeChat.setChatId(chatId);
            welcomeChat.setSenderId("system");
            welcomeChat.setMessage("You are now matched! Say hello! 👋");
            welcomeChat.setTimestamp(System.currentTimeMillis());
            
            // Save welcome message
            FirebaseConfig.put(MESSAGES_PATH + "/" + chatId + "/messages/" + welcomeChat.getMessageId(), welcomeChat);
            
            // Update metadata with welcome message
            Map<String, Object> updatedMeta = new HashMap<>();
            updatedMeta.put("lastMessage", welcomeChat.getMessage());
            updatedMeta.put("timestamp", welcomeChat.getTimestamp());
            FirebaseConfig.patch(MESSAGES_PATH + "/" + chatId + "/meta", updatedMeta);
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

    // Get all users who liked a specific user
    public List<String> getLikersOfUser(String userId) {
        List<String> likers = new ArrayList<>();
        try {
            // This is inefficient and not scalable.
            // A better approach would be to have a dedicated `likers` node in Firebase.
            // For now, we iterate through all likes.
            Map<String, Map<String, Boolean>> allLikes = FirebaseConfig.get(LIKES_PATH,
                new TypeToken<Map<String, Map<String, Boolean>>>(){}.getType());

            if (allLikes != null) {
                for (Map.Entry<String, Map<String, Boolean>> entry : allLikes.entrySet()) {
                    String otherUserId = entry.getKey();
                    Map<String, Boolean> likedMap = entry.getValue();
                    if (likedMap != null && likedMap.containsKey(userId)) {
                        likers.add(otherUserId);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return likers;
    }

    public boolean removeLike(String userId, String dislikedUserId) {
        try {
            // Remove the like entry
            FirebaseConfig.delete(LIKES_PATH + "/" + userId + "/" + dislikedUserId);

            // If users were matched, unmatch them
            if (isMatched(userId, dislikedUserId)) {
                unmatch(userId, dislikedUserId);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void unmatch(String userId1, String userId2) throws IOException {
        FirebaseConfig.delete(MATCHES_PATH + "/" + userId1 + "/" + userId2);
        FirebaseConfig.delete(MATCHES_PATH + "/" + userId2 + "/" + userId1);
    }
} 