package GameTests;
import Model.Game;
import Model.Bucket;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    //define variable to create game class
    public static final File BUCKET_FILE = new File("src/main/resources/test/GameTests/BucketDB");
    public static final File WORD_FILE = new File("src/main/resources/test/GameTests/WordDB");
    public static final int GAME_ID = 1;
    public static final String GAME_NAME = "NATURE";

    //variable to test calling information from the txt file
    public static final String TEXT_FILE_LINE = "11-Noun-1<<imagepath1>>";
    public static final int BUCKET_ID_FROM_TEXT_FILE_LINE = 11;
    public static final String BUCKET_NAME_FROM_TEXT_FILE_LINE = "Noun";
    public static final String BUCKET_IMAGEPATH_FROM_TEXT_FILE_LINE = "imagepath1";

    public static final int BUCKET_ID_NOT_IN_GAME = 21;

    //make buckets for the game 1 buckets in BucketDB.txt
    public static final Bucket BUCKET_1 = new Bucket(11,"Noun",1, "imagepath1");
    public static final Bucket BUCKET_2 = new Bucket(12,"Verb",1, "imagepath2");
    public static final Bucket BUCKET_3 = new Bucket(13,"Adjective",1, "imagepath3");

    //make a list for the buckets
    public static List<Bucket> EXPECTED_BUCKET_LIST;


    private static Game game;

    @BeforeEach
    public void setUp(){
        //create the test game using the variable defined above
        game = new Game(GAME_ID,GAME_NAME,BUCKET_FILE, WORD_FILE);

        //add buckets to expected bucket list output
        //turn to umodifiable list
        EXPECTED_BUCKET_LIST = List.of(BUCKET_1, BUCKET_2, BUCKET_3);

    }

    @Test
    public void game_getGameID_test(){
        assertEquals(GAME_ID, game.getGameID());
    }

    @Test
    public void game_getGameName_test(){
        assertEquals(GAME_NAME, game.getGameName());

    }

    @Test
    public void game_getBucketFile_test(){
        assertEquals(BUCKET_FILE, game.getBucketFile());
    }

    @Test
    public void game_getWordFile_test(){
        assertEquals(WORD_FILE, game.getWordFile());
    }

    @Test
    public void game_getBuckets_test(){
        assertEquals(EXPECTED_BUCKET_LIST, game.getBuckets());
    }

    @Test
    public void game_extractBucketID_test(){
        assertEquals(BUCKET_ID_FROM_TEXT_FILE_LINE, game.extractBucketID(TEXT_FILE_LINE));
    }

    @Test
    public void game_extractBucketName_test(){
        assertEquals(BUCKET_NAME_FROM_TEXT_FILE_LINE, game.extractBucketName(TEXT_FILE_LINE));
    }

    @Test
    public void game_extractGameID_test(){
        assertEquals(GAME_ID, game.extractGameID(TEXT_FILE_LINE));

    }

    @Test
    public void game_extractBucketImagePath_test(){
        assertEquals(BUCKET_IMAGEPATH_FROM_TEXT_FILE_LINE, game.extractBucketImagePath(TEXT_FILE_LINE));
    }

    @Test
    public void game_createBucket_test(){
        assertEquals(BUCKET_1.toString(), game.createBucket(TEXT_FILE_LINE).toString());
    }

    //isBucketInGame(bucketID): boolean - true
    @Test
    public void game_IsBucketInGame_BucketInGameTest(){
        assertTrue(game.isBucketInGame(BUCKET_ID_FROM_TEXT_FILE_LINE));
    }
    //isBucketInGame(bucketID): boolean - false
    @Test
    public void game_IsBucketInGame_BucketNOTinGameTest(){
        assertFalse(game.isBucketInGame(BUCKET_ID_NOT_IN_GAME));
    }


}
