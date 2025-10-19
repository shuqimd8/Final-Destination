import com.learneria.controller.LoginController;
import javafx.application.Platform;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginControllerTest {

    private LoginController controller;

    @BeforeAll
    static void initJavaFX() {
        try {
            // Initialize JavaFX runtime (needed for UI components in tests)
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Ignore if JavaFX toolkit already started
        }
    }

    @BeforeEach
    void setUp() {
        controller = new LoginController();

        // Create dummy JavaFX UI elements
        controller.usernameField = new TextField();
        controller.passwordField = new PasswordField();
    }

    @Test
    void testControllerExists() {
        assertNotNull(controller, "LoginController should be initialized");
    }

    @Test
    void testEmptyUsernamePassword() {
        controller.usernameField.setText("");
        controller.passwordField.setText("");

        assertDoesNotThrow(() -> controller.handleLogin(),
                "Should not crash when username/password fields are empty");
    }

    @Test
    void testInvalidCredentials() {
        controller.usernameField.setText("wronguser");
        controller.passwordField.setText("wrongpass");

        assertDoesNotThrow(() -> controller.handleLogin(),
                "Should not crash when invalid credentials are entered");
    }
}
