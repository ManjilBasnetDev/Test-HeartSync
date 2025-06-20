package heartsync.view;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class SecurityQuestionsDialog extends JDialog {
    private JTextField favoriteColorField;
    private JTextField firstSchoolField;
    private JLabel validationLabel;
    private JButton submitButton;
    private boolean confirmed = false;
    
    public SecurityQuestionsDialog(JFrame parent) {
        super(parent, "Security Questions", true);
        setLayout(new BorderLayout());
        setSize(600, 520);
        setResizable(false);
        setLocationRelativeTo(parent);
        
        // Main panel with pink background and rounded corners
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 219, 227));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Center panel to hold content
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel titleLabel = new JLabel("Security Questions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Info text with improved centering and styling
        JLabel infoLabel = new JLabel("<html><div style='text-align: center; font-size: 14px; color: #555;'>Please answer these security questions.<br>You'll need them if you forget your password.</div></html>");
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(15, 30, 25, 30));
        infoLabel.setMaximumSize(new Dimension(400, 60)); // Fixed width for better text flow
        
        // Form panel card with centered content
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40)); // Adjusted padding for better centering
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Favorite color question
        JLabel colorLabel = new JLabel("What is your favorite color?");
        favoriteColorField = new JTextField(20);
        styleTextField(favoriteColorField);
        
        gbc.gridy++;
        formPanel.add(colorLabel, gbc);
        
        gbc.gridy++;
        formPanel.add(favoriteColorField, gbc);
        
        // First school question
        JLabel schoolLabel = new JLabel("What was the name of your first school?");
        firstSchoolField = new JTextField(20);
        styleTextField(firstSchoolField);
        
        gbc.gridy++;
        formPanel.add(Box.createVerticalStrut(10), gbc);
        
        gbc.gridy++;
        formPanel.add(schoolLabel, gbc);
        
        gbc.gridy++;
        formPanel.add(firstSchoolField, gbc);
        
        // Button panel
        // Validation label
        validationLabel = new JLabel(" ");
        validationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        validationLabel.setForeground(new Color(200,0,0));
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(validationLabel, gbc);
        gbc.gridwidth = 1;
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        submitButton = createStyledButton("Save & Continue", new Color(229,89,36));
        submitButton.setEnabled(false);
        submitButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                JOptionPane.showMessageDialog(this, "Security questions saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
        
        JButton cancelButton = createStyledButton("Cancel", new Color(108,117,125));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
                // Build center panel with vertical glue for centering
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLabel);
        centerPanel.add(infoLabel);
        centerPanel.add(formPanel);
        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createVerticalGlue());

                // Add components to mainPanel with vertical glue for centering
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(centerPanel);
        mainPanel.add(Box.createVerticalGlue());
        
        // Add main panel to dialog
        add(mainPanel, BorderLayout.CENTER);
        
        // Set up escape key to close dialog
        getRootPane().registerKeyboardAction(
            e -> dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        // Set up enter key to submit
        getRootPane().setDefaultButton(submitButton);

        // Live validation
        DocumentListener dl = new DocumentListener() {
            private void check() {
                boolean ok = !favoriteColorField.getText().trim().isEmpty() && !firstSchoolField.getText().trim().isEmpty();
                submitButton.setEnabled(ok);
                validationLabel.setText(ok?" ":"Please fill in both answers");
            }
            public void insertUpdate(DocumentEvent e){check();}
            public void removeUpdate(DocumentEvent e){check();}
            public void changedUpdate(DocumentEvent e){check();}
        };
        favoriteColorField.getDocument().addDocumentListener(dl);
        firstSchoolField.getDocument().addDocumentListener(dl);
    }
    
    // ---------- Helper Styling Methods ----------
    private void styleTextField(JTextField tf){
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(new CompoundBorder(new LineBorder(new Color(200,200,200),1,true), new EmptyBorder(6,12,6,12)));
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.BLACK);
        tf.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e){
                tf.setBorder(new CompoundBorder(new LineBorder(new Color(229,89,36),2,true), new EmptyBorder(5,11,5,11)));
            }
            public void focusLost(FocusEvent e){
                tf.setBorder(new CompoundBorder(new LineBorder(new Color(200,200,200),1,true), new EmptyBorder(6,12,6,12)));
            }
        });
    }

    private JButton createStyledButton(String text, Color bg){
        JButton btn = new JButton(text){
            @Override
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()?bg.darker():(getModel().isRollover()?bg.brighter():bg));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI",Font.BOLD,14));
        btn.setPreferredSize(new Dimension(160,40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private boolean validateInput() {
        String color = favoriteColorField.getText().trim();
        String school = firstSchoolField.getText().trim();

        if (color.isEmpty()) {
            validationLabel.setText("Please enter your favorite color");
            favoriteColorField.requestFocus();
            return false;
        }

        if (school.isEmpty()) {
            validationLabel.setText("Please enter your first school name");
            firstSchoolField.requestFocus();
            return false;
        }

        if (color.length() < 3) {
            validationLabel.setText("Favorite color must be at least 3 characters long");
            favoriteColorField.requestFocus();
            return false;
        }

        if (school.length() < 3) {
            validationLabel.setText("School name must be at least 3 characters long");
            firstSchoolField.requestFocus();
            return false;
        }

        return true;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public String getFavoriteColor() {
        return favoriteColorField.getText().trim();
    }
    
    public String getFirstSchool() {
        return firstSchoolField.getText().trim();
    }

    public String getSecurityQuestion1() {
        return "What is your favorite color?";
    }

    public String getSecurityAnswer1() {
        return favoriteColorField.getText().trim();
    }

    public String getSecurityQuestion2() {
        return "What was the name of your first school?";
    }

    public String getSecurityAnswer2() {
        return firstSchoolField.getText().trim();
    }
}
