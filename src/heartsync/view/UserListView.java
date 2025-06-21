package heartsync.view;

import heartsync.database.DatabaseManagerProfile;
import heartsync.model.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class UserListView extends JPanel {

    private final List<String> userIds;
    private final String title;
    private final String currentUserId;

    public UserListView(List<String> userIds, String title, String currentUserId) {
        this.userIds = userIds;
        this.title = title;
        this.currentUserId = currentUserId;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel userGridPanel = new JPanel(new GridBagLayout());
        userGridPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(userGridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        loadUsers(userGridPanel);
    }

    private void loadUsers(JPanel panel) {
        // Use SwingWorker to load user data in the background
        new SwingWorker<List<UserProfile>, Void>() {
            @Override
            protected List<UserProfile> doInBackground() {
                return userIds.parallelStream()
                        .map(userId -> DatabaseManagerProfile.getInstance().getUserProfile(userId))
                        .filter(java.util.Objects::nonNull)
                        .collect(Collectors.toList());
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
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.insets = new Insets(10, 10, 10, 10);
                        gbc.gridx = 0;
                        gbc.gridy = 0;

                        for (UserProfile user : users) {
                            panel.add(createUserCard(user), gbc);
                            gbc.gridx++;
                            if (gbc.gridx % 3 == 0) {
                                gbc.gridx = 0;
                                gbc.gridy++;
                            }
                        }
                    }
                    panel.revalidate();
                    panel.repaint();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    // Handle error
                }
            }
        }.execute();
    }

    private JPanel createUserCard(UserProfile user) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        card.setPreferredSize(new Dimension(250, 300));
        card.setBackground(Color.WHITE);

        // Ideally, load image async
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(250, 200));
        if (user.getProfilePicPath() != null && !user.getProfilePicPath().isEmpty()) {
            // This can be slow, should be done in worker too
            ImageIcon icon = new ImageIcon(user.getProfilePicPath());
            Image img = icon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } else {
            imageLabel.setText("No Image");
        }
        card.add(imageLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel(user.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        infoPanel.add(nameLabel);

        if ("Matched Users".equals(title)) {
            JButton chatButton = new JButton("Chat");
            chatButton.addActionListener(e -> {
                 // TODO: Open a specific chat window with this user.
                 // The current ChatSystem opens a list of all chats.
                 // This needs to be refactored to support direct chats.
                 new ChatSystem().setVisible(true);
            });
            infoPanel.add(chatButton);
        }

        card.add(infoPanel, BorderLayout.SOUTH);
        return card;
    }
} 