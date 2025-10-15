package Controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;

/**
 * TeacherMainController — Main menu for teachers.
 * Links to Dashboard, Class Manager, Reports, and Settings.
 */
public class TeacherMainController {

    @FXML
    private void openDashboard() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_dashboard.fxml", "Teacher Dashboard");
    }

    @FXML
    private void openClassList() { // ✅ New button target
        SceneManager.switchScene("/com/learneria/fxml/teacher_class_list.fxml", "Manage Classes");
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
