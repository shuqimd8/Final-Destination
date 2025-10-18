package com.learneria.controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent; // ✅ Correct import

public class CreateAccountSelectController {

    @FXML
    public void handleStudent() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Student.fxml", "Create Student Account");
    }

    @FXML
    public void handleTeacher() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Form.fxml", "Create Teacher Account");
    }

    @FXML
    public void handleBack(MouseEvent event) {
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Log In");
    }
}
