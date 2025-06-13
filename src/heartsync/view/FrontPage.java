package heartsync.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class FrontPage extends JFrame {
    // Dummy matched user class
    static class MatchedUser {
        int id;
        String name;
        ImageIcon profilePic;
        MatchedUser(int id, String name, ImageIcon profilePic) {
            this.id = id;
            this.name = name;
            this.profilePic = profilePic;
        }
    }

    private int currentUserId = 1; // Placeholder for logged-in user

    public FrontPage() {
        setTitle("HeartSync - Front Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250, 245, 250));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(128, 0, 64));
        header.setPreferredSize(new Dimension(900, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(233, 54, 128)));
        JLabel logo = new JLabel();
        logo.setIcon(new ImageIcon(new BufferedImage(60, 60, BufferedImage.TYPE_INT_RGB))); // Placeholder
        logo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        JLabel appName = new JLabel("HEARTSYNC");
        appName.setFont(new Font("SansSerif", Font.BOLD, 36));
        appName.setForeground(Color.PINK);
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setOpaque(false);
        logoPanel.add(logo);
        logoPanel.add(appName);
        header.add(logoPanel, BorderLayout.WEST);
        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 28));
        backBtn.setBackground(new Color(128, 0, 64));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setOpaque(true);
        backBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        header.add(backBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Sidebar/Menu
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(233, 54, 128));
        sidebar.setPreferredSize(new Dimension(250, 350));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        sidebar.setOpaque(true);
        sidebar.setAlignmentY(Component.TOP_ALIGNMENT);
        sidebar.setMaximumSize(new Dimension(250, 400));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30, 20, 30, 20),
                BorderFactory.createLineBorder(new Color(128, 0, 64), 2, true)));
        JLabel menuTitle = new JLabel("HeartSync");
        menuTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        menuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(menuTitle);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(menuItem("❤", "LIKED USERS", Color.RED));
        sidebar.add(menuItem("🔖", "SAVED USERS", new Color(128, 0, 64)));
        sidebar.add(menuItem("🖤", "MY LIKERS", Color.BLACK));
        add(sidebar, BorderLayout.EAST);

        // Center: Matched users list
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(250, 245, 250));
        JLabel matchedLabel = new JLabel("Matched Users");
        matchedLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        matchedLabel.setForeground(new Color(128, 0, 64));
        matchedLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JPanel matchedListPanel = new JPanel();
        matchedListPanel.setLayout(new BoxLayout(matchedListPanel, BoxLayout.Y_AXIS));
        matchedListPanel.setBackground(new Color(250, 245, 250));
        matchedListPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(matchedListPanel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(420, 350));
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Dummy matched users
        List<MatchedUser> matchedUsers = new ArrayList<>();
        for (int i = 2; i <= 8; i++) {
            matchedUsers.add(new MatchedUser(i, "User " + i, createAvatarIcon(i)));
        }
        for (MatchedUser mu : matchedUsers) {
            matchedListPanel.add(matchedUserPanel(mu));
            matchedListPanel.add(Box.createVerticalStrut(16));
        }

        // Center layout
        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setOpaque(false);
        centerContent.add(matchedLabel);
        centerContent.add(scrollPane);
        centerPanel.add(centerContent);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel menuItem(String icon, String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        iconLabel.setForeground(color);
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        textLabel.setForeground(new Color(80, 30, 60));
        panel.add(iconLabel);
        panel.add(textLabel);
        return panel;
    }

    private JPanel matchedUserPanel(MatchedUser user) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(128, 0, 64), 2, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        panel.setMaximumSize(new Dimension(400, 80));
        panel.setPreferredSize(new Dimension(400, 80));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel pic = new JLabel(resizeIcon(user.profilePic, 56, 56));
        pic.setBorder(BorderFactory.createLineBorder(new Color(233, 54, 128), 2, true));
        pic.setOpaque(true);
        pic.setBackground(Color.WHITE);
        pic.setPreferredSize(new Dimension(56, 56));
        pic.setMaximumSize(new Dimension(56, 56));
        pic.setMinimumSize(new Dimension(56, 56));
        pic.setHorizontalAlignment(JLabel.CENTER);
        pic.setVerticalAlignment(JLabel.CENTER);
        pic.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        pic.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel name = new JLabel(user.name);
        name.setFont(new Font("SansSerif", Font.BOLD, 20));
        name.setForeground(new Color(128, 0, 64));
        name.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JButton chatBtn = new JButton("Chat");
        chatBtn.setBackground(new Color(233, 54, 128));
        chatBtn.setForeground(Color.WHITE);
        chatBtn.setFocusPainted(false);
        chatBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        chatBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        chatBtn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        chatBtn.setBorder(BorderFactory.createLineBorder(new Color(128, 0, 64), 1, true));
        chatBtn.addActionListener(e -> {
            ChatWindow chatWindow = new ChatWindow(currentUserId, user.id);
            chatWindow.setVisible(true);
        });
        // Hover effect for card
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(255, 230, 245));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }
        });
        panel.add(pic);
        panel.add(name);
        panel.add(Box.createHorizontalGlue());
        panel.add(chatBtn);
        return panel;
    }

    private ImageIcon resizeIcon(ImageIcon icon, int w, int h) {
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // Create a colored avatar icon for demo
    private ImageIcon createAvatarIcon(int seed) {
        int size = 56;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        Color[] colors = {new Color(233, 54, 128), new Color(128, 0, 64), new Color(255, 192, 203), new Color(80, 30, 60)};
        g2.setColor(colors[seed % colors.length]);
        g2.fillOval(0, 0, size, size);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 28));
        String letter = ("U" + seed).substring(0, 2);
        g2.drawString(letter, 12, 38);
        g2.dispose();
        return new ImageIcon(img);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrontPage().setVisible(true);
        });
    }
} 