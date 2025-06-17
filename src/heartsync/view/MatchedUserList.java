package logintestfinal.View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

public class MatchedUserList extends JFrame {
    private JPanel mainPanel;
    private JList<UserProfile> matchedUsersList;
    private DefaultListModel<UserProfile> listModel;
    private List<UserProfile> users;
    private JButton viewProfileButton;
    private JButton messageButton;
    private JButton backButton;
    private JLabel detailsLabel;

    // User profile class
    private static class UserProfile {
        String name;
        String initials;
        String details;
        Color avatarColor;
        public UserProfile(String name, String initials, String details, Color avatarColor) {
            this.name = name;
            this.initials = initials;
            this.details = details;
            this.avatarColor = avatarColor;
        }
        @Override
        public String toString() {
            return name;
        }
    }

    // Custom renderer for the matched user list
    private static class UserProfileRenderer extends JPanel implements ListCellRenderer<UserProfile> {
        private final JLabel avatarLabel = new JLabel();
        private final JLabel nameLabel = new JLabel();
        private final JLabel detailsLabel = new JLabel();
        public UserProfileRenderer() {
            setLayout(new BorderLayout(10, 0));
            setOpaque(true);
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            nameLabel.setForeground(new Color(30, 32, 34));
            detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            detailsLabel.setForeground(new Color(120, 130, 140));
            textPanel.add(nameLabel);
            textPanel.add(detailsLabel);
            add(avatarLabel, BorderLayout.WEST);
            add(textPanel, BorderLayout.CENTER);
            setBorder(new EmptyBorder(12, 16, 12, 16));
        }
        @Override
        public Component getListCellRendererComponent(JList<? extends UserProfile> list, UserProfile value, int index, boolean isSelected, boolean cellHasFocus) {
            avatarLabel.setIcon(createAvatarIcon(value.initials, value.avatarColor, 48));
            nameLabel.setText(value.name);
            detailsLabel.setText(value.details);
            setBackground(isSelected ? new Color(255, 228, 235) : Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(isSelected ? new Color(255, 182, 193) : new Color(240, 240, 240), 2, true),
                new EmptyBorder(12, 16, 12, 16)));
            return this;
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

    public MatchedUserList() {
        getContentPane().setBackground(new Color(255, 228, 235));
        setTitle("Matched Users");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        // Sample users
        users = new ArrayList<>();
        users.add(new UserProfile("Sarah Johnson", "SJ", "25, New York", new Color(210, 225, 255)));
        users.add(new UserProfile("Michael Brown", "MB", "28, Los Angeles", new Color(255, 210, 220)));
        users.add(new UserProfile("Emma Wilson", "EW", "24, Chicago", new Color(210, 255, 230)));
        users.add(new UserProfile("David Lee", "DL", "27, Boston", new Color(255, 245, 210)));
        users.add(new UserProfile("Sophia Chen", "SC", "26, San Francisco", new Color(220, 210, 255)));

        // List setup
        listModel = new DefaultListModel<>();
        for (UserProfile user : users) listModel.addElement(user);
        matchedUsersList = new JList<>(listModel);
        matchedUsersList.setCellRenderer(new UserProfileRenderer());
        matchedUsersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        matchedUsersList.setBackground(new Color(255, 245, 250));
        matchedUsersList.setFixedCellHeight(80);
        matchedUsersList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        matchedUsersList.setSelectedIndex(0);
        JScrollPane listScroll = new JScrollPane(matchedUsersList);
        listScroll.setPreferredSize(new Dimension(420, 400));
        listScroll.setBorder(BorderFactory.createEmptyBorder());

        // Card panel for the list
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.setPreferredSize(new Dimension(500, 500));
        mainPanel.setMaximumSize(new Dimension(600, 600));
        mainPanel.setMinimumSize(new Dimension(400, 400));
        mainPanel.setBackground(new Color(0,0,0,0));
        mainPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30),
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(230, 230, 230))
        ));
        JLabel titleLabel = new JLabel("Your Matches");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(30, 32, 34));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 24, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(listScroll, BorderLayout.CENTER);

        // Details panel with buttons
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(new EmptyBorder(16, 0, 0, 0));
        detailsLabel = new JLabel();
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        detailsLabel.setForeground(new Color(120, 130, 140));
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create a panel for horizontal button layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        
        viewProfileButton = new JButton("View Profile");
        messageButton = new JButton("Message");
        backButton = new JButton("Back");
        
        styleButton(viewProfileButton, new Color(70, 120, 255));
        styleButton(messageButton, new Color(255, 182, 193));
        styleButton(backButton, new Color(200, 200, 200));
        
        // Add buttons to the horizontal panel
        buttonPanel.add(viewProfileButton);
        buttonPanel.add(messageButton);
        buttonPanel.add(backButton);
        
        detailsPanel.add(detailsLabel);
        detailsPanel.add(Box.createVerticalStrut(16));
        detailsPanel.add(buttonPanel);
        
        mainPanel.add(detailsPanel, BorderLayout.SOUTH);

        // List selection listener to update details panel
        matchedUsersList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                UserProfile selected = matchedUsersList.getSelectedValue();
                if (selected != null) {
                    detailsLabel.setText(selected.name + " — " + selected.details);
                    viewProfileButton.setEnabled(true);
                    messageButton.setEnabled(true);
                } else {
                    detailsLabel.setText("");
                    viewProfileButton.setEnabled(false);
                    messageButton.setEnabled(false);
                }
            }
        });
        // Initialize details panel
        UserProfile selected = matchedUsersList.getSelectedValue();
        if (selected != null) {
            detailsLabel.setText(selected.name + " — " + selected.details);
            viewProfileButton.setEnabled(true);
            messageButton.setEnabled(true);
        } else {
            detailsLabel.setText("");
            viewProfileButton.setEnabled(false);
            messageButton.setEnabled(false);
        }

        // Button actions
        viewProfileButton.addActionListener(e -> {
            UserProfile sel = matchedUsersList.getSelectedValue();
            if (sel != null) {
                JOptionPane.showMessageDialog(this,
                        "Profile of " + sel.name + "\nDetails: " + sel.details,
                        "View Profile",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        messageButton.addActionListener(e -> {
            UserProfile sel = matchedUsersList.getSelectedValue();
            if (sel != null) {
                JOptionPane.showMessageDialog(this,
                        "Starting chat with " + sel.name + "...",
                        "Message",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        backButton.addActionListener(e -> dispose());

        // Center the card panel in the frame
        setLayout(new GridBagLayout());
        add(mainPanel, new GridBagConstraints());

        pack();
        setLocationRelativeTo(null);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color.darker(), 1, true),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MatchedUserList().setVisible(true));
    }
} 