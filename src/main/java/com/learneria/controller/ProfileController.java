package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Controller for the user profile screen.
 * <p>
 * Displays the player's username, total games played, average score,
 * and high scores for each game category (Food, Nature, Grammar).
 * Data is fetched dynamically from the database for the logged-in user.
 * </p>
 */
public class ProfileController {

    /** Displays the current username. */
    @FXML public Label usernameLabel;
    /** Displays the total number of games played by the user. */
    @FXML public Label totalGamesLabel;
    /** Displays the user's average score across all games. */
    @FXML public Label avgScoreLabel;
    /** Displays the user's highest score in the Food game. */
    @FXML public Label foodHighLabel;
    /** Displays the user's highest score in the Nature game. */
    @FXML public Label natureHighLabel;
    /** Displays the user's highest score in the Grammar game. */
    @FXML public Label grammarHighLabel;

    /**
     * Initializes the Profile screen.
     * <p>
     * Fetches the logged-in user’s name from {@link SceneManager}
     * and loads their gameplay statistics from the database.
     * </p>
     */
    @FXML
    public void initialize() {
        String currentUser = SceneManager.getCurrentUser();
        usernameLabel.setText("👤 " + currentUser);
        loadStats(currentUser);
    }


    /**
     * Loads overall statistics for the specified user.
     * <p>
     * Includes total games played, average score,
     * and highest scores for each game type.
     * </p>
     *
     * @param username the username whose statistics should be loaded
     */
    private void loadStats(String username) {
        try (Connection conn = Database.getInstance().getConnection()) {

            // Total games played
            try (PreparedStatement stmt1 = conn.prepareStatement(
                    "SELECT COUNT(*) AS total FROM scores WHERE username = ?")) {
                stmt1.setString(1, username);
                ResultSet rs1 = stmt1.executeQuery();
                if (rs1.next()) {
                    totalGamesLabel.setText(String.valueOf(rs1.getInt("total")));
                }
            }

            // Average score
            try (PreparedStatement stmt2 = conn.prepareStatement(
                    "SELECT AVG(score) AS avgScore FROM scores WHERE username = ?")) {
                stmt2.setString(1, username);
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next()) {
                    avgScoreLabel.setText(String.valueOf(rs2.getInt("avgScore")));
                }
            }

            // High scores
            setHighScore(conn, username, "Food", foodHighLabel);
            setHighScore(conn, username, "Nature", natureHighLabel);
            setHighScore(conn, username, "Grammar", grammarHighLabel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves and sets the user's highest score for a specific game.
     *
     * @param conn     active database connection
     * @param username target username
     * @param game     game category (e.g., "Food", "Nature", "Grammar")
     * @param label    the label where the high score will be displayed
     */
    private void setHighScore(Connection conn, String username, String game, Label label) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT MAX(score) AS high FROM scores WHERE username = ? AND game = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, game);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt("high") > 0) {
                label.setText(String.valueOf(rs.getInt("high")));
            } else {
                label.setText("-");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the Back button event.
     * <p>
     * Returns the user to their main dashboard.
     * </p>
     */
    @FXML
    public void handleBack() {
        SceneManager.goBackToDashboard();
    }
}
