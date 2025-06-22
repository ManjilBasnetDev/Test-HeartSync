package heartsync.view;

import heartsync.dao.UserRegisterDAO;
import heartsync.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.List;
import java.awt.geom.RoundRectangle2D;

public class DeleteUserPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color DELETE_COLOR = new Color(231, 76, 60);
    private static final int CARD_RADIUS = 15;
    
    private JPanel usersPanel;
    private UserRegisterDAO userDAO;
    private JTextField searchField;
    
    public DeleteUserPanel() {
        userDAO = new UserRegisterDAO();
        setupUI();
        loadUsers();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BACKGROUND_COLOR);
        
        // Header panel with search
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 0, 20));
        
        JLabel titleLabel = new JLabel("Delete User Accounts");
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
        refreshButton.addActionListener(e -> loadUsers());
        
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
    
    private void loadUsers() {
        usersPanel.removeAll();
        List<User> users = userDAO.getAllUsers();
        
        for (User user : users) {
            // Don't show admin users in the delete list
            if (user.getUserType() == null || !user.getUserType().equalsIgnoreCase("admin")) {
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
            if (user.getUserType() == null || !user.getUserType().equalsIgnoreCase("admin")) {
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
        
        // User type status
        String status = user.getUserType() != null ? user.getUserType() : "Regular User";
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPanel.add(statusLabel);
        
        userPanel.add(Box.createVerticalStrut(15));
        
        // Delete button
        JButton deleteBtn = createStyledButton("Delete Account", DELETE_COLOR);
        deleteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteBtn.addActionListener(e -> handleDelete(user, userPanel));
        userPanel.add(deleteBtn);
        
        usersPanel.add(userPanel);
    }
    
    private void handleDelete(User user, JPanel userPanel) {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to permanently delete the account for '" + user.getUsername() + "'?\n" +
            "This will remove:\n" +
            "• User profile and all personal data\n" +
            "• All likes given and received\n" +
            "• All matches and conversations\n" +
            "• All notifications\n\n" +
            "This action cannot be undone.",
            "Confirm Account Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            // Show progress dialog
            JDialog progressDialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Deleting User", true);
            JLabel progressLabel = new JLabel("Deleting user account and all associated data...");
            progressLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
            progressDialog.add(progressLabel);
            progressDialog.pack();
            progressDialog.setLocationRelativeTo(this);
            
            // Perform deletion in background thread
            SwingWorker<Boolean, Void> deleteWorker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return userDAO.deleteUser(user.getUsername());
                }
                
                @Override
                protected void done() {
                    progressDialog.dispose();
                    try {
                        boolean success = get();
                        if (success) {
                            // Remove the panel with animation
                            fadeOutPanel(userPanel);
                            JOptionPane.showMessageDialog(DeleteUserPanel.this,
                                "User account '" + user.getUsername() + "' and all associated data have been deleted successfully.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            String errorMessage = "Failed to delete user account.";
                            if (userDAO.getLastError() != null && !userDAO.getLastError().isEmpty()) {
                                errorMessage += "\nError: " + userDAO.getLastError();
                            }
                            errorMessage += "\n\nPlease try again or check the console for more details.";
                            
                            JOptionPane.showMessageDialog(DeleteUserPanel.this,
                                errorMessage,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        progressDialog.dispose();
                        JOptionPane.showMessageDialog(DeleteUserPanel.this,
                            "An unexpected error occurred while deleting the user:\n" + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            };
            
            deleteWorker.execute();
            progressDialog.setVisible(true);
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