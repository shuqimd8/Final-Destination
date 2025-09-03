package learneria;

import javafx.application.Application;
import javafx.stage.Stage;

public class Hub extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
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
