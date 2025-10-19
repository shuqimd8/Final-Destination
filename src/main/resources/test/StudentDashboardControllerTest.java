import com.learneria.controller.StudentDashboardController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for StudentDashboardController.
 * Verifies username display and logout safety.
 */
public class StudentDashboardControllerTest {

    private StudentDashboardController controller;

    @BeforeAll
    static void setupFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {}
    }

    @BeforeEach
    void setup() throws Exception {
        controller = new StudentDashboardController();

        // Inject welcome label
        Field labelField = StudentDashboardController.class.getDeclaredField("welcomeLabel");
        labelField.setAccessible(true);
        labelField.set(controller, new Label());
    }

    @Test
    void testSetUsername_updatesLabel() {
        controller.setUsername("Alice");
        assertEquals("Welcome, Alice!", ((Label) getPrivateField("welcomeLabel")).getText(),
                "Label text should update when username is set");
    }

    @Test
    void testHandleLogout_noCrash() {
        assertDoesNotThrow(() -> invokePrivate("handleLogout"),
                "handleLogout() should execute without exception");
    }

    /** Helper: reflectively invoke private methods */
    private void invokePrivate(String methodName) throws Exception {
        Method m = StudentDashboardController.class.getDeclaredMethod(methodName);
        m.setAccessible(true);
        m.invoke(controller);
    }

    /** Helper: reflectively access private fields */
    private Object getPrivateField(String field) {
        try {
            Field f = StudentDashboardController.class.getDeclaredField(field);
            f.setAccessible(true);
            return f.get(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
