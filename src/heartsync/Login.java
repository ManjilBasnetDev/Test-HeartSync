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
    private javax.swing.JTextArea txtPassword;
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
        txtUsername.setText("USERNAME");
        txtUsername.setForeground(Color.GRAY);
        
        // Password field setup
        txtPassword.setLineWrap(true);
        txtPassword.setWrapStyleWord(true);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 15, 10, 15)));
        txtPassword.setRows(1);
        txtPassword.setMaximumSize(new Dimension(400, 45));
        txtPassword.setPreferredSize(new Dimension(400, 45));
        txtPassword.setText("Enter password");
        txtPassword.setForeground(Color.GRAY);
    }

    // This method ensures the JTextArea displays either bullets or actual password
    private void updatePasswordField() {
        selfEdit = true; // Prevent listener from firing during this update
        if (isPasswordVisible) {
            txtPassword.setText(actualPassword);
        } else {
            StringBuilder hidden = new StringBuilder();
            for (int i = 0; i < actualPassword.length(); i++) {
                hidden.append("•");
            }
            txtPassword.setText(hidden.toString());
        }
        // Try to restore caret position, usually to the end after a programmatic change
        txtPassword.setCaretPosition(txtPassword.getText().length());
        selfEdit = false;
    }

    private void togglePasswordVisibility() {
        if (txtPassword.getText().equals("Enter password") && actualPassword.isEmpty()) {
            return; // Don't toggle if it's just a placeholder
        }
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            btnTogglePassword.setText("Hide");
        } else {
            btnTogglePassword.setText("Show");
        }
        updatePasswordField(); // Update text based on new visibility state
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
                if (txtPassword.getText().equals("Enter password")) {
                    txtPassword.setText("");
                    txtPassword.setForeground(Color.BLACK);
                    showHideController.setActualPassword("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtPassword.getText().trim().isEmpty()) {
                    txtPassword.setForeground(Color.GRAY);
                    txtPassword.setText("Enter password");
                    showHideController.reset();
                } else {
                    showHideController.setActualPassword(txtPassword.getText());
                }
            }
        });

        // Password text change listener
        txtPassword.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                if (!txtPassword.getText().equals("Enter password")) {
                    showHideController.setActualPassword(txtPassword.getText());
                }
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                if (!txtPassword.getText().equals("Enter password")) {
                    showHideController.setActualPassword(txtPassword.getText());
                }
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                if (!txtPassword.getText().equals("Enter password")) {
                    showHideController.setActualPassword(txtPassword.getText());
                }
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
                dispose(); // Close login window
                new ForgotPassword().setVisible(true);
            }
        });
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = showHideController.getActualPassword();
        UserDAO userDAO = null;
        
        try {
            // Validation
            if (username.isEmpty() || username.equals("USERNAME")) {
                throw new IllegalArgumentException("Please enter a username");
            }
            if (password.isEmpty() || password.equals("Enter password")) {
                throw new IllegalArgumentException("Please enter a password");
            }
            
            // Username validation
            if (username.length() < 3) {
                throw new IllegalArgumentException("Username must be at least 3 characters long");
            }
            if (!username.matches("^[a-zA-Z0-9_]+$")) {
                throw new IllegalArgumentException("Username can only contain letters, numbers, and underscores");
            }
            
            // Password validation
            if (password.length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters long");
            }

            userDAO = new UserDAO();
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
                txtPassword.setText("Enter password");
                txtPassword.setForeground(Color.GRAY);
                showHideController.reset();
                txtPassword.requestFocus();
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE);
            
            // Focus the appropriate field based on the error
            if (e.getMessage().toLowerCase().contains("username")) {
                txtUsername.requestFocus();
            } else {
                txtPassword.requestFocus();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Database error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
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
        
        // Create password field instead of text area
        txtPassword = new javax.swing.JTextArea();
        
        btnLogin = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnTogglePassword = new javax.swing.JButton();

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

        // Username field
        txtUsername.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtUsername.setBackground(Color.WHITE);
        txtUsername.setOpaque(true);
        txtUsername.setForeground(Color.GRAY);
        txtUsername.setLineWrap(true);
        txtUsername.setWrapStyleWord(true);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 15, 10, 15)));
        txtUsername.setRows(1);
        txtUsername.setMaximumSize(new Dimension(400, 45));
        txtUsername.setPreferredSize(new Dimension(400, 45));

        // Add username to its panel with same spacing as password panel
        usernamePanel.add(txtUsername);
        usernamePanel.add(Box.createRigidArea(new Dimension(80, 0))); // Same width as password panel

        // Password panel setup
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setMaximumSize(new Dimension(480, 45));
        passwordPanel.setPreferredSize(new Dimension(480, 45));
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Password field
        txtPassword.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setOpaque(true);
        txtPassword.setForeground(Color.GRAY);
        txtPassword.setLineWrap(true);
        txtPassword.setWrapStyleWord(true);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 15, 10, 15)));
        txtPassword.setRows(1);
        txtPassword.setMaximumSize(new Dimension(400, 45));
        txtPassword.setPreferredSize(new Dimension(400, 45));

        // Show/Hide button
        btnTogglePassword.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnTogglePassword.setText("Show");
        btnTogglePassword.setBackground(new java.awt.Color(240, 240, 240));
        btnTogglePassword.setForeground(new java.awt.Color(108, 117, 125));
        btnTogglePassword.setBorderPainted(false);
        btnTogglePassword.setFocusPainted(false);
        btnTogglePassword.setOpaque(true);
        btnTogglePassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTogglePassword.setPreferredSize(new Dimension(70, 45));
        btnTogglePassword.setMaximumSize(new Dimension(70, 45));
        btnTogglePassword.addActionListener(e -> togglePasswordVisibility());

        // Add components to password panel with spacing
        passwordPanel.add(txtPassword);
        passwordPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        passwordPanel.add(btnTogglePassword);

        // Forgot password link
        lblForgotPassword.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblForgotPassword.setForeground(new java.awt.Color(51, 51, 255));
        lblForgotPassword.setText("Forgot Password?");
        lblForgotPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblForgotPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose(); // Close login window
                new ForgotPassword().setVisible(true);
            }
        });
        lblForgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(400, 45));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Login button with orange background like homepage
        btnLogin.setFont(new java.awt.Font("Segoe UI", 0, 16));
        btnLogin.setText("Log in");
        btnLogin.setBackground(new java.awt.Color(229, 89, 36));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(190, 45));
        btnLogin.setMaximumSize(new Dimension(190, 45));

        // Back button with gray background
        btnBack.setFont(new java.awt.Font("Segoe UI", 0, 16));
        btnBack.setText("Back");
        btnBack.setBackground(new java.awt.Color(108, 117, 125));
        btnBack.setForeground(Color.WHITE);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setOpaque(true);
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBack.setPreferredSize(new Dimension(190, 45));
        btnBack.setMaximumSize(new Dimension(190, 45));

        // Add buttons to button panel
        buttonPanel.add(btnBack);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(btnLogin);

        // Update form panel components
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(jLabel3); // HeartSync
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(jLabel2); // Welcome Back
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(usernamePanel); // Use the new username panel
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(lblForgotPassword);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalGlue()); // Add glue to push content up
        formPanel.add(Box.createVerticalStrut(20));

        // Update the layout to make the white panel fill the space
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
            .addComponent(formPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
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

        setLocationRelativeTo(null);
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        
        // Check if either field is empty or contains placeholder text
        if (username.isEmpty() || username.equals("USERNAME") || 
            password.isEmpty() || password.equals("Enter password")) {
            
            StringBuilder message = new StringBuilder("Please enter ");
            
            if (username.isEmpty() || username.equals("USERNAME")) {
                message.append("username");
                if (password.isEmpty() || password.equals("Enter password")) {
                    message.append(" and password");
                }
            } else if (password.isEmpty() || password.equals("Enter password")) {
                message.append("password");
            }
            
            JOptionPane.showMessageDialog(this,
                message.toString(),
                "Required Fields",
                JOptionPane.WARNING_MESSAGE);
                
            // Focus the empty field
            if (username.isEmpty() || username.equals("USERNAME")) {
                txtUsername.requestFocus();
            } else {
                txtPassword.requestFocus();
            }
            return;
        }

        // Username validation
        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this,
                "Username must be at least 3 characters long",
                "Invalid Username",
                JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }

        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            JOptionPane.showMessageDialog(this,
                "Username can only contain letters, numbers, and underscores",
                "Invalid Username",
                JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }
        
        // Password validation
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 6 characters long",
                "Invalid Password",
                JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }
        
        try {
            UserDAO userDAO = new UserDAO();
            if (userDAO.authenticate(username, password)) {
                JOptionPane.showMessageDialog(this, 
                    "Welcome back, " + username + "!", 
                    "Login Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close login window
                // TODO: Add code to open main application window
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Invalid username or password. Please try again.", 
                    "Login Failed", 
                    JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Unable to connect to the database. Please try again later.\nError: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

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