package com.learneria.controller;

import com.learneria.utils.SceneManager;
import com.learneria.utils.UserAware;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StudentMainController implements UserAware {

    @FXML
    private Label welcomeLabel;

    private String username;

    @Override
    public void setUsername(String username) {
        this.username = username;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + username + " (Student)!");
        }
    }

    @FXML
    private void openGrammar() {
        SceneManager.switchScene(
                "/com/learneria/fxml/grammar_game.fxml",
                "Grammar Game"
        );
    }

    @FXML
    private void openFood() {
        SceneManager.switchScene(
                "/com/learneria/fxml/food_game.fxml",
                "Food Game"
        );
    }

    @FXML
    private void openNature() {
        SceneManager.switchScene(
                "/com/learneria/fxml/nature_game.fxml",
                "Nature Game"
        );
    }

    @FXML
    private void openSettings() {
        SceneManager.switchSceneWithUser(
                "/com/learneria/fxml/settings.fxml",  // <-- add leading slash and package path
                "Settings",
                username
        );
    }



    @FXML
    private void handleLogout() {
        SceneManager.switchScene(
                "/com/learneria/fxml/login.fxml",
                "Sign In"
        );
    }
}

