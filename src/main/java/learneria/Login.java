package learneria;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login implements SceneProvider {

    private final Scene scene;

    public Login(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Label label = new Label("Login Page (Placeholder)");
        Button backButton = new Button("⬅ Back to Menu");
        backButton.setOnAction(e -> stage.setScene(new MainMenu(stage).getScene()));

        root.getChildren().addAll(label, backButton);

        scene = new Scene(root, 1024, 768);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
