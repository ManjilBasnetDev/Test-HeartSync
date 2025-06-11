package com.heartsync;

import com.heartsync.gui.ChatWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class HeartSyncApp {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            // For testing, create two chat windows
            ChatWindow chat1 = new ChatWindow(1, 2);
            chat1.setLocation(100, 100);
            chat1.setVisible(true);

            ChatWindow chat2 = new ChatWindow(2, 1);
            chat2.setLocation(720, 100);
            chat2.setVisible(true);
        });
    }
} 