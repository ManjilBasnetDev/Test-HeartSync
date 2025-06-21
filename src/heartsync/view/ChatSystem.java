/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heartsync.view;

import heartsync.dao.ChatDAO;
import heartsync.dao.LikeDAO;
import heartsync.dao.UserDAO;
import heartsync.model.Chat;
import heartsync.model.User;
import heartsync.model.UserProfile;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main chat window with a split-pane layout.
 * Displays a list of matched users on the left and the conversation on the right.
 *
 * @author Manjil
 */
public class ChatSystem extends JFrame {

    private final User currentUser;
    private final LikeDAO likeDAO;
    private final UserDAO userDAO;
    private final ChatDAO chatDAO;

    private JSplitPane splitPane;
    private JPanel contactListPanel;
    private ConversationView conversationView;
    private JScrollPane contactListScrollPane;

    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color CONTACT_ITEM_COLOR = new Color(255, 255, 255);
    private static final Color CONTACT_ITEM_HOVER_COLOR = new Color(230, 240, 255);
    private static final Color SELECTED_CONTACT_COLOR = new Color(200, 220, 255);
    private static final Font NAME_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font MESSAGE_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color CONTACT_LIST_BACKGROUND_COLOR = new Color(255, 240, 245); // Light pink

    public ChatSystem(User currentUser) {
        this.currentUser = currentUser;
        this.likeDAO = new LikeDAO();
        this.userDAO = new UserDAO();
        this.chatDAO = new ChatDAO();

        setTitle("HeartSync Messenger");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        loadMatchedUsers();
    }

    private void initComponents() {
        // Left side: Panel to hold the list of contacts
        contactListPanel = new JPanel();
        contactListPanel.setLayout(new BoxLayout(contactListPanel, BoxLayout.Y_AXIS));
        contactListPanel.setBackground(CONTACT_LIST_BACKGROUND_COLOR);
        contactListPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        contactListScrollPane = new JScrollPane(contactListPanel);
        contactListScrollPane.getViewport().setBackground(CONTACT_LIST_BACKGROUND_COLOR);
        contactListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contactListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contactListScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        // Right side: Panel to show the conversation
        conversationView = new ConversationView(currentUser, null); // Initially no user selected

        // Split pane to divide contacts and conversation
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, contactListScrollPane, conversationView);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.1);
        splitPane.setOneTouchExpandable(true);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);

        // Add a header with a back button
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(230, 230, 250)); // Light lavender for the header
        JButton backButton = new JButton("← Back to Explore");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            new Swipe().setVisible(true);
            dispose();
        });
        headerPanel.add(backButton);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void loadMatchedUsers() {
        contactListPanel.removeAll();

        List<String> matchedUserIds = likeDAO.getMatches(currentUser.getUsername());
        List<UserProfile> matchedProfiles = matchedUserIds.stream()
                                                          .map(userDAO::findByUsername)
                                                          .filter(java.util.Objects::nonNull)
                                                          .collect(Collectors.toList());

        if (matchedProfiles.isEmpty()) {
            JLabel noMatchesLabel = new JLabel("No matches yet. Keep exploring!");
            noMatchesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noMatchesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            contactListPanel.add(noMatchesLabel);
        } else {
            for (UserProfile matchedProfile : matchedProfiles) {
                JPanel contactItem = createContactItem(matchedProfile);
                contactListPanel.add(contactItem);
                contactListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        contactListPanel.revalidate();
        contactListPanel.repaint();
    }

    private JPanel createContactItem(UserProfile matchedProfile) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(CONTACT_ITEM_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(5, 10, 5, 10), // Margin
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR) // Bottom border
        ));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        // Profile picture (placeholder)
        // In a real app, you would load the user's image here
        JLabel picLabel = new JLabel("👤");
        picLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        picLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
        panel.add(picLabel, BorderLayout.WEST);

        // Name and last message
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(matchedProfile.getFullName());
        nameLabel.setFont(NAME_FONT);
        textPanel.add(nameLabel);

        // Fetch and display the last message
        Chat lastChat = chatDAO.getLastMessage(currentUser.getUsername(), matchedProfile.getUsername());
        String lastMessageText = (lastChat != null) ? lastChat.getMessage() : "No messages yet.";
        if (lastMessageText.length() > 30) {
            lastMessageText = lastMessageText.substring(0, 27) + "...";
        }
        JLabel lastMessageLabel = new JLabel(lastMessageText);
        lastMessageLabel.setFont(MESSAGE_FONT);
        lastMessageLabel.setForeground(Color.GRAY);
        textPanel.add(lastMessageLabel);

        panel.add(textPanel, BorderLayout.CENTER);

        // Mouse listener for selection and hover effects
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // When a contact is clicked, update the conversation view
                conversationView.setParticipant(matchedProfile);
                setSelectedContact(panel);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!panel.getBackground().equals(SELECTED_CONTACT_COLOR)) {
                    panel.setBackground(CONTACT_ITEM_HOVER_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                 if (!panel.getBackground().equals(SELECTED_CONTACT_COLOR)) {
                    panel.setBackground(CONTACT_ITEM_COLOR);
                }
            }
        });

        return panel;
    }

    private void setSelectedContact(JPanel selectedPanel) {
        // Reset background of all contact items
        for (Component component : contactListPanel.getComponents()) {
            if (component instanceof JPanel) {
                component.setBackground(CONTACT_ITEM_COLOR);
            }
        }
        // Set background for the selected one
        selectedPanel.setBackground(SELECTED_CONTACT_COLOR);
    }
} 