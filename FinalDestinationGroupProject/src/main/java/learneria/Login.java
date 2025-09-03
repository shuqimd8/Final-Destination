package learneria;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Login implements  SceneProvider {

    @Override
    public Scene getScene()
    {
        Label title = new Label("Login / Register Page");
        Button backBtn = new Button("Back to Menu");

        backBtn.setOnAction(e -> SceneManager.switchTo(new MainMenu()));

        VBox layout = new VBox(10, title, backBtn);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        return new Scene(layout, 400, 300);
    }
}
