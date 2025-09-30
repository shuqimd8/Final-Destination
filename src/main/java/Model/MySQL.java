package Model;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MySQL {

    // Check connection to the database
    public static Connection establishConnection() {
        Connection c = null;
        try {
            c = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/?user=root",
                    "root",
                    "CompSci2004%"
            );
        } catch( Exception e ) {
            throw new DatabaseConnectionFailedException("Could not establish connection");
        }
        System.out.print("Connected");
        return c;
    }

    public static List<String> getStudentFromDB(Connection conn, String username) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> userData = new List<String>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NotNull
            @Override
            public Iterator<String> iterator() {
                return null;
            }

            @NotNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NotNull
            @Override
            public <T> T[] toArray(@NotNull T[] a) {
                return null;
            }

            @Override
            public boolean add(String s) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NotNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NotNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NotNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public String get(int index) {
                return "";
            }

            @Override
            public String set(int index, String element) {
                return "";
            }

            @Override
            public void add(int index, String element) {

            }

            @Override
            public String remove(int index) {
                return "";
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @NotNull
            @Override
            public ListIterator<String> listIterator() {
                return null;
            }

            @NotNull
            @Override
            public ListIterator<String> listIterator(int index) {
                return null;
            }

            @NotNull
            @Override
            public List<String> subList(int fromIndex, int toIndex) {
                return List.of();
            }
        };
        try {
            String checkForUsername = "SELECT * FROM `final_destination`.`Students` WHERE username = ?";
            pstmt = conn.prepareStatement(checkForUsername);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                for (int i = 1; i <= (columnCount-1); i++) {
                    userData.add(rs.getString(i));
                }
            } else {
                throw new UserNotFoundException("User not found");
            }
        }
        catch (Exception e) {
            System.out.println("Exception: " + e + "has occurred.");
        }
        return userData;
    }

    public static List<String> getTeacherFromDB(Connection conn, String username) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> userData = new List<String>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NotNull
            @Override
            public Iterator<String> iterator() {
                return null;
            }

            @NotNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NotNull
            @Override
            public <T> T[] toArray(@NotNull T[] a) {
                return null;
            }

            @Override
            public boolean add(String s) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NotNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NotNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NotNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public String get(int index) {
                return "";
            }

            @Override
            public String set(int index, String element) {
                return "";
            }

            @Override
            public void add(int index, String element) {

            }

            @Override
            public String remove(int index) {
                return "";
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @NotNull
            @Override
            public ListIterator<String> listIterator() {
                return null;
            }

            @NotNull
            @Override
            public ListIterator<String> listIterator(int index) {
                return null;
            }

            @NotNull
            @Override
            public List<String> subList(int fromIndex, int toIndex) {
                return List.of();
            }
        };
        try {
            String checkForUsername = "SELECT * FROM `final_destination`.`teachers` WHERE username = ?";
            pstmt = conn.prepareStatement(checkForUsername);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                for (int i = 1; i <= (columnCount-1); i++) {
                    userData.add(rs.getString(i));
                }
            } else {
                throw new UserNotFoundException("User not found");
            }
        }
        catch (Exception e) {
            System.out.println("Exception: " + e + "has occurred.");
        }
        return userData;
    }

    public static List<String> getStatsFromDB(Connection conn, String username) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> userData = new List<String>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NotNull
            @Override
            public Iterator<String> iterator() {
                return null;
            }

            @NotNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NotNull
            @Override
            public <T> T[] toArray(@NotNull T[] a) {
                return null;
            }

            @Override
            public boolean add(String s) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NotNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NotNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NotNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public String get(int index) {
                return "";
            }

            @Override
            public String set(int index, String element) {
                return "";
            }

            @Override
            public void add(int index, String element) {

            }

            @Override
            public String remove(int index) {
                return "";
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @NotNull
            @Override
            public ListIterator<String> listIterator() {
                return null;
            }

            @NotNull
            @Override
            public ListIterator<String> listIterator(int index) {
                return null;
            }

            @NotNull
            @Override
            public List<String> subList(int fromIndex, int toIndex) {
                return List.of();
            }
        };
        try {
            String checkForUsername = "SELECT * FROM `final_destination`.`student_statistics` WHERE student_user = ?";
            pstmt = conn.prepareStatement(checkForUsername);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                for (int i = 1; i <= (columnCount-1); i++) {
                    userData.add(rs.getString(i));
                }
            } else {
                throw new UserNotFoundException("User not found");
            }
        }
        catch (Exception e) {
            System.out.println("Exception: " + e + "has occurred.");
        }
        return userData;
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
