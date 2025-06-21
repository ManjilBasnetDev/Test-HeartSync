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
import java.awt.GridLayout;
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
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.SwingWorker;

import heartsync.controller.UserProfileController;
import heartsync.dao.LikeDAO;
import heartsync.database.DatabaseManagerProfile;
import heartsync.database.FirebaseConfig;
import heartsync.model.User;
import heartsync.model.UserProfile;
import heartsync.navigation.WindowManager;
import heartsync.view.ChatSystem;

import com.google.gson.reflect.TypeToken;

/**
 * Modern swipe interface for browsing through potential matches.
 * @author manjil-basnet
 */
public class Swipe extends JFrame {
    private static final int WINDOW_RADIUS = 20;
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250); // Modern light gray background
    private static final Color BUTTON_COLOR = new Color(220, 53, 69); // Modern red for action buttons
    private static final Color LIKE_COLOR = new Color(40, 167, 69); // Modern green
    private static final Color REJECT_COLOR = new Color(220, 53, 69); // Modern red
    private static final Color BUTTON_HOVER_OVERLAY = new Color(0, 0, 0, 40);
    private static final Color NAV_COLOR = new Color(73, 80, 87); // Modern dark gray for nav items
    private static final Color NAV_ACTIVE_COLOR = new Color(220, 53, 69); // Modern red for active nav
    private static final Color WINDOW_CONTROL_COLOR = new Color(51, 51, 51); // Dark gray for window controls
    private static final Color TITLE_BAR_COLOR = new Color(33, 33, 33); // Dark title bar color
    private static final Color SEARCH_BG = new Color(255, 255, 255); // White for search
    private static final Color CARD_BG = Color.WHITE; // White for card
    private static final Color BUTTON_TEXT = new Color(255, 255, 255); // White text for buttons
    private static final int CARD_RADIUS = 20; // Increased rounded corners for card
    private static final int IMAGE_CONTAINER_WIDTH = 450;  // Fixed width for image container
    private static final int IMAGE_CONTAINER_HEIGHT = 600; // Fixed height for image container
    private static final int CARD_WIDTH = 420;  // Slightly wider card
    private static final int CARD_HEIGHT = 700; // Reduced height to prevent overflow
    private static final int PHOTO_HEIGHT = 420; // Reduced height for better containment
    private static final Color CARD_SHADOW = new Color(0, 0, 0, 20); // Lighter shadow
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 60);
    private static final Color FILTER_BUTTON_COLOR = new Color(108, 117, 125); // Modern gray
    private static final Color SEARCH_ICON_COLOR = new Color(108, 117, 125); // Modern gray
    private static final Color SEARCH_BORDER_COLOR = new Color(222, 226, 230); // Light gray border
    
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
    private JLabel matchedLabel;
    private JLabel myLikesLabel;
    private JLabel myLikersLabel;
    private JLabel logoutLabel;
    private java.util.List<JLabel> allNavLabels;
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
        private static final int RADIUS = 20; // Smaller radius for better fit

        public RoundedButton(String text, Color color) {
            super(text);
            this.baseColor = color;
            setupButton();
            setPreferredSize(new Dimension(70, 70)); // Fixed size to prevent overflow
            setMaximumSize(new Dimension(70, 70));
            setMinimumSize(new Dimension(70, 70));
        }

        private void setupButton() {
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
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
            
            // Draw background with proper sizing
            if (isHovered) {
                g2.setColor(baseColor.darker());
            } else {
                g2.setColor(baseColor);
            }
            
            // Ensure we don't exceed bounds
            int width = Math.min(getWidth(), 70);
            int height = Math.min(getHeight(), 70);
            
            g2.fillRoundRect(0, 0, width - 1, height - 1, RADIUS, RADIUS);
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
        setUndecorated(false);
        
        // Initialize components
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Initialize card layout and content cards
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setOpaque(false);
        
        // Initialize labels with proper configuration
        nameLabel = new JLabel();
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        ageLabel = new JLabel();
        ageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        bioLabel = new JLabel();
        bioLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        locationLabel = new JLabel();
        locationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        interestsLabel = new JLabel();
        interestsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Initialize imageLabel with proper configuration
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(IMAGE_CONTAINER_WIDTH, IMAGE_CONTAINER_HEIGHT));
        
        // Initialize buttons
        backButton = new RoundedButton("Back", BUTTON_COLOR);
        likeButton = new RoundedButton("Like", LIKE_COLOR);
        rejectButton = new RoundedButton("Pass", REJECT_COLOR);
        nextButton = new RoundedButton("Next", BUTTON_COLOR);
        
        // Add button listeners
        backButton.addActionListener(e -> showPreviousProfile());
        likeButton.addActionListener(e -> likeCurrentProfile());
        rejectButton.addActionListener(e -> rejectCurrentProfile());
        nextButton.addActionListener(e -> showNextProfile());
        
        // Initialize lists
        profiles = new ArrayList<>();
        allProfiles = new ArrayList<>();
        currentIndex = 0;
        
        // Initialize navigation panel
        setupNavigationPanel();
        
        // Initialize search panel
        setupSearchPanel();
        
        // Set up UI components
        setupUI();
        
        // Add main panel to frame
        add(mainPanel);
        
        // Initialize profiles
        setupProfiles();
        
        // Set up search logic
        setupSearchLogic();
        
        // Show explore panel by default
        showExplore();
        
        // Show first profile if available
        if (!profiles.isEmpty()) {
            showCurrentProfile();
        }
    }

    private void setupUI() {
        // Top panel for navigation and search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Add navigation and search panels
        topPanel.add(navigationPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Create explore panel, profile panel, and other views
        JPanel explorePanel = createExplorePanel();
        JPanel profilePanel = createProfilePanel();

        contentCards.add(explorePanel, "EXPLORE");
        contentCards.add(profilePanel, "PROFILE");
        // User list views will be added dynamically when clicked

        mainPanel.add(contentCards, BorderLayout.CENTER);
    }

    private void setupNavigationPanel() {
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        navigationPanel.setOpaque(false);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Create navigation items
        exploreLabel = new JLabel("Explore");
        profileLabel = new JLabel("My Profile");
        chatLabel = new JLabel("Chat");
        matchedLabel = new JLabel("Matched Users");
        myLikesLabel = new JLabel("My Likes");
        myLikersLabel = new JLabel("My Likers");
        logoutLabel = new JLabel("Logout");

        allNavLabels = java.util.Arrays.asList(exploreLabel, profileLabel, chatLabel, matchedLabel, myLikesLabel, myLikersLabel, logoutLabel);

        Font navFont = new Font("Segoe UI", Font.BOLD, 16);

        for (JLabel label : allNavLabels) {
            label.setFont(navFont);
            label.setForeground(NAV_COLOR);
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setActiveNav(label); // Set active style

                    if (label == exploreLabel) {
                        cardLayout.show(contentCards, "EXPLORE");
                    } else if (label == profileLabel) {
                        cardLayout.show(contentCards, "PROFILE");
                    } else if (label == chatLabel) {
                        WindowManager.show(ChatSystem.class, ChatSystem::new, Swipe.this);
                    } else if (label == matchedLabel) {
                        List<String> matchedIds = likeDAO.getMatches(currentUserId);
                        UserCardListView matchedView = new UserCardListView(matchedIds, "Matched Users", currentUserId);
                        contentCards.add(matchedView, "MATCHED");
                        cardLayout.show(contentCards, "MATCHED");
                    } else if (label == myLikesLabel) {
                        List<String> likedIds = likeDAO.getLikedUsers(currentUserId);
                        UserCardListView likesView = new UserCardListView(likedIds, "My Likes", currentUserId);
                        contentCards.add(likesView, "LIKES");
                        cardLayout.show(contentCards, "LIKES");
                    } else if (label == myLikersLabel) {
                        List<String> likerIds = likeDAO.getLikersOfUser(currentUserId);
                        UserCardListView likersView = new UserCardListView(likerIds, "My Likers", currentUserId);
                        contentCards.add(likersView, "LIKERS");
                        cardLayout.show(contentCards, "LIKERS");
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
                            heartsync.view.HomePage.showHomePage();
                        }
                    }
                    contentCards.revalidate();
                    contentCards.repaint();
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
        for (JLabel label : allNavLabels) {
            navigationPanel.add(label);
        }
    }

    private void setupSearchPanel() {
        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Modern search field with rounded corners
        JPanel searchFieldPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded rectangle background
                g2.setColor(SEARCH_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Draw subtle border
                g2.setColor(SEARCH_BORDER_COLOR);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
            }
        };
        searchFieldPanel.setOpaque(false);
        searchFieldPanel.setPreferredSize(new Dimension(500, 45));

        // Search icon with better styling
        JLabel searchIcon = new JLabel("🔍") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SEARCH_ICON_COLOR);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                FontMetrics fm = g2.getFontMetrics();
                int x = 15;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2.drawString("🔍", x, y);
            }
        };
        searchIcon.setPreferredSize(new Dimension(50, 45));
        searchIcon.setOpaque(false);
        searchFieldPanel.add(searchIcon, BorderLayout.WEST);

        // Modern search field
        searchField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(SEARCH_ICON_COLOR);
                    g2.setFont(getFont().deriveFont(Font.PLAIN));
                    g2.drawString("Search profiles...", 5, getHeight() / 2 + 5);
                }
            }
        };
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchField.setOpaque(false);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(NAV_COLOR);
        searchField.setPreferredSize(new Dimension(400, 35));
        searchFieldPanel.add(searchField, BorderLayout.CENTER);

        // Modern filter button with better visibility
        filterButton = new JButton("Filters") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded rectangle background
                if (getModel().isPressed()) {
                    g2.setColor(FILTER_BUTTON_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(FILTER_BUTTON_COLOR.brighter());
                } else {
                    g2.setColor(FILTER_BUTTON_COLOR);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Draw text
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        filterButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterButton.setForeground(Color.WHITE);
        filterButton.setBorderPainted(false);
        filterButton.setContentAreaFilled(false);
        filterButton.setFocusPainted(false);
        filterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterButton.setPreferredSize(new Dimension(100, 45));
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
        // Use the existing imageLabel, don't create a new one
        if (imageLabel != null) {
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(IMAGE_CONTAINER_WIDTH, IMAGE_CONTAINER_HEIGHT));
        }

        JPanel explorePanel = new JPanel(new BorderLayout(20, 20));
        explorePanel.setOpaque(false);
        explorePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Main card panel with improved shadow
        cardPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow with multiple layers for depth
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(8, 8, getWidth() - 16, getHeight() - 16, CARD_RADIUS, CARD_RADIUS);
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, CARD_RADIUS, CARD_RADIUS);
                g2.setColor(CARD_SHADOW);
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, CARD_RADIUS, CARD_RADIUS);
                
                // Draw card background
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, CARD_RADIUS, CARD_RADIUS);
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create the photo container with improved gradient overlay
        JPanel photoContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient overlay at the bottom for better text readability
                GradientPaint gradient = new GradientPaint(
                    0, getHeight() - 120, new Color(0, 0, 0, 0),
                    0, getHeight(), new Color(0, 0, 0, 120)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, getHeight() - 120, getWidth(), 120);
            }
        };
        photoContainer.setOpaque(false);
        photoContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Configure image label with better padding
        imageLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        photoContainer.add(imageLabel, BorderLayout.CENTER);

        // Style the navigation arrows with better visibility
        JLabel leftArrow = createPhotoNavArrow("‹");
        JLabel rightArrow = createPhotoNavArrow("›");
        
        // Add hover effect and click handlers to arrows
        for (JLabel arrow : new JLabel[]{leftArrow, rightArrow}) {
            arrow.setForeground(new Color(255, 255, 255, 180));
            arrow.setFont(new Font("Segoe UI", Font.BOLD, 36));
            arrow.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
            arrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        leftArrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPreviousPhoto();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                leftArrow.setForeground(new Color(255, 255, 255, 255));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                leftArrow.setForeground(new Color(255, 255, 255, 180));
            }
        });
        
        rightArrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNextPhoto();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                rightArrow.setForeground(new Color(255, 255, 255, 255));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                rightArrow.setForeground(new Color(255, 255, 255, 180));
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
        layeredPane.setPreferredSize(new Dimension(CARD_WIDTH - 30, PHOTO_HEIGHT));
        
        photoContainer.setBounds(0, 0, CARD_WIDTH - 30, PHOTO_HEIGHT);
        arrowsPanel.setBounds(0, 0, CARD_WIDTH - 30, PHOTO_HEIGHT);
        
        layeredPane.add(photoContainer, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(arrowsPanel, JLayeredPane.PALETTE_LAYER);
        
        centerPanel.add(layeredPane, BorderLayout.CENTER);

        // Profile info panel with better spacing
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 15, 15));
        
        // Style labels with modern typography
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        nameLabel.setForeground(NAV_COLOR);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        ageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        ageLabel.setForeground(NAV_COLOR.brighter());
        ageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        bioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bioLabel.setForeground(NAV_COLOR);
        bioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components with better spacing
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(ageLabel);
        infoPanel.add(Box.createVerticalStrut(12));
        infoPanel.add(bioLabel);

        // Action buttons panel with fixed positioning
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.setMaximumSize(new Dimension(CARD_WIDTH - 30, 80)); // Fixed height to prevent overflow
        
        // Create modern action buttons with better sizing
        likeButton = createActionButton("♥", "Like", LIKE_COLOR);
        rejectButton = createActionButton("✕", "Pass", REJECT_COLOR);
        
        likeButton.addActionListener(e -> likeCurrentProfile());
        rejectButton.addActionListener(e -> rejectCurrentProfile());
        
        buttonPanel.add(rejectButton);
        buttonPanel.add(likeButton);

        // Bottom panel for info and buttons with proper constraints
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
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
                
                // Draw circle background with better styling
                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }
                
                // Draw shadow for depth
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                
                // Draw main button
                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }
                g2.fillOval(0, 0, getWidth() - 2, getHeight() - 2);
                
                // Draw icon and text
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                
                // Draw icon
                String iconText = icon;
                int iconWidth = fm.stringWidth(iconText);
                int iconX = (getWidth() - iconWidth) / 2;
                int iconY = (getHeight() / 2) - 5;
                g2.drawString(iconText, iconX, iconY);
                
                // Draw text below icon
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textX = (getWidth() - textWidth) / 2;
                int textY = (getHeight() / 2) + 15;
                g2.drawString(getText(), textX, textY);
            }
        };
        
        // Set proper size to prevent overflow
        button.setPreferredSize(new Dimension(70, 70));
        button.setMaximumSize(new Dimension(70, 70));
        button.setMinimumSize(new Dimension(70, 70));
        
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
            
            // First try to load from Firebase Storage URL
            if (photoPath.startsWith("http") || photoPath.startsWith("https")) {
                try {
                    URL imageUrl = new URL(photoPath);
                    image = ImageIO.read(imageUrl);
                } catch (Exception e) {
                    System.out.println("Failed to load image from URL: " + photoPath);
                }
            }
            
            // Then try as absolute path
            if (image == null && photoPath.startsWith("/")) {
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
            
            // Try loading from Firebase Storage using the user ID
            if (image == null && profile.userId != null) {
                try {
                    String firebaseUrl = heartsync.database.FirebaseStorageManager.getProfileImageUrl(profile.userId);
                    if (firebaseUrl != null && !firebaseUrl.isEmpty()) {
                        URL imageUrl = new URL(firebaseUrl);
                        image = ImageIO.read(imageUrl);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to load image from Firebase for user: " + profile.userId);
                }
            }
            
            if (image != null) {
                int originalWidth = image.getWidth(null);
                int originalHeight = image.getHeight(null);
                
                // Calculate scaling to fit within the photo container
                double widthScale = (double) (CARD_WIDTH - 60) / originalWidth;
                double heightScale = (double) (PHOTO_HEIGHT - 20) / originalHeight;
                double scale = Math.min(widthScale, heightScale); // Maintain aspect ratio
                
                int scaledWidth = (int) (originalWidth * scale);
                int scaledHeight = (int) (originalHeight * scale);
                
                // Ensure minimum size
                if (scaledWidth < 100) scaledWidth = 100;
                if (scaledHeight < 100) scaledHeight = 100;
                
                Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
                imageLabel.setText("");
            } else {
                // Set a default placeholder image
                imageLabel.setIcon(null);
                imageLabel.setText("📷");
                imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
                imageLabel.setForeground(SEARCH_ICON_COLOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageLabel.setIcon(null);
            imageLabel.setText("📷");
            imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
            imageLabel.setForeground(SEARCH_ICON_COLOR);
        }
    }

    private void showCurrentProfile() {
        if (profiles.isEmpty()) {
            nameLabel.setText("");
            ageLabel.setText("");
            // Improved empty state message with modern styling
            String icon = "<div style='font-size:48px;font-weight:bold;line-height:1;margin-bottom:15px;'>😔</div>";
            String msg = "<div style='font-size:16px;max-width:280px;word-break:break-word;margin-left:auto;margin-right:auto;color:#6c757d;'>No profiles found!<br><br>Try adjusting your search or filters to discover new people.</div>";
            String html = "<html><div style='text-align:center;display:flex;flex-direction:column;align-items:center;justify-content:center;height:100%;width:100%;'>"
                + icon + msg + "</div></html>";
            bioLabel.setText(html);
            bioLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setIcon(null);
            imageLabel.setText("📷");
            imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
            imageLabel.setForeground(SEARCH_ICON_COLOR);
            return;
        }
        if (currentIndex >= 0 && currentIndex < profiles.size()) {
            ProfileData profile = profiles.get(currentIndex);
            nameLabel.setText(profile.name);
            ageLabel.setText(profile.age + " years");
            // Center-align the bio text with better formatting
            String htmlBio = "<html><div style='text-align:center;width:" + (CARD_WIDTH - 80) + "px;line-height:1.4;'>"
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
        
        // Add to likes in Firebase
        if (likeDAO.addLike(currentUserId, profile.userId)) {
            // Show a confirmation popup
            JOptionPane.showMessageDialog(this,
                "You liked " + profile.name + "!",
                "Liked!",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Check if it's a match
            if (likeDAO.isMatched(currentUserId, profile.userId)) {
                // Show match notification
                JOptionPane.showMessageDialog(this,
                    "It's a Match with " + profile.name + "! 🎉\nYou can now chat with each other!",
                    "New Match!",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Remove from current list
            profiles.remove(currentIndex);

            if (profiles.isEmpty()) {
                profiles.addAll(allProfiles);
            }
            
            if (currentIndex >= profiles.size()) {
                currentIndex = 0;
            }
            
            showCurrentProfile();

        } else {
            JOptionPane.showMessageDialog(this,
                "Error saving like. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rejectCurrentProfile() {
        if (profiles.isEmpty()) return;
        ProfileData profile = profiles.get(currentIndex);
        
        // Add to passes in Firebase
        if (likeDAO.addPass(currentUserId, profile.userId)) {
            // Remove from current list
            profiles.remove(currentIndex);

            if (profiles.isEmpty()) {
                profiles.addAll(allProfiles);
            }
            
            if (currentIndex >= profiles.size()) {
                currentIndex = 0;
            }
            
            showCurrentProfile();
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
        JPanel scrollablePanel = new JPanel(new BorderLayout());
        scrollablePanel.setBackground(Color.WHITE);

        String currentUsername = heartsync.model.User.getCurrentUser().getUsername();
        UserProfile userProfile = DatabaseManagerProfile.getInstance().getUserProfile(currentUsername);

        if (userProfile == null) {
            scrollablePanel.add(new JLabel("Could not load your profile. Please try again later.", SwingConstants.CENTER));
            return scrollablePanel;
        }

        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();

        // --- Profile Header (Pic + Name + Location) ---
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setOpaque(false);

        JLabel profilePicLabel = new JLabel();
        profilePicLabel.setPreferredSize(new Dimension(150, 150));
        // SwingWorker to load image...
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                // ... same image loading logic as before ...
                 Image image = null;
                String picPath = userProfile.getProfilePicPath();
                if (picPath != null && !picPath.isEmpty()) {
                    if (picPath.startsWith("http")) {
                        image = ImageIO.read(new URL(picPath));
                    } else {
                        URL resourceUrl = getClass().getResource("/ImagePicker/" + picPath);
                        if (resourceUrl != null) image = ImageIO.read(resourceUrl);
                    }
                }
                if (image == null) image = ImageIO.read(getClass().getResource("/ImagePicker/RajeshHamalPhoto.png"));
                
                Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                BufferedImage circularImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circularImage.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new Ellipse2D.Float(0, 0, 150, 150));
                g2.drawImage(scaledImage, 0, 0, null);
                g2.dispose();
                return new ImageIcon(circularImage);
            }
            @Override
            protected void done() {
                try {
                    profilePicLabel.setIcon(get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
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
        container.add(headerPanel, gbc);

        // --- About Me Section ---
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Span across both columns
        gbc.insets = new Insets(40, 0, 0, 0);
        container.add(createDetailItem("About Me", userProfile.getAboutMe()), gbc);
        
        // --- Details Sections ---
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Reset gridwidth
        gbc.insets = new Insets(20, 0, 0, 0);
        JPanel detailsContainer = new JPanel(new GridLayout(0, 2, 40, 20));
        detailsContainer.setOpaque(false);
        detailsContainer.add(createDetailItem("Age", String.valueOf(userProfile.getAge())));
        detailsContainer.add(createDetailItem("Gender", userProfile.getGender()));
        detailsContainer.add(createDetailItem("Education", userProfile.getEducation()));
        detailsContainer.add(createDetailItem("Hobbies", String.join(", ", userProfile.getHobbies())));
        detailsContainer.add(createDetailItem("Relationship Goal", userProfile.getRelationshipGoal()));
        detailsContainer.add(createDetailItem("Phone Number", userProfile.getPhoneNumber()));
        container.add(detailsContainer, gbc);

        // --- Edit Button ---
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(50, 0, 0, 0);
        JButton editButton = createProfileButton("Edit Profile");
        editButton.addActionListener(e -> openProfileEditor());
        container.add(editButton, gbc);

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return scrollablePanel;
    }
    
    private JPanel createDetailItem(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 0)); // Use BorderLayout
        panel.setOpaque(false);

        JLabel labelComponent = new JLabel(label + ":"); // Add colon to label
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelComponent.setForeground(new Color(220, 53, 69));
        
        // Wrap label in a panel to align it to the top
        JPanel labelContainer = new JPanel(new BorderLayout());
        labelContainer.setOpaque(false);
        labelContainer.add(labelComponent, BorderLayout.NORTH);

        if ("Age".equals(label) && !value.endsWith(" years")) {
            value += " years";
        }

        JTextArea valueComponent = new JTextArea(value);
        valueComponent.setEditable(false);
        valueComponent.setOpaque(false);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Slightly larger font
        valueComponent.setLineWrap(true);
        valueComponent.setWrapStyleWord(true);
        valueComponent.setAlignmentX(Component.LEFT_ALIGNMENT); // Align text left

        panel.add(labelContainer, BorderLayout.WEST); // Add the container, not the label directly
        panel.add(valueComponent, BorderLayout.CENTER);
        
        // Special handling for "About Me" to be a bit taller
        if ("About Me".equals(label)) {
            panel.setPreferredSize(new Dimension(0, 70)); 
        } else {
             panel.setPreferredSize(new Dimension(0, 40)); 
        }

        return panel;
    }

    private void openProfileEditor() {
        String currentUsername = heartsync.model.User.getCurrentUser().getUsername();
        UserProfile userProfile = DatabaseManagerProfile.getInstance().getUserProfile(currentUsername);

        if (userProfile != null) {
            UserProfileController controller = new UserProfileController(userProfile, currentUsername);
            new ProfileSetupView(controller).setVisible(true);
            SwingUtilities.getWindowAncestor(this).dispose();
        }
    }

    private JButton createProfileButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(220, 53, 69));
        button.setOpaque(true); // Important for background color to show
        button.setBorderPainted(false); // No ugly border
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Modern hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 53, 69).darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 53, 69));
            }
        });
        return button;
    }

    private void showExplore() {
        cardLayout.show(contentCards, "EXPLORE");
        setActiveNav(exploreLabel);
    }

    public void showProfile() {
        cardLayout.show(contentCards, "PROFILE");
        setActiveNav(profileLabel);
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
                
                // Try to get Firebase Storage URL first
                if (userProfile.getUsername() != null) {
                    try {
                        String firebaseUrl = heartsync.database.FirebaseStorageManager.getProfileImageUrl(userProfile.getUsername());
                        if (firebaseUrl != null && !firebaseUrl.isEmpty()) {
                            photos.add(firebaseUrl);
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to get Firebase URL for user: " + userProfile.getUsername());
                    }
                }
                
                // Fallback to local profile picture path
                if (photos.isEmpty() && userProfile.getProfilePicPath() != null && !userProfile.getProfilePicPath().isEmpty()) {
                    photos.add(userProfile.getProfilePicPath());
                }
                
                // Final fallback to default image
                if (photos.isEmpty()) {
                    photos.add("RajeshHamalPhoto.png");
                }
                
                profiles.add(new ProfileData(name, age, bio, photos, userProfile.getUsername()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Add some sample profiles for testing if database fails
            profiles.add(new ProfileData("Sample User", 25, "This is a sample profile for testing.", 
                List.of("RajeshHamalPhoto.png"), "sample_user"));
        }
        allProfiles = new ArrayList<>(profiles);
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

    private void setupSearchLogic() {
        if (searchField == null) return;
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterProfiles(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterProfiles(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterProfiles(); }
        });
    }

    private void filterProfiles() {
        if (allProfiles == null) return;
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty() || query.equals("search profiles...")) {
            profiles.clear();
            profiles.addAll(allProfiles);
        } else {
            profiles.clear();
            for (ProfileData p : allProfiles) {
                boolean matches = false;
                // Name match
                if (p.name != null && p.name.toLowerCase().contains(query)) matches = true;
                // Bio/interests match
                if (!matches && p.bio != null && p.bio.toLowerCase().contains(query)) matches = true;
                // Age match (if query is a number)
                if (!matches) {
                    try {
                        int ageQuery = Integer.parseInt(query);
                        if (p.age == ageQuery) matches = true;
                    } catch (NumberFormatException ignored) {}
                }
                if (matches) profiles.add(p);
            }
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

    private void displayUserProfile(heartsync.model.UserProfile profile) {
        // Create profile panel
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(BACKGROUND_COLOR);
        profilePanel.setBorder(new EmptyBorder(40, 0, 40, 0));

        // Add profile information
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 20, 10);

        // Profile picture
        JLabel profilePicture = createProfilePicture();
        profilePicture.setPreferredSize(new Dimension(150, 150));
        profilePanel.add(profilePicture, gbc);

        // Add profile details
        addProfileDetails(profilePanel, profile, gbc);

        // Add profile panel to main panel
        mainPanel.add(profilePanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void addProfileDetails(JPanel panel, heartsync.model.UserProfile profile, GridBagConstraints gbc) {
        gbc.gridy++;
        
        // Name and age
        JLabel nameAge = new JLabel(profile.getFullName() + ", " + calculateAge(profile.getDateOfBirth()));
        nameAge.setFont(new Font("Segoe UI", Font.BOLD, 34));
        nameAge.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(nameAge, gbc);
        gbc.gridy++;

        // Location
        if (profile.getAddress() != null || profile.getCountry() != null) {
            String locationText = (profile.getAddress() != null ? profile.getAddress() : "") +
                                (profile.getCountry() != null ? ", " + profile.getCountry() : "");
            JLabel location = new JLabel(locationText);
            location.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            location.setForeground(new Color(120,120,120));
            panel.add(location, gbc);
            gbc.gridy++;
        }

        // About Me section
        if (profile.getAboutMe() != null && !profile.getAboutMe().isEmpty()) {
            JLabel aboutHeader = new JLabel("About Me");
            aboutHeader.setFont(new Font("Segoe UI", Font.BOLD, 26));
            aboutHeader.setForeground(new Color(229, 89, 36));
            aboutHeader.setBorder(new EmptyBorder(20,0,10,0));
            panel.add(aboutHeader, gbc);
            gbc.gridy++;

            JTextArea aboutMe = new JTextArea(profile.getAboutMe());
            aboutMe.setWrapStyleWord(true);
            aboutMe.setLineWrap(true);
            aboutMe.setOpaque(false);
            aboutMe.setEditable(false);
            aboutMe.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            aboutMe.setBorder(new EmptyBorder(0, 0, 20, 0));
            panel.add(aboutMe, gbc);
            gbc.gridy++;
        }

        // Action buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setOpaque(false);
        JButton editButton = createProfileButton("Edit Profile");
        editButton.addActionListener(e -> openProfileEditor());
        JButton postPicButton = createProfileButton("Post Pictures");
        postPicButton.addActionListener(e -> openPhotoUploader());
        buttonPanel.add(editButton);
        buttonPanel.add(postPicButton);
        
        gbc.gridy++;
        panel.add(buttonPanel, gbc);
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

    private void setActiveNav(JLabel activeLabel) {
        for (JLabel label : allNavLabels) {
            label.setForeground(label == activeLabel ? NAV_ACTIVE_COLOR : NAV_COLOR);
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
}