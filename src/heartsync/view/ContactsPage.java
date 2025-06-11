package heartsync.view;

import heartsync.dao.ContactDAO;
import heartsync.database.DatabaseConnection;
import heartsync.model.Contact;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;

public class ContactsPage extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(ContactsPage.class.getName());
    private final JTextArea fullNameField;
    private final JTextArea emailField;
    private final JTextArea messageArea;
    private final JButton sendButton;
    private final Border defaultBorder;
    private final Border errorBorder;
    private final ContactDAO contactDAO;

    public ContactsPage() {
        // Initialize all final fields first
        this.fullNameField = new JTextArea(1, 20);
        this.emailField = new JTextArea(1, 20);
        this.messageArea = new JTextArea();
        this.sendButton = createSendButton();
        
        // Initialize borders
        this.defaultBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
        this.errorBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 89, 89), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
        
        // Initialize DAO
        try {
            this.contactDAO = new ContactDAO();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize ContactDAO", e);
            throw new RuntimeException("Failed to initialize ContactDAO", e);
        }
        
        // Set up the panel
        setLayout(new BorderLayout());
        setBackground(new Color(255, 240, 245));
        
        // Create and add components
        setupComponents();
        
        // Test database connection after component initialization
        SwingUtilities.invokeLater(this::testDatabaseConnection);
    }
    
    private JButton createSendButton() {
        return new JButton("Send Message") {
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
    }
    
    private void setupComponents() {
        // Create a main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(255, 240, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));
        
        // Create form container
        JPanel formContainer = createFormContainer();
        
        // Add form elements
        setupFormElements(formContainer);
        
        // Center the form container
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(formContainer);
        mainPanel.add(Box.createVerticalGlue());
        
        // Add the main panel to the center
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createFormContainer() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        container.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        return container;
    }
    
    private void setupFormElements(JPanel formContainer) {
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
        
        // Setup text fields
        setupTextField(fullNameField);
        setupTextField(emailField);
        setupTextArea(messageArea);
        
        // Setup send button
        setupSendButton();
        
        // Required fields note
        JLabel requiredNote = new JLabel("* Required fields");
        requiredNote.setFont(new Font("SansSerif", Font.ITALIC, 12));
        requiredNote.setForeground(new Color(128, 128, 128));
        requiredNote.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components to form container
        addComponentsToForm(formContainer, titleLabel, subtitleLabel, requiredNote);
    }
    
    private void setupTextField(JTextArea field) {
        field.setLineWrap(true);
        field.setWrapStyleWord(true);
        field.setBorder(defaultBorder);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBackground(new Color(245, 245, 245));
        field.setForeground(Color.BLACK);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }
    
    private void setupTextArea(JTextArea area) {
        area.setRows(5);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(defaultBorder);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setBackground(new Color(245, 245, 245));
        area.setForeground(Color.BLACK);
    }
    
    private void setupSendButton() {
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
    }
    
    private void addComponentsToForm(JPanel formContainer, JLabel titleLabel, 
                                   JLabel subtitleLabel, JLabel requiredNote) {
        formContainer.add(titleLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(subtitleLabel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 35)));
        
        // Add name field
        addFieldToForm(formContainer, fullNameField, "Full Name *");
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Add email field
        addFieldToForm(formContainer, emailField, "Email Address *");
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Add message field
        addFieldToForm(formContainer, messageArea, "Your Message *");
        formContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Add button and note
        formContainer.add(sendButton);
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        formContainer.add(requiredNote);
    }
    
    private void addFieldToForm(JPanel formContainer, JTextArea field, String labelText) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(new Color(70, 70, 70));
        fieldPanel.add(label, BorderLayout.WEST);
        
        formContainer.add(fieldPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JScrollPane scrollPane = new JScrollPane(field);
        scrollPane.setBorder(null);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 
            field == messageArea ? 120 : 40));
        formContainer.add(scrollPane);
    }
    
    private void testDatabaseConnection() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null || conn.isClosed()) {
                LOGGER.warning("Could not establish database connection");
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    "Could not connect to database. Some features may not work.",
                    "Database Error",
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection error", e);
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                "Database connection error: " + e.getMessage(),
                "Database Error",
                JOptionPane.WARNING_MESSAGE);
        }
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
                } else if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                    errors.append("• Please enter a valid email address\n");
                    emailField.setBorder(errorBorder);
                    hasErrors = true;
                }
        
                // Validate Message
                if (message.isEmpty()) {
                    errors.append("• Message is required\n");
                    messageArea.setBorder(errorBorder);
                    hasErrors = true;
                }
        
                if (hasErrors) {
                    JOptionPane.showMessageDialog(this, errors.toString(), "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                // Submit the contact form
                Contact contact = new Contact(fullName, email, message);
                try {
                    contactDAO.saveContact(contact);
                    JOptionPane.showMessageDialog(this, "Thank you for your message!\nWe'll get back to you soon.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error saving contact", e);
                    JOptionPane.showMessageDialog(this, "An error occurred while saving your message.\nPlease try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        
            private void clearForm() {
                fullNameField.setText("");
                emailField.setText("");
                messageArea.setText("");
            }
        }