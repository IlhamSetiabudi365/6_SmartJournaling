import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.nio.file.*;

public class JournalPage {
    private User user;

    public JournalPage(User user) {
        this.user = user;
    }

    // --- NAVIGATION LOGIC ---
    // Displays the list of available dates (History)
    public void displayDates() {
        Scanner sc = new Scanner(System.in);
        LocalDate today = LocalDate.now();

        System.out.println("\n============================");
        System.out.println("      JOURNAL HISTORY       ");
        System.out.println("============================");
        
        // Display last 5 days
        for (int i = 4, j = 1; i >= 0; i--, j++) {
            LocalDate dateToShow = today.minusDays(i);
            System.out.print(j + ". " + dateToShow);
            if (i == 0) System.out.print(" (today)");
            System.out.println("");
        }
        
        System.out.println("0. Back to Main Menu");
        System.out.print("\nSelect a date (1-5) or 0 to go back: \n> ");
        
        try {
            int choice = sc.nextInt();
            if (choice == 0) return; // Returns to Welcome/Main Page

            if (choice >= 1 && choice <= 5) {
                LocalDate chosenDate = today.minusDays(5 - choice);
                showDateActions(chosenDate);
            } else {
                System.out.println("Invalid choice. Try again.");
                displayDates();
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number.");
            displayDates();
        }
    }

    // Secondary menu after a date is selected
    public void showDateActions(LocalDate chosenDate) {
        Scanner sc = new Scanner(System.in);
        boolean exists = doesJournalExist(chosenDate);

        System.out.println("\nDate selected: " + chosenDate);
        if (exists) {
            System.out.println("1. View Entry");
            System.out.println("2. Edit Entry (Overwrite)");
            System.out.println("3. Delete Entry");
            System.out.println("0. Back");
        } else {
            System.out.println("1. Create New Entry");
            System.out.println("0. Back");
        }
        
        System.out.print("Choice: ");
        int choice = sc.nextInt();

        if (choice == 1) {
            if (exists) viewJournal(chosenDate);
            else createJournal(chosenDate);
        } else if (choice == 2 && exists) {
            editJournal(chosenDate);
        } else if (choice == 3 && exists) {
            deleteJournal(chosenDate);
        } else {
            displayDates();
            return;
        }

        // Return to the date list after action is finished
        displayDates();
    }

    // --- FILE OPERATIONS (.txt) ---

    public void createJournal(LocalDate date) {
        System.out.println("\n--- Creating New Entry for " + date + " ---");
        editJournal(date); // Creation logic is the same as edit (overwriting empty/new file)
    }

    public void viewJournal(LocalDate date) {
        String filePath = directoryPath() + "/" + date + ".txt";
        try {
            String content = Files.readString(Path.of(filePath));
            System.out.println("\n----------------------------------------");
            System.out.println("JOURNAL ENTRY - " + date);
            System.out.println("----------------------------------------");
            System.out.println(content);
            System.out.println("----------------------------------------");
            System.out.println("Press Enter to go back...");
            new Scanner(System.in).nextLine();
        } catch (IOException e) {
            System.out.println("Error reading journal: " + e.getMessage());
        }
    }

    public void editJournal(LocalDate date) {
        String filePath = directoryPath() + "/" + date + ".txt";
        Scanner scan = new Scanner(System.in);
        
        try {
            // 1. RETRIEVE: Show existing content if file exists
            if (doesJournalExist(date)) {
                String oldContent = Files.readString(Path.of(filePath));
                System.out.println("\n[Current Content]:");
                System.out.println(oldContent);
                System.out.println("----------------------------------------");
            }

            // 2. INPUT: Capture new text
            System.out.println("Type your journal entry below (Press Enter to save):");
            System.out.print("> ");
            String newContent = scan.nextLine();
            
            // 3. WRITE: Save to .txt file (Overwrite)
            Files.writeString(Path.of(filePath), newContent);
            System.out.println("\nJournal saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving journal: " + e.getMessage());
        }
    }

    public void deleteJournal(LocalDate date) {
        String filePath = directoryPath() + "/" + date + ".txt";
        File file = new File(filePath);
        if (file.delete()) {
            System.out.println("Journal deleted successfully.");
        } else {
            System.out.println("Failed to delete journal.");
        }
    }

    // --- HELPER METHODS ---

    public boolean doesJournalExist(LocalDate date) {
        return new File(directoryPath() + "/" + date + ".txt").exists();
    }

    public String directoryPath() {
        String username = user.getDisplayName();
        String path = "user_journal/" + username;
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return path;
    }
}