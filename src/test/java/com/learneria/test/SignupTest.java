package com.learneria.test;

import com.learneria.utils.Database;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class SignupTest {

    @Test
    void testStudentSignup() throws Exception {
        String username = "studentTest";
        String password = "12345";

        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'student')";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
            }

            // Check it was saved
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users WHERE username='" + username + "'");
            assertTrue(rs.next(), "Student account should exist");
            assertEquals("student", rs.getString("role"));
        }
    }
}
