package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StatsScreenController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField userField;


    //BackButton
    @FXML
    private Button backButton;
    @FXML
    protected void onBackButtonClick() throws IOException {
        System.out.println("Return to student main menu");
        Stage stage = (Stage) backButton.getScene().getWindow();

        //for the purpose of this will create new stats screen instance but ideally
        // this is changed later to one of each screen from a centralised controller
        StudentMainApplication studentMain = new StudentMainApplication();
        studentMain.start(stage);

    }
    /// ignore possible code conflictions for now
    /*
    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        String userName = userField.getText();
        String userPassword = passwordField.getText();
        Student.studentLogin(userName, userPassword);
        // Further processing of userInput
    }

     */
}
