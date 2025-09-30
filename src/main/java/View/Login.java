package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class Login implements SceneProvider {
    private final Scene scene;

    public Login(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        String css = this.getClass().getResource("stylesheet.css").toExternalForm();
        stage.setTitle("Login");
        this.scene = new Scene(root);
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
