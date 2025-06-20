package heartsync.view;

import heartsync.dao.ContactDAO;
import heartsync.model.Contact;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import javax.swing.border.Border;

public class ContactsPage extends JPanel {
    private final JTextArea fullNameField;
    private final JTextArea emailField;
    private final JTextArea messageArea;
    private final JButton sendButton;
    private final Border defaultBorder;
    private final Border errorBorder;
    private final ContactDAO contactDAO;

    public ContactsPage() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 240, 245));

        // Initialize borders
        defaultBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
        errorBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 89, 89), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );

        // Create a main panel with some padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(255, 240, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        // Create a container panel for the form with fixed width
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        formContainer.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        
        // Title
        JLabel titleLabel = new JLabel("Contact Us", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("We'd love to hear from you!", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(128, 128, 128));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a panel for each form field to align labels to the left
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel("Full Name *");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setForeground(new Color(70, 70, 70));
        namePanel.add(nameLabel, BorderLayout.WEST);
        
        // Create single-line text area for full name
        fullNameField = new JTextArea(1, 20);
        fullNameField.setLineWrap(true);
        fullNameField.setWrapStyleWord(true);
        fullNameField.setBorder(defaultBorder);
        fullNameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        fullNameField.setBackground(new Color(245, 245, 245));
        fullNameField.setForeground(Color.BLACK);
        fullNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.setBackground(Color.WHITE);
        JLabel emailLabel = new JLabel("Email Address *");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        emailLabel.setForeground(new Color(70, 70, 70));
        emailPanel.add(emailLabel, BorderLayout.WEST);
        
        // Create single-line text area for email
        emailField = new JTextArea(1, 20);
        emailField.setLineWrap(true);
        emailField.setWrapStyleWord(true);
        emailField.setBorder(defaultBorder);
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailField.setBackground(new Color(245, 245, 245));
        emailField.setForeground(Color.BLACK);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.WHITE);
        JLabel messageLabel = new JLabel("Your Message *");
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        messageLabel.setForeground(new Color(70, 70, 70));
        messagePanel.add(messageLabel, BorderLayout.WEST);
        
        messageArea = new JTextArea();
        messageArea.setRows(5);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(defaultBorder);
        messageArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageArea.setBackground(new Color(245, 245, 245));           
        messageArea.setForeground(Color.BLACK);
        
        // Create scroll panes for all text areas
        JScrollPane nameScrollPane = new JScrollPane(fullNameField);
        nameScrollPane.setBorder(null);
        nameScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JScrollPane emailScrollPane = new JScrollPane(emailField);
        emailScrollPane.setBorder(null);
        emailScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messageScrollPane.setBorder(null);
        messageScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Send Button
        sendButton = new JButton("Send Message") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(50, 100, 140));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(90, 150, 200));
                } else {
                    g2.setColor(new Color(70, 130, 180));
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D r = fm.getStringBounds(getText(), g2);
                int x = (getWidth() - (int) r.getWidth()) / 2;
                int y = (getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setForeground(Color.WHITE);
        sendButton.setPreferredSize(new Dimension(200, 45));
        sendButton.setMaximumSize(new Dimension(200, 45));
        sendButton.setBorderPainted(false);
        sendButton.setContentAreaFilled(false);
        sendButton.setFocusPainted(false);
        sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> validateAndSubmit());

        // Required fields note
        JLabel requiredNote = new JLabel("* Required fields");
        requiredNote.setFont(new Font("SansSerif", Font.ITALIC, 12));
        requiredNote.setForeground(new Color(128, 128, 128));
        requiredNote.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components with balanced padding
        formContainer.add(titleLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(subtitleLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 35)));
        
        formContainer.add(namePanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(nameScrollPane);
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));
        
        formContainer.add(emailPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(emailScrollPane);
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));
        
        formContainer.add(messagePanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(messageScrollPane);
        formContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        
        formContainer.add(sendButton);
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        formContainer.add(requiredNote);

        // Center the form container
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(formContainer);
        mainPanel.add(Box.createVerticalGlue());

        // Add the main panel to the center of the ContactsPage
        add(mainPanel, BorderLayout.CENTER);
        
        // Initialize DAO
        contactDAO = new ContactDAO();
    }

    private void validateAndSubmit() {
        // Reset borders
        fullNameField.setBorder(defaultBorder);
        emailField.setBorder(defaultBorder);
        messageArea.setBorder(defaultBorder);

        // Get values
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String message = messageArea.getText().trim();

        // Validate
        boolean hasErrors = false;
        StringBuilder errors = new StringBuilder("Please fix the following errors:\n\n");

        // Validate Full Name
        if (fullName.isEmpty()) {
            errors.append("• Full Name is required\n");
            fullNameField.setBorder(errorBorder);
            hasErrors = true;
        } else if (!fullName.contains(" ")) {
            errors.append("• Please enter both first and last name\n");
            fullNameField.setBorder(errorBorder);
            hasErrors = true;
        }

        // Validate Email
        if (email.isEmpty()) {
            errors.append("• Email Address is required\n");
            emailField.setBorder(errorBorder);
            hasErrors = true;
        } else if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            errors.append("• Please enter a valid email address\n");
            emailField.setBorder(errorBorder);
            hasErrors = true;
        }

        // Validate Message
        if (message.isEmpty()) {
            errors.append("• Message is required\n");
            messageArea.setBorder(errorBorder);
            hasErrors = true;
        } else if (message.length() < 10) {
            errors.append("• Message must be at least 10 characters\n");
            messageArea.setBorder(errorBorder);
            hasErrors = true;
        }

        if (hasErrors) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                errors.toString(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // All validation passed, try to save
        sendButton.setEnabled(false);
        sendButton.setText("Sending...");

        try {
            Contact contact = new Contact(fullName, email, message);
            if (contactDAO.saveContact(contact)) {
                // Clear form
                fullNameField.setText("");
                emailField.setText("");
                messageArea.setText("");
                
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    "Thank you! Your message has been sent successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    "Failed to send message. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                "Invalid input: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            sendButton.setEnabled(true);
            sendButton.setText("Send Message");
        }
    }
}