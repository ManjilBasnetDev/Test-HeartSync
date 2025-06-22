package heartsync.view;

import heartsync.database.DatingDatabase;
import heartsync.model.UserProfile;
import heartsync.model.User;
import heartsync.controller.UserProfileController;
import heartsync.view.ui.RoundedBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicButtonUI;
import java.io.IOException;
import java.net.MalformedURLException;
import java.awt.geom.Ellipse2D;

public class DatingApp extends JFrame {
    
    // Modern cohesive color palette
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245); // Light Pink
    private static final Color TEXT_PRIMARY = new Color(219, 112, 147); // Pale Violet Red for titles
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128); // Medium gray
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(255, 105, 180); // Hot Pink
    private static final Color LIKE_BUTTON_COLOR = new Color(239, 71, 111);
    private static final Color PASS_BUTTON_COLOR = new Color(236, 239, 241);
    private static final Color SUCCESS_COLOR = new Color(52, 211, 153); // Mint green
    private static final Color DANGER_COLOR = new Color(248, 113, 113); // Soft red


    // Modern typography
    private static final Font HEADING_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font SUBHEADING_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font CAPTION_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 15);
    
    // Components
    private JPanel mainPanel;
    private JPanel navigationPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Current user
    private String currentUsername;
    private DatingDatabase database;
    
    // Navigation buttons
    private JButton myLikesBtn, matchesBtn, chatBtn, myLikersBtn, notificationsBtn, logoutBtn, exploreBtn;
    private JButton activeButton;
    
    // Content panels
    private JPanel chatPanel, matchesPanel, myLikesPanel, myLikersPanel, notificationsPanel, profilePanel, explorePanel;
    
    // Current profile index for explore
    private List<UserProfile> explorableProfiles;
    private int currentProfileIndex = 0;
    
    public DatingApp() {
        // Initialize without any default user - user must be logged in
        throw new IllegalStateException("DatingApp requires a logged-in user. Use DatingApp(String username) constructor.");
    }

    public DatingApp(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.currentUsername = username.trim();
        this.database = DatingDatabase.getInstance();
        
        // Ensure user profile exists in the dating database
        database.ensureUserProfileExists(currentUsername);
        
        initializeUI();
        
        // Set the explore section as default
        showExplore();
    }
    
    private void initializeUI() {
        setTitle("HeartSync - Find Love");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setResizable(false);
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Load profiles before creating content area that depends on them
        loadExplorableProfiles();
        
        createNavigation();
        createContentArea();
        
        add(mainPanel);
    }
    
    private void createNavigation() {
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navigationPanel.setBackground(new Color(255, 228, 225)); // Lighter pink for navbar
        navigationPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(221, 160, 221)));

        JLabel titleLabel = new JLabel("HeartSync");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        navigationPanel.add(titleLabel);

        // Spacer
        navigationPanel.add(Box.createHorizontalStrut(50));

        exploreBtn = createNavButton("Explore");
        myLikesBtn = createNavButton("My Likes");
        matchesBtn = createNavButton("Matches");
        chatBtn = createNavButton("Chat");
        myLikersBtn = createNavButton("My Likers");
        notificationsBtn = createNavButton("Notifications");
        logoutBtn = createNavButton("Log Out");

        navigationPanel.add(exploreBtn);
        navigationPanel.add(myLikesBtn);
        navigationPanel.add(matchesBtn);
        navigationPanel.add(chatBtn);
        navigationPanel.add(myLikersBtn);
        navigationPanel.add(notificationsBtn);
        navigationPanel.add(logoutBtn);

        mainPanel.add(navigationPanel, BorderLayout.NORTH);
        
        setupNavigationListeners();
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setForeground(Color.BLACK);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void createContentArea() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        
        // Create all panels
        explorePanel = new ExplorePanel(currentUsername);
        chatPanel = createChatPanel();
        matchesPanel = createMatchesPanel();
        myLikesPanel = createMyLikesPanel();
        myLikersPanel = createMyLikersPanel();
        notificationsPanel = createNotificationsPanel();
        profilePanel = createProfilePanel();
        
        // Add panels to card layout
        contentPanel.add(explorePanel, "EXPLORE");
        contentPanel.add(chatPanel, "CHAT");
        contentPanel.add(matchesPanel, "MATCHES");
        contentPanel.add(myLikesPanel, "MY_LIKES");
        contentPanel.add(myLikersPanel, "MY_LIKERS");
        contentPanel.add(notificationsPanel, "NOTIFICATIONS");
        contentPanel.add(profilePanel, "PROFILE");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    // ========== EXPLORE PANEL ==========
    
    private JPanel createExplorePanel() {
        explorePanel = new ExplorePanel(currentUsername);
        return explorePanel;
    }
    
    private JPanel createProfileCard(UserProfile profile) {
        if (profile == null) {
            // Return an empty panel or a message if the profile is null
            JPanel emptyPanel = new JPanel(new BorderLayout());
            JLabel messageLabel = new JLabel("Profile not available.", SwingConstants.CENTER);
            messageLabel.setFont(HEADING_FONT);
            messageLabel.setForeground(TEXT_SECONDARY);
            emptyPanel.add(messageLabel, BorderLayout.CENTER);
            return emptyPanel;
        }
        // Main card panel with shadow and rounded corners
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(8, 8, getWidth() - 20, getHeight() - 20, 25, 25);

                // Card
                g2.setColor(CARD_BACKGROUND);
                g2.fillRoundRect(4, 4, getWidth() - 20, getHeight() - 20, 25, 25);
            }
        };
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setOpaque(false);
        cardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        // --- Content Panel (Image + Info) ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new RoundedBorder(Color.lightGray));


        // --- Image Panel ---
        JLabel imageLabel = createProfileImage(profile);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(320, 400));
        imageLabel.setMaximumSize(new Dimension(320, 400));

        // --- Info Panel ---
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Description
        JTextArea aboutMe = new JTextArea(profile.getAboutMe());
        aboutMe.setFont(BODY_FONT);
        aboutMe.setForeground(TEXT_SECONDARY);
        aboutMe.setWrapStyleWord(true);
        aboutMe.setLineWrap(true);
        aboutMe.setOpaque(false);
        aboutMe.setEditable(false);
        aboutMe.setFocusable(false);
        aboutMe.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Interests
        JPanel interestsContainer = new JPanel();
        interestsContainer.setOpaque(false);
        interestsContainer.setLayout(new BoxLayout(interestsContainer, BoxLayout.Y_AXIS));
        interestsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel interestsTitle = new JLabel("Interests");
        interestsTitle.setFont(SUBHEADING_FONT.deriveFont(Font.BOLD, 16f));
        interestsTitle.setForeground(TEXT_PRIMARY);
        
        JPanel interestsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        interestsPanel.setOpaque(false);

        List<String> interests = profile.getHobbies();
        if (interests != null) {
            for (String interest : interests) {
                interestsPanel.add(createInterestTag(interest));
            }
        }

        interestsContainer.add(interestsTitle);
        interestsContainer.add(interestsPanel);

        infoPanel.add(aboutMe);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(interestsContainer);
        
        contentPanel.add(imageLabel);
        contentPanel.add(infoPanel);
        
        // --- Action Panel ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        actionPanel.setOpaque(false);

        JButton passButton = new CircleButton("×", PASS_BUTTON_COLOR, Color.DARK_GRAY);
        passButton.addActionListener(e -> passCurrentProfile());
        
        JButton likeButton = new CircleButton("♥", LIKE_BUTTON_COLOR, Color.WHITE);
        likeButton.addActionListener(e -> likeCurrentProfile());
        
        actionPanel.add(passButton);
        actionPanel.add(likeButton);

        // Profile Counter
        JLabel profileCounter = new JLabel();
        profileCounter.setFont(CAPTION_FONT);
        profileCounter.setForeground(TEXT_SECONDARY);
        profileCounter.setText("Profile " + (currentProfileIndex + 1) + " of " + (explorableProfiles != null ? explorableProfiles.size() : 0));
        profileCounter.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(actionPanel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(profileCounter);

        container.add(contentPanel);
        container.add(Box.createVerticalStrut(10));
        container.add(bottomPanel);

        cardPanel.add(container, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(cardPanel);

        return wrapper;
    }
    
    private JComponent createInterestTag(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(ACCENT_COLOR);
        label.setBorder(new EmptyBorder(5, 15, 5, 15));

        JPanel tagPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tagPanel.setBackground(new Color(255, 228, 225));
        tagPanel.setOpaque(false);
        tagPanel.add(label, BorderLayout.CENTER);

        return tagPanel;
    }
    
    private JLabel createProfileImage(UserProfile profile) {
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(320, 400));
        imageLabel.setMaximumSize(new Dimension(320, 400));

        try {
            String picPath = profile.getProfilePicPath();
            if (picPath == null || !picPath.startsWith("http")) {
                throw new MalformedURLException("Invalid or missing image URL");
            }
            URL url = new URL(picPath);
            BufferedImage originalImage = ImageIO.read(url);
            Image scaledImage = originalImage.getScaledInstance(320, 400, Image.SCALE_SMOOTH);
            
            BufferedImage roundedImage = new BufferedImage(320, 400, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = roundedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setClip(new RoundRectangle2D.Float(0, 0, 320, 400, 20, 20));
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();

            imageLabel.setIcon(new ImageIcon(roundedImage));

        } catch (Exception e) {
            imageLabel.setIcon(createPlaceholderIcon());
            System.err.println("Could not load profile image: " + e.getMessage());
        }

        imageLabel.setLayout(new BorderLayout());

        // Info panel on top of the image
        JPanel textPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                // Gradient from transparent to black
                GradientPaint gp = new GradientPaint(0, 0, new Color(0,0,0,0), 0, getHeight(), new Color(0,0,0,180));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        textPanel.add(Box.createVerticalGlue()); // Push content to the bottom

        // Name and Age
        JLabel nameLabel = new JLabel(profile.getFullName());
        nameLabel.setFont(HEADING_FONT);
        nameLabel.setForeground(Color.WHITE);
        
        // Distance
        JLabel distanceLabel = new JLabel("5 miles away"); // Placeholder
        distanceLabel.setFont(BODY_FONT);
        distanceLabel.setForeground(Color.WHITE);

        textPanel.add(nameLabel);
        textPanel.add(distanceLabel);
        imageLabel.add(textPanel, BorderLayout.SOUTH);

        return imageLabel;
    }
    
    private ImageIcon createPlaceholderIcon() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillOval(0, 0, 100, 100);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 40));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString("?", (100 - fm.stringWidth("?")) / 2, ((100 - fm.getHeight()) / 2) + fm.getAscent());
        g2.dispose();
        return new ImageIcon(image);
    }
    
    // ========== OTHER PANELS ==========
    
    private JPanel createChatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        // Header
        JLabel headerLabel = new JLabel("💬 Chat");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerLabel.setForeground(TEXT_PRIMARY);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(new EmptyBorder(0, 0, 40, 0));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);
        
        // Chat options panel
        JPanel chatOptions = new JPanel();
        chatOptions.setLayout(new BoxLayout(chatOptions, BoxLayout.Y_AXIS));
        chatOptions.setOpaque(false);
        
        // Open full chat system button
        JButton openChatBtn = new JButton("💬 Open Full Chat System");
        openChatBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        openChatBtn.setForeground(Color.WHITE);
        openChatBtn.setBackground(ACCENT_COLOR);
        openChatBtn.setBorderPainted(false);
        openChatBtn.setFocusPainted(false);
        openChatBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openChatBtn.setPreferredSize(new Dimension(350, 60));
        openChatBtn.setMaximumSize(new Dimension(350, 60));
        openChatBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add hover effect
        openChatBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                openChatBtn.setBackground(ACCENT_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                openChatBtn.setBackground(ACCENT_COLOR);
            }
        });
        
        openChatBtn.addActionListener(e -> {
            // Get the first match to open chat with
            List<UserProfile> matches = database.getMatches(currentUsername);
            if (!matches.isEmpty()) {
                new ChatSystem(currentUsername, matches.get(0).getUsername()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No matches found. Match with someone first!", "No Matches", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Quick matches info
        JLabel quickMatchLabel = new JLabel("<html><center>Quick access to your matches:<br>Start conversations with people you've matched with!</center></html>");
        quickMatchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        quickMatchLabel.setForeground(new Color(100, 100, 100));
        quickMatchLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quickMatchLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components with spacing
        chatOptions.add(Box.createVerticalStrut(20));
        chatOptions.add(openChatBtn);
        chatOptions.add(Box.createVerticalStrut(30));
        chatOptions.add(quickMatchLabel);
        chatOptions.add(Box.createVerticalStrut(20));
        
        mainContent.add(chatOptions, BorderLayout.CENTER);
        panel.add(mainContent, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createUserListPanel(String title, String emptyMessage) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("♥ " + title, SwingConstants.CENTER);
        titleLabel.setFont(HEADING_FONT);
        titleLabel.setForeground(TEXT_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 20, 20)); // 2 columns, dynamic rows
        gridPanel.setOpaque(false);
        
        // Wrap the grid in a scroll pane
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    
    private void loadUserList(JPanel containerPanel, List<UserProfile> profiles, String context) {
        JScrollPane scrollPane = (JScrollPane) containerPanel.getComponent(1);
        JPanel gridPanel = (JPanel) scrollPane.getViewport().getView();
        gridPanel.removeAll();
        
        if (profiles == null || profiles.isEmpty()) {
            JLabel emptyLabel = new JLabel("No one here yet!", SwingConstants.CENTER);
            emptyLabel.setFont(BODY_FONT);
            emptyLabel.setForeground(TEXT_SECONDARY);
            gridPanel.setLayout(new BorderLayout());
            gridPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            gridPanel.setLayout(new GridLayout(0, 3, 20, 20)); // Changed to 3 columns
            for (UserProfile profile : profiles) {
                gridPanel.add(createDashboardUserCard(profile, context));
            }
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createDashboardUserCard(UserProfile profile, String context) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(Color.LIGHT_GRAY),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Top section with image and info
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setOpaque(false);

        JLabel picLabel = new JLabel();
        picLabel.setPreferredSize(new Dimension(80, 80));
        try {
            String picPath = profile.getProfilePicPath();
            BufferedImage image;
            if (picPath != null && picPath.startsWith("http")) {
                image = ImageIO.read(new URL(picPath));
            } else {
                throw new IOException("Invalid path for user card image");
            }
            // Create a circular image
            Image scaledImage = image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            BufferedImage roundedImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = roundedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setClip(new Ellipse2D.Float(0, 0, 80, 80));
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();
            picLabel.setIcon(new ImageIcon(roundedImage));
        } catch (IOException e) {
            String initial = profile.getFullName().substring(0, 1).toUpperCase();
            picLabel.setText(initial);
            picLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
            picLabel.setForeground(Color.WHITE);
            picLabel.setBackground(ACCENT_COLOR);
            picLabel.setOpaque(true);
            picLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        topPanel.add(picLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JLabel nameLabel = new JLabel(profile.getFullName());
        nameLabel.setFont(SUBHEADING_FONT);
        nameLabel.setForeground(TEXT_PRIMARY);
        
        JLabel emailLabel = new JLabel(profile.getEmail());
        emailLabel.setFont(CAPTION_FONT);
        emailLabel.setForeground(TEXT_SECONDARY);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(emailLabel);
        topPanel.add(infoPanel, BorderLayout.CENTER);

        card.add(topPanel, BorderLayout.NORTH);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        switch (context) {
            case "likes":
                JButton dislikeButton = createModernButton("Dislike", DANGER_COLOR);
                dislikeButton.addActionListener(e -> {
                    database.removeLike(currentUsername, profile.getUsername());
                    loadUserList(myLikesPanel, database.getMyLikes(currentUsername), "likes");
                });
                buttonPanel.add(dislikeButton);
                break;
            case "likers":
                JButton likeBackButton = createModernButton("Like Back", SUCCESS_COLOR);
                likeBackButton.addActionListener(e -> {
                    database.likeUser(currentUsername, profile.getUsername());
                    loadUserList(myLikersPanel, database.getMyLikers(currentUsername), "likers");
                });
                buttonPanel.add(likeBackButton);
                break;
            case "matches":
                JButton chatButton = createModernButton("Chat", ACCENT_COLOR);
                chatButton.addActionListener(e -> new ChatSystem(currentUsername, profile.getUsername()).setVisible(true));
                buttonPanel.add(chatButton);
                break;
        }
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createMatchesPanel() {
        return createUserListPanel("People You Matched", "No matches yet. Keep exploring!");
    }

    private JPanel createMyLikesPanel() {
        return createUserListPanel("People You Liked", "You haven't liked anyone yet.");
    }

    private JPanel createMyLikersPanel() {
        return createUserListPanel("People Who Liked You", "No one has liked you yet. Check back soon!");
    }
    
    private JPanel createNotificationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("🔔 Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // List for notifications
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> notificationList = new JList<>(model);
        notificationList.setCellRenderer(new ModernListCellRenderer());
        JScrollPane scrollPane = new JScrollPane(notificationList);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = new JLabel("👤 My Profile");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(TEXT_PRIMARY);
        headerLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Profile content
        JPanel profileContent = new JPanel(new BorderLayout());
        profileContent.setOpaque(false);
        
        // Profile card
        UserProfile userProfile = database.getUserProfile(currentUsername);
        if (userProfile != null) {
            JPanel profileCard = createProfileCard(userProfile);
            profileContent.add(profileCard, BorderLayout.CENTER);
            
            // Edit profile button
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setOpaque(false);
            
            JButton editProfileBtn = new JButton("Edit Profile");
            editProfileBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            editProfileBtn.setForeground(ACCENT_COLOR);
            editProfileBtn.setBackground(ACCENT_COLOR);
            editProfileBtn.setBorderPainted(false);
            editProfileBtn.setFocusPainted(false);
            editProfileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            editProfileBtn.setPreferredSize(new Dimension(200, 40));
            
            editProfileBtn.addActionListener(e -> {
                UserProfileController controller = new UserProfileController(userProfile, currentUsername);
                new ProfileSetupView(controller).setVisible(true);
            });
            
            buttonPanel.add(editProfileBtn);
            profileContent.add(buttonPanel, BorderLayout.SOUTH);
        } else {
            JLabel noProfileLabel = new JLabel("<html><center>❌<br><br>Profile not found<br>Please try logging in again</center></html>");
            noProfileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noProfileLabel.setForeground(TEXT_SECONDARY);
            noProfileLabel.setHorizontalAlignment(SwingConstants.CENTER);
            profileContent.add(noProfileLabel, BorderLayout.CENTER);
        }
        
        panel.add(profileContent, BorderLayout.CENTER);
        return panel;
    }
    
    // ========== NAVIGATION METHODS ==========
    
    private void setActiveButton(JButton button) {
        if (activeButton != null) {
            activeButton.setForeground(Color.BLACK);
        }
        activeButton = button;
        if (activeButton != null) {
            activeButton.setForeground(TEXT_PRIMARY);
        }
    }
    
    private void showExplore() {
        setActiveButton(exploreBtn);
        cardLayout.show(contentPanel, "EXPLORE");
    }
    
    private void showChat() {
        setActiveButton(chatBtn);
        cardLayout.show(contentPanel, "CHAT");
    }
    
    private void showMatches() {
        setActiveButton(matchesBtn);
        loadUserList(matchesPanel, database.getMatches(currentUsername), "matches");
        cardLayout.show(contentPanel, "MATCHES");
    }
    
    private void showMyLikes() {
        setActiveButton(myLikesBtn);
        loadUserList(myLikesPanel, database.getMyLikes(currentUsername), "likes");
        cardLayout.show(contentPanel, "MY_LIKES");
    }
    
    private void showMyLikers() {
        setActiveButton(myLikersBtn);
        loadUserList(myLikersPanel, database.getMyLikers(currentUsername), "likers");
        cardLayout.show(contentPanel, "MY_LIKERS");
    }
    
    private void showNotifications() {
        setActiveButton(notificationsBtn);
        loadNotifications();
        cardLayout.show(contentPanel, "NOTIFICATIONS");
    }
    
    private void showProfile() {
        setActiveButton(null); // No button is active for the profile page itself
        refreshProfilePanel();
        cardLayout.show(contentPanel, "PROFILE");
    }
    
    private void refreshProfilePanel() {
        // Re-create and set the profile panel to reflect any updates
        if (profilePanel != null) {
            contentPanel.remove(profilePanel);
        }
        profilePanel = createProfilePanel();
        contentPanel.add(profilePanel, "PROFILE");
        cardLayout.show(contentPanel, "PROFILE"); // Ensure it's visible
    }
    
    private void logout() {
        // Here you would handle the logout logic, like showing the login screen
        // For now, we'll just close the application
        JOptionPane.showMessageDialog(this, "Logged out successfully!", "Logout", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
        new LoginView().setVisible(true); // Assuming LoginView is your login screen
    }
    
    // ========== DATA LOADING METHODS ==========
    
    private void loadExplorableProfiles() {
        try {
            explorableProfiles = database.getExplorableProfiles(currentUsername);
            currentProfileIndex = 0;
        } catch (Exception e) {
            System.err.println("Error loading explorable profiles: " + e.getMessage());
        }
    }
    
    private void updateExplorePanel() {
        explorePanel.removeAll();
        explorePanel.setLayout(new BorderLayout());
        explorePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel cardContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cardContainer.setOpaque(false);
        
        UserProfile currentProfile = null;
        if (explorableProfiles != null && currentProfileIndex < explorableProfiles.size()) {
            currentProfile = explorableProfiles.get(currentProfileIndex);
        }
        
        JPanel profileCard = createProfileCard(currentProfile);
        cardContainer.add(profileCard);
        
        explorePanel.add(cardContainer, BorderLayout.CENTER);
        explorePanel.revalidate();
        explorePanel.repaint();
    }
    
    private void loadNotifications() {
        // Get the JList from the scroll pane
        JScrollPane scrollPane = (JScrollPane) notificationsPanel.getComponent(1);
        JList<String> notificationList = (JList<String>) scrollPane.getViewport().getView();
        DefaultListModel<String> model = (DefaultListModel<String>) notificationList.getModel();
        
        model.clear();
        
        List<String> notifications = database.getNotifications(currentUsername);
        
        if (notifications.isEmpty()) {
            model.addElement("No notifications");
        } else {
            for (String notification : notifications) {
                model.addElement(notification);
            }
        }
    }
    
    // ========== ACTION METHODS ==========
    
    private void likeCurrentProfile() {
        if (explorableProfiles == null || explorableProfiles.isEmpty() || currentProfileIndex >= explorableProfiles.size()) {
            JOptionPane.showMessageDialog(this, "No more profiles to see!", "End of the Line", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        UserProfile likedProfile = explorableProfiles.get(currentProfileIndex);
        String likedUsername = likedProfile.getUsername();

        boolean success = database.likeUser(currentUsername, likedUsername);

        if (success) {
            // Now check for a match
            if (database.isMatched(currentUsername, likedUsername)) {
                JOptionPane.showMessageDialog(this,
                    "It's a match with " + likedProfile.getFullName() + "!",
                    "Congratulations!",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Something went wrong while liking the profile.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        nextProfile();
    }

    private void passCurrentProfile() {
        if (explorableProfiles != null && !explorableProfiles.isEmpty()) {
            nextProfile();
        }
    }
    
    private void nextProfile() {
        if (explorableProfiles == null || explorableProfiles.isEmpty()) {
            return; // Nothing to do if there are no profiles
        }

        currentProfileIndex++;
        
        // If we've reached the end of the list, loop back to the beginning
        if (currentProfileIndex >= explorableProfiles.size()) {
            currentProfileIndex = 0;
            JOptionPane.showMessageDialog(this, "You've seen everyone! Looping back to the start.", "End of Profiles", JOptionPane.INFORMATION_MESSAGE);
        }
        
        updateExplorePanel();
    }
    
    // ========== UTILITY METHODS ==========
    
    private int calculateAge(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            return 25; // Default age
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears();
        } catch (Exception e) {
            return 25; // Default age if parsing fails
        }
    }
    

    
    // Helper method to create modern styled buttons
    private JButton createModernButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(baseColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(baseColor.brighter());
                } else {
                    g2.setColor(baseColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBorder(new ModernBorder(baseColor));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    // Modern border class for navigation buttons
    static class ModernBorder extends EmptyBorder {
        private final Color color;
        ModernBorder(Color c) { super(8,16,8,16); this.color = c; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color.darker());
            g2.drawRoundRect(x, y, w - 1, h - 1, 12, 12);
        }
    }
    
    // Modern action button with enhanced styling
    private JButton createModernActionButton(String text, String icon, Color baseColor) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(baseColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(baseColor.brighter());
                } else {
                    g2.setColor(baseColor);
                }
                
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        button.setUI(new BasicButtonUI());
        button.setPreferredSize(new Dimension(50,50));
        button.setBorder(new EmptyBorder(0,0,0,0));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void setupNavigationListeners() {
        exploreBtn.addActionListener(e -> showExplore());
        myLikesBtn.addActionListener(e -> showMyLikes());
        matchesBtn.addActionListener(e -> showMatches());
        chatBtn.addActionListener(e -> showChat());
        myLikersBtn.addActionListener(e -> showMyLikers());
        notificationsBtn.addActionListener(e -> showNotifications());
        logoutBtn.addActionListener(e -> logout());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Use FlatLaf for a modern look and feel
            try {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            } catch (Exception ex) {
                System.err.println("Failed to initialize LaF");
            }
            
            // DatingApp now requires a logged-in user
            // Launch the proper application entry point instead
            heartsync.view.HomePage homePage = new heartsync.view.HomePage();
            homePage.setVisible(true);
        });
    }
    
    class ModernListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            label.setBorder(new EmptyBorder(10, 15, 10, 15));
            if (isSelected) {
                label.setBackground(ACCENT_COLOR);
                label.setForeground(Color.WHITE);
            } else {
                label.setBackground(Color.WHITE);
                label.setForeground(TEXT_SECONDARY);
            }
            return label;
        }
    }
}

// Custom Circle Button Class
class CircleButton extends JButton {
    private Color backgroundColor;
    private Color foregroundColor;
    private Color hoverColor;

    public CircleButton(String text, Color bgColor, Color fgColor) {
        super(text);
        this.backgroundColor = bgColor;
        this.foregroundColor = fgColor;
        this.hoverColor = bgColor.brighter();

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setPreferredSize(new Dimension(60, 60));
        setFont(new Font("Arial", Font.BOLD, 30));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backgroundColor = hoverColor;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backgroundColor = bgColor; // Revert to original color
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the circle
        g2.setColor(backgroundColor);
        g2.fillOval(0, 0, getWidth(), getHeight());
        
        // Draw the text (icon)
        g2.setColor(foregroundColor);
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(getText(), x, y);
        
        g2.dispose();
    }
}

class ExplorePanel extends JPanel {

    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245); // Light Pink
    private static final Color TEXT_PRIMARY = new Color(219, 112, 147); // Pale Violet Red for titles
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128); // Medium gray
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(255, 105, 180); // Hot Pink
    private static final Color LIKE_BUTTON_COLOR = new Color(239, 71, 111);
    private static final Color PASS_BUTTON_COLOR = new Color(236, 239, 241);
    private static final Font HEADING_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font SUBHEADING_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font CAPTION_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private String currentUsername;
    private DatingDatabase database;
    private List<UserProfile> explorableProfiles;
    private int currentProfileIndex = 0;

    public ExplorePanel(String username) {
        this.currentUsername = username;
        this.database = DatingDatabase.getInstance();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        
        loadExplorableProfiles();
    }

    private void loadExplorableProfiles() {
        // Show a loading message
        removeAll();
        JLabel loadingLabel = new JLabel("Finding people near you...", SwingConstants.CENTER);
        loadingLabel.setFont(HEADING_FONT);
        loadingLabel.setForeground(TEXT_SECONDARY);
        add(loadingLabel, BorderLayout.CENTER);
        revalidate();
        repaint();

        new SwingWorker<List<UserProfile>, Void>() {
            @Override
            protected List<UserProfile> doInBackground() throws Exception {
                return database.getExplorableProfiles(currentUsername);
            }

            @Override
            protected void done() {
                try {
                    explorableProfiles = get();
                    if (explorableProfiles == null) {
                        explorableProfiles = new ArrayList<>();
                    }
                    currentProfileIndex = 0;
                    updateExplorePanel();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle error case
                    explorableProfiles = new ArrayList<>();
                    updateExplorePanel();
                }
            }
        }.execute();
    }

    private void updateExplorePanel() {
        this.removeAll();

        if (explorableProfiles != null && !explorableProfiles.isEmpty() && currentProfileIndex < explorableProfiles.size()) {
            UserProfile currentProfile = explorableProfiles.get(currentProfileIndex);
            add(createProfileCard(currentProfile), BorderLayout.CENTER);
        } else {
            JLabel noMoreProfilesLabel = new JLabel("No more profiles to show.", SwingConstants.CENTER);
            noMoreProfilesLabel.setFont(HEADING_FONT);
            noMoreProfilesLabel.setForeground(TEXT_SECONDARY);
            add(noMoreProfilesLabel, BorderLayout.CENTER);
        }

        revalidate();
        repaint();
    }

    private JPanel createProfileCard(UserProfile profile) {
        if (profile == null) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            JLabel messageLabel = new JLabel("Profile not available.", SwingConstants.CENTER);
            emptyPanel.add(messageLabel, BorderLayout.CENTER);
            return emptyPanel;
        }

        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(8, 8, getWidth() - 20, getHeight() - 20, 25, 25);
                g2.setColor(CARD_BACKGROUND);
                g2.fillRoundRect(4, 4, getWidth() - 20, getHeight() - 20, 25, 25);
            }
        };
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setOpaque(false);
        cardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBackground(Color.WHITE);

        JLabel imageLabel = createProfileImage(profile);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(320, 400));
        imageLabel.setMaximumSize(new Dimension(320, 400));

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea aboutMe = new JTextArea(profile.getAboutMe());
        aboutMe.setFont(BODY_FONT);
        aboutMe.setForeground(TEXT_SECONDARY);
        aboutMe.setWrapStyleWord(true);
        aboutMe.setLineWrap(true);
        aboutMe.setOpaque(false);
        aboutMe.setEditable(false);
        aboutMe.setFocusable(false);
        aboutMe.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel interestsContainer = new JPanel();
        interestsContainer.setOpaque(false);
        interestsContainer.setLayout(new BoxLayout(interestsContainer, BoxLayout.Y_AXIS));
        interestsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel interestsTitle = new JLabel("Interests");
        interestsTitle.setFont(SUBHEADING_FONT.deriveFont(Font.BOLD, 16f));
        interestsTitle.setForeground(TEXT_PRIMARY);

        JPanel interestsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        interestsPanel.setOpaque(false);

        List<String> interests = profile.getHobbies();
        if (interests != null) {
            for (String interest : interests) {
                interestsPanel.add(createInterestTag(interest));
            }
        }

        interestsContainer.add(interestsTitle);
        interestsContainer.add(interestsPanel);
        infoPanel.add(aboutMe);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(interestsContainer);
        contentPanel.add(imageLabel);
        contentPanel.add(infoPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        actionPanel.setOpaque(false);

        JButton passButton = new CircleButton("×", PASS_BUTTON_COLOR, Color.DARK_GRAY);
        passButton.addActionListener(e -> passCurrentProfile());
        JButton likeButton = new CircleButton("♥", LIKE_BUTTON_COLOR, Color.WHITE);
        likeButton.addActionListener(e -> likeCurrentProfile());

        actionPanel.add(passButton);
        actionPanel.add(likeButton);

        JLabel profileCounter = new JLabel("Profile " + (currentProfileIndex + 1) + " of " + (explorableProfiles != null ? explorableProfiles.size() : 0));
        profileCounter.setFont(CAPTION_FONT);
        profileCounter.setForeground(TEXT_SECONDARY);
        profileCounter.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(actionPanel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(profileCounter);

        container.add(contentPanel);
        container.add(Box.createVerticalStrut(10));
        container.add(bottomPanel);

        cardPanel.add(container, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(cardPanel);

        return wrapper;
    }

    private JLabel createProfileImage(UserProfile profile) {
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(320, 400));
        imageLabel.setMaximumSize(new Dimension(320, 400));

        try {
            String picPath = profile.getProfilePicPath();
            if (picPath == null || !picPath.startsWith("http")) {
                throw new MalformedURLException("Invalid or missing image URL");
            }
            URL url = new URL(picPath);
            BufferedImage originalImage = ImageIO.read(url);
            Image scaledImage = originalImage.getScaledInstance(320, 400, Image.SCALE_SMOOTH);
            
            BufferedImage roundedImage = new BufferedImage(320, 400, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = roundedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setClip(new RoundRectangle2D.Float(0, 0, 320, 400, 20, 20));
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();

            imageLabel.setIcon(new ImageIcon(roundedImage));

        } catch (Exception e) {
            imageLabel.setIcon(createPlaceholderIcon());
            System.err.println("Could not load profile image: " + e.getMessage());
        }

        imageLabel.setLayout(new BorderLayout());

        // Info panel on top of the image
        JPanel textPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                // Gradient from transparent to black
                GradientPaint gp = new GradientPaint(0, 0, new Color(0,0,0,0), 0, getHeight(), new Color(0,0,0,180));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        textPanel.add(Box.createVerticalGlue()); // Push content to the bottom

        // Name and Age
        JLabel nameLabel = new JLabel(profile.getFullName());
        nameLabel.setFont(HEADING_FONT);
        nameLabel.setForeground(Color.WHITE);
        
        // Distance
        JLabel distanceLabel = new JLabel("5 miles away"); // Placeholder
        distanceLabel.setFont(BODY_FONT);
        distanceLabel.setForeground(Color.WHITE);

        textPanel.add(nameLabel);
        textPanel.add(distanceLabel);
        imageLabel.add(textPanel, BorderLayout.SOUTH);

        return imageLabel;
    }

    private ImageIcon createPlaceholderIcon() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillOval(0, 0, 100, 100);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 40));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString("?", (100 - fm.stringWidth("?")) / 2, ((100 - fm.getHeight()) / 2) + fm.getAscent());
        g2.dispose();
        return new ImageIcon(image);
    }

    private JComponent createInterestTag(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(ACCENT_COLOR);
        label.setBorder(new EmptyBorder(5, 15, 5, 15));

        JPanel tagPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tagPanel.setBackground(new Color(255, 228, 225));
        tagPanel.setOpaque(false);
        tagPanel.add(label, BorderLayout.CENTER);

        return tagPanel;
    }
    
    private void likeCurrentProfile() {
        if (explorableProfiles != null && !explorableProfiles.isEmpty()) {
            UserProfile likedProfile = explorableProfiles.get(currentProfileIndex);
            database.likeUser(currentUsername, likedProfile.getUsername());
            nextProfile();
        }
    }

    private void passCurrentProfile() {
        if (explorableProfiles != null && !explorableProfiles.isEmpty()) {
            nextProfile();
        }
    }

    private void nextProfile() {
        if (explorableProfiles == null || explorableProfiles.isEmpty()) {
            return; // Nothing to do if there are no profiles
        }

        currentProfileIndex++;
        
        // If we've reached the end of the list, loop back to the beginning
        if (currentProfileIndex >= explorableProfiles.size()) {
            currentProfileIndex = 0;
            JOptionPane.showMessageDialog(this, "You've seen everyone! Looping back to the start.", "End of Profiles", JOptionPane.INFORMATION_MESSAGE);
        }
        
        updateExplorePanel();
    }
} 