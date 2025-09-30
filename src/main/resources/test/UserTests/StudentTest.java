package UserTests;

import learneria.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {
    private static String FIRST_NAME = "James";
    private static String FIRST_NAME_TWO = "Jamie";
    private static String USERNAME = "JBoy66";
    private static String PASSWORD = "CoolMan123";
    private static String PASSWORD_TWO = "CoolBoy321";
    private static String TEACHER = "msDaisy";
    private static String TEACHER_TWO = "mrColby";

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
    public void testDoPasswordsMatch_True(){
        assertTrue(student.doPasswordsMatch(PASSWORD,PASSWORD));
    }

    @Test
    public void testDoPasswordsMatch_False(){
        assertFalse(student.doPasswordsMatch(PASSWORD,PASSWORD_TWO));
    }
}
