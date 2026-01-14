import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // database url
    private static final String URL = "jdbc:mysql://10.164.239.95:3306/SmartJournalDB";
    
    // credentials
    private static final String USER = "root";
    
    // password
    private static final String PASSWORD = "hellomysql!123";

    public static Connection getConnection() {
        try {
            // load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database Connection Failed: " + e.getMessage());
            return null;
        }
    }
}