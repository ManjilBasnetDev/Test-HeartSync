package heartsync.dao;

import heartsync.model.User;
import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.time.Instant;

public class ReportDAO {
    
    public boolean reportUser(String reportedUserId, String reporterUserId, String reason) {
        try {
            // Get the reported user
            Map<String, User> users = FirebaseConfig.get("users", 
                new TypeToken<Map<String, User>>(){}.getType());
            
            if (users != null && users.containsKey(reportedUserId)) {
                User reportedUser = users.get(reportedUserId);
                
                // Update user type to "reported"
                reportedUser.setUserType("reported");
                
                // Create report details
                Map<String, Object> reportDetails = new HashMap<>();
                reportDetails.put("reporterId", reporterUserId);
                reportDetails.put("reason", reason);
                reportDetails.put("timestamp", Instant.now().getEpochSecond());
                reportDetails.put("status", "pending"); // pending, reviewed, dismissed
                
                // Add report to reports collection
                String reportPath = "reports/" + reportedUserId;
                Map<String, Object> existingReports = FirebaseConfig.get(reportPath, 
                    new TypeToken<Map<String, Object>>(){}.getType());
                
                if (existingReports == null) {
                    existingReports = new HashMap<>();
                }
                
                String reportId = reporterUserId + "_" + Instant.now().getEpochSecond();
                existingReports.put(reportId, reportDetails);
                
                // Update both the user and reports in Firebase
                FirebaseConfig.set("users/" + reportedUserId, reportedUser);
                FirebaseConfig.set(reportPath, existingReports);
                
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean dismissReport(String userId) {
        try {
            // Get the user
            Map<String, User> users = FirebaseConfig.get("users", 
                new TypeToken<Map<String, User>>(){}.getType());
            
            if (users != null && users.containsKey(userId)) {
                User user = users.get(userId);
                
                // Update user type back to "verified"
                user.setUserType("verified");
                
                // Update reports status to dismissed
                String reportPath = "reports/" + userId;
                Map<String, Object> reports = FirebaseConfig.get(reportPath, 
                    new TypeToken<Map<String, Object>>(){}.getType());
                
                if (reports != null) {
                    for (Map.Entry<String, Object> entry : reports.entrySet()) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> reportDetails = (Map<String, Object>) entry.getValue();
                        if ("pending".equals(reportDetails.get("status"))) {
                            reportDetails.put("status", "dismissed");
                        }
                    }
                    
                    // Update both the user and reports in Firebase
                    FirebaseConfig.set("users/" + userId, user);
                    FirebaseConfig.set(reportPath, reports);
                }
                
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
} 