package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * TeacherStudentsController — lets teacher pick a class and view its students.
 */
public class TeacherStudentsController {

    @FXML private Label teacherNameLabel;
    @FXML private ComboBox<String> classDropdown;
    @FXML private ListView<String> studentList;
    @FXML private Label classCodeLabel;
    @FXML private Label avgScoreLabel;
    @FXML private Label totalStudentsLabel;

    private String currentTeacher;

    @FXML
    public void initialize() {
        currentTeacher = SceneManager.getCurrentUser();
        teacherNameLabel.setText("Teacher " + currentTeacher);

        loadClasses();
        setupStudentClick();
    }

    /** Load teacher’s classes into dropdown */
    private void loadClasses() {
        List<Map<String, String>> classes = Database.getClassesByTeacher(currentTeacher);
        ObservableList<String> classNames = FXCollections.observableArrayList();
        for (Map<String, String> c : classes) {
            classNames.add(c.get("class_name") + " (" + c.get("class_code") + ")");
        }
        classDropdown.setItems(classNames);

        classDropdown.setOnAction(event -> {
            String selected = classDropdown.getValue();
            if (selected == null) return;
            String classCode = selected.substring(selected.indexOf("(") + 1, selected.indexOf(")"));
            showClassStudents(classCode);
        });
    }

    /** Display students for selected class */
    private void showClassStudents(String classCode) {
        List<String> students = Database.getStudentsByClass(classCode);
        studentList.setItems(FXCollections.observableArrayList(students));
        classCodeLabel.setText("Class Code: " + classCode);
        totalStudentsLabel.setText("Students: " + students.size());

        // Show basic stats for class
        double avgScore = Database.getAverageClassScore(classCode);
        avgScoreLabel.setText("Class Avg Score: " + String.format("%.1f", avgScore));
    }

    /** When teacher clicks a student → open performance page */
    private void setupStudentClick() {
        studentList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // double-click
                String selectedStudent = studentList.getSelectionModel().getSelectedItem();
                if (selectedStudent == null || selectedStudent.isEmpty()) return;

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/learneria/fxml/student_performance.fxml"));
                    Parent root = loader.load();

                    // Get controller and pass student username
                    StudentPerformanceController controller = loader.getController();
                    controller.setStudent(selectedStudent);

                    Stage stage = (Stage) studentList.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Student Performance - " + selectedStudent);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Menu");
    }

    @FXML
    private void handleLogout() {
        SceneManager.setCurrentUser(null, null);
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login");
    }
}
