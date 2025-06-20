package heartsync.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;

import heartsync.model.User;
import heartsync.model.UserProfile;
import heartsync.database.DatabaseManagerProfile;
import heartsync.database.FirebaseConfig;
import heartsync.navigation.WindowManager;
import com.google.gson.reflect.TypeToken;

public class ChatSystem extends JFrame {
    private static final int WINDOW_RADIUS = 20;
    private static final Color THEME_COLOR = new Color(105, 0, 51); // #690033
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final int AVATAR_SIZE = 32;
    private static final int CARD_RADIUS = 10;
    private static final Font CHAT_FONT = new Font("Roboto", Font.PLAIN, 13);
    
    private final JPanel mainPanel;
    private final JPanel headerPanel;
    private final JPanel contentPanel;
    private final ArrayList<MatchedUser> matchedUsers;
    
    private static class MatchedUser {
        String name;
        String imagePath;
        String userId;
        
        MatchedUser(String name, String imagePath, String userId) {
            this.name = name;
            this.imagePath = imagePath;
            this.userId = userId;
        }
    }
    
    public ChatSystem() {
        setTitle("HeartSync");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setUndecorated(true);
        
        // Main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header Panel
        headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content Panel (Main area)
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        
        // Initialize matched users
        matchedUsers = new ArrayList<>();
        loadMatchedUsers();
        
        // Make window draggable
        setupWindowDragging();
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(THEME_COLOR);
        header.setPreferredSize(new Dimension(getWidth(), 70));
        
        // Left side with back button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        leftPanel.setOpaque(false);

        // Back button as a white arrow
        JButton backButton = new JButton("←");
        backButton.setForeground(TEXT_COLOR);
        backButton.setFont(new Font("Arial", Font.BOLD, 28));
        backButton.setBackground(THEME_COLOR);
        backButton.setBorder(null);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            WindowManager.show(Swipe.class, Swipe::new, null);
        });
        leftPanel.add(backButton);
        
        // Center logo
        JLabel logo = new JLabel("HEARTSYNC");
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(TEXT_COLOR);
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Right menu button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        rightPanel.setOpaque(false);
        JButton menuButton = new JButton("☰");
        menuButton.setForeground(TEXT_COLOR);
        menuButton.setFont(new Font("Arial", Font.BOLD, 24));
        menuButton.setBackground(THEME_COLOR);
        menuButton.setBorder(null);
        menuButton.setFocusPainted(false);
        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightPanel.add(menuButton);
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(logo, BorderLayout.CENTER);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private void loadMatchedUsers() {
        matchedUsers.clear();
        String currentUserId = User.getCurrentUser().getUserId();
        
        try {
            // Get current user's matches
            Map<String, Boolean> matches = FirebaseConfig.get("matches/" + currentUserId, 
                new TypeToken<Map<String, Boolean>>(){}.getType());
            
            if (matches != null && !matches.isEmpty()) {
                Map<String, User> users = FirebaseConfig.get("users", 
                    new TypeToken<Map<String, User>>(){}.getType());
                
                for (String matchedUserId : matches.keySet()) {
                    if (matches.get(matchedUserId)) {  // Only if it's a true match
                        User matchedUser = users.get(matchedUserId);
                        if (matchedUser != null) {
                            MatchedUser user = new MatchedUser(
                                matchedUser.getUsername(),
                                "/ImagePicker/RajeshHamalPhoto.png", // Default image for now
                                matchedUserId
                            );
                            matchedUsers.add(user);
                        }
                    }
                }
            }
            
            // Update UI with matched users
            displayMatchedUsers();
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading matches: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void displayMatchedUsers() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        for (MatchedUser user : matchedUsers) {
            JPanel userPanel = createUserPanel(user);
            contentPanel.add(userPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        }
        
        if (matchedUsers.isEmpty()) {
            JLabel noMatchesLabel = new JLabel("No matches yet");
            noMatchesLabel.setFont(CHAT_FONT);
            noMatchesLabel.setForeground(Color.GRAY);
            noMatchesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(noMatchesLabel);
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createUserPanel(MatchedUser user) {
        // Main panel with rounded corners and shadow
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow (more subtle)
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, CARD_RADIUS, CARD_RADIUS);
                
                // Draw background
                g2.setColor(THEME_COLOR);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CARD_RADIUS, CARD_RADIUS);
                
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                // Control the height of the panel
                Dimension size = super.getPreferredSize();
                return new Dimension(size.width, 44); // Fixed height for consistency
            }
        };
        
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panel.setBackground(new Color(0, 0, 0, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(2, 2, 2, 2),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Profile picture (circular with white border)
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(user.imagePath));
            Image image = originalIcon.getImage().getScaledInstance(AVATAR_SIZE, AVATAR_SIZE, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(image)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Draw white border (thinner)
                    g2.setColor(Color.WHITE);
                    g2.fill(new Ellipse2D.Float(0, 0, getWidth(), getHeight()));
                    
                    // Draw image
                    g2.setClip(new Ellipse2D.Float(1, 1, getWidth() - 2, getHeight() - 2));
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            imageLabel.setPreferredSize(new Dimension(AVATAR_SIZE, AVATAR_SIZE));
            panel.add(imageLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Name label
        JLabel nameLabel = new JLabel(user.name.toUpperCase());
        nameLabel.setFont(CHAT_FONT);
        nameLabel.setForeground(TEXT_COLOR);
        panel.add(nameLabel);
        
        // Click listener
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openChat(user);
            }
        });
        
        return panel;
    }
    
    private void openChat(MatchedUser user) {
        SwingUtilities.invokeLater(() -> {
            MessageBox messageBox = new MessageBox(user.name, user.imagePath, user.userId);
            messageBox.setVisible(true);
        });
    }
    
    private void setupWindowDragging() {
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
        
        headerPanel.addMouseListener(dragListener);
        headerPanel.addMouseMotionListener(dragListener);
    }
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            ChatSystem frame = new ChatSystem();
            frame.setVisible(true);
        });
    }
} 