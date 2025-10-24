package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;
import java.util.Map;

/**
 * Controller for the Teacher Reports screen.
 * <p>
 * Displays class performance summaries, top students, and
 * average game statistics for the currently logged-in teacher.
 * This view is intended to provide teachers with quick insights
 * into class-level and game-level performance metrics.
 * </p>
 * <p>
 * The controller retrieves all data dynamically from the
 * {@link Database} utility class and binds the results to
 * JavaFX UI components including tables and list views.
 * </p>
 */
public class TeacherReportsController {

    /** Table displaying overview of classes managed by the teacher. */
    @FXML private TableView<Map<String, Object>> classTable;
    /** Table column showing class names. */
    @FXML private TableColumn<Map<String, Object>, String> colClassName;
    /** Table column showing class codes. */
    @FXML private TableColumn<Map<String, Object>, String> colClassCode;
    /** Table column showing average class score. */
    @FXML private TableColumn<Map<String, Object>, String> colAvgScore;
    /** Table column showing number of students in the class. */
    @FXML private TableColumn<Map<String, Object>, String> colStudentCount;

    /** List view displaying top-performing students. */
    @FXML private ListView<String> topStudentsList;
    /** List view displaying average scores per game category. */
    @FXML private ListView<String> gameStatsList;

    /** Username of the currently logged-in teacher. */
    private String teacherUsername;

    /**
     * Initializes the Reports screen.
     * <p>
     * Retrieves the logged-in teacher’s username and loads
     * the class overview, top students, and game statistics.
     * </p>
     */
    @FXML
    public void initialize() {
        teacherUsername = SceneManager.getCurrentUser();

        loadClassOverview();
        loadTopStudents();
        loadGameStats();
    }

    /**
     * Loads a summary of all classes managed by the teacher.
     * <p>
     * Retrieves class data (name, code, average score, student count)
     * from the database and binds it to the {@link TableView}.
     * </p>
     */
    private void loadClassOverview() {
        List<Map<String, Object>> data = Database.getClassOverview(teacherUsername);

        colClassName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get("class_name").toString()));
        colClassCode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get("class_code").toString()));
        colAvgScore.setCellValueFactory(cell -> {
            Object val = cell.getValue().get("avg_score");
            String text = (val instanceof Double)
                    ? String.format("%.1f", (Double) val)
                    : String.valueOf(val);
            return new SimpleStringProperty(text);
        });
        colStudentCount.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().get("student_count"))));

        classTable.setItems(FXCollections.observableArrayList(data));
    }


    /**
     * Loads the top-performing students across all teacher classes.
     * <p>
     * Retrieves each student's average score and displays
     * them in the {@link #topStudentsList}.
     * </p>
     */
    private void loadTopStudents() {
        List<Map<String, Object>> top = Database.getTopStudents(teacherUsername);
        for (Map<String, Object> s : top) {
            topStudentsList.getItems().add("Top! " + s.get("username") + " — " + s.get("avg_score"));
        }
    }

    /**
     * Loads average game performance statistics for all classes.
     * <p>
     * Displays the average score per game category in the
     * {@link #gameStatsList}.
     * </p>
     */
    private void loadGameStats() {
        Map<String, Double> stats = Database.getAverageByGame(teacherUsername);
        stats.forEach((game, score) -> {
            gameStatsList.getItems().add(game + ": " + String.format("%.1f", score));
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
}
