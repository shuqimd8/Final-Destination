package Controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfileController {

    @FXML private Label usernameLabel;
    @FXML private Label totalGamesLabel;
    @FXML private Label avgScoreLabel;
    @FXML private Label foodHighLabel;
    @FXML private Label natureHighLabel;
    @FXML private Label grammarHighLabel;

    @FXML
    public void initialize() {
        String currentUser = SceneManager.getCurrentUser();
        usernameLabel.setText("👤 " + currentUser);
        loadStats(currentUser);
    }

    private void loadStats(String username) {
        try (Connection conn = Database.getInstance().getConnection()) {

            // Total games played
            try (PreparedStatement stmt1 = conn.prepareStatement(
                    "SELECT COUNT(*) AS total FROM scores WHERE username = ?")) {
                stmt1.setString(1, username);
                ResultSet rs1 = stmt1.executeQuery();
                if (rs1.next()) {
                    totalGamesLabel.setText("🎮 Total Games: " + rs1.getInt("total"));
                }
            }

            // Average score
            try (PreparedStatement stmt2 = conn.prepareStatement(
                    "SELECT AVG(score) AS avgScore FROM scores WHERE username = ?")) {
                stmt2.setString(1, username);
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next()) {
                    avgScoreLabel.setText("📊 Average Score: " + rs2.getInt("avgScore"));
                }
            }

            // Highest score per game
            setHighScore(conn, username, "Food", foodHighLabel);
            setHighScore(conn, username, "Nature", natureHighLabel);
            setHighScore(conn, username, "Grammar", grammarHighLabel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHighScore(Connection conn, String username, String game, Label label) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT MAX(score) AS high FROM scores WHERE username = ? AND game = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, game);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt("high") > 0) {
                label.setText(game + " High: " + rs.getInt("high"));
            } else {
                label.setText(game + " High: -");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle Back button → Return to the correct dashboard (teacher/student)
     */
    @FXML
    private void handleBack() {
        SceneManager.goBackToDashboard();
    }
}


