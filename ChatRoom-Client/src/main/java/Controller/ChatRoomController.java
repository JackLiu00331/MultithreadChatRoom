package Controller;

import Service.NetworkService;
import Util.AlertWindow;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ChatRoomController {

    @FXML
    public TextArea messageWindow;
    @FXML
    public TextField userInput;
    @FXML
    public Button sendBtn;
    public ListView userListView;
    public Label userCountLabel;
    public Label usernameLabel;
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
