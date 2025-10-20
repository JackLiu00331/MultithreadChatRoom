
import Database.DatabaseManager;
import Model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private ChatServer server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private static boolean isRunning = true;

    public ClientHandler(Socket clientSocket, ChatServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            while (isRunning) {
                Message message = (Message) inputStream.readObject();
                if (message != null) {
                    handleClientMessage(message);
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error initializing client handler: " + e.getMessage());
        } finally {
            cleanUp();
        }
    }

    private void handleClientMessage(Message message) {
        switch (message.getType()){
            case REGISTER -> handleRegister(message);
            default -> System.out.println("Unknown message type received: " + message.getType());
        }
    }

    private void handleRegister(Message message) {
        boolean success = DatabaseManager.registerUser(message.getSender());
        Message response;
        if (success) {
            response = new Message(Message.MessageType.REGISTER_SUCCESS, "Registration successful.");
        } else {
            response = new Message(Message.MessageType.REGISTER_FAILURE, "Registration failed. Username may already exist.");
        }
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Error sending registration response: " + e.getMessage());
        }
    }

    private void cleanUp() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            System.out.println("Error closing client connection: " + e.getMessage());
        }
    }
}
