package com.learneria.test;

import com.learneria.utils.Database;
import org.junit.jupiter.api.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests basic Database functions using an isolated SQLite file.
 * Does not depend on API sync or external data.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTest {

    private static final String TEST_DB_PATH = "test_learneria.db";

    @BeforeAll
    static void setupTestDatabase() throws Exception {
        // Redirect SQLite path to a local temp test file
        System.setProperty("user.home", new File(".").getAbsolutePath());

        // Create minimal schema manually to bypass full setup
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + TEST_DB_PATH);
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (username TEXT, class_code TEXT)");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS scores (username TEXT, game TEXT, score INTEGER)");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS classes (class_name TEXT, class_code TEXT, teacher_username TEXT)");
        conn.close();
    }

    @Test
    @Order(1)
    void testCreateClassAndRetrieve() {
        boolean created = Database.createClass("teacherA", "Test Class");
        assertTrue(created, "Should create class without error");

        List<Map<String, String>> classes = Database.getClassesByTeacher("teacherA");
        assertFalse(classes.isEmpty(), "Teacher should have at least one class");
        assertEquals("Test Class", classes.get(0).get("class_name"));
    }

    @Test
    @Order(2)
    void testAssignStudentAndFetch() throws Exception {
        // Insert mock student with required NOT NULL fields
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
            INSERT INTO users (username, password, role, teacher_code, name, class_code)
            VALUES ('student1', 'pass123', 'student', NULL, 'Alice', NULL)
        """);
        }

        Database.assignStudentToClass("student1", "CLS-1234");

        List<String> students = Database.getStudentsByClass("CLS-1234");
        System.out.println("🧪 Students in CLS-1234: " + students);
        assertTrue(students.contains("student1"), "Student should be listed under assigned class");
    }



    @Test
    @Order(3)
    void testUpdateAndFetchScore() {
        Database.updateScore("student1", "Grammar", 95, 10, 2, 1.5);
        double avg = Database.getAverageClassScore("CLS-1234");
        assertTrue(avg >= 0, "Average score query should not fail");
    }

    @Test
    @Order(4)
    void testReconnectAndConnection() {
        assertNotNull(Database.connect(), "Should return a valid DB connection");
    }

    @AfterAll
    static void cleanup() {
        File f = new File(TEST_DB_PATH);
        if (f.exists()) f.delete();
    }
}
