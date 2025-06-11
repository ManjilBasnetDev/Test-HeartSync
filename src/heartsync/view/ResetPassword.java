/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package heartsync.view;

import heartsync.Login;
import heartsync.controller.ResetController;
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
        validationLabel.setText(status.toString());

        allValid = hasUppercase && hasSpecial && hasNumber && isLongEnough && passwordsMatch;
        resetButton.setEnabled(allValid);
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
        
        try {
            if (resetController.updatePassword(userId, newPassword)) {
                JOptionPane.showMessageDialog(this,
                    "Password reset successful! Please login with your new password.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new Login().setVisible(true);
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