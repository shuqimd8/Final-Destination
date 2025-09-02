package com.example.finaldestinationgroupproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;

import java.io.IOException;

public class Program {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    public void signIn(String user, String pass) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");

            String sql = "SELECT * FROM students WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user); // Assuming user_id 101 is the specific entry to find

            rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("User Found:");
                if (rs.getString("password") == pass) {
                    System.out.println("Name: " + rs.getString("name"));
                    //create new object of student class
                }
                else {
                    System.out.println("Incorrect password.");
                }
            } else {
                sql = "SELECT * FROM teachers WHERE username = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, user); // Assuming user_id 101 is the specific entry to find

                // Step 3: Execute the Query
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    System.out.println("User Found:");
                    if (rs.getString("password") == pass) {
                        System.out.println("Name: " + rs.getString("name"));
                        //create new object of teacher class
                    }
                    else {
                        System.out.println("Incorrect password.");
                    }
                }
                else {
                    System.out.println("User not found.");
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Step 5: Close Resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void signUp(String userType, String username, String password, String passwordConfirm) {
        //check that username doesn't exist in the teacher or student table
        //check that password matches the criteria
        //check that both passwords match
        //if usertype is
    }
}
