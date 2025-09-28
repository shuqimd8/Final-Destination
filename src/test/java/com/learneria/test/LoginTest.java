package com.learneria.test;

import com.learneria.utils.Database;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {

    private static final String TEST_USERNAME = "test_student";
    private static final String TEST_PASSWORD = "1234";

    @BeforeAll
    static void setupTestUser() throws Exception {
        try (Connection conn = Database.getConnection()) {
            // Clean up if the user already exists
            PreparedStatement delete = conn.prepareStatement("DELETE FROM users WHERE username = ?");
            delete.setString(1, TEST_USERNAME);
            delete.executeUpdate();

            // Insert a new test student
            PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO users (username, password, role) VALUES (?, ?, ?)"
            );
            insert.setString(1, TEST_USERNAME);
            insert.setString(2, TEST_PASSWORD);
            insert.setString(3, "student");
            insert.executeUpdate();
        }
    }

    @Test
    @Order(1)
    void testValidLogin() throws Exception {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT role FROM users WHERE username = ? AND password = ?"
            );
            pstmt.setString(1, TEST_USERNAME);
            pstmt.setString(2, TEST_PASSWORD);

            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "User should be found in DB");
            assertEquals("student", rs.getString("role"), "Role should match 'student'");
        }
    }

    @Test
    @Order(2)
    void testInvalidLogin() throws Exception {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT role FROM users WHERE username = ? AND password = ?"
            );
            pstmt.setString(1, TEST_USERNAME);
            pstmt.setString(2, "wrongpassword");

            ResultSet rs = pstmt.executeQuery();

            assertFalse(rs.next(), "User should NOT be found with wrong password");
        }
    }

    @AfterAll
    static void cleanup() throws Exception {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement delete = conn.prepareStatement("DELETE FROM users WHERE username = ?");
            delete.setString(1, TEST_USERNAME);
            delete.executeUpdate();
        }
    }
}
