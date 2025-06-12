package heartsync.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import heartsync.controller.LikeController;
import heartsyncdatingapp.model.User;

public class MyLikers extends JFrame {
    private final JPanel mainPanel;
    private final JScrollPane scrollPane;
    private final JPanel likersPanel;
    private final LikeController likeController;
    private final int currentUserId;
    
    public MyLikers(int userId) {
        this.currentUserId = userId;
        this.likeController = new LikeController();
        this.mainPanel = new JPanel();
        this.likersPanel = new JPanel();
        this.scrollPane = new JScrollPane(likersPanel);
        
        initializeUI();
        refreshLikersList();
    }
    
    private void initializeUI() {
        setTitle("HeartSync - Users Who Liked You");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(255, 192, 203));
        
        JLabel headerLabel = new JLabel("Users Who Liked You");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        likersPanel.setLayout(new BoxLayout(likersPanel, BoxLayout.Y_AXIS));
        likersPanel.setBackground(Color.WHITE);
        
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void refreshLikersList() {
        List<User> likers = likeController.getLikers(currentUserId);
        
        likersPanel.removeAll();
        
        if (likers.isEmpty()) {
            JLabel emptyLabel = new JLabel("No one has liked your profile yet");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            likersPanel.add(emptyLabel);
        } else {
            for (User liker : likers) {
                JPanel userPanel = createUserPanel(liker);
                likersPanel.add(userPanel);
                likersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        likersPanel.revalidate();
        likersPanel.repaint();
    }
    
    private JPanel createUserPanel(User user) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 192, 203), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel usernameLabel = new JLabel(user.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel infoLabel = new JLabel();
        if (user.getBio() != null && !user.getBio().isEmpty()) {
            infoLabel.setText(user.getBio());
        } else if (user.getInterests() != null && !user.getInterests().isEmpty()) {
            infoLabel.setText("Interests: " + user.getInterests());
        }
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        infoPanel.add(usernameLabel);
        infoPanel.add(infoLabel);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonsPanel.setBackground(Color.WHITE);
        
        JButton viewProfileBtn = new JButton("View Profile");
        viewProfileBtn.setBackground(new Color(255, 192, 203));
        viewProfileBtn.setForeground(Color.WHITE);
        viewProfileBtn.addActionListener(e -> handleViewProfile(user.getId()));
        
        buttonsPanel.add(viewProfileBtn);
        
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.EAST);
        
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleViewProfile(user.getId());
            }
        });
        
        return panel;
    }
    
    private void handleViewProfile(int userId) {
        // TODO: Implement navigation to user profile
        System.out.println("Viewing profile of user with ID: " + userId);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MyLikers myLikers = new MyLikers(1); // Test with user ID 1
            myLikers.setVisible(true);
        });
    }
}
