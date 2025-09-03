package learneria;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MainMenu implements SceneProvider {

    @Override
    public Scene getScene() {
        Label title = new Label("Welcome to Learneria!");
        Button loginBtn = new Button("Login / Register");
        Button grmrBtn = new Button("Grammar");
        Button foodBtn = new Button("Food");
        Button natureBtn = new Button("Nature & Animals");
        Button exitBtn = new Button("Exit");

        loginBtn.setOnAction(e -> SceneManager.switchTo(new Login()));
        grmrBtn.setOnAction(e -> SceneManager.switchTo(new GrammarPage()));
        foodBtn.setOnAction(e -> SceneManager.switchTo(new FoodPage()));
        natureBtn.setOnAction(e -> SceneManager.switchTo(new NaturePage()));
        exitBtn.setOnAction(e -> System.exit(0));

        VBox layout = new VBox(10, title, loginBtn, grmrBtn, foodBtn, natureBtn, exitBtn);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        return new Scene(layout, 400, 400);
    }
}