package heartsync.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import heartsync.dao.ForgotDAO;
import heartsync.model.User;

public class ForgotPassword extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(ForgotPassword.class.getName());
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JTextField usernameField;
    private JTextField securityAnswer1Field;
    private JTextField securityAnswer2Field;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton resetButton;
    private JButton backButton;
    private ForgotDAO forgotDAO;

    public ForgotPassword() {
        forgotDAO = new ForgotDAO();
        initComponents();
        setupUI();
        setupListeners();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        mainPanel = new JPanel();
        titleLabel = new JLabel("HeartSync");
        subtitleLabel = new JLabel("Reset Password");
        usernameField = new JTextField();
        securityAnswer1Field = new JTextField();
        securityAnswer2Field = new JTextField();
        newPasswordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        resetButton = new JButton("Reset Password");
        backButton = new JButton("Back");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("HeartSync - Reset Password");
        setSize(700, 500);
        setResizable(false);
        setUndecorated(true);
    }

    private void setupUI() {
        mainPanel.setBackground(new Color(255, 192, 203));
        mainPanel.setLayout(null);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(300, 30, 150, 30);

        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        subtitleLabel.setBounds(290, 70, 150, 30);

        usernameField.setBounds(250, 120, 200, 30);
        usernameField.setText("Username");
        usernameField.setForeground(Color.GRAY);

        securityAnswer1Field.setBounds(250, 170, 200, 30);
        securityAnswer1Field.setText("Security Answer 1");
        securityAnswer1Field.setForeground(Color.GRAY);

        securityAnswer2Field.setBounds(250, 220, 200, 30);
        securityAnswer2Field.setText("Security Answer 2");
        securityAnswer2Field.setForeground(Color.GRAY);

        newPasswordField.setBounds(250, 270, 200, 30);
        newPasswordField.setEchoChar((char) 0);
        newPasswordField.setText("New Password");
        newPasswordField.setForeground(Color.GRAY);

        confirmPasswordField.setBounds(250, 320, 200, 30);
        confirmPasswordField.setEchoChar((char) 0);
        confirmPasswordField.setText("Confirm Password");
        confirmPasswordField.setForeground(Color.GRAY);

        resetButton.setBounds(300, 370, 120, 30);
        backButton.setBounds(20, 20, 80, 30);

        mainPanel.add(titleLabel);
        mainPanel.add(subtitleLabel);
        mainPanel.add(usernameField);
        mainPanel.add(securityAnswer1Field);
        mainPanel.add(securityAnswer2Field);
        mainPanel.add(newPasswordField);
        mainPanel.add(confirmPasswordField);
        mainPanel.add(resetButton);
        mainPanel.add(backButton);

        add(mainPanel);
    }

    private void setupListeners() {
        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("Username")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("Username");
                    usernameField.setForeground(Color.GRAY);
                }
            }
        });

        securityAnswer1Field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (securityAnswer1Field.getText().equals("Security Answer 1")) {
                    securityAnswer1Field.setText("");
                    securityAnswer1Field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (securityAnswer1Field.getText().isEmpty()) {
                    securityAnswer1Field.setText("Security Answer 1");
                    securityAnswer1Field.setForeground(Color.GRAY);
                }
            }
        });

        securityAnswer2Field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (securityAnswer2Field.getText().equals("Security Answer 2")) {
                    securityAnswer2Field.setText("");
                    securityAnswer2Field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (securityAnswer2Field.getText().isEmpty()) {
                    securityAnswer2Field.setText("Security Answer 2");
                    securityAnswer2Field.setForeground(Color.GRAY);
                }
            }
        });

        newPasswordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(newPasswordField.getPassword()).equals("New Password")) {
                    newPasswordField.setText("");
                    newPasswordField.setEchoChar('•');
                    newPasswordField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(newPasswordField.getPassword()).isEmpty()) {
                    newPasswordField.setEchoChar((char) 0);
                    newPasswordField.setText("New Password");
                    newPasswordField.setForeground(Color.GRAY);
                }
            }
        });

        confirmPasswordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(confirmPasswordField.getPassword()).equals("Confirm Password")) {
                    confirmPasswordField.setText("");
                    confirmPasswordField.setEchoChar('•');
                    confirmPasswordField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(confirmPasswordField.getPassword()).isEmpty()) {
                    confirmPasswordField.setEchoChar((char) 0);
                    confirmPasswordField.setText("Confirm Password");
                    confirmPasswordField.setForeground(Color.GRAY);
                }
            }
        });

        resetButton.addActionListener(e -> performPasswordReset());
        backButton.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
    }

    private void performPasswordReset() {
        String username = usernameField.getText().trim();
        String answer1 = securityAnswer1Field.getText().trim();
        String answer2 = securityAnswer2Field.getText().trim();
        String newPassword = String.valueOf(newPasswordField.getPassword()).trim();
        String confirmPassword = String.valueOf(confirmPasswordField.getPassword()).trim();

        if (username.isEmpty() || username.equals("Username")) {
            showError("Please enter your username");
            usernameField.requestFocus();
            return;
        }

        if (answer1.isEmpty() || answer1.equals("Security Answer 1")) {
            showError("Please enter security answer 1");
            securityAnswer1Field.requestFocus();
            return;
        }

        if (answer2.isEmpty() || answer2.equals("Security Answer 2")) {
            showError("Please enter security answer 2");
            securityAnswer2Field.requestFocus();
            return;
        }

        if (newPassword.isEmpty() || newPassword.equals("New Password")) {
            showError("Please enter your new password");
            newPasswordField.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty() || confirmPassword.equals("Confirm Password")) {
            showError("Please confirm your new password");
            confirmPasswordField.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("Passwords do not match");
            newPasswordField.setText("New Password");
            newPasswordField.setEchoChar((char) 0);
            newPasswordField.setForeground(Color.GRAY);
            confirmPasswordField.setText("Confirm Password");
            confirmPasswordField.setEchoChar((char) 0);
            confirmPasswordField.setForeground(Color.GRAY);
            newPasswordField.requestFocus();
            return;
        }

        try {
            User user = forgotDAO.findByUsername(username);
            if (user != null) {
                if (forgotDAO.validateSecurityAnswers(username, answer1, answer2)) {
                    if (forgotDAO.updatePassword(user.getEmail(), newPassword)) {
                        JOptionPane.showMessageDialog(this,
                            "Password reset successful. Please login with your new password.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        new LoginView().setVisible(true);
                    } else {
                        showError("Failed to update password");
                    }
                } else {
                    showError("Invalid security answers");
                }
            } else {
                showError("User not found");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error during password reset", ex);
            showError("Database error: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE)
        );
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error setting look and feel", e);
        }

        SwingUtilities.invokeLater(() -> {
            new ForgotPassword().setVisible(true);
        });
    }
} 