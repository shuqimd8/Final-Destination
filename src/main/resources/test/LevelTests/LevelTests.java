package LevelTests;

import Model.Level;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LevelTests {
    private static String LEVEL_NAME = "Spelling Bee";
    private static String LEVEL_NAME_TWO = "Worker Ant";
    private static int MIN_SCORE = 0;
    private static int MIN_SCORE_TWO = 50;
    private static int MAX_SCORE = 50;
    private static int MAX_SCORE_TWO = 100;
    private static String IMAGE_PATH = "/this/image/path";
    private static String IMAGE_PATH_TWO = "/new/image/path";

    private Level level;

    @BeforeEach
    public void setUp(){
        //Create the test Level
        level = new Level(LEVEL_NAME, MIN_SCORE, MAX_SCORE, IMAGE_PATH);
    }

    @Test
    public void getLevelNameTest(){assertEquals(LEVEL_NAME, level.getLevelName());}


    @Test
    public void setLevelNameTest(){
        level.setLevelName(LEVEL_NAME_TWO);
        assertEquals(LEVEL_NAME, level.getLevelName());
    }

    @Test
    public void getMinScoreTest(){assertEquals(MIN_SCORE, level.getMinScore());}

    @Test
    public void setMinScoreTest(){
        level.setMinScore(MIN_SCORE_TWO);
        assertEquals(MIN_SCORE_TWO, level.getMinScore());
    }

    @Test
    public void getMaxScoreTest(){assertEquals(MAX_SCORE, level.getMaxScore());}

    @Test
    public void setMaxScoreTest(){
        level.setMAXScore(MAX_SCORE_TWO);
        assertEquals(MAX_SCORE_TWO, level.getMaxScore());
    }

    @Test
    public void getImagePathTest(){assertEquals(IMAGE_PATH, level.getImagePath());}

    @Test
    public void setImagePathTest(){
        level.setImagePath(IMAGE_PATH_TWO);
        assertEquals(IMAGE_PATH_TWO, level.getImagePath());
    }











}
