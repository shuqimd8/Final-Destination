package com.learneria.controller;

import com.learneria.utils.Database;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;



public class CreateAccountSelectController {

    @FXML
    private void handleStudent() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Student.fxml", "Create Student Account");
    }

    @FXML
    private void handleTeacher() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Form.fxml", "Create Teacher Account");
    }
}


