package com.learneria.controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Controller class for the account type selection screen.
 * <p>
 * Allows users to choose between creating a student or teacher account,
 * or navigate back to the login screen.
 * </p>
 */
public class CreateAccountSelectController {

    /** ImageView element used as a back navigation arrow. */
    public ImageView backArrow;

    /**
     * Handles the selection of the "Student" option.
     * <p>
     * Switches the current scene to the student account creation form.
     * </p>
     */
    @FXML
    public void handleStudent() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Student.fxml", "Create Student Account");
    }

    /**
     * Handles the selection of the "Teacher" option.
     * <p>
     * Switches the current scene to the teacher account creation form.
     * </p>
     */
    @FXML
    public void handleTeacher() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Form.fxml", "Create Teacher Account");
    }

    /**
     * Handles the back arrow click event.
     * <p>
     * Returns the user to the login screen.
     * </p>
     *
     * @param event The mouse event triggered by clicking the back arrow.
     */
    @FXML
    public void handleBack(MouseEvent event) {
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Log In");
    }
}
