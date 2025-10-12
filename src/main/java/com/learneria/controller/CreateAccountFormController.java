package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CreateAccountFormController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    /** Handle form submission for creating a new teacher account. */
    @FXML
    private void handleSubmit() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("⚠️ Please fill in all fields!");
            return;
        }

        try {
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (username, password, role, name) VALUES (?, ?, 'teacher', ?)"
            );

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, username); // default: name = username
            stmt.executeUpdate();
            stmt.close();

            System.out.println("✅ Teacher registered: " + username);

            // ✅ Save session & open teacher main
            SceneManager.setCurrentUser(username, "teacher");
            SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Main");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Handle back button → return to account type selection */
    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Select.fxml",
                "Select Account Type");
    }
}



