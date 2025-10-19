package com.learneria.utils;

import java.sql.*;
import java.util.*;

/**
 * Handles all SQLite database logic for Papa's Learneria.
 * Includes user management, score tracking, and word retrieval.
 * Supports manual & automatic API sync (Datamuse + Food + Nature)
 * ✅ Now extended to support Teacher-Class system
 */
public class Database {
    private static Database instance;
    private Connection connection;
    private static boolean connectionLogged = false;

    // ============================
    // INITIALISATION
    // ============================
    private Database() throws SQLException {
        try {
            String homePath = System.getProperty("user.home") + "/learneria_data";
            java.io.File folder = new java.io.File(homePath);
            if (!folder.exists()) folder.mkdirs();

            String url = "jdbc:sqlite:" + homePath + "/learneria.db";
            connection = DriverManager.getConnection(url);

            if (!connectionLogged) {
                System.out.println("✅ Connected to database: " + url);
                connectionLogged = true;
            }

            createTablesIfMissing();
            ensureDefaultWords();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static synchronized Database getInstance() throws SQLException {
        if (instance == null) {
            instance = new Database();
        } else if (instance.connection == null || instance.connection.isClosed()) {
            instance.reconnect();
        }
        return instance;
    }

    private void reconnect() {
        try {
            String homePath = System.getProperty("user.home") + "/learneria_data";
            connection = DriverManager.getConnection("jdbc:sqlite:" + homePath + "/learneria.db");
            System.out.println("🔁 Reconnected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) reconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

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

        // ✅ new: teacher classes and quizzes
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
    }

    // ============================
    // 🧩 AUTO-POPULATE DEFAULT WORDS
    // ============================
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
                System.out.println("✅ Words table already has " + count + " entries. Skipping API sync.");
                return;
            }

            System.out.println("🧩 Inserting default words...");
            insertDefaultWords(conn);


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Failed to auto-populate words!");
        }
    }

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
        System.out.println("✅ Inserted " + defaultWords.length + " default words successfully.");
    }

    // ============================
    // 🧠 SCORE MANAGEMENT
    // ============================
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
            System.out.println("💾 Saved score for " + username + " (" + category + "): " + score);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ============================
    // 🔤 WORD RETRIEVAL METHODS
    // ============================
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
    // 🧑‍🏫 TEACHER CLASS SYSTEM
    // ============================
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

    /** 计算指定班级的平均分 */
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

    /** Get average score ） */
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

    /** 班级总览 */
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

    /** 老师名下前5名学生（平均分） */
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

    /** 各游戏平均分 */
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
    /** 获取学生基本信息 + 最近游戏时间 */
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

    /** 获取学生所有游戏记录 */
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
    /** 检查用户是否存在 */
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
    /** 从班级移除学生（解除 class_code） */
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
    /** Rename a class (by teacher + old name) */
    public static void renameClass(String teacherUsername, String oldName, String newName) {
        String sql = "UPDATE classes SET class_name = ? WHERE teacher_username = ? AND class_name = ?";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setString(2, teacherUsername);
            ps.setString(3, oldName);
            ps.executeUpdate();
            System.out.println("✏️ Renamed class from " + oldName + " → " + newName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Delete a class (and optionally detach its students) */
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

    /** Simple helper: returns only class names for this teacher */
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
    /** Get the player's highest Grammar score (already stored in 'scores' table). */
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
