package messagesystem.view;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import messagesystem.model.Message;
import messagesystem.model.User;
import messagesystem.view.components.RoundedButton;

public class ChatWindow extends JFrame {
    private User chatPartner;
    private List<Message> messages;
    private JPanel chatArea;
    private JTextField messageInput;
    private JScrollPane scrollPane;

    public ChatWindow(User chatPartner) {
        this.chatPartner = chatPartner;
        this.messages = new ArrayList<>();
        setupWindow();
        initializeComponents();
        loadDummyMessages();
    }

    private void setupWindow() {
        setTitle("Chat with " + chatPartner.getUsername());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(0, 0));

        // Header with gradient
        JPanel header = createGradientHeader();
        add(header, BorderLayout.NORTH);

        // Chat area
        chatArea = new JPanel();
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        chatArea.setBackground(ColorScheme.CHAT_BACKGROUND);
        chatArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Message input area with gradient
        JPanel inputPanel = createGradientInputPanel();
        add(inputPanel, BorderLayout.SOUTH);
    }

    private JPanel createGradientHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, ColorScheme.HEADER_FOOTER,
                    getWidth(), 0, new Color(169, 0, 169)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(getWidth(), 60));
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));

        // Profile picture placeholder with circular background emojis
        JLabel profilePic = new JLabel("👤") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(ColorScheme.SIDE_PANEL);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                
                super.paintComponent(g);
            }
        };
        profilePic.setPreferredSize(new Dimension(40, 40));
        profilePic.setHorizontalAlignment(SwingConstants.CENTER);
        profilePic.setFont(new Font("Dialog", Font.PLAIN, 24));
        profilePic.setForeground(new Color(80, 80, 80));
        header.add(profilePic);

        JLabel username = new JLabel(chatPartner.getUsername());
        username.setFont(new Font("Dialog", Font.BOLD, 16));
        username.setForeground(Color.WHITE);
        header.add(username);

        return header;
    }

    private JPanel createGradientInputPanel() {
        JPanel inputPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, ColorScheme.HEADER_FOOTER,
                    getWidth(), 0, new Color(169, 0, 169)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        inputPanel.setLayout(new BorderLayout(10, 0));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        messageInput = new JTextField();
        messageInput.setPreferredSize(new Dimension(getWidth(), 40));
        messageInput.setBorder(new EmptyBorder(5, 10, 5, 10));
        inputPanel.add(messageInput, BorderLayout.CENTER);

        RoundedButton sendButton = new RoundedButton("Send");
        sendButton.addActionListener(e -> sendMessage());
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add action listener for Enter key
        messageInput.addActionListener(e -> sendMessage());

        return inputPanel;
    }

    private void loadDummyMessages() {
        addMessage(new Message("Hi there! 👋", chatPartner, false));
        addMessage(new Message("Hello! Nice to meet you!", null, true));
        addMessage(new Message("How are you doing today?", chatPartner, false));
    }

    private void sendMessage() {
        String text = messageInput.getText().trim();
        if (!text.isEmpty()) {
            addMessage(new Message(text, null, true));
            messageInput.setText("");
        }
    }

    private void addMessage(Message message) {
        messages.add(message);
        
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setBackground(ColorScheme.CHAT_BACKGROUND);
        
        JTextArea messageText = new JTextArea(message.getContent());
        messageText.setEditable(false);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setOpaque(false);
        
        JPanel bubblePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                RoundRectangle2D bubble = new RoundRectangle2D.Float(
                    0, 0, getWidth() - 1, getHeight() - 1, 15, 15
                );
                g2.fill(bubble);
            }
        };
        bubblePanel.setLayout(new BorderLayout());
        bubblePanel.add(messageText, BorderLayout.CENTER);
        bubblePanel.setBorder(new EmptyBorder(8, 12, 8, 12));
        
        if (message.isFromCurrentUser()) {
            bubblePanel.setBackground(ColorScheme.MESSAGE_BUBBLE_SENT);
            messagePanel.add(bubblePanel, BorderLayout.EAST);
        } else {
            bubblePanel.setBackground(ColorScheme.MESSAGE_BUBBLE_RECEIVED);
            messagePanel.add(bubblePanel, BorderLayout.WEST);
        }
        
        chatArea.add(messagePanel);
        chatArea.add(Box.createRigidArea(new Dimension(0, 10)));
        
        chatArea.revalidate();
        chatArea.repaint();
        
        // Scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
} 