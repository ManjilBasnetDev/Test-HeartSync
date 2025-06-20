/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package heartsync.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import heartsync.controller.UserProfileController;
import heartsync.dao.LikeDAO;
import heartsync.database.DatabaseManagerProfile;
import heartsync.model.User;
import heartsync.model.UserProfile;
import heartsync.navigation.WindowManager;
import heartsync.database.FirebaseConfig;
import com.google.gson.reflect.TypeToken;

/**
 * Modern swipe interface for browsing through potential matches.
 * @author manjil-basnet
 */
public class Swipe extends JFrame {
    private static final int WINDOW_RADIUS = 20;
    private static final Color BACKGROUND_COLOR = new Color(255, 245, 250); // Very light pink background
    private static final Color BUTTON_COLOR = new Color(231, 76, 60); // Red for action buttons
    private static final Color LIKE_COLOR = new Color(46, 204, 113); // Fresh green
    private static final Color REJECT_COLOR = new Color(231, 76, 60); // Coral red
    private static final Color BUTTON_HOVER_OVERLAY = new Color(0, 0, 0, 40);
    private static final Color NAV_COLOR = new Color(50, 50, 50); // Dark gray for nav items
    private static final Color NAV_ACTIVE_COLOR = new Color(255, 105, 180); // Bright pink for active nav
    private static final Color WINDOW_CONTROL_COLOR = new Color(51, 51, 51); // Dark gray for window controls
    private static final Color TITLE_BAR_COLOR = new Color(33, 33, 33); // Dark title bar color
    private static final Color SEARCH_BG = new Color(245, 245, 245); // Light gray for search
    private static final Color CARD_BG = Color.WHITE; // White for card
    private static final Color BUTTON_TEXT = new Color(255, 255, 255); // White text for buttons
    private static final int CARD_RADIUS = 15; // Rounded corners for card
    private static final int IMAGE_CONTAINER_WIDTH = 450;  // Fixed width for image container
    private static final int IMAGE_CONTAINER_HEIGHT = 600; // Fixed height for image container
    private static final int CARD_WIDTH = 400;  // Card width
    private static final int CARD_HEIGHT = 750; // Increased height for more vertical space
    private static final int PHOTO_HEIGHT = 450; // Slightly reduced height for better containment
    private static final Color CARD_SHADOW = new Color(0, 0, 0, 30);
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 60);
    private static final Color FILTER_BUTTON_COLOR = new Color(103, 58, 183); // Modern purple
    private static final Color SEARCH_ICON_COLOR = new Color(128, 128, 128);
    
    private JPanel mainPanel;
    private JPanel cardPanel;
    private JPanel navigationPanel;
    private JPanel searchPanel;
    private JLabel imageLabel;
    private JLabel nameLabel;
    private JLabel ageLabel;
    private JLabel bioLabel;
    private JLabel locationLabel;
    private JLabel interestsLabel;
    private RoundedButton nextButton;
    private RoundedButton backButton;
    private RoundedButton likeButton;
    private RoundedButton rejectButton;
    private ArrayList<ProfileData> profiles;
    private ArrayList<ProfileData> allProfiles;
    private int currentIndex;
    private JLabel profileLabel;
    private JLabel exploreLabel;
    private JLabel chatLabel;
    private JTextField searchField;
    private JButton filterButton;
    private CardLayout cardLayout;
    private JPanel contentCards;
    private LikeDAO likeDAO;
    private String currentUserId;
    
    private static class ProfileData {
        String name;
        int age;
        String bio;
        List<String> photos;  // List to store multiple photos
        int currentPhotoIndex = 0;  // Track current photo index
        String userId;  // Add userId to track likes/passes

        ProfileData(String name, int age, String bio, List<String> photos, String userId) {
            this.name = name;
            this.age = age;
            this.bio = bio;
            this.photos = photos;
            this.userId = userId;
        }
    }

    private static class RoundedButton extends JButton {
        private boolean isHovered = false;
        private final Color baseColor;
        private static final int RADIUS = 22; // Slightly smaller

        public RoundedButton(String text, Color color) {
            super(text);
            this.baseColor = color;
            setupButton();
            setPreferredSize(new Dimension(90, 45)); // Slightly smaller buttons
        }

        private void setupButton() {
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background
            if (isHovered) {
                g2.setColor(baseColor.darker());
            } else {
                g2.setColor(baseColor);
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
            
            // Add subtle gradient effect
            if (isHovered) {
                g2.setColor(BUTTON_HOVER_OVERLAY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
            }
            
            super.paintComponent(g);
        }
    }

    public Swipe() {
        // Initialize LikeDAO
        likeDAO = new LikeDAO();
        currentUserId = User.getCurrentUser().getUserId();
        
        // Set up window properties
        setTitle("HeartSync - Find Love");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set application icon
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/ImagePicker/HomePageCoupleImg.png"));
            if (icon.getImage() != null) {
                setIconImage(icon.getImage());
            }
        } catch (Exception e) {
            System.err.println("Could not load application icon: " + e.getMessage());
        }
        
        // Initialize components
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Setup UI components
        setupUI();
        
        // Set the main panel as content pane
        setContentPane(mainPanel);
    }
    
    private void setupUI() {
        // Setup navigation panel
        setupNavigationPanel();
        mainPanel.add(navigationPanel, BorderLayout.NORTH);
        
        // Create card layout for content
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setBackground(BACKGROUND_COLOR);
        
        // Add content panels
        contentCards.add(createExplorePanel(), "explore");
        contentCards.add(new LikesView(User.getCurrentUser().getUserId(), false), "likes");
        contentCards.add(new LikesView(User.getCurrentUser().getUserId(), true), "likers");
        
        mainPanel.add(contentCards, BorderLayout.CENTER);
        
        // Show explore panel by default
        cardLayout.show(contentCards, "explore");
        
        // Load initial profiles
        setupProfiles();
    }

    private void setupNavigationPanel() {
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        navigationPanel.setOpaque(false);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Create navigation items
        exploreLabel = new JLabel("Explore");
        profileLabel = new JLabel("My Profile");
        chatLabel = new JLabel("Chat");
        JLabel matchedLabel = new JLabel("Matched Users");
        JLabel myLikesLabel = new JLabel("My Likes");
        JLabel myLikersLabel = new JLabel("My Likers");
        JLabel logoutLabel = new JLabel("Logout");
        
        // Style navigation items
        Font navFont = new Font("Segoe UI", Font.BOLD, 16);
        
        JLabel[] allLabels = new JLabel[]{exploreLabel, profileLabel, chatLabel, matchedLabel, myLikesLabel, myLikersLabel, logoutLabel};
        
        for (JLabel label : allLabels) {
            label.setFont(navFont);
            label.setForeground(NAV_COLOR);
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (label == exploreLabel) {
                        showExplore();
                    } else if (label == profileLabel) {
                        showProfile();
                    } else if (label == chatLabel) {
                        // TODO: Implement chat functionality
                        WindowManager.show(ChatSystem.class, ChatSystem::new, Swipe.this);
                    } else if (label == matchedLabel) {
                        // TODO: Show matched users
                    } else if (label == myLikesLabel) {
                        // TODO: Show users I liked
                    } else if (label == myLikersLabel) {
                        // TODO: Show users who liked me
                    } else if (label == logoutLabel) {
                        // Handle logout
                        int choice = JOptionPane.showConfirmDialog(
                            Swipe.this,
                            "Are you sure you want to logout?",
                            "Logout Confirmation",
                            JOptionPane.YES_NO_OPTION
                        );
                        if (choice == JOptionPane.YES_OPTION) {
                            WindowManager.show(LoginView.class, LoginView::new, Swipe.this);
                            dispose();
                        }
                    }
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!label.getForeground().equals(NAV_ACTIVE_COLOR)) {
                        label.setForeground(NAV_ACTIVE_COLOR.darker());
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!label.getForeground().equals(NAV_ACTIVE_COLOR)) {
                        label.setForeground(NAV_COLOR);
                    }
                }
            });
        }
        
        // Add all labels to the navigation panel
        for (JLabel label : allLabels) {
            navigationPanel.add(label);
        }
    }

    private void setupSearchPanel() {
        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.setBackground(BACKGROUND_COLOR);
        
        // Create modern search field
        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.setPreferredSize(new Dimension(400, 40));
        searchWrapper.setBackground(SEARCH_BG);
        searchWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        searchField = new JTextField();
        searchField.setBorder(null);
        searchField.setBackground(SEARCH_BG);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setForeground(new Color(60, 60, 60));
        
        // Add search icon
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchIcon.setForeground(SEARCH_ICON_COLOR);
        searchWrapper.add(searchIcon, BorderLayout.WEST);
        searchWrapper.add(searchField, BorderLayout.CENTER);
        
        searchPanel.add(searchWrapper);
        
        // Add filter button
        filterButton = createFiltersButton();
        searchPanel.add(filterButton);
        
        setupSearchLogic();
    }

    private void showFilterDialog() {
        JDialog filterDialog = new JDialog(this, "Search Filters", true);
        filterDialog.setLayout(new BorderLayout());
        filterDialog.setSize(400, 500);
        filterDialog.setLocationRelativeTo(this);

        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Education filter
        filterPanel.add(new JLabel("Education:"), gbc);
        String[] education = {"Any", "High School", "Bachelor's", "Master's", "PhD"};
        JComboBox<String> educationBox = new JComboBox<>(education);
        filterPanel.add(educationBox, gbc);

        // Relationship type filter
        filterPanel.add(new JLabel("Relationship Type:"), gbc);
        String[] relationshipTypes = {"Any", "Casual", "Serious", "Benefits"};
        JComboBox<String> relationshipBox = new JComboBox<>(relationshipTypes);
        filterPanel.add(relationshipBox, gbc);

        // Age range filter
        filterPanel.add(new JLabel("Age Range:"), gbc);
        JPanel agePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JSpinner minAge = new JSpinner(new SpinnerNumberModel(18, 18, 100, 1));
        JSpinner maxAge = new JSpinner(new SpinnerNumberModel(50, 18, 100, 1));
        agePanel.add(new JLabel("From:"));
        agePanel.add(minAge);
        agePanel.add(new JLabel("To:"));
        agePanel.add(maxAge);
        filterPanel.add(agePanel, gbc);

        // Apply button
        JButton applyButton = new JButton("Apply Filters");
        applyButton.addActionListener(e -> {
            // TODO: Apply filters to search
            filterDialog.dispose();
        });
        
        filterDialog.add(filterPanel, BorderLayout.CENTER);
        filterDialog.add(applyButton, BorderLayout.SOUTH);
        filterDialog.setVisible(true);
    }

    private JPanel createExplorePanel() {
        // Initialize labels if they haven't been initialized
        if (nameLabel == null) {
            nameLabel = new JLabel();
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        }
        
        if (ageLabel == null) {
            ageLabel = new JLabel();
            ageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            ageLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        }
        
        if (bioLabel == null) {
            bioLabel = new JLabel();
            bioLabel.setHorizontalAlignment(SwingConstants.CENTER);
            bioLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        }
        
        if (imageLabel == null) {
            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(IMAGE_CONTAINER_WIDTH, IMAGE_CONTAINER_HEIGHT));
        }
        
        // Create the main explore panel
        JPanel explorePanel = new JPanel(new BorderLayout(20, 20));
        explorePanel.setBackground(BACKGROUND_COLOR);
        explorePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create profile card panel
        cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Create photo navigation panel
        JPanel photoNavPanel = new JPanel(new BorderLayout());
        photoNavPanel.setOpaque(false);
        
        // Add left arrow
        JLabel leftArrow = new JLabel("←");
        leftArrow.setFont(new Font("SansSerif", Font.BOLD, 40));
        leftArrow.setForeground(new Color(200, 200, 200));
        leftArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        leftArrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPreviousProfile();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                leftArrow.setForeground(new Color(150, 150, 150));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                leftArrow.setForeground(new Color(200, 200, 200));
            }
        });
        
        // Add right arrow
        JLabel rightArrow = new JLabel("→");
        rightArrow.setFont(new Font("SansSerif", Font.BOLD, 40));
        rightArrow.setForeground(new Color(200, 200, 200));
        rightArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightArrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNextProfile();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                rightArrow.setForeground(new Color(150, 150, 150));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                rightArrow.setForeground(new Color(200, 200, 200));
            }
        });
        
        // Add arrows to photo nav panel
        photoNavPanel.add(leftArrow, BorderLayout.WEST);
        photoNavPanel.add(imageLabel, BorderLayout.CENTER);
        photoNavPanel.add(rightArrow, BorderLayout.EAST);
        
        // Create info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_BG);
        infoPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        // Add components to info panel
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(ageLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(bioLabel);
        
        // Add components to card panel
        cardPanel.add(photoNavPanel, BorderLayout.CENTER);
        cardPanel.add(infoPanel, BorderLayout.SOUTH);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setOpaque(false);
        
        // Initialize buttons if they haven't been
        if (likeButton == null) likeButton = new RoundedButton("Like", LIKE_COLOR);
        if (rejectButton == null) rejectButton = new RoundedButton("Pass", REJECT_COLOR);
        
        // Add button listeners
        likeButton.addActionListener(e -> likeCurrentProfile());
        rejectButton.addActionListener(e -> rejectCurrentProfile());
        
        // Add buttons to panel
        buttonsPanel.add(rejectButton);
        buttonsPanel.add(likeButton);
        
        // Add components to explore panel
        explorePanel.add(cardPanel, BorderLayout.CENTER);
        explorePanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return explorePanel;
    }

    private JLabel createPhotoNavArrow(String arrow) {
        JLabel arrowLabel = new JLabel(arrow);
        arrowLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        arrowLabel.setPreferredSize(new Dimension(40, 200));
        arrowLabel.setHorizontalAlignment(SwingConstants.CENTER);
        arrowLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        arrowLabel.setOpaque(false);
        arrowLabel.setForeground(NAV_COLOR);
        
        // Add hover effect
        arrowLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                arrowLabel.setForeground(NAV_COLOR.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                arrowLabel.setForeground(NAV_COLOR);
            }
        });
        
        return arrowLabel;
    }

    private RoundedButton createActionButton(String icon, String text, Color color) {
        RoundedButton button = new RoundedButton(text, color) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw circle background
                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                
                // Draw icon and text
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
                FontMetrics fm = g2.getFontMetrics();
                
                // Center the icon
                int iconX = (getWidth() - fm.stringWidth(icon)) / 2;
                int iconY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent() - 10;
                g2.drawString(icon, iconX, iconY);
                
                // Draw text below
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                g2.drawString(text, textX, getHeight() - 15);
            }
        };
        
        button.setPreferredSize(new Dimension(80, 80));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void showNextPhoto() {
        if (profiles.isEmpty()) return;
        ProfileData currentProfile = profiles.get(currentIndex);
        if (currentProfile.currentPhotoIndex < currentProfile.photos.size() - 1) {
            currentProfile.currentPhotoIndex++;
            updateProfilePhoto();
        }
    }

    private void showPreviousPhoto() {
        if (profiles.isEmpty()) return;
        ProfileData currentProfile = profiles.get(currentIndex);
        if (currentProfile.currentPhotoIndex > 0) {
            currentProfile.currentPhotoIndex--;
            updateProfilePhoto();
        }
    }

    private void updateProfilePhoto() {
        if (profiles.isEmpty()) return;
        ProfileData profile = profiles.get(currentIndex);
        if (profile.photos.isEmpty()) {
            imageLabel.setIcon(null);
            imageLabel.setText("No photos available");
            return;
        }
        String photoPath = profile.photos.get(profile.currentPhotoIndex);
        try {
            Image image = null;
            // First try as absolute path
            if (photoPath.startsWith("/")) {
                File file = new File(photoPath);
                if (file.exists()) {
                    image = ImageIO.read(file);
                }
            }
            // Then try as relative path in ImagePicker directory
            if (image == null) {
                File file = new File("src/ImagePicker/" + photoPath);
                if (file.exists()) {
                    image = ImageIO.read(file);
                }
            }
            // Finally try as resource
            if (image == null) {
                URL resourceUrl = getClass().getResource("/ImagePicker/" + photoPath);
                if (resourceUrl != null) {
                    image = ImageIO.read(resourceUrl);
                }
            }
            
            if (image != null) {
                int originalWidth = image.getWidth(null);
                int originalHeight = image.getHeight(null);
                double widthScale = (double) (CARD_WIDTH - 40) / originalWidth;
                double heightScale = (double) (PHOTO_HEIGHT - 10) / originalHeight;
                double scale = Math.min(widthScale, heightScale); // Changed to min to maintain aspect ratio
                int scaledWidth = (int) (originalWidth * scale);
                int scaledHeight = (int) (originalHeight * scale);
                Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
                imageLabel.setText("");
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("Error loading image");
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageLabel.setIcon(null);
            imageLabel.setText("Error loading image");
        }
    }

    private void showCurrentProfile() {
        if (profiles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No profiles match your search criteria.",
                "No Matches Found",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        ProfileData profile = profiles.get(currentIndex);
        
        // Update profile photo
        if (profile.photos != null && !profile.photos.isEmpty()) {
            try {
                String photoUrl = profile.photos.get(profile.currentPhotoIndex);
                ImageIcon icon = new ImageIcon(new URL(photoUrl));
                Image img = icon.getImage().getScaledInstance(IMAGE_CONTAINER_WIDTH, IMAGE_CONTAINER_HEIGHT, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                // Use default image if photo loading fails
                imageLabel.setIcon(new ImageIcon("path/to/default/profile.png"));
            }
        }
        
        // Update profile info
        nameLabel.setText(profile.name);
        ageLabel.setText(profile.age + " years");
        bioLabel.setText("<html><body style='width: 300px'>" + profile.bio + "</body></html>");
    }

    private void showNextProfile() {
        if (currentIndex < profiles.size() - 1) {
            currentIndex++;
            showCurrentProfile();
        } else if (!profiles.isEmpty()) {
            // Show "All profiles finished" message and loop back
            int choice = JOptionPane.showConfirmDialog(this,
                "You've seen all profiles! Would you like to start over?",
                "All Profiles Viewed",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (choice == JOptionPane.YES_OPTION) {
                currentIndex = 0;
                showCurrentProfile();
            }
        }
    }

    private void showPreviousProfile() {
        if (currentIndex > 0) {
            currentIndex--;
            showCurrentProfile();
        }
    }

    private void likeCurrentProfile() {
        if (profiles.isEmpty()) return;
        
        ProfileData profile = profiles.get(currentIndex);
        boolean success = likeDAO.addLike(currentUserId, profile.userId);
        
        if (success) {
            // Check if it's a match
            if (likeDAO.isMatched(currentUserId, profile.userId)) {
                JOptionPane.showMessageDialog(this,
                    "It's a match! You can now chat with " + profile.name,
                    "New Match!",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            showNextProfile();
        }
    }

    private void rejectCurrentProfile() {
        if (profiles.isEmpty()) return;
        ProfileData profile = profiles.get(currentIndex);
        
        // Add to passes in Firebase
        if (likeDAO.addPass(currentUserId, profile.userId)) {
            // Remove from current list
            profiles.remove(currentIndex);
            if (currentIndex >= profiles.size()) {
                currentIndex = 0;
            }
            
            if (profiles.isEmpty()) {
                showCurrentProfile(); // Will show the "no profiles" message
            } else {
                showCurrentProfile();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Error saving pass. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel createProfilePicture() {
        JLabel profilePicture = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1);
                g2.setClip(circle);
                
                BufferedImage img = null;
                String profilePicPath = UserProfile.getCurrentUser().getProfilePicPath();
                
                // Try to load the profile picture
                if (profilePicPath != null && !profilePicPath.isEmpty()) {
                    try {
                        // First try as absolute path
                        File file = new File(profilePicPath);
                        if (file.exists()) {
                            img = ImageIO.read(file);
                        }
                        
                        // Then try in ImagePicker directory
                        if (img == null) {
                            file = new File("src/ImagePicker/" + profilePicPath);
                            if (file.exists()) {
                                img = ImageIO.read(file);
                            }
                        }
                        
                        // Finally try as resource
                        if (img == null) {
                            URL resourceUrl = getClass().getResource("/ImagePicker/" + profilePicPath);
                            if (resourceUrl != null) {
                                img = ImageIO.read(resourceUrl);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
                // If no profile picture was loaded, try to load the default one
                if (img == null) {
                    try {
                        File defaultImg = new File("src/ImagePicker/RajeshHamalPhoto.png");
                        if (defaultImg.exists()) {
                            img = ImageIO.read(defaultImg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
                // Draw the image or a colored circle if no image is available
                if (img != null) {
                    g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g2.setColor(new Color(219, 112, 147));
                    g2.fill(circle);
                }
            }
        };

        profilePicture.setPreferredSize(new Dimension(150, 150));
        profilePicture.setMinimumSize(new Dimension(150, 150));
        profilePicture.setMaximumSize(new Dimension(150, 150));
        profilePicture.setAlignmentX(Component.CENTER_ALIGNMENT);

        return profilePicture;
    }

    private JPanel createProfilePanel() {
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(BACKGROUND_COLOR);
        profilePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 20, 10);
        
        // Profile picture
        JLabel profilePicture = createProfilePicture();
        profilePicture.setPreferredSize(new Dimension(150, 150));
        profilePanel.add(profilePicture, gbc);
        
        // Reset gridwidth for other components
        gbc.gridwidth = 1;
        gbc.gridy++;
        
        // Add profile information
        addSectionHeader(profilePanel, "Personal Information", gbc);
        gbc.gridy++;
        
        UserProfile currentProfile = UserProfile.getCurrentUser();
        
        // Basic Information
        addDetailRow(profilePanel, "Full Name:", currentProfile.getFullName(), gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Height:", currentProfile.getHeight() + " cm", gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Country:", currentProfile.getCountry(), gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Address:", currentProfile.getAddress(), gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Phone Number:", currentProfile.getPhoneNumber(), gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Gender:", currentProfile.getGender(), gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Education:", currentProfile.getEducation(), gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Interested In:", currentProfile.getPreferences(), gbc);
        gbc.gridy++;
        
        // Hobbies section
        gbc.gridwidth = 2;
        addSectionHeader(profilePanel, "Hobbies", gbc);
        gbc.gridy++;
        
        List<String> hobbies = currentProfile.getHobbies();
        if (hobbies != null && !hobbies.isEmpty()) {
            JTextArea hobbiesArea = new JTextArea(String.join(", ", hobbies));
            hobbiesArea.setWrapStyleWord(true);
            hobbiesArea.setLineWrap(true);
            hobbiesArea.setOpaque(false);
            hobbiesArea.setEditable(false);
            hobbiesArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            hobbiesArea.setBorder(new EmptyBorder(10, 10, 10, 10));
            
            JScrollPane scrollPane = new JScrollPane(hobbiesArea);
            scrollPane.setPreferredSize(new Dimension(400, 60));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            
            gbc.fill = GridBagConstraints.HORIZONTAL;
            profilePanel.add(scrollPane, gbc);
            gbc.gridy++;
        }
        
        // Relationship Goals section
        addSectionHeader(profilePanel, "Relationship Goals", gbc);
        gbc.gridy++;
        
        JLabel relationshipGoal = new JLabel(currentProfile.getRelationshipGoal());
        relationshipGoal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        relationshipGoal.setBorder(new EmptyBorder(10, 10, 10, 10));
        profilePanel.add(relationshipGoal, gbc);
        gbc.gridy++;
        
        // About Me section
        addSectionHeader(profilePanel, "About Me", gbc);
        gbc.gridy++;
        
        JTextArea aboutMe = new JTextArea(currentProfile.getAboutMe());
        aboutMe.setWrapStyleWord(true);
        aboutMe.setLineWrap(true);
        aboutMe.setOpaque(false);
        aboutMe.setEditable(false);
        aboutMe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        aboutMe.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(aboutMe);
        scrollPane.setPreferredSize(new Dimension(400, 100));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        profilePanel.add(scrollPane, gbc);
        gbc.gridy++;
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton editButton = createProfileButton("Edit Profile");
        editButton.addActionListener(e -> openProfileEditor());
        
        JButton photoButton = createProfileButton("Change Photo");
        photoButton.addActionListener(e -> openPhotoUploader());
        
        buttonPanel.add(editButton);
        buttonPanel.add(photoButton);
        
        gbc.gridy++;
        profilePanel.add(buttonPanel, gbc);
        
        return profilePanel;
    }

    private void addSectionHeader(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel header = new JLabel(text);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setForeground(NAV_ACTIVE_COLOR);
        header.setBorder(new EmptyBorder(10, 0, 5, 0));
        panel.add(header, gbc);
    }

    private void addDetailRow(JPanel panel, String label, String value, GridBagConstraints gbc) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(labelComponent, gbc);

        gbc.gridx = 1;
        JLabel valueComponent = new JLabel(value != null ? value : "Not specified");
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(valueComponent, gbc);
        
        gbc.gridx = 0;
    }

    private JButton createProfileButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(NAV_ACTIVE_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(NAV_ACTIVE_COLOR.brighter());
                } else {
                    g2.setColor(NAV_ACTIVE_COLOR);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
        
        return button;
    }

    private UserProfile loadUserProfile(String username) {
        UserProfile profile = new UserProfile();
        try {
            DatabaseManagerProfile dbManager = DatabaseManagerProfile.getInstance();
            profile = dbManager.getUserProfile(username);
            if (profile == null) {
                JOptionPane.showMessageDialog(this,
                    "Profile not found for user: " + username,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading profile: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return profile;
    }

    private void openProfileEditor() {
        String currentUsername = ""; // TODO: Get actual username from session
        UserProfile userProfile = loadUserProfile(currentUsername);
        
        if (userProfile != null) {
            UserProfileController controller = new UserProfileController(userProfile, currentUsername);
            ProfileSetupView profileSetup = new ProfileSetupView(controller);
            profileSetup.setVisible(true);
        }
    }

    private void openPhotoUploader() {
        // TODO: Implement photo upload functionality
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            // TODO: Handle the selected files (upload to server/database)
        }
    }

    private void viewProfile() {
        // TODO: Implement profile view functionality
    }

    private void showExplore() {
        // Remove any existing components
        mainPanel.removeAll();
        
        // Add top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(navigationPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Create and add explore panel
        JPanel explorePanel = createExplorePanel();
        mainPanel.add(explorePanel, BorderLayout.CENTER);
        
        // Update navigation indicators
        exploreLabel.setForeground(NAV_ACTIVE_COLOR);
        profileLabel.setForeground(NAV_COLOR);
        
        // Refresh the display
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void showProfile() {
        // Remove any existing components
        mainPanel.removeAll();
        
        // Add top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(navigationPanel, BorderLayout.NORTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Create and add profile panel
        JPanel profilePanel = createProfilePanel();
        mainPanel.add(profilePanel, BorderLayout.CENTER);
        
        // Update navigation indicators
        exploreLabel.setForeground(NAV_COLOR);
        profileLabel.setForeground(NAV_ACTIVE_COLOR);
        
        // Refresh the display
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void setupProfiles() {
        profiles = new ArrayList<>();
        try {
            // Get current user and their profile
            User currentUser = User.getCurrentUser();
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this,
                    "Error: Current user not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String currentUsername = currentUser.getUsername();
            currentUserId = currentUser.getUserId();
            
            // Get current user's gender from user_details using username
            String currentUserGender = null;
            try {
                Map<String, Object> userDetails = FirebaseConfig.get("user_details/" + currentUsername, 
                    new TypeToken<Map<String, Object>>(){}.getType());
                
                if (userDetails != null && userDetails.containsKey("gender")) {
                    currentUserGender = (String) userDetails.get("gender");
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Error fetching user gender: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (currentUserGender == null) {
                JOptionPane.showMessageDialog(this,
                    "Please set your gender in your profile first",
                    "Profile Incomplete",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Get all users and their details
            Map<String, User> allUsers = FirebaseConfig.get("users", 
                new TypeToken<Map<String, User>>(){}.getType());
            
            Map<String, Map<String, Object>> allUserDetails = FirebaseConfig.get("user_details", 
                new TypeToken<Map<String, Map<String, Object>>>(){}.getType());
            
            // Get liked and passed users
            Map<String, Boolean> likes = FirebaseConfig.get("user_likes/" + currentUserId + "/likes",
                new TypeToken<Map<String, Boolean>>(){}.getType());
            Map<String, Boolean> passes = FirebaseConfig.get("user_likes/" + currentUserId + "/passes",
                new TypeToken<Map<String, Boolean>>(){}.getType());
            
            Set<String> likedUsers = new HashSet<>();
            Set<String> passedUsers = new HashSet<>();
            
            if (likes != null) likedUsers.addAll(likes.keySet());
            if (passes != null) passedUsers.addAll(passes.keySet());
            
            if (allUsers != null && allUserDetails != null) {
                // First, create a map of username to userId for faster lookup
                Map<String, String> usernameToId = new HashMap<>();
                for (Map.Entry<String, User> entry : allUsers.entrySet()) {
                    usernameToId.put(entry.getValue().getUsername(), entry.getKey());
                }
                
                // Now process each user from user_details
                for (Map.Entry<String, Map<String, Object>> entry : allUserDetails.entrySet()) {
                    String username = entry.getKey();
                    Map<String, Object> details = entry.getValue();
                    
                    // Skip current user
                    if (username.equals(currentUsername)) continue;
                    
                    // Get userId from the map we created
                    String userId = usernameToId.get(username);
                    if (userId == null) continue;
                    
                    // Skip if user has been liked or passed
                    if (likedUsers.contains(userId) || passedUsers.contains(userId)) continue;
                    
                    // Get gender and check if it's different from current user
                    String userGender = (String) details.get("gender");
                    if (userGender == null || userGender.equals(currentUserGender)) continue;
                    
                    // Get required profile information
                    String name = (String) details.get("fullName");
                    String dateOfBirth = (String) details.get("dateOfBirth");
                    String bio = (String) details.get("aboutMe");
                    String profilePicPath = (String) details.get("profilePicPath");
                    
                    // Skip if required information is missing
                    if (name == null || name.isEmpty() || profilePicPath == null || profilePicPath.isEmpty()) continue;
                    
                    // Calculate age
                    int age = calculateAge(dateOfBirth);
                    
                    // Create photos list
                    List<String> photos = new ArrayList<>();
                    photos.add(profilePicPath);
                    
                    // Add to profiles list
                    profiles.add(new ProfileData(name, age, bio != null ? bio : "", photos, userId));
                }
            }
            
            // Save a copy for filtering
            allProfiles = new ArrayList<>(profiles);
            
            // Show first profile if available
            if (!profiles.isEmpty()) {
                showCurrentProfile();
            } else {
                showCurrentProfile(); // Will show the "no profiles" message
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading profiles: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JTextField createSearchField() {
        JTextField searchField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Draw search icon
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int iconSize = 14;
                int x = 10;
                int y = (getHeight() - iconSize) / 2;
                
                g2.setColor(SEARCH_ICON_COLOR);
                g2.setStroke(new BasicStroke(2));
                
                // Draw circle
                g2.drawOval(x, y, iconSize - 4, iconSize - 4);
                
                // Draw handle
                g2.drawLine(x + iconSize - 3, y + iconSize - 3, 
                           x + iconSize + 2, y + iconSize + 2);
            }
        };
        
        // Add padding for search icon
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 30, 5, 10)
        ));
        
        // Remove focus highlight
        searchField.setFocusable(true);
        searchField.setBackground(Color.WHITE);
        
        // Add placeholder text
        searchField.setForeground(Color.GRAY);
        searchField.setText("Search by name, age, or interests...");
        
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search by name, age, or interests...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search by name, age, or interests...");
                }
            }
        });
        
        return searchField;
    }

    private JButton createFiltersButton() {
        JButton filtersButton = new JButton("⚡ Filters") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(FILTER_BUTTON_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(FILTER_BUTTON_COLOR.brighter());
                } else {
                    g2.setColor(FILTER_BUTTON_COLOR);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        
        filtersButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filtersButton.setForeground(Color.WHITE);
        filtersButton.setBorderPainted(false);
        filtersButton.setContentAreaFilled(false);
        filtersButton.setFocusPainted(false);
        filtersButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filtersButton.setPreferredSize(new Dimension(100, 35));
        
        return filtersButton;
    }

    private JLabel createPhotoIndicator(int total, int current) {
        JPanel dotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        dotsPanel.setOpaque(false);
        
        for (int i = 0; i < total; i++) {
            JLabel dot = new JLabel("•");
            dot.setFont(new Font("Segoe UI", Font.BOLD, 24));
            dot.setForeground(i == current ? Color.WHITE : new Color(255, 255, 255, 128));
            dotsPanel.add(dot);
        }
        
        return new JLabel("•") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int dotSize = 8;
                int spacing = 12;
                int totalWidth = (total * (dotSize + spacing)) - spacing;
                int startX = (getWidth() - totalWidth) / 2;
                int centerY = getHeight() / 2;
                
                for (int i = 0; i < total; i++) {
                    if (i == current) {
                        g2.setColor(Color.WHITE);
                        g2.fillOval(startX + (i * (dotSize + spacing)), centerY - dotSize/2, dotSize, dotSize);
                    } else {
                        g2.setColor(new Color(255, 255, 255, 128));
                        g2.drawOval(startX + (i * (dotSize + spacing)), centerY - dotSize/2, dotSize, dotSize);
                    }
                }
            }
        };
    }

    private void setupSearchLogic() {
        if (searchField == null) return;
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterProfiles(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterProfiles(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterProfiles(); }
        });
    }

    private void filterProfiles() {
        String searchText = searchField.getText().toLowerCase().trim();
        
        if (searchText.isEmpty()) {
            profiles = new ArrayList<>(allProfiles);
        } else {
            profiles = new ArrayList<>();
            for (ProfileData profile : allProfiles) {
                if (profile.name.toLowerCase().contains(searchText) ||
                    profile.bio.toLowerCase().contains(searchText)) {
                    profiles.add(profile);
                }
            }
        }
        
        if (profiles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No profiles match your search criteria.",
                "No Matches Found",
                JOptionPane.INFORMATION_MESSAGE);
            profiles = new ArrayList<>(allProfiles);
        }
        
        currentIndex = 0;
        showCurrentProfile();
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            Swipe frame = new Swipe();
            frame.setVisible(true);
        });
    }

    private void displayUserProfile(UserProfile profile) {
        // Create profile panel
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(BACKGROUND_COLOR);
        profilePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 20, 10);
        
        // Profile picture
        JLabel profilePicture = createProfilePicture();
        profilePicture.setPreferredSize(new Dimension(150, 150));
        profilePanel.add(profilePicture, gbc);
        
        // Reset gridwidth for other components
        gbc.gridwidth = 1;
        gbc.gridy++;
        
        // Add profile information
        addSectionHeader(profilePanel, "Personal Information", gbc);
        gbc.gridy++;
        
        // Basic Information
        addDetailRow(profilePanel, "Full Name:", profile.getFullName(), gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Height:", profile.getHeight() + " cm", gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Country:", profile.getCountry(), gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Address:", profile.getAddress(), gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Phone Number:", profile.getPhoneNumber(), gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Gender:", profile.getGender(), gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Education:", profile.getEducation(), gbc);
        gbc.gridy++;
        
        addDetailRow(profilePanel, "Interested In:", profile.getPreferences(), gbc);
        gbc.gridy++;
        
        // Hobbies section
        gbc.gridwidth = 2;
        addSectionHeader(profilePanel, "Hobbies", gbc);
        gbc.gridy++;
        
        List<String> hobbies = profile.getHobbies();
        if (hobbies != null && !hobbies.isEmpty()) {
            JTextArea hobbiesArea = new JTextArea(String.join(", ", hobbies));
            hobbiesArea.setWrapStyleWord(true);
            hobbiesArea.setLineWrap(true);
            hobbiesArea.setOpaque(false);
            hobbiesArea.setEditable(false);
            hobbiesArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            hobbiesArea.setBorder(new EmptyBorder(10, 10, 10, 10));
            
            JScrollPane scrollPane = new JScrollPane(hobbiesArea);
            scrollPane.setPreferredSize(new Dimension(400, 60));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            
            gbc.fill = GridBagConstraints.HORIZONTAL;
            profilePanel.add(scrollPane, gbc);
            gbc.gridy++;
        }
        
        // Relationship Goals section
        addSectionHeader(profilePanel, "Relationship Goals", gbc);
        gbc.gridy++;
        
        JLabel relationshipGoal = new JLabel(profile.getRelationshipGoal());
        relationshipGoal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        relationshipGoal.setBorder(new EmptyBorder(10, 10, 10, 10));
        profilePanel.add(relationshipGoal, gbc);
        gbc.gridy++;
        
        // About Me section
        addSectionHeader(profilePanel, "About Me", gbc);
        gbc.gridy++;
        
        JTextArea aboutMe = new JTextArea(profile.getAboutMe());
        aboutMe.setWrapStyleWord(true);
        aboutMe.setLineWrap(true);
        aboutMe.setOpaque(false);
        aboutMe.setEditable(false);
        aboutMe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        aboutMe.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(aboutMe);
        scrollPane.setPreferredSize(new Dimension(400, 100));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        profilePanel.add(scrollPane, gbc);
        gbc.gridy++;
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton editButton = createProfileButton("Edit Profile");
        editButton.addActionListener(e -> openProfileEditor());
        
        JButton photoButton = createProfileButton("Change Photo");
        photoButton.addActionListener(e -> openPhotoUploader());
        
        buttonPanel.add(editButton);
        buttonPanel.add(photoButton);
        
        gbc.gridy++;
        profilePanel.add(buttonPanel, gbc);
        
        // Add profile panel to card panel
        cardPanel.removeAll();
        cardPanel.add(profilePanel, BorderLayout.CENTER);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private int calculateAge(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            return 0;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}