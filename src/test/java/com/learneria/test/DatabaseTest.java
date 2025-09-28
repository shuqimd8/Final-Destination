package com.learneria.test;

import com.learneria.utils.Database;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseTest {

    @BeforeEach
    void resetTable() throws SQLException {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            // wipe old data before each test
            stmt.execute("DELETE FROM users");
        }
    }

    @Test
    void testInsertAndFetchStudent() throws SQLException {
        try (Connection conn = Database.getConnection()) {
            // Insert student
            String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, "student1");
                pstmt.setString(2, "pass123");
                pstmt.setString(3, "student");
                pstmt.executeUpdate();
            }

            // Fetch student
            String querySql = "SELECT username, role FROM users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(querySql)) {
                pstmt.setString(1, "student1");
                ResultSet rs = pstmt.executeQuery();

                assertTrue(rs.next());
                assertEquals("student1", rs.getString("username"));
                assertEquals("student", rs.getString("role"));
            }
        }
    }

    @Test
    void testInsertAndFetchTeacher() throws SQLException {
        try (Connection conn = Database.getConnection()) {
            // Insert teacher
            String insertSql = "INSERT INTO users (username, password, role, teacher_code) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, "teacher1");
                pstmt.setString(2, "teach123");
                pstmt.setString(3, "teacher");
                pstmt.setString(4, "TCH001");
                pstmt.executeUpdate();
            }

            // Fetch teacher
            String querySql = "SELECT username, role, teacher_code FROM users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(querySql)) {
                pstmt.setString(1, "teacher1");
                ResultSet rs = pstmt.executeQuery();

                assertTrue(rs.next());
                assertEquals("teacher1", rs.getString("username"));
                assertEquals("teacher", rs.getString("role"));
                assertEquals("TCH001", rs.getString("teacher_code"));
            }
        }
    }

    @Test
    void testUniqueUsernameConstraint() throws SQLException {
        try (Connection conn = Database.getConnection()) {
            String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

            // First insert
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, "uniqueUser");
                pstmt.setString(2, "abc123");
                pstmt.setString(3, "student");
                pstmt.executeUpdate();
            }

            // Try duplicate insert
            boolean exceptionThrown = false;
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, "uniqueUser");
                pstmt.setString(2, "xyz789");
                pstmt.setString(3, "student");
                pstmt.executeUpdate();
            } catch (SQLException e) {
                exceptionThrown = true;
            }

            assertTrue(exceptionThrown, "Duplicate username should throw an exception");
        }
    }
}

