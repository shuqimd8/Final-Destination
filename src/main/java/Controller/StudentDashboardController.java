package Controller;

import com.learneria.utils.SceneManager;
import com.learneria.utils.UserAware;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StudentDashboardController implements UserAware {

    @FXML
    private Label welcomeLabel;

    private String username;

    @Override
    public void setUsername(String username) {
        this.username = username;
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    @FXML
    private void handleLogout() {
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Sign In");
    }
}

