package heartsync.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AdminPage extends JFrame {
    private static final int WINDOW_RADIUS = 35;
    private static final int FIELD_RADIUS = 35;
    private static final int BUTTON_RADIUS = 35;
    private static final String ADMIN_CODE = "admin123";
    
    private JPanel mainPanel;
    private JTextField adminCodeField;
    private JButton verifyButton;
    private JButton backButton;
    private Point mouseDownCompCoords;

    public AdminPage() {
        setUndecorated(true);
        initComponents();
        setupListeners();
        setupWindowDragging();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBackground(new Color(0, 0, 0, 0));
        
        // Main panel with pink background and rounded corners
        mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 192, 203));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS);
            }
        };
        mainPanel.setPreferredSize(new Dimension(400, 300));
        mainPanel.setOpaque(false);
        
        // Title
        JLabel titleLabel = new JLabel("ADMIN VERIFICATION");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 40, 400, 40);
        
        // Admin code field
        adminCodeField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), FIELD_RADIUS, FIELD_RADIUS);
                super.paintComponent(g);
            }
        };
        adminCodeField.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        adminCodeField.setBounds(50, 120, 300, 45);
        adminCodeField.setOpaque(false);
        adminCodeField.setText("ADMIN CODE");
        adminCodeField.setForeground(Color.GRAY);
        
        // Verify button
        verifyButton = new JButton("VERIFY -->") {
            private boolean isHovered = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Darker shade when hovered
                if (isHovered) {
                    g2.setColor(new Color(153, 255, 153)); // Darker green
                } else {
                    g2.setColor(new Color(204, 255, 204)); // Light green
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BUTTON_RADIUS, BUTTON_RADIUS);
                super.paintComponent(g);
            }
            
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }
        };
        verifyButton.setBorder(null);
        verifyButton.setForeground(new Color(0, 100, 0));
        verifyButton.setFont(new Font("Arial", Font.BOLD, 14));
        verifyButton.setBounds(50, 200, 145, 45);
        verifyButton.setContentAreaFilled(false);
        
        // Back button
        backButton = new JButton("BACK -->") {
            private boolean isHovered = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Darker shade when hovered
                if (isHovered) {
                    g2.setColor(new Color(255, 102, 102)); // Darker red
                } else {
                    g2.setColor(new Color(255, 127, 127)); // Coral red
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BUTTON_RADIUS, BUTTON_RADIUS);
                super.paintComponent(g);
            }
            
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }
        };
        backButton.setBorder(null);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(205, 200, 145, 45);
        backButton.setContentAreaFilled(false);
        
        // Add components
        mainPanel.add(titleLabel);
        mainPanel.add(adminCodeField);
        mainPanel.add(verifyButton);
        mainPanel.add(backButton);
        
        // Add main panel to frame
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
    
    private void setupListeners() {
        // Admin code field focus listeners
        adminCodeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (adminCodeField.getText().equals("ADMIN CODE")) {
                    adminCodeField.setText("");
                    adminCodeField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent evt) {
                if (adminCodeField.getText().isEmpty()) {
                    adminCodeField.setText("ADMIN CODE");
                    adminCodeField.setForeground(Color.GRAY);
                }
            }
        });

        // Verify button action listener
        verifyButton.addActionListener(e -> {
            String code = adminCodeField.getText();
            if (code.equals(ADMIN_CODE)) {
                JOptionPane.showMessageDialog(this,
                    "Admin verification successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
                // Here you would typically open the admin dashboard
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid admin code!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                adminCodeField.requestFocus();
            }
        });

        // Back button action listener
        backButton.addActionListener(e -> {
            Register register = new Register();
            Point location = getLocation();
            register.setLocation(location.x, location.y);
            register.setVisible(true);
            SwingUtilities.invokeLater(() -> {
                dispose(); // Dispose after register page is visible
            });
        });
    }
    
    private void setupWindowDragging() {
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
                setCursor(Cursor.getDefaultCursor());
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
        });
        
        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseDownCompCoords != null) {
                    Point currCoords = e.getLocationOnScreen();
                    setLocation(currCoords.x - mouseDownCompCoords.x, 
                              currCoords.y - mouseDownCompCoords.y);
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getY() < 50) { // Only allow dragging from the top area
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        });
    }
} 