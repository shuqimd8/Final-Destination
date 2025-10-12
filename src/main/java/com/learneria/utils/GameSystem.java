package com.learneria.utils;

import java.util.Random;

/**
 * Core reusable system for all mini-games (Grammar, Food, Nature)
 */
public class GameSystem {

    private final String category;
    private int score;
    private int correctCount;
    private int incorrectCount;
    private int roundCount;
    private long startTime;
    private long lastActionTime;
    private double totalResponseTime;
    private final Random random = new Random();

    public GameSystem(String category) {
        this.category = category;
        this.score = 0;
        this.correctCount = 0;
        this.incorrectCount = 0;
        this.roundCount = 0;
    }

    /** Start a new game session */
    public void startGame() {
        this.score = 0;
        this.correctCount = 0;
        this.incorrectCount = 0;
        this.roundCount = 1;
        this.startTime = System.currentTimeMillis();
        this.lastActionTime = startTime;
        this.totalResponseTime = 0;
    }

    /** Record player’s answer result */
    public void evaluateAnswer(String given, String expected) {
        long now = System.currentTimeMillis();
        totalResponseTime += (now - lastActionTime);
        lastActionTime = now;

        if (given.equalsIgnoreCase(expected)) {
            score += 10;
            correctCount++;
        } else {
            score -= 5;
            incorrectCount++;
        }
    }

    /** Proceed to the next round */
    public void nextRound() {
        roundCount++;
        lastActionTime = System.currentTimeMillis();
    }

    /** Get how many rounds have been played */
    public int getRoundCount() {
        return roundCount;
    }

    /** Average response speed per round (ms) */
    public double getAverageSpeed() {
        return (roundCount == 0) ? 0 : totalResponseTime / roundCount;
    }

    /** Randomized feedback strings */
    public static String getRandomFeedback() {
        String[] feedback = {
                "Keep it up!", "Nice job!", "You’re learning fast!",
                "Good work!", "Excellent!"
        };
        return feedback[new Random().nextInt(feedback.length)];
    }

    // ====== Getters for score and stats ======

    public int getScore() { return score; }
    public int getCorrectCount() { return correctCount; }
    public int getIncorrectCount() { return incorrectCount; }
    public String getCategory() { return category; }
}
