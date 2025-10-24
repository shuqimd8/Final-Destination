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
 * Controller for the Food Sorting Game.
 * <p>
 * This class manages the drag-and-drop gameplay for sorting food
 * items into four categories: Fruit, Meat, Grain, and Dairy.
 * The player earns 10 points per correct classification within 120 seconds.
 * </p>
 * <p>
 * Handles game initialization, timer countdown, word pool management,
 * drag-and-drop logic, score tracking, and transition to the game over screen.
 * </p>
 */
public class FoodGameController {

    // ============================
    //  FXML UI Elements
    // ============================

    /** The label showing the current draggable word. */
    @FXML public Label word1;
    /** Displays current score, feedback, timer, and high score. */
    @FXML public Label scoreLabel, feedbackLabel, timerLabel, highScoreLabel, usernameLabel;
    /** Image buckets representing each food category. */
    @FXML public ImageView bucketFruit, bucketMeat, bucketGrain, bucketDairy;

    // ============================
    //  Game State
    // ============================
    /** Array of available food categories. */
    private final String[] categories = {"Fruit", "Meat", "Grain", "Dairy"};
    /** Maps each label (word) to its correct category. */
    private final Map<Label, String> wordCategoryMap = new HashMap<>();
    /** Holds the list of words for each category. */
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

    // ============================
    //  INITIALISE
    // ============================

    /**
     * Initializes the Food Game scene.
     * <p>
     * Loads player information, sets up word pools, initializes the
     * timer, and prepares the drag-and-drop event handlers.
     * </p>
     */
    @FXML
    public void initialize() {
        username = SceneManager.getCurrentUser();

        if (usernameLabel != null && username != null)
            usernameLabel.setText(username);

        if (highScoreLabel != null) {
            int highScore = Database.getHighScore(username, "Food");
            highScoreLabel.setText(String.valueOf(highScore));
        }

        scoreLabel.setText("0");
        feedbackLabel.setText(" Drag the food into the correct bucket!");

        System.out.println(" Food Game starting for: " + username);

        setupWordPools();
        loadWord(word1);
        setupDragAndDrop();
        startTimer();
    }

    // ============================
    //  TIMER
    // ============================

    /**
     * Starts and updates the 120-second countdown timer.
     * <p>
     * When time reaches zero, the game ends automatically.
     * </p>
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

    // ============================
    //  WORD POOLS
    // ============================

    /**
     * Loads words from the database for each food category,
     * and adds fallback defaults if the database is empty.
     */
    private void setupWordPools() {
        for (String cat : categories) {
            List<String> pool = new ArrayList<>(Database.getAllWords(cat));
            if (pool.isEmpty()) {
                switch (cat) {
                    case "Fruit" -> pool.addAll(List.of("Apple", "Banana", "Orange", "Mango"));
                    case "Meat"  -> pool.addAll(List.of("Chicken", "Beef", "Fish", "Lamb"));
                    case "Grain" -> pool.addAll(List.of("Rice", "Bread", "Pasta", "Cereal"));
                    case "Dairy" -> pool.addAll(List.of("Milk", "Cheese", "Yogurt", "Butter"));
                }
            }
            Collections.shuffle(pool);
            wordPools.put(cat, pool);
        }
    }

    /**
     * Retrieves the next available word from the specified category.
     *
     * @param category The category to fetch the next word from.
     * @return A word string from the corresponding category.
     */
    private String getNextWord(String category) {
        List<String> pool = wordPools.get(category);
        if (pool == null || pool.isEmpty()) {
            pool = new ArrayList<>(Database.getAllWords(category));
            if (pool.isEmpty()) {
                switch (category) {
                    case "Fruit" -> pool.addAll(List.of("Apple", "Banana", "Orange", "Mango"));
                    case "Meat"  -> pool.addAll(List.of("Chicken", "Beef", "Fish", "Lamb"));
                    case "Grain" -> pool.addAll(List.of("Rice", "Bread", "Pasta", "Cereal"));
                    case "Dairy" -> pool.addAll(List.of("Milk", "Cheese", "Yogurt", "Butter"));
                }
            }
            Collections.shuffle(pool);
            wordPools.put(category, pool);
        }
        return pool.remove(0);
    }

    // ============================
    //  WORD GENERATION
    // ============================

    /**
     * Loads a random word from a random category
     * and assigns it to the given label.
     *
     * @param label The label to display the word.
     */
    private void loadWord(Label label) {
        String category = categories[random.nextInt(categories.length)];
        String word = getNextWord(category);
        label.setText(word);
        wordCategoryMap.put(label, category);
    }

    // ============================
    //  DRAG & DROP
    // ============================

    /**
     * Initializes drag and drop for all word labels and category buckets.
     */
    private void setupDragAndDrop() {
        setupDrag(word1);
        setupBucket(bucketFruit, "Fruit");
        setupBucket(bucketMeat, "Meat");
        setupBucket(bucketGrain, "Grain");
        setupBucket(bucketDairy, "Dairy");
    }


    /**
     * Configures drag detection for a draggable label.
     *
     * @param label The label element to make draggable.
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
     * Sets up a drop target (bucket) for a specific food category.
     *
     * @param bucket   The ImageView representing the category bucket.
     * @param category The name of the category.
     */
    private void setupBucket(ImageView bucket, String category) {
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
                    feedbackLabel.setText("Wrong! " + word + " is actually a " + correctCategory + "!");
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

    // ============================
    //  GAME OVER
    // ============================

    /**
     * Ends the game, updates the player’s stats in the database,
     * and navigates to the Game Over screen with performance summary.
     */
    private void handleGameOver() {
        if (timer != null) timer.stop();
        double avgSpeed = totalAnswers > 0 ? (120.0 / totalAnswers) : 0;

        Database.updateScore(
                SceneManager.getCurrentUser(),
                "Food",
                score,
                correctCount,
                incorrectCount,
                avgSpeed
        );

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

    // ============================
    //  BACK BUTTON
    // ============================

    /**
     * Returns to the dashboard when the Back button is clicked.
     * Stops the timer to prevent background updates.
     */
    @FXML
    public void handleBack() {
        if (timer != null) timer.stop();
        SceneManager.goBackToDashboard();
    }
}
