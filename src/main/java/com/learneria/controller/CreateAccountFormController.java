package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
/**
 * Controller class for the Create Account form.
 * <p>
 * Handles the logic for registering a new teacher account,
 * validating input fields, saving the user data into the database,
 * and switching scenes after account creation.
 * </p>
 */
public class CreateAccountFormController {

    /** Text field for entering the teacher's full name. */
    @FXML public TextField nameField;
    /** Text field for entering the teacher's username. */
    @FXML public TextField usernameField;
    /** Password field for entering the new account password. */
    @FXML public PasswordField passwordField;
    /** Password field for retyping the password to confirm. */
    @FXML public PasswordField retypePasswordField;
    /** Text field for entering the subject taught by the teacher. */
    @FXML public TextField subjectField;

    /**
     * Handles form submission for creating a new teacher account.
     * <p>
     * This method validates the form fields, checks password matching,
     * inserts a new teacher record into the database, and navigates
     * to the teacher main menu scene upon successful registration.
     * </p>
     */
    @FXML
    public void handleSubmit() {
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

    /**
     * Handles the Back button action.
     * <p>
     * Returns the user to the account type selection screen
     * when they decide not to create a new account.
     * </p>
     */
    @FXML
    public void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Select.fxml", "Select Account Type");
    }
}
