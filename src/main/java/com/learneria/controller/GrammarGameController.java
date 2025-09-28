package com.learneria.controller;

import com.learneria.utils.SceneManager;
import com.learneria.utils.ScoreManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class GrammarGameController {

    @FXML private Label word1;
    @FXML private Label word2;

    @FXML private Rectangle bucketNoun;
    @FXML private Rectangle bucketVerb;

    private final Map<String, String> wordCategory = new HashMap<>();
    private int score = 0;
    private int totalWords = 0;

    @FXML
    public void initialize() {
        // Example words (expand as needed)
        wordCategory.put("Dog", "Noun");
        wordCategory.put("Cat", "Noun");
        wordCategory.put("Run", "Verb");
        wordCategory.put("Jump", "Verb");

        // Assign demo words
        word1.setText("Dog");
        word2.setText("Run");

        totalWords = 2;

        setupDrag(word1);
        setupDrag(word2);

        setupDrop(bucketNoun, "Noun");
        setupDrop(bucketVerb, "Verb");
    }

    private void setupDrag(Label word) {
        word.setOnDragDetected(event -> {
            Dragboard db = word.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(word.getText());
            db.setContent(content);
            event.consume();
        });
    }

    private void setupDrop(Rectangle bucket, String bucketType) {
        bucket.setOnDragOver(event -> {
            if (event.getGestureSource() != bucket && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        bucket.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String word = db.getString();
                System.out.println("✅ Word '" + word + "' dropped into " + bucketType + " bucket!");

                if (wordCategory.containsKey(word) && wordCategory.get(word).equals(bucketType)) {
                    score += 10;
                    System.out.println("🎉 Correct! +10 points");
                } else {
                    System.out.println("❌ Incorrect! No points");
                }

                success = true;
            }

            event.setDropCompleted(success);

            if (--totalWords == 0) {
                handleGameOver();
            }

            event.consume();
        });
    }

    private void handleGameOver() {
        String currentUser = SceneManager.getCurrentUser();
        System.out.println("🏁 Game Over! Final score: " + score);

        // Save score in DB
        ScoreManager.insertScore(currentUser, "Grammar", score);

        // Back to Student Main
        SceneManager.switchSceneWithUser(
                "/com/learneria/fxml/student_main.fxml",
                "Student Main Menu",
                currentUser
        );
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/student_main.fxml", "Student Main Menu");
    }
}

