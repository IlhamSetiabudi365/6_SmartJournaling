import java.time.LocalTime;
import java.util.Scanner;

public class WelcomePage {

    public static void main(String[] args) {
        System.out.println("\n================================");
        System.out.println("===       Smart Journal      ===");
        System.out.println("================================");
        System.out.println("by : AkuSukaMakanBakso");
        System.out.println();

        // call login
        User currentUser = UserAccountLogin.execute();

        // show main menu if login success
        if (currentUser != null) {
            showMainMenu(currentUser);
        }
    }

    public static void showMainMenu(User user) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        
        // Initialize JournalPage with the logged-in user
        JournalPage journal = new JournalPage(user);

        // Display the time-based greeting
        welcomeMessage(user);

        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Journal Page         (press 1)");
            System.out.println("2. Sentiment Analysis   (press 2)");
            System.out.println("3. Summary Page         (press 3)");
            System.out.println("0. Exit                 (press 0)");

            System.out.print(">> ");
            if (!sc.hasNextInt()) {
                sc.next(); continue;
            }
            int choice = sc.nextInt();
            
            switch(choice){
                case 1 -> {
                    // CONNECTED: Open the Journal Page
                    journal.displayDates();
                }
                case 2 -> {
                    System.out.println("Feature coming soon...");
                }
                case 3 -> {
                    System.out.println("Feature coming soon...");
                }
                case 0 -> {
                    System.out.println("Exiting application. Goodbye!");
                    running = false;
                }
                default -> {
                    System.out.println("Invalid option.");
                }
            }
        }
    }

    public static void welcomeMessage(User user){
        LocalTime timeNow = LocalTime.now();
        String greeting = "Hello";

        if (timeNow.isAfter(LocalTime.of(0, 0)) && timeNow.isBefore(LocalTime.of(11, 59))){
            greeting = "Good Morning";
        } else if(timeNow.isAfter(LocalTime.of(12, 0)) && timeNow.isBefore(LocalTime.of(16, 59))){
            greeting = "Good Afternoon";
        } else if (timeNow.isAfter(LocalTime.of(17, 0)) && timeNow.isBefore(LocalTime.of(23, 59))){
            greeting = "Good Evening";
        }
        
        System.out.println("\n" + greeting + ", " + user.getDisplayName() + "!");
    }
}