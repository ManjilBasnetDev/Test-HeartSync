package heartsync.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ChatWindow extends JFrame {
    private JPanel mainPanel;
    private JPanel chatPanel;
    private JScrollPane scrollPane;
    private JTextField messageField;
    private int currentUserId;
    private int otherUserId;

    public ChatWindow(int currentUserId, int otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;

        setTitle("HeartSync - Chat");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250, 245, 250));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(128, 0, 64));
        header.setPreferredSize(new Dimension(400, 60));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(233, 54, 128)));

        JLabel nameLabel = new JLabel("User " + otherUserId);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        backBtn.setBackground(new Color(128, 0, 64));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setOpaque(true);
        backBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());

        header.add(nameLabel, BorderLayout.WEST);
        header.add(backBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Chat area
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(250, 245, 250));

        // Add some dummy messages
        addMessage("Hey, how are you?", false);
        addMessage("I'm good, thanks! How about you?", true);
        addMessage("I'm doing great! Would you like to meet for coffee sometime?", false);

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        // Message input area
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(233, 54, 128)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        messageField = new JTextField();
        messageField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 54, 128)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(new Color(233, 54, 128));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> sendMessage());

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Add enter key listener to message field
        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
    }

    private void addMessage(String text, boolean isCurrentUser) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setOpaque(false);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel bubblePanel = new JPanel(new BorderLayout());
        bubblePanel.setBackground(isCurrentUser ? new Color(233, 54, 128) : Color.WHITE);
        bubblePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 54, 128), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JLabel messageLabel = new JLabel(text);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(isCurrentUser ? Color.WHITE : new Color(51, 51, 51));
        bubblePanel.add(messageLabel, BorderLayout.CENTER);

        if (isCurrentUser) {
            messagePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            bubblePanel.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
        } else {
            messagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            bubblePanel.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
        }

        messagePanel.add(bubblePanel);
        chatPanel.add(messagePanel);
        chatPanel.add(Box.createVerticalStrut(5));

        // Scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            addMessage(message, true);
            messageField.setText("");
            
            // Simulate a reply after 1 second
            Timer timer = new Timer(1000, e -> {
                addMessage("Thanks for your message! I'll get back to you soon.", false);
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatWindow(1, 2).setVisible(true);
        });
    }
} 