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
 * Controller for the Student Performance screen.
 * <p>
 * Displays detailed statistics for an individual student's gameplay,
 * including scores, correct/incorrect answers, and average speed for each game.
 * </p>
 * <p>
 * The data is retrieved dynamically from the database using helper methods in
 * {@link Database}. This controller is primarily used by teachers viewing
 * student progress through the class management dashboard.
 * </p>
 */
public class StudentPerformanceController {

    /** Displays the selected student's name. */
    @FXML private Label studentNameLabel;
    /** Displays the student's class code. */
    @FXML private Label classCodeLabel;
    /** Displays the date/time of the student's last played session. */
    @FXML private Label lastPlayedLabel;

    /** TableView displaying the student’s score records. */
    @FXML private TableView<Map<String, Object>> scoreTable;
    /** Table column showing the game name. */
    @FXML private TableColumn<Map<String, Object>, String> colGame;
    /** Table column showing the total score per game. */
    @FXML private TableColumn<Map<String, Object>, String> colScore;
    /** Table column showing the number of correct answers. */
    @FXML private TableColumn<Map<String, Object>, String> colCorrect;
    /** Table column showing the number of incorrect answers. */
    @FXML private TableColumn<Map<String, Object>, String> colIncorrect;
    /** Table column showing the average answer speed. */
    @FXML private TableColumn<Map<String, Object>, String> colAvgSpeed;

    /** Stores the username of the student currently being viewed. */
    private String studentUsername;

    /**
     * Sets the student to display performance data for.
     * <p>
     * This method is called externally by the "Manage Classes" screen
     * when a teacher selects a student from the list.
     * </p>
     *
     * @param username the username of the student to display
     */
    public void setStudent(String username) {
        this.studentUsername = username;
        loadStudentInfo();
        loadScores();
    }

    /**
     * Loads and displays the selected student’s general information.
     * <p>
     * Retrieves the student’s name, class code, and last played date from
     * {@link Database#getStudentInfo(String)}.
     * </p>
     */
    private void loadStudentInfo() {
        Map<String, String> info = Database.getStudentInfo(studentUsername);
        if (info != null) {
            studentNameLabel.setText("Student:" + info.getOrDefault("name", studentUsername));
            classCodeLabel.setText(" Class: " + info.getOrDefault("class_code", "-"));
            lastPlayedLabel.setText(" Last Played: " + info.getOrDefault("last_played", "-"));
        }
    }

    /**
     * Loads and populates the student's score data into the table.
     * <p>
     * Fetches all game performance records using
     * {@link Database#getScoresByStudent(String)} and formats each entry
     * for the JavaFX TableView.
     * </p>
     */
    private void loadScores() {
        List<Map<String, Object>> scores = Database.getScoresByStudent(studentUsername);

        colGame.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().get("game"))));
        colScore.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().get("score"))));
        colCorrect.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().get("correct"))));
        colIncorrect.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().get("incorrect"))));
        colAvgSpeed.setCellValueFactory(cell ->
                new SimpleStringProperty(String.format("%.1f",
                        Double.parseDouble(String.valueOf(cell.getValue().get("avgSpeed"))))));

        scoreTable.setItems(FXCollections.observableArrayList(scores));
    }

    /**
     * Handles the Back button click.
     * <p>
     * Returns the user to the teacher's class management screen.
     * </p>
     */
    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_class_list.fxml", "Manage Classes");
    }
}
