package heartsync.dao;

import heartsync.model.Chat;
import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.io.IOException;

public class ChatDAO {
    private static final String MESSAGES_PATH = "messages";
    
    public boolean sendMessage(String chatId, Chat chat) {
        try {
            String messageId = UUID.randomUUID().toString();
            chat.setMessageId(messageId);
            chat.setChatId(chatId);
            chat.setTimestamp(System.currentTimeMillis());
            
            // Save the message
            FirebaseConfig.put(MESSAGES_PATH + "/" + chatId + "/messages/" + messageId, chat);
            
            // Update metadata
            Map<String, Object> meta = new java.util.HashMap<>();
            meta.put("lastMessage", chat.getMessage());
            meta.put("timestamp", chat.getTimestamp());
            FirebaseConfig.patch(MESSAGES_PATH + "/" + chatId + "/meta", meta);
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Chat> getMessages(String chatId) {
        List<Chat> messages = new ArrayList<>();
        try {
            Map<String, Chat> map = FirebaseConfig.get(MESSAGES_PATH + "/" + chatId + "/messages", 
                new TypeToken<Map<String, Chat>>(){}.getType());
            if (map != null) {
                messages.addAll(map.values());
                messages.sort((a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
    public List<String> getUserChats(String userId) {
        List<String> chatIds = new ArrayList<>();
        try {
            Map<String, Map<String, Object>> chats = FirebaseConfig.get(MESSAGES_PATH, 
                new TypeToken<Map<String, Map<String, Object>>>(){}.getType());
                
            if (chats != null) {
                for (Map.Entry<String, Map<String, Object>> entry : chats.entrySet()) {
                    String chatId = entry.getKey();
                    Map<String, Object> meta = (Map<String, Object>) entry.getValue().get("meta");
                    if (meta != null) {
                        String user1 = (String) meta.get("user1");
                        String user2 = (String) meta.get("user2");
                        if (userId.equals(user1) || userId.equals(user2)) {
                            chatIds.add(chatId);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chatIds;
    }
} 