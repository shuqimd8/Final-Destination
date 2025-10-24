package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.sql.*;

/**
 * Controller for the Student Statistics screen.
 * <p>
 * Displays detailed cumulative performance statistics for a selected student,
 * including total correct/incorrect answers, number of games played,
 * total words sorted, and average answer speed.
 * </p>
 * <p>
 * This view is typically accessed by teachers from their student management
 * dashboard to review learning progress and engagement analytics.
 * </p>
 */
public class StudentStatsController {

    /** Label showing the selected student's name and title. */
    @FXML private Label studentNameLabel;
    /** Label displaying the total number of correct answers. */
    @FXML private Label correctLabel;
    /** Label displaying the total number of incorrect answers. */
    @FXML private Label incorrectLabel;
    /** Label showing how many games the student has played. */
    @FXML private Label gamesLabel;
    /** Label showing the total number of words attempted across all games. */
    @FXML private Label wordsLabel;
    /** Label displaying the student's average answering speed. */
    @FXML private Label speedLabel;

    /**
     * Sets the student whose statistics should be displayed.
     * <p>
     * Updates the label title and triggers data retrieval from the database.
     * </p>
     *
     * @param student the username of the student to display
     */
    public void setStudentName(String student) {
        studentNameLabel.setText("📚 " + student + "'s Stats");
        loadStats(student);
    }

    /**
     * Loads performance statistics for the given student.
     * <p>
     * Executes a SQL query that aggregates total correct/incorrect counts,
     * distinct games played, total words attempted, and average answer speed
     * from the <code>scores</code> table.
     * </p>
     *
     * @param username the username of the student whose statistics are being loaded
     */
    private void loadStats(String username) {
        String sql = """
            SELECT 
                SUM(correct) AS total_correct,
                SUM(incorrect) AS total_incorrect,
                COUNT(DISTINCT date_played) AS games_played,
                COUNT(*) AS total_words,
                AVG(avgSpeed) AS avg_speed
            FROM scores
            WHERE username = ?
        """;

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                correctLabel.setText("Total Correct Answers: " + rs.getInt("total_correct"));
                incorrectLabel.setText("Total Incorrect Answers: " + rs.getInt("total_incorrect"));
                gamesLabel.setText("Games Played: " + rs.getInt("games_played"));
                wordsLabel.setText("Total Words: " + rs.getInt("total_words"));
                speedLabel.setText(String.format("Average Speed: %.2f s", rs.getDouble("avg_speed")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the Back button click event.
     * <p>
     * Returns the user to the teacher’s student management dashboard.
     * </p>
     */
    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_students.fxml", "Teacher Dashboard");
    }
}
