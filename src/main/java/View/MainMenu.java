
package View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainMenu implements SceneProvider {

    private final Scene scene;

    public MainMenu(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("Papa’s Learnaria");
        title.setFont(new Font("Comic Sans MS", 36));
        title.setTextFill(Color.DARKBLUE);
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

       /* ImageView papaImg = new ImageView(new Image("/Images/papa.png"));
        papaImg.setFitHeight(300);
        papaImg.setPreserveRatio(true);
        root.setLeft(papaImg); */

        HBox categoryBox = new HBox(30);
        categoryBox.setAlignment(Pos.CENTER);

        Button grammarBtn = createCircleButton("Grammar", "#FFD966"); // Yellow
        Button natureBtn = createCircleButton("Nature", "#93C47D");  // Green
        Button foodBtn = createCircleButton("Food", "#B4A7D6");      // Purple


        categoryBox.getChildren().addAll(grammarBtn, natureBtn, foodBtn);
        root.setCenter(categoryBox);

        VBox userBox = new VBox(10);
        userBox.setAlignment(Pos.TOP_RIGHT);

        Label name = new Label("{USERNAME}");
        name.setFont(new Font(18));

        Label role = new Label("Busy Bee");
        role.setFont(new Font(14));

       /* ImageView beeIcon = new ImageView(new Image("bee.png"));
        beeIcon.setFitHeight(40);
        beeIcon.setPreserveRatio(true); */

        Button signOutBtn = new Button("Sign Out");
        signOutBtn.setStyle("-fx-background-color: #66CCFF; -fx-font-size: 14px;");

        VBox.setMargin(signOutBtn, new Insets(10, 0, 0, 0));

        userBox.getChildren().addAll(name, role, signOutBtn);
        root.setRight(userBox);

        Button statsBtn = new Button("STATS");
        statsBtn.setStyle("-fx-background-color: #66CCFF; -fx-font-size: 16px;");
        BorderPane.setAlignment(statsBtn, Pos.BOTTOM_RIGHT);
        root.setBottom(statsBtn);

        scene = new Scene(root, 900, 650);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        // Inside MainMenu constructor, after button creation

        grammarBtn.setOnAction(e -> {
            SceneProvider grammarPage = new GrammarPage(stage);
            stage.setScene(grammarPage.getScene());
        });

        natureBtn.setOnAction(e -> {
            SceneProvider naturePage = new NaturePage(stage);
            stage.setScene(naturePage.getScene());
        });

        foodBtn.setOnAction(e -> {
            SceneProvider foodPage = new FoodPage(stage);
            stage.setScene(foodPage.getScene());
        });
        statsBtn.setOnAction(e -> {
            SceneProvider settingsPage = new SettingsPage(stage);
            stage.setScene(settingsPage.getScene());
        });


    }

    @Override
    public Scene getScene() {
        return scene;
    }

    // Helper to make colored circle buttons
    private Button createCircleButton(String text, String color) {
        Button btn = new Button(text);
        btn.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
        btn.setStyle(
                "-fx-background-radius: 100; " +
                        "-fx-min-width: 150px; -fx-min-height: 150px; " +
                        "-fx-max-width: 150px; -fx-max-height: 150px; " +
                        "-fx-background-color: " + color + "; " +
                        "-fx-font-size: 16px;"
        );
        return btn;
    }
}