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
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

// Add these imports
import heartsync.view.Swipe;
import heartsync.view.MessageBox;

public class ChatSystem extends JFrame {
    private static final int WINDOW_RADIUS = 20;
    private static final Color BACKGROUND_COLOR = new Color(255, 216, 227); // Pink background
    private static final Color HEADER_COLOR = new Color(102, 0, 51); // Dark purple for header
    private static final Color MENU_BACKGROUND = new Color(219, 68, 134); // Pink for menu
    private static final Color TEXT_COLOR = new Color(51, 51, 51); // Dark gray for text
    
    private final JPanel mainPanel;
    private final JPanel headerPanel;
    private final JPanel menuPanel;
    private final JPanel contentPanel;
    private final ArrayList<MatchedUser> matchedUsers;
    
    private static class MatchedUser {
        String name;
        String imagePath;
        String lastMessage;
        
        MatchedUser(String name, String imagePath, String lastMessage) {
            this.name = name;
            this.imagePath = imagePath;
            this.lastMessage = lastMessage;
        }
    }
    
    public ChatSystem() {
        setTitle("HeartSync - Chat System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setUndecorated(true);
        
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
        
        // Header Panel
        headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Menu Panel (Right side)
        menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.EAST);
        
        // Content Panel (Main chat area)
        contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        
        // Initialize matched users
        matchedUsers = new ArrayList<>();
        setupSampleUsers();
        
        // Make window draggable
        setupWindowDragging();
        
        // Set window shape
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS));
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(getWidth(), 70));
        
        // Logo and back button panel
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);

        // Back button
        JButton backButton = new JButton("← Back");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(HEADER_COLOR);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                Swipe swipe = new Swipe();
                swipe.setVisible(true);
            });
        });
        leftPanel.add(backButton);
        
        // Logo
        JLabel logo = new JLabel("HeartSync");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        logo.setBorder(new EmptyBorder(10, 20, 10, 0));
        leftPanel.add(logo);
        
        header.add(leftPanel, BorderLayout.WEST);
        
        // Close button
        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(231, 76, 60));
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);
        header.add(buttonPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createMenuPanel() {
        JPanel menu = new JPanel();
        menu.setBackground(MENU_BACKGROUND);
        menu.setPreferredSize(new Dimension(250, getHeight()));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        
        // Add some padding at the top
        menu.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Menu items
        String[] menuItems = {"❤️ LIKED USERS", "🔖 SAVED USERS", "💝 MY LIKERS"};
        for (String item : menuItems) {
            JButton menuButton = new JButton(item);
            menuButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
            menuButton.setForeground(Color.WHITE);
            menuButton.setBackground(MENU_BACKGROUND);
            menuButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            menuButton.setMaximumSize(new Dimension(250, 50));
            menuButton.setFocusPainted(false);
            menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            menuButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    menuButton.setBackground(MENU_BACKGROUND.darker());
                }
                public void mouseExited(MouseEvent evt) {
                    menuButton.setBackground(MENU_BACKGROUND);
                }
            });
            
            menu.add(menuButton);
            menu.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        return menu;
    }
    
    private JPanel createContentPanel() {
        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        
        // Add matched users list here
        return content;
    }
    
    private void setupSampleUsers() {
        matchedUsers.add(new MatchedUser("Rajesh Dai", "/ImagePicker/RajeshHamalPhoto.png", "Hello! How are you?"));
        matchedUsers.add(new MatchedUser("Rajesh Hamal", "/ImagePicker/RajeshHamalPhoto2.jpg", "Nice to meet you!"));
        updateContentPanel();
    }
    
    private void updateContentPanel() {
        contentPanel.removeAll();
        
        for (MatchedUser user : matchedUsers) {
            JPanel userPanel = createUserChatPanel(user);
            contentPanel.add(userPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createUserChatPanel(MatchedUser user) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Make entire panel clickable
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openChat(user);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(245, 245, 245));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }
        });
        
        // User image
        try {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource(user.imagePath));
            Image image = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            panel.add(imageLabel, BorderLayout.WEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // User info
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(user.name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(TEXT_COLOR);
        
        JLabel messageLabel = new JLabel(user.lastMessage);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(Color.GRAY);
        
        infoPanel.add(nameLabel);
        infoPanel.add(messageLabel);
        panel.add(infoPanel, BorderLayout.CENTER);
        
        // Add a subtle arrow or indicator
        JLabel arrowLabel = new JLabel("→");
        arrowLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        arrowLabel.setForeground(new Color(200, 200, 200));
        arrowLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        panel.add(arrowLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void openChat(MatchedUser user) {
        SwingUtilities.invokeLater(() -> {
            MessageBox messageBox = new MessageBox(user.name, user.imagePath);
            messageBox.setVisible(true);
        });
    }
    
    private void setupWindowDragging() {
        MouseAdapter dragListener = new MouseAdapter() {
            private Point mouseDownCompCoords;
            
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }
            
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                mouseDownCompCoords = null;
            }
            
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
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