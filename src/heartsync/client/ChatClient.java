package heartsync.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int userId;
    private MessageListener messageListener;
    private ExecutorService messageThread;
    private volatile boolean running;

    public interface MessageListener {
        void onMessageReceived(int senderId, String message);
        void onMatchReceived(int matchedUserId);
        void onConnectionError(String error);
    }

    public ChatClient(int userId, MessageListener listener) {
        this.userId = userId;
        this.messageListener = listener;
        this.messageThread = Executors.newSingleThreadExecutor();
    }

    public void connect() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send user ID to server
            out.println("USER:" + userId);

            // Start message listener thread
            running = true;
            messageThread.execute(this::listenForMessages);
        } catch (IOException e) {
            messageListener.onConnectionError("Failed to connect to server: " + e.getMessage());
        }
    }

    private void listenForMessages() {
        try {
            String message;
            while (running && (message = in.readLine()) != null) {
                if (message.startsWith("MSG:")) {
                    // Format: MSG:senderId:messageText
                    String[] parts = message.substring(4).split(":", 2);
                    if (parts.length == 2) {
                        int senderId = Integer.parseInt(parts[0]);
                        String messageText = parts[1];
                        messageListener.onMessageReceived(senderId, messageText);
                    }
                } else if (message.startsWith("MATCH:")) {
                    // Format: MATCH:matchedUserId
                    int matchedUserId = Integer.parseInt(message.substring(6));
                    messageListener.onMatchReceived(matchedUserId);
                }
            }
        } catch (IOException e) {
            if (running) {
                messageListener.onConnectionError("Connection lost: " + e.getMessage());
            }
        }
    }

    public void sendMessage(int recipientId, String message) {
        if (out != null) {
            out.println("MSG:" + recipientId + ":" + message);
        }
    }

    public void disconnect() {
        running = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error during disconnect: " + e.getMessage());
        }
        messageThread.shutdown();
    }
} 