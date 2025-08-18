package com.example.finaldestinationgroupproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

import static javafx.application.Application.launch;

public class GUI extends Application {
    //hierarchy
    //stage
    //scene
    //vbox - vertical box
    //hbox - horizontal
    //textbox - title
    //tilepane - grid view
    @Override
    public void start(Stage stage) throws IOException {
        VBox root = new VBox();
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setSpacing(15.0);
        root.setFillWidth(false);

        HBox title = new HBox();
        title.setAlignment(javafx.geometry.Pos.CENTER);
        title.setSpacing(15.0);

        TilePane tiles = new TilePane();
        tiles.setAlignment(javafx.geometry.Pos.CENTER);
        tiles.setPrefColumns(3);

        TextField titleText = new TextField();
        titleText.setText("TextField");
        Label label = new Label("Fun Maths!");

        title.getChildren().addAll(titleText);

        Button button1 = new Button("1");
        Button button2 = new Button("2");
        Button button3 = new Button("3");
        Button button4 = new Button("4");
        Button button5 = new Button("5");
        Button button6 = new Button("6");

        tiles.getChildren().addAll(button1, button2, button3, button4, button5, button6);

        root.getChildren().addAll(title, tiles);
        // Define the scene, add to the stage (window) and show the stage
        Scene scene = new Scene(root, 1920, 1080);

        stage.setScene(scene);
        stage.setTitle("JavaFX Example Scene");
        stage.show();

        String stylesheet = GUI.class.getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
    }

    public static void main(String[] args) {
        launch();
    }
}