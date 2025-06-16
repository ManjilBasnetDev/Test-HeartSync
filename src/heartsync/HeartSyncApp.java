package heartsync;

import heartsync.util.DatabaseConnection;
import heartsync.view.LoginView;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

public class HeartSyncApp {
    public static void main(String[] args) {
        // Initialize database
        DatabaseConnection.getConnection();

        // Set up look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start the application
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });

        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseConnection.closeConnection();
        }));
    }
} 