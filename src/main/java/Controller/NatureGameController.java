package Controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * NatureGameController — drag-and-drop Nature sorting game.
 * Categories: Animal, Plant, Non-Living, Weather.
 * Duration: 2 minutes. Tracks score, correct/incorrect, and average speed.
 */
public class NatureGameController {

    // ====== FXML UI ======
    @FXML private Label word1, word2, word3;
    @FXML private Label feedbackLabel, scoreLabel, timerLabel;
    @FXML private Rectangle bucketAnimal, bucketPlant, bucketNonLiving, bucketWeather;

    // ====== Game Logic ======
    private final String[] categories = {"Animal", "Plant", "Non-Living", "Weather"};
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
        System.out.println("🌿 Nature Game starting...");
        setupWordPools();
        setupWords();
        setupDragAndDrop();
        startTimer();
        updateScoreDisplay();
        feedbackLabel.setText("Drag nature words into the correct category!");
    }

    // ============================
    // TIMER (2 minutes)
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
    // WORD POOL MANAGEMENT
    // ============================
    private void setupWordPools() {
        for (String cat : categories) {
            List<String> pool = new ArrayList<>(Database.getAllWords(cat));

            // ✅ fallback defaults if database empty
            if (pool.isEmpty()) {
                switch (cat) {
                    case "Animal" -> pool.addAll(List.of("Dog", "Cat", "Bird", "Fish"));
                    case "Plant" -> pool.addAll(List.of("Tree", "Flower", "Grass", "Leaf"));
                    case "Non-Living" -> pool.addAll(List.of("Rock", "Water", "Air", "Fire"));
                    case "Weather" -> pool.addAll(List.of("Rain", "Sun", "Cloud", "Wind"));
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
            if (pool.isEmpty()) {
                switch (category) {
                    case "Animal" -> pool.addAll(List.of("Dog", "Cat", "Bird", "Fish"));
                    case "Plant" -> pool.addAll(List.of("Tree", "Flower", "Grass", "Leaf"));
                    case "Non-Living" -> pool.addAll(List.of("Rock", "Water", "Air", "Fire"));
                    case "Weather" -> pool.addAll(List.of("Rain", "Sun", "Cloud", "Wind"));
                }
            }
            Collections.shuffle(pool);
            wordPools.put(category, pool);
        }

        return pool.remove(0);
    }

    // ============================
    // INITIAL WORD SETUP
    // ============================
    private void setupWords() {
        loadWord(word1);
        loadWord(word2);
        loadWord(word3);
    }

    private void loadWord(Label label) {
        String category = categories[random.nextInt(categories.length)];
        String word = getNextWord(category);

        int safetyCounter = 0;
        while (isWordAlreadyVisible(word) && safetyCounter++ < 5) {
            category = categories[random.nextInt(categories.length)];
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
    // DRAG & DROP LOGIC
    // ============================
    private void setupDragAndDrop() {
        setupDrag(word1);
        setupDrag(word2);
        setupDrag(word3);

        setupBucket(bucketAnimal, "Animal");
        setupBucket(bucketPlant, "Plant");
        setupBucket(bucketNonLiving, "Non-Living");
        setupBucket(bucketWeather, "Weather");
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
                    feedbackLabel.setText("❌ Incorrect! " + word + " is a " + correctCategory);
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

            // ✅ Pass stats to GameOverController
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

        // ✅ Save to database
        Database.updateScore(
                SceneManager.getCurrentUser(),
                "Nature",
                score,
                correctCount,
                incorrectCount,
                avgSpeed
        );
    }

    // ============================
    // BACK TO MENU
    // ============================
    @FXML
    private void handleBack() {
        if (timer != null) timer.stop();
        SceneManager.goBackToDashboard();
    }
}
