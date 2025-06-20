/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package heartsync.view;

import heartsync.dao.UserRegisterDAO;
import heartsync.model.User;
import heartsync.database.FirebaseStorageManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;

/**
 *
 * @author HP
 */
public class ReviewAndApprove extends JPanel {

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color VERIFIED_COLOR = new Color(46, 204, 113);
    private static final Color REJECT_COLOR = new Color(231, 76, 60);
    private static final int CARD_RADIUS = 15;
    
    private JPanel usersPanel;
    private UserRegisterDAO userDAO;

    /**
     * Creates new form ReviewAndApprove
     */
    public ReviewAndApprove() {
        userDAO = new UserRegisterDAO();
        setupUI();
        refreshUsers();
    }

    private void setupUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(BACKGROUND_COLOR);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 0, 20));
        
        JLabel titleLabel = new JLabel("Review & Approve Users");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton refreshButton = createStyledButton("Refresh", PRIMARY_COLOR);
        refreshButton.addActionListener(e -> refreshUsers());
        headerPanel.add(refreshButton, BorderLayout.EAST);
        
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
    
    private void refreshUsers() {
        usersPanel.removeAll();
        List<User> users = userDAO.getAllUsers();
        
        for (User user : users) {
            String userType = user.getUserType();
            // Only show users that need review
            if (userType == null || (!userType.equalsIgnoreCase("admin") && 
                !userType.equalsIgnoreCase("verified") && 
                !userType.equalsIgnoreCase("rejected"))) {
                addUserPanel(user);
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
        
        // Profile picture
        JLabel profilePic = new JLabel();
        profilePic.setPreferredSize(new Dimension(100, 100));
        profilePic.setMaximumSize(new Dimension(100, 100));
        profilePic.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Try to load profile image from Firebase
        try {
            String imageUrl = FirebaseStorageManager.getProfileImageUrl(user.getUserId());
            if (imageUrl != null && !imageUrl.isEmpty()) {
                URL url = new URL(imageUrl);
                BufferedImage originalImage = ImageIO.read(url);
                
                // Create circular mask
                BufferedImage circularImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circularImage.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 100, 100));
                g2.drawImage(originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH), 0, 0, null);
                g2.dispose();
                
                profilePic.setIcon(new ImageIcon(circularImage));
            } else {
                // Use default image if no profile picture
                ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/ImagePicker/RajeshHamalPhoto.png"));
                Image img = defaultIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                
                // Create circular mask for default image
                BufferedImage circularImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circularImage.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 100, 100));
                g2.drawImage(img, 0, 0, null);
                g2.dispose();
                
                profilePic.setIcon(new ImageIcon(circularImage));
            }
        } catch (Exception e) {
            // Use default image on error
            ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/ImagePicker/RajeshHamalPhoto.png"));
            Image img = defaultIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            
            // Create circular mask for default image
            BufferedImage circularImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circularImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 100, 100));
            g2.drawImage(img, 0, 0, null);
            g2.dispose();
            
            profilePic.setIcon(new ImageIcon(circularImage));
        }
        
        userPanel.add(profilePic);
        userPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Name panel to hold name and verified badge
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        namePanel.setBackground(CARD_COLOR);
        
        // Full name with larger font
        String displayName = user.getFullName() != null && !user.getFullName().isEmpty() 
            ? user.getFullName() 
            : user.getUsername();
        JLabel nameLabel = new JLabel(displayName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(Color.BLACK);
        namePanel.add(nameLabel);
        
        // Add verified badge if user is verified
        if (user.isVerified()) {
            JLabel verifiedBadge = new JLabel("✓");
            verifiedBadge.setFont(new Font("Segoe UI", Font.BOLD, 16));
            verifiedBadge.setForeground(VERIFIED_COLOR);
            namePanel.add(verifiedBadge);
        }
        
        userPanel.add(namePanel);
        
        // Username in smaller font
        JLabel usernameLabel = new JLabel("@" + user.getUsername());
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPanel.add(usernameLabel);
        
        // Email if available
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            JLabel emailLabel = new JLabel(user.getEmail());
            emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            emailLabel.setForeground(Color.BLACK);
            emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            userPanel.add(emailLabel);
        }
        
        userPanel.add(Box.createVerticalStrut(15));
        
        // Status
        JLabel statusLabel = new JLabel("Pending Review");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPanel.add(statusLabel);
        
        userPanel.add(Box.createVerticalStrut(15));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(CARD_COLOR);
        
        JButton approveBtn = createStyledButton("Approve", PRIMARY_COLOR);
        approveBtn.addActionListener(e -> handleApprove(user, userPanel));
        buttonPanel.add(approveBtn);
        
        JButton rejectBtn = createStyledButton("Reject", REJECT_COLOR);
        rejectBtn.addActionListener(e -> handleReject(user, userPanel));
        buttonPanel.add(rejectBtn);
        
        userPanel.add(buttonPanel);
        usersPanel.add(userPanel);
    }
    
    private void handleApprove(User user, JPanel userPanel) {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to approve user '" + user.getUsername() + "'?",
            "Confirm Approval",
            JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            if (userDAO.verifyUser(user.getUserId())) {
                // Remove the panel with animation
                fadeOutPanel(userPanel);
                JOptionPane.showMessageDialog(this,
                    "User '" + user.getUsername() + "' has been approved.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to approve user. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleReject(User user, JPanel userPanel) {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to reject user '" + user.getUsername() + "'?",
            "Confirm Rejection",
            JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            if (userDAO.rejectUser(user.getUserId())) {
                // Remove the panel with animation
                fadeOutPanel(userPanel);
                JOptionPane.showMessageDialog(this,
                    "User '" + user.getUsername() + "' has been rejected.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to reject user. Please try again.",
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
        button.setPreferredSize(new Dimension(100, 35));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReviewAndApprove.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReviewAndApprove.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReviewAndApprove.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReviewAndApprove.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReviewAndApprove().setVisible(true);
            }
        });
    }
}