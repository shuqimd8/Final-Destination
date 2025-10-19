import com.learneria.controller.TeacherClassListController;
import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TeacherClassListController.
 * Verifies initialize(), loadClasses(), showClass(), and navigation methods.
 */
public class TeacherClassListControllerTest {

    private TeacherClassListController controller;

    @BeforeAll
    static void setupFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {}
    }

    @BeforeEach
    void setup() throws Exception {
        controller = new TeacherClassListController();

        setPrivateField("teacherNameLabel", new Label());
        setPrivateField("classListView", new ListView<>());
        setPrivateField("classNameLabel", new Label());
        setPrivateField("classCodeLabel", new Label());
        setPrivateField("studentCountLabel", new Label());
        setPrivateField("avgScoreLabel", new Label());
        setPrivateField("studentListView", new ListView<>());
        setPrivateField("viewStudentButton", new Button());
    }

    private void setPrivateField(String name, Object value) throws Exception {
        Field f = TeacherClassListController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }

    private Object getPrivateField(String name) throws Exception {
        Field f = TeacherClassListController.class.getDeclaredField(name);
        f.setAccessible(true);
        return f.get(controller);
    }

    // -------------------------------------------------------
    // TESTS
    // -------------------------------------------------------

    @Test
    void testInitialize_loadsClasses_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class);
             MockedStatic<Database> mockDb = mockStatic(Database.class)) {

            mockScene.when(SceneManager::getCurrentUser).thenReturn("teacherZ");
            mockDb.when(() -> Database.getClassesByTeacher("teacherZ"))
                    .thenReturn(List.of(Map.of("class_name", "Science", "class_code", "SCI101")));

            assertDoesNotThrow(() -> controller.initialize(), "initialize() should not throw");

            Label teacherLabel = (Label) getPrivateField("teacherNameLabel");
            assertTrue(teacherLabel.getText().contains("teacherZ"));

            ListView<String> classList = (ListView<String>) getPrivateField("classListView");
            ObservableList<String> items = classList.getItems();
            assertEquals(1, items.size());
            assertTrue(items.get(0).contains("SCI101"));
        }
    }

    @Test
    void testShowClass_updatesLabelsAndStudents() throws Exception {
        try (MockedStatic<Database> mockDb = mockStatic(Database.class)) {
            mockDb.when(() -> Database.getStudentsByClass("SCI101"))
                    .thenReturn(List.of("Alice", "Bob", "Charlie"));
            mockDb.when(() -> Database.getAverageClassScore("SCI101"))
                    .thenReturn(88.5);

            Field mapField = TeacherClassListController.class.getDeclaredField("classTitleToCode");
            mapField.setAccessible(true);
            ((Map<String, String>) mapField.get(controller)).put("Science (SCI101)", "SCI101");

            var method = TeacherClassListController.class.getDeclaredMethod("showClass", String.class);
            method.setAccessible(true);
            assertDoesNotThrow(() -> method.invoke(controller, "Science (SCI101)"));

            Label codeLabel = (Label) getPrivateField("classCodeLabel");
            Label countLabel = (Label) getPrivateField("studentCountLabel");
            Label avgLabel = (Label) getPrivateField("avgScoreLabel");

            assertEquals("SCI101", codeLabel.getText());
            assertEquals("3", countLabel.getText());
            assertTrue(avgLabel.getText().contains("88.5"));
        }
    }

    @Test
    void testOpenSelectedStudent_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            ListView<String> studentList = (ListView<String>) getPrivateField("studentListView");
            studentList.getItems().add("Alice");
            studentList.getSelectionModel().select("Alice");

            var method = TeacherClassListController.class.getDeclaredMethod("openSelectedStudent");
            method.setAccessible(true);
            assertDoesNotThrow(() -> method.invoke(controller));

            mockScene.verify(() ->
                    SceneManager.switchSceneWithController(
                            eq("/com/learneria/fxml/student_performance.fxml"),
                            eq("Student Performance"),
                            any()), times(1));
        }
    }

    @Test
    void testHandleBackAndLogout_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            var backMethod = TeacherClassListController.class.getDeclaredMethod("handleBack");
            backMethod.setAccessible(true);
            assertDoesNotThrow(() -> backMethod.invoke(controller));
            mockScene.verify(() ->
                            SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Main Menu"),
                    times(1));

            var logoutMethod = TeacherClassListController.class.getDeclaredMethod("handleLogout");
            logoutMethod.setAccessible(true);
            assertDoesNotThrow(() -> logoutMethod.invoke(controller));
            mockScene.verify(() -> SceneManager.setCurrentUser(null, null), times(1));
            mockScene.verify(() ->
                    SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login"), times(1));
        }
    }
}
