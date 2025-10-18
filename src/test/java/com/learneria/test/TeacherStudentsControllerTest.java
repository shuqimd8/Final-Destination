package com.learneria.test;

import com.learneria.controller.TeacherStudentsController;
import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
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
 * Unit tests for TeacherStudentsController.
 * Verifies initialize(), loadClasses(), showClassStudents(),
 * handleBack(), and handleLogout() behave safely.
 */
public class TeacherStudentsControllerTest {

    private TeacherStudentsController controller;

    @BeforeAll
    static void setupFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {}
    }

    @BeforeEach
    void setup() throws Exception {
        controller = new TeacherStudentsController();

        // Inject mock UI components
        setPrivateField("teacherNameLabel", new Label());
        setPrivateField("classDropdown", new ComboBox<>());
        setPrivateField("studentList", new ListView<>());
        setPrivateField("classCodeLabel", new Label());
        setPrivateField("avgScoreLabel", new Label());
        setPrivateField("totalStudentsLabel", new Label());
    }

    private void setPrivateField(String name, Object value) throws Exception {
        Field f = TeacherStudentsController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }

    private Object getPrivateField(String name) throws Exception {
        Field f = TeacherStudentsController.class.getDeclaredField(name);
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

            mockScene.when(SceneManager::getCurrentUser).thenReturn("teacherA");
            mockDb.when(() -> Database.getClassesByTeacher("teacherA"))
                    .thenReturn(List.of(Map.of("class_name", "Biology", "class_code", "BIO101")));

            assertDoesNotThrow(() -> controller.initialize(), "initialize() should not crash");

            ComboBox<String> combo = (ComboBox<String>) getPrivateField("classDropdown");
            ObservableList<String> items = combo.getItems();
            assertEquals(1, items.size());
            assertTrue(items.get(0).contains("BIO101"));

            Label teacherLabel = (Label) getPrivateField("teacherNameLabel");
            assertTrue(teacherLabel.getText().contains("teacherA"));
        }
    }

    @Test
    void testShowClassStudents_populatesLists() throws Exception {
        try (MockedStatic<Database> mockDb = mockStatic(Database.class)) {
            mockDb.when(() -> Database.getStudentsByClass("BIO101"))
                    .thenReturn(List.of("Alice", "Bob"));
            mockDb.when(() -> Database.getAverageClassScore("BIO101"))
                    .thenReturn(87.5);

            var method = TeacherStudentsController.class
                    .getDeclaredMethod("showClassStudents", String.class);
            method.setAccessible(true);

            assertDoesNotThrow(() -> method.invoke(controller, "BIO101"));

            ListView<String> list = (ListView<String>) getPrivateField("studentList");
            assertEquals(2, list.getItems().size());
            assertTrue(list.getItems().contains("Alice"));

            Label avgScoreLabel = (Label) getPrivateField("avgScoreLabel");
            assertTrue(avgScoreLabel.getText().contains("87.5"));
        }
    }

    @Test
    void testHandleBack_and_Logout_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {

            // HandleBack
            var backMethod = TeacherStudentsController.class
                    .getDeclaredMethod("handleBack");
            backMethod.setAccessible(true);
            assertDoesNotThrow(() -> backMethod.invoke(controller));
            mockScene.verify(() ->
                            SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Menu"),
                    times(1));

            // HandleLogout
            var logoutMethod = TeacherStudentsController.class
                    .getDeclaredMethod("handleLogout");
            logoutMethod.setAccessible(true);
            assertDoesNotThrow(() -> logoutMethod.invoke(controller));
            mockScene.verify(() ->
                            SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login"),
                    times(1));
        }
    }
}
