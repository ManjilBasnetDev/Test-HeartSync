package heartsync.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import heartsync.dao.LikeDAO;
import heartsync.dao.UserDAO;
import heartsync.model.User;
import heartsync.model.UserProfile;

public class LikesView extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(255, 245, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(255, 105, 180);
    private static final int CARD_WIDTH = 300;
    private static final int CARD_HEIGHT = 400;
    
    private final LikeDAO likeDAO;
    private final UserDAO userDAO;
    private final String currentUserId;
    private final boolean isLikersView;
    private JPanel cardsPanel;
    
    public LikesView(String userId, boolean showLikers) {
        this.currentUserId = userId;
        this.isLikersView = showLikers;
        this.likeDAO = new LikeDAO();
        this.userDAO = new UserDAO();
        
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        
        // Create header
        JLabel headerLabel = new JLabel(isLikersView ? "People Who Like You" : "People You Like");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(51, 51, 51));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(headerLabel, BorderLayout.NORTH);
        
        // Create scrollable cards panel
        cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        cardsPanel.setBackground(BACKGROUND_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load and display profiles
        loadProfiles();
    }
    
    private void loadProfiles() {
        List<String> userIds;
        if (isLikersView) {
            // Get users who like the current user
            userIds = getLikers();
        } else {
            // Get users liked by the current user
            userIds = likeDAO.getLikedUsers(currentUserId);
        }
        
        for (String userId : userIds) {
            try {
                User user = userDAO.getUserById(userId);
                if (user != null) {
                    addProfileCard(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private List<String> getLikers() {
        List<String> likers = new ArrayList<>();
        try {
            // Get all users
            List<User> allUsers = userDAO.getAllUsers();
            for (User user : allUsers) {
                // Check if this user has liked the current user
                List<String> userLikes = likeDAO.getLikedUsers(user.getUserId());
                if (userLikes.contains(currentUserId)) {
                    likers.add(user.getUserId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return likers;
    }
    
    private void addProfileCard(User user) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Profile picture
        JLabel photoLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw circular mask
                Shape circle = new java.awt.geom.Ellipse2D.Double(0, 0, getWidth(), getWidth());
                g2d.setClip(circle);
                
                // Draw image or default
                ImageIcon icon = new ImageIcon(user.getProfilePictureUrl() != null ? 
                    user.getProfilePictureUrl() : "path/to/default/image.png");
                Image img = icon.getImage();
                g2d.drawImage(img, 0, 0, getWidth(), getWidth(), this);
            }
        };
        photoLabel.setPreferredSize(new Dimension(CARD_WIDTH - 20, CARD_WIDTH - 20));
        
        // User info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_COLOR);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Name
        JLabel nameLabel = new JLabel(user.getUsername());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Bio
        JLabel bioLabel = new JLabel(user.getBio() != null ? user.getBio() : "");
        bioLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(bioLabel);
        
        card.add(photoLabel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);
        
        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_COLOR, 2),
                    BorderFactory.createEmptyBorder(9, 9, 9, 9)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        });
        
        cardsPanel.add(card);
    }
} 