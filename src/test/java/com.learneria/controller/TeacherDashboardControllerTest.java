package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeacherDashboardControllerTest {

    private TeacherDashboardController controller;
    private Label teacherLabel;
    private ListView<String> listView;

    // Start JavaFX runtime once for all tests
    @BeforeAll
    static void initFX() {
        try {
            Platform.startup(() -> {}); // Start JavaFX platform only once
        } catch (IllegalStateException ignored) {
            // JavaFX already started
        }
    }

    @BeforeEach
    void setup() {
        controller = new TeacherDashboardController();
        teacherLabel = new Label();
        listView = new ListView<>();
        controller.setTestingLabels(teacherLabel, listView);
    }

    @Test
    void testInitializeDisplaysTeacherName() {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class);
             MockedStatic<Database> mockDB = mockStatic(Database.class)) {

            mockScene.when(SceneManager::getCurrentUser).thenReturn("teacher@example.com");
            mockDB.when(() -> Database.getClassesByTeacher(anyString()))
                    .thenReturn(Collections.emptyList());

            Platform.runLater(() -> {
                controller.initialize();
                assertEquals("Teacher", teacherLabel.getText());
            });
        }
    }

    @Test
    void testRefreshClassListAddsItems() {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class);
             MockedStatic<Database> mockDB = mockStatic(Database.class)) {

            mockScene.when(SceneManager::getCurrentUser).thenReturn("teacher@example.com");

            Map<String, String> class1 = Map.of("class_name", "Math", "class_code", "M01");
            Map<String, String> class2 = Map.of("class_name", "Science", "class_code", "S02");

            mockDB.when(() -> Database.getClassesByTeacher(anyString()))
                    .thenReturn(List.of(class1, class2));

            Platform.runLater(() -> {
                controller.initialize();
                assertTrue(listView.getItems().contains("Math (M01)"));
                assertTrue(listView.getItems().contains("Science (S02)"));
            });
        }
    }

    @Test
    void testHandleLogoutTriggersSceneChange() {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            controller.handleLogout();
            mockScene.verify(() -> SceneManager.setCurrentUser(null, null));
            mockScene.verify(() -> SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login"));
        }
    }
}
