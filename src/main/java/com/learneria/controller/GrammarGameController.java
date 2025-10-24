package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * Controller for the Grammar Sorting Game.
 * <p>
 * This controller manages the Grammar game, where players drag and drop words
 * into buckets labeled as Noun, Verb, or Adjective.
 * The player has 120 seconds to sort as many words as possible,
 * earning +10 points for each correct answer.
 * </p>
 * <p>
 * Handles word generation, drag-and-drop behavior, scoring, and end-of-game
 * transitions to the Game Over screen.
 * </p>
 */
public class GrammarGameController {

    /** Label displaying the draggable word. */
    @FXML public Label word1;
    /** Image buckets representing the grammar categories. */
    @FXML public ImageView bucketNoun, bucketVerb, bucketAdjective;
    /** Game UI labels for feedback, score, timer, high score, and username. */
    @FXML public Label feedbackLabel, scoreLabel, timerLabel, highScoreLabel, usernameLabel;

    /** The available grammar categories. */
    private final String[] categories = {"Noun", "Verb", "Adjective"};
    /** Maps each displayed label to its correct category. */
    private final Map<Label, String> wordCategoryMap = new HashMap<>();
    /** Stores available words per category for random selection. */
    private final Map<String, List<String>> wordPools = new HashMap<>();

    private int score = 0;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private int totalAnswers = 0;

    private long startTime;
    private AnimationTimer timer;
    private long timeRemaining = 120; // seconds
    private final Random random = new Random();
    private String username;

    /**
     * Initializes the Grammar Game.
     * <p>
     * Loads the logged-in user's data, high score, word pools,
     * and sets up drag-and-drop gameplay with a 120-second timer.
     * </p>
     */
    @FXML
    public void initialize() {
        username = SceneManager.getCurrentUser();

        //  Display logged in user's name
        if (usernameLabel != null && username != null)
            usernameLabel.setText(username);

        // Fetch real high score from DB
        if (highScoreLabel != null) {
            int highScore = Database.getHighScore(username, "Grammar");
            highScoreLabel.setText(String.valueOf(highScore));
        }

        // Reset score display
        scoreLabel.setText("0");

        System.out.println("🎮 Grammar Game starting for: " + username);
        setupWordPools();
        loadWord(word1);
        setupDragAndDrop();
        startTimer();
    }

    /**
     * Starts the game timer and updates the timer label each second.
     * When time runs out, triggers the game over sequence.
     */
    private void startTimer() {
        startTime = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsed = (now - startTime) / 1_000_000_000;
                timeRemaining = 120 - elapsed;

                if (timeRemaining <= 0) {
                    timerLabel.setText("Time: 0s");
                    stop();
                    handleGameOver();
                } else {
                    timerLabel.setText("Time: " + timeRemaining + "s");
                }
            }
        };
        timer.start();
    }


    /**
     * Initializes the word pools for each grammar category
     * by fetching data from the database and shuffling them.
     */
    private void setupWordPools() {
        for (String cat : categories) {
            List<String> pool = Database.getAllWords(cat);
            if (pool != null) {
                Collections.shuffle(pool);
                wordPools.put(cat, new ArrayList<>(pool));
            }
        }
    }

    /**
     * Retrieves the next available word from a given category.
     * If the pool is empty, it reinitializes from the database.
     *
     * @param category the grammar category to retrieve a word from
     * @return the next word from the pool
     */
    private String getNextWord(String category) {
        List<String> pool = wordPools.get(category);
        if (pool == null || pool.isEmpty()) {
            pool = Database.getAllWords(category);
            Collections.shuffle(pool);
            wordPools.put(category, pool);
        }
        return pool.remove(0);
    }


    /**
     * Loads a random word into the given label and maps it
     * to its correct category.
     *
     * @param label the label used to display the word
     */
    private void loadWord(Label label) {
        String category = categories[random.nextInt(categories.length)];
        String word = getNextWord(category);
        label.setText(word);
        wordCategoryMap.put(label, category);
    }

    /**
     * Configures drag-and-drop logic for words and target buckets.
     */
    private void setupDragAndDrop() {
        setupDrag(word1);
        setupBucket(bucketNoun, "Noun");
        setupBucket(bucketVerb, "Verb");
        setupBucket(bucketAdjective, "Adjective");
    }


    /**
     * Enables dragging for a label element.
     *
     * @param label the label to make draggable
     */
    private void setupDrag(Label label) {
        label.setOnDragDetected(event -> {
            Dragboard db = label.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(label.getText());
            db.setContent(content);
            event.consume();
        });
    }

    /**
     * Configures a drop target bucket for a given grammar category.
     *
     * @param bucket   the target UI node (bucket)
     * @param category the category name this bucket represents
     */
    private void setupBucket(javafx.scene.Node bucket, String category) {
        bucket.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof Label && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
                bucket.setStyle("-fx-effect: dropshadow(gaussian, #40FFE2, 25, 0.5, 0, 0);");
            }
            event.consume();
        });

        bucket.setOnDragExited(event -> bucket.setStyle(""));

        bucket.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                Label source = (Label) event.getGestureSource();
                String word = db.getString();
                String correctCategory = wordCategoryMap.get(source);
                totalAnswers++;

                if (correctCategory.equalsIgnoreCase(category)) {
                    score += 10;
                    correctCount++;
                    feedbackLabel.setText("Correct! " + word + " is a " + category + "!");
                } else {
                    incorrectCount++;
                    feedbackLabel.setText("Wrong! " + word + " is not a " + category + "!");
                }

                scoreLabel.setText(String.valueOf(score));
                loadWord(source);
                success = true;
            }

            bucket.setStyle(""); // reset glow
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Ends the game session, updates scores in the database,
     * and transitions to the Game Over screen showing statistics.
     */
    private void handleGameOver() {
        double avgSpeed = totalAnswers > 0 ? (120.0 / totalAnswers) : 0;

        Database.updateScore(SceneManager.getCurrentUser(),
                "Grammar", score, correctCount, incorrectCount, avgSpeed);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/learneria/fxml/game_over.fxml"));
            Parent root = loader.load();

            GameOverController controller = loader.getController();
            controller.setStats(correctCount, incorrectCount, score, avgSpeed);

            Scene scene = new Scene(root);
            Stage stage = (Stage) scoreLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Game Over");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ends the game session, updates scores in the database,
     * and transitions to the Game Over screen showing statistics.
     */
    @FXML
    public void handleBack() {
        if (timer != null) timer.stop();
        SceneManager.goBackToDashboard();
    }
}
