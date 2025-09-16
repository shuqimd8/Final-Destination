package unitTesting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class testDB {
    public static void establishConnection() {
        Connection c = null;
        Statement stmt = null;
        try {
            c = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/?user=root",
                    "root",
                    "CompSci2004%"
            );
        } catch( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Database connected.");
    }
}
