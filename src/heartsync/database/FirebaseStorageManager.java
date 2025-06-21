package heartsync.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import heartsync.dao.UserDAO;
import heartsync.model.User;
import heartsync.model.UserProfile;

public class FirebaseStorageManager {
    private static final int MAX_IMAGE_SIZE = 800; // Maximum width or height in pixels
    private static final UserDAO userDAO = new UserDAO();

    public static String getProfileImageUrl(String username) {
        try {
            // Get user profile to check if they have a profile picture
            UserProfile profile = DatabaseManagerProfile.getInstance().getUserProfile(username);
            if (profile != null && profile.getProfilePicPath() != null && !profile.getProfilePicPath().isEmpty()) {
                return profile.getProfilePicPath();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String uploadProfilePicture(File imageFile, String username) throws IOException {
        if (!imageFile.exists()) {
            throw new IOException("File not found: " + imageFile.getAbsolutePath());
        }

        // Read and resize the image
        BufferedImage originalImage = ImageIO.read(imageFile);
        BufferedImage resizedImage = resizeImage(originalImage);

        // Convert to Base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "png", baos);
        String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
        
        // Create data URL
        String dataUrl = "data:image/png;base64," + base64Image;
        
        // Store in Firebase Database
        try {
            // Try to get the user profile
            UserProfile profile = DatabaseManagerProfile.getInstance().getUserProfile(username);
            
            if (profile != null) {
                // Update profile with the Base64 image
                profile.setProfilePicPath(dataUrl);
                DatabaseManagerProfile.getInstance().updateUserProfile(profile);
            } else {
                // If profile doesn't exist yet, create one
                profile = new UserProfile();
                profile.setUsername(username);
                profile.setProfilePicPath(dataUrl);
                DatabaseManagerProfile.getInstance().saveUserProfile(profile);
            }
            
            return dataUrl;
        } catch (Exception e) {
            throw new IOException("Failed to update profile with image: " + e.getMessage());
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // Calculate new dimensions while maintaining aspect ratio
        int newWidth = originalWidth;
        int newHeight = originalHeight;
        
        if (originalWidth > MAX_IMAGE_SIZE || originalHeight > MAX_IMAGE_SIZE) {
            if (originalWidth > originalHeight) {
                newWidth = MAX_IMAGE_SIZE;
                newHeight = (int) ((float) originalHeight / originalWidth * MAX_IMAGE_SIZE);
            } else {
                newHeight = MAX_IMAGE_SIZE;
                newWidth = (int) ((float) originalWidth / originalHeight * MAX_IMAGE_SIZE);
            }
        }
        
        // Create new image
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        resizedImage.createGraphics().drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        return resizedImage;
    }

    public static void deleteProfilePicture(String username) throws IOException {
        try {
            // Get and update the user profile
            UserProfile profile = DatabaseManagerProfile.getInstance().getUserProfile(username);
            if (profile != null) {
                profile.setProfilePicPath(null);
                DatabaseManagerProfile.getInstance().updateUserProfile(profile);
            }
        } catch (Exception e) {
            throw new IOException("Failed to delete profile picture: " + e.getMessage());
        }
    }
} 