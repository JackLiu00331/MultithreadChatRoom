import Database.DatabaseManager;
import Model.Message;
import Model.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final int PORT = 8888;
    private static final int THREAD_POOL_SIZE = 10;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private static boolean isRunning = true;
    private static Map<String, ClientHandler> onlineClients = new ConcurrentHashMap<>();

    public ChatServer() {
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        DatabaseManager.initializeDatabase();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server - Chat server started on port " + PORT);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server - New client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                threadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            if (isRunning) {
                System.out.println("Server - Error starting server: " + e.getMessage());
            }
        } finally {
            shutdownAll();
        }
    }

    private void shutdownAll() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            threadPool.shutdown();
            System.out.println("Server - Server shut down.");
        } catch (IOException e) {
            System.out.println("Server - Error closing server socket: " + e.getMessage());
        }
    }


    public void addClient(String username, ClientHandler clientHandler) {
        onlineClients.put(username, clientHandler);
    }

    public void removeClient(String username) {
        onlineClients.remove(username);
    }

    public void broadcastMessage(Message message, String excludeUser) {
        for (Map.Entry<String, ClientHandler> entry : onlineClients.entrySet()) {
            if (!entry.getKey().equals(excludeUser)) {
                entry.getValue().sendMessage(message);
            }
        }
    }

    public void broadcastToAll(Message message) {
        for (ClientHandler clientHandler : onlineClients.values()) {
            clientHandler.sendMessage(message);
        }
    }

    public void notifyUserJoin(String username){
        Message joinMessage = new Message(Message.MessageType.USER_JOIN, username + " has joined the chat.");
        System.out.println(joinMessage.getContent());
        broadcastToAll(joinMessage);
        broadcastUserList();
    }

    public void notifyUserLeave(String username){
        Message leaveMessage = new Message(Message.MessageType.USER_LEAVE, username + " has left the chat.");
        broadcastMessage(leaveMessage, username);
        broadcastUserList();
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }

    public Set<String> getOnlineUsers() {
        return new HashSet<>(onlineClients.keySet());
    }

    public void broadcastUserList() {
        Message userListMessage = new Message(Message.MessageType.USER_LIST);
        userListMessage.setExtraData(getOnlineUsers());
        broadcastToAll(userListMessage);
    }
}
