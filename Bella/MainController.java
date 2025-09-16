package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label welcomeText;
    //PlayButton
    @FXML
    protected void onPlayButtonClick() {
        System.out.println("Play");
    }
}
