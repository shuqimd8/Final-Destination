package com.learneria.test;

import com.learneria.controller.TeacherMainController;
import com.learneria.utils.SceneManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TeacherMainController.
 * Verifies all navigation buttons trigger SceneManager correctly.
 */
public class TeacherMainControllerTest {

    private TeacherMainController controller;

    @BeforeEach
    void setup() {
        controller = new TeacherMainController();
    }

    private void invokePrivate(String name) throws Exception {
        Method m = TeacherMainController.class.getDeclaredMethod(name);
        m.setAccessible(true);
        m.invoke(controller);
    }

    @Test
    void testOpenDashboard_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class)) {
            assertDoesNotThrow(() -> invokePrivate("openDashboard"));
            mock.verify(() -> SceneManager.switchScene(
                    "/com/learneria/fxml/teacher_dashboard.fxml", "Teacher Dashboard"), times(1));
        }
    }

    @Test
    void testOpenClassList_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class)) {
            assertDoesNotThrow(() -> invokePrivate("openClassList"));
            mock.verify(() -> SceneManager.switchScene(
                    "/com/learneria/fxml/teacher_class_list.fxml", "Manage Classes"), times(1));
        }
    }

    @Test
    void testOpenReports_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class)) {
            assertDoesNotThrow(() -> invokePrivate("openReports"));
            mock.verify(() -> SceneManager.switchScene(
                    "/com/learneria/fxml/teacher_reports.fxml", "Reports"), times(1));
        }
    }

    @Test
    void testOpenSettings_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class)) {
            assertDoesNotThrow(() -> invokePrivate("openSettings"));
            mock.verify(() -> SceneManager.switchScene(
                    "/com/learneria/fxml/settings.fxml", "Settings"), times(1));
        }
    }

    @Test
    void testHandleLogout_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class)) {
            assertDoesNotThrow(() -> invokePrivate("handleLogout"));
            mock.verify(() -> SceneManager.setCurrentUser(null, null), times(1));
            mock.verify(() -> SceneManager.switchScene(
                    "/com/learneria/fxml/login.fxml", "Login"), times(1));
        }
    }
}
