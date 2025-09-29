package unitTesting;

import learneria.Student;
import learneria.Teacher;
import learneria.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class testTeacher {
    User userClass = new User();
    Teacher teacherClass = new Teacher();
    int username = 1;
    String usernameStr = "1";
    String validPassword = "PassWord002";
    String invalidPassword = "pass";

    public void addTestTeacher() {
        teacherClass.addTeacher(username, validPassword, "TestTeacher");
    }
    public boolean catchDuplicateUsername() {
        if (!userClass.checkUsernameNotInDB(usernameStr)) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean addValidPassword() {
        return userClass.checkPassword(validPassword);
    }
    public boolean catchInvalidPassword() {
        try {
            userClass.checkPassword(invalidPassword);
        } catch (Exception e) {
            return true;
        }
        return false;
    }
    public boolean testValidLogin() {
        try {
            userClass.userLogin(usernameStr, validPassword);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean catchInvalidLogin() {
        try {
            userClass.userLogin(usernameStr, invalidPassword);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    public boolean teacherTestsCorrect() {
        addTestTeacher();
        if (catchDuplicateUsername()) {
            if (addValidPassword()) {
                if (catchInvalidPassword()) {
                    if (testValidLogin()) {
                        if (catchInvalidLogin()) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
