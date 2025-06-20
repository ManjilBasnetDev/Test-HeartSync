package heartsync.view;

import heartsync.dao.UserRegisterDAO;
import heartsync.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.List;
import java.awt.geom.RoundRectangle2D;

public class ReportedUsersPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final int CARD_RADIUS = 15;
    
    private JPanel usersPanel;
    private UserRegisterDAO userDAO;
    private JTextField searchField;
    
    public ReportedUsersPanel() {
        userDAO = new UserRegisterDAO();
        setupUI();
        loadReportedUsers();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BACKGROUND_COLOR);
        
        // Header panel with search
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 0, 20));
        
        JLabel titleLabel = new JLabel("Reported Users");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(BACKGROUND_COLOR);
        
        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterUsers(searchField.getText());
            }
        });
        
        JButton refreshButton = createStyledButton("Refresh", PRIMARY_COLOR);
        refreshButton.addActionListener(e -> loadReportedUsers());
        
        searchPanel.add(searchField);
        searchPanel.add(refreshButton);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Users grid panel
        usersPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        usersPanel.setBackground(BACKGROUND_COLOR);
        usersPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(usersPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadReportedUsers() {
        usersPanel.removeAll();
        List<User> users = userDAO.getAllUsers();
        
        for (User user : users) {
            // Show only reported users
            if (user.getUserType() != null && user.getUserType().equalsIgnoreCase("reported")) {
                addUserPanel(user);
            }
        }
        
        usersPanel.revalidate();
        usersPanel.repaint();
    }
    
    private void filterUsers(String searchText) {
        usersPanel.removeAll();
        List<User> users = userDAO.getAllUsers();
        
        String searchLower = searchText.toLowerCase();
        for (User user : users) {
            if (user.getUserType() != null && user.getUserType().equalsIgnoreCase("reported")) {
                if (searchText.isEmpty() ||
                    user.getUsername().toLowerCase().contains(searchLower) ||
                    (user.getFullName() != null && user.getFullName().toLowerCase().contains(searchLower)) ||
                    (user.getEmail() != null && user.getEmail().toLowerCase().contains(searchLower))) {
                    addUserPanel(user);
                }
            }
        }
        
        usersPanel.revalidate();
        usersPanel.repaint();
    }
    
    private void addUserPanel(User user) {
        JPanel userPanel = createRoundedPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(CARD_COLOR);
        userPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Name panel
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        namePanel.setBackground(CARD_COLOR);
        
        String displayName = user.getFullName() != null && !user.getFullName().isEmpty() 
            ? user.getFullName() 
            : user.getUsername();
        JLabel nameLabel = new JLabel(displayName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(Color.BLACK);
        namePanel.add(nameLabel);
        
        userPanel.add(namePanel);
        
        // Username
        JLabel usernameLabel = new JLabel("@" + user.getUsername());
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPanel.add(usernameLabel);
        
        // Email
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            JLabel emailLabel = new JLabel(user.getEmail());
            emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            emailLabel.setForeground(Color.BLACK);
            emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            userPanel.add(emailLabel);
        }
        
        userPanel.add(Box.createVerticalStrut(15));
        
        // Report status
        JLabel statusLabel = new JLabel("⚠ Reported User");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(WARNING_COLOR);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPanel.add(statusLabel);
        
        userPanel.add(Box.createVerticalStrut(15));
        
        // Action buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(CARD_COLOR);
        
        JButton clearBtn = createStyledButton("Clear Report", PRIMARY_COLOR);
        clearBtn.addActionListener(e -> handleClearReport(user, userPanel));
        buttonPanel.add(clearBtn);
        
        JButton banBtn = createStyledButton("Ban User", DANGER_COLOR);
        banBtn.addActionListener(e -> handleBanUser(user, userPanel));
        buttonPanel.add(banBtn);
        
        userPanel.add(buttonPanel);
        usersPanel.add(userPanel);
    }
    
    private void handleClearReport(User user, JPanel userPanel) {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to clear the report for user '" + user.getUsername() + "'?",
            "Confirm Clear Report",
            JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            user.setUserType("verified");
            if (userDAO.updateUser(user)) {
                fadeOutPanel(userPanel);
                JOptionPane.showMessageDialog(this,
                    "Report cleared for user '" + user.getUsername() + "'.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to clear report. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleBanUser(User user, JPanel userPanel) {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to ban user '" + user.getUsername() + "'?\n" +
            "This will permanently delete their account.",
            "Confirm Ban User",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            if (userDAO.deleteUser(user.getUsername())) {
                fadeOutPanel(userPanel);
                JOptionPane.showMessageDialog(this,
                    "User '" + user.getUsername() + "' has been banned and their account deleted.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to ban user. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void fadeOutPanel(JPanel panel) {
        Timer timer = new Timer(20, null);
        float opacity = 1.0f;
        
        timer.addActionListener(new ActionListener() {
            float currentOpacity = opacity;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                currentOpacity -= 0.1f;
                panel.setOpaque(false);
                panel.setBackground(new Color(1f, 1f, 1f, currentOpacity));
                panel.repaint();
                
                if (currentOpacity <= 0) {
                    timer.stop();
                    usersPanel.remove(panel);
                    usersPanel.revalidate();
                    usersPanel.repaint();
                }
            }
        });
        
        timer.start();
    }
    
    private JPanel createRoundedPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CARD_RADIUS, CARD_RADIUS));
                g2.dispose();
            }
        };
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? color.darker() : color);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
} 