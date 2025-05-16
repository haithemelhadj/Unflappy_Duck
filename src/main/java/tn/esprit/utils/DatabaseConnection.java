package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/piwpiw";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // Modified to create new connections each time
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
            throw e;
        }
    }
}






/*package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
   private static DatabaseConnection instance;
   private final String URL = "jdbc:mysql://localhost:3306/pidevv";
   private final String USERNAME = "root";
   private final String PASSWORD = "";
   private Connection cnx;

   private DatabaseConnection() {
       try {
           cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
           System.out.println("Connected to database");
       } catch (SQLException e) {
           System.out.println("Connection error: " + e.getMessage());
       }
   }

   public static DatabaseConnection getInstance() {
       if (instance == null) {
           instance = new DatabaseConnection();
       }
       return instance;
   }

   // Add this method to get the connection
   public Connection getConnection() {
       return cnx;
   }
} */


