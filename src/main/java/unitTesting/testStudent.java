package unitTesting;

import learneria.Student;
import learneria.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class testStudent {

    User userClass = new User();
    Student studentClass = new Student();
    String username = "username123";
    String validPassword = "PassWord25";
    String invalidPassword = "pass";

    public void addTestStudent() {
        studentClass.addStudent(username, validPassword, 1, "Test");
    }
    public boolean catchDuplicateUsername() {
        if (!userClass.isUsernameUnique(username)) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean addValidPassword() {
        return userClass.isPasswordValid(validPassword);
    }
    public boolean catchInvalidPassword() {
        try {
            userClass.isPasswordValid(invalidPassword);
        } catch (Exception e) {
            return true;
        }
        return false;
    }
    public boolean testValidLogin() {
        try {
            userClass.userLogin(username, validPassword);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean catchInvalidLogin() {
        try {
            userClass.userLogin(username, invalidPassword);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    public boolean studentTestsCorrect() {
        addTestStudent();
        if (catchDuplicateUsername()) {
            if (addValidPassword()) {
                if (catchInvalidPassword()) {
                    if (testValidLogin()) {
                        if (catchInvalidLogin()) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                    else {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
}
