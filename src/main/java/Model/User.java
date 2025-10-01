package Model;

import java.util.*;

/**
 * A class for all users
 *
 * @author Olivia Greensill
 */
public class User {
    private String Username;
    private String Password;
    private String FirstName;

//    public User (String Username) {
//        Username = username;
//    }

    /**
     * A method to receive the login details and run the method to verify the information
     */
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

    /**
     * A method to check whether the inputted username already exists in the database
     *
     * @param username The username that a new user has inputted
     *
     * @return true if the username does not already exist
     * @return false if the username already exists in the database
     */
    public static boolean isUsernameUnique(String username) {
        MySQL database = new MySQL();
        List<String> studentDetails = (database.getStudentFromDB(database.establishConnection(), username));

        if (studentDetails != null) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * A method to check whether the password matches the constraints
     *
     * @param password The password that a new user has inputted
     *
     * @return true if the password is valid
     * @return false if the password is invalid
     */
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

    /**
     * A method to check if an inputted password is the same as another password (either inputted or in the database)
     * @param password The password that the user has inputted
     * @param password1 The second password that will be compared
     * @return true if the passwords match
     * @return false if the passwords do not match
     */
    public static boolean doPasswordsMatch(String password, String password1) {
        return (password.equals(password1));
    }

    /**
     * The method to find the data for the user and verify that the inputted data is correct
     *
     * @param username The inputted username
     * @param password The inputted password
     * @throws LoginFailedException
     */
    public static void userLogin(String username, String password) {
        MySQL database = new MySQL();
        List<String> studentDetails = (database.getStudentFromDB(database.establishConnection(), username));
        List<String> teacherDetails = (database.getTeacherFromDB(database.establishConnection(), username));

        if (studentDetails != null) {
            if (doPasswordsMatch(studentDetails.get(1), password)) {
                //success
            }
            else {
                throw new LoginFailedException("Login failed, please check that your details are correct;");
            }
        }
        else {
            if (teacherDetails != null) {
                if (doPasswordsMatch(teacherDetails.get(1), password)) {
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
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String NameInput) {
        FirstName = NameInput;
    }

    public String getUsername() {return "";//dummy
    }

    public String getPassword() {return "";//dummy
    }

    public void setPassword(String passwordInput) {
        Password = passwordInput;
    }
}
