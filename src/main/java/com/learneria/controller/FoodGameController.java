package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * FoodGameController — drag-and-drop food sorting game.
 * Categories: Fruit, Meat, Grain, Dairy.
 * Duration: 2 minutes.
 */
public class FoodGameController {

    // ====== FXML UI ======
    @FXML public Label word1, word2, word3;
    @FXML public Label scoreLabel, feedbackLabel, timerLabel;
    @FXML public Rectangle bucketFruit, bucketMeat, bucketGrain, bucketDairy;

    // ====== Game Logic ======
    private final String[] categories = {"Fruit", "Meat", "Grain", "Dairy"};
    private final Map<Label, String> wordCategoryMap = new HashMap<>();
    private final Map<String, List<String>> wordPools = new HashMap<>();

    private int score = 0;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private int totalAnswers = 0;

    private long startTime;
    private AnimationTimer timer;
    private long timeRemaining = 120; // seconds
    private final Random random = new Random();

    // ============================
    // INITIALISE
    // ============================
    @FXML
    public void initialize() {
        System.out.println("🍎 Food Game starting...");
        setupWordPools();
        setupWords();
        setupDragAndDrop();
        startTimer();
        updateScoreDisplay();
        feedbackLabel.setText("Drag each food to its correct category!");
    }

    // ============================
    // TIMER
    // ============================
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
    // WORD POOLS
    // ============================
    private void setupWordPools() {
        for (String cat : categories) {
            List<String> pool = new ArrayList<>(Database.getAllWords(cat));
            if (pool.isEmpty()) {
                // fallback
                switch (cat) {
                    case "Fruit" -> pool.addAll(List.of("Apple", "Banana", "Orange"));
                    case "Meat" -> pool.addAll(List.of("Chicken", "Beef", "Fish"));
                    case "Grain" -> pool.addAll(List.of("Rice", "Bread", "Cereal"));
                    case "Dairy" -> pool.addAll(List.of("Milk", "Cheese", "Yogurt"));
                }
            }
            Collections.shuffle(pool);
            wordPools.put(cat, pool);
        }
    }

    private String getNextWord(String category) {
        List<String> pool = wordPools.get(category);
        if (pool == null || pool.isEmpty()) {
            pool = new ArrayList<>(Database.getAllWords(category));
            Collections.shuffle(pool);
            wordPools.put(category, pool);
        }
        return pool.remove(0);
    }

    // ============================
    // INITIAL WORDS
    // ============================
    private void setupWords() {
        loadWord(word1);
        loadWord(word2);
        loadWord(word3);
    }

    private void loadWord(Label label) {
        String category = categories[random.nextInt(categories.length)];
        String word = getNextWord(category);

        while (isWordAlreadyVisible(word)) {
            word = getNextWord(category);
        }

        label.setText(word);
        wordCategoryMap.put(label, category);
    }

    private boolean isWordAlreadyVisible(String word) {
        return word.equals(word1.getText()) ||
                word.equals(word2.getText()) ||
                word.equals(word3.getText());
    }

    // ============================
    // DRAG & DROP
    // ============================
    private void setupDragAndDrop() {
        setupDrag(word1);
        setupDrag(word2);
        setupDrag(word3);

        setupBucket(bucketFruit, "Fruit");
        setupBucket(bucketMeat, "Meat");
        setupBucket(bucketGrain, "Grain");
        setupBucket(bucketDairy, "Dairy");
    }

    private void setupDrag(Label label) {
        label.setOnDragDetected(event -> {
            Dragboard db = label.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(label.getText());
            db.setContent(content);
            event.consume();
        });
    }

    private void setupBucket(Rectangle bucket, String category) {
        bucket.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof Label && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

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
                    feedbackLabel.setText("✅ Correct! " + word + " → " + category);
                } else {
                    incorrectCount++;
                    feedbackLabel.setText("❌ " + word + " is a " + correctCategory);
                }

                updateScoreDisplay();
                loadWord(source);
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void updateScoreDisplay() {
        scoreLabel.setText("Score: " + score);
    }

    // ============================
    // GAME OVER — show GameOverController
    // ============================
    private void handleGameOver() {
        if (timer != null) timer.stop();
        double avgSpeed = totalAnswers > 0 ? (120.0 / totalAnswers) : 0;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/learneria/fxml/game_over.fxml"));
            Parent root = loader.load();

            // ✅ Pass stats
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

        // ✅ Save score
        Database.updateScore(
                SceneManager.getCurrentUser(),
                "Food",
                score,
                correctCount,
                incorrectCount,
                avgSpeed
        );
    }

    // ============================
    // BACK BUTTON
    // ============================
    @FXML
    private void handleBack() {
        if (timer != null) timer.stop();
        SceneManager.goBackToDashboard();
    }
}
