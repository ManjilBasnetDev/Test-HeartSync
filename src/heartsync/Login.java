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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

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
        setupActionListeners();
        showHideController = new ShowHideController(txtPassword, btnTogglePassword);
        setSize(700, 500);
        setResizable(false);
        
        // Store reference to previous window
        for (Window window : Window.getWindows()) {
            if (window != this && window.isVisible()) {
                previousWindow = window;
                break;
            }
        }
    }

    private String actualPassword = "";
    private boolean isPasswordVisible = false;
    private boolean selfEdit = false; // Class-level flag to prevent listener loops

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
        txtUsername.setForeground(Color.BLACK);
        
        // Password field setup
        txtPassword = new javax.swing.JPasswordField();
        txtPassword.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setOpaque(true);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 15, 10, 15)));
        txtPassword.setMaximumSize(new Dimension(400, 45));
        txtPassword.setPreferredSize(new Dimension(400, 45));
        txtPassword.setForeground(Color.BLACK);
    }

    private void setupActionListeners() {
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
                SwingUtilities.invokeLater(() -> showHideController.onFocusGained());
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                SwingUtilities.invokeLater(() -> showHideController.onFocusLost());
            }
        });

        // Password document listener
        txtPassword.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                handlePasswordChange();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                handlePasswordChange();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                handlePasswordChange();
            }

            private void handlePasswordChange() {
                SwingUtilities.invokeLater(() -> {
                    String pass = String.valueOf(txtPassword.getPassword());
                    if (!pass.equals("Enter password")) {
                        txtPassword.setForeground(Color.BLACK);
                        if (txtPassword.getEchoChar() == (char)0) {
                            txtPassword.setEchoChar('•');
                        }
                    }
                });
            }
        });

        // Back button action
        btnBack.addActionListener(e -> {
            if (previousWindow != null) {
                previousWindow.setVisible(true);
            }
            dispose();
        });

        // Login button action
        btnLogin.addActionListener(e -> performLogin());

        // Forgot password link
        lblForgotPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblForgotPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // TODO: Implement forgot password functionality
                JOptionPane.showMessageDialog(Login.this,
                    "Forgot password functionality will be implemented soon.",
                    "Coming Soon",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = String.valueOf(txtPassword.getPassword());
        
        try {
            // Username validation
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                txtUsername.requestFocus();
                return;
            }
            if (username.length() < 3) {
                JOptionPane.showMessageDialog(this, "Username must be at least 3 characters long", "Invalid Username", JOptionPane.WARNING_MESSAGE);
                txtUsername.requestFocus();
                return;
            }
            if (!username.matches("^[a-zA-Z0-9_]+$")) {
                JOptionPane.showMessageDialog(this, "Username can only contain letters, numbers, and underscores", "Invalid Username", JOptionPane.WARNING_MESSAGE);
                txtUsername.requestFocus();
                return;
            }

            // Password validation
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a password", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                txtPassword.requestFocus();
                return;
            }
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long", "Invalid Password", JOptionPane.WARNING_MESSAGE);
                txtPassword.requestFocus();
                return;
            }
            if (!password.matches(".*[A-Z].*")) {
                JOptionPane.showMessageDialog(this, "Password must contain at least one uppercase letter", "Invalid Password", JOptionPane.WARNING_MESSAGE);
                txtPassword.requestFocus();
                return;
            }
            if (!password.matches(".*[0-9].*")) {
                JOptionPane.showMessageDialog(this, "Password must contain at least one number", "Invalid Password", JOptionPane.WARNING_MESSAGE);
                txtPassword.requestFocus();
                return;
            }

            UserDAO userDAO = new UserDAO();
            if (userDAO.authenticate(username, password)) {
                JOptionPane.showMessageDialog(this, 
                    "Welcome back, " + username + "!", 
                    "Login Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
                // TODO: Add code to open main application window
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Invalid username or password. Please try again.", 
                    "Login Failed", 
                    JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                showHideController.reset();
                txtPassword.requestFocus();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Database error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void togglePasswordVisibility() {
        showHideController.togglePasswordVisibility();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        JPanel formPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblForgotPassword = new javax.swing.JLabel();
        
        // Create text areas instead of text fields
        txtUsername = new javax.swing.JTextArea();
        
        // Create password field and show/hide button
        txtPassword = new javax.swing.JPasswordField();
        btnTogglePassword = new javax.swing.JButton();
        btnTogglePassword.setText("Show");
        btnTogglePassword.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnTogglePassword.setBackground(new java.awt.Color(240, 240, 240));
        btnTogglePassword.setForeground(new java.awt.Color(108, 117, 125));
        btnTogglePassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 15, 10, 15)));
        btnTogglePassword.setFocusPainted(false);
        btnTogglePassword.setOpaque(true);
        btnTogglePassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTogglePassword.setPreferredSize(new Dimension(70, 45));
        btnTogglePassword.setMaximumSize(new Dimension(70, 45));
        
        btnLogin = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HeartSync Dating App - Login");
        setMinimumSize(new Dimension(700, 500));
        getContentPane().setBackground(new Color(255, 219, 227));

        // Main panel with pink background
        jPanel1.setBackground(new Color(255, 219, 227));
        jPanel1.setBorder(new EmptyBorder(30, 40, 30, 40));

        // White form panel like Contact Us page
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setMaximumSize(new Dimension(600, 400));

        // App name
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 32));
        jLabel3.setForeground(new java.awt.Color(239, 83, 80));
        jLabel3.setText("HeartSync");
        jLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Welcome text
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel2.setForeground(new java.awt.Color(128, 128, 128));
        jLabel2.setText("Welcome Back!");
        jLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a panel for username with the same layout as password panel
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        usernamePanel.setBackground(Color.WHITE);
        usernamePanel.setMaximumSize(new Dimension(480, 45));
        usernamePanel.setPreferredSize(new Dimension(480, 45));
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username label (left of field)
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblUsername.setForeground(new java.awt.Color(108, 117, 125));
        lblUsername.setBorder(new EmptyBorder(0, 0, 0, 10));
        usernamePanel.add(lblUsername);
        
        // Username field
        txtUsername.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtUsername.setBackground(Color.WHITE);
        txtUsername.setOpaque(true);
        txtUsername.setForeground(Color.BLACK);
        txtUsername.setLineWrap(true);
        txtUsername.setWrapStyleWord(true);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 15, 10, 15)));
        txtUsername.setRows(1);
        txtUsername.setMaximumSize(new Dimension(400, 45));
        txtUsername.setPreferredSize(new Dimension(400, 45));
        usernamePanel.add(txtUsername);
        usernamePanel.add(Box.createRigidArea(new Dimension(80, 0))); // Same width as password panel

        // Password panel setup
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setMaximumSize(new Dimension(480, 45));
        passwordPanel.setPreferredSize(new Dimension(480, 45));
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Password label (left of field)
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblPassword.setForeground(new java.awt.Color(108, 117, 125));
        lblPassword.setBorder(new EmptyBorder(0, 0, 0, 10));
        passwordPanel.add(lblPassword);

        // Password field
        txtPassword.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setOpaque(true);
        txtPassword.setForeground(Color.BLACK);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 15, 10, 15)));
        txtPassword.setMaximumSize(new Dimension(400, 45));
        txtPassword.setPreferredSize(new Dimension(400, 45));
        passwordPanel.add(txtPassword);
        passwordPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        passwordPanel.add(btnTogglePassword);

        // Initialize ShowHideController
        showHideController = new ShowHideController(txtPassword, btnTogglePassword);

        // Add components to form panel
        formPanel.add(jLabel3);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(jLabel2);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        formPanel.add(usernamePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Forgot password link
        lblForgotPassword.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblForgotPassword.setForeground(new java.awt.Color(51, 51, 255));
        lblForgotPassword.setText("Forgot Password?");
        lblForgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(lblForgotPassword);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Login button
        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnLogin.setText("Login");
        btnLogin.setBackground(new java.awt.Color(239, 83, 80));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(400, 45));
        btnLogin.setPreferredSize(new Dimension(400, 45));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(btnLogin);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Back button
        btnBack.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnBack.setText("Back");
        btnBack.setBackground(new java.awt.Color(240, 240, 240));
        btnBack.setForeground(new java.awt.Color(108, 117, 125));
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setOpaque(true);
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBack.setMaximumSize(new Dimension(400, 45));
        btnBack.setPreferredSize(new Dimension(400, 45));
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(btnBack);

        // Add form panel to main panel
        jPanel1.add(formPanel);
        getContentPane().add(jPanel1);

        pack();
        setLocationRelativeTo(null);
        
        // Set up action listeners
        setupActionListeners();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}