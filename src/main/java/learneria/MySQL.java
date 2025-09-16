package learneria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQL {

    // Check connection to the database
    public static void establishConnection() {
        Connection c = null;
        Statement stmt = null;
        try {
            c = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/?user=root",
                    "root",
                    "CompSci2004%"
            );
        } catch( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    // Add two test users (teacher and student)
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

            // Add test student
            String addStudentToDB = "INSERT INTO `final_destination`.`Students`(`username`, `hashed_password`, `teacher`, `f_name`) VALUES ('Test01', 'TestPass25', 1, 'Tester')";
            stmt = conn.prepareStatement(addStudentToDB);

            stmt.executeUpdate(addStudentToDB);

            System.out.println("Created tests");
        } catch (Exception e) {
            System.out.println("Exception: " + e + "has occurred.");
        }
    }
}
