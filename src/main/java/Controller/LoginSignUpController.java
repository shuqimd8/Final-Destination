package Controller;

import Model.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginSignUpController {
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

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        String userName = userField.getText();
        String userPassword = passwordField.getText();
        Student.studentLogin(userName, userPassword);
        // Further processing of userInput
    }
}
