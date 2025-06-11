package messagesystem.view;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import messagesystem.model.User;
import messagesystem.view.components.RoundedButton;
import messagesystem.view.components.UserListWindow;

public class MainWindow extends JFrame {
    private JPanel matchesPanel;
    private JPanel sidePanel;
    
    // Sample users for demonstration
    private final User[] likedUsers = {
        new User("Sarah Smith", null),
        new User("John Doe", null),
        new User("Emily Johnson", null)
    };
    
    private final User[] savedUsers = {
        new User("Michael Brown", null),
        new User("Lisa Anderson", null),
        new User("David Wilson", null)
    };
    
    private final User[] myLikers = {
        new User("Jessica Lee", null),
        new User("Robert Taylor", null),
        new User("Amanda White", null)
    };
    
    public MainWindow() {
        setupWindow();
        initializeComponents();
    }
    
    private void setupWindow() {
        setTitle("HeartSync");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout(0, 0));
        
        // Header with gradient
        JPanel header = createGradientHeader();
        add(header, BorderLayout.NORTH);
        
        // Main content area with matches
        matchesPanel = new JPanel();
        matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));
        matchesPanel.setBackground(ColorScheme.CHAT_BACKGROUND);
        matchesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Add RAJESH DAI as a matched user
        JPanel matchCard = createMatchCard(new User("RAJESH DAI", null));
        matchesPanel.add(matchCard);
        matchesPanel.add(Box.createVerticalGlue());
        
        JScrollPane matchesScroll = new JScrollPane(matchesPanel);
        matchesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        matchesScroll.setBorder(null);
        add(matchesScroll, BorderLayout.CENTER);
        
        // Side panel with gradient
        sidePanel = createGradientSidePanel();
        add(sidePanel, BorderLayout.EAST);
    }
    
    private JPanel createGradientHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, ColorScheme.HEADER_FOOTER,
                    getWidth(), 0, new Color(169, 0, 169)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        header.setPreferredSize(new Dimension(getWidth(), 60));
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        
        JLabel logo = new JLabel("HeartSync");
        logo.setFont(new Font("Dialog", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        header.add(logo);
        
        return header;
    }
    
    private JPanel createGradientSidePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, ColorScheme.SIDE_PANEL,
                    0, getHeight(), new Color(255, 182, 193)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        panel.setPreferredSize(new Dimension(200, getHeight()));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        addSidePanelButton(panel, "❤️ Liked Users", () -> showLikedUsers());
        addSidePanelButton(panel, "🔖 Saved Users", () -> showSavedUsers());
        addSidePanelButton(panel, "🖤 My Likers", () -> showMyLikers());
        
        return panel;
    }
    
    private void showLikedUsers() {
        new UserListWindow("Liked Users", Arrays.asList(likedUsers)).setVisible(true);
    }
    
    private void showSavedUsers() {
        new UserListWindow("Saved Users", Arrays.asList(savedUsers)).setVisible(true);
    }
    
    private void showMyLikers() {
        new UserListWindow("My Likers", Arrays.asList(myLikers)).setVisible(true);
    }
    
    private JPanel createMatchCard(User user) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(new Color(240, 240, 240), 1, true)
        ));
        card.setBackground(Color.WHITE);
        
        // Profile picture placeholder with circular background
        JLabel profilePic = new JLabel("👤") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(ColorScheme.SIDE_PANEL);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                
                super.paintComponent(g);
            }
        };
        profilePic.setHorizontalAlignment(SwingConstants.CENTER);
        profilePic.setFont(new Font("Dialog", Font.PLAIN, 32));
        profilePic.setPreferredSize(new Dimension(60, 60));
        profilePic.setForeground(new Color(80, 80, 80));
        card.add(profilePic, BorderLayout.WEST);
        
        // Username
        JLabel username = new JLabel(user.getUsername());
        username.setFont(new Font("Dialog", Font.BOLD, 16));
        username.setBorder(new EmptyBorder(0, 10, 0, 0));
        card.add(username, BorderLayout.CENTER);
        
        // Add hover effect and click listener
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 250, 250));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                new ChatWindow(user).setVisible(true);
            }
        });
        
        return card;
    }
    
    private void addSidePanelButton(JPanel panel, String text, Runnable action) {
        RoundedButton button = new RoundedButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.addActionListener(e -> action.run());
        
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
} 