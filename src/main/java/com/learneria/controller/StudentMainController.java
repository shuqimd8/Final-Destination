package com.learneria.controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StudentMainController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        // Show logged-in username
        String currentUser = SceneManager.getCurrentUser();
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser + "!");
        }
    }

    @FXML
    private void openGrammar() {
        SceneManager.switchScene("/com/learneria/fxml/grammar_game.fxml", "Grammar Game");
    }

    @FXML
    private void openFood() {
        SceneManager.switchScene("/com/learneria/fxml/food_game.fxml", "Food Game");
    }

    @FXML
    private void openNature() {
        SceneManager.switchScene("/com/learneria/fxml/nature_game.fxml", "Nature Game");
    }

    @FXML
    private void openSettings() {
        SceneManager.switchScene("/com/learneria/fxml/settings.fxml", "Settings");
    }

    @FXML
    private void handleProfile() {
        SceneManager.switchScene("/com/learneria/fxml/profile.fxml", "Profile");
    }

    @FXML
    private void handleLogout() {
        // Clear stored user and go back to login
        SceneManager.setCurrentUser(null, null);
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login");
    }
}



