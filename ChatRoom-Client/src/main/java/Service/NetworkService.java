package Service;

import Model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkService {
    private static NetworkService instance;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8888;

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
            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
            return true;
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
            return false;
        }
    }

    public Message sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
            return (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error sending/receiving message: " + e.getMessage());
            return null;
        }
    }

    public void sendMessageOnly(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Error sending message: " + e.getMessage());
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
            System.out.println("Error disconnecting from server: " + e.getMessage());
        }
    }
}
