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
 * Controller for the Nature Sorting Game.
 * <p>
 * This class manages the drag-and-drop game where users sort words into
 * four nature-related categories: Animal, Plant, Non-Living, and Weather.
 * Players have 120 seconds to correctly categorize as many words as possible.
 * </p>
 * <p>
 * Handles game initialization, timer logic, drag-and-drop functionality,
 * scoring, and database updates for player statistics.
 * </p>
 */
public class NatureGameController {

    /** Label displaying the current word to be sorted. */
    @FXML public Label word1;
    /** Image buckets for each of the four nature categories. */
    @FXML public ImageView bucketAnimal, bucketPlant, bucketNonLiving, bucketWeather;
    /** Labels displaying feedback, score, timer, high score, and username. */
    @FXML public Label feedbackLabel, scoreLabel, timerLabel, highScoreLabel, usernameLabel;

    /** The available nature categories. */
    private final String[] categories = {"Animal", "Plant", "Non-Living", "Weather"};
    /** Maps each word label to its corresponding correct category. */
    private final Map<Label, String> wordCategoryMap = new HashMap<>();
    /** Stores the available word lists per category for random selection. */
    private final Map<String, List<String>> wordPools = new HashMap<>();

    private int score = 0;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private int totalAnswers = 0;

    private long startTime;
    private AnimationTimer timer;
    private long timeRemaining = 120;
    private final Random random = new Random();
    private String username;


    /**
     * Initializes the Nature Game.
     * <p>
     * Loads the logged-in user’s name and high score,
     * sets up the word pools, and starts the timer.
     * </p>
     */
    @FXML
    public void initialize() {
        username = SceneManager.getCurrentUser();

        if (usernameLabel != null && username != null)
            usernameLabel.setText(username);

        if (highScoreLabel != null) {
            int highScore = Database.getHighScore(username, "Nature");
            highScoreLabel.setText(String.valueOf(highScore));
        }

        scoreLabel.setText("0");
        feedbackLabel.setText("Drag the word into the correct bucket!");

        setupWordPools();
        loadWord(word1);
        setupDragAndDrop();
        startTimer();
    }

    /**
     * Starts the game timer and updates the time display every second.
     * When time expires, triggers the game-over handler.
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
     * Initializes the word pools by retrieving words from the database
     * or using default fallback lists for each category.
     */
    private void setupWordPools() {
        for (String cat : categories) {
            List<String> pool = new ArrayList<>(Database.getAllWords(cat));

            if (pool.isEmpty()) {
                switch (cat) {
                    case "Animal" -> pool.addAll(List.of("Lion", "Tiger", "Elephant", "Monkey"));
                    case "Plant" -> pool.addAll(List.of("Tree", "Flower", "Grass", "Leaf"));
                    case "Non-Living" -> pool.addAll(List.of("Rock", "Fire", "Water", "Wind"));
                    case "Weather" -> pool.addAll(List.of("Rain", "Snow", "Sun", "Cloud"));
                }
            }

            Collections.shuffle(pool);
            wordPools.put(cat, pool);
        }
    }

    /**
     * Retrieves the next available word for the specified category.
     * Replenishes the word pool if empty.
     *
     * @param category The category to fetch the next word from.
     * @return The next word for the given category.
     */
    private String getNextWord(String category) {
        List<String> pool = wordPools.get(category);

        if (pool == null || pool.isEmpty()) {
            pool = new ArrayList<>(Database.getAllWords(category));
            if (pool.isEmpty()) {
                switch (category) {
                    case "Animal" -> pool.addAll(List.of("Lion", "Tiger", "Elephant", "Monkey"));
                    case "Plant" -> pool.addAll(List.of("Tree", "Flower", "Grass", "Leaf"));
                    case "Non-Living" -> pool.addAll(List.of("Rock", "Fire", "Water", "Wind"));
                    case "Weather" -> pool.addAll(List.of("Rain", "Snow", "Sun", "Cloud"));
                }
            }
            Collections.shuffle(pool);
            wordPools.put(category, pool);
        }
        return pool.remove(0);
    }
    /**
     * Loads a random word into the specified label and stores its correct category.
     *
     * @param label The label used to display the new word.
     */
    private void loadWord(Label label) {
        String category = categories[random.nextInt(categories.length)];
        String word = getNextWord(category);
        label.setText(word);
        wordCategoryMap.put(label, category);
    }

    /**
     * Configures drag-and-drop for all word labels and category buckets.
     */
    private void setupDragAndDrop() {
        setupDrag(word1);
        setupBucket(bucketAnimal, "Animal");
        setupBucket(bucketPlant, "Plant");
        setupBucket(bucketNonLiving, "Non-Living");
        setupBucket(bucketWeather, "Weather");
    }

    /**
     * Enables dragging functionality for a given label.
     *
     * @param label The label that can be dragged by the user.
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
     * Configures the drop behavior for a specific bucket (category).
     *
     * @param bucket   The ImageView node representing the category bucket.
     * @param category The category name associated with the bucket.
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
                    feedbackLabel.setText("Correct! " + word + " → " + category);
                } else {
                    incorrectCount++;
                    feedbackLabel.setText("Incorrect! " + word + " is a " + correctCategory);
                }

                scoreLabel.setText(String.valueOf(score));
                loadWord(source);
                success = true;
            }

            bucket.setStyle("");
            event.setDropCompleted(success);
            event.consume();
        });
    }


    /**
     * Handles the game-over sequence.
     * <p>
     * Updates the player’s performance data in the database,
     * calculates average response time, and navigates to the Game Over screen.
     * </p>
     */
    private void handleGameOver() {
        double avgSpeed = totalAnswers > 0 ? (120.0 / totalAnswers) : 0;
        Database.updateScore(SceneManager.getCurrentUser(), "Nature", score, correctCount, incorrectCount, avgSpeed);

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
     * Handles the Back button click.
     * <p>
     * Stops the timer (if running) and returns the user to their dashboard.
     * </p>
     */
    @FXML
    public void handleBack() {
        if (timer != null) timer.stop();
        SceneManager.goBackToDashboard();
    }
}
