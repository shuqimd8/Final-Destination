import com.learneria.utils.SceneManager;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SceneManagerTest {

    private Stage mockStage;

    @BeforeAll
    void initJavaFX() throws Exception {
        // Ensure JavaFX platform is initialized
        if (!Platform.isFxApplicationThread()) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            latch.await();
        }
    }

    @BeforeEach
    void setup() {
        mockStage = mock(Stage.class);
        SceneManager.setStage(mockStage);
    }

    @Test
    void setAndGetCurrentUserAndRole_workCorrectly() {
        SceneManager.setCurrentUser("testerUser", "student");
        assertEquals("testerUser", SceneManager.getCurrentUser());
        assertEquals("student", SceneManager.getCurrentRole());
    }

    @Test
    void isStageReady_returnsTrueAfterSetStage() {
        assertTrue(SceneManager.isStageReady());
    }

    @Test
    void goBackToDashboard_teacher_callsCorrectScene() {
        SceneManager.setCurrentUser("teacherUser", "teacher");

        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class, CALLS_REAL_METHODS)) {
            mock.when(() -> SceneManager.switchScene(anyString(), anyString())).then(inv -> null);

            SceneManager.goBackToDashboard();

            mock.verify(() ->
                    SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Main Menu"));
        }
    }

    @Test
    void goBackToDashboard_student_callsCorrectScene() {
        SceneManager.setCurrentUser("studentUser", "student");

        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class, CALLS_REAL_METHODS)) {
            mock.when(() -> SceneManager.switchScene(anyString(), anyString())).then(inv -> null);

            SceneManager.goBackToDashboard();

            mock.verify(() ->
                    SceneManager.switchScene("/com/learneria/fxml/student_main.fxml", "Student Main Menu"));
        }
    }

    @Test
    void goBackToDashboard_unknownRole_goesToLogin() {
        SceneManager.setCurrentUser("anon", "guest");

        try (MockedStatic<SceneManager> mock = mockStatic(SceneManager.class, CALLS_REAL_METHODS)) {
            mock.when(() -> SceneManager.switchScene(anyString(), anyString())).then(inv -> null);

            SceneManager.goBackToDashboard();

            mock.verify(() ->
                    SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login"));
        }
    }

    @Test
    void switchScene_doesNotCrashEvenIfFXMLMissing() {
        // No FXML actually exists; we just ensure it doesn’t throw
        assertDoesNotThrow(() ->
                SceneManager.switchScene("/fake/path.fxml", "Fake Window"));
    }
}
