/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package heartsync.view;

/**
 *
 * @author manjil-basnet
 */
import heartsync.controller.NotificationController;
import heartsync.model.Notification;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class MatchNotif extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MatchNotif.class.getName());

    private final NotificationController notificationController = new NotificationController();
    private DefaultListModel<String> notifListModel = new DefaultListModel<>();
    private JList<String> notifList;
    private JButton markReadButton;

    /**
     * Creates new form MatchNotif
     */
    public MatchNotif() {
        super("Notifications");
        initComponents();
        setupCustomUI();
        refreshNotifications();
    }

    private void setupCustomUI() {
        notifList = new JList<>(notifListModel);
        notifList.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        notifList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notifList.setCellRenderer(new NotificationCellRenderer());

        JScrollPane scrollPane = new JScrollPane(notifList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Notifications"));

        markReadButton = new JButton("Mark All as Read");
        markReadButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        markReadButton.setBackground(new Color(229, 89, 36));
        markReadButton.setForeground(Color.WHITE);
        markReadButton.addActionListener(e -> {
            notificationController.markAllAsRead();
            refreshNotifications();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(markReadButton);

        getContentPane().removeAll();
        setLayout(new BorderLayout(10, 10));
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(520, 480));
        pack();
        setLocationRelativeTo(null);

        notifList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = notifList.locationToIndex(evt.getPoint());
                    if (index >= 0 && !notifListModel.isEmpty()) {
                        String notifText = notifListModel.getElementAt(index);
                        // Try to extract user name from the notification (for demo, just show content)
                        String message;
                        if (notifText.contains("💘 Match")) {
                            message = "Open profile for: " + notifText.replaceAll(".*💘 Match: You have a new match: ", "").replaceAll("!.*", "");
                        } else if (notifText.contains("💬 Message")) {
                            message = "Open chat/profile for: " + notifText.replaceAll(".*💬 Message: New message from: ", "").replaceAll("!.*", "");
                        } else {
                            message = notifText;
                        }
                        JOptionPane.showMessageDialog(MatchNotif.this, message, "Profile", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }

    public void refreshNotifications() {
        notifListModel.clear();
        List<?> notificationsRaw = notificationController.getNotifications();
        for (Object obj : notificationsRaw) {
            if (obj instanceof Notification n) {
                String status = n.isRead() ? "(Read) " : "(New) ";
                String type = n.getType() == Notification.Type.MATCH ? "💘 Match" : "💬 Message";
                notifListModel.addElement(status + type + ": " + n.getContent());
            }
        }
        if (notifListModel.isEmpty()) {
            notifListModel.addElement("No notifications yet.");
        }
    }

    // For other views/controllers to show this window
    public static void showNotificationWindow() {
        java.awt.EventQueue.invokeLater(() -> new MatchNotif().setVisible(true));
    }

    // Optionally, allow direct push from controller
    public void showAndRefresh() {
        refreshNotifications();
        setVisible(true);
    }

    // Custom renderer for better UI
    private static class NotificationCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            if (value.toString().contains("(New)")) {
                label.setForeground(new Color(229, 89, 36));
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            } else {
                label.setForeground(Color.DARK_GRAY);
            }
            return label;
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // Add a dummy notification for demo/testing
        heartsync.controller.NotificationController notifCtrl = new heartsync.controller.NotificationController();
        notifCtrl.notifyMatch("Demo User");
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
        java.awt.EventQueue.invokeLater(() -> new MatchNotif().setVisible(true));
    }

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}
