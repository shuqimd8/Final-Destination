package learneria;

import learneria.Student ;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;

public class User {
    private String Username;
    private String Password;

//    public User (String username, String password) {
//        Username = username;
//        Password = password;
//    }

    public static void getLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String usernameInput = scanner.nextLine();
        System.out.print("Enter your password: ");
        String passwordInput = scanner.nextLine();
        scanner.close();

        Student.studentLogin(usernameInput, passwordInput);
    }

    public static String getUserType() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you a 1. student or a 2. teacher");
        String userTypeInput = scanner.nextLine();
        int userType = Integer.parseInt(userTypeInput);
        scanner.close();

        if (userType == 1) {
            return "student";
        }
        else {
            return "teacher";
        }
    }

    public static void CreateNewStudent() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String nameInput = scanner.nextLine();

        String firstLetter = nameInput.substring(0, 1).toUpperCase();
        String restOfString = nameInput.substring(1);
        String name = firstLetter + restOfString;

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        if (isUsernameUnique(username)) {
            System.out.print("Enter your password: ");
        }
        else {
//            System.out.print("Username taken");
            throw new DuplicateUsernameException("Username taken, please select another one.");
        }
        String password = scanner.nextLine();

        System.out.print("Re-enter your password: ");
        String pass2 = scanner.nextLine();

        if (password.equals(pass2)) {
            System.out.print("Enter your teachers user number: ");
        }
        else {
//            System.out.print("Passwords do not match, please try again.");
            throw new PasswordsDoNotMatchException("Passwords do not match, please try again.");
        }
        String teach = scanner.nextLine();

        int teacher = Integer.parseInt(teach);

        scanner.close();

        Student.addStudent(username, password, teacher, name);
    }

    public static void CreateNewTeacher() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String nameInput = scanner.nextLine();

        String firstLetter = nameInput.substring(0, 1).toUpperCase();
        String restOfString = nameInput.substring(1);
        String name = firstLetter + restOfString;

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        if (isUsernameUnique(username)) {
            System.out.print("Enter your password: ");
        }
        else {
            System.out.print("Username taken");
        }
        String password = scanner.nextLine();

        System.out.print("Re-enter your password: ");
        String pass2 = scanner.nextLine();

        if (!password.equals(pass2)) {
            System.out.print("Passwords do not match, please try again.");
        }

        scanner.close();

//        Teacher.addTeacher(username, password, name);
    }

    public static void getStudents() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/?user=root",
                    "root",
                    "CompSci2004%"
            );

            String getAllStudents = "SELECT * FROM `final_destination`.`Students`";
            stmt = conn.prepareStatement(getAllStudents);

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

    public static void getTeachers() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/?user=root",
                    "root",
                    "CompSci2004%"
            );

            String getAllTeachers = "SELECT * FROM `final_destination`.`teachers`";
            stmt = conn.prepareStatement(getAllTeachers);

            rs = stmt.executeQuery(getAllTeachers);

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

    public static boolean isUsernameUnique(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/?user=root",
                    "root",
                    "CompSci2004%"
            );

            String checkForUsername = "SELECT * FROM `final_destination`.`Students` WHERE username = ?";
            pstmt = conn.prepareStatement(checkForUsername);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return false;
            }
            else {
                return true;
            }
        }
        catch (Exception e) {
            System.out.println("Exception: " + e + "has occurred.");
            return false;
        }
    }
    public boolean isPasswordValid (String password) {
        int uppercase = 0;
        int lowercase = 0;
        int numbers = 0;
        if (password.length() >= 8) {
            for (int i = 0; i < password.length(); i++) {
                if (Character.isUpperCase(password.charAt(i))) {
                    uppercase++;
                }
                else if (Character.isLowerCase(password.charAt(i))) {
                    lowercase++;
                }
                else if (Character.isDigit(password.charAt(i))) {
                    numbers++;
                }

                if (uppercase > 0 && lowercase > 0 && numbers >0) {
                    return true;
                }

            }
            if (uppercase > 0 && lowercase > 0 && numbers >0) {
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
    public static boolean doPasswordsMatch(String password, String password1) {
        return (password.equals(password1));
    }
    public static void userLogin(String username, String password) {
        MySQL database = new MySQL();
        List<String> studentDetails = (database.getStudentFromDB(database.establishConnection(), username));

        if (studentDetails != null) {
            if (doPasswordsMatch(studentDetails.get(1), password)) {
                //success
            }
            else {
                throw new LoginFailedException("Login failed, please check that your details are correct;");
            }
        }
        else {
            throw new LoginFailedException("Login failed, please check that your details are correct;");
        }
    }
}
