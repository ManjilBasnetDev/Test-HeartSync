package heartsync.view;

import heartsync.controller.LoginController;
import heartsync.controller.ResetController;
import heartsync.database.DatabaseConnection;
import heartsync.dao.UserDAOForgot;
import heartsync.model.UserForgot;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicTextFieldUI;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForgotPassword extends javax.swing.JFrame {
    private ResetController resetController;
    private JTextField usernameTextField;
    private JTextField favoriteColorTextField;
    private JTextField firstSchoolTextField;
    private JButton resetButton;
    private JButton backButton;
    private JLabel validationLabel;

    private static ForgotPassword instance = null;
    
    // Private constructor to prevent direct instantiation
    private ForgotPassword() {
        try {
            // Set system look and feel and UI properties
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextField.foreground", Color.BLACK);
            UIManager.put("TextField.caretForeground", Color.BLACK);
            UIManager.put("TextField.selectionBackground", new Color(229, 89, 36));
            UIManager.put("TextField.selectionForeground", Color.WHITE);
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Panel.background", new Color(255, 219, 227));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initComponents();
        setLocationRelativeTo(null);
        resetController = new ResetController();
        setupValidation();
        setupAccessibility();
        
        // Add window listener to handle cleanup
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                instance = null;
            }
        });
    }
    
    // Singleton pattern to ensure only one instance exists
    public static synchronized ForgotPassword getInstance() {
        if (instance == null || !instance.isDisplayable()) {
            instance = new ForgotPassword();
        }
        return instance;
    }
    
    public static void showForgotPassword() {
        SwingUtilities.invokeLater(() -> {
            ForgotPassword dialog = getInstance();
            dialog.setVisible(true);
            dialog.toFront();
        });
    }

    private void setupAccessibility() {
        usernameTextField.setToolTipText("Enter your username");
        favoriteColorTextField.setToolTipText("Enter your favorite color");
        firstSchoolTextField.setToolTipText("Enter your first school name");
        resetButton.setToolTipText("Click to reset your password");
        backButton.setToolTipText("Return to home page");
    }

    private void setupValidation() {
        DocumentListener validationListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateFields();
            }
        };

        usernameTextField.getDocument().addDocumentListener(validationListener);
        favoriteColorTextField.getDocument().addDocumentListener(validationListener);
        firstSchoolTextField.getDocument().addDocumentListener(validationListener);
    }

    private UserForgot currentUser = null;
    private String dialogResult = null;
    
    private String showResetPasswordDialog() {
        // Create a custom dialog
        JDialog dialog = new JDialog(this, "Reset Password", true);
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);
        
        // Field to store the dialog result
        final String[] dialogResult = new String[1];
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 15, 5);
        
        // Title
        JLabel titleLabel = new JLabel("Create New Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 60));
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(titleLabel, gbc);
        
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        
        // New password field
        JLabel newPassLabel = new JLabel("New Password:");
        newPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(newPassLabel, gbc);
        
        gbc.gridy++;
        JPasswordField newPassField = new JPasswordField(25);
        stylePasswordField(newPassField);
        formPanel.add(newPassField, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 5, 0);
        
        // Confirm password field
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(confirmPassLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        JPasswordField confirmPassField = new JPasswordField(25);
        stylePasswordField(confirmPassField);
        formPanel.add(confirmPassField, gbc);
        
        // Validation label
        JLabel validationLabel = new JLabel(" ");
        validationLabel.setForeground(new Color(200, 0, 0));
        validationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(validationLabel, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton cancelButton = new JButton("Cancel");
        styleDialogButton(cancelButton, new Color(108, 117, 125));
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton okButton = new JButton("Update Password");
        styleDialogButton(okButton, new Color(40, 167, 69));
        
        okButton.addActionListener(e -> {
            String newPassword = new String(newPassField.getPassword());
            String confirmPassword = new String(confirmPassField.getPassword());
            
            // Validate passwords
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                validationLabel.setText("Password fields cannot be empty");
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                validationLabel.setText("Passwords do not match");
                return;
            }
            
            if (newPassword.length() < 8) {
                validationLabel.setText("Password must be at least 8 characters");
                return;
            }
            
            dialogResult[0] = newPassword;
            dialog.dispose();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        
        dialogResult[0] = null;
        dialog.setVisible(true);
        
        return dialogResult[0];
    }
    
    private void handleResetPassword() {
        String username = usernameTextField.getText().trim();
        String favoriteColor = favoriteColorTextField.getText().trim();
        String firstSchool = firstSchoolTextField.getText().trim();

        try {
            // First validate security questions
            UserDAOForgot userDAO = new UserDAOForgot();
            if (userDAO.validateSecurityQuestions(username, favoriteColor, firstSchool)) {
                // If security questions are valid, show password reset dialog
                String newPassword = showResetPasswordDialog();
                if (newPassword != null && !newPassword.isEmpty()) {
                    // Update the password in the database
                    userDAO.updatePassword(username, DatabaseConnection.hashPassword(newPassword));
                    
                    // Show success message
                    JOptionPane.showMessageDialog(this,
                        "Your password has been reset successfully.",
                        "Password Reset",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Close the dialog and show login screen
                    dispose();
                    // Use the LoginController to properly manage the login view
                    heartsync.controller.LoginController.createAndShowLoginView();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid security answers. Please try again.",
                    "Validation Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void validateFields() {
        if (validationLabel == null) return;

        String username = usernameTextField.getText().trim();
        String favoriteColor = favoriteColorTextField.getText().trim();
        String firstSchool = firstSchoolTextField.getText().trim();

        StringBuilder status = new StringBuilder("<html>");
        boolean allValid = true;

        // Only fetch user if username has changed or we don't have a user yet
        if (currentUser == null || !username.equals(currentUser.getUsername())) {
            if (!username.isEmpty() && username.length() >= 3) {
                try {
                    UserDAOForgot dao = new UserDAOForgot();
                    currentUser = dao.findByUsername(username);
                } catch (Exception ex) {
                    System.err.println("DB error during validation: " + ex.getMessage());
                    currentUser = null;
                }
            } else {
                currentUser = null;
            }
        }

        // Username validation
        if (username.isEmpty()) {
            status.append("<font color='#666666'>• Enter your username</font><br>");
            allValid = false;
        } else if (username.length() < 3) {
            status.append("<font color='#666666'>• Username too short</font><br>");
            allValid = false;
        } else if (currentUser == null) {
            status.append("<font color='#FF0000'>• Username not found</font><br>");
            allValid = false;
        } else {
            status.append("<font color='#43A047'>• Username valid</font><br>");
        }

        // Only validate security questions if we have a valid user
        if (currentUser != null) {
            // Favorite color validation with null check
            String userColor = currentUser.getFavoriteColor();
            if (favoriteColor.isEmpty()) {
                status.append("<font color='#666666'>• Favorite color required</font><br>");
                allValid = false;
            } else if (userColor == null || !favoriteColor.equalsIgnoreCase(userColor.trim())) {
                status.append("<font color='#FF0000'>• Favorite color incorrect</font><br>");
                allValid = false;
            } else {
                status.append("<font color='#43A047'>• Favorite color correct</font><br>");
            }

            // First school validation with null check
            String userSchool = currentUser.getFirstSchool();
            if (firstSchool.isEmpty()) {
                status.append("<font color='#666666'>• First school required</font><br>");
                allValid = false;
            } else if (userSchool == null || !firstSchool.equalsIgnoreCase(userSchool.trim())) {
                status.append("<font color='#FF0000'>• First school incorrect</font><br>");
                allValid = false;
            } else {
                status.append("<font color='#43A047'>• First school correct</font><br>");
            }
        } else {
            status.append("<font color='#666666'>• Answer security questions</font><br>");
            allValid = false;
        }

        status.append("</html>");
        validationLabel.setText(status.toString());
        resetButton.setEnabled(allValid);

        if (allValid) {
            resetButton.requestFocusInWindow();
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setTitle("HeartSync - Forgot Password");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setMinimumSize(new Dimension(500, 600));
        setUndecorated(true);

        // Main panel with pink background and rounded corners
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 219, 227));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        mainPanel.setBackground(new Color(255, 219, 227));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel with white background and rounded corners
        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Title with modern styling
        JLabel titleLabel = new JLabel("Forgot Password?", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Don't worry, we'll help you recover it", SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.BLACK);
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(subtitleLabel, gbc);

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setForeground(Color.BLACK);
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(usernameLabel, gbc);

        usernameTextField = new JTextField();
        styleTextField(usernameTextField);
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(usernameTextField, gbc);

        // Security Questions Section
        JLabel securityLabel = new JLabel("Security Questions");
        securityLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        securityLabel.setForeground(Color.BLACK);
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(securityLabel, gbc);

        // Favorite Color Question
        JLabel colorLabel = new JLabel("What is your favorite color?");
        colorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        colorLabel.setForeground(Color.BLACK);
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(colorLabel, gbc);

        favoriteColorTextField = new JTextField();
        styleTextField(favoriteColorTextField);
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(favoriteColorTextField, gbc);

        // First School Question
        JLabel schoolLabel = new JLabel("What was your first school?");
        schoolLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        schoolLabel.setForeground(Color.BLACK);
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(schoolLabel, gbc);

        firstSchoolTextField = new JTextField();
        styleTextField(firstSchoolTextField);
        gbc.insets = new Insets(0, 0, 25, 0);
        formPanel.add(firstSchoolTextField, gbc);

        // Add validation label
        validationLabel = new JLabel();
        validationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        validationLabel.setForeground(new Color(70, 70, 70));
        gbc.insets = new Insets(0, 0, 25, 0);
        formPanel.add(validationLabel, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        // Reset Password Button
        resetButton = createStyledButton("Reset Password", new Color(229, 89, 36));
        resetButton.setPreferredSize(new Dimension(170, 45));
        resetButton.addActionListener(e -> handleResetPassword());
        resetButton.setEnabled(false);
        buttonPanel.add(resetButton);

        // Add some space between buttons
        buttonPanel.add(Box.createHorizontalStrut(20));
        
        // Back Button
        backButton = createStyledButton("Back", new Color(108, 117, 125));
        backButton.setPreferredSize(new Dimension(170, 45));
        backButton.addActionListener(e -> {
            dispose();
            new HomePage().setVisible(true);
        });
        buttonPanel.add(backButton);
        
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(0, 45));
        textField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 15, 5, 15)
        ));
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        textField.setCaretColor(Color.BLACK);
        textField.setOpaque(true);

        // Override the text field's paint component to ensure consistent background
        textField.setUI(new javax.swing.plaf.basic.BasicTextFieldUI() {
            @Override
            protected void paintBackground(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(textField.getBackground());
                g2d.fillRect(0, 0, textField.getWidth(), textField.getHeight());
                g2d.dispose();
            }
        });
        
        // Add focus listener for consistent appearance
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(new CompoundBorder(
                    new LineBorder(new Color(229, 89, 36)),
                    new EmptyBorder(5, 15, 5, 15)
                ));
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(new CompoundBorder(
                    new LineBorder(new Color(200, 200, 200)),
                    new EmptyBorder(5, 15, 5, 15)
                ));
            }
        });
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(backgroundColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(backgroundColor.brighter());
                } else {
                    g2.setColor(backgroundColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void stylePasswordField(JPasswordField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 15, 5, 15)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setOpaque(true);
    }
    
    private void styleDialogButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    public static void main(String args[]) {
        /* Set the system look and feel */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /* Create and display the form */
        SwingUtilities.invokeLater(() -> {
            ForgotPassword dialog = ForgotPassword.getInstance();
            dialog.setVisible(true);
        });
    }
}