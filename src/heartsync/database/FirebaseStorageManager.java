package heartsync.database;

import java.io.IOException;
import heartsync.dao.UserDAO;
import heartsync.model.User;

/**
 * FirebaseStorageManager now uses Base64 encoding for image storage
 * This replaces the actual Firebase Storage with database-based image storage
 */
public class FirebaseStorageManager {
    private static final UserDAO userDAO = new UserDAO();

    /**
     * Get profile image as Base64 string for a user
     * @param userId User ID
     * @return Base64 encoded image string or null if not found
     */
    public static String getProfileImageUrl(String userId) {
        try {
            // Get user from database to check if they have a profile picture
            User user = userDAO.getUserById(userId);
            if (user != null && user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()) {
                // Return the Base64 string (stored in profilePictureUrl field)
                return user.getProfilePictureUrl();
            }
            // Return null to trigger fallback mechanism
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            // Return null on error to trigger fallback mechanism
            return null;
        }
    }

    /**
     * "Upload" profile picture by encoding it to Base64 and storing in database
     * @param filePath Path to the image file
     * @return Base64 encoded string (acts as the "URL")
     * @throws IOException if encoding fails
     */
    public static String uploadProfilePicture(String filePath) throws IOException {
        // Encode the image file to Base64
        String base64Image = Base64ImageManager.encodeImageToBase64(filePath);
        
        if (base64Image == null) {
            throw new IOException("Failed to encode image to Base64");
        }
        
        // Return the Base64 string (this acts as the "download URL")
        return base64Image;
    }

    /**
     * "Delete" profile picture by returning null (since it's stored in database)
     * The actual deletion happens when the user record is updated
     * @param downloadUrl The Base64 string (ignored for compatibility)
     * @throws IOException (never thrown, kept for interface compatibility)
     */
    public static void deleteProfilePicture(String downloadUrl) throws IOException {
        // Since images are stored as Base64 in the database,
        // deletion happens when the user profile is updated with null/empty value
        // This method is kept for interface compatibility
    }
    
    /**
     * Check if a "URL" is actually a Base64 encoded image
     * @param url The URL/Base64 string to check
     * @return true if it's a valid Base64 image
     */
    public static boolean isBase64Image(String url) {
        return Base64ImageManager.isValidBase64Image(url);
    }
} 