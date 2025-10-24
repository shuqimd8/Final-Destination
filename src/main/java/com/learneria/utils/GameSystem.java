package com.learneria.utils;

import java.util.Random;

/**
 * The {@code GameSystem} class defines the reusable core game logic used by all
 * Papa's Learneria mini-games (Grammar, Food, Nature).
 * <p>
 * It handles score tracking, response timing, and round management.
 * Each instance represents one active game session for a specific category.
 * </p>
 */
public class GameSystem {

    /** The category this game session belongs to (e.g. "Grammar", "Food"). */
    private final String category;
    /** Current accumulated player score. */
    private int score;
    /** Number of correct answers provided. */
    private int correctCount;
    /** Number of incorrect answers provided. */
    private int incorrectCount;
    /** Total number of rounds played so far. */
    private int roundCount;
    /** Timestamp (ms) when the game started. */
    private long startTime;
    /** Timestamp (ms) when the last action occurred. */
    private long lastActionTime;
    /** Sum of all response durations for computing average speed. */
    private double totalResponseTime;
    /** Random generator used for feedback selection. */
    private final Random random = new Random();

    /**
     * Constructs a new {@code GameSystem} for the specified category.
     *
     * @param category the category name associated with this game session
     */
    public GameSystem(String category) {
        this.category = category;
        this.score = 0;
        this.correctCount = 0;
        this.incorrectCount = 0;
        this.roundCount = 0;
    }

    /**
     * Starts or resets a new game session.
     * <p>
     * This method clears all score and statistic data and sets the
     * starting time for measuring response speed.
     * </p>
     */
    public void startGame() {
        this.score = 0;
        this.correctCount = 0;
        this.incorrectCount = 0;
        this.roundCount = 1;
        this.startTime = System.currentTimeMillis();
        this.lastActionTime = startTime;
        this.totalResponseTime = 0;
    }


    /**
     * Evaluates a player’s submitted answer against the expected correct answer.
     * <p>
     * Adds 10 points for a correct answer, subtracts 5 points for a wrong one,
     * and updates average response-time tracking.
     * </p>
     *
     * @param given    the answer provided by the player
     * @param expected the correct expected answer
     */
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

    /**
     * Moves the session to the next round and updates the timer.
     */
    public void nextRound() {
        roundCount++;
        lastActionTime = System.currentTimeMillis();
    }

    /**
     * Returns how many rounds have been played in this session.
     *
     * @return the current round count
     */
    public int getRoundCount() {
        return roundCount;
    }

    /**
     * Calculates the player’s average response speed per round in milliseconds.
     *
     * @return average response time, or 0 if no rounds have been played
     */
    public double getAverageSpeed() {
        return (roundCount == 0) ? 0 : totalResponseTime / roundCount;
    }

    /**
     * Provides a random encouraging feedback message.
     *
     * @return a random feedback string such as “Nice job!”
     */
    public static String getRandomFeedback() {
        String[] feedback = {
                "Keep it up!", "Nice job!", "You’re learning fast!",
                "Good work!", "Excellent!"
        };
        return feedback[new Random().nextInt(feedback.length)];
    }

    // ====== Getters for score and stats ======

    /**
     * Returns the player’s current score.
     *
     * @return the score value
     */
    public int getScore() { return score; }

    /**
     * Returns how many correct answers have been given.
     *
     * @return number of correct responses
     */
    public int getCorrectCount() { return correctCount; }

    /**
     * Returns how many incorrect answers have been given.
     *
     * @return number of incorrect responses
     */
    public int getIncorrectCount() { return incorrectCount; }


    /**
     * Returns the category name for this game session.
     *
     * @return the category string
     */
    public String getCategory() { return category; }
}
