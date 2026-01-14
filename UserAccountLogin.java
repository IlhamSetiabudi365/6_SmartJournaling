import java.util.Scanner;

import javax.xml.transform.Source;

public class UserAccountLogin {

    // CHANGED: Returns a 'User' object so WelcomePage knows who logged in
    public static User execute() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("=== User Account Login ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Please select an option (1-3): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input.");
                scanner.next();
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Switch case for user choice
            switch (choice) {
                // Login process
                case 1:
                    System.out.println("\nLogin selected.");
                    String email = checkEmail();

                    System.out.print("\nEnter Password: ");
                    String password = scanner.nextLine();

                    // Authenticate user
                    User user = UserManager.login(email, password);
                    if (user != null) {
                        System.out.println("\nLogin successful! Welcome, " + user.getDisplayName() + ".");
                        return user; // <--- RETURN THE USER OBJECT
                    } else {
                        System.out.println("\nLogin failed! Invalid email or password.");
                    }
                    break;

                // Registration process
                case 2:
                    System.out.println("\nRegister selected.");
                    System.out.print("Enter A Display Name: ");
                    String newDisplayName = scanner.nextLine();
                    String newEmail = checkEmail();
                    System.out.print("\nEnter A Password: ");
                    String newPassword = scanner.nextLine();

                    User newUser = new User(newEmail, newDisplayName, newPassword);
                    if (UserManager.register(newUser)) {
                        System.out.println("\nRegistration successful! You can now log in.");
                    } else {
                        System.out.println("\nRegistration failed! Email may already be in use.");
                    }
                    break;

                // Exit process
                case 3:
                    System.out.println("\nThank you for using Smart Journal!");
                    System.out.println("\nExiting...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        return null;
    }

    public static String checkEmail() {
        String email = "";
        Scanner scanner = new Scanner(System.in);
        boolean isValid = false;
        while (!isValid) {
            System.out.print("\nEnter Email Address: ");
            email = scanner.nextLine();
            try {
                String[] emailCheck = email.split("@");
                if (emailCheck.length != 2) {
                throw new IllegalArgumentException("Must contain exactly one '@' symbol.");
                }

                String domain = emailCheck[1];
                if (!domain.contains(".") || domain.startsWith(".") || domain.endsWith(".")) {
                throw new IllegalArgumentException("Invalid domain");
                }
                isValid = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return (email);
    }

}