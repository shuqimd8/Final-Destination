package View;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
    }

    public static void switchTo(SceneProvider page) {
        Scene scene = page.getScene();
        stage.setScene(scene);
    }
}

