import com.learneria.controller.StudentPerformanceController;
import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for StudentPerformanceController.
 * Ensures setStudent, loadStudentInfo, loadScores, and handleBack execute safely.
 */
public class StudentPerformanceControllerTest {

    private StudentPerformanceController controller;

    @BeforeAll
    static void setupFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {}
    }

    @BeforeEach
    void setup() throws Exception {
        controller = new StudentPerformanceController();

        // Inject mock UI elements
        setPrivateField("studentNameLabel", new Label());
        setPrivateField("classCodeLabel", new Label());
        setPrivateField("lastPlayedLabel", new Label());

        TableView<Map<String, Object>> table = new TableView<>();
        setPrivateField("scoreTable", table);
        setPrivateField("colGame", new TableColumn<Map<String, Object>, String>());
        setPrivateField("colScore", new TableColumn<Map<String, Object>, String>());
        setPrivateField("colCorrect", new TableColumn<Map<String, Object>, String>());
        setPrivateField("colIncorrect", new TableColumn<Map<String, Object>, String>());
        setPrivateField("colAvgSpeed", new TableColumn<Map<String, Object>, String>());
    }

    private void setPrivateField(String name, Object value) throws Exception {
        Field f = StudentPerformanceController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }

    private Object getPrivateField(String name) throws Exception {
        Field f = StudentPerformanceController.class.getDeclaredField(name);
        f.setAccessible(true);
        return f.get(controller);
    }

    private void invokePrivate(String name) throws Exception {
        Method m = StudentPerformanceController.class.getDeclaredMethod(name);
        m.setAccessible(true);
        m.invoke(controller);
    }

    @Test
    void testSetStudent_noCrashAndUpdatesLabels() throws Exception {
        try (MockedStatic<Database> mockDb = mockStatic(Database.class)) {
            mockDb.when(() -> Database.getStudentInfo("alex"))
                    .thenReturn(Map.of("name", "Alex", "class_code", "CS101", "last_played", "2025-10-01"));
            mockDb.when(() -> Database.getScoresByStudent("alex"))
                    .thenReturn(List.of(Map.of("game", "Food", "score", 100, "correct", 10, "incorrect", 2, "avgSpeed", 5.2)));

            assertDoesNotThrow(() -> controller.setStudent("alex"), "setStudent() should not throw any exception");

            Label nameLabel = (Label) getPrivateField("studentNameLabel");
            Label classLabel = (Label) getPrivateField("classCodeLabel");
            Label lastLabel = (Label) getPrivateField("lastPlayedLabel");

            assertTrue(nameLabel.getText().contains("Alex"));
            assertTrue(classLabel.getText().contains("CS101"));
            assertTrue(lastLabel.getText().contains("2025-10-01"));
        }
    }

    @Test
    void testLoadScores_populatesTable() throws Exception {
        try (MockedStatic<Database> mockDb = mockStatic(Database.class)) {
            mockDb.when(() -> Database.getScoresByStudent("sam"))
                    .thenReturn(List.of(Map.of("game", "Nature", "score", 85, "correct", 8, "incorrect", 3, "avgSpeed", 6.7)));

            setPrivateField("studentUsername", "sam");
            invokePrivate("loadScores");

            TableView<Map<String, Object>> table = (TableView<Map<String, Object>>) getPrivateField("scoreTable");
            ObservableList<Map<String, Object>> items = table.getItems();

            assertNotNull(items, "Table items should not be null");
            assertFalse(items.isEmpty(), "Table should contain score data");
        }
    }

    @Test
    void testHandleBack_noCrash() {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            assertDoesNotThrow(() -> invokePrivate("handleBack"),
                    "handleBack() should execute without throwing");
        }
    }
}
