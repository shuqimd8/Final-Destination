package com.example.finaldestinationgroupproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQL {

    public static void establishConnection() {
        Connection c = null;
        Statement stmt = null;
        try {
//            Class.forName("com.mysql.JDBC");
            c = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/finalDestinationTables",
                    "root",
                    "CompSci2004%"
            );
        } catch( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}
