/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package heartsync.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import heartsync.Login;

/**
 *
 * @author manjil-basnet
 */
public class HomePage extends JFrame {
    private JPanel mainPanel;
    private JPanel contentCards;
    private CardLayout cardLayout;
    private JLabel heartSyncLabel;
    private JLabel homeLabel;
    private JLabel aboutLabel;
    private JLabel featuresLabel;
    private JLabel contactLabel;
    private JButton loginButton;
    private JButton createAccountButton;
    private JLabel lookingForLabel;
    private JLabel loveLabel;
    private JLabel isWhatYouLabel;
    private JLabel needLabel;
    private JLabel coupleImageLabel;
    private JLabel handImageLabel;
    
    // Keep track of current navigation
    private JLabel currentNavItem;

    /**
     * Creates new form ProfileSetup
     */
    public HomePage() {
        setTitle("HeartSync - Find Love");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Create main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Create navigation panel
        JPanel navPanel = createNavigationPanel();
        mainPanel.add(navPanel, BorderLayout.NORTH);
        
        // Create card layout for content
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setBackground(Color.WHITE);
        
        // Add different content panels
        contentCards.add(createHomePanel(), "home");
        contentCards.add(createAboutPanel(), "about");
        contentCards.add(createFeaturesPanel(), "features");
        contentCards.add(new ContactsPage(), "contact");
        
        mainPanel.add(contentCards, BorderLayout.CENTER);
        
        // Add mainPanel to the frame
        add(mainPanel);
        
        // Set home as current by default
        setCurrentNavItem(homeLabel);
        cardLayout.show(contentCards, "home");
        
        // Make sure everything is properly laid out
        revalidate();
        repaint();
    }
    
    private void setCurrentNavItem(JLabel navItem) {
        if (currentNavItem != null) {
            currentNavItem.setForeground(new Color(51, 51, 51));
            currentNavItem.setBorder(null);
        }
        
        currentNavItem = navItem;
        currentNavItem.setForeground(new Color(229, 89, 36)); // Orange color for active item
        currentNavItem.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(229, 89, 36)));
    }
        
    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        navPanel.setBackground(Color.WHITE);
        
        // Create logo panel with heart icon
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        logoPanel.setOpaque(false);
        
        JLabel heartIcon = new JLabel("❤");
        heartIcon.setFont(new Font("SansSerif", Font.PLAIN, 20));
        heartIcon.setForeground(new Color(255, 89, 89));
        
        JLabel heartLabel = new JLabel("Heart");
        heartLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        heartLabel.setForeground(new Color(255, 89, 89));
        
        JLabel syncLabel = new JLabel("Sync");
        syncLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        syncLabel.setForeground(new Color(128, 128, 128));
        
        logoPanel.add(heartIcon);
        logoPanel.add(heartLabel);
        logoPanel.add(syncLabel);
        
        // Navigation items
        homeLabel = new JLabel("Home");
        aboutLabel = new JLabel("About Us");
        featuresLabel = new JLabel("Features");
        contactLabel = new JLabel("Contact Us");
        loginButton = new JButton("Login");
        
        // Style navigation items
        Font navFont = new Font("SansSerif", Font.PLAIN, 16);
        Color navColor = new Color(51, 51, 51);
        
        homeLabel.setFont(navFont);
        aboutLabel.setFont(navFont);
        featuresLabel.setFont(navFont);
        contactLabel.setFont(navFont);
        
        homeLabel.setForeground(navColor);
        aboutLabel.setForeground(navColor);
        featuresLabel.setForeground(navColor);
        contactLabel.setForeground(navColor);
        
        // Style login button
        loginButton.setFont(navFont);
        loginButton.setBackground(new Color(229, 89, 36));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(true);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(100, 35));
        
        // Add navigation items
        navPanel.add(logoPanel);
        navPanel.add(Box.createHorizontalStrut(50));
        navPanel.add(homeLabel);
        navPanel.add(aboutLabel);
        navPanel.add(featuresLabel);
        navPanel.add(contactLabel);
        navPanel.add(Box.createHorizontalStrut(50));
        navPanel.add(loginButton);
        
        // Add event listeners
        homeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aboutLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        featuresLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contactLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        homeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setCurrentNavItem(homeLabel);
                cardLayout.show(contentCards, "home");
            }
        });
        
        aboutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setCurrentNavItem(aboutLabel);
                cardLayout.show(contentCards, "about");
            }
        });
        
        featuresLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setCurrentNavItem(featuresLabel);
                cardLayout.show(contentCards, "features");
            }
        });
        
        contactLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setCurrentNavItem(contactLabel);
                cardLayout.show(contentCards, "contact");
            }
        });
        
        loginButton.addActionListener(e -> {
            Login loginPage = new Login();
            loginPage.setVisible(true);
            dispose(); // Close the home page when login page opens
        });
        
        return navPanel;
    }
    
    private JPanel createHomePanel() {
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBackground(new Color(255, 219, 227));
        
        // Create text content
        lookingForLabel = new JLabel("Looking for");
        loveLabel = new JLabel("Love?");
        
        // Create panel for HeartSync text with colored parts
        JPanel heartSyncPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        heartSyncPanel.setOpaque(false);
        
        JLabel heartMainLabel = new JLabel("Heart");
        heartMainLabel.setFont(new Font("SansSerif", Font.PLAIN, 48));
        heartMainLabel.setForeground(new Color(255, 89, 89)); // Same red as logo
        
        JLabel syncMainLabel = new JLabel("Sync");
        syncMainLabel.setFont(new Font("SansSerif", Font.PLAIN, 48));
        syncMainLabel.setForeground(new Color(128, 128, 128)); // Same grey as logo
        
        JLabel restOfText = new JLabel(" is what you");
        restOfText.setFont(new Font("SansSerif", Font.PLAIN, 48));
        restOfText.setForeground(new Color(51, 51, 51));
        
        heartSyncPanel.add(heartMainLabel);
        heartSyncPanel.add(syncMainLabel);
        heartSyncPanel.add(restOfText);
        
        needLabel = new JLabel("need!");
        
        // Style text
        Font headingFont = new Font("SansSerif", Font.PLAIN, 48);
        Color textColor = new Color(51, 51, 51);
        
        lookingForLabel.setFont(headingFont);
        loveLabel.setFont(headingFont);
        needLabel.setFont(headingFont);
        
        lookingForLabel.setForeground(textColor);
        loveLabel.setForeground(textColor);
        needLabel.setForeground(textColor);
        
        // Position text
        lookingForLabel.setBounds(100, 120, 400, 60);
        loveLabel.setBounds(100, 180, 400, 60);
        heartSyncPanel.setBounds(100, 300, 600, 60);
        needLabel.setBounds(100, 360, 400, 60);
        
        // Create buttons with modern styling
        loginButton = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(180, 230, 180));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(200, 255, 200));
                } else {
                    g2.setColor(new Color(217, 255, 217));
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D r = fm.getStringBounds(getText(), g2);
                int x = (getWidth() - (int) r.getWidth()) / 2;
                int y = (getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
                
                g2.setColor(new Color(51, 51, 51));
                g2.setFont(getFont());
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        
        createAccountButton = new JButton("Create Account") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(200, 70, 20));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(240, 100, 50));
                } else {
                    g2.setColor(new Color(229, 89, 36));
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D r = fm.getStringBounds(getText(), g2);
                int x = (getWidth() - (int) r.getWidth()) / 2;
                int y = (getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        
        // Style buttons
        Font buttonFont = new Font("SansSerif", Font.BOLD, 16);
        
        loginButton.setFont(buttonFont);
        loginButton.setForeground(new Color(51, 51, 51));
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        createAccountButton.setFont(buttonFont);
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setBorderPainted(false);
        createAccountButton.setFocusPainted(false);
        createAccountButton.setContentAreaFilled(false);
        createAccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Position buttons
        loginButton.setBounds(100, 500, 180, 55);
        createAccountButton.setBounds(300, 500, 230, 55);
        
        // Add action listeners
        loginButton.addActionListener(e -> {
            Login loginPage = new Login();
            loginPage.setVisible(true);
            dispose(); // Close the home page when login page opens
        });
        
        createAccountButton.addActionListener(e -> {
            Register register = new Register();
            register.setVisible(true);
            dispose(); // Close the home page when register page opens
        });
        
        // Load and add images
        try {
            // Load couple image
            ImageIcon coupleIcon = null;
            java.net.URL coupleUrl = getClass().getResource("/ImagePicker/HomePageCoupleImg.png");
            if (coupleUrl != null) {
                coupleIcon = new ImageIcon(coupleUrl);
                Image coupleImg = coupleIcon.getImage().getScaledInstance(400, 560, Image.SCALE_SMOOTH);
                coupleImageLabel = new JLabel(new ImageIcon(coupleImg));
                coupleImageLabel.setBounds(720, 20, 400, 560);
            } else {
                System.err.println("Could not find couple image resource");
                coupleImageLabel = new JLabel("Couple Image");
            }

            // Load hand image
            ImageIcon handIcon = null;
            java.net.URL handUrl = getClass().getResource("/ImagePicker/HomePageHandImg.png");
            if (handUrl != null) {
                handIcon = new ImageIcon(handUrl);
                Image handImg = handIcon.getImage().getScaledInstance(200, 280, Image.SCALE_SMOOTH);
                handImageLabel = new JLabel(new ImageIcon(handImg));
                handImageLabel.setBounds(450, 60, 200, 280);
            } else {
                System.err.println("Could not find hand image resource");
                handImageLabel = new JLabel("Hand Image");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading images: " + e.getMessage());
            coupleImageLabel = new JLabel("Couple Image");
            handImageLabel = new JLabel("Hand Image");
        }
        
        // Add components
        contentPanel.add(lookingForLabel);
        contentPanel.add(loveLabel);
        contentPanel.add(heartSyncPanel);
        contentPanel.add(needLabel);
        contentPanel.add(loginButton);
        contentPanel.add(createAccountButton);
        contentPanel.add(coupleImageLabel);
        contentPanel.add(handImageLabel);
        
        return contentPanel;
    }
    
    private JPanel createAboutPanel() {
        JPanel aboutPanel = new JPanel();
        aboutPanel.setBackground(new Color(255, 219, 227));
        aboutPanel.setLayout(new BorderLayout(20, 20));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel titleLabel = new JLabel("About HeartSync");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea descriptionArea = new JTextArea(
            "HeartSync is a modern dating platform designed to help people find meaningful connections. " +
            "Our mission is to create genuine relationships by matching people based on their interests, values, and lifestyle.\n\n" +
            "Founded in 2024, HeartSync has grown to become one of the most trusted dating platforms, " +
            "known for its innovative approach to online dating and commitment to user safety and privacy.\n\n" +
            "What sets us apart:\n" +
            "• Advanced matching algorithms\n" +
            "• Verified profiles\n" +
            "• Safe and secure environment\n" +
            "• 24/7 customer support\n" +
            "• Success stories from happy couples"
        );
        
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        descriptionArea.setForeground(new Color(51, 51, 51));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(descriptionArea);
        
        aboutPanel.add(contentPanel, BorderLayout.CENTER);
        
        return aboutPanel;
    }
    
    private JPanel createFeaturesPanel() {
        JPanel featuresPanel = new JPanel();
        featuresPanel.setBackground(new Color(255, 219, 227));
        featuresPanel.setLayout(new BorderLayout(20, 20));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel titleLabel = new JLabel("Features");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] features = {
            "Smart Matching",
            "Video Chat",
            "Profile Verification",
            "Interest Groups",
            "Event Planning",
            "Safety Features"
        };
        
        String[] descriptions = {
            "Our AI-powered algorithm finds your perfect match based on compatibility.",
            "Connect face-to-face with potential matches in a safe environment.",
            "Verified badges ensure you're meeting real people.",
            "Join groups based on shared interests and hobbies.",
            "Plan and organize dates with built-in scheduling tools.",
            "Advanced security measures to protect your privacy and safety."
        };
        
        JPanel featuresList = new JPanel();
        featuresList.setLayout(new GridLayout(features.length, 1, 0, 20));
        featuresList.setOpaque(false);
        
        for (int i = 0; i < features.length; i++) {
            JPanel featurePanel = new JPanel(new BorderLayout(10, 5));
            featurePanel.setOpaque(false);
            
            JLabel featureTitle = new JLabel(features[i]);
            featureTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
            featureTitle.setForeground(new Color(229, 89, 36));
            
            JLabel featureDesc = new JLabel(descriptions[i]);
            featureDesc.setFont(new Font("SansSerif", Font.PLAIN, 16));
            featureDesc.setForeground(new Color(51, 51, 51));
            
            featurePanel.add(featureTitle, BorderLayout.NORTH);
            featurePanel.add(featureDesc, BorderLayout.CENTER);
            
            featuresList.add(featurePanel);
        }
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(featuresList);
        
        featuresPanel.add(contentPanel, BorderLayout.CENTER);
        
        return featuresPanel;
    }
    
    private JPanel createContactPanel() {
        JPanel contactPanel = new JPanel();
        contactPanel.setBackground(new Color(255, 219, 227));
        contactPanel.setLayout(new BorderLayout(20, 20));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel titleLabel = new JLabel("Contact Us");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Create labels with better contrast
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setForeground(new Color(51, 51, 51));
        
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        emailLabel.setForeground(new Color(51, 51, 51));
        
        JLabel messageLabel = new JLabel("Your Message:");
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        messageLabel.setForeground(new Color(51, 51, 51));
        
        // Create text fields with improved background for contrast
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameField.setPreferredSize(new Dimension(300, 35));
        nameField.setBackground(new Color(255,245,245)); // Light pink background
        nameField.setOpaque(true);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 89, 36), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        nameField.setForeground(new Color(153, 153, 153));
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (nameField.getForeground().equals(new Color(153, 153, 153))) {
                    nameField.setText("");
                    nameField.setForeground(Color.BLACK);
                }
                nameField.setBackground(new Color(255,245,245));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setForeground(new Color(153, 153, 153));
                }
                nameField.setBackground(new Color(255,245,245));
            }
        });
        
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(300, 35));
        emailField.setBackground(new Color(255,245,245)); // Light pink background
        emailField.setOpaque(true);
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 89, 36), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        emailField.setForeground(new Color(153, 153, 153));
        emailField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (emailField.getForeground().equals(new Color(153, 153, 153))) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
                emailField.setBackground(new Color(255,245,245));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setForeground(new Color(153, 153, 153));
                }
                emailField.setBackground(new Color(255,245,245));
            }
        });
        
        JTextArea messageArea = new JTextArea(5, 20);
        messageArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBackground(new Color(255,245,245)); // Light pink background
        messageArea.setOpaque(true);
        messageArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 89, 36), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        messageArea.setForeground(new Color(153, 153, 153));
        messageArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (messageArea.getForeground().equals(new Color(153, 153, 153))) {
                    messageArea.setText("");
                    messageArea.setForeground(Color.BLACK);
                }
                messageArea.setBackground(new Color(255,245,245));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (messageArea.getText().isEmpty()) {
                    messageArea.setForeground(new Color(153, 153, 153));
                }
                messageArea.setBackground(new Color(255,245,245));
            }
        });
        
        JButton submitButton = createStyledButton("Send Message");
        
        // Add components to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(nameLabel, gbc);
        
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(nameField, gbc);
        
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(emailLabel, gbc);
        
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(emailField, gbc);
        
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(messageLabel, gbc);
        
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        
        // Create scroll pane with white background
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        formPanel.add(scrollPane, gbc);
        
        gbc.gridy = 6;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(submitButton, gbc);
        
        submitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Thank you for your message! We'll get back to you soon.",
                "Message Sent",
                JOptionPane.INFORMATION_MESSAGE);
            nameField.setText("");
            emailField.setText("");
            messageArea.setText("");
        });
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(formPanel);
        
        contactPanel.add(contentPanel, BorderLayout.CENTER);
        
        return contactPanel;
    }
    
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 35));
        field.setBackground(Color.WHITE);  // Ensure white background
        field.setOpaque(true);  // Make sure the background is painted
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 89, 36), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setText(placeholder);
        field.setForeground(new Color(153, 153, 153));  // Light gray for placeholder
        
        // Add focus listeners for placeholder behavior
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);  // Black text for user input
                }
                field.setBackground(Color.WHITE);  // Ensure white background when focused
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(153, 153, 153));
                }
                field.setBackground(Color.WHITE);  // Ensure white background when focus lost
            }
        });
        return field;
    }
    
    private JTextArea createStyledTextArea(String placeholder) {
        JTextArea area = new JTextArea(5, 20);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(Color.WHITE);  // Ensure white background
        area.setOpaque(true);  // Make sure the background is painted
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 89, 36), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        area.setText(placeholder);
        area.setForeground(new Color(153, 153, 153));
        
        // Add focus listeners for placeholder behavior
        area.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (area.getText().equals(placeholder)) {
                    area.setText("");
                    area.setForeground(Color.BLACK);  // Black text for user input
                }
                area.setBackground(Color.WHITE);  // Ensure white background when focused
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (area.getText().isEmpty()) {
                    area.setText(placeholder);
                    area.setForeground(new Color(153, 153, 153));
                }
                area.setBackground(Color.WHITE);  // Ensure white background when focus lost
            }
        });
        return area;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(200, 70, 20));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(240, 100, 50));
                } else {
                    g2.setColor(new Color(229, 89, 36));
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D r = fm.getStringBounds(getText(), g2);
                int x = (getWidth() - (int) r.getWidth()) / 2;
                int y = (getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    public static void main(String[] args) {
        try {
            // Use cross-platform look and feel
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            
            // Set button UI defaults
            UIManager.put("Button.background", new Color(70, 130, 180));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
            
            // Set panel UI defaults
            UIManager.put("Panel.background", new Color(255, 240, 245));
            UIManager.put("Label.font", new Font("Arial", Font.BOLD, 14));
            UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 14));
            UIManager.put("TextArea.font", new Font("Arial", Font.PLAIN, 14));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            HomePage frame = new HomePage();
            frame.setVisible(true);
        });
    }
}