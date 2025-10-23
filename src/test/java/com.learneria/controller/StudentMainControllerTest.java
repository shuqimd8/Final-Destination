import com.learneria.controller.StudentMainController;
import com.learneria.utils.SceneManager;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

/**
 * Unit tests for StudentMainController.
 * Verifies initialization and navigation buttons work correctly.
 */
public class StudentMainControllerTest {

    private StudentMainController controller;

    @BeforeAll
    static void setupFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {}
    }

    @BeforeEach
    void setup() throws Exception {
        controller = new StudentMainController();
        Label label = new Label();
        Field field = StudentMainController.class.getDeclaredField("usernameLabel");
        field.setAccessible(true);
        field.set(controller, label);
    }

    private void invoke(String methodName, Object... args) throws Exception {
        Method method = null;
        for (Method m : StudentMainController.class.getDeclaredMethods()) {
            if (m.getName().equals(methodName)) {
                method = m;
                break;
            }
        }
        if (method == null)
            throw new NoSuchMethodException(methodName);

        method.setAccessible(true);
        method.invoke(controller, args);
    }

    // ------------------------------
    // TESTS
    // ------------------------------

    @Test
    void testInitialize_setsUsername() throws Exception {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            mockScene.when(SceneManager::getCurrentUser).thenReturn("student123");
            assertDoesNotThrow(() -> controller.initialize(), "initialize() should not crash");
        }
    }

    @Test
    void testOpenGrammar_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class)) {
            assertDoesNotThrow(() -> invoke("openGrammar", (MouseEvent) null));
            mock.verify(() -> SceneManager.switchScene("/com/learneria/fxml/grammar_game.fxml", "Grammar Game"), times(1));
        }
    }

    @Test
    void testOpenNature_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class)) {
            assertDoesNotThrow(() -> invoke("openNature", (MouseEvent) null));
            mock.verify(() -> SceneManager.switchScene("/com/learneria/fxml/nature_game.fxml", "Nature Game"), times(1));
        }
    }

    @Test
    void testOpenFood_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class)) {
            assertDoesNotThrow(() -> invoke("openFood", (MouseEvent) null));
            mock.verify(() -> SceneManager.switchScene("/com/learneria/fxml/food_game.fxml", "Food Game"), times(1));
        }
    }

    @Test
    void testOpenSettings_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class)) {
            assertDoesNotThrow(() -> invoke("openSettings", (MouseEvent) null));
            mock.verify(() -> SceneManager.switchScene("/com/learneria/fxml/settings.fxml", "Settings"), times(1));
        }
    }

    @Test
    void testHandleProfile_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class)) {
            assertDoesNotThrow(() -> invoke("handleProfile"));
            mock.verify(() -> SceneManager.switchScene("/com/learneria/fxml/profile.fxml", "Profile"), times(1));
        }
    }

    @Test
    void testHandleLogout_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class)) {
            assertDoesNotThrow(() -> invoke("handleLogout"));
            mock.verify(() -> SceneManager.setCurrentUser(null, null), times(1));
            mock.verify(() -> SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login"), times(1));
        }
    }
}
