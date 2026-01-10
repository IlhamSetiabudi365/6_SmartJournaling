import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

public class JournalPage{
    private User user;

    public JournalPage(User user) {
        this.user = user;
    }
    
    public void displayDates() {
        Scanner sc = new Scanner(System.in);
        //get date today
        LocalDate today = LocalDate.now();

        //calculate and display dates from 6 days ago up to and including today
        System.out.println("\n=== Journal Dates ===\n0.Main Menu");
        for(int i=6 , j=1 ; i>=0 ; i-- , j++){
            LocalDate dateToShow = today.minusDays(i);
            System.out.print(j + "." + dateToShow);
            if (i==0){
                System.out.print("(today)");
            }
            System.out.println("");
        }

        //prompt user
        System.out.print("\nSelect a date to view journal, or create a new journal for today: \n>> ");
        try {
            int choice = sc.nextInt();
            if (choice == 0) return; // Returns to Welcome/Main Page

            if (choice >= 1 && choice <= 7) {
                LocalDate chosenDate = today.minusDays(7 - choice);
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
            if (exists) {
                viewJournal(chosenDate);
            }
            else {
                createJournal(chosenDate);
                showDateActions(chosenDate);
                return;
            }
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

    public void createJournal(LocalDate date) {
        try {
            // Get the directory path and ensure it exists
            String directoryPath = directoryPath();
            
            // Create the file with the date as the filename
            String filePath = directoryPath + "/" + date + ".txt";
            PrintWriter writer = new PrintWriter(new FileWriter(filePath));
            writer.println("Journal created for " + date);
            writer.close();
            System.out.println("\nJournal saved successfully to " + filePath);
        } catch (IOException e) {
            System.out.println("Error creating journal: " + e.getMessage());
        }
    }

    //ref bliali
    public void viewJournal(LocalDate date) {
        WeatherRecorder recorder = new WeatherRecorder();
        WeatherResult result = recorder.getWeather("Kuala Lumpur");
        String filePath = directoryPath() + "/" + date + ".txt";
        try {
            String content = Files.readString(Path.of(filePath));
            System.out.println("\n----------------------------------------");
            System.out.println("JOURNAL ENTRY - " + date);
            System.out.println("Location: " + result.location);
            System.out.println("Weather: " + result.weather);
            System.out.println("----------------------------------------");
            System.out.println(content);
            System.out.println("----------------------------------------");
            System.out.println("Press Enter to go back...");
            new Scanner(System.in).nextLine();
        } catch (IOException e) {
            System.out.println("Error reading journal: " + e.getMessage());
        }
    }

    //ref bliali
    public void editJournal(LocalDate date) {
        String filePath = directoryPath() + "/" + date + ".txt";
        Scanner scan = new Scanner(System.in);
        StringBuilder contentBuilder = new StringBuilder();

        try {
            if (doesJournalExist(date)) {
                System.out.println("\n[Current Content]:\n" + Files.readString(Path.of(filePath)));
                System.out.println("----------------------------------------");
            }

            System.out.println("Type your journal entry. (Type 'SAVE' on a new line to finish):");
            
            while (true) {
                String line = scan.nextLine();
                // Check for exit keyword
                if (line.equalsIgnoreCase("SAVE")) {
                    break;
                }
                contentBuilder.append(line).append(System.lineSeparator());
            }

            Files.writeString(Path.of(filePath), contentBuilder.toString());
            System.out.println("\nJournal saved successfully!");
            
        } catch (IOException e) {
            System.out.println("Error saving journal: " + e.getMessage());
        }
    }

    public String directoryPath(){
        // Create the directory path: user_journal/[username]/
        String username = user.getDisplayName();
        String directoryPath = "user_journal/" + username;
        File directory = new File(directoryPath);
        
        // Create the directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        return directoryPath;
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

    public boolean doesJournalExist(LocalDate date) {
        return new File(directoryPath() + "/" + date + ".txt").exists();
    }

}