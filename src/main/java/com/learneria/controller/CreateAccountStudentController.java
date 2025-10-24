package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Controller for the Student Account Creation form.
 * <p>
 * Handles registration for new student users, including
 * validation of inputs, optional linking to a teacher’s class
 * via a code, and insertion of account data into the database.
 * </p>
 */
public class CreateAccountStudentController {

    /** Field for entering the student's full name. */
    @FXML private TextField nameField;
    /** Field for entering the student's username. */
    @FXML private TextField usernameField;
    /** Field for entering the student's password. */
    @FXML private PasswordField passwordField;
    /** Field for confirming the password. */
    @FXML private PasswordField retypePasswordField;
    /** Field for entering an optional teacher or class code. */
    @FXML private TextField teacherCodeField;

    /**
     * Handles submission of the student registration form.
     * <p>
     * Validates all required fields, checks password matching,
     * verifies class code if provided, inserts data into the
     * database, and navigates to the Student Main Menu.
     * </p>
     */
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

    /**
     * Handles the Back button action.
     * <p>
     * Returns the user to the account type selection screen.
     * </p>
     */
    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Select.fxml", "Select Account Type");
    }
}
