import com.learneria.utils.GameSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSystemTest {

    private GameSystem game;

    @BeforeEach
    void setUp() {
        game = new GameSystem("Grammar");
    }

    @Test
    void testStartGameInitializesAllValues() {
        game.startGame();
        assertEquals(0, game.getScore());
        assertEquals(0, game.getCorrectCount());
        assertEquals(0, game.getIncorrectCount());
        assertEquals(1, game.getRoundCount(), "Round count should start at 1");
    }

    @Test
    void testEvaluateAnswerCorrectIncreasesScore() {
        game.startGame();
        game.evaluateAnswer("dog", "dog");
        assertEquals(10, game.getScore(), "Correct answer should add 10 points");
        assertEquals(1, game.getCorrectCount());
        assertEquals(0, game.getIncorrectCount());
    }

    @Test
    void testEvaluateAnswerIncorrectDecreasesScore() {
        game.startGame();
        game.evaluateAnswer("cat", "dog");
        assertEquals(-5, game.getScore(), "Incorrect answer should subtract 5 points");
        assertEquals(0, game.getCorrectCount());
        assertEquals(1, game.getIncorrectCount());
    }

    @Test
    void testNextRoundIncrementsRoundCount() {
        game.startGame();
        int before = game.getRoundCount();
        game.nextRound();
        assertEquals(before + 1, game.getRoundCount());
    }

    @Test
    void testAverageSpeedReturnsPositiveAfterEvaluations() throws InterruptedException {
        game.startGame();
        game.evaluateAnswer("dog", "dog");
        Thread.sleep(10); // simulate reaction delay
        game.nextRound();
        game.evaluateAnswer("cat", "dog");
        double avg = game.getAverageSpeed();
        assertTrue(avg >= 0, "Average speed should be non-negative");
    }

    @Test
    void testCategoryGetter() {
        assertEquals("Grammar", game.getCategory());
    }

    @Test
    void testRandomFeedbackReturnsValidString() {
        String feedback = GameSystem.getRandomFeedback();
        assertNotNull(feedback);
        assertFalse(feedback.isBlank());
    }

    @Test
    void testMultipleRoundsAndScoringConsistency() {
        game.startGame();
        game.evaluateAnswer("A", "A");
        game.nextRound();
        game.evaluateAnswer("B", "C");
        game.nextRound();
        assertEquals(3, game.getRoundCount(), "Round count should start at 1 and increment each nextRound()");
        assertEquals(5, game.getScore(), "10 - 5 = 5 after one correct and one incorrect");
        assertEquals(1, game.getCorrectCount());
        assertEquals(1, game.getIncorrectCount());
    }
}
