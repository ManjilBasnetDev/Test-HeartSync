package heartsync.view;

import heartsync.database.DatingDatabase;
import heartsync.model.UserProfile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class ExplorePanel extends JPanel {

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
        updateExplorePanel();
    }

    private void loadExplorableProfiles() {
        try {
            explorableProfiles = database.getExplorableProfiles(currentUsername);
            currentProfileIndex = 0;
            if (explorableProfiles == null) {
                explorableProfiles = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Error loading explorable profiles: " + e.getMessage());
            explorableProfiles = new ArrayList<>();
        }
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
                interestsPanel.add(createInterestButton(interest));
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
            URL url = new URL(profile.getProfilePicPath());
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
            imageLabel.setText("No Image");
            e.printStackTrace();
        }

        imageLabel.setLayout(new BorderLayout());

        JPanel textPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
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
        textPanel.add(Box.createVerticalGlue());

        JLabel nameLabel = new JLabel(profile.getFullName());
        nameLabel.setFont(HEADING_FONT);
        nameLabel.setForeground(Color.WHITE);

        JLabel distanceLabel = new JLabel("5 miles away"); // Placeholder
        distanceLabel.setFont(BODY_FONT);
        distanceLabel.setForeground(Color.WHITE);

        textPanel.add(nameLabel);
        textPanel.add(distanceLabel);
        imageLabel.add(textPanel, BorderLayout.SOUTH);

        return imageLabel;
    }

    private JButton createInterestButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(ACCENT_COLOR);
        button.setBackground(new Color(255, 228, 225));
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(5, 15, 5, 15));
        return button;
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
        currentProfileIndex++;
        updateExplorePanel();
    }


} 