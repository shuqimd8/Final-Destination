package com.example.demo.learneria;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GrammarPage implements SceneProvider {

    @Override
    public Scene getScene() {
        // title
        Label title = new Label("Papa’s Learnaria");
        title.getStyleClass().add("highlight");

        HBox topBar = new HBox(title);
        topBar.setAlignment(Pos.CENTER);
        topBar.setSpacing(20);

        // Middle (timer)
        Label timer = new Label("2:39s");
        timer.setStyle("-fx-font-size: 32px; -fx-text-fill: black; -fx-font-weight: bold;");

        /// ERROR WITH FOLLOWING LINE OF CODE
        //return new Scene(layout, 1600, 900);

        Label word = new Label("Jump");
        word.setStyle("-fx-font-size: 40px; -fx-text-fill: black; -fx-font-family: 'Jost';");

        VBox centerBox = new VBox(timer, word);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(20);

        // left High Score
        VBox leftBox = new VBox(
                new Label("HIGH SCORE:"),
                new Label("230")
        );
        leftBox.setAlignment(Pos.TOP_CENTER);
        leftBox.setSpacing(10);

        // Right Score
        VBox rightBox = new VBox(
                new Label("Score:"),
                new Label("50")
        );
        rightBox.setAlignment(Pos.TOP_CENTER);
        rightBox.setSpacing(10);

        // bucket button
        Button nounBtn = new Button("Noun");
        Button verbBtn = new Button("Verb");
        Button adjBtn = new Button("Adjective");

        HBox bottomBar = new HBox(nounBtn, verbBtn, adjBtn);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setSpacing(50);

        // Main
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(centerBox);
        root.setLeft(leftBox);
        root.setRight(rightBox);
        root.setBottom(bottomBar);

        //Scene
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        return scene;
    }
}
