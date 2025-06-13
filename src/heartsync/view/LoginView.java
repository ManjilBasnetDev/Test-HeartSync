package heartsync.view;

import java.awt.Color;
import java.awt.Font;
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

import heartsync.controller.UserController;
import heartsync.controller.UserProfileController;
import heartsync.model.User;

public class LoginView extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(LoginView.class.getName());
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;
    private JLabel forgotPasswordLabel;
    private UserController userController;

    public LoginView() {
        userController = new UserController();
        initComponents();
        setupUI();
        setupListeners();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        mainPanel = new JPanel();
        titleLabel = new JLabel("HeartSync");
        subtitleLabel = new JLabel("Login");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        backButton = new JButton("Back");
        forgotPasswordLabel = new JLabel("Forgot Password?");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("HeartSync - Login");
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
        subtitleLabel.setBounds(320, 70, 100, 30);

        usernameField.setBounds(250, 160, 200, 30);
        usernameField.setText("Username");
        usernameField.setForeground(Color.GRAY);

        passwordField.setBounds(250, 210, 200, 30);
        passwordField.setEchoChar((char) 0);
        passwordField.setText("Password");
        passwordField.setForeground(Color.GRAY);

        loginButton.setBounds(300, 270, 100, 30);
        backButton.setBounds(20, 20, 80, 30);
        forgotPasswordLabel.setBounds(290, 320, 120, 20);
        forgotPasswordLabel.setForeground(Color.BLUE);
        forgotPasswordLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        mainPanel.add(titleLabel);
        mainPanel.add(subtitleLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordField);
        mainPanel.add(loginButton);
        mainPanel.add(backButton);
        mainPanel.add(forgotPasswordLabel);

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

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•');
                    passwordField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setEchoChar((char) 0);
                    passwordField.setText("Password");
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        loginButton.addActionListener(e -> performLogin());
        backButton.addActionListener(e -> {
            dispose();
            new HomePage().setVisible(true);
        });
        forgotPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new ForgotPassword().setVisible(true);
            }
        });
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();

        if (username.isEmpty() || username.equals("Username")) {
            showError("Please enter a username");
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty() || password.equals("Password")) {
            showError("Please enter a password");
            passwordField.requestFocus();
            return;
        }

        try {
            if (userController.authenticate(username, password)) {
                User user = userController.getUser(username);
                JOptionPane.showMessageDialog(this,
                    "Welcome back, " + username + "!",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
                UserProfileController profileController = new UserProfileController();
                new ProfileSetupView(profileController).setVisible(true);
            } else {
                showError("Invalid username or password");
                passwordField.setText("Password");
                passwordField.setEchoChar((char) 0);
                passwordField.setForeground(Color.GRAY);
                passwordField.requestFocus();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error during login", ex);
            showError("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected error during login", ex);
            showError("An unexpected error occurred");
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
            new LoginView().setVisible(true);
        });
    }
} 