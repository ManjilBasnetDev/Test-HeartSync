package heartsync.dao;

import com.google.gson.reflect.TypeToken;
import heartsync.database.FirebaseConfig;
import heartsync.model.User;
import java.util.Map;

public class ResetDAO {
    private static final String USERS_PATH = "users";

    public boolean resetPassword(String username, String newPassword) {
        try {
            Map<String, User> users = FirebaseConfig.get(USERS_PATH, 
                new TypeToken<Map<String, User>>(){}.getType());
            
            if (users != null && users.containsKey(username)) {
                User user = users.get(username);
                user.setPassword(newPassword);
                users.put(username, user);
                FirebaseConfig.set(USERS_PATH, users);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateSecurityAnswer(String username, String answer) {
        try {
            Map<String, User> users = FirebaseConfig.get(USERS_PATH, 
                new TypeToken<Map<String, User>>(){}.getType());
            
            if (users != null && users.containsKey(username)) {
                User user = users.get(username);
                return user.getSecurityAnswer().equals(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
} 