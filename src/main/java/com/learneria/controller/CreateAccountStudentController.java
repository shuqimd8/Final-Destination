package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CreateAccountStudentController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField teacherCodeField;

    /** Handle form submission for creating a new student account. */
    @FXML
    private void handleSubmit() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String teacherCode = teacherCodeField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("⚠️ Please fill in all required fields!");
            return;
        }

        try {
            Connection conn = Database.getInstance().getConnection();

            // ✅ check teacher code (link to class)
            String classCodeToUse = null;
            if (!teacherCode.isEmpty()) {
                if (Database.isValidClassCode(teacherCode)) {
                    classCodeToUse = teacherCode;
                    System.out.println("🧩 Valid teacher code found: " + classCodeToUse);
                } else {
                    System.out.println("❌ Invalid teacher code, will create as normal student.");
                }
            }

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (username, password, role, teacher_code, name, class_code) VALUES (?, ?, 'student', ?, ?, ?)"
            );

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, teacherCode.isEmpty() ? null : teacherCode);
            stmt.setString(4, username); // default: name = username
            stmt.setString(5, classCodeToUse);
            stmt.executeUpdate();
            stmt.close();

            // ✅ Log + session
            System.out.println("✅ Student registered: " + username +
                    (classCodeToUse != null ? " → Joined class " + classCodeToUse : " (no class)"));

            // ✅ If joined class, reflect link
            if (classCodeToUse != null) {
                Database.assignStudentToClass(username, classCodeToUse);
            }

            // ✅ Redirect
            SceneManager.setCurrentUser(username, "student");
            SceneManager.switchScene("/com/learneria/fxml/student_main.fxml", "Student Main");

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
