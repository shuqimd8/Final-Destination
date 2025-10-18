package com.learneria.test;

import com.learneria.utils.Database;
import com.learneria.utils.ScoreManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Clean unit test for ScoreManager.insertScore().
 * Verifies SQL execution without touching the real database.
 */
public class ScoreManagerTest {

    private Connection mockConnection;
    private PreparedStatement mockStatement;

    @BeforeEach
    void setup() throws Exception {
        // Mock connection and statement
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        // Mock Database singleton instance
        Database mockDb = mock(Database.class);
        when(mockDb.getConnection()).thenReturn(mockConnection);

        // Inject mock Database into static instance
        var field = Database.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, mockDb);
    }

    @Test
    void testInsertScore_noCrash() {
        assertDoesNotThrow(() ->
                        ScoreManager.insertScore("Alice", "FoodGame", 120),
                "Should not throw any exception");
    }

    @Test
    void testInsertScore_executesCorrectSQL() throws Exception {
        ScoreManager.insertScore("Bob", "GrammarGame", 95);

        verify(mockConnection).prepareStatement("INSERT INTO scores (username, game, score) VALUES (?, ?, ?)");
        verify(mockStatement).setString(1, "Bob");
        verify(mockStatement).setString(2, "GrammarGame");
        verify(mockStatement).setInt(3, 95);
        verify(mockStatement).executeUpdate();
        verify(mockStatement).close();
    }

    @Test
    void testInsertScore_handlesExceptionGracefully() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new RuntimeException("DB error"));

        Database mockDb = mock(Database.class);
        when(mockDb.getConnection()).thenReturn(mockConnection);

        var field = Database.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, mockDb);

        assertDoesNotThrow(() ->
                        ScoreManager.insertScore("Eve", "NatureGame", 75),
                "Should handle database exception gracefully");
    }
}
