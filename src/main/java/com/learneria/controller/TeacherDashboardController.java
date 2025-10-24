package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Optional;
import java.util.List;
import java.util.*;

/**
 * Controller for the Teacher Dashboard screen.
 * <p>
 * Displays the list of classes managed by the logged-in teacher and provides
 * basic class management functionality, such as adding, editing, and deleting
 * classes. Also allows navigation to settings and logout screens.
 * </p>
 * <p>
 * This serves as the main interface for teachers to manage their classroom
 * activities and student groups within the Papa Learneria application.
 * </p>
 */
public class TeacherDashboardController {

    /** List of all classes belonging to the logged-in teacher. */
    @FXML public ListView<String> classListView;
    /** Label displaying the teacher’s name. */
    @FXML public Label teacherNameLabel;

    /** Stores the username of the currently logged-in teacher. */
    private String teacherUsername;

    /**
     * Initializes the dashboard screen.
     * <p>
     * Loads the teacher’s username, updates the display label,
     * and populates the class list from the database.
     * </p>
     */
    @FXML
    public void initialize() {
        teacherUsername = SceneManager.getCurrentUser();

        // Display the logged-in teacher name dynamically
        if (teacherUsername != null && !teacherUsername.isBlank()) {
            String displayName = teacherUsername;
            if (displayName.contains("@")) {
                displayName = displayName.substring(0, displayName.indexOf("@"));
            }
            displayName = Character.toUpperCase(displayName.charAt(0)) + displayName.substring(1);
            teacherNameLabel.setText(displayName);
        } else {
            teacherNameLabel.setText("Unknown");
        }

        refreshClassList();
    }

    /**
     * Loads and displays all classes belonging to the current teacher.
     * <p>
     * Retrieves class data (name + code) from the database and updates
     * the list view.
     * </p>
     */
    private void refreshClassList() {
        // Get full class info (name + code)
        List<Map<String, String>> classes = Database.getClassesByTeacher(teacherUsername);

        classListView.getItems().clear();
        for (Map<String, String> c : classes) {
            String name = c.get("class_name");
            String code = c.get("class_code");

            // Show both name and code nicely formatted
            classListView.getItems().add(name + " (" + code + ")");
        }
    }

    /**
     * Handles the “Add Class” button.
     * <p>
     * Opens a text input dialog to collect a new class name, inserts it into
     * the database, and refreshes the displayed class list.
     * </p>
     */
    @FXML
    public void handleAddClass() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Class");
        dialog.setHeaderText("Create a new class");
        dialog.setContentText("Enter class name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            Database.createClass(teacherUsername, name);
            refreshClassList();
        });
    }

    /**
     * Handles the “Edit Class” button.
     * <p>
     * Allows renaming of a selected class via a text input dialog.
     * </p>
     */
    @FXML
    public void handleEditClass() {
        String selected = classListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog(selected);
        dialog.setTitle("Edit Class");
        dialog.setHeaderText("Rename Class");
        dialog.setContentText("New class name:");

        dialog.showAndWait().ifPresent(newName -> {
            Database.renameClass(teacherUsername, selected, newName);
            refreshClassList();
        });
    }


    /**
     * Handles the “Delete Class” button.
     * <p>
     * Displays a confirmation alert before permanently removing
     * the selected class from the database.
     * </p>
     */
    @FXML
    public void handleDeleteClass() {
        String selected = classListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Class");
        confirm.setHeaderText("Are you sure you want to delete \"" + selected + "\"?");
        confirm.setContentText("This will remove the class and all its associated data.");

        confirm.showAndWait().ifPresent(r -> {
            if (r.getButtonData().isDefaultButton()) {
                Database.deleteClass(teacherUsername, selected);
                refreshClassList();
            }
        });
    }

    /**
     * Opens the Settings screen for the teacher.
     */
    @FXML
    public void openSettings() {
        SceneManager.switchScene("/com/learneria/fxml/settings.fxml", "Settings");
    }

    /**
     * Logs the teacher out and returns to the Login screen.
     */
    @FXML
    public void handleLogout() {
        SceneManager.setCurrentUser(null, null);
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login");
    }

    /**
     * Navigates back to the Teacher Main Menu screen.
     */
    @FXML
    public void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Main Menu");
    }
    /**
     * Setter for injecting mock UI elements during testing.
     *
     * @param teacherLabel label component for displaying teacher name
     * @param listView     list view component for displaying class names
     */
    void setTestingLabels(Label teacherLabel, ListView<String> listView) {
        this.teacherNameLabel = teacherLabel;
        this.classListView = listView;
    }

}
