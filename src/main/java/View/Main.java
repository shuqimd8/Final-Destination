package View;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 启动时加载 MainMenu
        MainMenu mainMenu = new MainMenu(primaryStage);
        primaryStage.setScene(mainMenu.getScene());
        primaryStage.setTitle("Papa's Learneria");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
