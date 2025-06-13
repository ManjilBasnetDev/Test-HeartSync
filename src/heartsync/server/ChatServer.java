package heartsync.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final int PORT = 5000;
    private ServerSocket serverSocket;
    private Map<Integer, ClientHandler> connectedClients;
    private Map<Integer, Set<Integer>> userMatches;
    private ExecutorService clientThreadPool;

    public ChatServer() {
        connectedClients = new ConcurrentHashMap<>();
        userMatches = new ConcurrentHashMap<>();
        clientThreadPool = Executors.newCachedThreadPool();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Chat server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientThreadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public void addMatch(int user1Id, int user2Id) {
        userMatches.computeIfAbsent(user1Id, k -> new HashSet<>()).add(user2Id);
        userMatches.computeIfAbsent(user2Id, k -> new HashSet<>()).add(user1Id);
        
        // Notify both users about the match
        notifyMatch(user1Id, user2Id);
        notifyMatch(user2Id, user1Id);
    }

    private void notifyMatch(int userId, int matchedUserId) {
        ClientHandler handler = connectedClients.get(userId);
        if (handler != null) {
            handler.sendMessage("MATCH:" + matchedUserId);
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private int userId;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // First message should be the user ID
                String initialMessage = in.readLine();
                if (initialMessage != null && initialMessage.startsWith("USER:")) {
                    userId = Integer.parseInt(initialMessage.substring(5));
                    connectedClients.put(userId, this);
                    System.out.println("User " + userId + " registered");
                }

                String message;
                while ((message = in.readLine()) != null) {
                    handleMessage(message);
                }
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                cleanup();
            }
        }

        private void handleMessage(String message) {
            if (message.startsWith("MSG:")) {
                // Format: MSG:recipientId:messageText
                String[] parts = message.substring(4).split(":", 2);
                if (parts.length == 2) {
                    int recipientId = Integer.parseInt(parts[0]);
                    String messageText = parts[1];
                    sendMessageToUser(recipientId, "MSG:" + userId + ":" + messageText);
                }
            }
        }

        public void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }

        private void sendMessageToUser(int recipientId, String message) {
            ClientHandler recipient = connectedClients.get(recipientId);
            if (recipient != null) {
                recipient.sendMessage(message);
            }
        }

        private void cleanup() {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
                connectedClients.remove(userId);
                System.out.println("User " + userId + " disconnected");
            } catch (IOException e) {
                System.err.println("Error during cleanup: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start();
    }
} 