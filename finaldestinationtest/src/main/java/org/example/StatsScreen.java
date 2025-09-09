package org.example;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class StatsScreen extends Application {

    @Override
    public void start(Stage stage) {
        // Main root layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // ===== Header Bar =====
        HBox header = new HBox();
        header.setPadding(new Insets(10));
        header.setSpacing(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #FFD84D; -fx-border-color: black; -fx-border-width: 0 0 2 0;");

        Button backButton = new Button("←");
        backButton.setStyle("-fx-font-size: 18px; -fx-background-radius: 50%; -fx-background-color: #4DA6FF; -fx-text-fill: white;");

        Label title = new Label("Stats");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(backButton, spacer, title, spacer);

        // ===== Stats Content =====
        GridPane statsGrid = new GridPane();
        statsGrid.setPadding(new Insets(30));
        statsGrid.setHgap(40);
        statsGrid.setVgap(20);

        String[][] stats = {
                {"Total Correct Answers", "300"},
                {"Average Correct Answers", "15"},
                {"Total Incorrect Answers", "152"},
                {"Average Incorrect Answers", "5"},
                {"Games Played", "20"},
                {"Total Words", "452"},
                {"Average Answer Speed", "2 words per second"}
        };

        for (int i = 0; i < stats.length; i++) {
            Label statName = new Label(stats[i][0]);
            statName.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            Label statValue = new Label(stats[i][1]);
            statValue.setFont(Font.font("Arial", 16));

            statsGrid.add(statName, 0, i);
            statsGrid.add(statValue, 1, i);
        }

        VBox contentBox = new VBox(statsGrid);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: white; -fx-border-radius: 15; -fx-background-radius: 15;");

        // Put everything together
        VBox mainLayout = new VBox(10, header, contentBox);
        mainLayout.setAlignment(Pos.TOP_CENTER);

        root.setTop(header);
        root.setCenter(contentBox);

        // Scene
        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("Papa's Learnaria - Stats");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}