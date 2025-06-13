package heartsyncdatingapp.view;

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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import heartsyncdatingapp.controller.UserProfileController;
import heartsyncdatingapp.database.DatabaseManagerProfile;
import heartsyncdatingapp.model.UserProfile;

public class MoreInfoView extends JFrame {
    private UserProfileController controller;
    private Map<String, List<JCheckBox>> hobbyCategories;
    private String selectedRelation = "";
    private JButton hobbiesButton;
    private ButtonGroup relationButtonGroup;
    private JLabel selectedHobbiesLabel;

    public MoreInfoView(UserProfileController controller) {
        this.controller = controller;
        this.hobbyCategories = new HashMap<>();
        initializeHobbyCategories();
        initializeUI();
    }

    private void initializeHobbyCategories() {
        // Sports & Fitness
        List<JCheckBox> sports = new ArrayList<>();
        sports.add(new JCheckBox("Football"));
        sports.add(new JCheckBox("Basketball"));
        sports.add(new JCheckBox("Tennis"));
        sports.add(new JCheckBox("Swimming"));
        sports.add(new JCheckBox("Yoga"));
        sports.add(new JCheckBox("Gym & Fitness"));
        sports.add(new JCheckBox("Running"));
        sports.add(new JCheckBox("Cricket"));
        hobbyCategories.put("Sports & Fitness", sports);

        // Arts & Creativity
        List<JCheckBox> arts = new ArrayList<>();
        arts.add(new JCheckBox("Painting"));
        arts.add(new JCheckBox("Music"));
        arts.add(new JCheckBox("Dancing"));
        arts.add(new JCheckBox("Photography"));
        arts.add(new JCheckBox("Writing"));
        arts.add(new JCheckBox("Crafts"));
        arts.add(new JCheckBox("Singing"));
        arts.add(new JCheckBox("Digital Art"));
        hobbyCategories.put("Arts & Creativity", arts);

        // Entertainment & Media
        List<JCheckBox> entertainment = new ArrayList<>();
        entertainment.add(new JCheckBox("Movies"));
        entertainment.add(new JCheckBox("Gaming"));
        entertainment.add(new JCheckBox("Reading"));
        entertainment.add(new JCheckBox("TV Series"));
        entertainment.add(new JCheckBox("Anime/Manga"));
        entertainment.add(new JCheckBox("Podcasts"));
        entertainment.add(new JCheckBox("Social Media"));
        entertainment.add(new JCheckBox("Blogging"));
        hobbyCategories.put("Entertainment & Media", entertainment);

        // Food & Culinary
        List<JCheckBox> culinary = new ArrayList<>();
        culinary.add(new JCheckBox("Cooking"));
        culinary.add(new JCheckBox("Baking"));
        culinary.add(new JCheckBox("Wine Tasting"));
        culinary.add(new JCheckBox("Food Photography"));
        culinary.add(new JCheckBox("Restaurant Hopping"));
        culinary.add(new JCheckBox("BBQ & Grilling"));
        culinary.add(new JCheckBox("International Cuisine"));
        culinary.add(new JCheckBox("Coffee/Tea Culture"));
        hobbyCategories.put("Food & Culinary", culinary);

        // Travel & Adventure
        List<JCheckBox> travel = new ArrayList<>();
        travel.add(new JCheckBox("Backpacking"));
        travel.add(new JCheckBox("Road Trips"));
        travel.add(new JCheckBox("Hiking"));
        travel.add(new JCheckBox("Camping"));
        travel.add(new JCheckBox("Beach Life"));
        travel.add(new JCheckBox("Mountain Climbing"));
        travel.add(new JCheckBox("Cultural Travel"));
        travel.add(new JCheckBox("Adventure Sports"));
        hobbyCategories.put("Travel & Adventure", travel);

        // Lifestyle & Wellness
        List<JCheckBox> lifestyle = new ArrayList<>();
        lifestyle.add(new JCheckBox("Meditation"));
        lifestyle.add(new JCheckBox("Pet Care"));
        lifestyle.add(new JCheckBox("Fashion"));
        lifestyle.add(new JCheckBox("Gardening"));
        lifestyle.add(new JCheckBox("Interior Design"));
        lifestyle.add(new JCheckBox("Volunteering"));
        lifestyle.add(new JCheckBox("Self-Development"));
        lifestyle.add(new JCheckBox("Environmental Care"));
        hobbyCategories.put("Lifestyle & Wellness", lifestyle);
    }

    private void initializeUI() {
        setTitle("HeartSync - Tell Us More");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        // Main panel with pink background
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
        JLabel headerLabel = new JLabel("TELL US MORE", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        formPanel.add(headerLabel, gbc);

        // Hobbies Section
        JPanel hobbiesSection = new JPanel(new BorderLayout(10, 10));
        hobbiesSection.setOpaque(false);
        
        JLabel hobbiesLabel = new JLabel("HOBBIES & INTERESTS");
        hobbiesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        hobbiesSection.add(hobbiesLabel, BorderLayout.NORTH);
        
        selectedHobbiesLabel = new JLabel("No hobbies selected yet");
        selectedHobbiesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        selectedHobbiesLabel.setForeground(Color.GRAY);
        
        hobbiesButton = new JButton("Select Hobbies");
        styleButton(hobbiesButton);
        hobbiesButton.setPreferredSize(new Dimension(200, 40));
        
        JPanel hobbiesButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hobbiesButtonPanel.setOpaque(false);
        hobbiesButtonPanel.add(hobbiesButton);
        hobbiesButtonPanel.add(selectedHobbiesLabel);
        
        hobbiesSection.add(hobbiesButtonPanel, BorderLayout.CENTER);
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(hobbiesSection, gbc);

        // Relation Choice Section
        JLabel relationLabel = new JLabel("RELATIONSHIP PREFERENCE");
        relationLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.insets = new Insets(20, 0, 10, 0);
        formPanel.add(relationLabel, gbc);

        // Modern radio button group for relation choices
        JPanel relationPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        relationPanel.setOpaque(false);
        relationButtonGroup = new ButtonGroup();
        
        String[] relations = {"CASUAL", "SERIOUS", "FRIENDS WITH BENEFITS"};
        for (String relation : relations) {
            JRadioButton radioBtn = new JRadioButton(relation);
            radioBtn.setFont(new Font("Arial", Font.PLAIN, 16));
            radioBtn.setOpaque(false);
            radioBtn.setFocusPainted(false);
            radioBtn.addActionListener(e -> {
                selectedRelation = relation;
                controller.setRelationChoice(relation);
            });
            
            // Custom radio button UI
            radioBtn.setIcon(createRadioIcon(false));
            radioBtn.setSelectedIcon(createRadioIcon(true));
            radioBtn.setRolloverIcon(createRadioRolloverIcon());
            
            relationButtonGroup.add(radioBtn);
            relationPanel.add(radioBtn);
        }

        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(relationPanel, gbc);

        // Finish button
        JButton finishButton = new JButton("FINISH");
        styleButton(finishButton);
        finishButton.setPreferredSize(new Dimension(200, 50));
        finishButton.addActionListener(e -> handleFinish());
        
        JPanel finishPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        finishPanel.setOpaque(false);
        finishPanel.add(finishButton);
        
        gbc.insets = new Insets(20, 0, 0, 0);
        formPanel.add(finishPanel, gbc);

        // Add form panel to container
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(containerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        pack();
        
        // Set minimum size to prevent window from becoming too small
        setMinimumSize(new Dimension(600, 500));
        
        setupHobbiesDialog();
    }

    private ImageIcon createRadioIcon(boolean selected) {
        int size = 24;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (selected) {
            g2d.setColor(new Color(219, 112, 147));
            g2d.fillOval(0, 0, size - 1, size - 1);
            g2d.setColor(Color.WHITE);
            g2d.fillOval(size/4, size/4, size/2, size/2);
        } else {
            g2d.setColor(Color.GRAY);
            g2d.drawOval(0, 0, size - 1, size - 1);
        }
        
        g2d.dispose();
        return new ImageIcon(image);
    }

    private ImageIcon createRadioRolloverIcon() {
        int size = 24;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(219, 112, 147, 100));
        g2d.fillOval(0, 0, size - 1, size - 1);
        g2d.setColor(new Color(219, 112, 147));
        g2d.drawOval(0, 0, size - 1, size - 1);
        
        g2d.dispose();
        return new ImageIcon(image);
    }

    private void setupHobbiesDialog() {
        hobbiesButton.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "Select Your Hobbies", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(this);

            // Header panel with counter
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(219, 112, 147));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            
            JLabel titleLabel = new JLabel("Select 5-12 Hobbies");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel, BorderLayout.WEST);
            
            JLabel counterLabel = new JLabel("Selected: 0/12");
            counterLabel.setFont(new Font("Arial", Font.BOLD, 16));
            counterLabel.setForeground(Color.WHITE);
            headerPanel.add(counterLabel, BorderLayout.EAST);
            
            dialog.add(headerPanel, BorderLayout.NORTH);

            // Main content panel
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(Color.WHITE);

            // Track selected count
            final int[] selectedCount = {0};

            // Add hobby categories
            for (Map.Entry<String, List<JCheckBox>> entry : hobbyCategories.entrySet()) {
                JPanel categoryPanel = new JPanel();
                categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
                categoryPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(219, 112, 147), 2),
                        entry.getKey(),
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new Font("Arial", Font.BOLD, 16),
                        new Color(219, 112, 147)
                    ),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                categoryPanel.setBackground(Color.WHITE);

                JPanel checkboxGrid = new JPanel(new GridLayout(0, 4, 15, 10));
                checkboxGrid.setBackground(Color.WHITE);

                for (JCheckBox checkbox : entry.getValue()) {
                    checkbox.setFont(new Font("Arial", Font.PLAIN, 14));
                    checkbox.setBackground(Color.WHITE);
                    
                    // Reset the checkbox state
                    checkbox.setSelected(false);
                    
                    checkbox.addItemListener(itemEvent -> {
                        if (itemEvent.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                            if (selectedCount[0] >= 12) {
                                checkbox.setSelected(false);
                                JOptionPane.showMessageDialog(dialog,
                                    "You can only select up to 12 hobbies!",
                                    "Selection Limit Reached",
                                    JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            selectedCount[0]++;
                        } else {
                            selectedCount[0]--;
                        }
                        counterLabel.setText(String.format("Selected: %d/12", selectedCount[0]));
                    });
                    checkboxGrid.add(checkbox);
                }

                categoryPanel.add(checkboxGrid);
                contentPanel.add(categoryPanel);
                contentPanel.add(Box.createVerticalStrut(15));
            }

            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            dialog.add(scrollPane, BorderLayout.CENTER);

            // Footer panel with save button
            JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            footerPanel.setBackground(Color.WHITE);
            footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            
            JButton saveButton = new JButton("Save Selection");
            styleButton(saveButton);
            saveButton.setPreferredSize(new Dimension(200, 45));
            
            saveButton.addActionListener(ev -> {
                if (selectedCount[0] < 5) {
                    JOptionPane.showMessageDialog(dialog,
                        "Please select at least 5 hobbies!",
                        "Insufficient Selection",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                List<String> selectedHobbies = new ArrayList<>();
                for (List<JCheckBox> checkboxes : hobbyCategories.values()) {
                    for (JCheckBox checkbox : checkboxes) {
                        if (checkbox.isSelected()) {
                            selectedHobbies.add(checkbox.getText());
                        }
                    }
                }
                controller.updateHobbies(selectedHobbies);
                selectedHobbiesLabel.setText(selectedCount[0] + " hobbies selected");
                selectedHobbiesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                selectedHobbiesLabel.setForeground(new Color(219, 112, 147));
                dialog.dispose();
            });

            footerPanel.add(saveButton);
            dialog.add(footerPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
        });
    }

    private void handleFinish() {
        // Validate selections
        if (selectedRelation.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select your relationship preference!",
                "Missing Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> selectedHobbies = new ArrayList<>();
        for (List<JCheckBox> checkboxes : hobbyCategories.values()) {
            for (JCheckBox checkbox : checkboxes) {
                if (checkbox.isSelected()) {
                    selectedHobbies.add(checkbox.getText());
                }
            }
        }

        if (selectedHobbies.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select your hobbies and interests!",
                "Missing Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Proceed with saving
        try {
            UserProfile profile = controller.getModel();
            DatabaseManagerProfile dbManager = DatabaseManagerProfile.getInstance();
            int userId = dbManager.saveUserProfile(
                controller.getCurrentUsername(),
                profile.getFullName(),
                profile.getHeight(),
                profile.getWeight(),
                profile.getCountry(),
                profile.getAddress(),
                profile.getPhoneNumber(),
                profile.getQualification(),
                profile.getGender(),
                profile.getPreferences(),
                profile.getAboutMe(),
                profile.getProfilePicPath(),
                selectedRelation,
                selectedHobbies
            );

            if (userId != -1) {
                JOptionPane.showMessageDialog(this,
                    "Profile created successfully!\nYour User ID is: " + userId,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to save profile. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error saving profile: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(AbstractButton button) {
        button.setBackground(new Color(219, 112, 147));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!button.isSelected()) {
                    button.setBackground(new Color(219, 82, 127));
                }
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!button.isSelected()) {
                    button.setBackground(new Color(219, 112, 147));
                }
            }
        });
    }
}
    