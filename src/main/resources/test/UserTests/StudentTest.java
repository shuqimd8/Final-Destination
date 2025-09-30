package UserTests;
import Model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {
    private static final String FIRST_NAME = "James";
    private static final String FIRST_NAME_TWO = "Jamie";
    private static final String USERNAME = "JBoy66";
    private static final String PASSWORD = "CoolMan123";
    private static final String PASSWORD_TWO = "CoolBoy321";
    private static final String TEACHER = "msDaisy";
    private static final String TEACHER_TWO = "mrColby";
    private static final int TOTAL_SCORE = 10;
    private static final int TOTAL_SCORE_TWO = 20;

    private Student student;

    @BeforeEach
    public void setUp(){
        //create the test student
        student = new Student(USERNAME, PASSWORD, FIRST_NAME, TEACHER);
    }

    @Test
    public void testGetFirstName(){
        assertEquals(FIRST_NAME, student.getFirstName());
    }

    @Test
    public void testSetFirstName(){
        student.setFirstName(FIRST_NAME_TWO);
        assertEquals(FIRST_NAME_TWO, student.getFirstName());
    }

    @Test
    public void testGetUsername(){
        assertEquals(USERNAME, student.getUsername());
    }

    @Test
    public void testGetPassword(){
        assertEquals(PASSWORD, student.getPassword());
    }

    @Test
    public void testSetPassword(){
        student.setPassword(PASSWORD_TWO);
        assertEquals(PASSWORD_TWO, student.getPassword());
    }

    @Test
    public void testGetTeacher(){
        assertEquals(TEACHER, student.getTeacher());
    }

    @Test
    public void testSetTeacher(){
        student.setTeacher(TEACHER_TWO);
        assertEquals(TEACHER_TWO, student.getTeacher());
    }

    @Test
    public void testGetTotalScore(){
        assertEquals(TOTAL_SCORE,student.getTotalScore());
    }

    @Test
    public void testSetTotalScore(){
        student.setTotalScore(TOTAL_SCORE_TWO);
        assertEquals(TOTAL_SCORE_TWO,student.getTotalScore());
    }


    @Test
    public void testDoPasswordsMatch_True(){
        assertTrue(student.doPasswordsMatch(PASSWORD,PASSWORD));
    }

    @Test
    public void testDoPasswordsMatch_False(){
        assertFalse(student.doPasswordsMatch(PASSWORD,PASSWORD_TWO));
    }
}
