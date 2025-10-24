import com.learneria.controller.NatureGameController;
import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NatureGameControllerTest {

    private NatureGameController controller;
    private MockedStatic<SceneManager> sceneManagerMock;
    private MockedStatic<Database> databaseMock;

    @BeforeAll
    void setupJavaFX() throws Exception {
        if (!Platform.isFxApplicationThread()) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            latch.await();
        }
    }

    @BeforeEach
    void setUp() {
        controller = new NatureGameController();

        // Mock FXML fields
        controller.word1 = new Label();
        controller.bucketAnimal = new ImageView();
        controller.bucketPlant = new ImageView();
        controller.bucketNonLiving = new ImageView();
        controller.bucketWeather = new ImageView();
        controller.feedbackLabel = new Label();
        controller.scoreLabel = new Label();
        controller.timerLabel = new Label();
        controller.highScoreLabel = new Label();
        controller.usernameLabel = new Label();

        sceneManagerMock = mockStatic(SceneManager.class);
        databaseMock = mockStatic(Database.class);
    }

    @AfterEach
    void tearDown() {
        sceneManagerMock.close();
        databaseMock.close();
    }

    @Test
    void initialize_setsUser_highScore_loadsWord() {
        sceneManagerMock.when(SceneManager::getCurrentUser).thenReturn("Tester");
        databaseMock.when(() -> Database.getHighScore("Tester", "Nature")).thenReturn(50);

        for (String cat : List.of("Animal", "Plant", "Non-Living", "Weather")) {
            databaseMock.when(() -> Database.getAllWords(cat))
                    .thenReturn(List.of(cat + "_Word1", cat + "_Word2"));
        }

        controller.initialize();

        assertEquals("Tester", controller.usernameLabel.getText());
        assertEquals("50", controller.highScoreLabel.getText());
        assertEquals("0", controller.scoreLabel.getText());
        assertTrue(controller.feedbackLabel.getText().contains("Drag"));
        assertNotNull(controller.word1.getText());
    }

    @Test
    void handleBack_stopsTimer_andGoesBack() throws Exception {
        // Mock timer via reflection
        AnimationTimer mockTimer = mock(AnimationTimer.class);
        Field timerField = NatureGameController.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        timerField.set(controller, mockTimer);

        sceneManagerMock.when(SceneManager::goBackToDashboard).then(invocation -> null);

        assertDoesNotThrow(() -> controller.handleBack());

        verify(mockTimer, atLeastOnce()).stop();
        sceneManagerMock.verify(SceneManager::goBackToDashboard);
    }

    @Test
    void setupWordPools_usesDatabaseAndFallback() {
        databaseMock.when(() -> Database.getAllWords("Animal")).thenReturn(List.of());
        databaseMock.when(() -> Database.getAllWords("Plant")).thenReturn(List.of("Tree"));
        databaseMock.when(() -> Database.getAllWords("Non-Living")).thenReturn(List.of());
        databaseMock.when(() -> Database.getAllWords("Weather")).thenReturn(List.of());

        assertDoesNotThrow(controller::initialize);
        assertFalse(controller.word1.getText().isEmpty());
    }
}
