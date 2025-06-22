package heartsync.database;

import heartsync.model.UserProfile;
import heartsync.model.User;
import com.google.gson.reflect.TypeToken;
import java.util.*;

public class DatingDatabase {
    private static DatingDatabase instance;
    
    // Firebase paths
    private static final String USERS_PATH = "users";
    private static final String LIKES_PATH = "likes";
    private static final String MATCHES_PATH = "matches";
    private static final String NOTIFICATIONS_PATH = "notifications";
    
    private DatingDatabase() {}
    
    public static DatingDatabase getInstance() {
        if (instance == null) {
            instance = new DatingDatabase();
        }
        return instance;
    }
    
    public int getUserCount() {
        try {
            String path = USERS_PATH;
            Map<String, Object> users = FirebaseConfig.get(path, new TypeToken<Map<String, Object>>(){}.getType());
            if (users != null) {
                return users.size();
            }
        } catch (Exception e) {
            System.err.println("Error getting user count: " + e.getMessage());
        }
        return 0;
    }
    
    // ========== USER PROFILE METHODS ==========
    
    public UserProfile getUserProfile(String username) {
        try {
            String path = USERS_PATH + "/" + username;
            Map<String, Object> userData = FirebaseConfig.get(path, new TypeToken<Map<String, Object>>(){}.getType());
            return mapToUserProfile(username, userData);
        } catch (Exception e) {
            System.err.println("Error getting user profile: " + e.getMessage());
            return null;
        }
    }
    
    // Method to create/update user profile from User object
    public boolean createOrUpdateUserProfile(User user) {
        try {
            if (user == null || user.getUsername() == null) {
                return false;
            }
            
            // Get gender - if not set in User object, try to get from profile setup data
            String gender = user.getGender();
            String fullName = user.getFullName();
            String aboutMe = user.getBio();
            String profilePicPath = user.getProfilePictureUrl();
            
            if (gender == null || gender.isEmpty() || fullName == null || fullName.isEmpty()) {
                // Try to get data from the UserProfile data stored by ProfileSetupView
                try {
                    heartsync.database.DatabaseManagerProfile dbManager = heartsync.database.DatabaseManagerProfile.getInstance();
                    heartsync.model.UserProfile profileData = dbManager.getUserProfile(user.getUsername());
                    
                    if (profileData != null) {
                        if (gender == null || gender.isEmpty()) {
                            gender = profileData.getGender();
                        }
                        if (fullName == null || fullName.isEmpty()) {
                            fullName = profileData.getFullName();
                        }
                        if (aboutMe == null || aboutMe.isEmpty()) {
                            aboutMe = profileData.getAboutMe();
                        }
                        if (profilePicPath == null || profilePicPath.isEmpty()) {
                            profilePicPath = profileData.getProfilePicPath();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error getting profile setup data: " + e.getMessage());
                }
            }
            
            // Create user data map for Firebase
            Map<String, Object> userData = new HashMap<>();
            userData.put("fullName", fullName != null ? fullName : user.getUsername());
            userData.put("gender", gender);
            userData.put("dateOfBirth", user.getDateOfBirth());
            userData.put("email", user.getEmail());
            userData.put("phoneNumber", user.getPhoneNumber());
            userData.put("aboutMe", aboutMe);
            userData.put("profilePicPath", profilePicPath);
            userData.put("interests", user.getInterests());
            userData.put("userType", user.getUserType());
            
            // Add default values for missing fields
            userData.put("country", "Not specified");
            userData.put("address", "Not specified");
            userData.put("education", "Not specified");
            userData.put("occupation", "Not specified");
            userData.put("height", 170);
            userData.put("weight", 70);
            userData.put("hobbies", new ArrayList<>());
            userData.put("relationshipGoal", "Dating");
            userData.put("religion", "Not specified");
            userData.put("ethnicity", "Not specified");
            userData.put("languages", new ArrayList<>());
            
            String userPath = USERS_PATH + "/" + user.getUsername();
            FirebaseConfig.set(userPath, userData);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error creating/updating user profile: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to ensure user profile exists after registration
    public void ensureUserProfileExists(String username) {
        try {
            UserProfile existingProfile = getUserProfile(username);
            if (existingProfile == null || existingProfile.getGender() == null || existingProfile.getGender().isEmpty()) {
                // Try to get User data and create/update profile
                heartsync.dao.UserDAO userDAO = new heartsync.dao.UserDAO();
                User user = userDAO.getUserByUsername(username);
                if (user != null) {
                    createOrUpdateUserProfile(user);
                }
            }
        } catch (Exception e) {
            System.err.println("Error ensuring user profile exists: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public List<UserProfile> getExplorableProfiles(String currentUsername) {
        try {
            // Get current user's profile to check gender
            UserProfile currentUser = getUserProfile(currentUsername);
            if (currentUser == null) {
                return new ArrayList<>();
            }
            
            String currentGender = currentUser.getGender();
            if (currentGender == null || currentGender.isEmpty()) {
                return new ArrayList<>();
            }
            
            String targetGender = currentGender.equalsIgnoreCase("Male") ? "Female" : "Male";
            
            // Get all users
            Map<String, Map<String, Object>> allUsers = FirebaseConfig.get(USERS_PATH, 
                new TypeToken<Map<String, Map<String, Object>>>(){}.getType());
            
            if (allUsers == null) {
                return new ArrayList<>();
            }
            
            List<UserProfile> explorableProfiles = new ArrayList<>();
            
            // Get users that current user has already liked
            Set<String> likedUsers = getLikedUsernames(currentUsername);
            
            for (Map.Entry<String, Map<String, Object>> entry : allUsers.entrySet()) {
                String username = entry.getKey();
                Map<String, Object> userData = entry.getValue();
                
                // Skip current user
                if (username.equals(currentUsername)) {
                    continue;
                }
                
                // Skip already liked users
                if (likedUsers.contains(username)) {
                    continue;
                }
                
                // Check gender preference
                String userGender = (String) userData.get("gender");
                if (targetGender.equalsIgnoreCase(userGender)) {
                    UserProfile profile = mapToUserProfile(username, userData);
                    if (profile != null) {
                        explorableProfiles.add(profile);
                    }
                }
            }
            
            return explorableProfiles;
            
        } catch (Exception e) {
            System.err.println("Error getting explorable profiles: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ========== LIKE SYSTEM ==========
    
    public boolean likeUser(String fromUsername, String toUsername) {
        try {
            // Save the like
            String likePath = LIKES_PATH + "/" + fromUsername + "/" + toUsername;
            FirebaseConfig.set(likePath, true);
            
            // Check if it's a mutual like (match)
            if (hasUserLiked(toUsername, fromUsername)) {
                // It's a match!
                createMatch(fromUsername, toUsername);
                
                // Send match notifications
                addNotification(fromUsername, "🎉 You matched with " + getUserDisplayName(toUsername) + "!");
                addNotification(toUsername, "🎉 You matched with " + getUserDisplayName(fromUsername) + "!");
                
                System.out.println("Match created between " + fromUsername + " and " + toUsername);
                return true;
            } else {
                // Just a like, send notification to the liked user
                addNotification(toUsername, "❤️ " + getUserDisplayName(fromUsername) + " liked you!");
                System.out.println(fromUsername + " liked " + toUsername);
                return true;
            }
            
        } catch (Exception e) {
            System.err.println("Error liking user: " + e.getMessage());
            return false;
        }
    }
    
    public boolean hasUserLiked(String fromUsername, String toUsername) {
        try {
            String likePath = LIKES_PATH + "/" + fromUsername + "/" + toUsername;
            Boolean liked = FirebaseConfig.get(likePath, Boolean.class);
            return liked != null && liked;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Set<String> getLikedUsernames(String username) {
        try {
            String likesPath = LIKES_PATH + "/" + username;
            Map<String, Boolean> likes = FirebaseConfig.get(likesPath, 
                new TypeToken<Map<String, Boolean>>(){}.getType());
            
            if (likes != null) {
                return likes.keySet();
            }
        } catch (Exception e) {
            System.err.println("Error getting liked users: " + e.getMessage());
        }
        return new HashSet<>();
    }
    
    public boolean removeLike(String fromUsername, String toUsername) {
        try {
            String likePath = LIKES_PATH + "/" + fromUsername + "/" + toUsername;
            FirebaseConfig.set(likePath, null); // Setting to null deletes the node

            // If they were a match, remove the match for both users
            if (isMatched(fromUsername, toUsername)) {
                String match1Path = MATCHES_PATH + "/" + fromUsername + "/" + toUsername;
                String match2Path = MATCHES_PATH + "/" + toUsername + "/" + fromUsername;
                FirebaseConfig.set(match1Path, null);
                FirebaseConfig.set(match2Path, null);
                System.out.println("Match removed between " + fromUsername + " and " + toUsername);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error removing like: " + e.getMessage());
            return false;
        }
    }
    
    public List<UserProfile> getMyLikes(String username) {
        Set<String> likedUsernames = getLikedUsernames(username);
        List<UserProfile> profiles = new ArrayList<>();
        
        for (String likedUsername : likedUsernames) {
            UserProfile profile = getUserProfile(likedUsername);
            if (profile != null) {
                profiles.add(profile);
            }
        }
        
        return profiles;
    }
    
    public List<UserProfile> getMyLikers(String username) {
        try {
            Map<String, Map<String, Boolean>> allLikes = FirebaseConfig.get(LIKES_PATH,
                new TypeToken<Map<String, Map<String, Boolean>>>(){}.getType());
            
            List<UserProfile> likers = new ArrayList<>();
            
            if (allLikes == null) {
                return likers;
            }

            for (Map.Entry<String, Map<String, Boolean>> entry : allLikes.entrySet()) {
                String likerUsername = entry.getKey();
                Map<String, Boolean> likedMap = entry.getValue();

                if (likedMap != null && likedMap.containsKey(username) && Boolean.TRUE.equals(likedMap.get(username))) {
                    UserProfile profile = getUserProfile(likerUsername);
                    if (profile != null) {
                        likers.add(profile);
                    }
                }
            }
            return likers;
            
        } catch (Exception e) {
            System.err.println("Error getting likers: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // ========== MATCH SYSTEM ==========
    
    private void createMatch(String user1, String user2) {
        try {
            String match1Path = MATCHES_PATH + "/" + user1 + "/" + user2;
            String match2Path = MATCHES_PATH + "/" + user2 + "/" + user1;
            
            FirebaseConfig.set(match1Path, true);
            FirebaseConfig.set(match2Path, true);
            
        } catch (Exception e) {
            System.err.println("Error creating match: " + e.getMessage());
        }
    }
    
    public List<UserProfile> getMatches(String username) {
        try {
            String matchesPath = MATCHES_PATH + "/" + username;
            Map<String, Boolean> matches = FirebaseConfig.get(matchesPath, 
                new TypeToken<Map<String, Boolean>>(){}.getType());
            
            List<UserProfile> matchedProfiles = new ArrayList<>();
            
            if (matches != null) {
                for (String matchedUsername : matches.keySet()) {
                    UserProfile profile = getUserProfile(matchedUsername);
                    if (profile != null) {
                        matchedProfiles.add(profile);
                    }
                }
            }
            
            return matchedProfiles;
            
        } catch (Exception e) {
            System.err.println("Error getting matches: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public boolean isMatched(String user1, String user2) {
        try {
            String matchPath = MATCHES_PATH + "/" + user1 + "/" + user2;
            Boolean matched = FirebaseConfig.get(matchPath, Boolean.class);
            return matched != null && matched;
        } catch (Exception e) {
            return false;
        }
    }
    
    // ========== NOTIFICATION SYSTEM ==========
    
    public void addNotification(String username, String message) {
        try {
            String notificationPath = NOTIFICATIONS_PATH + "/" + username;
            
            // Get existing notifications
            List<String> notifications = getNotifications(username);
            notifications.add(0, message); // Add to beginning
            
            // Keep only last 50 notifications
            if (notifications.size() > 50) {
                notifications = notifications.subList(0, 50);
            }
            
            FirebaseConfig.set(notificationPath, notifications);
            
        } catch (Exception e) {
            System.err.println("Error adding notification: " + e.getMessage());
        }
    }
    
    public List<String> getNotifications(String username) {
        try {
            String notificationPath = NOTIFICATIONS_PATH + "/" + username;
            List<String> notifications = FirebaseConfig.get(notificationPath, 
                new TypeToken<List<String>>(){}.getType());
            
            return notifications != null ? notifications : new ArrayList<>();
            
        } catch (Exception e) {
            System.err.println("Error getting notifications: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ========== HELPER METHODS ==========
    
    private String getUserDisplayName(String username) {
        UserProfile profile = getUserProfile(username);
        if (profile != null && profile.getFullName() != null && !profile.getFullName().trim().isEmpty()) {
            return profile.getFullName();
        }
        return username;
    }
    
    private UserProfile mapToUserProfile(String username, Map<String, Object> userData) {
        if (userData == null) return null;
        
        try {
            UserProfile profile = new UserProfile();
            profile.setUsername(username);
            profile.setFullName((String) userData.get("fullName"));
            profile.setGender((String) userData.get("gender"));
            profile.setDateOfBirth((String) userData.get("dateOfBirth"));
            profile.setCountry((String) userData.get("country"));
            profile.setAddress((String) userData.get("address"));
            profile.setPhoneNumber((String) userData.get("phoneNumber"));
            profile.setEducation((String) userData.get("education"));
            profile.setAboutMe((String) userData.get("aboutMe"));
            profile.setProfilePicPath((String) userData.get("profilePicPath"));
            profile.setRelationshipGoal((String) userData.get("relationshipGoal"));
            
            Object hobbiesObj = userData.get("hobbies");
            if (hobbiesObj instanceof List) {
                profile.setHobbies((List<String>) hobbiesObj);
            }
            
            Object heightObj = userData.get("height");
            if (heightObj instanceof Number) {
                profile.setHeight(((Number) heightObj).intValue());
            }
            
            return profile;
            
        } catch (Exception e) {
            System.err.println("Error mapping user profile: " + e.getMessage());
            return null;
        }
    }
    
    // Method to clean up test users from the database
    public void removeTestUsers() {
        try {
            String[] testUsernames = {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "Sarah", "David", "Emily", "John", "Maria", "Bob", 
                "Alice Johnson", "Bob Smith", "Carol Williams", "David Brown", "Emma Davis",
                "User2", "testuser", "demo", "sample"
            };
            
            System.out.println("Cleaning up test users...");
            
            for (String username : testUsernames) {
                try {
                    // Remove from users path
                    String userPath = USERS_PATH + "/" + username;
                    FirebaseConfig.set(userPath, null);
                    
                    // Remove from likes path
                    String likesPath = LIKES_PATH + "/" + username;
                    FirebaseConfig.set(likesPath, null);
                    
                    // Remove from matches path
                    String matchesPath = MATCHES_PATH + "/" + username;
                    FirebaseConfig.set(matchesPath, null);
                    
                    // Remove from notifications path
                    String notificationsPath = NOTIFICATIONS_PATH + "/" + username;
                    FirebaseConfig.set(notificationsPath, null);
                    
                    System.out.println("Removed test user: " + username);
                } catch (Exception e) {
                    // Ignore errors for non-existent users
                }
            }
            
            System.out.println("Test user cleanup completed!");
            
        } catch (Exception e) {
            System.err.println("Error during test user cleanup: " + e.getMessage());
        }
    }

    // Method to delete a user completely from the dating database
    public boolean deleteUser(String username) {
        try {
            // Remove from users path
            String userPath = USERS_PATH + "/" + username;
            FirebaseConfig.set(userPath, null);
            
            // Remove from likes path (their likes)
            String likesPath = LIKES_PATH + "/" + username;
            FirebaseConfig.set(likesPath, null);
            
            // Remove likes TO this user from others
            Map<String, Map<String, Boolean>> allLikes = FirebaseConfig.get(LIKES_PATH,
                new TypeToken<Map<String, Map<String, Boolean>>>(){}.getType());
            if (allLikes != null) {
                for (String likerUsername : allLikes.keySet()) {
                    String likePath = LIKES_PATH + "/" + likerUsername + "/" + username;
                    FirebaseConfig.set(likePath, null);
                }
            }
            
            // Remove from matches path (their matches)
            String matchesPath = MATCHES_PATH + "/" + username;
            FirebaseConfig.set(matchesPath, null);
            
            // Remove matches TO this user from others
            Map<String, Map<String, Boolean>> allMatches = FirebaseConfig.get(MATCHES_PATH,
                new TypeToken<Map<String, Map<String, Boolean>>>(){}.getType());
            if (allMatches != null) {
                for (String matcherUsername : allMatches.keySet()) {
                    String matchPath = MATCHES_PATH + "/" + matcherUsername + "/" + username;
                    FirebaseConfig.set(matchPath, null);
                }
            }
            
            // Remove from notifications path
            String notificationsPath = NOTIFICATIONS_PATH + "/" + username;
            FirebaseConfig.set(notificationsPath, null);
            
            System.out.println("Successfully deleted user: " + username);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
} 