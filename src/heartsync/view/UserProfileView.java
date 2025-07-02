package heartsync.view;

import heartsync.model.UserProfile;
import heartsync.database.Base64ImageManager;
import heartsync.database.FirebaseStorageManager;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class UserProfileView extends JFrame {

    private final UserProfile userProfile;

    public UserProfileView(UserProfile userProfile) {
        this.userProfile = userProfile;
        initUI();
    }

    private void initUI() {
        setTitle(userProfile.getFullName() + "'s Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 900);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(createProfilePanel());
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createProfilePanel() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();

        // Header (Pic, Name, Location)
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        container.add(createHeaderPanel(), gbc);

        // About Me
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(40, 0, 0, 0);
        container.add(createDetailItem("About Me:", userProfile.getAboutMe()), gbc);

        // Details Grid
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        container.add(createDetailsGrid(), gbc);

        return container;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setOpaque(false);

        JLabel profilePicLabel = new JLabel();
        profilePicLabel.setPreferredSize(new Dimension(150, 150));
        loadUserImage(profilePicLabel, userProfile.getProfilePicPath(), userProfile.getUsername());
        headerPanel.add(profilePicLabel, BorderLayout.WEST);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);

        JLabel nameLabel = new JLabel(userProfile.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        namePanel.add(nameLabel);
        namePanel.add(Box.createVerticalStrut(5));

        JLabel locationLabel = new JLabel(userProfile.getAddress() + ", " + userProfile.getCountry());
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        locationLabel.setForeground(Color.GRAY);
        namePanel.add(locationLabel);

        headerPanel.add(namePanel, BorderLayout.CENTER);
        return headerPanel;
    }
    
    private JPanel createDetailsGrid() {
        JPanel detailsContainer = new JPanel(new GridLayout(0, 2, 40, 20));
        detailsContainer.setOpaque(false);
        detailsContainer.add(createDetailItem("Age:", userProfile.getAge() + " years"));
        detailsContainer.add(createDetailItem("Gender:", userProfile.getGender()));
        detailsContainer.add(createDetailItem("Education:", userProfile.getEducation()));
        detailsContainer.add(createDetailItem("Hobbies:", String.join(", ", userProfile.getHobbies())));
        detailsContainer.add(createDetailItem("Relationship Goal:", userProfile.getRelationshipGoal()));
        detailsContainer.add(createDetailItem("Phone Number:", userProfile.getPhoneNumber()));
        return detailsContainer;
    }

    private JPanel createDetailItem(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelComponent.setForeground(new Color(220, 53, 69));

        JPanel labelContainer = new JPanel(new BorderLayout());
        labelContainer.setOpaque(false);
        labelContainer.add(labelComponent, BorderLayout.NORTH);

        JTextArea valueComponent = new JTextArea(value);
        valueComponent.setEditable(false);
        valueComponent.setOpaque(false);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        valueComponent.setLineWrap(true);
        valueComponent.setWrapStyleWord(true);

        panel.add(labelContainer, BorderLayout.WEST);
        panel.add(valueComponent, BorderLayout.CENTER);
        
        if ("Hobbies:".equals(label)) {
            panel.setPreferredSize(new Dimension(0, 100));
        } else if (!"About Me:".equals(label)) {
            panel.setPreferredSize(new Dimension(0, 40));
        }

        return panel;
    }

    private void loadUserImage(JLabel imageLabel, String picPath, String username) {
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                Image image = null;
                String imageData = FirebaseStorageManager.getProfileImageUrl(username);
                
                // Try to load from Base64 first
                if (imageData != null && !imageData.isEmpty()) {
                    if (FirebaseStorageManager.isBase64Image(imageData)) {
                        // It's a Base64 encoded image
                        ImageIcon base64Icon = Base64ImageManager.createCircularProfileImageIcon(imageData, 150);
                        if (base64Icon != null) {
                            return base64Icon;
                        }
                    } else {
                        // It's an old URL, try to load it
                        try {
                            image = ImageIO.read(new URL(imageData));
                        } catch (Exception e) {
                            System.out.println("Failed to load image from URL: " + e.getMessage());
                        }
                    }
                }
                
                // Fallback to picPath if available
                if (image == null && picPath != null && !picPath.isEmpty()) {
                    if (Base64ImageManager.isValidBase64Image(picPath)) {
                        // picPath is actually Base64
                        ImageIcon base64Icon = Base64ImageManager.createCircularProfileImageIcon(picPath, 150);
                        if (base64Icon != null) {
                            return base64Icon;
                        }
                    } else if (picPath.startsWith("http")) {
                        try {
                            image = ImageIO.read(new URL(picPath));
                        } catch (Exception e) {
                            System.out.println("Failed to load image from URL: " + e.getMessage());
                        }
                    } else {
                        try {
                            URL resourceUrl = getClass().getResource("/ImagePicker/" + picPath);
                            if (resourceUrl != null) image = ImageIO.read(resourceUrl);
                        } catch (Exception e) {
                            System.out.println("Failed to load image from resource: " + e.getMessage());
                        }
                    }
                }
                
                // Final fallback to default image
                if (image == null) {
                    try {
                        image = ImageIO.read(getClass().getResource("/ImagePicker/RajeshHamalPhoto.png"));
                    } catch (Exception e) {
                        // If even default image fails, create a placeholder
                        return Base64ImageManager.createPlaceholderIcon(150, 150);
                    }
                }
                
                // Convert regular image to circular
                if (image != null) {
                    Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    BufferedImage circularImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = circularImage.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setClip(new Ellipse2D.Float(0, 0, 150, 150));
                    g2.drawImage(scaledImage, 0, 0, null);
                    g2.dispose();
                    return new ImageIcon(circularImage);
                }
                
                // Ultimate fallback
                return Base64ImageManager.createPlaceholderIcon(150, 150);
            }

            @Override
            protected void done() {
                try {
                    imageLabel.setIcon(get());
                } catch (Exception e) {
                    e.printStackTrace();
                    // Set placeholder on error
                    imageLabel.setIcon(Base64ImageManager.createPlaceholderIcon(150, 150));
                }
            }
        }.execute();
    }
} 