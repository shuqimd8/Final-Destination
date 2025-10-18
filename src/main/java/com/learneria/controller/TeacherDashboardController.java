package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Optional;
import java.util.List;
import java.util.*;

public class TeacherDashboardController {

    @FXML public ListView<String> classListView;
    @FXML public Label teacherNameLabel;

    private String teacherUsername;

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

    /** Load the teacher’s classes from DB */
    /** Load the teacher’s classes from DB and show code */
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


    /** Add a new class */
    @FXML
    private void handleAddClass() {
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

    /** Edit the selected class */
    @FXML
    private void handleEditClass() {
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

    /** Delete the selected class */
    @FXML
    private void handleDeleteClass() {
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

    /** Open Settings page */
    @FXML
    private void openSettings() {
        SceneManager.switchScene("/com/learneria/fxml/settings.fxml", "Settings");
    }

    /** Log out to login page */
    @FXML
    public void handleLogout() {
        SceneManager.setCurrentUser(null, null);
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login");
    }
    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Main Menu");
    }

    public void setTestingLabels(Label teacherLabel, ListView<String> listView) {
        this.teacherNameLabel = teacherLabel;
        this.classListView = listView;
    }

}
