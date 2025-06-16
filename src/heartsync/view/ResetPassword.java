/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package heartsync.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import heartsync.controller.ResetController;
import heartsync.view.LoginView;
import heartsync.controller.LoginController;
import heartsync.view.HomePage;

public class ResetPassword extends JFrame {
    private final int userId;
    private final ResetController resetController;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton resetButton;
    private JLabel validationLabel;
    private JCheckBox showPasswordCheckbox;
    private String username;

    public ResetPassword(int userId, String username) {
        super("HeartSync - Reset Password");
        this.userId = userId;
        this.username = username;
        this.resetController = new ResetController();
        
        // Set both user ID and username in the controller
        resetController.setUserId(userId);
        resetController.setUsername(username);
        
        // Initialize UI components first
        initComponents();
        
        // Set up validation and accessibility
        setupValidation();
        setupAccessibility();
        
        // Initial validation
        if (newPasswordField != null && confirmPasswordField != null) {
            validatePasswords();
        }
    }

    private void initComponents() {
        // Set window properties
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(500, 400));
        setResizable(false);
        setLocationRelativeTo(null); // Center the window

        // Main panel with pink background
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(255, 219, 227));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);

        // Title with modern styling
        JLabel titleLabel = new JLabel("Reset Your Password", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(titleLabel, gbc);

        // New Password field
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel newPasswordLabel = new JLabel("New Password");
        newPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newPasswordLabel.setForeground(Color.BLACK);
        formPanel.add(newPasswordLabel, gbc);

        newPasswordField = new JPasswordField();
        newPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        newPasswordField.setPreferredSize(new Dimension(0, 40));
        newPasswordField.setBackground(Color.WHITE);
        newPasswordField.setOpaque(true);
        newPasswordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(newPasswordField, gbc);

        // Confirm Password field
        gbc.insets = new Insets(15, 0, 5, 0);
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmLabel.setForeground(Color.BLACK);
        formPanel.add(confirmLabel, gbc);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setPreferredSize(new Dimension(0, 40));
        confirmPasswordField.setBackground(Color.WHITE);
        confirmPasswordField.setOpaque(true);
        confirmPasswordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        gbc.insets = new Insets(5, 0, 15, 0);
        formPanel.add(confirmPasswordField, gbc);

        // Show password checkbox
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordCheckbox.setBackground(Color.WHITE);
        showPasswordCheckbox.addActionListener(e -> {
            boolean show = showPasswordCheckbox.isSelected();
            newPasswordField.setEchoChar(show ? '\0' : '•');
            confirmPasswordField.setEchoChar(show ? '\0' : '•');
        });
        formPanel.add(showPasswordCheckbox, gbc);

        // Validation label
        validationLabel = new JLabel();
        validationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        validationLabel.setForeground(Color.GRAY);
        gbc.insets = new Insets(10, 0, 20, 0);
        formPanel.add(validationLabel, gbc);

        // Reset button
        resetButton = new JButton("Reset Password");
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resetButton.setPreferredSize(new Dimension(0, 40));
        resetButton.setBackground(new Color(229, 89, 36));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setBorderPainted(false);
        resetButton.setOpaque(true);
        resetButton.setEnabled(false);
        resetButton.addActionListener(e -> handleResetPassword());
        
        // Add hover effect
        resetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (resetButton.isEnabled()) {
                    resetButton.setBackground(new Color(240, 100, 50));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (resetButton.isEnabled()) {
                    resetButton.setBackground(new Color(229, 89, 36));
                }
            }
        });
        
        gbc.insets = new Insets(10, 0, 0, 0);
        formPanel.add(resetButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void setupValidation() {
        if (newPasswordField == null || confirmPasswordField == null || validationLabel == null) {
            return; // UI components not initialized yet
        }
        DocumentListener validationListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validatePasswords();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validatePasswords();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validatePasswords();
            }
        };

        newPasswordField.getDocument().addDocumentListener(validationListener);
        confirmPasswordField.getDocument().addDocumentListener(validationListener);
    }

    private void validatePasswords() {
        if (newPasswordField == null || confirmPasswordField == null) {
            return; // UI components not initialized yet
        }
        String password = new String(newPasswordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());
        
        StringBuilder status = new StringBuilder("<html>");
        boolean allValid = true;

        // Check password requirements
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean isLongEnough = password.length() >= 8;
        boolean passwordsMatch = password.equals(confirm);

        status.append(hasUppercase ? "<font color='green'>✓</font>" : "<font color='red'>✗</font>")
              .append(" Uppercase<br>");
        status.append(hasSpecial ? "<font color='green'>✓</font>" : "<font color='red'>✗</font>")
              .append(" Special character<br>");
        status.append(hasNumber ? "<font color='green'>✓</font>" : "<font color='red'>✗</font>")
              .append(" Number<br>");
        status.append(isLongEnough ? "<font color='green'>✓</font>" : "<font color='red'>✗</font>")
              .append(" At least 8 characters<br>");
        status.append(passwordsMatch ? "<font color='green'>✓</font>" : "<font color='red'>✗</font>")
              .append(" Passwords match");
        
        status.append("</html>");
        if (validationLabel != null) {
            validationLabel.setText(status.toString());
        }

        allValid = hasUppercase && hasSpecial && hasNumber && isLongEnough && passwordsMatch;
        if (resetButton != null) {
            resetButton.setEnabled(allValid);
        }
    }

    private void setupAccessibility() {
        newPasswordField.setToolTipText("Enter your new password");
        confirmPasswordField.setToolTipText("Confirm your new password");
        resetButton.setToolTipText("Click to reset your password");
        showPasswordCheckbox.setToolTipText("Show or hide password characters");

        // Add keyboard mnemonics
        resetButton.setMnemonic('R');
        showPasswordCheckbox.setMnemonic('S');
    }

    private void handleResetPassword() {
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Double-check client-side validation
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Password fields cannot be empty.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match. Please try again.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Disable UI during operation
        resetButton.setEnabled(false);
        newPasswordField.setEnabled(false);
        confirmPasswordField.setEnabled(false);
        
        try {
            System.out.println("Attempting to update password for user ID: " + userId);
            System.out.println("Username from controller: " + username);
            
            // Update password through the controller
            boolean success = resetController.updatePassword(userId, newPassword);
            
            if (success) {
                System.out.println("Password update successful, showing success message...");
                JOptionPane.showMessageDialog(this,
                    "Your password has been successfully updated!",
                    "Password Updated",
                    JOptionPane.INFORMATION_MESSAGE);
                // Close reset window
                dispose();
                // Redirect to login screen
                SwingUtilities.invokeLater(() -> LoginView.showLoginView());
            
            } else {
                throw new Exception("Failed to update password. Please try again later.");
            }
        } catch (Exception ex) {
            String errorMsg = "Failed to update password. ";
            if (ex.getCause() != null) {
                errorMsg += ex.getCause().getMessage();
            } else if (ex.getMessage() != null) {
                errorMsg += ex.getMessage();
            } else {
                errorMsg += "An unknown error occurred.";
            }
            
            JOptionPane.showMessageDialog(this,
                errorMsg,
                "Password Update Failed",
                JOptionPane.ERROR_MESSAGE);
                
            // Clear sensitive fields on error
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        } finally {
            // Re-enable UI
            SwingUtilities.invokeLater(() -> {
                resetButton.setEnabled(true);
                newPasswordField.setEnabled(true);
                confirmPasswordField.setEnabled(true);
                newPasswordField.requestFocus();
            });
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // For testing purposes only
            new ResetPassword(1, "testuser").setVisible(true);
        });
    }
}