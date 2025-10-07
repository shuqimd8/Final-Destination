package DatabaseTests;

import Model.MySQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class InsertTest {
    String studentusername = "TestStudent24";
    String studentpassword = "passWord2025";
    String studentname = "Will";

    int teacherUsername = 1;
    String teacherpassword = "passWord20";
    String teacherFname = "Jimmy";
    String teacherLname = "Jones";

    @Test
    public void InsertStudentTest() throws SQLException {
        try {
            MySQL.AddStudentToDB(MySQL.establishConnection(), studentusername, studentpassword, studentname, teacherUsername);
        }
        catch (Exception e) {
            throw new SQLException();
        }
    }

    @Test
    public void InsertTeacherTest() throws SQLException {
        try {
            MySQL.AddTeacherToDB(MySQL.establishConnection(), teacherUsername, teacherpassword, teacherFname, teacherLname);
        }
        catch (Exception e) {
            throw new SQLException();
        }
    }
}
