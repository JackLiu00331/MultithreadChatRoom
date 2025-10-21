package Service;

import Model.Message;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class NetworkService {
    private static NetworkService instance;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8888;
    private Consumer<Message> messageListener;
    private Thread listeningThread;

    private NetworkService() {
    }

    public static NetworkService getInstance() {
        if (instance == null) {
            instance = new NetworkService();
        }
        return instance;
    }

    public boolean connect() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            return false;
        }
    }

    public void startListening() {
        listeningThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Message message = (Message) inputStream.readObject();
                    if (messageListener != null) {
                        Platform.runLater(() -> messageListener.accept(message));
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error receiving message: " + e.getMessage());
            }
        });
        listeningThread.setDaemon(true);
        listeningThread.start();
    }

    public void sendMessage(Message message) {
        try {
            synchronized (outputStream) {
                outputStream.writeObject(message);
                outputStream.flush();
            }
        } catch (IOException e) {
            System.err.println("Error sending/receiving message: " + e.getMessage());
        }
    }

    public Message sendAndWait(Message message) {
        try {
            synchronized (outputStream) {
                outputStream.writeObject(message);
                outputStream.flush();
            }
            return (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error sending/receiving message: " + e.getMessage());
            return null;
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public void disconnect() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            System.err.println("Error disconnecting from server: " + e.getMessage());
        }
    }

    public void setMessageListener(Consumer<Message> messageListener) {
        this.messageListener = messageListener;
    }

}

