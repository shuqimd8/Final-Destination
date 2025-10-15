package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CreateAccountStudentController {

    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField retypePasswordField;
    @FXML private TextField teacherCodeField;

    @FXML
    private void handleSubmit() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String retypePassword = retypePasswordField.getText().trim();
        String teacherCode = teacherCodeField.getText().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || retypePassword.isEmpty()) {
            System.out.println("⚠Please fill in all required fields!");
            return;
        }

        if (!password.equals(retypePassword)) {
            System.out.println("Passwords do not match!");
            return;
        }

        try {
            Connection conn = Database.getInstance().getConnection();

            String classCodeToUse = null;
            if (!teacherCode.isEmpty() && Database.isValidClassCode(teacherCode)) {
                classCodeToUse = teacherCode;
                System.out.println("Valid teacher code found: " + classCodeToUse);
            }

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (name, username, password, role, teacher_code, class_code) VALUES (?, ?, ?, 'student', ?, ?)"
            );

            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, teacherCode.isEmpty() ? null : teacherCode);
            stmt.setString(5, classCodeToUse);
            stmt.executeUpdate();
            stmt.close();

            System.out.println(" Student account created: " + username);
            SceneManager.setCurrentUser(username, "student");
            SceneManager.switchScene("/com/learneria/fxml/student_main.fxml", "Student Main");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Select.fxml", "Select Account Type");
    }
}
