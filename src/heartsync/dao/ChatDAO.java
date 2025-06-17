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
    public boolean sendMessage(String chatId, Chat chat) {
        try {
            String messageId = UUID.randomUUID().toString();
            chat.setMessageId(messageId);
            chat.setChatId(chatId);
            FirebaseConfig.put(FirebaseConfig.getChatPath(chatId) + "/" + messageId, chat);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Chat> getMessages(String chatId) {
        List<Chat> messages = new ArrayList<>();
        try {
            Map<String, Chat> map = FirebaseConfig.get(FirebaseConfig.getChatPath(chatId), new TypeToken<Map<String, Chat>>(){}.getType());
            if (map != null) {
                messages.addAll(map.values());
                messages.sort((a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }
} 