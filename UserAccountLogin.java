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

            int choice;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                scanner.nextLine(); // Clear invalid input
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            // Switch case for user choice
            switch (choice) {
                // Inside UserAccountLogin.java

                case 1: 
                    System.out.println("\nLogin selected.");
                    System.out.print("\nEnter Email Address: ");
                    String email = scanner.nextLine();
                    System.out.print("\nEnter Password: ");
                    String password = scanner.nextLine();

                    User user = UserManager.login(email, password);
                    
                    if (user != null) {
                        System.out.println("\nLogin successful! Welcome, " + user.getDisplayName() + ".");
                        
                        // --- THIS IS THE MISSING LINK ---
                        // 1. Create the journal object (this launches the invisible JavaFX toolkit)
                        JournalPage journal = new JournalPage(user);
                        
                        // 2. Start the menu loop
                        journal.displayDates();
                        
                        // 3. Stop the Login loop so we don't go back to login screen
                        running = false;
                        
                    } else {
                        System.out.println("\nLogin failed! Invalid email or password.");
                    }
                    break;

                // Registration process
                case 2:
                    System.out.println("\nRegister selected.");
                    System.out.print("Enter A Display Name: ");
                    String newDisplayName = scanner.nextLine();
                    System.out.print("\nEnter An Email Address: ");
                    String newEmail = scanner.nextLine();
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
                    running = false;
                    break;

                // Invalid option handling
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
}