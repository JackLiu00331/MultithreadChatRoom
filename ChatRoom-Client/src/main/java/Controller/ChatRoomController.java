package Controller;

import Model.Message;
import Service.NetworkService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class ChatRoomController {

    @FXML
    public TextArea messageWindow;
    @FXML
    public TextField userInput;
    @FXML
    public Button sendBtn;
    @FXML
    public ListView<String> userListView;
    @FXML
    public Label userCountLabel;

    private NetworkService networkService;
    private ObservableList<String> userList;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    public void initialize() {
        networkService = NetworkService.getInstance();
        userList = FXCollections.observableArrayList();
        userListView.setItems(userList);
        networkService.setMessageListener(this::handleIncomingMessage);
    }

    private void handleIncomingMessage(Message message) {
        Platform.runLater(() -> {
            switch (message.getType()){
                case USER_LIST -> updateUserList(message);
                case USER_JOIN, USER_LEAVE -> displaySystemMessage(message);
                case CHAT -> displayChatMessage(message);
                default -> System.out.println("Unhandled message type: " + message.getType());
            }
        });

    }


    private void displaySystemMessage(Message message) {
        String systemMessage = message.getContent();
        String timestamp = LocalDateTime.now().format(timeFormatter);
        displayMessage("["+timestamp+"] [System]: " + systemMessage);
    }

    private void displayMessage(String message) {
        messageWindow.appendText(message + "\n");
        messageWindow.setScrollTop(Double.MAX_VALUE);
    }

    private void updateUserList(Message message) {
        Set<String> userList = (Set<String>) message.getExtraData();
        this.userList.setAll(userList);
        userCountLabel.setText("Online: " + userList.size());
    }

    @FXML
    public void handleChat() {
        String content = userInput.getText().trim();
        if (content.isEmpty()) {
            return;
        }
        Message chatMessage = new Message(Message.MessageType.CHAT, content);
        networkService.sendMessage(chatMessage);
        String timestamp = LocalDateTime.now().format(timeFormatter);
        displayMessage("["+timestamp+"] [Me]: " + content);
        userInput.clear();
    }

    private void displayChatMessage(Message message) {
        String timestamp = LocalDateTime.now().format(timeFormatter);
        String formattedMessage = "[" + timestamp + "] [" + message.getSender().getUsername() +"]: "+ message.getContent();
        displayMessage(formattedMessage);
    }
}
