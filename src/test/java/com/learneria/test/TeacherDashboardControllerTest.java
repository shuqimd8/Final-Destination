package com.learneria.test;

import com.learneria.controller.TeacherDashboardController;
import com.learneria.utils.SceneManager;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TeacherDashboardController.
 * Verifies setUsername() and handleLogout() methods work correctly.
 */
public class TeacherDashboardControllerTest {

    private TeacherDashboardController controller;

    @BeforeAll
    static void setupFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
            // JavaFX already initialized
        }
    }

    @BeforeEach
    void setup() throws Exception {
        controller = new TeacherDashboardController();
        Label label = new Label();
        setPrivateField("welcomeLabel", label);
    }

    private void setPrivateField(String name, Object value) throws Exception {
        Field f = TeacherDashboardController.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(controller, value);
    }

    private Label getLabel(String name) throws Exception {
        Field f = TeacherDashboardController.class.getDeclaredField(name);
        f.setAccessible(true);
        return (Label) f.get(controller);
    }

    @Test
    void testSetUsername_updatesLabel() throws Exception {
        controller.setUsername("MrSmith");
        Label label = getLabel("welcomeLabel");
        assertEquals("Welcome, MrSmith!", label.getText(), "Label should display the username");
    }

    @Test
    void testHandleLogout_noCrash() throws Exception {
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {
            var method = TeacherDashboardController.class.getDeclaredMethod("handleLogout");
            method.setAccessible(true);
            assertDoesNotThrow(() -> method.invoke(controller),
                    "handleLogout() should not throw any exception");
            mockScene.verify(() ->
                    SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Sign In"), times(1));
        }
    }
}
