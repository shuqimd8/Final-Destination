package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the Teacher Class List screen.
 * <p>
 * Displays all classes created by the logged-in teacher, including
 * student membership, class performance averages, and the ability to
 * view individual student performance.
 * </p>
 * <p>
 * Provides navigation to the student performance screen, settings,
 * and logout options. All data is dynamically retrieved from the
 * {@link Database} utility.
 * </p>
 */
public class TeacherClassListController {

    /** Label showing the logged-in teacher’s username. */
    @FXML private Label teacherNameLabel;
    /** List of all classes owned by the teacher. */
    @FXML private ListView<String> classListView;
    /** Label showing the selected class name. */
    @FXML private Label classNameLabel;
    /** Label showing the selected class code. */
    @FXML private Label classCodeLabel;
    /** Label showing the number of students enrolled in the selected class. */
    @FXML private Label studentCountLabel;
    /** Label showing the average score for the selected class. */
    @FXML private Label avgScoreLabel;
    /** List of all students in the selected class. */
    @FXML private ListView<String> studentListView;
    /** Button used to open a detailed view of the selected student. */
    @FXML private Button viewStudentButton;

    /** Username of the currently logged-in teacher. */
    private String teacherUsername;
    /** Maps displayed class titles to their corresponding class codes. */
    private final Map<String, String> classTitleToCode = new HashMap<>();

    /**
     * Initializes the class list view for the teacher.
     * <p>
     * Retrieves all classes from the database, binds click listeners,
     * and configures the student list to open details on double-click.
     * </p>
     */
    @FXML
    public void initialize() {
        teacherUsername = SceneManager.getCurrentUser();
        teacherNameLabel.setText("Teacher " + teacherUsername);

        loadClasses();

        classListView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) showClass(n);
        });

        studentListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) openSelectedStudent();
        });

        viewStudentButton.setOnAction(e -> openSelectedStudent());
        viewStudentButton.setDisable(true);

        studentListView.getSelectionModel().selectedItemProperty().addListener((o1, o2, n) ->
                viewStudentButton.setDisable(n == null));
    }

    /**
     * Loads all classes owned by the current teacher.
     * <p>
     * Populates the class list and maps each class title to its unique code.
     * Automatically selects and displays the first class, if available.
     * </p>
     */
    private void loadClasses() {
        List<Map<String, String>> classes = Database.getClassesByTeacher(teacherUsername);

        ObservableList<String> items = FXCollections.observableArrayList();
        classTitleToCode.clear();
        for (Map<String, String> c : classes) {
            String title = c.get("class_name") + " (" + c.get("class_code") + ")";
            items.add(title);
            classTitleToCode.put(title, c.get("class_code"));
        }
        classListView.setItems(items);

        if (!items.isEmpty()) {
            classListView.getSelectionModel().select(0);
            showClass(items.get(0));
        } else {
            // clear right pane
            classNameLabel.setText("-");
            classCodeLabel.setText("-");
            studentCountLabel.setText("-");
            avgScoreLabel.setText("-");
            studentListView.setItems(FXCollections.observableArrayList());
        }
    }
    /**
     * Displays details for the selected class.
     * <p>
     * Updates class information (name, code, student count, and average score)
     * and refreshes the student list.
     * </p>
     *
     * @param classTitle the selected class name and code in display format
     */
    private void showClass(String classTitle) {
        String code = classTitleToCode.get(classTitle);
        classNameLabel.setText(classTitle.substring(0, classTitle.indexOf(" (")));
        classCodeLabel.setText(code);

        List<String> students = Database.getStudentsByClass(code);
        studentListView.setItems(FXCollections.observableArrayList(students));
        studentCountLabel.setText(String.valueOf(students.size()));

        double avg = Database.getAverageClassScore(code);
        avgScoreLabel.setText(students.isEmpty() ? "-" : String.format("%.1f", avg));
    }

    /**
     * Opens the detailed performance view for the selected student.
     * <p>
     * Transfers the selected student’s username to
     * {@link StudentPerformanceController} via the {@link SceneManager}.
     * </p>
     */
    private void openSelectedStudent() {
        String username = studentListView.getSelectionModel().getSelectedItem();
        if (username == null || username.isBlank()) return;

        // open student_performance.fxml and pass username
        SceneManager.switchSceneWithController(
                "/com/learneria/fxml/student_performance.fxml",
                "Student Performance",
                controller -> {
                    if (controller instanceof StudentPerformanceController spc) {
                        spc.setStudent(username);
                    }
                }
        );
    }
    /**
     * Handles the Back button click.
     * <p>
     * Returns the teacher to the main menu screen.
     * </p>
     */
    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Main Menu");
    }

    /**
     * Logs the teacher out and returns to the login screen.
     * <p>
     * Clears the user session stored in {@link SceneManager}.
     * </p>
     */
    @FXML
    private void handleLogout() {
        SceneManager.setCurrentUser(null, null);
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login");
    }

    /**
     * Opens the settings screen for the teacher.
     */
    @FXML
    private void openSettings() {
        SceneManager.switchScene("/com/learneria/fxml/settings.fxml", "Settings");
    }

}
