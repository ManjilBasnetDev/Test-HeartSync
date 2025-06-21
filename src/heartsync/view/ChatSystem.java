/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.view;

import heartsync.dao.ChatDAO;
import heartsync.dao.LikeDAO;
import heartsync.dao.UserDAO;
import heartsync.model.Chat;
import heartsync.model.User;
import heartsync.model.UserProfile;
import heartsync.database.DatabaseManagerProfile;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

/**
 * Main chat window with a split-pane layout.
 * Displays a list of matched users on the left and the conversation on the right.
 *
 * @author Manjil
 */
public class ChatSystem extends JFrame {

    private final User currentUser;
    private final LikeDAO likeDAO;
    private final UserDAO userDAO;
    private final ChatDAO chatDAO;

    private JSplitPane splitPane;
    private JPanel contactListPanel;
    private ConversationView conversationView;
    private JScrollPane contactListScrollPane;

    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color CONTACT_ITEM_COLOR = new Color(255, 255, 255);
    private static final Color CONTACT_ITEM_HOVER_COLOR = new Color(230, 240, 255);
    private static final Color SELECTED_CONTACT_COLOR = new Color(200, 220, 255);
    private static final Font NAME_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font MESSAGE_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color CONTACT_LIST_BACKGROUND_COLOR = new Color(255, 240, 245); // Light pink

    public ChatSystem(User currentUser) {
        this.currentUser = currentUser;
        this.likeDAO = new LikeDAO();
        this.userDAO = new UserDAO();
        this.chatDAO = new ChatDAO();

        setTitle("HeartSync Messenger");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set application icon if available
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/ImagePicker/HomePageCoupleImg.png"));
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                setIconImage(icon.getImage());
            }
        } catch (Exception e) {
            // Silently handle icon loading failure
            System.out.println("Application icon not found, using default.");
        }

        initComponents();
        loadMatchedUsers();
    }

    private void initComponents() {
        // Left side: Panel to hold the list of contacts
        contactListPanel = new JPanel();
        contactListPanel.setLayout(new BoxLayout(contactListPanel, BoxLayout.Y_AXIS));
        contactListPanel.setBackground(CONTACT_LIST_BACKGROUND_COLOR);
        contactListPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        contactListScrollPane = new JScrollPane(contactListPanel);
        contactListScrollPane.getViewport().setBackground(CONTACT_LIST_BACKGROUND_COLOR);
        contactListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contactListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contactListScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        // Right side: Panel to show the conversation
        conversationView = new ConversationView(currentUser, null); // Initially no user selected

        // Split pane to divide contacts and conversation
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, contactListScrollPane, conversationView);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.1);
        splitPane.setOneTouchExpandable(true);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);

        // Add a header with a back button
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(230, 230, 250)); // Light lavender for the header
        JButton backButton = new JButton("← Back to Explore");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            new Swipe().setVisible(true);
            dispose();
        });
        headerPanel.add(backButton);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void loadMatchedUsers() {
        contactListPanel.removeAll();

        List<String> matchedUserIds = likeDAO.getMatches(currentUser.getUsername());
        
        // Add a header showing total matches
        JLabel matchesHeader = new JLabel(String.format("  Matches (%d)", matchedUserIds.size()));
        matchesHeader.setFont(new Font("Arial", Font.BOLD, 16));
        matchesHeader.setBorder(new EmptyBorder(10, 10, 10, 10));
        contactListPanel.add(matchesHeader);
        contactListPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        List<UserProfile> matchedProfiles = matchedUserIds.stream()
                                                          .map(userId -> DatabaseManagerProfile.getInstance().getUserProfile(userId))
                                                          .filter(java.util.Objects::nonNull)
                                                          .collect(Collectors.toList());

        if (matchedProfiles.isEmpty()) {
            JPanel noMatchesPanel = new JPanel();
            noMatchesPanel.setLayout(new BoxLayout(noMatchesPanel, BoxLayout.Y_AXIS));
            noMatchesPanel.setBackground(CONTACT_LIST_BACKGROUND_COLOR);
            noMatchesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel emojiLabel = new JLabel("💝");
            emojiLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
            emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel noMatchesLabel = new JLabel("No matches yet");
            noMatchesLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noMatchesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel subtitleLabel = new JLabel("Keep exploring to find your match!");
            subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            subtitleLabel.setForeground(Color.GRAY);
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton exploreButton = new JButton("Explore Now");
            exploreButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            exploreButton.addActionListener(e -> {
                new Swipe().setVisible(true);
                dispose();
            });

            noMatchesPanel.add(Box.createVerticalGlue());
            noMatchesPanel.add(emojiLabel);
            noMatchesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            noMatchesPanel.add(noMatchesLabel);
            noMatchesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            noMatchesPanel.add(subtitleLabel);
            noMatchesPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            noMatchesPanel.add(exploreButton);
            noMatchesPanel.add(Box.createVerticalGlue());

            contactListPanel.add(noMatchesPanel);
        } else {
            for (UserProfile matchedProfile : matchedProfiles) {
                JPanel contactItem = createContactItem(matchedProfile);
                contactListPanel.add(contactItem);
                contactListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        contactListPanel.revalidate();
        contactListPanel.repaint();
    }

    private JPanel createContactItem(UserProfile matchedProfile) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(CONTACT_ITEM_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(5, 10, 5, 10), // Margin
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR) // Bottom border
        ));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        // Profile picture
        JLabel picLabel = new JLabel();
        picLabel.setPreferredSize(new Dimension(50, 50));
        picLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Load profile picture asynchronously
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                Image image = null;
                String picPath = matchedProfile.getProfilePicPath();
                
                if (picPath != null && !picPath.isEmpty()) {
                    if (picPath.startsWith("data:image")) {
                        // Handle Base64 image data
                        String base64Image = picPath.substring(picPath.indexOf(",") + 1);
                        byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);
                        try (java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(imageBytes)) {
                            image = ImageIO.read(bis);
                        }
                    } else if (picPath.startsWith("http")) {
                        image = ImageIO.read(new java.net.URL(picPath));
                    }
                }
                
                if (image != null) {
                    // Create circular avatar
                    int size = 50;
                    BufferedImage circularImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = circularImage.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
                    g2.drawImage(image.getScaledInstance(size, size, Image.SCALE_SMOOTH), 0, 0, null);
                    g2.dispose();
                    return new ImageIcon(circularImage);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        picLabel.setIcon(icon);
                    } else {
                        picLabel.setText("👤");
                        picLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
                        picLabel.setForeground(new Color(108, 117, 125));
                    }
                } catch (Exception e) {
                    picLabel.setText("👤");
                    picLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
                    picLabel.setForeground(new Color(108, 117, 125));
                }
            }
        }.execute();

        panel.add(picLabel, BorderLayout.WEST);

        // Name and last message
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(matchedProfile.getFullName());
        nameLabel.setFont(NAME_FONT);
        textPanel.add(nameLabel);

        // Add user ID label
        JLabel userIdLabel = new JLabel("@" + matchedProfile.getUsername());
        userIdLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        userIdLabel.setForeground(new Color(128, 128, 128));
        textPanel.add(userIdLabel);

        // Fetch and display the last message
        Chat lastChat = chatDAO.getLastMessage(currentUser.getUsername(), matchedProfile.getUsername());
        String lastMessageText = (lastChat != null) ? lastChat.getMessage() : "No messages yet.";
        if (lastMessageText.length() > 30) {
            lastMessageText = lastMessageText.substring(0, 27) + "...";
        }
        JLabel lastMessageLabel = new JLabel(lastMessageText);
        lastMessageLabel.setFont(MESSAGE_FONT);
        lastMessageLabel.setForeground(Color.GRAY);
        textPanel.add(lastMessageLabel);

        panel.add(textPanel, BorderLayout.CENTER);

        // Add online status indicator
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        statusPanel.setOpaque(false);
        JLabel statusDot = new JLabel("●");
        statusDot.setFont(new Font("Arial", Font.BOLD, 12));
        statusDot.setForeground(new Color(46, 204, 113)); // Green for online
        statusPanel.add(statusDot);
        panel.add(statusPanel, BorderLayout.EAST);

        // Mouse listener for selection and hover effects
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // When a contact is clicked, update the conversation view
                conversationView.setParticipant(matchedProfile);
                setSelectedContact(panel);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!panel.getBackground().equals(SELECTED_CONTACT_COLOR)) {
                    panel.setBackground(CONTACT_ITEM_HOVER_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                 if (!panel.getBackground().equals(SELECTED_CONTACT_COLOR)) {
                    panel.setBackground(CONTACT_ITEM_COLOR);
                }
            }
        });

        return panel;
    }

    private void setSelectedContact(JPanel selectedPanel) {
        // Reset background of all contact items
        for (Component component : contactListPanel.getComponents()) {
            if (component instanceof JPanel) {
                component.setBackground(CONTACT_ITEM_COLOR);
            }
        }
        // Set background for the selected one
        selectedPanel.setBackground(SELECTED_CONTACT_COLOR);
    }
} 