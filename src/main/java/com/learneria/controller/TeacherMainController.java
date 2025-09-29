package com.learneria.controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;

public class TeacherMainController {

    @FXML
    private void openDashboard() {
        SceneManager.switchSceneWithUser(
                "/com/learneria/fxml/teacher_dashboard.fxml",
                "Teacher Dashboard",
                SceneManager.getCurrentUser()
        );
    }

    @FXML
    private void openStudentList() {
        SceneManager.switchSceneWithUser(
                "/com/learneria/fxml/teacher_students.fxml",
                "My Students",
                SceneManager.getCurrentUser()
        );
    }

    @FXML
    private void openReports() {
        SceneManager.switchSceneWithUser(
                "/com/learneria/fxml/teacher_reports.fxml",
                "Reports",
                SceneManager.getCurrentUser()
        );
    }

    @FXML
    private void openSettings() {
        SceneManager.switchSceneWithUser(
                "/com/learneria/fxml/settings.fxml",
                "Settings",
                SceneManager.getCurrentUser() // ✅ Pass logged-in teacher username
        );
    }

    @FXML
    private void handleLogout() {
        SceneManager.setCurrentUser(null);
        SceneManager.switchScene(
                "/com/learneria/fxml/login.fxml",
                "Login"
        );
    }
}

