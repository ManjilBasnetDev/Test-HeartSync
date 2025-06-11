package messagesystem.view.components;

import messagesystem.model.User;
import messagesystem.view.ColorScheme;
import messagesystem.view.ChatWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class UserListWindow extends JFrame {
    protected JPanel usersPanel;
    protected String title;
    protected List<User> users;

    public UserListWindow(String title, List<User> users) {
        this.title = title;
        this.users = users;
        setupWindow();
        initializeComponents();
    }

    private void setupWindow() {
        setTitle(title);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(0, 0));

        // Header with gradient
        add(createGradientHeader(), BorderLayout.NORTH);

        // Users list
        usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        usersPanel.setBackground(ColorScheme.CHAT_BACKGROUND);
        usersPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (User user : users) {
            usersPanel.add(createUserCard(user));
            usersPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        JScrollPane scrollPane = new JScrollPane(usersPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
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
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel);
        
        return header;
    }

    protected JPanel createUserCard(User user) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
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
        profilePic.setPreferredSize(new Dimension(50, 50));
        profilePic.setForeground(new Color(80, 80, 80));
        card.add(profilePic, BorderLayout.WEST);
        
        // Username
        JLabel username = new JLabel(user.getUsername());
        username.setFont(new Font("Dialog", Font.BOLD, 16));
        username.setBorder(new EmptyBorder(0, 10, 0, 0));
        card.add(username, BorderLayout.CENTER);
        
        // Chat button
        RoundedButton chatButton = new RoundedButton("Chat");
        chatButton.addActionListener(e -> {
            new ChatWindow(user).setVisible(true);
        });
        card.add(chatButton, BorderLayout.EAST);
        
        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 250, 250));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });
        
        return card;
    }
} 