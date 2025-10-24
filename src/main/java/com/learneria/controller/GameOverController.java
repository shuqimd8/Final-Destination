package com.learneria.controller;

import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the Game Over screen.
 * <p>
 * Displays the player's performance summary after completing a game,
 * including total correct/incorrect answers, score, and average response speed.
 * </p>
 * <p>
 * Also provides navigation back to the dashboard via the Continue button.
 * </p>
 */
public class GameOverController {

    /** Label displaying the total number of correct answers. */
    @FXML private Label correctLabel;
    /** Label displaying the total number of incorrect answers. */
    @FXML private Label incorrectLabel;
    /** Label displaying the player's total score. */
    @FXML private Label scoreLabel;
    /** Label displaying the player's average answer speed. */
    @FXML private Label avgSpeedLabel;


    /**
     * Populates the Game Over screen with the player's statistics.
     *
     * @param correct   The total number of correct answers.
     * @param incorrect The total number of incorrect answers.
     * @param score     The player's total score.
     * @param avgSpeed  The player's average time per answer.
     */
    public void setStats(int correct, int incorrect, int score, double avgSpeed) {
        correctLabel.setText("Total Correct Answers: " + correct);
        incorrectLabel.setText("Total Incorrect Answers: " + incorrect);
        scoreLabel.setText("Score: " + score);
        avgSpeedLabel.setText("Average Answer Speed: " + String.format("%.1f", avgSpeed) + "s");
    }

    /**
     * Handles the Continue button click event.
     * <p>
     * Returns the player to the dashboard after finishing a game.
     * </p>
     */
    @FXML
    private void handleContinue() {
        SceneManager.goBackToDashboard();
    }
}

