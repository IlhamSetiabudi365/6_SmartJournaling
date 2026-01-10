import java.sql.*;
//import com.mysql.cj.jdbc.Driver;

public class UserManager {

    // LOGIN: Ask the Database if this user exists
    public static User login(String email, String rawPassword) {
        // SQL Query: Find user with matching email and password
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn == null) {
                System.out.println("Error: Database connection failed.");
                return null;
            }

            // 1. Encrypt the typed password (so it matches the DB)
            String encryptedPass = SecurityUtility.encrypt(rawPassword);

            // 2. Fill in the '?' placeholders safely
            stmt.setString(1, email);
            stmt.setString(2, encryptedPass);

            // 3. Run the query
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // SUCCESS: User found!
                String foundName = rs.getString("display_name");
                String foundEmail = rs.getString("email");
                String foundPass = rs.getString("password");
                return new User(foundEmail, foundName, foundPass);
            }

        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return null;
    }

    // REGISTER: Send new user to the Database
    public static boolean register(User newUser) {
        String query = "INSERT INTO users (email, display_name, password) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn == null) return false;

            // 1. Encrypt the password before saving
            String encryptedPass = SecurityUtility.encrypt(newUser.getPassword());

            // 2. Fill in the data
            stmt.setString(1, newUser.getEmail());
            stmt.setString(2, newUser.getDisplayName());
            stmt.setString(3, encryptedPass);

            // 3. Execute the update
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // If 1 row added, it worked.

        } catch (SQLIntegrityConstraintViolationException e) {
            // This specific error happens if the Primary Key (Email) already exists
            System.out.println("Registration Failed: That email is already registered.");
            return false;
        } catch (SQLException e) {
            System.out.println("Register Error: " + e.getMessage());
            return false;
        }
    }
}