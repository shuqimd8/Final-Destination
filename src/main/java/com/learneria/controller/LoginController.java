package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Controller for the Login screen.
 * <p>
 * Handles user authentication by verifying credentials stored in the database.
 * Directs the user to their appropriate dashboard based on their role (teacher or student).
 * </p>
 */
public class LoginController {

    /** Text field for the user's username input. */
    @FXML
    public TextField usernameField;

    /** Password field for the user's password input. */
    @FXML
    public PasswordField passwordField;

    /**
     * Handles the Sign In button action.
     * <p>
     * Validates user input, checks the credentials against the database,
     * and redirects the user to either the student or teacher main menu
     * based on their assigned role.
     * </p>
     */
    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Please enter username and password");
            return;
        }

        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println(" Login successful: " + username + " (" + role + ")");

                // Remember the logged-in user & role globally
                SceneManager.setCurrentUser(username, role);

                //  Navigate based on role
                if ("student".equalsIgnoreCase(role)) {
                    SceneManager.switchScene(
                            "/com/learneria/fxml/student_main.fxml",
                            "Student Main Menu"
                    );
                } else if ("teacher".equalsIgnoreCase(role)) {
                    SceneManager.switchScene(
                            "/com/learneria/fxml/teacher_main.fxml",
                            "Teacher Main Menu"
                    );
                } else {
                    System.out.println("️ Unknown role: " + role);
                }

            } else {
                System.out.println(" Invalid credentials!");
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the Create Account button action.
     * <p>
     * Redirects the user to the account type selection page
     * for creating a new account.
     * </p>
     */
    @FXML
    private void handleCreateAccount() {
        SceneManager.switchScene(
                "/com/learneria/fxml/createAccount_Select.fxml",
                "Create Account - Select Type"
        );
    }
}

