import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame {
    private JPanel matchesPanel;
    private JPanel sidePanel;
    
    public MainWindow() {
        setupWindow();
        initializeComponents();
    }
    
    private void setupWindow() {
        setTitle("HeartSync");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(ColorScheme.HEADER_FOOTER);
        header.setPreferredSize(new Dimension(getWidth(), 60));
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        
        JLabel logo = new JLabel("HeartSync");
        logo.setFont(new Font("Dialog", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);
        header.add(logo);
        
        add(header, BorderLayout.NORTH);
        
        // Main content area with matches
        matchesPanel = new JPanel();
        matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));
        matchesPanel.setBackground(ColorScheme.CHAT_BACKGROUND);
        
        // Add RAJESH DAI as a matched user
        JPanel matchCard = createMatchCard(new User("RAJESH DAI", null));
        matchesPanel.add(matchCard);
        matchesPanel.add(Box.createVerticalGlue());
        
        JScrollPane matchesScroll = new JScrollPane(matchesPanel);
        matchesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(matchesScroll, BorderLayout.CENTER);
        
        // Side panel
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(200, getHeight()));
        sidePanel.setBackground(ColorScheme.SIDE_PANEL);
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        addSidePanelButton("❤️ Liked Users");
        addSidePanelButton("🔖 Saved Users");
        addSidePanelButton("🖤 My Likers");
        
        add(sidePanel, BorderLayout.EAST);
    }
    
    private JPanel createMatchCard(User user) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setBackground(Color.WHITE);
        
        // Profile picture placeholder
        JLabel profilePic = new JLabel("👤");
        profilePic.setFont(new Font("Dialog", Font.PLAIN, 32));
        card.add(profilePic, BorderLayout.WEST);
        
        // Username
        JLabel username = new JLabel(user.getUsername());
        username.setFont(new Font("Dialog", Font.BOLD, 16));
        card.add(username, BorderLayout.CENTER);
        
        // Add hover effect and click listener
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 245, 245));
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
    
    private void addSidePanelButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(ColorScheme.BUTTON_BACKGROUND);
        button.setForeground(ColorScheme.BUTTON_TEXT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ColorScheme.BUTTON_BACKGROUND);
            }
        });
        
        sidePanel.add(button);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
} 