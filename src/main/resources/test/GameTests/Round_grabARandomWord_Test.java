package GameTests;

import Model.Game;
import Model.Round;
import Model.Word;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class Round_grabARandomWord_Test {
    //define variable to create game parent class
    public static final File BUCKET_FILE = new File("src/main/resources/test/GameTests/BucketDB");
    public static final File WORD_FILE = new File("src/main/resources/test/GameTests/WordDB");
    public static final int GAME_ID = 1;
    public static final String GAME_NAME = "NATURE";
    //establish parent class
    public static final Game game = new Game(GAME_ID,GAME_NAME,BUCKET_FILE, WORD_FILE);

    //variable to test calling information from the txt file
    public static final String TEXT_FILE_LINE = "111-cat-11";
    public static final int WORD_ID_FROM_TEXT_FILE_LINE = 111;
    public static final String WORD_FROM_TEXT_FILE_LINE = "cat";
    public static final int BUCKET_ID_FROM_TEXT_FILE_LINE = 11;

    public static final Word WORD = new Word(WORD_ID_FROM_TEXT_FILE_LINE, WORD_FROM_TEXT_FILE_LINE, BUCKET_ID_FROM_TEXT_FILE_LINE);

    public static final String TEXT_FILE_LINE_WORD_NOT_FOR_GAME = "162-chair-23";

    private Round round;

    @BeforeEach
    public void setUp(){
        round = new Round(game);
    }


    //+ grabARandomWord(): Word


    //+ extractWordID(String TextFileLine):int
    @Test
    public void round_extractWordID_test(){
        assertEquals(WORD_ID_FROM_TEXT_FILE_LINE, round.extractWordID(TEXT_FILE_LINE));
    }

    //+ extractWord(String TextFileLine): String
    @Test
    public void round_extractWord_test(){
        assertEquals(WORD_FROM_TEXT_FILE_LINE, round.extractWord(TEXT_FILE_LINE));
    }
    //+ extractBucketIDForWord(String TextFileLine): int
    @Test
    public void round_extractBucketIDForWord_test(){
        assertEquals(BUCKET_ID_FROM_TEXT_FILE_LINE, round.extractBucketIDForWord(TEXT_FILE_LINE));
    }

    //+ isWordForGame(String textfileline): boolean
    //yes for game
    @Test
    public void round_isWordForGame_WordIsForGameTest(){
        assertTrue(round.isWordForGame(TEXT_FILE_LINE));
    }
    //not for game
    @Test
    public void round_isWordForGame_WordIsNotGameTest(){
        assertFalse(round.isWordForGame(TEXT_FILE_LINE_WORD_NOT_FOR_GAME));
    }
}
