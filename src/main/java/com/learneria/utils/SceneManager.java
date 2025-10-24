package com.learneria.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * The {@code SceneManager} class provides a centralised way to manage
 * scene navigation, controller communication, and user session tracking
 * across the Papa Learneria JavaFX application.
 * <p>
 * It supports:
 * <ul>
 *   <li>Scene switching with or without controller access</li>
 *   <li>Global stylesheet application</li>
 *   <li>Role-based navigation (teacher/student)</li>
 *   <li>Popup modal creation</li>
 * </ul>
 */
public class SceneManager {

    /** Reference to the application's main primary stage. */
    private static Stage primaryStage;
    /** Username of the currently logged-in user. */
    private static String currentUser;
    /** Role of the current user — typically "teacher" or "student". */
    private static String currentRole; // "teacher" or "student"

    /**
     * Assigns the primary stage of the application.
     * This should be called once in {@code Main.java} during startup.
     *
     * @param stage the JavaFX {@link Stage} used as the main application window
     */
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Saves the current logged-in user's username and role for session use.
     *
     * @param username the username of the current user
     * @param role     the role of the user (e.g., "teacher" or "student")
     */
    public static void setCurrentUser(String username, String role) {
        currentUser = username;
        currentRole = role;
    }

    /**
     * Returns the username of the currently logged-in user.
     *
     * @return current username
     */
    public static String getCurrentUser() {
        return currentUser;
    }


    /**
     * Returns the role of the current user.
     *
     * @return the user’s role string
     */
    public static String getCurrentRole() {
        return currentRole;
    }

    /**
     * Switches the current scene to a new FXML layout.
     * Automatically applies the global stylesheet and injects the current
     * username into controllers implementing {@link com.learneria.utils.UserAware}.
     *
     * @param fxmlPath path to the FXML file (relative to the resource root)
     * @param title    title of the window
     */
    public static void switchScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Inject username if controller implements UserAware
            Object controller = loader.getController();
            if (controller instanceof com.learneria.utils.UserAware) {
                ((com.learneria.utils.UserAware) controller).setUsername(currentUser);
            }

            // Create the scene
            Scene scene = new Scene(root);

            //  Apply global stylesheet if available (safe)
            try {
                String css = SceneManager.class
                        .getResource("/com/learneria/styles/styles.css")
                        .toExternalForm();
                scene.getStylesheets().add(css);
            } catch (Exception ignored) {
                System.out.println("⚠️ Stylesheet not found, continuing without it.");
            }

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("❌ Failed to switch scene: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Switches to a new scene and provides the loaded controller to a custom
     * consumer for setup. Useful when data or configuration must be passed
     * to the new scene’s controller before display.
     * <p>
     * Example usage:
     * <pre>
     * SceneManager.switchSceneWithController("/path.fxml", "Title", controller -> {
     *     if (controller instanceof SomeController c) c.setData(...);
     * });
     * </pre>
     *
     * @param fxmlPath            FXML path for the new scene
     * @param title               title of the window
     * @param controllerConsumer  lambda function to operate on the new controller
     */
    public static void switchSceneWithController(String fxmlPath, String title, Consumer<Object> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controllerConsumer != null) controllerConsumer.accept(controller);

            Scene scene = new Scene(root);

            // ✅ Apply stylesheet again (for consistency)
            try {
                String css = SceneManager.class
                        .getResource("/com/learneria/styles/styles.css")
                        .toExternalForm();
                scene.getStylesheets().add(css);
            } catch (Exception ignored) {}

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("❌ Failed to load controller for: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Redirects the user back to their main dashboard depending on role.
     * <ul>
     *     <li>Teacher → Teacher Main Menu</li>
     *     <li>Student → Student Main Menu</li>
     *     <li>Unknown → Login Screen</li>
     * </ul>
     */
    public static void goBackToDashboard() {
        if ("teacher".equalsIgnoreCase(currentRole)) {
            switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Main Menu");
        } else if ("student".equalsIgnoreCase(currentRole)) {
            switchScene("/com/learneria/fxml/student_main.fxml", "Student Main Menu");
        } else {
            switchScene("/com/learneria/fxml/login.fxml", "Login");
        }
    }

    /**
     * Displays a modal popup window for quick alerts, confirmation prompts,
     * or short forms. The popup is modal and blocks interaction with the
     * main window until closed.
     *
     * @param fxmlPath path to the FXML file defining the popup
     * @param title    title of the popup window
     */
    public static void showPopupScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(title);

            Scene popupScene = new Scene(root);

            // ✅ Add stylesheet to popups too
            try {
                String css = SceneManager.class
                        .getResource("/com/learneria/styles/styles.css")
                        .toExternalForm();
                popupScene.getStylesheets().add(css);
            } catch (Exception ignored) {}

            popupStage.setScene(popupScene);
            popupStage.showAndWait();

        } catch (IOException e) {
            System.err.println("❌ Failed to open popup: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Checks whether the primary stage has been set and is ready for use.
     *
     * @return {@code true} if the stage is available, otherwise {@code false}
     */
    public static boolean isStageReady() {
        return primaryStage != null;
    }


    /**
     * Overloaded placeholder for scene switching.
     * <p>
     * This method is intentionally left blank for potential
     * future compatibility or simplified calls.
     * </p>
     *
     * @param s path to the FXML (currently unused)
     */
    public static void switchScene(String s) {

    }
}
