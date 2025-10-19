import com.learneria.controller.TeacherDashboardController;
import com.learneria.utils.SceneManager;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mockStatic;

class TeacherDashboardControllerTest {

    public TeacherDashboardController controller;

    @BeforeEach
    void setUp() {
        controller = new TeacherDashboardController();
        controller.classListView = new ListView<>();
        controller.teacherNameLabel = new Label();
    }

    @Test
    void testInitialize_setsTeacherName() {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            mockScene.when(SceneManager::getCurrentUser).thenReturn("teacher01");
            controller.initialize();

            assertEquals("Teacher01", controller.teacherNameLabel.getText(),
                    "Teacher name should display with capital first letter");
        }
    }

    @Test
    void testInitialize_nullUser_showsUnknown() {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            mockScene.when(SceneManager::getCurrentUser).thenReturn(null);
            controller.initialize();
            assertEquals("Unknown", controller.teacherNameLabel.getText(),
                    "Should show 'Unknown' when username is null");
        }
    }

    @Test
    void testHandleLogout_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            mockScene.when(() -> SceneManager.switchScene(anyString(), anyString())).thenAnswer(invocation -> null);
            mockScene.when(() -> SceneManager.setCurrentUser(null, null)).thenAnswer(invocation -> null);

            var method = TeacherDashboardController.class.getDeclaredMethod("handleLogout");
            method.setAccessible(true);

            assertDoesNotThrow(() -> method.invoke(controller),
                    "handleLogout() should not throw any exception");
        }
    }

    @Test
    void testOpenSettings_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            mockScene.when(() -> SceneManager.switchScene(anyString(), anyString())).thenAnswer(invocation -> null);

            var method = TeacherDashboardController.class.getDeclaredMethod("openSettings");
            method.setAccessible(true);

            assertDoesNotThrow(() -> method.invoke(controller),
                    "openSettings() should not throw any exception");
        }
    }

    @Test
    void testHandleBack_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            mockScene.when(() -> SceneManager.switchScene(anyString(), anyString())).thenAnswer(invocation -> null);

            var method = TeacherDashboardController.class.getDeclaredMethod("handleBack");
            method.setAccessible(true);

            assertDoesNotThrow(() -> method.invoke(controller),
                    "handleBack() should not throw any exception");
        }
    }
}
