package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.sql.*;

public class StudentStatsController {

    @FXML private Label studentNameLabel;
    @FXML private Label correctLabel;
    @FXML private Label incorrectLabel;
    @FXML private Label gamesLabel;
    @FXML private Label wordsLabel;
    @FXML private Label speedLabel;

    public void setStudentName(String student) {
        studentNameLabel.setText("📚 " + student + "'s Stats");
        loadStats(student);
    }

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

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_students.fxml", "Teacher Dashboard");
    }
}
