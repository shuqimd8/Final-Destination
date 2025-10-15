package Controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class CreateAccountSelectController {

    @FXML
    private void handleStudent() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Student.fxml", "Create Student Account");
    }

    @FXML
    private void handleTeacher() {
        SceneManager.switchScene("/com/learneria/fxml/createAccount_Form.fxml", "Create Teacher Account");
    }

    @FXML
    private void handleBack(MouseEvent event) {
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Log In");
    }
}
