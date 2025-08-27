package com.example.finaldestinationgroupproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

//        run creation
//        Databasing.create_db();
//        Databasing.create_tables();

public class Databasing {
    public static void create_db() {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
    public static void create_tables() {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();

//            create database tables
//            student table (username, password, name, level, teacher)
            String sqlONE = "CREATE TABLE STUDENTS " +
                    "(USERNAME TEXT NOT NULL," +
                    " PASSWORD           TEXT    NOT NULL, " +
                    " NAME            TEXT     NOT NULL, " +
                    " LEVEL TEXT DEFAULT 'Book-Worm', " +
                    " TEACHER TEXT, " +
                    " OVERALL_SCORE   INT, " +
                    " HIGH_SCORE   INT" +
                    " CORRECT_ANSWERS   INT" +
                    " INCORRECT_ANSWERS   INT" +
                    " ANSWER_SPEED TIME" +
                    " TOTAL_GAMES INT, " +
                    " PRIMARY KEY (USERNAME)," +
                    " FOREIGN KEY (LEVEL) REFERENCES LEVELS(LEVEL_NAME), " +
                    " FOREIGN KEY (TEACHER) REFERENCES TEACHERS(USERNAME) )";

//            teacher table (username, password, name)
            String sqlTWO = "CREATE TABLE TEACHERS " +
                    "(USERNAME TEXT NOT NULL," +
                    " PASSWORD           TEXT    NOT NULL, " +
                    " NAME            TEXT     NOT NULL, " +
                    " PRIMARY KEY (USERNAME) )";

//            level table (level name, min score, max score, image)
            String sqlTHREE = "CREATE TABLE LEVELS " +
                    "(LEVEL_NAME TEXT NOT NULL," +
                    " MIN_SCORE           INT    NOT NULL, " +
                    " MAX_SCORE            INT     NOT NULL, " +
                    " IMAGE        TEXT  NOT NULL, " +
                    " PRIMARY KEY (LEVEL_NAME) )";

//            game table (gameID, student, correct answers, incorrect answers, score, answer speed)
            String sqlFOUR = "CREATE TABLE GAMES " +
                    "(GAME_ID INT NOT NULL," +
                    " STUDENT TEXT NOT NULL, " +
                    " CORRECT_ANSWERS     INT, " +
                    " INCORRECT_ANSWERS      INT, " +
                    " SCORE   INT, " +
                    " ANSWER_SPEED TIME, " +
                    " PRIMARY KEY (GAME_ID), " +
                    " FOREIGN KEY (STUDENT) REFERENCES STUDENTS(USERNAME) )";

//            bucket table (bucket id, bucket name, category)
            String sqlFIVE = "CREATE TABLE BUCKETS " +
                    "(BUCKET_ID INT NOT NULL," +
                    " BUCKET_NAME TEXT NOT NULL, " +
                    " CATEGORY INT, " +
                    " PRIMARY KEY (BUCKET_ID), " +
                    " FOREIGN KEY(CATEGORY) REFERENCES CATEGORIES(CATEGORY_ID) )";

//            category table (category id, category name)
            String sqlSIX = "CREATE TABLE CATEGORIES " +
                    "(CATEGORY_ID  INT  NOT NULL," +
                    " CATEGORY_NAME  TEXT    NOT NULL, " +
                    " PRIMARY KEY (CATEGORY_ID) )";

//            word table
            String sqlSEVEN = "CREATE TABLE WORDS " +
                    "(WORD_ID INT NOT NULL," +
                    " WORD TEXT NOT NULL, " +
                    " BUCKET INT, " +
                    " NUM_OF_LETTERS INT, " +
                    " PRIMARY KEY(WORD_ID), " +
                    " FOREIGN KEY(BUCKET) REFERENCES BUCKETS(BUCKET_ID))";

            stmt.executeUpdate(sqlONE);
            stmt.executeUpdate(sqlTWO);
            stmt.executeUpdate(sqlTHREE);
            stmt.executeUpdate(sqlFOUR);
            stmt.executeUpdate(sqlFIVE);
            stmt.executeUpdate(sqlSIX);
            stmt.executeUpdate(sqlSEVEN);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
}
