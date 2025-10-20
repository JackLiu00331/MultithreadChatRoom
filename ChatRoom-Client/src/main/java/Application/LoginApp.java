package Application;

import javafx.application.Application;
import javafx.stage.Stage;

public class LoginApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        SceneManager.setStage(stage);
        SceneManager.switchScene("login-view", "Chat Room Login");
    }
}
