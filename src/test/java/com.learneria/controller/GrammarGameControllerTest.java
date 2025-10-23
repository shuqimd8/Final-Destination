import com.learneria.controller.GrammarGameController;
import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GrammarGameControllerTest {

    private GrammarGameController controller;
    private MockedStatic<SceneManager> sceneManagerMock;
    private MockedStatic<Database> databaseMock;

    @BeforeAll
    void initJavaFX() throws Exception {
        if (!Platform.isFxApplicationThread()) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            latch.await();
        }
    }

    @BeforeEach
    void setUp() {
        controller = new GrammarGameController();
        controller.word1 = new Label();
        controller.bucketNoun = new ImageView();
        controller.bucketVerb = new ImageView();
        controller.bucketAdjective = new ImageView();
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
    void initialize_setsUsername_highScore_andLoadsWord() {
        sceneManagerMock.when(SceneManager::getCurrentUser).thenReturn("Tester");
        databaseMock.when(() -> Database.getHighScore("Tester", "Grammar")).thenReturn(90);

        // Return mutable lists (fixes UnsupportedOperationException)
        for (String cat : List.of("Noun", "Verb", "Adjective")) {
            databaseMock.when(() -> Database.getAllWords(cat))
                    .thenReturn(new ArrayList<>(List.of(cat + "Word1", cat + "Word2")));
        }

        controller.initialize();

        assertEquals("Tester", controller.usernameLabel.getText());
        assertEquals("90", controller.highScoreLabel.getText());
        assertEquals("0", controller.scoreLabel.getText());
        assertNotNull(controller.word1.getText());
    }

    @Test
    void setupWordPools_fallsBackToDBResults() {
        databaseMock.when(() -> Database.getAllWords("Noun"))
                .thenReturn(new ArrayList<>(List.of("Cat", "Dog")));
        databaseMock.when(() -> Database.getAllWords("Verb"))
                .thenReturn(new ArrayList<>(List.of("Run", "Jump")));
        databaseMock.when(() -> Database.getAllWords("Adjective"))
                .thenReturn(new ArrayList<>(List.of("Happy", "Sad")));

        sceneManagerMock.when(SceneManager::getCurrentUser).thenReturn("Tester");
        databaseMock.when(() -> Database.getHighScore("Tester", "Grammar")).thenReturn(0);

        assertDoesNotThrow(controller::initialize);
        assertFalse(controller.word1.getText().isEmpty());
    }

    @Test
    void handleBack_stopsTimer_andReturnsToDashboard() throws Exception {
        AnimationTimer mockTimer = mock(AnimationTimer.class);
        Field timerField = GrammarGameController.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        timerField.set(controller, mockTimer);

        sceneManagerMock.when(SceneManager::goBackToDashboard).then(invocation -> null);

        controller.handleBack();

        verify(mockTimer, atLeastOnce()).stop();
        sceneManagerMock.verify(SceneManager::goBackToDashboard);
    }
}
