package GameTests;
import Model.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Collections;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    //define variable to create game class
    public static final File BUCKET_FILE = new File("BucketDB.txt");
    public static final int GAME_ID = 1;

    //variable to test calling information from the txt file
    public static final String TEXT_FILE_LINE = "11-Noun-1<<imagepath1>>";
    public static final int BUCKET_ID_FROM_TEXT_FILE_LINE = 11;
    public static final String BUCKET_NAME_FROM_TEXT_FILE_LINE = "Noun";
    public static final String BUCKET_IMAGEPATH_FROM_TEXT_FILE_LINE = "imagepath1";

    //make buckets for the game 1 buckets in BucketDB.txt
    public static final Bucket BUCKET_1 = new Bucket(11,"Noun","imagepath1");
    public static final Bucket BUCKET_2 = new Bucket(12,"Noun","imagepath2");
    public static final Bucket BUCKET_3 = new Bucket(13,"Noun","imagepath3");

    //make a list for the buckets
    public static Bucket[] EXPECTED_BUCKET_LIST = {BUCKET_1, BUCKET_2, BUCKET_3};

    private Game game;

    @BeforeEach
    public void setUp(){
        //create the test game using the variable defined above
        game = new Game(GAME_ID,BUCKET_FILE);
    }

    @Test
    public void game_getGameID_test(){
        assertEquals(GAME_ID, game.getGameID());
    }

    @Test
    public void game_getBucketFile_test(){
        assertEquals(BUCKET_FILE, game.getBucketFile());
    }

    @Test
    public void game_getBuckets_test(){
        assertEquals(EXPECTED_BUCKET_LIST, game.getBucketList());
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
    public void game_extractBucketImagePath_test(){
        assertEquals(BUCKET_IMAGEPATH_FROM_TEXT_FILE_LINE, game.extractBucketImagePath(TEXT_FILE_LINE));
    }

}
