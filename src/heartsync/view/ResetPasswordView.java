package heartsync.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import heartsync.dao.ResetDAO;
import heartsync.model.User;

public class ResetPasswordView extends JPanel {
    private JTextField usernameField;
    private JTextField firstSchoolField;
    private JTextField favoriteColorField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton resetButton;
    private ResetDAO resetDAO;

    public ResetPasswordView() {
        try {
            resetDAO = new ResetDAO();
            setupUI();
        } catch (Exception e) {
            showError("Error initializing reset view: " + e.getMessage());
        }
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Reset Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        // First School
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("First School:"), gbc);
        gbc.gridx = 1;
        firstSchoolField = new JTextField(20);
        formPanel.add(firstSchoolField, gbc);

        // Favorite Color
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Favorite Color:"), gbc);
        gbc.gridx = 1;
        favoriteColorField = new JTextField(20);
        formPanel.add(favoriteColorField, gbc);

        // New Password
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        newPasswordField = new JPasswordField(20);
        formPanel.add(newPasswordField, gbc);

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordField, gbc);

        // Reset Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        resetButton = new JButton("Reset Password");
        resetButton.addActionListener(e -> handleReset());
        formPanel.add(resetButton, gbc);

        // Add panels to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }

    private void handleReset() {
        String username = usernameField.getText().trim();
        String firstSchool = firstSchoolField.getText().trim();
        String favoriteColor = favoriteColorField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validate input
        if (username.isEmpty() || firstSchool.isEmpty() || favoriteColor.isEmpty() || 
            newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        try {
            // Verify security answers
            if (resetDAO.validateSecurityAnswer(username, firstSchool)) {
                // Reset password
                if (resetDAO.resetPassword(username, newPassword)) {
                    showSuccess("Password reset successful");
                    clearFields();
                } else {
                    showError("Failed to reset password");
                }
            } else {
                showError("Invalid security answers");
            }
        } catch (Exception e) {
            showError("Error resetting password: " + e.getMessage());
        }
    }

    private void clearFields() {
        usernameField.setText("");
        firstSchoolField.setText("");
        favoriteColorField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
} 