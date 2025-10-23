package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel; // initializes JavaFX Toolkit
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TeacherDashboardController using JUnit5 + Mockito.
 * No actual UI or DB is created; JavaFX toolkit runs headless.
 */
class TeacherDashboardControllerTest {

    private TeacherDashboardController controller;
    private Label teacherLabel;
    private ListView<String> classListView;

    private static MockedStatic<SceneManager> sceneMock;
    private static MockedStatic<Database> dbMock;

    @BeforeAll
    static void setupStatics() {
        // Mock static methods from SceneManager and Database
        sceneMock = Mockito.mockStatic(SceneManager.class);
        dbMock = Mockito.mockStatic(Database.class);
    }

    @AfterAll
    static void closeStatics() {
        sceneMock.close();
        dbMock.close();
    }

    @BeforeEach
    void setUp() {
        // ✅ initialize JavaFX runtime so Label/ListView don’t crash
        if (!Platform.isFxApplicationThread()) {
            new JFXPanel();
        }

        controller = new TeacherDashboardController();
        teacherLabel = new Label();
        classListView = new ListView<>();

        controller.setTestingLabels(teacherLabel, classListView);
        sceneMock.when(SceneManager::getCurrentUser).thenReturn("teacherA");
    }

    // ------------------------------------------------------------------
    // TESTS
    // ------------------------------------------------------------------

    @Test
    @DisplayName("initialize() sets teacher name and loads classes")
    void testInitializeSetsNameAndLoadsClasses() {
        List<Map<String, String>> fakeClasses = List.of(
                Map.of("class_name", "Math", "class_code", "C101"),
                Map.of("class_name", "Science", "class_code", "C102")
        );
        dbMock.when(() -> Database.getClassesByTeacher("teacherA")).thenReturn(fakeClasses);

        controller.initialize();

        assertEquals("TeacherA", teacherLabel.getText());
        assertEquals(2, classListView.getItems().size());
        assertTrue(classListView.getItems().get(0).contains("Math"));
    }

    @Test
    @DisplayName("openSettings() switches to Settings scene")
    void testOpenSettings() {
        sceneMock.when(() ->
                        SceneManager.switchScene("/com/learneria/fxml/settings.fxml", "Settings"))
                .then(inv -> null);

        controller.openSettings();

        sceneMock.verify(() ->
                SceneManager.switchScene("/com/learneria/fxml/settings.fxml", "Settings"));
    }

    @Test
    @DisplayName("handleLogout() clears user and switches scene")
    void testHandleLogout() {
        sceneMock.when(() -> SceneManager.setCurrentUser(null, null)).then(inv -> null);
        sceneMock.when(() ->
                        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login"))
                .then(inv -> null);

        controller.handleLogout();

        sceneMock.verify(() -> SceneManager.setCurrentUser(null, null));
        sceneMock.verify(() ->
                SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login"));
    }

    @Test
    @DisplayName("handleBack() switches to teacher main menu")
    void testHandleBack() {
        sceneMock.when(() ->
                        SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Main Menu"))
                .then(inv -> null);

        controller.handleBack();

        sceneMock.verify(() ->
                SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Main Menu"));
    }

    @Test
    @DisplayName("refreshClassList() updates ListView with classes")
    void testRefreshClassList() {
        List<Map<String, String>> fake = List.of(
                Map.of("class_name", "English", "class_code", "C200")
        );
        dbMock.when(() -> Database.getClassesByTeacher("teacherA")).thenReturn(fake);

        // initialize() calls refreshClassList internally
        controller.initialize();

        assertEquals(1, classListView.getItems().size());
        assertTrue(classListView.getItems().get(0).contains("English"));
    }
}
