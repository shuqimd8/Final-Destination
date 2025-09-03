package com.example.finaldestinationgroupproject;

import java.sql.*;

public class User {
    private String Username;
    private String Password;

    public User (String username, String password) {
        Username = username;
        Password = password;
    }

    public void login(String username, String password) {

    }

    public boolean checkUsername (String username) {
        //check through student and teacher tables that the username doesn't already exist
        //if it doesn't:
        return true;
        // else:
        //return false

//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        Boolean taken = false;
//
//        try {
//            Class.forName("org.sqlite.JDBC");
//            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
//            System.out.println("Opened database successfully");
//
//            String sql = "SELECT * FROM students WHERE username = ?";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, username); // Assuming user_id 101 is the specific entry to find
//
//            rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                taken = true;
//            }
//            else {
//                taken = false;
//            }
//
//        }
//        catch ( Exception e ) {
//            System.out.println("Exception: " + e + "has occurred.");
//        }
//        finally {
//            return taken;
//        }
    }
    public boolean checkPassword (String password) {
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
                else {
                    continue;
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
}
