package learneria;

import java.sql.*;

public class Teacher {
    String TeacherUsername;
    String TeacherName;
    String TeacherPassword;

    public Teacher(String user, String pass, String name) {
        TeacherUsername = user;
        TeacherPassword = pass;
        TeacherName = name;
    }

    public static void addTeacher(int username, String password, String name) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/?user=root",
                    "root",
                    "CompSci2004%"
            );

            String addTeacherToDB = "INSERT INTO `final_destination`.`teachers` VALUES (?, ?, ?)";

            pstmt = conn.prepareStatement(addTeacherToDB);

            pstmt.setInt(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, name);

            pstmt.executeUpdate();

            System.out.println("Created account");
        } catch (Exception e) {
            System.out.println("Exception: " + e + "has occurred.");
        }
    }

    public static void getTeachersStudents(int teacher) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/?user=root",
                    "root",
                    "CompSci2004%"
            );

            String getAllStudents = "SELECT * FROM `final_destination`.`Students` WHERE teacher = ?";
            stmt = conn.prepareStatement(getAllStudents);
            stmt.setInt(1, teacher);

            rs = stmt.executeQuery(getAllStudents);

            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();

            // Print column headers
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rsmd.getColumnName(i) + "\t");
            }
            System.out.println();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }


        } catch (Exception e) {
            System.out.println("Exception: " + e + "has occurred.");
        }
    }
}
