import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // 1. Database URL
    private static final String URL = "jdbc:mysql://localhost:3306/SmartJournalDB";
    
    // 2. YOUR CREDENTIALS
    private static final String USER = "root"; 
    
    // REPLACE '1234' WITH YOUR ACTUAL NEW PASSWORD
    private static final String PASSWORD = "password@12";

    public static Connection getConnection() {
        try {
            // Load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database Connection Failed: " + e.getMessage());
            return null;
        }
    }
}