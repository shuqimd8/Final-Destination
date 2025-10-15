package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CreateAccountFormController {

    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField retypePasswordField;
    @FXML private TextField subjectField;

    /** Handle form submission for creating a new teacher account. */
    @FXML
    private void handleSubmit() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String retypePassword = retypePasswordField.getText().trim();
        String subject = subjectField.getText().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || retypePassword.isEmpty() || subject.isEmpty()) {
            System.out.println("Please fill in all fields!");
            return;
        }

        if (!password.equals(retypePassword)) {
            System.out.println(" Passwords do not match!");
            return;
        }

        try {
            Connection conn = Database.getInstance().getConnection();

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (name, username, password, role, subject_taught) VALUES (?, ?, ?, 'teacher', ?)"
            );

            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, subject);
            stmt.executeUpdate();
            stmt.close();

            System.out.println(" Teacher registered: " + username);

            // Save session & open teacher main
            SceneManager.setCurrentUser(username, "teacher");
            SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Main");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Handle back button → return to account type selection */
    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Select.fxml", "Select Account Type");
    }
}
