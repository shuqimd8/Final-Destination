import com.learneria.controller.ProfileController;
import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileControllerTest {

    private ProfileController controller;
    private MockedStatic<SceneManager> sceneManagerMock;
    private MockedStatic<Database> databaseMock;

    private Database dbInstanceMock;
    private Connection connMock;

    @BeforeAll
    void initJavaFX() throws Exception {
        if (!Platform.isFxApplicationThread()) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            latch.await();
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        sceneManagerMock = mockStatic(SceneManager.class);
        databaseMock = mockStatic(Database.class);

        controller = new ProfileController();
        controller.usernameLabel = new Label();
        controller.totalGamesLabel = new Label();
        controller.avgScoreLabel = new Label();
        controller.foodHighLabel = new Label();
        controller.natureHighLabel = new Label();
        controller.grammarHighLabel = new Label();

        dbInstanceMock = mock(Database.class);
        connMock = mock(Connection.class);
        when(dbInstanceMock.getConnection()).thenReturn(connMock);
        databaseMock.when(Database::getInstance).thenReturn(dbInstanceMock);
    }

    @AfterEach
    void tearDown() {
        sceneManagerMock.close();
        databaseMock.close();
    }

    @Test
    void initialize_setsUsernameAndLoadsStats() throws Exception {
        // Mock SceneManager username
        sceneManagerMock.when(SceneManager::getCurrentUser).thenReturn("Tester");

        // ===== Mock total games =====
        PreparedStatement stmt1 = mock(PreparedStatement.class);
        ResultSet rs1 = mock(ResultSet.class);
        when(connMock.prepareStatement("SELECT COUNT(*) AS total FROM scores WHERE username = ?"))
                .thenReturn(stmt1);
        when(stmt1.executeQuery()).thenReturn(rs1);
        when(rs1.next()).thenReturn(true);
        when(rs1.getInt("total")).thenReturn(5);

        // ===== Mock average score =====
        PreparedStatement stmt2 = mock(PreparedStatement.class);
        ResultSet rs2 = mock(ResultSet.class);
        when(connMock.prepareStatement("SELECT AVG(score) AS avgScore FROM scores WHERE username = ?"))
                .thenReturn(stmt2);
        when(stmt2.executeQuery()).thenReturn(rs2);
        when(rs2.next()).thenReturn(true);
        when(rs2.getInt("avgScore")).thenReturn(70);

        // ===== Mock high scores (sequence for Food, Nature, Grammar) =====
        PreparedStatement stmtHigh1 = mock(PreparedStatement.class);
        PreparedStatement stmtHigh2 = mock(PreparedStatement.class);
        PreparedStatement stmtHigh3 = mock(PreparedStatement.class);

        ResultSet rsHigh1 = mock(ResultSet.class);
        ResultSet rsHigh2 = mock(ResultSet.class);
        ResultSet rsHigh3 = mock(ResultSet.class);

        when(connMock.prepareStatement("SELECT MAX(score) AS high FROM scores WHERE username = ? AND game = ?"))
                .thenReturn(stmtHigh1, stmtHigh2, stmtHigh3);

        when(stmtHigh1.executeQuery()).thenReturn(rsHigh1);
        when(stmtHigh2.executeQuery()).thenReturn(rsHigh2);
        when(stmtHigh3.executeQuery()).thenReturn(rsHigh3);

        when(rsHigh1.next()).thenReturn(true);
        when(rsHigh2.next()).thenReturn(true);
        when(rsHigh3.next()).thenReturn(true);

        when(rsHigh1.getInt("high")).thenReturn(90); // Food
        when(rsHigh2.getInt("high")).thenReturn(75); // Nature
        when(rsHigh3.getInt("high")).thenReturn(88); // Grammar

        // ===== Run controller =====
        controller.initialize();

        // ===== Assertions =====
        assertEquals("👤 Tester", controller.usernameLabel.getText());
        assertEquals("5", controller.totalGamesLabel.getText());
        assertEquals("70", controller.avgScoreLabel.getText());
        assertEquals("90", controller.foodHighLabel.getText());
        assertEquals("75", controller.natureHighLabel.getText());
        assertEquals("88", controller.grammarHighLabel.getText());
    }

    @Test
    void handleBack_triggersSceneChange() {
        sceneManagerMock.when(SceneManager::goBackToDashboard).then(invocation -> null);
        assertDoesNotThrow(() -> controller.handleBack());
        sceneManagerMock.verify(SceneManager::goBackToDashboard);
    }
}
