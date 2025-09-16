package com.example.demo;

import com.example.demo.learneria.Hub;
import com.example.demo.learneria.MainMenu;
import com.example.demo.learneria.Settings;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.application.Application;

import java.io.IOException;

public class StudentMainController {
    @FXML
    private Label welcomeText;
    @FXML
    private Circle myCircle;
    @FXML
    private ImageView myImageView; // Injects the ImageView from FXML

    /// Button Navigation ///
    //PlayButton
    @FXML
    private Button playButton;
    @FXML
    protected void onPlayButtonClick() throws IOException {
        System.out.println("Play");
        Stage stage = (Stage) playButton.getScene().getWindow();
        Hub hub = new Hub();
        hub.start(stage);
    }
    //PlayerView
    @FXML
    private Button playerViewButton;
    @FXML
    protected void onPlayerViewButtonClick() {
        System.out.println("PlayerView");
    }
    //SignOut
    @FXML
    private Button signOutButton;
    @FXML
    protected void onSignOutButtonClick() {
        System.out.println("SignOut");
    }
    //settings
    @FXML
    private Button settingsButton;
    @FXML
    protected void onSettingsButtonClick() throws IOException{
        System.out.println("settings");
        Stage stage = (Stage) settingsButton.getScene().getWindow();

        //for the purpose of this will create new stats screen instance but ideally
        // this is changed later to one of each screen from a centralised controller
        Settings settings = new Settings();
        settings.start(stage);
    }
    //stats
    @FXML
    private Button statsButton;
    @FXML
    protected void onStatsButtonClick() throws IOException {
        Stage stage = (Stage) statsButton.getScene().getWindow();
        System.out.println("stats");

        //for the purpose of this will create new stats screen instance but ideally
        // this is changed later to one of each screen from a centralised controller
        StatsScreen statsScreen = new StatsScreen();
        statsScreen.start(stage);

        /*
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("StatsScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

         */

    }
}
