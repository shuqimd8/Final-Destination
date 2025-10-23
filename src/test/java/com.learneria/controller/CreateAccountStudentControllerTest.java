import com.learneria.controller.CreateAccountStudentController;
import javafx.application.Platform;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountStudentControllerTest {

    private CreateAccountStudentController controller;

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
        controller = new CreateAccountStudentController();
        setPrivateField("nameField", new TextField());
        setPrivateField("usernameField", new TextField());
        setPrivateField("passwordField", new PasswordField());
        setPrivateField("retypePasswordField", new PasswordField());
        setPrivateField("teacherCodeField", new TextField());
    }

    // Helper method to access private fields
    private void setPrivateField(String fieldName, Object value) {
        try {
            Field field = CreateAccountStudentController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(controller, value);
        } catch (Exception e) {
            fail("Failed to set field: " + fieldName);
        }
    }

    // Helper method to call private methods
    private void invokePrivateMethod(String methodName) {
        try {
            Method method = CreateAccountStudentController.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testControllerExists() {
        assertNotNull(controller, "Controller should be created");
    }

    @Test
    void testEmptyFields_noCrash() {
        assertDoesNotThrow(() -> invokePrivateMethod("handleSubmit"),
                "Should not crash when required fields are empty");
    }

    @Test
    void testMismatchedPasswords_noCrash() {
        ((TextField) getPrivateField("nameField")).setText("Alice");
        ((TextField) getPrivateField("usernameField")).setText("student1");
        ((PasswordField) getPrivateField("passwordField")).setText("12345");
        ((PasswordField) getPrivateField("retypePasswordField")).setText("54321");
        ((TextField) getPrivateField("teacherCodeField")).setText("T123");

        assertDoesNotThrow(() -> invokePrivateMethod("handleSubmit"),
                "Should not crash when passwords do not match");
    }

    @Test
    void testValidInput_noCrash() {
        ((TextField) getPrivateField("nameField")).setText("Bob");
        ((TextField) getPrivateField("usernameField")).setText("student2");
        ((PasswordField) getPrivateField("passwordField")).setText("pass123");
        ((PasswordField) getPrivateField("retypePasswordField")).setText("pass123");
        ((TextField) getPrivateField("teacherCodeField")).setText("ABC123");

        assertDoesNotThrow(() -> invokePrivateMethod("handleSubmit"),
                "Should not crash with valid input");
    }

    @Test
    void testBackNavigation_noCrash() {
        assertDoesNotThrow(() -> invokePrivateMethod("handleBack"),
                "Should not crash when back button pressed");
    }

    private Object getPrivateField(String fieldName) {
        try {
            Field f = CreateAccountStudentController.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(controller);
        } catch (Exception e) {
            fail("Failed to access field: " + fieldName);
            return null;
        }
    }
}
