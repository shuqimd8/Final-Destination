import com.learneria.controller.TeacherReportsController;
import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

/**
 * Unit tests for TeacherReportsController.
 * Verifies initialize() populates data and handleBack() runs without error.
 */
public class TeacherReportsControllerTest {

    private TeacherReportsController controller;

    @BeforeAll
    static void setupFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {}
    }

    @BeforeEach
    void setup() throws Exception {
        controller = new TeacherReportsController();

        setPrivateField("classTable", new TableView<>());
        setPrivateField("colClassName", new TableColumn<>());
        setPrivateField("colClassCode", new TableColumn<>());
        setPrivateField("colAvgScore", new TableColumn<>());
        setPrivateField("colStudentCount", new TableColumn<>());
        setPrivateField("topStudentsList", new ListView<>());
        setPrivateField("gameStatsList", new ListView<>());
    }

    private void setPrivateField(String name, Object value) throws Exception {
        Field f = TeacherReportsController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }

    private Object getPrivateField(String name) throws Exception {
        Field f = TeacherReportsController.class.getDeclaredField(name);
        f.setAccessible(true);
        return f.get(controller);
    }

    @Test
    void testInitialize_loadsAllSections_noCrash() throws Exception { // ✅ added “throws Exception”
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class);
             MockedStatic<Database> mockDb = mockStatic(Database.class)) {

            mockScene.when(SceneManager::getCurrentUser).thenReturn("teacher01");

            mockDb.when(() -> Database.getClassOverview("teacher01"))
                    .thenReturn(List.of(Map.of(
                            "class_name", "Class A",
                            "class_code", "A101",
                            "avg_score", 85.5,
                            "student_count", 20
                    )));
            mockDb.when(() -> Database.getTopStudents("teacher01"))
                    .thenReturn(List.of(Map.of("username", "Alice", "avg_score", 95.0)));
            mockDb.when(() -> Database.getAverageByGame("teacher01"))
                    .thenReturn(Map.of("Grammar", 88.2, "Food", 91.5));

            assertDoesNotThrow(() -> controller.initialize(), "initialize() should not crash");

            TableView<Map<String, Object>> table =
                    (TableView<Map<String, Object>>) getPrivateField("classTable");
            ObservableList<Map<String, Object>> items = table.getItems();
            assertEquals(1, items.size());
            assertEquals("Class A", items.get(0).get("class_name"));

            ListView<String> topList =
                    (ListView<String>) getPrivateField("topStudentsList");
            assertTrue(topList.getItems().get(0).contains("Alice"));

            ListView<String> gameList =
                    (ListView<String>) getPrivateField("gameStatsList");
            assertTrue(gameList.getItems().stream().anyMatch(s -> s.contains("Grammar")));
        }
    }

    @Test
    void testHandleBack_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            var method = TeacherReportsController.class.getDeclaredMethod("handleBack");
            method.setAccessible(true);
            assertDoesNotThrow(() -> method.invoke(controller),
                    "handleBack() should execute safely");
            mockScene.verify(() ->
                            SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Menu"),
                    times(1));
        }
    }
}
