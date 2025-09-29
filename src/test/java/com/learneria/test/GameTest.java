package com.learneria.test;

import com.learneria.utils.GameLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {

    private GameLogic game;

    @BeforeEach
    void setUp() {
        game = new GameLogic();
    }

    @Test
    void testScoreIncrementsOnCorrectAnswer() {
        game.addCorrectAnswer();
        assertEquals(10, game.getScore(), "Score should be 10 after one correct answer");
    }

    @Test
    void testScoreDoesNotIncrementOnIncorrectAnswer() {
        game.addIncorrectAnswer();
        assertEquals(0, game.getScore(), "Score should remain 0 after incorrect answer");
    }

    @Test
    void testMultipleCorrectAnswers() {
        game.addCorrectAnswer();
        game.addCorrectAnswer();
        game.addCorrectAnswer();
        assertEquals(30, game.getScore(), "Score should be 30 after three correct answers");
    }

    @Test
    void testResetScore() {
        game.addCorrectAnswer();
        game.addCorrectAnswer();
        game.reset();
        assertEquals(0, game.getScore(), "Score should reset to 0");
    }
}
