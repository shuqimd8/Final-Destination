package Controller;

import com.learneria.utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ScoreManager {

    public static void insertScore(String username, String game, int score) {
        try {
            // Reuse the persistent connection from Database.java
            Connection conn = Database.getInstance().getConnection();

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO scores (username, game, score) VALUES (?, ?, ?)"
            );
            stmt.setString(1, username);
            stmt.setString(2, game);
            stmt.setInt(3, score);
            stmt.executeUpdate();
            stmt.close(); // ✅ only close the statement, not the connection

            System.out.println("✅ Score saved: " + username + " | " + game + " | " + score);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

