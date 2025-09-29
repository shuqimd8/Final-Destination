package com.learneria.controller;

import com.learneria.utils.Database;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class LoginAndSignupTest {

    private static Connection conn;

    @BeforeAll
    static void setup() throws Exception {
        conn = Database.getInstance().getConnection();
    }

    @AfterEach
    void cleanup() throws Exception {
        // clean up test users after each test
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE username LIKE 'testuser%'")) {
            stmt.executeUpdate();
        }
    }

    @Test
    void testSignupStudent() throws Exception {
        String username = "testuser1";
        String password = "pass123";

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (username, password, role, name) VALUES (?, ?, 'student', ?)"
        )) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, username);
            stmt.executeUpdate();
        }

        // check DB
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next(), "User should exist in DB after signup");
            assertEquals("student", rs.getString("role"));
            assertEquals(password, rs.getString("password"));
        }
    }

    @Test
    void testSignupTeacher() throws Exception {
        String username = "testuser2";
        String password = "teach123";

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (username, password, role, name) VALUES (?, ?, 'teacher', ?)"
        )) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, username);
            stmt.executeUpdate();
        }

        // check DB
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next(), "Teacher should exist in DB after signup");
            assertEquals("teacher", rs.getString("role"));
        }
    }

    @Test
    void testLoginSuccess() throws Exception {
        String username = "testuser3";
        String password = "mypassword";

        // Insert test account first
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (username, password, role, name) VALUES (?, ?, 'student', ?)"
        )) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, username);
            stmt.executeUpdate();
        }

        // Try to login
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT role FROM users WHERE username = ? AND password = ?"
        )) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next(), "Login should succeed with correct username/password");
            assertEquals("student", rs.getString("role"));
        }
    }

    @Test
    void testLoginFailWrongPassword() throws Exception {
        String username = "testuser4";
        String password = "realpass";

        // Insert account
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (username, password, role, name) VALUES (?, ?, 'student', ?)"
        )) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, username);
            stmt.executeUpdate();
        }

        // Try wrong password
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT role FROM users WHERE username = ? AND password = ?"
        )) {
            stmt.setString(1, username);
            stmt.setString(2, "wrongpass");
            ResultSet rs = stmt.executeQuery();

            assertFalse(rs.next(), "Login should fail with wrong password");
        }
    }
}
