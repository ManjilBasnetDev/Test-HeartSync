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
import heartsync.model.LoginFinal;

public class ResetPassword extends JFrame {
    private final ResetController resetController;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton resetButton;
    private JLabel validationLabel;
    private JCheckBox showPasswordCheckbox;
    private int userId;

    public ResetPassword(int userId) {
        this.userId = userId;
        this.resetController = new ResetController();
        resetController.setUserId(userId);
        initComponents();
        setupValidation();
        setupAccessibility();
    }

    private void initComponents() {
        setTitle("HeartSync - Reset Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(500, 400));
        setResizable(false);

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

        // Title
        JLabel titleLabel = new JLabel("Reset Your Password", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        formPanel.add(titleLabel, gbc);

        // New Password field
        JLabel newPasswordLabel = new JLabel("New Password");
        newPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(newPasswordLabel, gbc);

        newPasswordField = new JPasswordField();
        newPasswordField.setPreferredSize(new Dimension(0, 40));
        formPanel.add(newPasswordField, gbc);

        // Confirm Password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(new Dimension(0, 40));
        formPanel.add(confirmPasswordField, gbc);

        // Show password checkbox
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        showPasswordCheckbox.addActionListener(e -> {
            char echo = showPasswordCheckbox.isSelected() ? 0 : '*';
            newPasswordField.setEchoChar(echo);
            confirmPasswordField.setEchoChar(echo);
        });
        formPanel.add(showPasswordCheckbox, gbc);

        // Validation label
        validationLabel = new JLabel();
        validationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(validationLabel, gbc);

        // Reset button
        resetButton = new JButton("Reset Password");
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resetButton.setBackground(new Color(229, 89, 36));
        resetButton.setForeground(Color.WHITE);
        resetButton.setPreferredSize(new Dimension(0, 40));
        resetButton.addActionListener(e -> handleResetPassword());
        formPanel.add(resetButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void setupValidation() {
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
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        StringBuilder status = new StringBuilder("<html>");
        boolean allValid = true;

        // Check password length
        boolean isLongEnough = newPassword.length() >= 8;
        status.append(isLongEnough ? "<font color='green'>✓</font>" : "<font color='red'>✗</font>")
              .append(" At least 8 characters<br>");
        allValid &= isLongEnough;

        // Check for uppercase
        boolean hasUppercase = !newPassword.equals(newPassword.toLowerCase());
        status.append(hasUppercase ? "<font color='green'>✓</font>" : "<font color='red'>✗</font>")
              .append(" Contains uppercase letter<br>");
        allValid &= hasUppercase;

        // Check for number
        boolean hasNumber = newPassword.matches(".*\\d.*");
        status.append(hasNumber ? "<font color='green'>✓</font>" : "<font color='red'>✗</font>")
              .append(" Contains number<br>");
        allValid &= hasNumber;

        // Check for special character
        boolean hasSpecial = !newPassword.matches("[A-Za-z0-9 ]*");
        status.append(hasSpecial ? "<font color='green'>✓</font>" : "<font color='red'>✗</font>")
              .append(" Contains special character<br>");
        allValid &= hasSpecial;

        // Check passwords match
        boolean passwordsMatch = newPassword.equals(confirmPassword);
        status.append(passwordsMatch ? "<font color='green'>✓</font>" : "<font color='red'>✗</font>")
              .append(" Passwords match");
        allValid &= passwordsMatch;

        status.append("</html>");
        validationLabel.setText(status.toString());
        resetButton.setEnabled(allValid);
    }

    private void setupAccessibility() {
        newPasswordField.setToolTipText("Enter your new password");
        confirmPasswordField.setToolTipText("Confirm your new password");
        resetButton.setToolTipText("Click to reset your password");
        showPasswordCheckbox.setToolTipText("Show or hide password characters");
    }

    private void handleResetPassword() {
        String newPassword = new String(newPasswordField.getPassword());
        
        try {
            if (resetController.updatePassword(userId, newPassword)) {
                JOptionPane.showMessageDialog(this,
                    "Password reset successful! Please login with your new password.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginFinal().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to reset password. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
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
            new ResetPassword(1).setVisible(true);
        });
    }
}