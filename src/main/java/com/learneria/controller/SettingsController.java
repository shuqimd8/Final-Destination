package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import com.learneria.utils.UserAware;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

/**
 * Controller for the Settings screen.
 * <p>
 * Provides functionality for viewing and editing user account information,
 * including name, username, and password. Supports dynamic updates to the
 * database and refreshes display fields accordingly.
 * </p>
 * <p>
 * Implements {@link UserAware} to load user-specific data after login.
 * </p>
 */
public class SettingsController implements UserAware {

    /** Label displaying the user's name. */
    @FXML private Label nameLabel;
    /** Label displaying the user's username. */
    @FXML private Label usernameLabel;
    /** Label displaying the user's masked password. */
    @FXML private Label passwordLabel;

    /** The username of the currently logged-in user. */
    private String username;

    /**
     * Called when the controller is initialized with a user session.
     *
     * @param username the currently logged-in username
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
        loadUserData();
    }

    /**
     * Loads user information from the database and displays it on the UI.
     * <p>
     * Fetches username, password (masked), and display name fields for
     * the current user.
     * </p>
     */
    private void loadUserData() {
        try {
            Connection conn = Database.getInstance().getConnection();

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT username, password, COALESCE(name, username) AS displayName FROM users WHERE username = ?"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usernameLabel.setText(rs.getString("username"));
                passwordLabel.setText("********"); // mask password
                nameLabel.setText(rs.getString("displayName"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the Back button click.
     * <p>
     * Returns the user to the appropriate dashboard depending on their role.
     * </p>
     */
    @FXML
    private void handleBack() {
        // Return to dashboard depending on role
        SceneManager.goBackToDashboard();
    }

    /**
     * Prompts the user to edit their display name and updates it in the database.
     */
    @FXML
    private void editName() {
        editField("Enter new name:", "name", nameLabel);
    }

    /**
     * Prompts the user to edit their username and updates it in the database.
     * Also updates the global reference in {@link SceneManager}.
     */
    @FXML
    private void editUsername() {
        editField("Enter new username:", "username", usernameLabel);
        this.username = usernameLabel.getText(); // update reference
    }

    /**
     * Prompts the user to edit their password and updates it in the database.
     * The displayed password remains masked.
     */
    @FXML
    private void editPassword() {
        editField("Enter new password:", "password", passwordLabel);
        passwordLabel.setText("********"); // always masked
    }

    /**
     * Displays a dialog allowing the user to input a new value for a specific field,
     * then updates the database if the input is valid.
     *
     * @param prompt      the message displayed to the user
     * @param column      the database column to update
     * @param targetLabel the label to update visually (if applicable)
     */
    private void editField(String prompt, String column, Label targetLabel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(prompt);
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(newValue -> {
            if (!newValue.trim().isEmpty()) {
                updateDatabaseField(column, newValue.trim());
                if (!column.equals("password")) {
                    targetLabel.setText(newValue.trim());
                }
            }
        });
    }

    /**
     * Updates the user's database record for a given column.
     * <p>
     * Safely executes an SQL update query and handles username changes
     * by updating the local and global references.
     * </p>
     *
     * @param column   the database field to update
     * @param newValue the new value entered by the user
     */
    private void updateDatabaseField(String column, String newValue) {
        try {
            Connection conn = Database.getInstance().getConnection();
            String sql = "UPDATE users SET " + column + " = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newValue);
            stmt.setString(2, username);
            stmt.executeUpdate();
            stmt.close();

            // If username changed, update local and global references
            if (column.equals("username")) {
                username = newValue;
                SceneManager.setCurrentUser(newValue, SceneManager.getCurrentRole());
            }

            System.out.println(" Updated " + column + " for user: " + username);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  Theme buttons (placeholders)
    /** Sets the default application theme. */
    @FXML private void setDefaultTheme() { System.out.println(" Default theme set"); }
    /** Sets the red/green accessibility theme. */
    @FXML private void setRGTheme() { System.out.println(" Red/Green theme set"); }
    /** Sets the blue/yellow accessibility theme. */
    @FXML private void setBYTheme() { System.out.println(" Blue/Yellow theme set"); }
}


