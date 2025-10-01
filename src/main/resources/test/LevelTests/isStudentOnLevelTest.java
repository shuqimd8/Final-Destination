package LevelTests;
import Model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


public class isStudentOnLevelTest {
    //Cleary define the values that matter
    private static final int MIN_SCORE = 50;
    private static final int MAX_SCORE = 100;
    //create a test Level using the above values (other variables don't matter for the tests)
    private Level level;
    //create a test student
    private Student student;

    @BeforeEach
    public void setUp(){
        //create a test student
        student = new Student("Billy32", "BillyIsCool", "Billy", "msDaisy");
        //create a test Level
        level = new Level("Worker Ant", MIN_SCORE, MAX_SCORE,"/this/image/path");

    }

    //student at min value, and hence function should return true
    @Test
    public void isStudentOnLevelTest_StudentScoreAtMinLevelValue(){
        student.setTotalScore(MIN_SCORE);
        assertTrue(level.isStudentOnLevel(student));
    }
    //student at max, and hence function should return true
    @Test
    public void isStudentOnLevelTest_StudentScoreAtMaxLevelValue(){
        student.setTotalScore(MAX_SCORE);
        assertTrue(level.isStudentOnLevel(student));
    }
    //student within range, and hence function should return true
    @Test
    public void isStudentOnLevelTest_StudentScoreWithinLevelRange(){
        student.setTotalScore(MIN_SCORE + 1);
        assertTrue(level.isStudentOnLevel(student));
    }
    //student below range, and hence function should return false
    @Test
    public void isStudentOnLevelTest_StudentScoreBelowMinLevelRange(){
        student.setTotalScore(MIN_SCORE-1);
        assertFalse(level.isStudentOnLevel(student));
    }
    //student above range, and hence function should return false
    @Test
    public void isStudentOnLevelTest_StudentScoreAboveMaxLevelRange(){
        student.setTotalScore(MAX_SCORE+1);
        assertFalse(level.isStudentOnLevel(student));
    }
}
