package heartsync.view;

import heartsync.database.DatabaseManagerProfile;
import heartsync.model.UserProfile;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import heartsync.dao.LikeDAO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserCardListView extends JPanel {

    private final List<String> userIds;
    private final String title;
    private final String currentUserId;

    public UserCardListView(List<String> userIds, String title, String currentUserId) {
        this.userIds = userIds;
        this.title = title;
        this.currentUserId = currentUserId;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(236, 240, 241));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel userGridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        userGridPanel.setBackground(new Color(236, 240, 241));
        userGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(userGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        loadUsers(userGridPanel);
    }

    private void loadUsers(JPanel panel) {
        new SwingWorker<List<UserProfile>, Void>() {
            @Override
            protected List<UserProfile> doInBackground() throws Exception {
                return userIds.stream()
                        .map(userId -> DatabaseManagerProfile.getInstance().getUserProfile(userId))
                        .filter(java.util.Objects::nonNull)
                        .toList();
            }

            @Override
            protected void done() {
                try {
                    List<UserProfile> users = get();
                    if (users.isEmpty()) {
                        panel.setLayout(new GridBagLayout());
                        JLabel emptyLabel = new JLabel("No users to display.");
                        emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                        panel.add(emptyLabel);
                    } else {
                        for (UserProfile user : users) {
                            panel.add(createUserCard(user));
                        }
                    }
                    panel.revalidate();
                    panel.repaint();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private JPanel createUserCard(UserProfile user) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(220, 280));
        
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(100, 100));
        imageLabel.setMaximumSize(new Dimension(100, 100));
        
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                Image image = null;
                String firebaseUrl = null;

                // 1. Try to get image URL from Firebase Storage
                try {
                    firebaseUrl = heartsync.database.FirebaseStorageManager.getProfileImageUrl(user.getUsername());
                    if (firebaseUrl != null && !firebaseUrl.isEmpty()) {
                        image = ImageIO.read(new URL(firebaseUrl));
                    }
                } catch (Exception e) {
                    System.err.println("Failed to load image from Firebase for " + user.getUsername() + ": " + e.getMessage());
                }

                // 2. Fallback to local path if Firebase fails
                if (image == null) {
                    String picPath = user.getProfilePicPath();
                    if (picPath != null && !picPath.isEmpty()) {
                        if (picPath.startsWith("http")) { // In case the path is a URL itself
                            image = ImageIO.read(new URL(picPath));
                        } else {
                            URL resourceUrl = getClass().getResource("/ImagePicker/" + picPath);
                            if (resourceUrl != null) image = ImageIO.read(resourceUrl);
                        }
                    }
                }
                
                // 3. Fallback to default image if all else fails
                if (image == null) {
                    image = ImageIO.read(getClass().getResource("/ImagePicker/RajeshHamalPhoto.png"));
                }
                
                Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                BufferedImage circularImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circularImage.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new Ellipse2D.Float(0, 0, 100, 100));
                g2.drawImage(scaledImage, 0, 0, null);
                g2.dispose();
                return new ImageIcon(circularImage);
            }

            @Override
            protected void done() {
                try {
                    imageLabel.setIcon(get());
                } catch (Exception e) {
                    e.printStackTrace();
                    // Optionally set a default error icon
                }
            }
        }.execute();

        card.add(imageLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel nameLabel = new JLabel(user.getFullName());
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        card.add(nameLabel);

        card.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel usernameLabel = new JLabel("@" + user.getUsername());
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(usernameLabel);
        
        card.add(Box.createVerticalStrut(15));
        
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new UserProfileView(user).setVisible(true);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        LikeDAO likeDAO = new LikeDAO();

        if ("Matched Users".equals(title)) {
            JButton chatButton = createStyledButton("Chat", new Color(41, 128, 185));
            chatButton.addActionListener(e -> {
                 new ChatSystem().setVisible(true);
                 SwingUtilities.getWindowAncestor(this).dispose();
            });
            buttonPanel.add(chatButton);

            JButton dislikeButton = createStyledButton("Dislike", new Color(231, 76, 60));
            dislikeButton.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to dislike " + user.getFullName() + "?",
                    "Confirm Dislike",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    likeDAO.removeLike(currentUserId, user.getUsername());
                    card.getParent().remove(card);
                    card.getParent().revalidate();
                    card.getParent().repaint();
                }
            });
            buttonPanel.add(dislikeButton);
        } else if ("My Likes".equals(title)) {
            JButton dislikeButton = createStyledButton("Dislike", new Color(231, 76, 60));
            dislikeButton.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove this user from your likes?",
                    "Confirm Dislike",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    likeDAO.removeLike(currentUserId, user.getUsername());
                    card.getParent().remove(card);
                    card.getParent().revalidate();
                    card.getParent().repaint();
                }
            });
            buttonPanel.add(dislikeButton);
        } else if ("My Likers".equals(title)) {
            JButton likeBackButton = createStyledButton("Like Back", new Color(46, 204, 113));
            likeBackButton.addActionListener(e -> {
                likeDAO.addLike(currentUserId, user.getUsername());
                card.getParent().remove(card);
                card.getParent().revalidate();
                card.getParent().repaint();
            });
            buttonPanel.add(likeBackButton);

            JButton passButton = createStyledButton("Pass", new Color(231, 76, 60));
            passButton.addActionListener(e -> {
                likeDAO.addPass(currentUserId, user.getUsername());
                card.getParent().remove(card);
                card.getParent().revalidate();
                card.getParent().repaint();
            });
            buttonPanel.add(passButton);
        }
        
        card.add(buttonPanel);

        return card;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(90, 30));
        
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(c.getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, c.getWidth(), c.getHeight(), 10, 10));
                super.paint(g2d, c);
                g2d.dispose();
            }
        });

        return button;
    }
} 