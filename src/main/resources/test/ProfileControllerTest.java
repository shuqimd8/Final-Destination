import com.learneria.controller.ProfileController;
import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class ProfileControllerTest {

    private ProfileController controller;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @BeforeAll
    static void setupJavaFX() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setup() throws Exception {
        controller = new ProfileController();

        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(anyString())).thenReturn(10);

        Database mockDb = mock(Database.class);
        when(mockDb.getConnection()).thenReturn(mockConnection);
        Field dbField = Database.class.getDeclaredField("instance");
        dbField.setAccessible(true);
        dbField.set(null, mockDb);

        SceneManager.setCurrentUser("TestUser", "student");

        setPrivateField(controller, "usernameLabel", new Label());
        setPrivateField(controller, "totalGamesLabel", new Label());
        setPrivateField(controller, "avgScoreLabel", new Label());
        setPrivateField(controller, "foodHighLabel", new Label());
        setPrivateField(controller, "natureHighLabel", new Label());
        setPrivateField(controller, "grammarHighLabel", new Label());
    }

    private void setPrivateField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    private void invokePrivateMethod(Object target, String name) throws Exception {
        Method m = target.getClass().getDeclaredMethod(name);
        m.setAccessible(true);
        m.invoke(target);
    }

    @Test
    void testInitialize_noCrash() {
        assertDoesNotThrow(() -> controller.initialize(), "initialize() should not crash");
    }

    @Test
    void testHandleBack_noCrash() {
        assertDoesNotThrow(() -> {
            invokePrivateMethod(controller, "handleBack");
        }, "handleBack() should not throw");
    }

    @Test
    void testLoadStats_executesSQL() throws Exception {
        invokePrivateMethod(controller, "initialize");
        verify(mockConnection, atLeastOnce()).prepareStatement(anyString());
        verify(mockStatement, atLeastOnce()).executeQuery();
    }
}
