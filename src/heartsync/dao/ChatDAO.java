/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.dao;

import heartsync.database.FirebaseConfig;
import heartsync.model.Chat;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

/**
 * Handles all database operations related to Chat.
 *
 * @author Manjil
 */
public class ChatDAO {
    private static final String MESSAGES_PATH = "messages";
    private static final String MATCHES_PATH = "matches";

    public String getChatId(String user1, String user2) {
        if (user1.compareTo(user2) > 0) {
            String temp = user1;
            user1 = user2;
            user2 = temp;
        }
        return user1 + "_" + user2;
    }

    public void sendMessage(Chat chat) throws IOException {
        String chatId = getChatId(chat.getSenderId(), chat.getReceiverId());
        // The 'post' method handles creating a new unique ID for the message
        FirebaseConfig.post("chats/" + chatId, chat);
    }

    public List<Chat> getConversation(String user1, String user2) {
        String chatId = getChatId(user1, user2);
        try {
            Map<String, Chat> messages = FirebaseConfig.get("chats/" + chatId, new TypeToken<Map<String, Chat>>() {}.getType());
            if (messages == null) {
                return new ArrayList<>();
            }
            // Convert map to list and sort by timestamp
            return messages.values().stream()
                    .sorted(Comparator.comparing(Chat::getTimestamp))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Chat getLastMessage(String user1, String user2) {
        List<Chat> conversation = getConversation(user1, user2);
        if (conversation.isEmpty()) {
            return null;
        }
        return conversation.get(conversation.size() - 1);
    }
    
    public Chat getLastChat(String chatId) {
        try {
            Map<String, Chat> chats = FirebaseConfig.get("chats/" + chatId, new TypeToken<Map<String, Chat>>(){}.getType());
            if (chats == null || chats.isEmpty()) {
                return null;
            }

            // Find the latest chat entry by timestamp
            return Collections.max(chats.values(), Comparator.comparing(Chat::getTimestamp));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getMatchedChats(String currentUserId) {
        try {
            // Get all chat metadata
            Map<String, Map<String, Object>> allChats = FirebaseConfig.get(MESSAGES_PATH, 
                new TypeToken<Map<String, Map<String, Object>>>(){}.getType());

            if (allChats == null) {
                return new ArrayList<>();
            }

            return allChats.entrySet().stream()
                .filter(entry -> {
                    Map<String, Object> meta = (Map<String, Object>) entry.getValue().get("meta");
                    if (meta != null) {
                        String user1 = (String) meta.get("user1");
                        String user2 = (String) meta.get("user2");
                        return currentUserId.equals(user1) || currentUserId.equals(user2);
                    }
                    return false;
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error getting matched chats: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Map<String, Object> getChatMetadata(String chatId) {
        try {
            String metaPath = MESSAGES_PATH + "/" + chatId + "/meta";
            return FirebaseConfig.get(metaPath, new TypeToken<Map<String, Object>>(){}.getType());
        } catch (Exception e) {
            System.err.println("Error getting chat metadata: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public String getChatIdForUsers(String user1Id, String user2Id) {
        // Create chat ID by sorting user IDs alphabetically
        return user1Id.compareTo(user2Id) < 0 
            ? user1Id + "_" + user2Id 
            : user2Id + "_" + user1Id;
    }

    public void updateLastMessage(String chatId, String message) {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("lastMessage", message);
            updates.put("timestamp", System.currentTimeMillis());
            
            String metaPath = MESSAGES_PATH + "/" + chatId + "/meta";
            FirebaseConfig.patch(metaPath, updates);
        } catch (Exception e) {
            System.err.println("Error updating last message: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 