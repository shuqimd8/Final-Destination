package Model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * SceneManager — central navigation and session handler for Papa Learneria.
 * Supports role-aware scene switching, controller passing, modals, and global styling.
 */
public class SceneManager {

    private static Stage primaryStage;
    private static String currentUser;
    private static String currentRole; // "teacher" or "student"

    /** Assign the main stage on startup (called once in Main.java) */
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    /** Save logged-in user and their role */
    public static void setCurrentUser(String username, String role) {
        currentUser = username;
        currentRole = role;
    }

    /** Get current username */
    public static String getCurrentUser() {
        return currentUser;
    }

    /** Get current role */
    public static String getCurrentRole() {
        return currentRole;
    }

    /** Switch scene normally by FXML path + window title */
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

            // ✅ Apply global stylesheet if available (safe)
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
     * Switch scene and give access to the new controller for setup (e.g., passing parameters)
     * Example usage:
     * SceneManager.switchSceneWithController("/path.fxml", "Title", controller -> {
     *     if (controller instanceof SomeController c) c.setData(...);
     * });
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

    /** Go back to dashboard depending on current role */
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
     * Optional — show a popup modal window (useful for alerts or quick forms)
     * @param fxmlPath FXML path to load
     * @param title Window title
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

    /** Check if primary stage is ready */
    public static boolean isStageReady() {
        return primaryStage != null;
    }

    public static void switchScene(String s) {
    }
}
