import com.learneria.controller.SettingsController;
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

/**
 * Unit tests for SettingsController.
 * Verifies loading, updating, and navigation work safely.
 */
public class SettingsControllerTest {

    private SettingsController controller;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @BeforeAll
    static void setupFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {}
    }

    @BeforeEach
    void setup() throws Exception {
        controller = new SettingsController();

        // mock database objects
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(anyString())).thenReturn("dummy");

        Database mockDb = mock(Database.class);
        when(mockDb.getConnection()).thenReturn(mockConnection);
        Field dbField = Database.class.getDeclaredField("instance");
        dbField.setAccessible(true);
        dbField.set(null, mockDb);

        SceneManager.setCurrentUser("tester", "student");

        // inject labels
        setPrivateField("nameLabel", new Label());
        setPrivateField("usernameLabel", new Label());
        setPrivateField("passwordLabel", new Label());
    }

    private void setPrivateField(String field, Object value) throws Exception {
        Field f = SettingsController.class.getDeclaredField(field);
        f.setAccessible(true);
        f.set(controller, value);
    }

    private void invokePrivate(String method) throws Exception {
        Method m = SettingsController.class.getDeclaredMethod(method);
        m.setAccessible(true);
        m.invoke(controller);
    }

    @Test
    void testLoadUserData_noCrash() {
        assertDoesNotThrow(() -> {
            Method m = SettingsController.class.getDeclaredMethod("loadUserData");
            m.setAccessible(true);
            m.invoke(controller);
        }, "loadUserData() should execute without exceptions");
    }

    @Test
    void testHandleBack_noCrash() {
        assertDoesNotThrow(() -> invokePrivate("handleBack"),
                "handleBack() should not crash");
    }

    @Test
    void testUpdateDatabaseField_noCrash() {
        assertDoesNotThrow(() -> {
            Method m = SettingsController.class.getDeclaredMethod(
                    "updateDatabaseField", String.class, String.class);
            m.setAccessible(true);
            m.invoke(controller, "name", "NewName");
        }, "updateDatabaseField() should not crash");
    }

    @Test
    void testDatabaseInteractionOccurs() throws Exception {
        Method m = SettingsController.class.getDeclaredMethod(
                "updateDatabaseField", String.class, String.class);
        m.setAccessible(true);
        m.invoke(controller, "password", "abc123");

        verify(mockConnection, atLeastOnce()).prepareStatement(anyString());
        verify(mockStatement, atLeastOnce()).executeUpdate();
    }
}
