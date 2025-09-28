package com.learneria.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ScoreManager {

    public static void insertScore(String username, String game, int score) {
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO scores (username, game, score) VALUES (?, ?, ?)"
             )) {
            stmt.setString(1, username);
            stmt.setString(2, game);
            stmt.setInt(3, score);
            stmt.executeUpdate();
            System.out.println("✅ Score saved: " + username + " | " + game + " | " + score);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
