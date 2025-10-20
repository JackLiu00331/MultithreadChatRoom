package Controller;

import Application.SceneManager;
import Model.Message;
import Model.User;
import Service.NetworkService;
import Util.AlertWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
        Message response = networkService.sendMessage(message);
        if (response != null) {
            if (response.getType() == Message.MessageType.LOGIN_SUCCESS) {
                AlertWindow.showConfirm("Login Successful", "Welcome, " + username + "!");
                try {
                    SceneManager.switchScene("chat-view", "Chat Room - " + username);
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
}
