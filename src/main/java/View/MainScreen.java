package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScreen extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //String stylesheet = HelloApplication.class.getResource("stylesheet.css").toExternalForm();
        FXMLLoader fxmlLoader = new FXMLLoader(MainScreen.class.getResource("MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");

        //scene. getStylesheets().add(stylesheet);
        stage.setScene(scene);
        stage.show();


    }
}
