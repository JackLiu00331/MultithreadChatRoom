import Database.DatabaseManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final int PORT = 8888;
    private static final int THREAD_POOL_SIZE = 10;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private static boolean isRunning = true;

    public ChatServer() {
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        DatabaseManager.initializeDatabase();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Chat server started on port " + PORT);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                threadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            if (isRunning) {
                System.out.println("Error starting server: " + e.getMessage());
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
            System.out.println("Server shut down.");
        } catch (IOException e) {
            System.out.println("Error closing server socket: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }
}
