package heartsync;

import heartsync.dao.UserDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Edsha
 */
public class Login extends javax.swing.JFrame {

    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnTogglePassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblForgotPassword;
    private javax.swing.JTextArea txtUsername;
    private javax.swing.JPasswordField txtPassword;
    private ShowHideController showHideController;
    private Window previousWindow;

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        setupTextFields();
        showHideController = new ShowHideController(txtPassword, btnTogglePassword);
        setupActionListeners();
        setSize(700, 500);
        setResizable(false);
        setLocationRelativeTo(null);

        // Store reference to previous window
        for (Window window : Window.getWindows()) {
            if (window != this && window.isVisible()) {
                previousWindow = window;
                break;
            }
        }
    }

    private void setupTextFields() {
        // Username field setup
        txtUsername.setLineWrap(true);
        txtUsername.setWrapStyleWord(true);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 15, 10, 15)));
        txtUsername.setRows(1);
        txtUsername.setMaximumSize(new Dimension(400, 45));
        txtUsername.setPreferredSize(new Dimension(400, 45));
        txtUsername.setText("USERNAME");
        txtUsername.setForeground(Color.GRAY);

        // Password field setup
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 15, 10, 15)));
        txtPassword.setMaximumSize(new Dimension(400, 45));
        txtPassword.setPreferredSize(new Dimension(400, 45));
        txtPassword.setText("Enter password");
        txtPassword.setEchoChar((char)0); // Make placeholder visible
        txtPassword.setForeground(Color.GRAY);
    }

    private void setupActionListeners() {
    // Ensure login button triggers login
    btnLogin.addActionListener(e -> performLogin());
        // Username focus listener
        txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtUsername.getText().equals("USERNAME")) {
                    txtUsername.setText("");
                    txtUsername.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtUsername.getText().trim().isEmpty()) {
                    txtUsername.setForeground(Color.GRAY);
                    txtUsername.setText("USERNAME");
                }
            }
        });

        // Password focus listener
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(txtPassword.getPassword()).equals("Enter password")) {
                    txtPassword.setText("");
                    txtPassword.setEchoChar('•');
                    txtPassword.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (String.valueOf(txtPassword.getPassword()).isEmpty()) {
                    txtPassword.setForeground(Color.GRAY);
                    txtPassword.setText("Enter password");
                    txtPassword.setEchoChar((char) 0);
                }
            }
        });

        // Toggle button listener
        btnTogglePassword.addActionListener(e -> showHideController.togglePasswordVisibility());

        // Forgot password link action
        lblForgotPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(Login.this, "Forgot Password functionality not yet implemented.");
            }
        });
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || username.equals("USERNAME")) {
            JOptionPane.showMessageDialog(this, "Please enter a username.", "Input Required", JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }
        if (password.isEmpty() || password.equals("Enter password")) {
            JOptionPane.showMessageDialog(this, "Please enter a password.", "Input Required", JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username must be at least 3 characters long.", "Invalid Username", JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            JOptionPane.showMessageDialog(this, "Username can only contain letters, numbers, and underscores.", "Invalid Username", JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", "Invalid Password", JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            if (userDAO.authenticate(username, password)) {
                JOptionPane.showMessageDialog(this, "Welcome back, " + username + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                // new MainDashboard().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        // Safely load login image icon
        java.net.URL iconURL = getClass().getResource("/heartsync/Img/Login.png");
        if (iconURL != null) {
            jLabel1.setIcon(new javax.swing.ImageIcon(iconURL));
        }
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        usernamePanel.setBackground(Color.WHITE);
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtUsername = new javax.swing.JTextArea();

        txtPassword = new JPasswordField();

        btnLogin = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnTogglePassword = new javax.swing.JButton();
        lblForgotPassword = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(700, 500));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36));
        jLabel3.setText("HeartSync");
        jLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel2.setText("Welcome Back!");
        jLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsername.setColumns(20);
        txtUsername.setRows(1);
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernamePanel.add(txtUsername);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtPassword.setColumns(20);
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordPanel.add(txtPassword);
        passwordPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        btnTogglePassword.setText("Show");
        btnTogglePassword.setFocusPainted(false);
        btnTogglePassword.setOpaque(true);
        btnTogglePassword.setContentAreaFilled(false);
        btnTogglePassword.setBorderPainted(false);
        btnTogglePassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordPanel.add(btnTogglePassword);

        lblForgotPassword.setFont(new java.awt.Font("Segoe UI", 1, 12));
        lblForgotPassword.setForeground(new java.awt.Color(229, 89, 36));
        lblForgotPassword.setText("Forgot Password?");
        lblForgotPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblForgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnBack.setBackground(new java.awt.Color(229, 89, 36));
        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("Back");
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setPreferredSize(new java.awt.Dimension(150, 40));

        btnLogin.setBackground(new java.awt.Color(229, 89, 36));
        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Login");
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new java.awt.Dimension(150, 40));

        buttonPanel.add(btnBack);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(btnLogin);

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(jLabel3);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(jLabel2);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(usernamePanel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(lblForgotPassword);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalGlue());
        formPanel.add(Box.createVerticalStrut(20));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(formPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(formPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        performLogin();
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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }


}