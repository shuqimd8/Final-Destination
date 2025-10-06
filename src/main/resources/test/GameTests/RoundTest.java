package GameTests;
import Model.Game;
import Model.Round;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Collections;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class RoundTest {
    //define variable to create game parent class
    public static final File BUCKET_FILE = new File("src/main/resources/test/GameTests/BucketDB");
    public static final File WORD_FILE = new File("src/main/resources/test/GameTests/WordDB");
    public static final int GAME_ID = 1;
    public static final String GAME_NAME = "NATURE";
    //establish parent class
    public static final Game game = new Game(GAME_ID,GAME_NAME,BUCKET_FILE, WORD_FILE);

    //create test value for round class
    public static final int SCORE = 20;
    public static final int NO_CORRECT_WORDS = 4;
    public static final int NO_INCORRECT_WORDS = 5;
    public static final int NO_WORDS_DISPLAYED = 9;

    //to test add to score ->
    public static final int ADD_TO_SCORE = 5;
    public static final int NEW_SCORE = SCORE + ADD_TO_SCORE;

    //remenmber this class extends game
    private Round round;

    @BeforeEach
    public void setUp(){
        //set up round for tests
        round = new Round(game);
    }

    //score starts at 0
    @Test
    public void round_ScoreStartsAt0_test(){
        assertEquals(0, round.getScore());
    }
    //no correct words starts at 0
    @Test
    public void round_NoCorrectWordsStartsAt0_test(){
        assertEquals(0, round.getNoCorrectWords());
    }
    //no incorrect words starts at 0
    @Test
    public void round_NoIncorrectWordsStartsAt0_test(){
        assertEquals(0, round.getNoIncorrectWords());
    }
    //no words start at 0
    @Test
    public void round_NoWordsDisplayedStartsAt0_test(){
        assertEquals(0, round.getNoWordsDisplayed());
    }

    //+ getScore(): int
    @Test
    public void round_getScore_test(){
        assertEquals(0, round.getScore());
    }
    //+ setScore(int): int
    @Test
    public void round_setScore_test(){
        round.setScore(SCORE);
        assertEquals(SCORE, round.getScore());
    }
    //+ addToScore(int):void
    @Test
    public void round_addToScore_test(){
        round.setScore(SCORE);
        round.addToScore(ADD_TO_SCORE);
        assertEquals(NEW_SCORE, round.getScore());
    }
    //+ getNoCorrectWords():int
    @Test
    public void round_getNoCorrectWords_test(){
        assertEquals(0, round.getNoCorrectWords());
    }
    //+ setNoCorrectWords(int): void
    @Test
    public void round_setNoCorrectWords_test(){
        round.setNoCorrectWords(NO_CORRECT_WORDS);
        assertEquals(NO_CORRECT_WORDS, round.getNoCorrectWords());
    }
    //+ getNoIncorrectWords(): int
    @Test
    public void round_getNoIncorrectWords_test(){
        assertEquals(0, round.getNoIncorrectWords());
    }
    //+ setNoIncorrectWords(int): void
    @Test
    public void round_setNoIncorrectWords_test(){
        round.setNoIncorrectWords(NO_INCORRECT_WORDS);
        assertEquals(NO_INCORRECT_WORDS,round.getNoIncorrectWords());
    }

    //+ getNoWordsDisplayed():int
    @Test
    public void round_getNoWordsDisaplyed_test(){
        assertEquals(0, round.getNoWordsDisplayed());
    }
    //+ setNoWordsDisplayed(int):void
    @Test
    public void round_setNoWordsDisaplyed_test(){
        round.setNoWordsDisplayed(NO_WORDS_DISPLAYED);
        assertEquals(NO_WORDS_DISPLAYED, round.getNoWordsDisplayed());
    }



    //+ calculateScore(noCorrectWords): int
    @Test
    public void round_calculateScore_test(){

    }


}
