package com.learneria.test;

import com.learneria.utils.Database;
import com.learneria.utils.ScoreManager;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    private static Connection conn;

    @BeforeAll
    static void setup() throws Exception {
        conn = Database.getInstance().getConnection();
        System.out.println("✅ Connected to test database");
    }

    @BeforeEach
    void clearTables() throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM scores")) {
            stmt.executeUpdate();
        }
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users")) {
            stmt.executeUpdate();
        }
    }

    @Test
    void testInsertUser() throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (username, password, role, name) VALUES (?, ?, 'student', ?)"
        )) {
            stmt.setString(1, "testUser");
            stmt.setString(2, "pass123");
            stmt.setString(3, "Test User");
            stmt.executeUpdate();
        }

        boolean exists = false;
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM users WHERE username = ?"
        )) {
            stmt.setString(1, "testUser");
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
        }
        assertTrue(exists, "❌ User was not inserted!");
    }

    @Test
    void testInsertScore() throws Exception {
        // Insert user first (so foreign key constraints don’t break later if enabled)
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (username, password, role, name) VALUES (?, ?, 'student', ?)"
        )) {
            stmt.setString(1, "testUser");
            stmt.setString(2, "pass123");
            stmt.setString(3, "Test User");
            stmt.executeUpdate();
        }

        // Insert score
        ScoreManager.insertScore("testUser", "Food", 50);

        boolean exists = false;
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM scores WHERE username = ? AND game = ? AND score = ?"
        )) {
            stmt.setString(1, "testUser");
            stmt.setString(2, "Food");
            stmt.setInt(3, 50);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
        }

        // Debug print all rows in scores
        ResultSet rs2 = conn.createStatement().executeQuery("SELECT * FROM scores");
        while (rs2.next()) {
            System.out.println("DEBUG Score Row: " +
                    rs2.getString("username") + " | " +
                    rs2.getString("game") + " | " +
                    rs2.getInt("score"));
        }

        assertTrue(exists, "❌ Score was not inserted!");
    }
}

