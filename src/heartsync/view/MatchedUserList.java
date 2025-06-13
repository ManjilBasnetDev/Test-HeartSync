package heartsync.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MatchedUserList extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JList<String> matchedUsersList;
    private JButton viewProfileButton;
    private JButton messageButton;
    private JButton backButton;
    private JLabel titleLabel;
    private DefaultListModel<String> listModel;
    private List<String> matches;

    public MatchedUserList() {
        initComponents();
        loadMatches(); // Load matches when the window opens
    }

    private void loadMatches() {
        // TODO: Replace this with actual database calls
        matches = new ArrayList<>();
        matches.add("Sarah Johnson - 25 - New York");
        matches.add("Michael Brown - 28 - Los Angeles");
        matches.add("Emma Wilson - 24 - Chicago");
        matches.add("David Lee - 27 - Boston");
        matches.add("Sophia Chen - 26 - San Francisco");
        
        // Clear existing list and add new matches
        listModel.clear();
        for (String match : matches) {
            listModel.addElement(match);
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Matched Users");
        setResizable(false);
        
        // Initialize components
        mainPanel = new JPanel();
        listModel = new DefaultListModel<>();
        matchedUsersList = new JList<>(listModel);
        viewProfileButton = new JButton("View Profile");
        messageButton = new JButton("Message");
        backButton = new JButton("Back");
        titleLabel = new JLabel("Your Matches");
        
        // Set up the main panel
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(255, 182, 193)); // Light pink background
        
        // Configure the title
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Configure the list
        matchedUsersList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        matchedUsersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        matchedUsersList.setBackground(Color.WHITE);
        matchedUsersList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return label;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(matchedUsersList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        // Configure buttons
        viewProfileButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        messageButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(255, 182, 193));
        buttonPanel.add(viewProfileButton);
        buttonPanel.add(messageButton);
        buttonPanel.add(backButton);
        
        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        getContentPane().add(mainPanel);
        
        // Add action listeners
        setupActionListeners();
        
        // Set frame properties
        pack();
        setLocationRelativeTo(null);
    }

    private void setupActionListeners() {
        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = matchedUsersList.getSelectedValue();
                if (selectedUser != null) {
                    // TODO: Implement view profile functionality
                    JOptionPane.showMessageDialog(MatchedUserList.this, 
                        "Viewing profile of: " + selectedUser,
                        "Profile View",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(MatchedUserList.this, 
                        "Please select a user first!",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        messageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = matchedUsersList.getSelectedValue();
                if (selectedUser != null) {
                    // TODO: Implement messaging functionality
                    JOptionPane.showMessageDialog(MatchedUserList.this, 
                        "Opening chat with: " + selectedUser,
                        "Chat",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(MatchedUserList.this, 
                        "Please select a user first!",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                // TODO: Navigate back to previous screen
            }
        });
    }

    // Method to add a matched user to the list
    public void addMatchedUser(String username) {
        listModel.addElement(username);
    }

    // Method to clear the list
    public void clearList() {
        listModel.clear();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MatchedUserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MatchedUserList().setVisible(true);
            }
        });
    }
} 