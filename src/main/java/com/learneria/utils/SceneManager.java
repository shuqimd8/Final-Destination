package com.learneria.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;
    private static String currentUser;
    private static String currentRole; // 👈 NEW

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void setCurrentUser(String username, String role) {
        currentUser = username;
        currentRole = role;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static String getCurrentRole() {
        return currentRole;
    }

    public static void switchScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

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

    // 👇 NEW helper method to go back correctly based on role
    public static void goBackToDashboard() {
        if ("teacher".equalsIgnoreCase(currentRole)) {
            switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Main Menu");
        } else {
            switchScene("/com/learneria/fxml/student_main.fxml", "Student Main Menu");
        }
    }
}


