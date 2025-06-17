package heartsync.dao;

import heartsync.model.UserForgot;
import heartsync.model.User;
import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import java.io.IOException;

/**
 * DAO dedicated to Forgot-Password flow.
 * Moved into its own file so the public class name matches the filename.
 */
public class UserDAOForgot {
    public UserForgot findByUsername(String username) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user.getUsername().equals(username)) {
                        UserForgot uf = new UserForgot();
                        uf.setId(0); // Not used
                        uf.setUsername(user.getUsername());
                        uf.setPassword(user.getPassword());
                        uf.setFavoriteColor(user.getFavoriteColor());
                        uf.setFirstSchool(user.getFirstSchool());
                        return uf;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateSecurityQuestions(String username, String favoriteColor, String firstSchool) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (User user : users.values()) {
                    if (user.getUsername().equals(username)
                        && favoriteColor != null && firstSchool != null
                        && favoriteColor.equalsIgnoreCase(user.getFavoriteColor())
                        && firstSchool.equalsIgnoreCase(user.getFirstSchool())) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveSecurityQuestions(String username, String favoriteColor, String firstSchool) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user.getUsername().equals(username)) {
                        user.setFavoriteColor(favoriteColor);
                        user.setFirstSchool(firstSchool);
                        FirebaseConfig.put(FirebaseConfig.getUserPath(entry.getKey()), user);
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(String username, String newPlainPassword) {
        try {
            Map<String, User> users = FirebaseConfig.get("users", new TypeToken<Map<String, User>>(){}.getType());
            if (users != null) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    User user = entry.getValue();
                    if (user.getUsername().equals(username)) {
                        user.setPassword(newPlainPassword);
                        FirebaseConfig.put(FirebaseConfig.getUserPath(entry.getKey()), user);
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
