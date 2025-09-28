package com.learneria;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        SceneManager.setStage(stage);
        // Must include the correct resource path:
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Papa’s Learneria - Sign In");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
