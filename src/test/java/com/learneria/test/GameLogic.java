package com.learneria.utils;

public class GameLogic {
    private int score;

    public GameLogic() {
        this.score = 0;
    }

    public void addCorrectAnswer() {
        score += 10; // Each correct = +10 points
    }

    public void addIncorrectAnswer() {
        // no score change
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }
}
