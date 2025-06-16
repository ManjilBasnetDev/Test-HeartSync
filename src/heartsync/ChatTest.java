package heartsync;

import heartsync.view.ChatWindow;
import javax.swing.SwingUtilities;

public class ChatTest {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Create a test chat window with user IDs 1 and 2
            ChatWindow chatWindow = new ChatWindow(1, 2);
            chatWindow.setVisible(true);
            
            // Set the window location to center of screen
            chatWindow.setLocationRelativeTo(null);
        });
    }
} 