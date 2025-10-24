package com.learneria.controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;

/**
 * Controller for the Teacher Main Menu screen.
 * <p>
 * Serves as the central navigation hub for teachers after logging in.
 * Provides access to classroom management, performance reports,
 * settings, and logout functionality.
 * </p>
 */
public class TeacherMainController {

    /**
     * Opens the Teacher Dashboard scene.
     * <p>
     * Displays an overview of the teacher’s classes and provides
     * management options for creating, renaming, or deleting classes.
     * </p>
     */
    @FXML
    private void openDashboard() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_dashboard.fxml", "Teacher Dashboard");
    }

    /**
     * Opens the Class List screen.
     * <p>
     * Allows the teacher to view class codes, enrolled students,
     * and navigate to individual student performance pages.
     * </p>
     */
    @FXML
    private void openClassList() { //  New button target
        SceneManager.switchScene("/com/learneria/fxml/teacher_class_list.fxml", "Manage Classes");
    }

    /**
     * Opens the Reports screen.
     * <p>
     * Displays class and student performance analytics
     * (placeholder for future reporting features).
     * </p>
     */
    @FXML
    private void openReports() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_reports.fxml", "Reports");
    }

    /**
     * Opens the Settings screen.
     * <p>
     * Allows teachers to update personal information and theme preferences.
     * </p>
     */
    @FXML
    private void openSettings() {
        SceneManager.switchScene("/com/learneria/fxml/settings.fxml", "Settings");
    }

    /**
     * Logs out the current teacher and returns to the Login screen.
     * <p>
     * Clears the user session stored in {@link SceneManager}.
     * </p>
     */
    @FXML
    private void handleLogout() {
        SceneManager.setCurrentUser(null, null);
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login");
    }
}
