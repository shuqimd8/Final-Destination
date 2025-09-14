package learneria;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu implements SceneProvider {

    private final Scene scene;

    public MainMenu(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        // Title
        Button grammarBtn = new Button("Grammar Page");
        Button foodBtn = new Button("Food Page");
        Button natureBtn = new Button("Nature Page");
        Button hubBtn = new Button("Hub");
        Button loginBtn = new Button("Login");

        // Navigation
        grammarBtn.setOnAction(e -> stage.setScene(new GrammarPage(stage).getScene()));
        foodBtn.setOnAction(e -> stage.setScene(new FoodPage(stage).getScene()));
        natureBtn.setOnAction(e -> stage.setScene(new NaturePage(stage).getScene()));
        hubBtn.setOnAction(e -> stage.setScene(new Hub(stage).getScene()));
        loginBtn.setOnAction(e -> stage.setScene(new Login(stage).getScene()));

        root.getChildren().addAll(grammarBtn, foodBtn, natureBtn, hubBtn, loginBtn);

        scene = new Scene(root, 1024, 768);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
