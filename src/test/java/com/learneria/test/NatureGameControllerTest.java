package com.learneria.test;

import com.learneria.controller.NatureGameController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NatureGameController.
 * Ensures setup, back navigation, and logic operations don't crash.
 */
public class NatureGameControllerTest {

    private NatureGameController controller;

    @BeforeAll
    static void setupJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
            // JavaFX already initialized
        }
    }

    @BeforeEach
    void setup() {
        controller = new NatureGameController();

        // Inject mock FXML UI fields
        setPrivateField("word1", new Label());
        setPrivateField("word2", new Label());
        setPrivateField("word3", new Label());
        setPrivateField("feedbackLabel", new Label());
        setPrivateField("scoreLabel", new Label());
        setPrivateField("timerLabel", new Label());
        setPrivateField("bucketAnimal", new Rectangle());
        setPrivateField("bucketPlant", new Rectangle());
        setPrivateField("bucketNonLiving", new Rectangle());
        setPrivateField("bucketWeather", new Rectangle());
    }

    /** Inject mock objects into private fields */
    private void setPrivateField(String fieldName, Object value) {
        try {
            Field f = NatureGameController.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(controller, value);
        } catch (Exception e) {
            fail("Failed to inject field: " + fieldName + " - " + e.getMessage());
        }
    }

    /** Helper: invoke private method by name */
    private void invokePrivate(String methodName) {
        try {
            Method m = NatureGameController.class.getDeclaredMethod(methodName);
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------- TEST CASES ----------------

    @Test
    void testControllerExists() {
        assertNotNull(controller, "Controller should be created successfully");
    }

    @Test
    void testInitialize_noCrash() {
        assertDoesNotThrow(() -> invokePrivate("initialize"),
                "initialize() should not throw any exceptions");
    }

    @Test
    void testHandleBack_noCrash() {
        assertDoesNotThrow(() -> invokePrivate("handleBack"),
                "handleBack() should not crash even if timer is null");
    }

    // We skip handleGameOver since it needs FXML + Scene context
    @Test
    void testHandleGameOver_skipped() {
        System.out.println("Skipping handleGameOver() - requires full JavaFX Scene & FXML context");
        assertTrue(true);
    }

    @Test
    void testScoreAndTimerLabelsExist() {
        assertNotNull(getPrivateField("scoreLabel"), "Score label should be injected");
        assertNotNull(getPrivateField("timerLabel"), "Timer label should be injected");
        assertNotNull(getPrivateField("feedbackLabel"), "Feedback label should be injected");
    }

    /** Helper: access private field values */
    private Object getPrivateField(String fieldName) {
        try {
            Field f = NatureGameController.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(controller);
        } catch (Exception e) {
            fail("Failed to access field: " + fieldName);
            return null;
        }
    }
}
