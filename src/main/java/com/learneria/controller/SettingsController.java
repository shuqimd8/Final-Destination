package com.learneria.controller;

import com.learneria.utils.SceneManager;
import com.learneria.utils.UserAware;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class SettingsController implements UserAware {

    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;

    private String username;

    @Override
    public void setUsername(String username) {
        this.username = username;
        usernameLabel.setText(username);
        nameLabel.setText("Jake"); // default for now
        passwordLabel.setText("********");
    }

    @FXML
    private void handleBack() {
        // Return to Student Main for now (later detect Teacher vs Student)
        SceneManager.switchSceneWithUser("student_main.fxml", "Student Main Menu", username);
    }

    @FXML
    private void editName() {
        editField("Enter new name:", nameLabel);
    }

    @FXML
    private void editUsername() {
        editField("Enter new username:", usernameLabel);
    }

    @FXML
    private void editPassword() {
        editField("Enter new password:", passwordLabel);
    }

    private void editField(String prompt, Label targetLabel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(prompt);
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(targetLabel::setText);
    }

    // Dummy theme methods (later can connect to CSS)
    @FXML
    private void setDefaultTheme() { System.out.println("✅ Default theme set"); }

    @FXML
    private void setRGTheme() { System.out.println("✅ R/G theme set"); }

    @FXML
    private void setBYTheme() { System.out.println("✅ B/Y theme set"); }
}
