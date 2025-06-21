package heartsync.view;

import heartsync.dao.ChatDAO;
import heartsync.model.Chat;
import heartsync.model.User;
import heartsync.model.UserProfile;
import heartsync.database.FirebaseStorageManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;

public class ConversationView extends JFrame {

    private final UserProfile recipient;
    private final String currentUserId;
    private final ChatDAO chatDAO;
    private final String chatId;

    private JPanel messagesPanel;
    private JTextField messageField;

    public ConversationView(UserProfile recipient) {
        this.recipient = recipient;
        this.currentUserId = User.getCurrentUser().getUserId();
        this.chatDAO = new ChatDAO();
        this.chatId = chatDAO.getChatId(currentUserId, recipient.getUsername());
        initUI();
        loadMessages();
    }

    private void initUI() {
        setTitle(recipient.getFullName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createMessagesScrollPane(), BorderLayout.CENTER);
        add(createInputPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        header.setPreferredSize(new Dimension(getWidth(), 60));

        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        backButton.setBorder(BorderFactory.createEmptyBorder(0,15,0,0));
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            new ChatSystem().setVisible(true);
            dispose();
        });
        
        JLabel nameLabel = new JLabel(recipient.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(40, 40));
        loadRecipientImage(imageLabel, recipient.getUsername());
        
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userInfoPanel.setOpaque(false);
        userInfoPanel.add(imageLabel);
        userInfoPanel.add(nameLabel);

        header.add(backButton, BorderLayout.WEST);
        header.add(userInfoPanel, BorderLayout.CENTER);

        return header;
    }

    private JScrollPane createMessagesScrollPane() {
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(new Color(249, 249, 249));
        messagesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Scroll to bottom automatically
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);
        verticalScrollBar.addAdjustmentListener(e -> {
            if (!e.getValueIsAdjusting()) {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });

        return scrollPane;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(0, 149, 246));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        return inputPanel;
    }

    private void sendMessage() {
        String text = messageField.getText().trim();
        if (text.isEmpty()) {
            return;
        }

        Chat message = new Chat();
        message.setSenderId(currentUserId);
        message.setReceiverId(recipient.getUsername());
        message.setMessage(text);

        boolean success = chatDAO.sendMessage(chatId, message);
        
        if (success) {
            messageField.setText("");
            addMessageBubble(message);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to send message. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMessages() {
        new SwingWorker<List<Chat>, Void>() {
            @Override
            protected List<Chat> doInBackground() {
                return chatDAO.getMessages(chatId);
            }

            @Override
            protected void done() {
                try {
                    List<Chat> messages = get();
                    for (Chat message : messages) {
                        addMessageBubble(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    
    private void addMessageBubble(Chat message) {
        boolean isSentByMe = message.getSenderId().equals(currentUserId);
        JPanel bubblePanel = new MessageBubble(message.getMessage(), isSentByMe);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(bubblePanel, isSentByMe ? BorderLayout.EAST : BorderLayout.WEST);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, bubblePanel.getPreferredSize().height));
        
        messagesPanel.add(wrapper);
        messagesPanel.add(Box.createVerticalStrut(5));
        messagesPanel.revalidate();
        messagesPanel.repaint();
    }
    
    private void loadRecipientImage(JLabel imageLabel, String username) {
         new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                String firebaseUrl = FirebaseStorageManager.getProfileImageUrl(username);
                Image image;
                if (firebaseUrl != null && !firebaseUrl.isEmpty()) {
                    image = ImageIO.read(new URL(firebaseUrl));
                } else {
                    image = ImageIO.read(getClass().getResource("/ImagePicker/RajeshHamalPhoto.png"));
                }
                
                Image scaledImage = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                BufferedImage circularImage = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circularImage.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new Ellipse2D.Float(0, 0, 40, 40));
                g2.drawImage(scaledImage, 0, 0, null);
                g2.dispose();
                return new ImageIcon(circularImage);
            }

            @Override
            protected void done() {
                try {
                    imageLabel.setIcon(get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}

class MessageBubble extends JPanel {
    private String message;
    private boolean isSentByMe;

    MessageBubble(String message, boolean isSentByMe) {
        this.message = message;
        this.isSentByMe = isSentByMe;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isSentByMe) {
            g2.setColor(new Color(0, 149, 246));
        } else {
            g2.setColor(new Color(229, 229, 229));
        }
        
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        JTextArea temp = new JTextArea(message);
        temp.setLineWrap(true);
        temp.setWrapStyleWord(true);
        temp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        temp.setBorder(new EmptyBorder(10, 10, 10, 10));
        temp.setSize(new Dimension(200, Short.MAX_VALUE));
        return temp.getPreferredSize();
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        JTextArea text = new JTextArea(message);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setOpaque(false);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        text.setForeground(isSentByMe ? Color.WHITE : Color.BLACK);
        text.setBorder(new EmptyBorder(5,5,5,5));
        add(text, BorderLayout.CENTER);
    }
} 