package com.learneria.controller;

import com.learneria.utils.SceneManager;
import com.learneria.utils.UserAware;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


/**
 * Controller for the Student Dashboard screen.
 * <p>
 * Displays a personalized welcome message to the logged-in student
 * and provides basic navigation actions such as logging out.
 * </p>
 * <p>
 * Implements {@link UserAware} so the student's username is injected
 * and displayed dynamically upon login.
 * </p>
 */
public class StudentDashboardController implements UserAware {

    /** Label displaying a welcome message to the logged-in student. */
    @FXML
    private Label welcomeLabel;

    /** Stores the username of the currently logged-in student. */
    private String username;


    /**
     * Called automatically when the user logs in.
     * <p>
     * Sets the username and updates the welcome message displayed on screen.
     * </p>
     *
     * @param username the username of the logged-in student
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    /**
     * Handles the Logout button event.
     * <p>
     * Redirects the user back to the login screen and clears session state.
     * </p>
     */
    @FXML
    private void handleLogout() {
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Sign In");
    }
}

