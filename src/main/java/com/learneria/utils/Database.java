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

            // Ensure users table exists with teacher_code optional
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS users (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "username TEXT UNIQUE NOT NULL," +
                                "password TEXT NOT NULL," +
                                "role TEXT NOT NULL," +
                                "teacher_code TEXT NULL" +
                                ")"
                );
                System.out.println("✅ Users table ready (teacher_code optional)");
            }

            // Add teacher_code column if old table didn’t have it
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("ALTER TABLE users ADD COLUMN teacher_code TEXT NULL");
                System.out.println("✅ teacher_code column added to users table");
            } catch (SQLException ignore) {
                // Column already exists → safe to ignore
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

    public Connection getConnection() {
        return connection;
    }
}

