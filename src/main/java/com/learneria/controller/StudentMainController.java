package com.learneria.controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Controller for the Student Main Menu screen.
 * <p>
 * Provides navigation for students to access the Grammar, Nature, and Food games,
 * as well as profile and settings pages. Displays the logged-in user’s name
 * dynamically retrieved from the {@link SceneManager}.
 * </p>
 */
public class StudentMainController {

    /** Label displaying the currently logged-in student's username. */
    @FXML
    private Label usernameLabel;

    /**
     * Initializes the main menu view.
     * <p>
     * Displays the username of the currently logged-in user at the top of the screen.
     * </p>
     */
    @FXML
    public void initialize() {
        // Display current user's name
        String currentUser = SceneManager.getCurrentUser();
        if (currentUser != null) {
            usernameLabel.setText(currentUser);
        }
    }

    /**
     * Opens the Grammar Game screen when selected.
     *
     * @param event mouse click event
     */
    @FXML
    private void openGrammar(MouseEvent event) {
        SceneManager.switchScene("/com/learneria/fxml/grammar_game.fxml", "Grammar Game");
    }

    /**
     * Opens the Nature Game screen when selected.
     *
     * @param event mouse click event
     */
    @FXML
    private void openNature(MouseEvent event) {
        SceneManager.switchScene("/com/learneria/fxml/nature_game.fxml", "Nature Game");
    }

    /**
     * Opens the Food Game screen when selected.
     *
     * @param event mouse click event
     */
    @FXML
    private void openFood(MouseEvent event) {
        SceneManager.switchScene("/com/learneria/fxml/food_game.fxml", "Food Game");
    }

    /**
     * Opens the Settings screen for the student.
     *
     * @param event mouse click event
     */
    @FXML
    private void openSettings(MouseEvent event) {
        SceneManager.switchScene("/com/learneria/fxml/settings.fxml", "Settings");
    }

    /**
     * Opens the student's profile screen where progress and scores are displayed.
     */
    @FXML
    private void handleProfile() {
        SceneManager.switchScene("/com/learneria/fxml/profile.fxml", "Profile");
    }

    /**
     * Logs the user out and returns to the login screen.
     * <p>
     * Clears the current user session stored in {@link SceneManager}.
     * </p>
     */
    @FXML
    private void handleLogout() {
        SceneManager.setCurrentUser(null, null);
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login");
    }
}
