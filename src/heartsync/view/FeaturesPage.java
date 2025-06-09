/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package heartsync.view;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author manjil-basnet
 */
public class FeaturesPage extends JFrame {

    /**
     * Creates new form FeaturesPage
     */
    public FeaturesPage() {
        setTitle("HeartSync Features");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Add title
        JLabel titleLabel = new JLabel("HeartSync Features");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Add features
        String[] features = {
            "Smart Matching Algorithm",
            "Real-time Chat",
            "Profile Customization",
            "Privacy Controls",
            "Event Planning",
            "Photo Sharing"
        };
        
        for (String feature : features) {
            JPanel featurePanel = new JPanel();
            featurePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            featurePanel.setBackground(Color.WHITE);
            featurePanel.setMaximumSize(new Dimension(600, 50));
            
            JLabel bulletLabel = new JLabel("• ");
            bulletLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            
            featurePanel.add(bulletLabel);
            featurePanel.add(featureLabel);
            mainPanel.add(featurePanel);
        }
        
        // Add to scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        add(scrollPane);
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
            java.util.logging.Logger.getLogger(FeaturesPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FeaturesPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FeaturesPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FeaturesPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FeaturesPage().setVisible(true);
            }
        });
    }
}