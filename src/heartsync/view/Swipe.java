/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package heartsync.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import heartsync.controller.UserProfileController;
import heartsync.model.UserProfile;
import java.awt.image.BufferedImage;
import heartsync.database.DatabaseManagerProfile;

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
    private RoundedButton nextButton;
    private RoundedButton backButton;
    private RoundedButton likeButton;
    private RoundedButton rejectButton;
    private ArrayList<ProfileData> profiles;
    private int currentIndex;
    private JLabel profileLabel;
    private JLabel exploreLabel;
    private JLabel chatLabel;
    private JTextField searchField;
    private JButton filterButton;
    private CardLayout cardLayout;
    private JPanel contentCards;
    
    private static class ProfileData {
        String name;
        int age;
        String bio;
        List<String> photos;  // List to store multiple photos
        int currentPhotoIndex = 0;  // Track current photo index

        ProfileData(String name, int age, String bio, List<String> photos) {
            this.name = name;
            this.age = age;
            this.bio = bio;
            this.photos = photos;
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
        setTitle("HeartSync - Find Love");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 1000);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Initialize components
        imageLabel = new JLabel();
        nameLabel = new JLabel();
        ageLabel = new JLabel();
        bioLabel = new JLabel();
        profiles = new ArrayList<>();
        
        // Initialize buttons
        nextButton = new RoundedButton("Next →", NAV_ACTIVE_COLOR);
        backButton = new RoundedButton("← Back", NAV_ACTIVE_COLOR);
        likeButton = new RoundedButton("♥ Like", LIKE_COLOR);
        rejectButton = new RoundedButton("✕ Pass", REJECT_COLOR);
        
        // Main panel with padding
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(255, 235, 245)); // Slightly darker pink background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Navigation panel at top
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        navPanel.setOpaque(false);
        
        // Create navigation labels
        exploreLabel = new JLabel("Explore");
        chatLabel = new JLabel("Chat");
        profileLabel = new JLabel("My Profile");
        
        // Style navigation labels
        Font navFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color navTextColor = new Color(50, 50, 50);
        
        for (JLabel label : new JLabel[]{exploreLabel, chatLabel, profileLabel}) {
            label.setFont(navFont);
            label.setForeground(navTextColor);
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            navPanel.add(label);
        }
        
        // Add underline to Explore by default
        exploreLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.PINK));
        
        // Add click listeners
        exploreLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exploreLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.PINK));
                chatLabel.setBorder(null);
                profileLabel.setBorder(null);
                showExplore();
            }
        });
        
        chatLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chatLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.PINK));
                exploreLabel.setBorder(null);
                profileLabel.setBorder(null);
                // TODO: Implement chat view
                JOptionPane.showMessageDialog(Swipe.this,
                    "Chat functionality coming soon!",
                    "Coming Soon",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        profileLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                profileLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.PINK));
                exploreLabel.setBorder(null);
                chatLabel.setBorder(null);
                showProfile();
            }
        });
        
        mainPanel.add(navPanel, BorderLayout.NORTH);

        // Content cards for different views
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setOpaque(false);
        
        // Create search panel for explore view only
        JPanel exploreContent = new JPanel(new BorderLayout(10, 10));
        exploreContent.setOpaque(false);
        
        // Search panel
        searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 50));
        
        // Search field with custom styling
        searchField = createSearchField();
        
        // Filters button with better visibility
        filterButton = createFiltersButton();
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(filterButton, BorderLayout.EAST);
        
        exploreContent.add(searchPanel, BorderLayout.NORTH);
        exploreContent.add(createExplorePanel(), BorderLayout.CENTER);
        
        contentCards.add(exploreContent, "explore");
        contentCards.add(createProfilePanel(), "profile");
        
        mainPanel.add(contentCards, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        showExplore(); // Show explore by default
    }

    private void setupUI() {
        // Top panel for navigation and search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Setup navigation
        setupNavigationPanel();
        topPanel.add(navigationPanel, BorderLayout.NORTH);

        // Setup search
        setupSearchPanel();
        topPanel.add(searchPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Setup content panels
        JPanel explorePanel = createExplorePanel();
        JPanel profilePanel = createProfilePanel();
        
        contentCards.add(explorePanel, "explore");
        contentCards.add(profilePanel, "profile");
        mainPanel.add(contentCards, BorderLayout.CENTER);
    }
    
    private void setupNavigationPanel() {
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        navigationPanel.setOpaque(false);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Create navigation items
        exploreLabel = new JLabel("Explore");
        profileLabel = new JLabel("My Profile");
        
        // Style navigation items
        Font navFont = new Font("Segoe UI", Font.BOLD, 16);
        
        for (JLabel label : new JLabel[]{exploreLabel, profileLabel}) {
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
                    }
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    if ((label == exploreLabel && !exploreLabel.getForeground().equals(NAV_ACTIVE_COLOR)) ||
                        (label == profileLabel && !profileLabel.getForeground().equals(NAV_ACTIVE_COLOR))) {
                        label.setForeground(NAV_ACTIVE_COLOR.darker());
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    if ((label == exploreLabel && !exploreLabel.getForeground().equals(NAV_ACTIVE_COLOR)) ||
                        (label == profileLabel && !profileLabel.getForeground().equals(NAV_ACTIVE_COLOR))) {
                        label.setForeground(NAV_COLOR);
                    }
                }
            });
        }
        
        navigationPanel.add(exploreLabel);
        navigationPanel.add(profileLabel);
    }

    private void setupSearchPanel() {
        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Search field with icon
        JPanel searchFieldPanel = new JPanel(new BorderLayout());
        searchFieldPanel.setPreferredSize(new Dimension(500, 40));
        searchFieldPanel.setBackground(SEARCH_BG);
        searchFieldPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NAV_COLOR.brighter(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchFieldPanel.add(searchIcon, BorderLayout.WEST);

        searchField = new JTextField();
        searchField.setBorder(null);
        searchField.setBackground(SEARCH_BG);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(400, 30));
        searchFieldPanel.add(searchField, BorderLayout.CENTER);

        // Filter button
        filterButton = new JButton("Filters");
        filterButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterButton.setPreferredSize(new Dimension(100, 40));
        filterButton.setBackground(NAV_ACTIVE_COLOR);
        filterButton.setForeground(Color.WHITE);
        filterButton.setBorderPainted(false);
        filterButton.setFocusPainted(false);
        filterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterButton.addActionListener(e -> showFilterDialog());

        searchPanel.add(searchFieldPanel);
        searchPanel.add(filterButton);
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
        JPanel explorePanel = new JPanel(new BorderLayout(20, 20));
        explorePanel.setOpaque(false);
        explorePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Main card panel
        cardPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2.setColor(CARD_SHADOW);
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, CARD_RADIUS, CARD_RADIUS);
                
                // Draw card background
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, CARD_RADIUS, CARD_RADIUS);
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the photo container with gradient overlay
        JPanel photoContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                
                // Create gradient overlay at the bottom
                GradientPaint gradient = new GradientPaint(
                    0, getHeight() - 100, new Color(0, 0, 0, 0),
                    0, getHeight(), new Color(0, 0, 0, 100)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, getHeight() - 100, getWidth(), 100);
            }
        };
        photoContainer.setOpaque(false);
        photoContainer.setBorder(BorderFactory.createLineBorder(new Color(255, 192, 203, 50), 1));

        // Configure image label with padding
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        photoContainer.add(imageLabel, BorderLayout.CENTER);

        // Style the navigation arrows
        JLabel leftArrow = createPhotoNavArrow("←");
        JLabel rightArrow = createPhotoNavArrow("→");
        
        // Add hover effect and click handlers to arrows
        for (JLabel arrow : new JLabel[]{leftArrow, rightArrow}) {
            arrow.setForeground(new Color(255, 255, 255, 200));
            arrow.setFont(new Font("Segoe UI", Font.BOLD, 32));
            arrow.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            arrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        leftArrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPreviousPhoto();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                leftArrow.setForeground(new Color(255, 255, 255, 200));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                leftArrow.setForeground(Color.WHITE);
            }
        });
        
        rightArrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNextPhoto();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                rightArrow.setForeground(new Color(255, 255, 255, 200));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                rightArrow.setForeground(Color.WHITE);
            }
        });

        // Center panel for photo and info
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        // Add navigation arrows with proper layering
        JPanel arrowsPanel = new JPanel(new BorderLayout());
        arrowsPanel.setOpaque(false);
        arrowsPanel.add(leftArrow, BorderLayout.WEST);
        arrowsPanel.add(rightArrow, BorderLayout.EAST);
        
        // Layer the components
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(CARD_WIDTH - 20, PHOTO_HEIGHT));
        
        photoContainer.setBounds(0, 0, CARD_WIDTH - 20, PHOTO_HEIGHT);
        arrowsPanel.setBounds(0, 0, CARD_WIDTH - 20, PHOTO_HEIGHT);
        
        layeredPane.add(photoContainer, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(arrowsPanel, JLayeredPane.PALETTE_LAYER);
        
        centerPanel.add(layeredPane, BorderLayout.CENTER);

        // Profile info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        
        // Style labels
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(NAV_COLOR);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        ageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        ageLabel.setForeground(NAV_COLOR.brighter());
        ageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        bioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bioLabel.setForeground(NAV_COLOR);
        bioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components with spacing
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(ageLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(bioLabel);

        // Action buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        // Create modern action buttons
        likeButton = createActionButton("♥", "Like", LIKE_COLOR);
        rejectButton = createActionButton("✕", "Pass", REJECT_COLOR);
        
        likeButton.addActionListener(e -> likeCurrentProfile());
        rejectButton.addActionListener(e -> rejectCurrentProfile());
        
        buttonPanel.add(rejectButton);
        buttonPanel.add(likeButton);

        // Bottom panel for info and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 5));
        bottomPanel.setOpaque(false);
        bottomPanel.add(infoPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add all components to the card
        cardPanel.add(centerPanel, BorderLayout.CENTER);
        cardPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Center the card in the explore panel
        JPanel cardContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cardContainer.setOpaque(false);
        cardContainer.add(cardPanel);
        
        explorePanel.add(cardContainer, BorderLayout.CENTER);
        
        // Load initial profile
        setupProfiles();
        showCurrentProfile();
        
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
            imageLabel.setText("");
            return;
        }
        String photoPath = profile.photos.get(profile.currentPhotoIndex);
        try {
            Image image;
            if (photoPath.startsWith("/")) {
                // Try as file path
                File file = new File(photoPath);
                if (file.exists()) {
                    image = ImageIO.read(file);
                } else {
                    image = null;
                }
            } else {
                // Try as resource
                ImageIcon imageIcon = new ImageIcon(getClass().getResource(photoPath));
                image = imageIcon.getImage();
            }
            if (image != null) {
                int originalWidth = image.getWidth(null);
                int originalHeight = image.getHeight(null);
                double widthScale = (double) (CARD_WIDTH - 40) / originalWidth;
                double heightScale = (double) (PHOTO_HEIGHT - 10) / originalHeight;
                double scale = Math.max(widthScale, heightScale);
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
            nameLabel.setText("");
            ageLabel.setText("");
            bioLabel.setText("<html><div style='text-align:center;width:300px;'>No profiles to explore yet!</div></html>");
            imageLabel.setIcon(null);
            imageLabel.setText("");
            return;
        }
        if (currentIndex >= 0 && currentIndex < profiles.size()) {
            ProfileData profile = profiles.get(currentIndex);
            nameLabel.setText(profile.name);
            ageLabel.setText(profile.age + " years");
            // Center-align the bio text visually and horizontally
            String htmlBio = "<html><div style='text-align:center;display:flex;flex-direction:column;justify-content:center;align-items:center;height:100%;width:" + (CARD_WIDTH - 80) + "px;'>"
                + profile.bio.replace(". ", ".<br><br>") + "</div></html>";
            bioLabel.setText(htmlBio);
            bioLabel.setHorizontalAlignment(SwingConstants.CENTER);
            updateProfilePhoto();
        }
    }

    private void showNextProfile() {
        if (profiles.isEmpty()) return;
        if (profiles.size() == 1) {
            showCurrentProfile();
            return;
        }
        if (currentIndex < profiles.size() - 1) {
            currentIndex++;
        } else {
            currentIndex = 0; // Loop to first
        }
        profiles.get(currentIndex).currentPhotoIndex = 0;
        showCurrentProfile();
    }

    private void showPreviousProfile() {
        if (profiles.isEmpty()) return;
        if (profiles.size() == 1) {
            showCurrentProfile();
            return;
        }
        if (currentIndex > 0) {
            currentIndex--;
        } else {
            currentIndex = profiles.size() - 1; // Loop to last
        }
        profiles.get(currentIndex).currentPhotoIndex = 0;
        showCurrentProfile();
    }

    private void likeCurrentProfile() {
        if (profiles.isEmpty()) return;
        ProfileData profile = profiles.get(currentIndex);
        JOptionPane.showMessageDialog(this,
            "You liked " + profile.name + "!\nMatch functionality coming soon.",
            "Liked",
            JOptionPane.INFORMATION_MESSAGE);
        showNextProfile();
    }

    private void rejectCurrentProfile() {
        if (profiles.isEmpty()) return;
        showNextProfile();
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
                String profilePicPath = UserProfile.getCurrentUser().getProfilePicPath();
                if (profilePicPath != null && !profilePicPath.isEmpty()) {
                    try {
                        BufferedImage img = ImageIO.read(new File(profilePicPath));
                        if (img != null) {
                            g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
                        }
                    } catch (IOException e) {
                        g2.setColor(new Color(219, 112, 147));
                        g2.fill(circle);
                    }
                } else {
                    g2.setColor(new Color(219, 112, 147));
                    g2.fill(circle);
                }
                // No border
            }
        };

        profilePicture.setPreferredSize(new Dimension(150, 150));
        profilePicture.setMinimumSize(new Dimension(150, 150));
        profilePicture.setMaximumSize(new Dimension(150, 150));
        profilePicture.setAlignmentX(Component.CENTER_ALIGNMENT);

        return profilePicture;
    }

    private JPanel createProfilePanel() {
        JPanel profilePanel = new JPanel(new BorderLayout(20, 20));
        profilePanel.setOpaque(false);
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a card-like panel for profile content
        JPanel contentCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Draw shadow
                g2.setColor(CARD_SHADOW);
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, CARD_RADIUS, CARD_RADIUS);
                // Draw card background
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, CARD_RADIUS, CARD_RADIUS);
            }
        };
        contentCard.setOpaque(false);
        contentCard.setLayout(new BorderLayout(0, 20));
        contentCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top section with user info
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);

        // Profile picture (use actual image if available)
        JLabel profilePicture = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1);
                g2.setClip(circle);
                String profilePicPath = UserProfile.getCurrentUser().getProfilePicPath();
                if (profilePicPath != null && !profilePicPath.isEmpty()) {
                    try {
                        BufferedImage img = ImageIO.read(new File(profilePicPath));
                        if (img != null) {
                            g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
                        }
                    } catch (IOException e) {
                        g2.setColor(new Color(219, 112, 147));
                        g2.fill(circle);
                    }
                } else {
                    g2.setColor(new Color(219, 112, 147));
                    g2.fill(circle);
                }
                // No border
            }
        };
        profilePicture.setPreferredSize(new Dimension(150, 150));
        profilePicture.setMaximumSize(new Dimension(150, 150));
        profilePicture.setAlignmentX(Component.CENTER_ALIGNMENT);

        // User details
        JLabel nameLabel = new JLabel(UserProfile.getCurrentUser().getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Calculate age from date of birth
        String dob = UserProfile.getCurrentUser().getDateOfBirth();
        int age = calculateAge(dob);
        JLabel ageLabel = new JLabel(age + " years");
        ageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        ageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Add components to user info panel
        userInfoPanel.add(profilePicture);
        userInfoPanel.add(Box.createVerticalStrut(15));
        userInfoPanel.add(nameLabel);
        userInfoPanel.add(Box.createVerticalStrut(5));
        userInfoPanel.add(ageLabel);
        userInfoPanel.add(Box.createVerticalStrut(20));

        // Create details panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 0);

        // Basic Information
        addSectionHeader(detailsPanel, "Basic Information", gbc);
        addDetailRow(detailsPanel, "Height", UserProfile.getCurrentUser().getHeight() + " cm", gbc);
        addDetailRow(detailsPanel, "Weight", UserProfile.getCurrentUser().getWeight() + " kg", gbc);
        addDetailRow(detailsPanel, "Gender", UserProfile.getCurrentUser().getGender(), gbc);
        addDetailRow(detailsPanel, "Interested In", UserProfile.getCurrentUser().getPreferences(), gbc);
        
        // Location Information
        addSectionHeader(detailsPanel, "Location", gbc);
        addDetailRow(detailsPanel, "Country", UserProfile.getCurrentUser().getCountry(), gbc);
        addDetailRow(detailsPanel, "Address", UserProfile.getCurrentUser().getAddress(), gbc);
        
        // Contact Information
        addSectionHeader(detailsPanel, "Contact", gbc);
        addDetailRow(detailsPanel, "Phone", UserProfile.getCurrentUser().getPhoneNumber(), gbc);
        addDetailRow(detailsPanel, "Email", UserProfile.getCurrentUser().getEmail(), gbc);
        
        // Background Information
        addSectionHeader(detailsPanel, "Background", gbc);
        addDetailRow(detailsPanel, "Education", UserProfile.getCurrentUser().getQualification(), gbc);
        addDetailRow(detailsPanel, "Occupation", UserProfile.getCurrentUser().getOccupation(), gbc);
        addDetailRow(detailsPanel, "Religion", UserProfile.getCurrentUser().getReligion(), gbc);
        addDetailRow(detailsPanel, "Ethnicity", UserProfile.getCurrentUser().getEthnicity(), gbc);
        
        // Languages and Interests
        addSectionHeader(detailsPanel, "Languages & Interests", gbc);
        addDetailRow(detailsPanel, "Languages", String.join(", ", UserProfile.getCurrentUser().getLanguages()), gbc);
        addDetailRow(detailsPanel, "Hobbies", UserProfile.getCurrentUser().getInterests(), gbc);
        
        // Relationship Goals
        addSectionHeader(detailsPanel, "Looking For", gbc);
        addDetailRow(detailsPanel, "Relationship Goal", UserProfile.getCurrentUser().getRelationshipGoal(), gbc);
        
        // About Me
        addSectionHeader(detailsPanel, "About Me", gbc);
        addDetailRow(detailsPanel, "", UserProfile.getCurrentUser().getAboutMe(), gbc);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton editProfileBtn = createProfileButton("✎ Edit Profile");
        editProfileBtn.addActionListener(e -> {
            UserProfileController controller = new UserProfileController(UserProfile.getCurrentUser(), heartsync.model.User.getCurrentUser().getUsername());
            ProfileSetupView profileSetup = new ProfileSetupView(controller);
            profileSetup.setLocationRelativeTo(this);
            profileSetup.setVisible(true);
            profileSetup.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    UserProfile.setCurrentUser(null);
                    contentCards.remove(1);
                    contentCards.add(createProfilePanel(), "profile");
                    showProfile();
                }
            });
        });
        JButton viewProfileBtn = createProfileButton("👁 View Profile");
        JButton postPicturesBtn = createProfileButton("📷 Post Pictures");

        buttonPanel.add(postPicturesBtn);
        buttonPanel.add(editProfileBtn);
        buttonPanel.add(viewProfileBtn);

        // Add all sections to the content card
        contentCard.add(userInfoPanel, BorderLayout.NORTH);
        contentCard.add(new JScrollPane(detailsPanel) {
            {
                setBorder(null);
                setOpaque(false);
                getViewport().setOpaque(false);
            }
        }, BorderLayout.CENTER);
        contentCard.add(buttonPanel, BorderLayout.SOUTH);

        profilePanel.add(contentCard, BorderLayout.CENTER);
        return profilePanel;
    }

    private void addSectionHeader(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel header = new JLabel(text);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setForeground(new Color(103, 58, 183));
        gbc.insets = new Insets(15, 0, 5, 0);
        panel.add(header, gbc);
        gbc.insets = new Insets(5, 0, 5, 0);
    }

    private int calculateAge(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            return 0;
        }
        try {
            String[] parts = dateOfBirth.split("-");
            if (parts.length != 3) return 0;
            
            int birthYear = Integer.parseInt(parts[0]);
            int currentYear = java.time.Year.now().getValue();
            return currentYear - birthYear;
        } catch (Exception e) {
            return 0;
        }
    }

    private void addDetailRow(JPanel panel, String label, String value, GridBagConstraints gbc) {
        JLabel labelComponent = new JLabel(label + ":");
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(labelComponent, gbc);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(valueComponent, gbc);
    }

    private JButton createProfileButton(String text) {
        JButton button = new JButton(text) {
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
        
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));
        
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
        exploreLabel.setForeground(NAV_ACTIVE_COLOR);
        exploreLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, NAV_ACTIVE_COLOR));
        chatLabel.setForeground(NAV_COLOR);
        chatLabel.setBorder(null);
        profileLabel.setForeground(NAV_COLOR);
        profileLabel.setBorder(null);
        cardLayout.show(contentCards, "explore");
    }

    public void showProfile() {
        profileLabel.setForeground(NAV_ACTIVE_COLOR);
        profileLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, NAV_ACTIVE_COLOR));
        exploreLabel.setForeground(NAV_COLOR);
        exploreLabel.setBorder(null);
        chatLabel.setForeground(NAV_COLOR);
        chatLabel.setBorder(null);
        cardLayout.show(contentCards, "profile");
    }
    
    private void setupProfiles() {
        profiles = new ArrayList<>();
        try {
            String currentUsername = heartsync.model.User.getCurrentUser().getUsername();
            List<UserProfile> otherProfiles = heartsync.database.DatabaseManagerProfile.getInstance().getAllUserProfilesExcept(currentUsername);
            for (UserProfile userProfile : otherProfiles) {
                String name = userProfile.getFullName() != null ? userProfile.getFullName() : "Unknown";
                int age = userProfile.getAge() > 0 ? userProfile.getAge() : calculateAge(userProfile.getDateOfBirth());
                String bio = userProfile.getAboutMe() != null ? userProfile.getAboutMe() : "";
                List<String> photos = new ArrayList<>();
                if (userProfile.getProfilePicPath() != null && !userProfile.getProfilePicPath().isEmpty()) {
                    photos.add(userProfile.getProfilePicPath());
                }
                profiles.add(new ProfileData(name, age, bio, photos));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentIndex = 0;
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
}