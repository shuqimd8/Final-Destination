package com.learneria.controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameOverController {

    @FXML private Label correctLabel;
    @FXML private Label incorrectLabel;
    @FXML private Label scoreLabel;
    @FXML private Label avgSpeedLabel;

    public void setStats(int correct, int incorrect, int score, double avgSpeed) {
        correctLabel.setText("Total Correct Answers: " + correct);
        incorrectLabel.setText("Total Incorrect Answers: " + incorrect);
        scoreLabel.setText("Score: " + score);
        avgSpeedLabel.setText("Average Answer Speed: " + String.format("%.1f", avgSpeed) + "s");
    }

    @FXML
    private void handleContinue() {
        SceneManager.goBackToDashboard();
    }
}

