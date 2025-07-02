package heartsync.database;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Base64ImageManager handles image encoding/decoding for database storage
 * This replaces Firebase Storage with Base64 encoded images stored directly in the database
 */
public class Base64ImageManager {
    
    // Maximum image size in bytes (1MB compressed)
    private static final int MAX_IMAGE_SIZE = 1024 * 1024;
    
    // Standard compression quality for profile pictures
    private static final int PROFILE_PIC_WIDTH = 300;
    private static final int PROFILE_PIC_HEIGHT = 300;
    
    /**
     * Encode a file image to Base64 string for database storage
     * @param filePath Path to the image file
     * @return Base64 encoded string of the image
     * @throws IOException if file cannot be read or processed
     */
    public static String encodeImageToBase64(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            return null;
        }
        
        File imageFile = new File(filePath);
        if (!imageFile.exists()) {
            throw new IOException("Image file not found: " + filePath);
        }
        
        // Read and resize the image
        BufferedImage originalImage = ImageIO.read(imageFile);
        if (originalImage == null) {
            throw new IOException("Invalid image file: " + filePath);
        }
        
        // Resize to standard profile picture size
        BufferedImage resizedImage = resizeImage(originalImage, PROFILE_PIC_WIDTH, PROFILE_PIC_HEIGHT);
        
        // Convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String formatName = getImageFormat(filePath);
        
        // Use JPEG for better compression
        if (!"png".equalsIgnoreCase(formatName)) {
            formatName = "jpg";
        }
        
        ImageIO.write(resizedImage, formatName, baos);
        byte[] imageBytes = baos.toByteArray();
        
        // Check if image is too large
        if (imageBytes.length > MAX_IMAGE_SIZE) {
            // Try to compress further by reducing quality
            baos = new ByteArrayOutputStream();
            BufferedImage smallerImage = resizeImage(originalImage, 200, 200);
            ImageIO.write(smallerImage, "jpg", baos);
            imageBytes = baos.toByteArray();
            
            if (imageBytes.length > MAX_IMAGE_SIZE) {
                throw new IOException("Image too large even after compression. Please use a smaller image.");
            }
        }
        
        // Encode to Base64
        return Base64.getEncoder().encodeToString(imageBytes);
    }
    
    /**
     * Decode Base64 string to BufferedImage
     * @param base64String Base64 encoded image string
     * @return BufferedImage object
     * @throws IOException if decoding fails
     */
    public static BufferedImage decodeBase64ToImage(String base64String) throws IOException {
        if (base64String == null || base64String.trim().isEmpty()) {
            return null;
        }
        
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            return ImageIO.read(bais);
        } catch (Exception e) {
            throw new IOException("Failed to decode Base64 image: " + e.getMessage());
        }
    }
    
    /**
     * Create ImageIcon from Base64 string with specified dimensions
     * @param base64String Base64 encoded image string
     * @param width Desired width
     * @param height Desired height
     * @return ImageIcon object or null if decoding fails
     */
    public static ImageIcon createImageIconFromBase64(String base64String, int width, int height) {
        try {
            BufferedImage image = decodeBase64ToImage(base64String);
            if (image != null) {
                Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (IOException e) {
            System.err.println("Error creating ImageIcon from Base64: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Create standard profile picture ImageIcon from Base64 string
     * @param base64String Base64 encoded image string
     * @return ImageIcon object with standard profile picture dimensions
     */
    public static ImageIcon createProfileImageIcon(String base64String) {
        return createImageIconFromBase64(base64String, 150, 150);
    }
    
    /**
     * Create circular profile picture ImageIcon from Base64 string
     * @param base64String Base64 encoded image string
     * @param size Diameter of the circular image
     * @return Circular ImageIcon or null if processing fails
     */
    public static ImageIcon createCircularProfileImageIcon(String base64String, int size) {
        try {
            BufferedImage image = decodeBase64ToImage(base64String);
            if (image != null) {
                BufferedImage circularImage = createCircularImage(image, size);
                return new ImageIcon(circularImage);
            }
        } catch (IOException e) {
            System.err.println("Error creating circular ImageIcon from Base64: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Create rounded rectangular profile picture ImageIcon from Base64 string
     * @param base64String Base64 encoded image string
     * @param width Width of the image
     * @param height Height of the image
     * @param cornerRadius Radius of rounded corners
     * @return Rounded ImageIcon or null if processing fails
     */
    public static ImageIcon createRoundedProfileImageIcon(String base64String, int width, int height, int cornerRadius) {
        try {
            BufferedImage image = decodeBase64ToImage(base64String);
            if (image != null) {
                BufferedImage roundedImage = createRoundedImage(image, width, height, cornerRadius);
                return new ImageIcon(roundedImage);
            }
        } catch (IOException e) {
            System.err.println("Error creating rounded ImageIcon from Base64: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Resize image to specified dimensions
     */
    private static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }
    
    /**
     * Create circular image from BufferedImage
     */
    private static BufferedImage createCircularImage(BufferedImage image, int size) {
        BufferedImage circularImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2d = circularImage.createGraphics();
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Create circular clip
        g2d.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2d.drawImage(image, 0, 0, size, size, null);
        g2d.dispose();
        
        return circularImage;
    }
    
    /**
     * Create rounded rectangular image from BufferedImage
     */
    private static BufferedImage createRoundedImage(BufferedImage image, int width, int height, int cornerRadius) {
        BufferedImage roundedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2d = roundedImage.createGraphics();
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Create rounded rectangle clip
        g2d.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        
        return roundedImage;
    }
    
    /**
     * Get image format from file extension
     */
    private static String getImageFormat(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "png":
                return "png";
            case "jpg":
            case "jpeg":
                return "jpg";
            case "gif":
                return "gif";
            default:
                return "jpg"; // Default to JPEG
        }
    }
    
    /**
     * Check if a string is a valid Base64 encoded image
     * @param base64String String to check
     * @return true if valid Base64 image, false otherwise
     */
    public static boolean isValidBase64Image(String base64String) {
        if (base64String == null || base64String.trim().isEmpty()) {
            return false;
        }
        
        try {
            byte[] decoded = Base64.getDecoder().decode(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(decoded);
            BufferedImage image = ImageIO.read(bais);
            return image != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Create a placeholder ImageIcon for when no profile picture is available
     * @param width Width of placeholder
     * @param height Height of placeholder
     * @return Placeholder ImageIcon
     */
    public static ImageIcon createPlaceholderIcon(int width, int height) {
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2d = placeholder.createGraphics();
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Gray background
        g2d.setColor(new java.awt.Color(200, 200, 200));
        g2d.fillRect(0, 0, width, height);
        
        // Person icon
        g2d.setColor(new java.awt.Color(150, 150, 150));
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Head
        int headRadius = width / 6;
        g2d.fillOval(centerX - headRadius, centerY - height / 3, headRadius * 2, headRadius * 2);
        
        // Body
        int bodyWidth = width / 3;
        int bodyHeight = height / 3;
        g2d.fillOval(centerX - bodyWidth / 2, centerY - headRadius / 2, bodyWidth, bodyHeight);
        
        g2d.dispose();
        return new ImageIcon(placeholder);
    }
} 