import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

public class JournalPage {
    private User user;

    public JournalPage(User user) {
        this.user = user;
    }

    public void displayDates() {
        Scanner sc = new Scanner(System.in);
        // get date today
        LocalDate today = LocalDate.now();

        // calculate and display dates from 6 days ago up to and including today
        System.out.println("\n0. Main Menu");// \n=== Journal Dates ===\n
        for (int i = 6, j = 1; i >= 0; i--, j++) {
            LocalDate dateToShow = today.minusDays(i);
            System.out.print(j + ". " + dateToShow);
            if (i == 0) {
                System.out.print("(today)");
            }
            System.out.println("");
        }

        // option to view older dates
        System.out.println("\n8. Dates before " + today.minusDays(6));

        // prompt user
        System.out.print("\nSelect a date to view journal, or create a new journal for today: \n>> ");
        try {
            int choice = sc.nextInt();
            if (choice == 0)

                return; // Returns to Welcome/Main Page

            if (choice >= 1 && choice <= 7) {
                LocalDate chosenDate = today.minusDays(7 - choice);
                showDateActions(chosenDate);
            } else if (choice == 8) {
                displayOlderDates(today.minusDays(7));
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
            } else {
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

    // Inside JournalPage.java

    public void createJournal(LocalDate date) {
        try {
            String directoryPath = directoryPath();
            String filePath = directoryPath + "/" + date + ".txt";

            System.out.println("\n--- Creating New Journal Entry ---");

            // 1. Get Weather (Live)
            System.out.print("Fetching weather... ");
            WeatherRecorder weatherBot = new WeatherRecorder();
            WeatherResult weatherRes = weatherBot.getWeather("Kuala Lumpur"); // Or your specific city
            System.out.println("Done.");

            // 2. Get User Input (Journal Content)
            Scanner scan = new Scanner(System.in);
            System.out.println("Type your journal entry below. (Type 'SAVE' on a new line to finish):");
            StringBuilder contentBuilder = new StringBuilder();

            while (true) {
                String line = scan.nextLine();
                if (line.equalsIgnoreCase("SAVE"))
                    break;
                contentBuilder.append(line).append(System.lineSeparator()); // Use lineSeparator for clean formatting
            }
            String fullText = contentBuilder.toString();

            // 3. Get Sentiment (AI Analysis)
            System.out.print("Analyzing mood... ");
            SentimentAnalyzer moodBot = new SentimentAnalyzer();
            String mood = moodBot.analyze(fullText);
            System.out.println("Done (" + mood + ").");

            // 4. SAVE EVERYTHING TO FILE
            PrintWriter writer = new PrintWriter(new FileWriter(filePath));
            writer.println("Journal created for " + date);
            writer.println("Location: " + weatherRes.location);
            writer.println("Weather: " + weatherRes.weather); // Key for Summary Page
            writer.println("MOOD: " + mood); // Key for Summary Page
            writer.println("----------------------------------------");
            writer.println(fullText);
            writer.close();

            System.out.println("\nJournal saved successfully to " + filePath);

        } catch (IOException e) {
            System.out.println("Error creating journal: " + e.getMessage());
        }
    }

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
            String fullText = contentBuilder.toString();

            // 3. Get Sentiment (AI Analysis)
            System.out.print("Analyzing mood... ");
            SentimentAnalyzer moodBot = new SentimentAnalyzer();
            String mood = moodBot.analyze(fullText);
            System.out.println("Done (" + mood + ").");

            PrintWriter writer = new PrintWriter(new FileWriter(filePath));
            
            WeatherRecorder weatherBot = new WeatherRecorder();
            WeatherResult weatherRes = weatherBot.getWeather("Kuala Lumpur");

            writer.println("Journal edited for " + date);
            writer.println("Location: " + weatherRes.location);
            writer.println("Weather: " + weatherRes.weather); // Key for Summary Page
            writer.println("MOOD: " + mood); // Key for Summary Page
            writer.println("----------------------------------------");
            writer.println(fullText);
            writer.close();

            System.out.println("\nJournal saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving journal: " + e.getMessage());
        }
    }

    public String directoryPath() {
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

    // view older dates
    public void displayOlderDates(LocalDate date) {
        Scanner sc = new Scanner(System.in);

        // calculate and display dates from a week ago
        System.out.println("\n=== Journal Dates ===\n0. Back to Recent Dates");
        for (int i = 0; i < 7; i++) {
            LocalDate dateToShow = date.plusDays(i);
            int menuNum = i + 1;
            System.out.println(menuNum + ". " + dateToShow);
        }

        // option to view even older dates
        System.out.println("\n8. Dates before " + date);

        // prompt user
        System.out.print("\nSelect a date to view journal, or create a new journal: \n>> ");
        try {
            int choice = sc.nextInt();
            if (choice == 0) {
                displayDates(); // Returns back to current week (displayDates())
                return;
            }
            if (choice >= 1 && choice <= 7) {
                LocalDate chosenDate = date.minusDays(choice - 1);
                showDateActions(chosenDate);
            } else if (choice == 8) {
                displayOlderDates(date.minusDays(7));
            } else {
                System.out.println("Invalid choice. Try again.");
                displayOlderDates(date);
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number.");
            displayDates();
        }
    }

}