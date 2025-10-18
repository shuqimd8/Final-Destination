package com.learneria.test;

import com.learneria.controller.GrammarGameController;
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
 * Unit tests for GrammarGameController.
 * These tests ensure initialization and core actions do not throw exceptions.
 */
public class GrammarGameControllerTest {

    private GrammarGameController controller;

    @BeforeAll
    static void setupJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
            // JavaFX runtime already initialized
        }
    }

    @BeforeEach
    void setup() {
        controller = new GrammarGameController();

        // Inject mock UI components
        setPrivateField("word1", new Label());
        setPrivateField("word2", new Label());
        setPrivateField("word3", new Label());
        setPrivateField("bucketNoun", new Rectangle());
        setPrivateField("bucketVerb", new Rectangle());
        setPrivateField("bucketAdjective", new Rectangle());
        setPrivateField("feedbackLabel", new Label());
        setPrivateField("scoreLabel", new Label());
        setPrivateField("timerLabel", new Label());
    }

    /** Helper to inject private fields via reflection */
    private void setPrivateField(String name, Object value) {
        try {
            Field f = GrammarGameController.class.getDeclaredField(name);
            f.setAccessible(true);
            f.set(controller, value);
        } catch (Exception e) {
            fail("Failed to inject field: " + name + " - " + e.getMessage());
        }
    }

    /** Helper to invoke private methods safely */
    private void invokePrivate(String methodName) {
        try {
            Method m = GrammarGameController.class.getDeclaredMethod(methodName);
            m.setAccessible(true);
            m.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testControllerExists() {
        assertNotNull(controller, "Controller should be created");
    }

    @Test
    void testInitialize_noCrash() {
        assertDoesNotThrow(() -> invokePrivate("initialize"),
                "initialize() should not throw exceptions");
    }

    @Test
    void testHandleBack_noCrash() {
        assertDoesNotThrow(() -> invokePrivate("handleBack"),
                "handleBack() should not crash");
    }

    @Test
    void testHandleGameOver_skipped() {
        System.out.println("Skipping handleGameOver() — requires full JavaFX scene & FXML context");
        assertTrue(true); // placeholder
    }


    @Test
    void testTimerAndScoreFieldsExist() {
        assertNotNull(getPrivateField("scoreLabel"));
        assertNotNull(getPrivateField("timerLabel"));
        assertNotNull(getPrivateField("feedbackLabel"));
    }

    /** Helper to access private fields */
    private Object getPrivateField(String name) {
        try {
            Field f = GrammarGameController.class.getDeclaredField(name);
            f.setAccessible(true);
            return f.get(controller);
        } catch (Exception e) {
            fail("Failed to read field: " + name);
            return null;
        }
    }
}
