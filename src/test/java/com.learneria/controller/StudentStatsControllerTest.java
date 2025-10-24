import com.learneria.controller.StudentStatsController;
import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StudentStatsController.
 * Verifies setStudentName(), loadStats(), and handleBack() logic without a real DB.
 */
public class StudentStatsControllerTest {

    private StudentStatsController controller;

    @BeforeAll
    static void setupFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {}
    }

    @BeforeEach
    void setup() throws Exception {
        controller = new StudentStatsController();

        // Inject FXML labels
        setPrivateField("studentNameLabel", new Label());
        setPrivateField("correctLabel", new Label());
        setPrivateField("incorrectLabel", new Label());
        setPrivateField("gamesLabel", new Label());
        setPrivateField("wordsLabel", new Label());
        setPrivateField("speedLabel", new Label());
    }

    private void setPrivateField(String name, Object value) throws Exception {
        Field f = StudentStatsController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }

    private Label getLabel(String name) throws Exception {
        Field f = StudentStatsController.class.getDeclaredField(name);
        f.setAccessible(true);
        return (Label) f.get(controller);
    }

    private void invokeSetStudent(String username) throws Exception {
        controller.setStudentName(username);
    }

    // ---------- TESTS ----------

    @Test
    void testSetStudentName_updatesLabelAndLoadsStats() throws Exception {
        // Mock JDBC chain
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("total_correct")).thenReturn(15);
        when(mockRs.getInt("total_incorrect")).thenReturn(3);
        when(mockRs.getInt("games_played")).thenReturn(5);
        when(mockRs.getInt("total_words")).thenReturn(200);
        when(mockRs.getDouble("avg_speed")).thenReturn(4.5);

        try (MockedStatic<Database> mockDb = mockStatic(Database.class)) {
            mockDb.when(Database::connect).thenReturn(mockConn);

            assertDoesNotThrow(() -> invokeSetStudent("Alex"));

            Label name = getLabel("studentNameLabel");
            Label correct = getLabel("correctLabel");
            Label incorrect = getLabel("incorrectLabel");
            Label games = getLabel("gamesLabel");
            Label words = getLabel("wordsLabel");
            Label speed = getLabel("speedLabel");

            assertTrue(name.getText().contains("Alex"));
            assertTrue(correct.getText().contains("15"));
            assertTrue(incorrect.getText().contains("3"));
            assertTrue(games.getText().contains("5"));
            assertTrue(words.getText().contains("200"));
            assertTrue(speed.getText().contains("4.50"));
        }
    }

    @Test
    void testHandleBack_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            // use reflection to access private handleBack()
            var method = StudentStatsController.class.getDeclaredMethod("handleBack");
            method.setAccessible(true);
            assertDoesNotThrow(() -> method.invoke(controller),
                    "handleBack() should execute without throwing");
        }
    }

}
