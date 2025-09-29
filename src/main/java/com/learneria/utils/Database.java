package com.learneria.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Database instance;
    private Connection connection;

    private Database() throws SQLException {
        try {
            String url = "jdbc:sqlite:src/main/resources/db/learneria.db";
            connection = DriverManager.getConnection(url);
            System.out.println("✅ Database connected at " + url);

            // Create users table (new installs)
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS users (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "username TEXT UNIQUE NOT NULL," +
                                "password TEXT NOT NULL," +
                                "role TEXT NOT NULL," +
                                "teacher_code TEXT NULL," +
                                "name TEXT" +
                                ")"
                );
            }

            // Ensure 'name' column exists (migration for old DBs)
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("ALTER TABLE users ADD COLUMN name TEXT");
                System.out.println("⚡ Added missing 'name' column to users table");
            } catch (SQLException ignore) {
                // ignore: column already exists
            }

            // Create scores table
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS scores (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "username TEXT NOT NULL," +
                                "game TEXT NOT NULL," +
                                "score INTEGER NOT NULL," +
                                "date_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                                ")"
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Database getInstance() throws SQLException {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // Always return the same open connection
    public Connection getConnection() {
        return connection;
    }

    // Optional: close at app shutdown
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("❌ Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

