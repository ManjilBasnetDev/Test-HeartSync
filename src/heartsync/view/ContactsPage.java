package heartsync.view;

import heartsync.model.Contact;
import heartsync.model.User;
import javax.swing.*;
import java.util.List;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ContactsPage extends JPanel {
    private final User currentUser;
    private List<Contact> contacts;
    private boolean dialogShown = false;

    public ContactsPage(User user) {
        this.currentUser = user;
        initComponents();
        
        // Add a component listener to detect when the panel becomes visible
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (!dialogShown) {
                    loadContacts();
                    dialogShown = true;
                }
            }
        });
    }

    private void loadContacts() {
        try {
            // TODO: Implement Firebase contact loading
            // For now, show a message that this feature is coming soon
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), 
                    "Contact management feature is coming soon!", 
                    "Feature Coming Soon", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), 
                "Error loading contacts: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        // Implementation of initComponents method
    }
}