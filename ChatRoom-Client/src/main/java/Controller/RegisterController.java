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

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordField1;
    @FXML
    private Button submitBtn;
    @FXML
    private TextField nameField;

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


    public void handleRegister() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = passwordField1.getText();
        String name = nameField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
            AlertWindow.showWarning("Registration Warning", "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            AlertWindow.showError("Registration Error", "Passwords do not match!");
            return;
        }

        User newUser = new User(username, name, password);
        Message message = new Message(Message.MessageType.REGISTER, newUser);
        Message response = networkService.sendMessage(message);
        if (response != null) {
            if(response.getType() == Message.MessageType.REGISTER_SUCCESS) {
                AlertWindow.showConfirm("Registration Successful", response.getContent());
                try {
                    SceneManager.switchScene("login-view", "Chat Room Login");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                AlertWindow.showError("Registration Failed", response.getContent());
            }
        } else {
            AlertWindow.showError("Network Error", "No response from server. Please try again later.");
        }
    }
}
