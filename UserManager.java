import java.io.*;
import java.util.*;

// Manages user login and registration
public class UserManager {

    private static final String FILE_NAME = "UserData.txt";

    // Ensure the user data file exists
    private static void ensureFileExists() {
        File file = new File(FILE_NAME);
        try {
            if (file.createNewFile()) {
                System.out.println("UserData.txt created.");
            }
        } catch (IOException e) {
            System.out.println("Error ensuring UserData.txt exists: " + e.getMessage());
        }
    }

    // Load users from the data file
    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(FILE_NAME))) {

            while (scanner.hasNextLine()) {
                String email = scanner.nextLine().trim();
                if(email.isEmpty()) continue;
                if (!scanner.hasNextLine()) break;
                String displayName = scanner.nextLine().trim();
                if (!scanner.hasNextLine()) break;
                String password = scanner.nextLine().trim();

                users.add(new User(email, displayName, password));
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    // Authenticate user login
    public static User login(String email, String password) {
        ArrayList<User> users = loadUsers();

        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(SecurityUtility.encrypt(password))) {
                return user;
            }
        }
        return null;
    }

    // Register a new user
    public static boolean register(User newUser) {
        ensureFileExists();
        ArrayList<User> users = loadUsers();

        for (User user : users) {
            if (user.getEmail().equals(newUser.getEmail())) {
                return false; // Email already in use
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            String encryptedPassword = SecurityUtility.encrypt(newUser.getPassword());
            writer.println(newUser.getEmail());
            writer.println(newUser.getDisplayName());
            writer.println(encryptedPassword);
            writer.println(); // Blank line between users
            return true;
        } catch (IOException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }
}