package com.learneria.test;

import com.learneria.controller.TeacherDashboardController;
import com.learneria.utils.SceneManager;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

public class TeacherDashboardControllerTest {

    private static TeacherDashboardController controller;

    @BeforeAll
    static void initJavaFX() {
        // Initializes JavaFX Toolkit so tests can run headlessly
        new JFXPanel();
        controller = new TeacherDashboardController();
    }

    @Test
    void testHandleLogout_switchesSceneWithoutCrash() {
        //  Open static mock for SceneManager
        try (MockedStatic<SceneManager> mockScene = mockStatic(SceneManager.class)) {

            // Directly call the logout method (no Platform.runLater)
            controller.handleLogout();

            //  Verify SceneManager called exactly once
            mockScene.verify(() ->
                            SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login"),
                    times(1));
        }
    }
}
