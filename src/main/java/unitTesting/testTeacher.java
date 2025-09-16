package unitTesting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class testTeacher {
    public static void addTest() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/?user=root",
                    "root",
                    "CompSci2004%"
            );

            // Add test teacher
            String addTeacherToDB = "INSERT INTO `final_destination`.`teachers`(`teacher_ID`, `hashed_password`, `f_name`, `l_name`) VALUES ('1', 'TestPass20', 'Mister', 'Tester')";
            stmt = conn.prepareStatement(addTeacherToDB);

            stmt.executeUpdate(addTeacherToDB);

            System.out.println("Created tests");
        } catch (Exception e) {
            System.out.println("Exception: " + e + "has occurred.");
        }
    }
    public static void checkTeacherTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/?user=root",
                    "root",
                    "CompSci2004%"
            );

            // Add test teacher
            String getTeacherTable = "SELECT * FROM `final_destination`.`teachers`";
            stmt = conn.prepareStatement(getTeacherTable);

            stmt.executeQuery(getTeacherTable);

            System.out.println("Table found");
        } catch (Exception e) {
            System.out.println("Exception: " + e + "has occurred.");
        }
    }
}
