package com.learneria.utils;


import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * The {@code ScoreManager} class provides a lightweight utility for recording
 * player scores into the shared SQLite database used by Papa’s Learneria.
 * <p>
 * It delegates database connection handling to {@link Database} and focuses only
 * on safely inserting score entries without closing the shared connection.
 * </p>
 */
public class ScoreManager {

    /**
     * Inserts a score entry into the {@code scores} table for a given user and game.
     * <p>
     * This method uses the persistent database connection from {@link Database}
     * and records the username, game category, and numerical score.
     * </p>
     *
     * @param username the name of the player whose score is being recorded
     * @param game     the game identifier or category (e.g. “Grammar”, “Food”)
     * @param score    the player’s score value to insert
     */
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
            stmt.close(); //  only close the statement, not the connection

            System.out.println("✅ Score saved: " + username + " | " + game + " | " + score);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

