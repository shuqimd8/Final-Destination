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
 * Controller for the Teacher Students screen.
 * <p>
 * Allows teachers to select one of their classes, view the enrolled students,
 * and access each student’s individual performance report.
 * </p>
 * <p>
 * Data is dynamically fetched from the {@link Database} utility and
 * displayed using JavaFX UI components including combo boxes, labels,
 * and list views.
 * </p>
 */
public class TeacherStudentsController {

    /** Label displaying the logged-in teacher’s name. */
    @FXML private Label teacherNameLabel;
    /** Dropdown list of classes managed by the teacher. */
    @FXML private ComboBox<String> classDropdown;
    /** Dropdown list of classes managed by the teacher. */
    @FXML private ListView<String> studentList;
    /** Label displaying the current class code. */
    @FXML private Label classCodeLabel;
    /** Label displaying the average class score. */
    @FXML private Label avgScoreLabel;
    /** Label displaying the total number of students in the class. */
    @FXML private Label totalStudentsLabel;

    /** Username of the currently logged-in teacher. */
    private String currentTeacher;


    /**
     * Initializes the Teacher Students screen.
     * <p>
     * Loads the teacher’s name, populates the class dropdown list,
     * and enables double-click actions for viewing student details.
     * </p>
     */
    @FXML
    public void initialize() {
        currentTeacher = SceneManager.getCurrentUser();
        teacherNameLabel.setText("Teacher " + currentTeacher);

        loadClasses();
        setupStudentClick();
    }

    /**
     * Loads all classes associated with the current teacher into the dropdown.
     * <p>
     * When a class is selected, the corresponding student list and
     * performance statistics are automatically displayed.
     * </p>
     */
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

    /**
     * Displays all students enrolled in the specified class.
     * <p>
     * Also updates the labels showing class code, total student count,
     * and average class score.
     * </p>
     *
     * @param classCode the code of the selected class
     */
    private void showClassStudents(String classCode) {
        List<String> students = Database.getStudentsByClass(classCode);
        studentList.setItems(FXCollections.observableArrayList(students));
        classCodeLabel.setText("Class Code: " + classCode);
        totalStudentsLabel.setText("Students: " + students.size());

        // Show basic stats for class
        double avgScore = Database.getAverageClassScore(classCode);
        avgScoreLabel.setText("Class Avg Score: " + String.format("%.1f", avgScore));
    }

    /**
     * Enables double-click functionality on the student list.
     * <p>
     * When a teacher double-clicks a student’s name, a new scene
     * opens showing that student’s detailed performance statistics.
     * </p>
     */
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

    /**
     * Handles the Back button.
     * <p>
     * Returns the user to the Teacher Main Menu screen.
     * </p>
     */
    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Menu");
    }


    /**
     * Logs out the teacher and returns to the Login screen.
     * <p>
     * Clears the user session information stored in {@link SceneManager}.
     * </p>
     */
    @FXML
    private void handleLogout() {
        SceneManager.setCurrentUser(null, null);
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login");
    }
}
