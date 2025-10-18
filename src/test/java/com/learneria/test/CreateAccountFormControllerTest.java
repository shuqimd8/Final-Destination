package com.learneria.test;

import com.learneria.controller.CreateAccountFormController;
import javafx.application.Platform;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountFormControllerTest {

    private CreateAccountFormController controller;

    /** Initialize JavaFX platform once before all tests */
    @BeforeAll
    static void initToolkit() {
        try {
            if (!Platform.isFxApplicationThread()) {
                // Launch JavaFX runtime in background without JFXPanel
                Platform.startup(() -> {});
            }
        } catch (IllegalStateException ignored) {
            // JavaFX already started, ignore
        }
    }

    /** Create a fresh controller instance before each test */
    @BeforeEach
    void setup() {
        controller = new CreateAccountFormController();
        setField("nameField", new TextField());
        setField("usernameField", new TextField());
        setField("passwordField", new PasswordField());
        setField("retypePasswordField", new PasswordField());
        setField("subjectField", new TextField());
    }

    /** Helper: safely inject private fields via reflection */
    private void setField(String fieldName, Object value) {
        try {
            Field f = CreateAccountFormController.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(controller, value);
        } catch (Exception e) {
            fail("Failed to inject field: " + fieldName + " - " + e.getMessage());
        }
    }

    /** Ensure controller initializes correctly */
    @Test
    void testControllerInitializes() {
        assertNotNull(controller, "Controller should be created");
    }

    /** Test: empty fields should not crash */
    @Test
    void testEmptyFields_noCrash() {
        assertDoesNotThrow(() -> {
            controller.handleSubmit();
        }, "Should not throw any exception when fields are empty");
    }

    /** Test: mismatched passwords should not crash */
    @Test
    void testMismatchedPasswords_noCrash() {
        ((TextField) getField("nameField")).setText("Mr. Bean");
        ((TextField) getField("usernameField")).setText("teacher1");
        ((PasswordField) getField("passwordField")).setText("abc123");
        ((PasswordField) getField("retypePasswordField")).setText("xyz999");
        ((TextField) getField("subjectField")).setText("Math");

        assertDoesNotThrow(() -> controller.handleSubmit(),
                "Should not crash when passwords do not match");
    }

    /** Test: valid input should not crash */
    @Test
    void testValidInput_noCrash() {
        ((TextField) getField("nameField")).setText("John Doe");
        ((TextField) getField("usernameField")).setText("teacher2");
        ((PasswordField) getField("passwordField")).setText("secure123");
        ((PasswordField) getField("retypePasswordField")).setText("secure123");
        ((TextField) getField("subjectField")).setText("Science");

        assertDoesNotThrow(() -> controller.handleSubmit(),
                "Should not crash with valid inputs");
    }

    /** Test: clicking back should not crash */
    @Test
    void testBackNavigation_noCrash() {
        assertDoesNotThrow(() -> controller.handleBack(),
                "Should not crash when back button pressed");
    }

    /** Helper: access private fields */
    private Object getField(String fieldName) {
        try {
            Field f = CreateAccountFormController.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(controller);
        } catch (Exception e) {
            fail("Failed to access field: " + fieldName);
            return null;
        }
    }
}
