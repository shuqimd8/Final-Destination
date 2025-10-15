package com.learneria.controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class StudentMainController {

    @FXML
    private Label usernameLabel;

    @FXML
    public void initialize() {
        // Display current user's name
        String currentUser = SceneManager.getCurrentUser();
        if (currentUser != null) {
            usernameLabel.setText(currentUser);
        }
    }

    @FXML
    private void openGrammar(MouseEvent event) {
        SceneManager.switchScene("/com/learneria/fxml/grammar_game.fxml", "Grammar Game");
    }

    @FXML
    private void openNature(MouseEvent event) {
        SceneManager.switchScene("/com/learneria/fxml/nature_game.fxml", "Nature Game");
    }

    @FXML
    private void openFood(MouseEvent event) {
        SceneManager.switchScene("/com/learneria/fxml/food_game.fxml", "Food Game");
    }

    @FXML
    private void openSettings(MouseEvent event) {
        SceneManager.switchScene("/com/learneria/fxml/settings.fxml", "Settings");
    }

    @FXML
    private void handleProfile() {
        SceneManager.switchScene("/com/learneria/fxml/profile.fxml", "Profile");
    }

    @FXML
    private void handleLogout() {
        SceneManager.setCurrentUser(null, null);
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login");
    }
}
