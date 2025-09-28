package com.learneria.controller;

import com.learneria.utils.SceneManager;
import com.learneria.utils.UserAware;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TeacherMainController implements UserAware {

    @FXML
    private Label welcomeLabel;

    private String username;

    @Override
    public void setUsername(String username) {
        this.username = username;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + username + " (Teacher)!");
        }
    }

    @FXML
    private void handleLogout() {
        // ✅ Go back to login screen
        SceneManager.switchScene(
                "/com/learneria/fxml/login.fxml",
                "Sign In"
        );
    }
}

