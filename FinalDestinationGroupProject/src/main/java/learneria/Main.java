package learneria;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        GrammarPage grammarPage = new GrammarPage();
        Scene scene = grammarPage.getScene();

        stage.setTitle("Papa’s Learnaria - Grammar Page");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
