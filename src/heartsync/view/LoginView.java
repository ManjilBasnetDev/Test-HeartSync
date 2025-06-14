/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package heartsync.view;

/**
 *
 * @author manjil-basnet
 */
public class LoginView extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginView.class.getName());

    /**
     * Creates new form LoginView
     */
    public LoginView() {
        initComponents();
        styleButtons();
        // --- Back Button Logic ---
        jButton1.addActionListener(evt -> dispose());
        // --- Show/Hide Toggle Logic ---
        jPasswordField1.setEchoChar('\u2022');
        jToggleButton1.setText("Show");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (jToggleButton1.isSelected()) {
                    jPasswordField1.setEchoChar((char) 0);
                    jToggleButton1.setText("Hide");
                } else {
                    jPasswordField1.setEchoChar('\u2022');
                    jToggleButton1.setText("Show");
                }
            }
        });
        // --- Make Forgot Password Clickable ---
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showMessage("Forgot Password clicked! (Implement recovery flow here)", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        });
        // Login button's action is handled by controller; no listener here.
    }

    // Helper for showing popups
    private void showMessage(String message, int messageType) {
        javax.swing.JOptionPane.showMessageDialog(this, message, "HeartSync Login", messageType);
    }

    private void styleButtons() {
        // Style Login button
        jButton2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 15));
        jButton2.setForeground(java.awt.Color.WHITE);
        jButton2.setPreferredSize(new java.awt.Dimension(150, 40));
        jButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        jButton2.setContentAreaFilled(false);
        jButton2.setFocusPainted(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setOpaque(false);
        jButton2.setUI(new ModernButtonUI(new java.awt.Color(229, 89, 36), new java.awt.Color(240, 100, 50), new java.awt.Color(200, 70, 20)));

        // Style Back button
        jButton1.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 15));
        jButton1.setForeground(java.awt.Color.WHITE);
        jButton1.setPreferredSize(new java.awt.Dimension(150, 40));
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setOpaque(false);
        jButton1.setUI(new ModernButtonUI(new java.awt.Color(109, 117, 122), new java.awt.Color(130, 130, 130), new java.awt.Color(80, 80, 80)));
    }

    // --- ModernButtonUI class for custom button look ---
    private static class ModernButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
        private final java.awt.Color normalColor;
        private final java.awt.Color hoverColor;
        private final java.awt.Color pressedColor;
        public ModernButtonUI(java.awt.Color normal, java.awt.Color hover, java.awt.Color pressed) {
            this.normalColor = normal;
            this.hoverColor = hover;
            this.pressedColor = pressed;
        }
        @Override
        public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
            javax.swing.AbstractButton b = (javax.swing.AbstractButton) c;
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            java.awt.Color fill = normalColor;
            if (b.getModel().isPressed()) fill = pressedColor;
            else if (b.getModel().isRollover()) fill = hoverColor;
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 25, 25);
            java.awt.FontMetrics fm = g2.getFontMetrics();
            String text = b.getText();
            java.awt.geom.Rectangle2D r = fm.getStringBounds(text, g2);
            int x = (c.getWidth() - (int) r.getWidth()) / 2;
            int y = (c.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
            g2.setFont(b.getFont().deriveFont(java.awt.Font.BOLD, 18f));
g2.setColor(java.awt.Color.WHITE);
g2.drawString(text, x, y);
            g2.dispose();
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
        jPanel2 = new javax.swing.JPanel();
jPanel2.setPreferredSize(new java.awt.Dimension(600, 520));
jPanel2.setMinimumSize(new java.awt.Dimension(600, 520));
jPanel2.setMaximumSize(new java.awt.Dimension(600, 520));
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jToggleButton1 = new javax.swing.JToggleButton();
jToggleButton1.setPreferredSize(new java.awt.Dimension(70, 32));
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));
        jPanel1.setForeground(new java.awt.Color(255, 204, 204));

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 102, 102));
        jLabel1.setText("Heart");

        jLabel2.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Sync");

        jLabel3.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel3.setText("Username:");

        jLabel4.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel4.setText("Password:");

        jToggleButton1.setText("Show");

        jButton1.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jButton1.setText("Back");

        jButton2.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jButton2.setText("Login");

        jLabel5.setForeground(new java.awt.Color(102, 153, 255));
        jLabel5.setText("Forgot Password?");

        jLabel6.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Welcome Back!");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3)
                            .addComponent(jTextField1)
                            .addComponent(jLabel4)
                            .addComponent(jPasswordField1))
                        .addGap(18, 18, 18)
                        .addComponent(jToggleButton1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addComponent(jLabel5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(250, 250, 250)
                        .addComponent(jLabel6)))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(31, 31, 31)
                .addComponent(jLabel6)
                .addGap(39, 39, 39)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton1))
                .addGap(27, 27, 27)
                .addComponent(jLabel5)
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(92, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new LoginView().setVisible(true));
    }

    
    // ----- Controller Integration Methods -----
    public void addLoginButtonListener(java.awt.event.ActionListener l) {
        jButton2.addActionListener(l);
    }

    public void addBackButtonListener(java.awt.event.ActionListener l) {
        jButton1.addActionListener(l);
    }

    public void addShowPasswordButtonListener(java.awt.event.ActionListener l) {
        jToggleButton1.addActionListener(l);
    }

    public void addForgotPasswordListener(java.awt.event.MouseListener l) {
        jLabel5.addMouseListener(l);
    }

    public void addUsernameFieldFocusListener(java.awt.event.FocusListener l) {
        jTextField1.addFocusListener(l);
    }

    public String getUsername() {
        return jTextField1.getText().trim();
    }

    public String getPassword() {
        return new String(jPasswordField1.getPassword());
    }

    public void togglePasswordVisibility() {
        if (jToggleButton1.isSelected()) {
            jPasswordField1.setEchoChar((char) 0);
            jToggleButton1.setText("Hide");
        } else {
            jPasswordField1.setEchoChar('\u2022');
            jToggleButton1.setText("Show");
        }
    }

    // Overloaded showMessage to accept custom title (used by controller)
    public void showMessage(String message, String title, int messageType) {
        javax.swing.JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // Utility to clear input fields
    public void clearFields() {
        jTextField1.setText("");
        jPasswordField1.setText("");
        jTextField1.requestFocusInWindow();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
