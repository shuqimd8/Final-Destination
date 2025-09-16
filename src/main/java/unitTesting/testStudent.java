package unitTesting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class testStudent {
    public static void addTest() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/?user=root",
                    "root",
                    "CompSci2004%"
            );

            // Add test student
            String addStudentToDB = "INSERT INTO `final_destination`.`Students`(`username`, `hashed_password`, `teacher`, `f_name`) VALUES ('Test01', 'TestPass25', 1, 'Tester')";
            stmt = conn.prepareStatement(addStudentToDB);

            stmt.executeUpdate(addStudentToDB);

            System.out.println("Created tests");
        } catch (Exception e) {
            System.out.println("Exception: " + e + "has occurred.");
        }
    }
    public static void checkStudentTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/?user=root",
                    "root",
                    "CompSci2004%"
            );

            // Add test teacher
            String getStudentTable = "SELECT * FROM `final_destination`.`Students`";
            stmt = conn.prepareStatement(getStudentTable);

            stmt.executeQuery(getStudentTable);

            System.out.println("Table found");
        } catch (Exception e) {
            System.out.println("Exception: " + e + "has occurred.");
        }
    }
}
