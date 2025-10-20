import Application.LoginApp;
import Service.NetworkService;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        new Thread(() -> NetworkService.getInstance().connect()).start();
        Application.launch(LoginApp.class, args);
    }
}
