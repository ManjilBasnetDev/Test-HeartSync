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

/**
 * Handles all database operations related to Chat.
 *
 * @author Manjil
 */
public class ChatDAO {

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
} 