package com.learneria.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;
    private static String currentUser; // Track logged-in username

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void switchScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            // If controller is UserAware → inject username
            Object controller = loader.getController();
            if (controller instanceof com.learneria.utils.UserAware) {
                ((com.learneria.utils.UserAware) controller).setUsername(currentUser);
            }

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchSceneWithUser(String fxmlPath, String title, String username) {
        setCurrentUser(username); // Save logged-in user
        switchScene(fxmlPath, title);
    }
}

