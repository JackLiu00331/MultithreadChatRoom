package Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class SceneManager {
    private static Stage stage;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchScene(String filename, String title) throws IOException {
        var url = SceneManager.class.getResource("/View/" + filename + ".fxml");
        System.out.println("FXML URL: " + url);

        if (url == null) {
            System.err.println("FXML file not found!");
            return;
        }
        FXMLLoader loader = new FXMLLoader(url);
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
