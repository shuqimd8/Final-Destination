package Controller;

import Model.Game;
import Model.Round;
import Model.SceneManager;
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

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * GrammarGameController — drag-and-drop Grammar sorting game.
 * Duration: 2 minutes. Tracks score, correct/incorrect, average speed.
 */
public class GrammarGameController {

    // ====== FXML UI ======
    @FXML private Label word1, word2, word3;
    @FXML private Rectangle bucketNoun, bucketVerb, bucketAdjective;
    @FXML private Label feedbackLabel, scoreLabel, timerLabel;

    // ====== Game Logic ======
    File bucketFile = new File("src/main/java/TxtFiles/Buckets.txt");
    File wordFile = new File("src/main/java/TxtFiles/Words.txt");


    Game game = new Game(1,"Grammar",bucketFile, wordFile);
    private Round round;


    private int score = 0;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private int totalAnswers = 0;

    private long startTime;
    private AnimationTimer timer;
    private long timeRemaining = 120; // seconds
    private final Random random = new Random();

    // ============================
    // INITIALIZE
    // ============================
    @FXML
    public void initialize() {
        System.out.println("🎮 Grammar Game starting...");
        round = new Round(game);

        //setupDragAndDrop();
        startTimer();
    }

    //set word: grab word from round class and set to UI

    public void displayWord(){

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
            List<String> pool = Database.getAllWords(cat);
            Collections.shuffle(pool);
            wordPools.put(cat, new ArrayList<>(pool));
        }
    }

    private String getNextWord(String category) {
        List<String> pool = wordPools.get(category);
        if (pool == null || pool.isEmpty()) {
            pool = Database.getAllWords(category);
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

        while (isWordAlreadyVisible(word)) {
            word = getNextWord(category);
        }

        label.setText(word);
        wordCategoryMap.put(label, category);
    }

    private boolean isWordAlreadyVisible(String word) {
        return word.equals(word1.getText()) || word.equals(word2.getText()) || word.equals(word3.getText());
    }

    // ============================
    // DRAG & DROP LOGIC
    // ============================
    private void setupDragAndDrop() {
        setupDrag(word1);
        setupDrag(word2);
        setupDrag(word3);

        setupBucket(bucketNoun, "Noun");
        setupBucket(bucketVerb, "Verb");
        setupBucket(bucketAdjective, "Adjective");
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
                    feedbackLabel.setText("✅ " + word + " is a " + category + "!");
                } else {
                    incorrectCount++;
                    feedbackLabel.setText("❌ " + word + " is not a " + category + "!");
                }

                scoreLabel.setText("Score: " + score);
                loadWord(source);
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    // ============================
    // GAME OVER (Pass stats to next scene)
    // ============================
    private void handleGameOver() {
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

        // Save to database
        Database.updateScore(SceneManager.getCurrentUser(), "Grammar", score, correctCount, incorrectCount, avgSpeed);
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


