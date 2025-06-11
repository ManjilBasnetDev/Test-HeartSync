import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

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
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(ColorScheme.HEADER_FOOTER);
        header.setPreferredSize(new Dimension(getWidth(), 60));
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));

        // Profile picture placeholder
        JLabel profilePic = new JLabel("👤");
        profilePic.setFont(new Font("Dialog", Font.PLAIN, 24));
        profilePic.setForeground(Color.WHITE);
        header.add(profilePic);

        // Username
        JLabel username = new JLabel(chatPartner.getUsername());
        username.setFont(new Font("Dialog", Font.BOLD, 16));
        username.setForeground(Color.WHITE);
        header.add(username);

        add(header, BorderLayout.NORTH);

        // Chat area
        chatArea = new JPanel();
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        chatArea.setBackground(ColorScheme.CHAT_BACKGROUND);

        scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Message input area
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(ColorScheme.HEADER_FOOTER);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        messageInput = new JTextField();
        messageInput.setPreferredSize(new Dimension(getWidth(), 40));
        inputPanel.add(messageInput, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(ColorScheme.BUTTON_BACKGROUND);
        sendButton.setForeground(ColorScheme.BUTTON_TEXT);
        sendButton.addActionListener(e -> sendMessage());
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // Add action listener for Enter key
        messageInput.addActionListener(e -> sendMessage());
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
        
        JPanel bubblePanel = new JPanel();
        bubblePanel.setLayout(new BorderLayout());
        bubblePanel.add(messageText, BorderLayout.CENTER);
        bubblePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
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