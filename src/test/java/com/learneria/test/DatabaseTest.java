package com.learneria.test;

import com.learneria.utils.Database;
import com.learneria.utils.ScoreManager;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ✅ Tests for the permanent learneria_data database.
 * Tables (users, scores, words) are auto-created by Database.java.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTest {

    private static Connection conn;

    @BeforeAll
    static void setup() throws Exception {
        conn = Database.getInstance().getConnection();
        System.out.println("✅ Connected to permanent database: " + conn.getMetaData().getURL());
    }

    @BeforeEach
    void clearTables() throws Exception {
        // Clean up between tests (don’t delete words!)
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM scores")) {
            stmt.executeUpdate();
        }
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users")) {
            stmt.executeUpdate();
        }
        System.out.println("🧹 Tables cleared before test");
    }

    @Test
    @Order(1)
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
            if (rs.next() && rs.getInt(1) > 0) exists = true;
        }

        assertTrue(exists, "❌ User not inserted properly!");
        System.out.println("✅ testInsertUser PASSED");
    }

    @Test
    @Order(2)
    void testInsertScore() throws Exception {
        // Insert user first
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (username, password, role, name) VALUES (?, ?, 'student', ?)"
        )) {
            stmt.setString(1, "scoreUser");
            stmt.setString(2, "1234");
            stmt.setString(3, "Score Tester");
            stmt.executeUpdate();
        }

        // Insert score via ScoreManager
        ScoreManager.insertScore("scoreUser", "Food", 75);

        boolean exists = false;
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM scores WHERE username = ? AND game = ? AND score = ?"
        )) {
            stmt.setString(1, "scoreUser");
            stmt.setString(2, "Food");
            stmt.setInt(3, 75);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) exists = true;
        }

        // Print all scores for debug
        System.out.println("📊 Current scores table:");
        ResultSet rs2 = conn.createStatement().executeQuery("SELECT * FROM scores");
        while (rs2.next()) {
            System.out.printf("   %s | %s | %d | %s%n",
                    rs2.getString("username"),
                    rs2.getString("game"),
                    rs2.getInt("score"),
                    rs2.getString("date_played"));
        }

        assertTrue(exists, "❌ Score was not inserted properly!");
        System.out.println("✅ testInsertScore PASSED");
    }

    @Test
    @Order(3)
    void testWordsTable() throws Exception {
        // The Database auto-populates the words table if empty
        ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM words");
        rs.next();
        int count = rs.getInt(1);
        assertTrue(count > 0, "❌ Words table seems empty!");
        System.out.println("✅ Words table contains " + count + " entries.");

        // Random sample
        rs = conn.createStatement().executeQuery("SELECT word, category FROM words ORDER BY RANDOM() LIMIT 5");
        System.out.println("🎲 Random sample words:");
        while (rs.next()) {
            System.out.println("   " + rs.getString("word") + " (" + rs.getString("category") + ")");
        }

        // Check helper method
        List<String> verbs = Database.getAllWords("Verb");
        assertNotNull(verbs);
        System.out.println("🧠 getAllWords('Verb') → " + verbs.size() + " results");
    }
}

