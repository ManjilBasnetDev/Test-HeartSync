package heartsync.view;

import heartsync.controller.UserProfileController;
import heartsync.model.UserProfile;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class ProfileSetupView extends JFrame {
    private UserProfileController controller;
    private JTextField nameField;
    private JSlider heightSlider;
    private JSlider weightSlider;
    private JSlider ageSlider;
    private JTextField countryField;
    private JTextField addressField;
    private JTextField phoneField;
    private JComboBox<String> qualificationComboBox;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> preferencesComboBox;
    private JTextArea aboutMeArea;
    private JLabel profilePicLabel;
    private String profilePicPath;
    private JLabel heightValueLabel;
    private JLabel weightValueLabel;
    private JLabel ageValueLabel;

    public ProfileSetupView(UserProfileController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("HeartSync - Profile Setup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        // Create the main scrollable panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 192, 203));

        // Create a container panel for the form with padding
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        containerPanel.setBackground(new Color(255, 192, 203));

        // Create form panel with white background and rounded corners
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2d.dispose();
            }
        };
        formPanel.setOpaque(false);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 30, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Header
        JLabel headerLabel = new JLabel("CREATE YOUR PROFILE", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        formPanel.add(headerLabel, gbc);

        // Profile picture section
        JPanel picPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        picPanel.setOpaque(false);
        profilePicLabel = new JLabel();
        profilePicLabel.setPreferredSize(new Dimension(150, 150));
        profilePicLabel.setBorder(BorderFactory.createLineBorder(new Color(219, 112, 147), 2));
        setDefaultProfilePic();
        picPanel.add(profilePicLabel);

        JButton uploadButton = new JButton("Upload Picture");
        styleButton(uploadButton);
        uploadButton.addActionListener(e -> handlePictureUpload());
        picPanel.add(uploadButton);

        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(picPanel, gbc);

        // Form fields
        gbc.insets = new Insets(5, 0, 5, 0);

        // Create form fields
        nameField = createStyledTextField("Full Name");
        
        // Height Slider with value label
        heightSlider = createStyledSlider(140, 220, 170);
        heightValueLabel = new JLabel(heightSlider.getValue() + " cm");
        heightValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        heightSlider.addChangeListener(e -> heightValueLabel.setText(heightSlider.getValue() + " cm"));
        
        // Weight Slider with value label
        weightSlider = createStyledSlider(40, 150, 70);
        weightValueLabel = new JLabel(weightSlider.getValue() + " kg");
        weightValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        weightSlider.addChangeListener(e -> weightValueLabel.setText(weightSlider.getValue() + " kg"));

        // Age Slider with value label
        ageSlider = createStyledSlider(18, 75, 25);
        ageValueLabel = new JLabel(ageSlider.getValue() + " years");
        ageValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ageSlider.addChangeListener(e -> {
            int age = ageSlider.getValue();
            ageValueLabel.setText(age + " years");
        });
        
        countryField = createStyledTextField("Country");
        addressField = createStyledTextField("Address");
        phoneField = createStyledTextField("Phone Number");

        // Qualification dropdown
        String[] qualifications = {
            "High School",
            "Associate's Degree",
            "Bachelor's Degree",
            "Master's Degree",
            "Doctorate (Ph.D.)",
            "Professional Degree (MD, JD, etc.)",
            "Trade School/Vocational Training",
            "Other"
        };
        qualificationComboBox = new JComboBox<>(qualifications);
        styleComboBox(qualificationComboBox);

        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        styleComboBox(genderComboBox);

        String[] preferences = {"Men", "Women", "Both"};
        preferencesComboBox = new JComboBox<>(preferences);
        styleComboBox(preferencesComboBox);

        aboutMeArea = new JTextArea(4, 20);
        aboutMeArea.setLineWrap(true);
        aboutMeArea.setWrapStyleWord(true);
        aboutMeArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(219, 112, 147), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        aboutMeArea.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add form fields with labels
        addFormField(formPanel, "Full Name:", nameField, gbc);
        
        // Add height slider with value label
        JPanel heightPanel = new JPanel(new BorderLayout(10, 0));
        heightPanel.setOpaque(false);
        heightPanel.add(heightSlider, BorderLayout.CENTER);
        heightPanel.add(heightValueLabel, BorderLayout.EAST);
        addFormField(formPanel, "Height:", heightPanel, gbc);
        
        // Add weight slider with value label
        JPanel weightPanel = new JPanel(new BorderLayout(10, 0));
        weightPanel.setOpaque(false);
        weightPanel.add(weightSlider, BorderLayout.CENTER);
        weightPanel.add(weightValueLabel, BorderLayout.EAST);
        addFormField(formPanel, "Weight:", weightPanel, gbc);

        // Add age slider with value label
        JPanel agePanel = new JPanel(new BorderLayout(10, 0));
        agePanel.setOpaque(false);
        agePanel.add(ageSlider, BorderLayout.CENTER);
        agePanel.add(ageValueLabel, BorderLayout.EAST);
        addFormField(formPanel, "Age:", agePanel, gbc);
        
        addFormField(formPanel, "Country:", countryField, gbc);
        addFormField(formPanel, "Address:", addressField, gbc);
        addFormField(formPanel, "Phone Number:", phoneField, gbc);
        addFormField(formPanel, "Qualification:", qualificationComboBox, gbc);
        addFormField(formPanel, "Gender:", genderComboBox, gbc);
        addFormField(formPanel, "Interested In:", preferencesComboBox, gbc);

        JLabel aboutMeLabel = new JLabel("About Me:");
        aboutMeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.insets = new Insets(10, 0, 5, 0);
        formPanel.add(aboutMeLabel, gbc);

        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(new JScrollPane(aboutMeArea), gbc);

        // Next button
        JButton nextButton = new JButton("NEXT");
        styleButton(nextButton);
        nextButton.setPreferredSize(new Dimension(200, 50));
        nextButton.addActionListener(this::handleNext);

        gbc.insets = new Insets(20, 0, 0, 0);
        formPanel.add(nextButton, gbc);

        // Create a scroll pane for the form
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        containerPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(containerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        pack();
        
        // Set minimum size to prevent window from becoming too small
        setMinimumSize(new Dimension(600, 500));
        
        // Make the window start at 80% of screen height if the content is larger
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxHeight = (int)(screenSize.height * 0.8);
        if (getHeight() > maxHeight) {
            setSize(getWidth(), maxHeight);
        }
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(219, 112, 147), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(219, 112, 147));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(219, 82, 127));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(219, 112, 147));
            }
        });
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(219, 112, 147), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void addFormField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.insets = new Insets(10, 0, 5, 0);
        panel.add(label, gbc);

        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(field, gbc);
    }

    private void setDefaultProfilePic() {
        ImageIcon defaultIcon = new ImageIcon(new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB));
        Graphics2D g = (Graphics2D) defaultIcon.getImage().getGraphics();
        g.setColor(new Color(219, 112, 147));
        g.fillRect(0, 0, 150, 150);
        g.dispose();
        profilePicLabel.setIcon(defaultIcon);
    }

    private void handlePictureUpload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg") ||
                       f.getName().toLowerCase().endsWith(".jpeg") ||
                       f.getName().toLowerCase().endsWith(".png");
            }
            public String getDescription() {
                return "Image files (*.jpg, *.jpeg, *.png)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            profilePicPath = selectedFile.getAbsolutePath();
            
            try {
                ImageIcon imageIcon = new ImageIcon(profilePicPath);
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                profilePicLabel.setIcon(new ImageIcon(image));
                controller.setProfilePicture(profilePicPath);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error loading image: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleNext(ActionEvent e) {
        // Validate fields
        if (nameField.getText().trim().isEmpty() ||
            countryField.getText().trim().isEmpty() ||
            addressField.getText().trim().isEmpty() ||
            phoneField.getText().trim().isEmpty() ||
            aboutMeArea.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields!",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Update model through controller
        try {
            controller.updateBasicInfo(
                nameField.getText().trim(),
                heightSlider.getValue(),
                weightSlider.getValue(),
                countryField.getText().trim(),
                addressField.getText().trim(),
                phoneField.getText().trim(),
                qualificationComboBox.getSelectedItem().toString(),
                genderComboBox.getSelectedItem().toString(),
                preferencesComboBox.getSelectedItem().toString(),
                aboutMeArea.getText().trim()
            );

            // Show next view
            this.setVisible(false);
            controller.showMoreInfoView();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving profile: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JSlider createStyledSlider(int min, int max, int initial) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, initial);
        slider.setMajorTickSpacing((max - min) / 4);
        slider.setMinorTickSpacing((max - min) / 20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBackground(Color.WHITE);
        slider.setForeground(new Color(219, 112, 147));
        slider.setFont(new Font("Arial", Font.PLAIN, 12));
        return slider;
    }

    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Create a dummy user profile for testing
                UserProfile dummyProfile = new UserProfile();
                
                // Create controller with dummy profile
                UserProfileController controller = new UserProfileController(dummyProfile);
                
                // Create and show the profile setup view
                ProfileSetupView profileSetup = new ProfileSetupView(controller);
                profileSetup.setVisible(true);
                
                // Center the window on screen
                profileSetup.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error starting Profile Setup: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
} 