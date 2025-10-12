package com.learneria.utils;

import java.sql.*;
import java.util.*;

/**
 * Handles all SQLite database logic for Papa's Learneria.
 * Includes user management, score tracking, and word retrieval.
 * Supports manual & automatic API sync (Datamuse + Food + Nature).
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
            // ✅ Create persistent folder in user home
            String homePath = System.getProperty("user.home") + "/learneria_data";
            java.io.File folder = new java.io.File(homePath);
            if (!folder.exists()) {
                folder.mkdirs();
                System.out.println("📁 Created folder: " + folder.getAbsolutePath());
            }

            // ✅ Connect to SQLite database
            String url = "jdbc:sqlite:" + homePath + "/learneria.db";
            connection = DriverManager.getConnection(url);

            if (!connectionLogged) {
                System.out.println("✅ Connected to database: " + url);
                connectionLogged = true;
            }

            createTablesIfMissing();
            ensureDefaultWords(); // populate if empty

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // ============================
    // SINGLETON INSTANCE
    // ============================
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
            String url = "jdbc:sqlite:" + homePath + "/learneria.db";
            connection = DriverManager.getConnection(url);
            System.out.println("🔁 Reconnected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                reconnect();
            }
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
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "username TEXT UNIQUE NOT NULL," +
                            "password TEXT NOT NULL," +
                            "role TEXT NOT NULL," +
                            "teacher_code TEXT NULL," +
                            "name TEXT)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS scores (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "username TEXT NOT NULL," +
                            "game TEXT NOT NULL," +
                            "score INTEGER NOT NULL," +
                            "correct INTEGER DEFAULT 0," +
                            "incorrect INTEGER DEFAULT 0," +
                            "avgSpeed REAL DEFAULT 0," +
                            "date_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS words (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "word TEXT NOT NULL," +
                            "category TEXT NOT NULL," +
                            "UNIQUE(word, category))"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

            // Only auto-sync if database was empty
            runFullAPISync();

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
                {"Pineapple", "Fruit"}, {"Strawberry", "Fruit"}, {"Watermelon", "Fruit"}, {"Pear", "Fruit"}, {"Kiwi", "Fruit"},

                {"Chicken", "Meat"}, {"Beef", "Meat"}, {"Pork", "Meat"}, {"Fish", "Meat"}, {"Lamb", "Meat"},
                {"Duck", "Meat"}, {"Turkey", "Meat"}, {"Sausage", "Meat"}, {"Bacon", "Meat"}, {"Ham", "Meat"},

                {"Bread", "Grain"}, {"Rice", "Grain"}, {"Pasta", "Grain"}, {"Noodles", "Grain"}, {"Cereal", "Grain"},
                {"Oats", "Grain"}, {"Barley", "Grain"}, {"Corn", "Grain"}, {"Tortilla", "Grain"}, {"Bagel", "Grain"},

                {"Milk", "Dairy"}, {"Cheese", "Dairy"}, {"Yogurt", "Dairy"}, {"Butter", "Dairy"}, {"Cream", "Dairy"},
                {"Ice Cream", "Dairy"}, {"Custard", "Dairy"}, {"Ghee", "Dairy"}, {"Paneer", "Dairy"}, {"Mozzarella", "Dairy"},

                {"Cake", "Dessert"}, {"Pie", "Dessert"}, {"Donut", "Dessert"}, {"Pudding", "Dessert"}, {"Brownie", "Dessert"},
                {"Chocolate", "Dessert"}, {"Cupcake", "Dessert"}, {"Cookie", "Dessert"}, {"Muffin", "Dessert"}, {"Candy", "Dessert"},

                // ===== NATURE =====
                {"Lion", "Animal"}, {"Elephant", "Animal"}, {"Tiger", "Animal"}, {"Deer", "Animal"}, {"Monkey", "Animal"},
                {"Eagle", "Animal"}, {"Snake", "Animal"}, {"Frog", "Animal"}, {"Wolf", "Animal"}, {"Bear", "Animal"},

                {"Tree", "Plant"}, {"Flower", "Plant"}, {"Grass", "Plant"}, {"Moss", "Plant"}, {"Bush", "Plant"},
                {"Leaf", "Plant"}, {"Fern", "Plant"}, {"Vine", "Plant"}, {"Cactus", "Plant"}, {"Bamboo", "Plant"},

                {"Rock", "Non-Living"}, {"Water", "Non-Living"}, {"Fire", "Non-Living"}, {"Sand", "Non-Living"}, {"Wind", "Non-Living"},
                {"Soil", "Non-Living"}, {"Cloud", "Non-Living"}, {"Mountain", "Non-Living"}, {"Stone", "Non-Living"}, {"Ice", "Non-Living"},

                {"Rain", "Weather"}, {"Snow", "Weather"}, {"Storm", "Weather"}, {"Fog", "Weather"}, {"Sun", "Weather"},
                {"Wind", "Weather"}, {"Lightning", "Weather"}, {"Thunder", "Weather"}, {"Rainbow", "Weather"}, {"Drought", "Weather"},
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

    public static String getWordCategory(String word) {
        String query = "SELECT category FROM words WHERE word = ?";
        try (PreparedStatement ps = connect().prepareStatement(query)) {
            ps.setString(1, word);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("category");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ============================
    // 🌐 API SYNC (AUTO + MANUAL)
    // ============================
    public static void runFullAPISync() {
        try {
            System.out.println("🌐 Fetching Grammar words from Datamuse...");
            WordAPI.syncPOSWords(200);

            System.out.println("🍎 Fetching Food words...");
            try {
                FoodAPI.syncFoodWords();
            } catch (Exception e) {
                System.out.println("⚠️ Food API unavailable, skipping.");
            }

            System.out.println("🌳 Fetching Nature words...");
            NatureAPI.syncNatureWords();

            System.out.println("✅ All API sync complete!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("⚠️ API sync failed: " + e.getMessage());
        }
    }

    // ✅ Manual "Force API Refresh"
    public static void refreshAllAPI() {
        System.out.println("🔄 Manual API Refresh triggered...");
        runFullAPISync();
    }
}
