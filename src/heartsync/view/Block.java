/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

public class Block extends JFrame {
    private JPanel cardPanel;
    private JLabel avatarLabel;
    private JLabel nameLabel;
    private JLabel statusLabel;
    private JButton blockButton;
    private JList<UserProfile> profileList;
    private DefaultListModel<UserProfile> listModel;
    private List<UserProfile> users;

    // User profile class
    private static class UserProfile {
        String name;
        String initials;
        String status;
        Color avatarColor;
        public UserProfile(String name, String initials, String status, Color avatarColor) {
            this.name = name;
            this.initials = initials;
            this.status = status;
            this.avatarColor = avatarColor;
        }
        @Override
        public String toString() {
            return name;
        }
    }

    // Custom renderer for the profile list
    private static class ProfileListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof UserProfile) {
                UserProfile profile = (UserProfile) value;
                label.setText(profile.name);
                label.setIcon(createAvatarIcon(profile.initials, profile.avatarColor, 32));
                label.setIconTextGap(12);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                label.setBorder(new EmptyBorder(6, 8, 6, 8));
            }
            return label;
        }
    }

    // Helper to create a circular avatar icon
    private static Icon createAvatarIcon(String initials, Color color, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillOval(0, 0, size, size);
        g2.setColor(new Color(70, 120, 255));
        g2.setFont(new Font("Segoe UI", Font.BOLD, size / 2));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(initials);
        int textHeight = fm.getAscent();
        g2.drawString(initials, (size - textWidth) / 2, (size + textHeight / 2) / 2);
        g2.dispose();
        return new ImageIcon(image);
    }

    public Block() {
        // Set light pink background
        getContentPane().setBackground(new Color(255, 228, 235));
        setTitle("Block User");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        // Sample users
        users = new ArrayList<>();
        users.add(new UserProfile("John Doe", "JD", "Active Member", new Color(210, 225, 255)));
        users.add(new UserProfile("Emily Smith", "ES", "Active Member", new Color(255, 210, 220)));
        users.add(new UserProfile("Michael Lee", "ML", "Active Member", new Color(210, 255, 230)));
        users.add(new UserProfile("Sophia Brown", "SB", "Active Member", new Color(255, 245, 210)));
        users.add(new UserProfile("David Kim", "DK", "Active Member", new Color(220, 210, 255)));

        // Profile list setup
        listModel = new DefaultListModel<>();
        for (UserProfile user : users) listModel.addElement(user);
        profileList = new JList<>(listModel);
        profileList.setCellRenderer(new ProfileListRenderer());
        profileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        profileList.setBackground(Color.WHITE);
        profileList.setFixedCellHeight(48);
        profileList.setBorder(new EmptyBorder(10, 5, 10, 5));
        profileList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        profileList.setSelectedIndex(0);

        JScrollPane listScroll = new JScrollPane(profileList);
        listScroll.setPreferredSize(new Dimension(180, 320));
        listScroll.setBorder(BorderFactory.createEmptyBorder());

        // Card panel setup
        cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
        cardPanel.setPreferredSize(new Dimension(350, 350));
        cardPanel.setMaximumSize(new Dimension(400, 400));
        cardPanel.setMinimumSize(new Dimension(300, 300));
        cardPanel.setBackground(new Color(0,0,0,0));
        cardPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30),
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(230, 230, 230))
        ));

        // Avatar
        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(80, 80));
        avatarLabel.setMaximumSize(new Dimension(80, 80));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Name label
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(new Color(30, 32, 34));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(0, 0, 8, 0));

        // Status label
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        statusLabel.setForeground(new Color(140, 150, 160));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(new EmptyBorder(0, 0, 24, 0));

        // Block button
        blockButton = new JButton("Block User");
        blockButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        blockButton.setForeground(new Color(200, 0, 0));
        blockButton.setBackground(new Color(255, 235, 235));
        blockButton.setFocusPainted(false);
        blockButton.setBorder(BorderFactory.createEmptyBorder(12, 32, 12, 32));
        blockButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        blockButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        blockButton.setMaximumSize(new Dimension(180, 48));
        blockButton.setMinimumSize(new Dimension(120, 40));
        blockButton.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(12, 32, 12, 32)
        ));
        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserProfile selected = profileList.getSelectedValue();
                int confirm = JOptionPane.showConfirmDialog(Block.this,
                        "Are you sure you want to block " + selected.name + "?",
                        "Confirm Block",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(Block.this,
                            "User has been blocked successfully!",
                            "Block Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            }
        });

        // Add components to card panel
        cardPanel.add(avatarLabel);
        cardPanel.add(nameLabel);
        cardPanel.add(statusLabel);
        cardPanel.add(blockButton);

        // Split pane for list and card
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, cardPanel);
        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(2);
        splitPane.setBorder(null);
        splitPane.setBackground(new Color(255, 228, 235));

        // Main layout
        setLayout(new GridBagLayout());
        add(splitPane, new GridBagConstraints());

        // Update card when selection changes
        profileList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                UserProfile selected = profileList.getSelectedValue();
                if (selected != null) {
                    avatarLabel.setIcon(createAvatarIcon(selected.initials, selected.avatarColor, 80));
                    nameLabel.setText(selected.name);
                    statusLabel.setText(selected.status);
                }
            }
        });
        // Initialize card with first user
        UserProfile selected = profileList.getSelectedValue();
        if (selected != null) {
            avatarLabel.setIcon(createAvatarIcon(selected.initials, selected.avatarColor, 80));
            nameLabel.setText(selected.name);
            statusLabel.setText(selected.status);
        }

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Block().setVisible(true));
    }
}
