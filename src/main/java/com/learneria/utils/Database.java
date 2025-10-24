package com.learneria.utils;

import java.sql.*;
import java.util.*;

/**
 * The {@code Database} class provides a singleton-based interface for all SQLite
 * interactions in Papa's Learneria. It handles user management, score tracking,
 * word retrieval, and teacher-classroom systems.
 * <p>
 * It also supports automatic creation of necessary tables and insertion of default
 * words for grammar, food, and nature games.
 * </p>
 * <p>
 * Extended features include teacher-class management, quiz records, and score analytics.
 * </p>
 */
public class Database {
    /** Singleton instance of the database. */
    private static Database instance;
    /** Active database connection object. */
    private Connection connection;
    /** Prevents multiple connection logs during initialization. */
    private static boolean connectionLogged = false;

    // ============================
    // INITIALISATION
    // ============================
    /**
     * Private constructor to initialize the SQLite database connection.
     * <p>
     * Ensures that the storage directory and database file exist,
     * creates tables if missing, and inserts default words when first run.
     * </p>
     *
     * @throws SQLException if database connection or table setup fails
     */
    private Database() throws SQLException {
        try {
            String homePath = System.getProperty("user.home") + "/learneria_data";
            java.io.File folder = new java.io.File(homePath);
            if (!folder.exists()) folder.mkdirs();

            String url = "jdbc:sqlite:" + homePath + "/learneria.db";
            connection = DriverManager.getConnection(url);

            if (!connectionLogged) {
                System.out.println(" Connected to database: " + url);
                connectionLogged = true;
            }

            createTablesIfMissing();
            ensureDefaultWords();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Returns the singleton instance of {@code Database}.
     * Reconnects automatically if the previous connection was closed.
     *
     * @return the single shared instance of Database
     * @throws SQLException if initialization or reconnection fails
     */
    public static synchronized Database getInstance() throws SQLException {
        if (instance == null) {
            instance = new Database();
        } else if (instance.connection == null || instance.connection.isClosed()) {
            instance.reconnect();
        }
        return instance;
    }
    /**
     * Reconnects to the SQLite database if the connection was lost.
     */
    private void reconnect() {
        try {
            String homePath = System.getProperty("user.home") + "/learneria_data";
            connection = DriverManager.getConnection("jdbc:sqlite:" + homePath + "/learneria.db");
            System.out.println(" Reconnected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the active {@link Connection} instance.
     * If the connection is closed or null, it will reconnect automatically.
     *
     * @return a valid SQL connection object
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) reconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Provides a shortcut static accessor for acquiring the current connection.
     *
     * @return a valid SQLite connection
     * @throws RuntimeException if initialization fails
     */
    public static Connection connect() {
        try {
            return getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Database not initialized properly.", e);
        }
    }

    // ============================
    // TABLE CREATION
    // ============================
    /**
     * Creates all required database tables if they do not already exist.
     * Includes users, scores, words, classes, and quizzes tables.
     * <p>
     * Also performs lightweight auto-migration to add new columns when needed.
     * </p>
     */
    private void createTablesIfMissing() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL,
                        role TEXT NOT NULL,
                        teacher_code TEXT,
                        name TEXT,
                        class_code TEXT
                    )
                    """);
        } catch (SQLException e) { e.printStackTrace(); }

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS scores (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT NOT NULL,
                        game TEXT NOT NULL,
                        score INTEGER NOT NULL,
                        correct INTEGER DEFAULT 0,
                        incorrect INTEGER DEFAULT 0,
                        avgSpeed REAL DEFAULT 0,
                        date_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
        } catch (SQLException e) { e.printStackTrace(); }

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS words (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        word TEXT NOT NULL,
                        category TEXT NOT NULL,
                        UNIQUE(word, category)
                    )
                    """);
        } catch (SQLException e) { e.printStackTrace(); }

        //  new for teacher classes and quizzes
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS classes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        class_name TEXT NOT NULL,
                        class_code TEXT UNIQUE NOT NULL,
                        teacher_username TEXT NOT NULL
                    )
                    """);
        } catch (SQLException e) { e.printStackTrace(); }

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS quizzes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        class_code TEXT NOT NULL,
                        title TEXT NOT NULL,
                        questions_json TEXT,
                        date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
        } catch (SQLException e) { e.printStackTrace(); }

// ============================
//  AUTO-MIGRATION  SECTION
// ============================
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("ALTER TABLE users ADD COLUMN subject_taught TEXT;");
            System.out.println(" Added missing column: subject_taught");
        } catch (SQLException ignore) {
            // Column already exists – ignore safely
        }

    }


    // ============================
    //  AUTO-POPULATE DEFAULT WORDS
    // ============================
    /**
     * Ensures that the {@code words} table contains default entries.
     * If the table is empty, a predefined set of category-based words
     * (Grammar, Food, Nature) is inserted.
     */
    private void ensureDefaultWords() {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM words");
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            stmt.close();

            if (count > 0) {
                System.out.println(" Words table already has " + count + " entries. Skipping API sync.");
                return;
            }

            System.out.println(" Inserting default words...");
            insertDefaultWords(conn);


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Failed to auto-populate words!");
        }
    }

    /**
     * Inserts the predefined list of default words into the {@code words} table.
     *
     * @param conn the active database connection
     * @throws SQLException if insertion fails
     */
    private void insertDefaultWords(Connection conn) throws SQLException {
        String[][] defaultWords = {
                // ===== GRAMMAR =====
                {"Dog", "Noun"}, {"Cat", "Noun"}, {"Book", "Noun"}, {"Car", "Noun"}, {"Table", "Noun"},
                {"Run", "Verb"}, {"Jump", "Verb"}, {"Write", "Verb"}, {"Sing", "Verb"}, {"Swim", "Verb"},
                {"Happy", "Adjective"}, {"Blue", "Adjective"}, {"Fast", "Adjective"}, {"Strong", "Adjective"}, {"Tall", "Adjective"},

                // ===== FOOD =====
                {"Apple", "Fruit"}, {"Banana", "Fruit"}, {"Orange", "Fruit"}, {"Mango", "Fruit"}, {"Grapes", "Fruit"},
                {"Chicken", "Meat"}, {"Beef", "Meat"}, {"Fish", "Meat"}, {"Lamb", "Meat"},
                {"Bread", "Grain"}, {"Rice", "Grain"}, {"Pasta", "Grain"}, {"Cereal", "Grain"},
                {"Milk", "Dairy"}, {"Cheese", "Dairy"}, {"Yogurt", "Dairy"}, {"Butter", "Dairy"},
                {"Cake", "Dessert"}, {"Pie", "Dessert"}, {"Donut", "Dessert"}, {"Cookie", "Dessert"},

                // ===== NATURE =====
                {"Lion", "Animal"}, {"Elephant", "Animal"}, {"Tiger", "Animal"}, {"Monkey", "Animal"},
                {"Tree", "Plant"}, {"Flower", "Plant"}, {"Grass", "Plant"}, {"Leaf", "Plant"},
                {"Rock", "Non-Living"}, {"Water", "Non-Living"}, {"Fire", "Non-Living"}, {"Wind", "Non-Living"},
                {"Rain", "Weather"}, {"Snow", "Weather"}, {"Sun", "Weather"}, {"Cloud", "Weather"}
        };

        String sql = "INSERT OR IGNORE INTO words (word, category) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String[] pair : defaultWords) {
                ps.setString(1, pair[0]);
                ps.setString(2, pair[1]);
                ps.addBatch();
            }
            ps.executeBatch();
        }
        System.out.println(" Inserted " + defaultWords.length + " default words successfully.");
    }

    // ============================
    //  SCORE MANAGEMENT
    // ============================
    /**
     * Inserts a new score record into the {@code scores} table.
     *
     * @param username  the player's username
     * @param category  the game category (e.g., Grammar, Food)
     * @param score     the total score achieved
     * @param correct   number of correct answers
     * @param incorrect number of incorrect answers
     * @param avgSpeed  average response speed
     */
    public static void updateScore(String username, String category,
                                   int score, int correct, int incorrect, double avgSpeed) {
        String sql = "INSERT INTO scores (username, game, score, correct, incorrect, avgSpeed, date_played) " +
                "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, category);
            ps.setInt(3, score);
            ps.setInt(4, correct);
            ps.setInt(5, incorrect);
            ps.setDouble(6, avgSpeed);
            ps.executeUpdate();
            System.out.println(" Saved score for " + username + " (" + category + "): " + score);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ============================
    //  WORD RETRIEVAL METHODS
    // ============================
    /**
     * Retrieves a random word from the specified category.
     *
     * @param category the category to filter by (e.g., "Noun", "Fruit")
     * @return a random word from the given category, or {@code null} if not found
     */
    public static String getRandomWord(String category) {
        String query = "SELECT word FROM words WHERE category = ? ORDER BY RANDOM() LIMIT 1";
        try (PreparedStatement ps = connect().prepareStatement(query)) {
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("word");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a list of all words in a specified category.
     *
     * @param category the category of words to retrieve
     * @return list of words under that category
     */
    public static List<String> getAllWords(String category) {
        List<String> list = new ArrayList<>();
        String query = "SELECT word FROM words WHERE category = ?";
        try (PreparedStatement ps = connect().prepareStatement(query)) {
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(rs.getString("word"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    // ============================
    //  TEACHER CLASS SYSTEM
    // ============================
    /**
     * Creates a new class associated with a teacher.
     *
     * @param teacher   the teacher's username
     * @param className the name of the class
     * @return {@code true} if creation was successful, otherwise {@code false}
     */
    public static boolean createClass(String teacher, String className) {
        String classCode = "CLS-" + (1000 + new Random().nextInt(9000));
        String sql = "INSERT INTO classes (class_name, class_code, teacher_username) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, className);
            ps.setString(2, classCode);
            ps.setString(3, teacher);
            ps.executeUpdate();
            System.out.println("🎓 Created class: " + className + " (" + classCode + ")");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Retrieves all classes managed by a given teacher.
     *
     * @param teacherUsername the teacher's username
     * @return list of maps containing class names and codes
     */
    public static List<Map<String, String>> getClassesByTeacher(String teacherUsername) {
        List<Map<String, String>> classes = new ArrayList<>();
        String sql = "SELECT class_name, class_code FROM classes WHERE teacher_username = ?";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, teacherUsername);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("class_name", rs.getString("class_name"));
                map.put("class_code", rs.getString("class_code"));
                classes.add(map);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return classes;
    }

    /**
     * Checks if a class code exists in the database.
     *
     * @param code the class code to verify
     * @return {@code true} if valid, otherwise {@code false}
     */
    public static boolean isValidClassCode(String code) {
        try (PreparedStatement ps = connect().prepareStatement("SELECT 1 FROM classes WHERE class_code = ?")) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Assigns a student to a specific class.
     *
     * @param username  the student's username
     * @param classCode the code of the class to assign to
     */
    public static void assignStudentToClass(String username, String classCode) {
        try (PreparedStatement ps = connect().prepareStatement("UPDATE users SET class_code = ? WHERE username = ?")) {
            ps.setString(1, classCode);
            ps.setString(2, username);
            ps.executeUpdate();
            System.out.println("👩‍🎓 Assigned " + username + " to class " + classCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns all student usernames assigned to a given class.
     *
     * @param classCode the code of the class
     * @return list of usernames
     */
    public static List<String> getStudentsByClass(String classCode) {
        List<String> students = new ArrayList<>();
        String sql = "SELECT username FROM users WHERE class_code = ?";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, classCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) students.add(rs.getString("username"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Calculates the average score for a given class.
     *
     * @param classCode the class identifier
     * @return the average score across all students
     */
    public static double getAverageClassScore(String classCode) {
        String sql = """
        SELECT AVG(score) as avg_score
        FROM scores
        WHERE username IN (SELECT username FROM users WHERE class_code = ?)
    """;
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, classCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("avg_score");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Retrieves all recorded scores for a specific student.
     *
     * @param username the student's username
     * @return list of score records with metadata
     */
    public static List<Map<String, Object>> getStudentScores(String username) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT game, score, correct, incorrect, date_played FROM scores WHERE username = ?";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("game", rs.getString("game"));
                entry.put("score", rs.getInt("score"));
                entry.put("correct", rs.getInt("correct"));
                entry.put("incorrect", rs.getInt("incorrect"));
                entry.put("date_played", rs.getString("date_played"));
                list.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Returns a teacher’s class overview with average scores and student counts.
     *
     * @param teacherUsername the teacher's username
     * @return list of class summary maps
     */
    public static List<Map<String, Object>> getClassOverview(String teacherUsername) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = """
        SELECT c.class_name, c.class_code,
               COUNT(u.username) AS student_count,
               COALESCE(AVG(s.score), 0) AS avg_score
        FROM classes c
        LEFT JOIN users u ON c.class_code = u.class_code
        LEFT JOIN scores s ON s.username = u.username
        WHERE c.teacher_username = ?
        GROUP BY c.class_code
    """;
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, teacherUsername);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("class_name", rs.getString("class_name"));
                row.put("class_code", rs.getString("class_code"));
                row.put("avg_score", rs.getDouble("avg_score"));
                row.put("student_count", rs.getInt("student_count"));
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Returns the top five students under a teacher ranked by average score.
     *
     * @param teacherUsername the teacher's username
     * @return list of top-performing students and their averages
     */
    public static List<Map<String, Object>> getTopStudents(String teacherUsername) {
        List<Map<String, Object>> top = new ArrayList<>();
        String sql = """
        SELECT u.username, AVG(s.score) AS avg_score
        FROM users u
        JOIN classes c ON u.class_code = c.class_code
        JOIN scores s ON u.username = s.username
        WHERE c.teacher_username = ?
        GROUP BY u.username
        ORDER BY avg_score DESC
        LIMIT 5
    """;
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, teacherUsername);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("username", rs.getString("username"));
                row.put("avg_score", rs.getDouble("avg_score"));
                top.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return top;
    }


    /**
     * Computes the average mark by game category for a teacher’s classes.
     *
     * @param teacherUsername the teacher's username
     * @return map of game names to average scores
     */
    public static Map<String, Double> getAverageByGame(String teacherUsername) {
        Map<String, Double> map = new LinkedHashMap<>();
        String sql = """
        SELECT s.game, AVG(s.score) AS avg_score
        FROM scores s
        JOIN users u ON s.username = u.username
        JOIN classes c ON u.class_code = c.class_code
        WHERE c.teacher_username = ?
        GROUP BY s.game
    """;
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, teacherUsername);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("game"), rs.getDouble("avg_score"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }
    /**
     * Retrieves basic student info (name, class code, and last played date).
     *
     * @param username the student username
     * @return map containing student info, or {@code null} if not found
     */
    public static Map<String, String> getStudentInfo(String username) {
        String sql = """
        SELECT name, class_code,
               COALESCE(MAX(date_played), 'Never') AS last_played
        FROM users
        LEFT JOIN scores ON users.username = scores.username
        WHERE users.username = ?
    """;
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, String> info = new HashMap<>();
                info.put("name", rs.getString("name"));
                info.put("class_code", rs.getString("class_code"));
                info.put("last_played", rs.getString("last_played"));
                return info;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Retrieves all score records for a student sorted by date.
     *
     * @param username the student username
     * @return list of score details
     */
    public static List<Map<String, Object>> getScoresByStudent(String username) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = """
        SELECT game, score, correct, incorrect, avgSpeed
        FROM scores
        WHERE username = ?
        ORDER BY date_played DESC
    """;
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("game", rs.getString("game"));
                row.put("score", rs.getInt("score"));
                row.put("correct", rs.getInt("correct"));
                row.put("incorrect", rs.getInt("incorrect"));
                row.put("avgSpeed", rs.getDouble("avgSpeed"));
                list.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    /**
     * Checks if a user exists in the database.
     *
     * @param username the username to check
     * @return {@code true} if found, otherwise {@code false}
     */
    public static boolean userExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Removes a student from their assigned class.
     *
     * @param username the student's username
     */
    public static void removeStudentFromClass(String username) {
        String sql = "UPDATE users SET class_code = NULL WHERE username = ?";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
            System.out.println("🗑️ Removed " + username + " from their class.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Renames an existing class owned by a teacher.
     *
     * @param teacherUsername the teacher's username
     * @param oldName         current class name
     * @param newName         desired new class name
     */
    public static void renameClass(String teacherUsername, String oldName, String newName) {
        String sql = "UPDATE classes SET class_name = ? WHERE teacher_username = ? AND class_name = ?";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setString(2, teacherUsername);
            ps.setString(3, oldName);
            ps.executeUpdate();
            System.out.println("✏ Renamed class from " + oldName + " → " + newName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a class owned by a teacher and detaches its students.
     *
     * @param teacherUsername the teacher's username
     * @param className       name of the class to delete
     */
    public static void deleteClass(String teacherUsername, String className) {
        String code = null;
        try (PreparedStatement ps = connect().prepareStatement(
                "SELECT class_code FROM classes WHERE teacher_username = ? AND class_name = ?")) {
            ps.setString(1, teacherUsername);
            ps.setString(2, className);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) code = rs.getString("class_code");
        } catch (SQLException e) { e.printStackTrace(); }

        if (code == null) return;

        // Unlink students from class first
        try (PreparedStatement ps1 = connect().prepareStatement(
                "UPDATE users SET class_code = NULL WHERE class_code = ?")) {
            ps1.setString(1, code);
            ps1.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }

        // Delete class entry
        try (PreparedStatement ps2 = connect().prepareStatement(
                "DELETE FROM classes WHERE class_code = ?")) {
            ps2.setString(1, code);
            ps2.executeUpdate();
            System.out.println("🗑️ Deleted class " + className + " (" + code + ")");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Retrieves only class names associated with a given teacher.
     *
     * @param teacherUsername the teacher's username
     * @return list of class names
     */
    public static List<String> getClassNamesByTeacher(String teacherUsername) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT class_name FROM classes WHERE teacher_username = ?";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, teacherUsername);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(rs.getString("class_name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * Returns the player's highest recorded score for a given game.
     *
     * @param username the username of the player
     * @param game     the game name
     * @return the highest score found, or {@code 0} if none exist
     */
    public static int getHighScore(String username, String game) {
        String sql = "SELECT MAX(score) AS max_score FROM scores WHERE username = ? AND game = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, game);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("max_score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // default if no record found
    }

}
