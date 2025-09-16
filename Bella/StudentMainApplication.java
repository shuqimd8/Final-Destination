package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentMainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String stylesheet = StudentMainApplication.class.getResource("StudentMainMenu.css").toExternalForm();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("StudentMainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");

        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);
        stage.show();
    }
}
