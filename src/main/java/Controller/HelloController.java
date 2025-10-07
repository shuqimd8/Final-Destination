package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private Circle myCircle;
    @FXML
    private ImageView myImageView; // Injects the ImageView from FXML

    //@Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load the image from the classpath
        Image image = new Image(getClass().getResourceAsStream("C:\\Users\\Bella\\devel\\CAB302\\fuckAround\\Icons\\PapaMascot.png"));
        myImageView.setImage(image);
    }


    @FXML
    protected void onHelloButtonClick() {
        System.out.println("button clicked");
    }
}
