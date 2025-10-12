package com.learneria.controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;

public class TeacherMainController {

    @FXML
    private void openDashboard() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_dashboard.fxml", "Teacher Dashboard");
    }

    @FXML
    private void openStudentList() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_students.fxml", "My Students");
    }

    @FXML
    private void openReports() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_reports.fxml", "Reports");
    }

    @FXML
    private void openSettings() {
        SceneManager.switchScene("/com/learneria/fxml/settings.fxml", "Settings");
    }

    @FXML
    private void handleLogout() {
        SceneManager.setCurrentUser(null, null);
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login");
    }
}


