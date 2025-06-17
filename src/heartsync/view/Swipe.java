/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package heartsync.view;

import heartsync.model.User;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Modern swipe interface for browsing through potential matches.
 * @author manjil-basnet
 */
public class Swipe extends JFrame {
    private static final int WINDOW_RADIUS = 20;
    private static final Color BACKGROUND_COLOR = new Color(255, 216, 227); // Pink background
    private static final Color PRIMARY_COLOR = new Color(255, 64, 129); // Material Design Pink
    private static final Color SECONDARY_COLOR = new Color(68, 138, 255); // Material Design Blue
    private static final Color BUTTON_COLOR = new Color(229, 89, 36); // Orange
    private static final Color LIKE_COLOR = new Color(46, 204, 113); // Green
    private static final Color REJECT_COLOR = new Color(231, 76, 60); // Red
    private static final Color BUTTON_HOVER_OVERLAY = new Color(0, 0, 0, 40); // Semi-transparent black for hover
    private static final Color CLOSE_BUTTON_COLOR = new Color(231, 76, 60); // Red for close button
    private static final Color NAV_BACKGROUND = new Color(255, 255, 255, 230); // Semi-transparent white
    
    private final JPanel mainPanel;
    private final JPanel cardPanel;
    private final JPanel navigationPanel;
    private final CardLayout cardLayout;
    private final JLabel imageLabel;
    private final JLabel nameLabel;
    private final JLabel ageLabel;
    private final JLabel bioLabel;
    private final RoundedButton nextButton;
    private final RoundedButton backButton;
    private final RoundedButton likeButton;
    private final RoundedButton rejectButton;
    private final RoundedButton closeButton;
    private final RoundedButton messageButton;
    private final RoundedButton myProfileButton;
    private final RoundedButton searchButton;
    private final ArrayList<ProfileData> profiles;
    private int currentIndex;
    private User currentUser; // Add field to store the current user
    
    // Method to set the current user after login
    public void setUser(User user) {
        this.currentUser = user;
        // You can update the UI here based on the user if needed
        if (user != null) {
            setTitle("HeartSync - Welcome, " + user.getUsername() + "!");
        }
    }
    
    private static class ProfileData {
        String imagePath;
        String name;
        int age;
        String bio;
        
        ProfileData(String imagePath, String name, int age, String bio) {
            this.imagePath = imagePath;
            this.name = name;
            this.age = age;
            this.bio = bio;
        }
    }

    private static class RoundedButton extends JButton {
        private boolean isHovered = false;
        private final Color baseColor;
        private static final int RADIUS = 25; // More rounded corners

        public RoundedButton(String text, Color color) {
            super(text);
            this.baseColor = color;
            setupButton();
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
        setTitle("HeartSync - Find Matches");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setUndecorated(true);
        
        // Initialize CardLayout
        cardLayout = new CardLayout();
        
        // Initialize UI components
        imageLabel = new JLabel();
        nameLabel = new JLabel();
        ageLabel = new JLabel();
        bioLabel = new JLabel();
        rejectButton = new RoundedButton("✕", REJECT_COLOR);
        backButton = new RoundedButton("←", BUTTON_COLOR);
        nextButton = new RoundedButton("→", BUTTON_COLOR);
        likeButton = new RoundedButton("♥", LIKE_COLOR);
        closeButton = new RoundedButton("×", CLOSE_BUTTON_COLOR);
        messageButton = new RoundedButton("💬 Messages", PRIMARY_COLOR);
        myProfileButton = new RoundedButton("👤 My Profile", PRIMARY_COLOR);
        searchButton = new RoundedButton("🔍 Search", SECONDARY_COLOR);
        
        // Main panel with rounded corners
        mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BACKGROUND_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS);
            }
        };
        mainPanel.setOpaque(false);
        
        // Create top navigation panel
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navigationPanel.setBackground(NAV_BACKGROUND);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        // Create additional navigation buttons
        RoundedButton matchesButton = new RoundedButton("❤️ Matches", SECONDARY_COLOR);
        RoundedButton settingsButton = new RoundedButton("⚙️ Settings", PRIMARY_COLOR);
        
        // Style navigation buttons
        Dimension navButtonSize = new Dimension(150, 40);
        Font navButtonFont = new Font("Segoe UI", Font.BOLD, 14);
        
        for (RoundedButton btn : new RoundedButton[]{myProfileButton, searchButton, messageButton, matchesButton, settingsButton}) {
            btn.setPreferredSize(navButtonSize);
            btn.setFont(navButtonFont);
        }
        
        // Add buttons to navigation panel
        navigationPanel.add(myProfileButton);
        navigationPanel.add(searchButton);
        navigationPanel.add(messageButton);
        navigationPanel.add(matchesButton);
        navigationPanel.add(settingsButton);
        
        // Add window control buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        controlPanel.setOpaque(false);
        
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        closeButton.setPreferredSize(new Dimension(35, 35));
        closeButton.addActionListener(e -> dispose());
        
        RoundedButton minimizeButton = new RoundedButton("−", SECONDARY_COLOR);
        minimizeButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        minimizeButton.setPreferredSize(new Dimension(35, 35));
        minimizeButton.addActionListener(e -> setState(Frame.ICONIFIED));
        
        controlPanel.add(minimizeButton);
        controlPanel.add(closeButton);
        
        // Create card panel for different views
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        
        // Create swipe view panel
        JPanel swipeView = createSwipeView();
        JPanel profileView = createProfileView();
        JPanel searchView = createSearchView();
        JPanel chatView = createChatView();
        
        // Add views to card panel
        cardPanel.add(swipeView, "SWIPE");
        cardPanel.add(profileView, "PROFILE");
        cardPanel.add(searchView, "SEARCH");
        cardPanel.add(chatView, "CHAT");
        
        // Add components to main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(navigationPanel, BorderLayout.CENTER);
        topPanel.add(controlPanel, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Set content pane
        setContentPane(mainPanel);
        
        // Setup window shape
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS));
        
        // Initialize profiles
        profiles = new ArrayList<>();
        setupProfiles();
        setupListeners();
        showCurrentProfile();
    }
    
    private JPanel createSwipeView() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        
        // Profile image
        imageLabel.setBounds(50, 20, 500, 500);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        
        // Profile info
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(new Color(51, 51, 51));
        nameLabel.setBounds(50, 540, 300, 30);
        
        ageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        ageLabel.setForeground(new Color(102, 102, 102));
        ageLabel.setBounds(50, 575, 100, 25);
        
        bioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bioLabel.setForeground(new Color(102, 102, 102));
        bioLabel.setBounds(50, 610, 500, 60);
        
        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.setOpaque(false);
        actionPanel.setBounds(50, 680, 500, 60);
        
        Dimension actionButtonSize = new Dimension(50, 50);
        for (RoundedButton btn : new RoundedButton[]{rejectButton, backButton, nextButton, likeButton}) {
            btn.setPreferredSize(actionButtonSize);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        }
        
        actionPanel.add(rejectButton);
        actionPanel.add(backButton);
        actionPanel.add(nextButton);
        actionPanel.add(likeButton);
        
        panel.add(imageLabel);
        panel.add(nameLabel);
        panel.add(ageLabel);
        panel.add(bioLabel);
        panel.add(actionPanel);
        
        return panel;
    }
    
    private JPanel createProfileView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(new JLabel("Profile View Coming Soon!", SwingConstants.CENTER));
        return panel;
    }
    
    private JPanel createSearchView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(new JLabel("Search View Coming Soon!", SwingConstants.CENTER));
        return panel;
    }
    
    private JPanel createChatView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(new JLabel("Chat View Coming Soon!", SwingConstants.CENTER));
        return panel;
    }

    private void setupProfiles() {
        // Add sample profiles - in a real app, these would come from a database
        profiles.add(new ProfileData(
            "/ImagePicker/RajeshHamalPhoto.png",
            "Rajesh Hamal",
            45,
            "Mahanayak of Nepali Film Industry. Actor, Model, and Philanthropist."
        ));
        profiles.add(new ProfileData(
            "/ImagePicker/RajeshHamalPhoto2.jpg",
            "Rajesh Hamal",
            45,
            "Award-winning actor with over 200 films. Loves adventure and traveling."
        ));
        currentIndex = 0;
    }
    
    private void setupListeners() {
        // Window dragging
        MouseAdapter dragListener = new MouseAdapter() {
            private Point mouseDownCompCoords;
            
            @Override
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseDownCompCoords != null) {
                    Point currCoords = e.getLocationOnScreen();
                    setLocation(currCoords.x - mouseDownCompCoords.x, 
                              currCoords.y - mouseDownCompCoords.y);
                }
            }
        };
        
        mainPanel.addMouseListener(dragListener);
        mainPanel.addMouseMotionListener(dragListener);
        
        // Navigation button actions
        myProfileButton.addActionListener(e -> cardLayout.show(cardPanel, "PROFILE"));
        searchButton.addActionListener(e -> cardLayout.show(cardPanel, "SEARCH"));
        messageButton.addActionListener(e -> cardLayout.show(cardPanel, "CHAT"));
        
        // Swipe button actions
        nextButton.addActionListener(e -> showNextProfile());
        backButton.addActionListener(e -> showPreviousProfile());
        likeButton.addActionListener(e -> likeCurrentProfile());
        rejectButton.addActionListener(e -> rejectCurrentProfile());
    }
    
    private void showCurrentProfile() {
        if (currentIndex >= 0 && currentIndex < profiles.size()) {
            ProfileData profile = profiles.get(currentIndex);
            
            // Load and scale image
            try {
                java.net.URL imageUrl = getClass().getResource(profile.imagePath);
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image img = icon.getImage().getScaledInstance(
                        imageLabel.getWidth(), 
                        imageLabel.getHeight(), 
                        Image.SCALE_SMOOTH
                    );
                    imageLabel.setIcon(new ImageIcon(img));
                }
            } catch (Exception e) {
                e.printStackTrace();
                imageLabel.setIcon(null);
                JOptionPane.showMessageDialog(this,
                    "Error loading image: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
            // Update profile info
            nameLabel.setText(profile.name);
            ageLabel.setText(profile.age + " years");
            bioLabel.setText("<html>" + profile.bio + "</html>");
            
            // Update button states
            backButton.setEnabled(currentIndex > 0);
            nextButton.setEnabled(currentIndex < profiles.size() - 1);
        }
    }
    
    private void showNextProfile() {
        if (currentIndex < profiles.size() - 1) {
            currentIndex++;
            showCurrentProfile();
        } else {
            JOptionPane.showMessageDialog(this,
                "No more profiles to show!",
                "End of List",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showPreviousProfile() {
        if (currentIndex > 0) {
            currentIndex--;
            showCurrentProfile();
        } else {
            JOptionPane.showMessageDialog(this,
                "This is the first profile!",
                "First Profile",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void likeCurrentProfile() {
        ProfileData profile = profiles.get(currentIndex);
        JOptionPane.showMessageDialog(this,
            "You liked " + profile.name + "!\nMatch functionality coming soon.",
            "Liked",
            JOptionPane.INFORMATION_MESSAGE);
        showNextProfile();
    }
    
    private void rejectCurrentProfile() {
        ProfileData profile = profiles.get(currentIndex);
        JOptionPane.showMessageDialog(this,
            "You passed on " + profile.name + ".",
            "Passed",
            JOptionPane.INFORMATION_MESSAGE);
        showNextProfile();
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