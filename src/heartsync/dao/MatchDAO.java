package heartsync.dao;

import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class MatchDAO {
    public boolean setMatch(String userId, String otherUserId, boolean matched) {
        try {
            FirebaseConfig.put(FirebaseConfig.getMatchPath(userId, otherUserId), matched);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isMatch(String userId, String otherUserId) {
        try {
            Boolean match = FirebaseConfig.get(FirebaseConfig.getMatchPath(userId, otherUserId), Boolean.class);
            return match != null && match;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getMatches(String userId) {
        List<String> matches = new ArrayList<>();
        try {
            Map<String, Boolean> map = FirebaseConfig.get("matches/" + userId, new TypeToken<Map<String, Boolean>>(){}.getType());
            if (map != null) {
                for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                    if (entry.getValue() != null && entry.getValue()) {
                        matches.add(entry.getKey());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }
} 