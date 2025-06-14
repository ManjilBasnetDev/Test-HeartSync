/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package heartsync.view;

import heartsync.dao.ReportDAO;
import heartsync.dao.UserDAO;
import heartsync.model.Report;
import heartsync.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author HP
 */
public class ReporUser extends javax.swing.JFrame {
    private int reporterId;
    private int reportedUserId;
    private final ReportDAO reportDAO;
    private final UserDAO userDAO;
    private static final Color PRIMARY_COLOR = new Color(231, 76, 60);
    private static final Color SECONDARY_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = Color.BLACK;

    /**
     * Creates new form ReporUser
     */
    public ReporUser(int reporterId, int reportedUserId) {
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
        this.reportDAO = new ReportDAO();
        this.userDAO = new UserDAO();
        initComponents();
        setupUI();
        setupReasonComboBox();
        loadUserInfo();
        setLocationRelativeTo(null);
    }

    private void setupUI() {
        // Set up the main panel
        jPanel1.setBackground(SECONDARY_COLOR);
        
        // Style the title
        jLabel1.setForeground(PRIMARY_COLOR);
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        // Style the labels
        jLabel2.setForeground(TEXT_COLOR);
        jLabel3.setForeground(TEXT_COLOR);
        jLabel4.setForeground(TEXT_COLOR);
        charCountLabel.setForeground(TEXT_COLOR);
        
        // Style the combo box
        reasonComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reasonComboBox.setBackground(Color.WHITE);
        reasonComboBox.setForeground(TEXT_COLOR);
        
        // Style the text area
        descriptionTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionTextArea.setBackground(Color.WHITE);
        descriptionTextArea.setForeground(TEXT_COLOR);
        descriptionTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Style the buttons
        styleButton(submitButton, PRIMARY_COLOR);
        styleButton(cancelButton, new Color(149, 165, 166));
        
        // Add character counter
        descriptionTextArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateCharCount(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateCharCount(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateCharCount(); }
        });
        
        // Set initial character count
        updateCharCount();
    }
    
    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
    }
    
    private void loadUserInfo() {
        User reportedUser = userDAO.getUserById(reportedUserId);
        if (reportedUser != null) {
            jLabel4.setText("Reporting: " + reportedUser.getUsername());
        }
    }
    
    private void updateCharCount() {
        int count = descriptionTextArea.getText().length();
        int maxLength = 500;
        charCountLabel.setText(count + "/" + maxLength + " characters");
        
        // Change color if approaching limit
        if (count > maxLength * 0.9) {
            charCountLabel.setForeground(PRIMARY_COLOR);
        } else {
            charCountLabel.setForeground(TEXT_COLOR);
        }
    }

    private void setupReasonComboBox() {
        String[] reasons = {
            "Harassment",
            "Bullying",
            "Inappropriate Content",
            "Spam",
            "Fake Profile",
            "Hate Speech",
            "Underage User",
            "Impersonation",
            "Threats",
            "Scam/Fraud",
            "Inappropriate Messages",
            "Privacy Violation",
            "Stalking",
            "Discrimination",
            "Sexual Harassment",
            "Violent Content",
            "Drug/Alcohol Promotion",
            "Identity Theft",
            "Account Compromise",
            "Other"
        };
        reasonComboBox.setModel(new DefaultComboBoxModel<>(reasons));
        
        // Add tooltip for each reason
        for (String reason : reasons) {
            String tooltip = getReasonTooltip(reason);
            reasonComboBox.setToolTipText(tooltip);
        }
    }
    
    private String getReasonTooltip(String reason) {
        switch (reason) {
            case "Harassment":
                return "Repeated unwanted contact or behavior that causes distress";
            case "Bullying":
                return "Intentionally harmful behavior, threats, or intimidation";
            case "Inappropriate Content":
                return "Content that violates community guidelines";
            case "Spam":
                return "Unsolicited commercial messages or repetitive content";
            case "Fake Profile":
                return "Profile created using false information or impersonation";
            case "Hate Speech":
                return "Content promoting hatred against specific groups";
            case "Underage User":
                return "User appears to be under the minimum age requirement";
            case "Impersonation":
                return "Pretending to be someone else or a public figure";
            case "Threats":
                return "Direct threats of harm or violence";
            case "Scam/Fraud":
                return "Attempting to deceive or defraud other users";
            case "Inappropriate Messages":
                return "Unwanted or offensive private messages";
            case "Privacy Violation":
                return "Sharing personal information without consent";
            case "Stalking":
                return "Excessive unwanted attention or following";
            case "Discrimination":
                return "Unfair treatment based on personal characteristics";
            case "Sexual Harassment":
                return "Unwanted sexual advances or behavior";
            case "Violent Content":
                return "Content promoting or glorifying violence";
            case "Drug/Alcohol Promotion":
                return "Promoting illegal substances or underage drinking";
            case "Identity Theft":
                return "Using someone else's identity or personal information";
            case "Account Compromise":
                return "Suspicious activity indicating account may be hacked";
            case "Other":
                return "Other violations not covered by the above categories";
            default:
                return "";
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        reasonComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        submitButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        charCountLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Report User");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 153, 153));

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 24));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Report User");

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI", 0, 14));
        jLabel2.setText("Reason for Report:");

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI", 0, 14));
        jLabel3.setText("Description:");

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI", 0, 14));
        jLabel4.setText("Reporting: ");

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(descriptionTextArea);

        submitButton.setFont(new java.awt.Font("Yu Gothic UI", 1, 14));
        submitButton.setText("Submit Report");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        cancelButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 14));
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(reasonComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(submitButton)
                        .addGap(10, 10, 10)
                        .addComponent(cancelButton)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addGap(5, 5, 5)
                .addComponent(reasonComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(submitButton)
                    .addComponent(cancelButton))
                .addContainerGap(20, Short.MAX_VALUE))
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
    }// </editor-fold>                        

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String reason = (String) reasonComboBox.getSelectedItem();
        String description = descriptionTextArea.getText().trim();
        
        // Validate input
        if (description.isEmpty()) {
            showError("Please provide a description of the issue.");
            return;
        }
        
        if (description.length() > 500) {
            showError("Description cannot exceed 500 characters.");
            return;
        }
        
        // Create report with timestamp
        Report report = new Report(reporterId, reportedUserId, reason, description);
        report.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        
        // Show confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to submit this report?",
            "Confirm Report",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (reportDAO.createReport(report)) {
                JOptionPane.showMessageDialog(this,
                    "Report submitted successfully. Our team will review it shortly.",
                    "Report Submitted",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                showError("Failed to submit report. Please try again.");
            }
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel? Any entered information will be lost.",
            "Confirm Cancel",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
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
            java.util.logging.Logger.getLogger(ReporUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReporUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReporUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReporUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // For testing purposes, using sample user IDs
                new ReporUser(1, 2).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> reasonComboBox;
    private javax.swing.JButton submitButton;
    private javax.swing.JLabel charCountLabel;
    // End of variables declaration                   
}
