package UserTests;
import Model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


public class IsPasswordValidTests {
    //at least 8 letters, 1 capital, 1 lowercase, and 1 number
    public static String PASSWORD_VALID = "Abcdefgh1";
    //invalid letter length
    public static String PASSWORD_INVALID_LETTER_LEN = "Abc1";
    //invalid capital
    public static String PASSWORD_INVALID_NO_CAPITAL = "abcdefgh1";
    //invalid lower
    public static String PASSWORD_INVALID_NO_LOWERCASE = "ABCDEFGH1";
    //invalid number
    public static String PASSWORD_INVALID_NO_NUMBER = "Abcdefgh";

    private Student student;
    @BeforeEach
    public void setUp(){
        student = new Student("JBoy66", "CoolMan123", "James","msDaisy");
    }

    @Test
    public void testIsPasswordValid_ValidInput(){
        assertTrue(student.isPasswordValid(PASSWORD_VALID));

    }

    @Test
    public void testIsPasswordValid_LessThan8Letters(){
        assertFalse(student.isPasswordValid(PASSWORD_INVALID_LETTER_LEN));
    }

    @Test
    public void testIsPasswordValid_NoCapital(){
        assertFalse(student.isPasswordValid(PASSWORD_INVALID_NO_CAPITAL));
    }

    @Test
    public void testIsPasswordValid_NoLowerCase(){
        assertFalse(student.isPasswordValid(PASSWORD_INVALID_NO_LOWERCASE));
    }

    @Test
    public void testIsPasswordValid_NoNumber(){
        assertFalse(student.isPasswordValid(PASSWORD_INVALID_NO_NUMBER));
    }
}
