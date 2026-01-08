import java.util.Scanner;

public class UserAccountLogin {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
        System.out.println("=== User Account Login ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Please select an option (1-3): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        // Switch case for user choice
        switch (choice) {
            // Login process
            case 1: System.out.println("Login selected.");
                    // Enter email
                    System.out.print("Enter Email Address: ");
                    String email = scanner.nextLine();
                    // Enter password
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();

                    // Authenticate user using the entered email and password
                    User user = UserManager.login(email, password);
                    if (user != null) {
                        System.out.println("Login successful! Welcome, " + user.getDisplayName() + ".");
                        running = false;
                    } else {
                        System.out.println("Login failed! Invalid email or password.");
                    }
                    break;

            // Registration process
            case 2: System.out.println("Register selected.");
                    // Enter display name
                    System.out.print("Enter A Display Name: ");
                    String newDisplayName = scanner.nextLine();
                    // Enter email
                    System.out.print("Enter An Email Address: ");
                    String newEmail = scanner.nextLine();
                    // Enter password
                    System.out.print("Enter A Password: ");
                    String newPassword = scanner.nextLine();

                    // Create new user using the registration details entered by the user
                    User newUser = new User(newEmail, newDisplayName, newPassword);
                    if (UserManager.register(newUser)) {
                        System.out.println("Registration successful! You can now log in.");
                    } else {
                        System.out.println("Registration failed! Email may already be in use.");
                    }
                    break;

            // Exit process
            case 3: System.out.println("Thank you for using Smart Journal!");
                    System.out.println("Exiting...");
                    running = false;
                    break;

            // Invalid option handling
            default: System.out.println("Invalid option. Please try again.");
        }
        }       scanner.close();
    }
}