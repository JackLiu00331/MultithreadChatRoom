package Controller;

import Service.NetworkService;
import Util.AlertWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatRoomController {

    @FXML
    public TextArea messageWindow;
    @FXML
    public TextField userInput;
    @FXML
    public Button sendBtn;
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

}
