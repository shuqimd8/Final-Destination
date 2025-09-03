package com.example.finaldestinationgroupproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Student {
    private String StudentUsername;
    private String StudentName;
    private String StudentPassword;
    private String Teacher;
    private String Level;

    public Student(String user, String pass, String name, String teacher) {
        StudentUsername = user;
        StudentPassword = pass;
        StudentName = name;
        Teacher = teacher;
    }

    public void createStudent (String username, String password, String name, String teacher) {
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            Class.forName("org.sqlite.JDBC");
//            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
//
//            String addStudentToDB = "INSERT INTO students VALUES (?, ?, ?, 'Book-Worm', ?, 0, 0, 0, 0, 0, 0)";
//            pstmt = conn.prepareStatement(addStudentToDB);
//            pstmt.setString(1, user);
//            pstmt.setString(2, pass);
//            pstmt.setString(3, name);
//            pstmt.setString(4, teach);
//
//            pstmt.executeUpdate();
//            System.out.println("Created account");
//        }
//        catch ( Exception e ) {
//            System.out.println("Exception: " + e + "has occurred.");
//        }
    }
}
