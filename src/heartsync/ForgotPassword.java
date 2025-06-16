package heartsync;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import heartsync.controller.ResetController;
import heartsync.dao.ForgotDAO;
import heartsync.model.User;
import heartsync.view.LoginView;

public class ForgotPassword extends javax.swing.JFrame {
    private static final Logger LOGGER = Logger.getLogger(ForgotPassword.class.getName());
    private ResetController resetController;
    private JTextField usernameField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JTextField favoriteColorField;
    private JTextField firstSchoolField;
    private JButton resetButton;
    private JButton backButton;
    private JLabel validationLabel;

    public ForgotPassword() {
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
    }

    private void setupAccessibility() {
        usernameField.setToolTipText("Enter your username");
        favoriteColorField.setToolTipText("Enter your favorite color");
        firstSchoolField.setToolTipText("Enter your first school name");
        resetButton.setToolTipText("Click to reset your password");
        backButton.setToolTipText("Return to login page");
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

        usernameField.getDocument().addDocumentListener(validationListener);
        favoriteColorField.getDocument().addDocumentListener(validationListener);
        firstSchoolField.getDocument().addDocumentListener(validationListener);
    }

    private void validateFields() {
        if (validationLabel == null) return;

        String username = usernameField.getText().trim();
        String favoriteColor = favoriteColorField.getText().trim();
        String firstSchool = firstSchoolField.getText().trim();

        StringBuilder status = new StringBuilder("<html>");
        boolean allValid = true;

        // Username validation
        if (username.isEmpty()) {
            status.append("<font color='#666666'>• Username required</font><br>");
            allValid = false;
        } else if (username.length() < 3) {
            status.append("<font color='#666666'>• Username too short</font><br>");
            allValid = false;
        } else {
            status.append("<font color='#43A047'>• Username valid</font><br>");
        }

        // Security questions validation
        if (favoriteColor.isEmpty()) {
            status.append("<font color='#666666'>• Favorite color required</font><br>");
            allValid = false;
        } else {
            status.append("<font color='#43A047'>• Favorite color provided</font><br>");
        }

        if (firstSchool.isEmpty()) {
            status.append("<font color='#666666'>• First school required</font><br>");
            allValid = false;
        } else {
            status.append("<font color='#43A047'>• First school provided</font><br>");
        }

        status.append("</html>");
        validationLabel.setText(status.toString());
        resetButton.setEnabled(allValid);
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

        usernameField = new JTextField();
        styleTextField(usernameField);
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(usernameField, gbc);

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

        favoriteColorField = new JTextField();
        styleTextField(favoriteColorField);
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(favoriteColorField, gbc);

        // First School Question
        JLabel schoolLabel = new JLabel("What was your first school?");
        schoolLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        schoolLabel.setForeground(Color.BLACK);
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(schoolLabel, gbc);

        firstSchoolField = new JTextField();
        styleTextField(firstSchoolField);
        gbc.insets = new Insets(0, 0, 25, 0);
        formPanel.add(firstSchoolField, gbc);

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
        resetButton.addActionListener(e -> handlePasswordReset());
        resetButton.setEnabled(false);
        buttonPanel.add(resetButton);

        // Add some space between buttons
        buttonPanel.add(Box.createHorizontalStrut(20));

        // Back Button
        backButton = createStyledButton("Back to Login", new Color(108, 117, 125));
        backButton.setPreferredSize(new Dimension(170, 45));
        backButton.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
        buttonPanel.add(backButton);

        gbc.insets = new Insets(0, 0, 0, 0);
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

    private void handlePasswordReset() {
        String username = usernameField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String favoriteColor = favoriteColorField.getText().trim();
        String firstSchool = firstSchoolField.getText().trim();

        // Validate input
        if (username.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() ||
            favoriteColor.isEmpty() || firstSchool.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ForgotDAO forgotDAO = new ForgotDAO();
            User user = forgotDAO.findByUsername(username);
            
            if (user != null && forgotDAO.validateSecurityAnswers(username, favoriteColor, firstSchool)) {
                if (forgotDAO.updatePassword(username, newPassword)) {
                    JOptionPane.showMessageDialog(this,
                        "Password reset successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Return to login
                    dispose();
                    new LoginView().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to reset password. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid security answers or username",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during password reset: {0}", e.getMessage());
            JOptionPane.showMessageDialog(this,
                "An error occurred: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new ForgotPassword().setVisible(true);
        });
    }
} 