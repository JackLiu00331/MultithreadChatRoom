package Controller;

import Application.SceneManager;
import Model.Message;
import Model.User;
import Service.NetworkService;
import Util.AlertWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class LoginController {
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField usernameField;
    @FXML
    public Button submitBtn;
    @FXML
    public Button registerBtn;

    private NetworkService networkService;

    @FXML
    public void initialize() {
        networkService = NetworkService.getInstance();
        if (!networkService.isConnected()) {
            if (!networkService.connect()) {
                AlertWindow.showError("Network Error", "Unable to connect to the server. Please try again later.");
            }
        }
    }


    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            AlertWindow.showError("Login Error", "Username and password cannot be empty.");
            return;
        }
        User loginUser = new User(username, "", password);
        loginUser.setLastLoginTime(LocalDateTime.now());
        Message message = new Message(Message.MessageType.LOGIN, loginUser);
        Message response = networkService.sendAndWait(message);
        if (response != null) {
            if (response.getType() == Message.MessageType.LOGIN_SUCCESS) {
                networkService.startListening();
                AlertWindow.showConfirm("Login Successful", "Welcome, " + username + "!");
                try {
                    SceneManager.switchScene("chat-view", "Chat Room - " + username);
                    Message readyMessage = new Message(Message.MessageType.CHAT_ROOM_READY, loginUser);
                    networkService.sendMessage(readyMessage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                AlertWindow.showError("Login Failed", response.getContent());
            }
        } else {
            AlertWindow.showError("Network Error", "No response from server. Please try again later.");
        }
    }

    public void handleRegister() {
        try {
            SceneManager.switchScene("register-view", "Chat Room Register");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //private void openChatRoom(String username) {
    //    try {
    //        FXMLLoader loader = new FXMLLoader(
    //                getClass().getResource("/com/chao/chatroom/Client/View/chat-view.fxml")
    //        );
    //        Scene chatScene = new Scene(loader.load());
    //
    //        ChatRoomController chatController = loader.getController();
    //
    //        Stage stage = (Stage) submitBtn.getScene().getWindow();
    //        stage.setScene(chatScene);
    //        stage.setTitle("Chat Room - " + username);
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //}
}
