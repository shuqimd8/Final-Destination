package learneria;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu implements SceneProvider {

    private final Scene scene;
    private final Stage stage;

    public MainMenu(Stage stage) {
        this.stage = stage;

        BorderPane root = new BorderPane();

        // === Menu Buttons ===
        VBox menuBox = new VBox(15);
        menuBox.setAlignment(Pos.CENTER);

        Button grammarBtn = new Button("Grammar Page");
        Button foodBtn = new Button("Food Page");
        Button natureBtn = new Button("Nature Page");
        Button settingsBtn = new Button("Settings");
        Button exitBtn = new Button("Exit");

        // === Actions ===
        grammarBtn.setOnAction(e -> {
            GrammarPage grammarPage = new GrammarPage(stage);
            stage.setScene(grammarPage.getScene());
        });

        foodBtn.setOnAction(e -> {
            FoodPage foodPage = new FoodPage(stage);
            stage.setScene(foodPage.getScene());
        });

        natureBtn.setOnAction(e -> {
            NaturePage naturePage = new NaturePage(stage);
            stage.setScene(naturePage.getScene());
        });

        settingsBtn.setOnAction(e -> {
            SettingsPage settingsPage = new SettingsPage(stage);
            stage.setScene(settingsPage.getScene());
        });

        exitBtn.setOnAction(e -> {
            stage.close(); // exits the app
        });

        // === Add buttons ===
        menuBox.getChildren().addAll(grammarBtn, foodBtn, natureBtn, settingsBtn, exitBtn);

        root.setCenter(menuBox);

        scene = new Scene(root, 800, 600);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}

