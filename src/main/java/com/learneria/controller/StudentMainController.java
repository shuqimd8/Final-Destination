package com.learneria.controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StudentMainController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        // Show logged-in username in welcome message
        String currentUser = SceneManager.getCurrentUser();
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser + "!");
        }
    }

    @FXML
    private void openGrammar() {
        SceneManager.switchSceneWithUser(
                "/com/learneria/fxml/grammar_game.fxml",
                "Grammar Game",
                SceneManager.getCurrentUser()
        );
    }

    @FXML
    private void openFood() {
        SceneManager.switchSceneWithUser(
                "/com/learneria/fxml/food_game.fxml",
                "Food Game",
                SceneManager.getCurrentUser()
        );
    }

    @FXML
    private void openNature() {
        SceneManager.switchSceneWithUser(
                "/com/learneria/fxml/nature_game.fxml",
                "Nature Game",
                SceneManager.getCurrentUser()
        );
    }

    @FXML
    private void openSettings() {
        SceneManager.switchSceneWithUser(
                "/com/learneria/fxml/settings.fxml",
                "Settings",
                SceneManager.getCurrentUser()
        );
    }

    @FXML
    private void handleProfile() {
        SceneManager.switchSceneWithUser(
                "/com/learneria/fxml/profile.fxml",
                "Profile",
                SceneManager.getCurrentUser()
        );
    }

    @FXML
    private void handleLogout() {
        SceneManager.setCurrentUser(null);
        SceneManager.switchScene(
                "/com/learneria/fxml/login.fxml",
                "Login"
        );
    }
}


