package com.example.demo.learneria;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Hub extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage)throws IOException {
        this.primaryStage = stage;
        SceneManager.setStage(primaryStage);
        SceneManager.switchTo(new MainMenu()); // start at main menu
        primaryStage.setTitle("Learneria Game Hub");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
