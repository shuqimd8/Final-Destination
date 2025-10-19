import com.learneria.controller.CreateAccountSelectController;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateAccountSelectControllerTest {

    private CreateAccountSelectController controller;

    @BeforeAll
    static void setupJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
            // Already started
        }
    }

    @BeforeEach
    void setup() {
        controller = new CreateAccountSelectController();
    }

    @Test
    void testControllerExists() {
        assertNotNull(controller, "Controller should be created");
    }

    @Test
    void testHandleStudent_noCrash() {
        assertDoesNotThrow(() -> controller.handleStudent(),
                "Should not crash when switching to student creation scene");
    }

    @Test
    void testHandleTeacher_noCrash() {
        assertDoesNotThrow(() -> controller.handleTeacher(),
                "Should not crash when switching to teacher creation scene");
    }

    @Test
    void testHandleBack_noCrash() {
        assertDoesNotThrow(() -> controller.handleBack(null),
                "Should not crash when returning to login scene");
    }
}
