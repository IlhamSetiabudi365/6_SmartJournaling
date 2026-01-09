import java.io.*;
import java.time.LocalDate;
import java.util.*;
//imports for javaFX
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.nio.file.Files;
import java.nio.file.Path;

public class JournalPage{
    private User user;

    public JournalPage(User user) {
        this.user = user;
    }
    
    public void displayDates() {
        Scanner sc = new Scanner(System.in);
        //get date today
        LocalDate today = LocalDate.now();

        //calculate and diplay dates from 4 days ago up to and including today
        System.out.println("\n=== Journal Dates ===");
        for(int i=4 , j=1 ; i>=0 ; i-- , j++){
            LocalDate dateToShow = today.minusDays(i);
            System.out.print(j + "." + dateToShow);
            if (i==0){
                System.out.print("(today)");
            }
            System.out.println("");
        }

        //prompt user
        System.out.print("\nSelect a date to view journal, or create a new journal for today: \n>");
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
    
    //helper method to check if a file exists for that date
    public boolean doesJournalExist(LocalDate date){
        String username = user.getDisplayName();
        String path = "user_journal/" + username + "/" + date + ".txt";
        File file = new File(path);
        return file.exists();
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

    public void createJournal(LocalDate date) {
        try {
            // Get the directory path and ensure it exists
            String directoryPath = directoryPath();
            
            // Create the file with the date as the filename
            String filePath = directoryPath + "/" + date + ".txt";
            PrintWriter writer = new PrintWriter(new FileWriter(filePath));
            writer.println("Journal created for " + date);
            writer.close();
            System.out.println("Journal saved successfully to " + filePath);
        } catch (IOException e) {
            System.out.println("Error creating journal: " + e.getMessage());
        }
    }

    public void viewJournal(LocalDate date) {
        try{
            String directoryPath = directoryPath();
            
            String filePath = directoryPath + "/" + date + ".txt";
            Scanner scanner = new Scanner(new File(filePath));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }scanner.close();
            System.out.println("\n1. Edit Journal");
            System.out.println("2. Back to Dates");
            Scanner sc = new Scanner(System.in);
            System.out.print("Choice: ");
            int choice = sc.nextInt();
            switch(choice){
                case 1 -> {editJournal(date);}
                case 2 -> {displayDates();}
                default -> {System.out.println("invalid input.");}
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }


    public void editJournal(LocalDate date) {
        new JFXPanel();
        
        Platform.runLater(() ->{
        try {

            // Get the directory path and ensure it exists
            String directoryPath = directoryPath();
            String filePath = directoryPath + "/" + date + ".txt";
            Path path = Path.of(filePath);

            String content = Files.readString(path);

            Stage stage = new Stage();
            TextArea textArea = new TextArea(content);

            textArea.setPrefHeight(400);
            textArea.setWrapText(true);
            
            Button saveBtn = new Button("Save and Exit");
            
            saveBtn.setOnAction(e -> {
                try {
                    Files.writeString(path, textArea.getText());
                    System.out.println("File updated successfully!");
                    stage.close();
                    showDateActions(date);
                } catch (IOException ex) {
                    System.out.println("Error saving: " + ex.getMessage());
                }
            });

            VBox layout = new VBox(10, textArea, saveBtn);
            stage.setScene(new Scene(layout, 500, 450));
            stage.setTitle("Editing Journal: " + date);
            stage.show();

        
        } catch (IOException e) {
            System.out.println("Error editing journal: " + e.getMessage());
        }
        });
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

    // --- HELPER METHODS ---

    public boolean doesJournalExist(LocalDate date) {
        return new File(directoryPath() + "/" + date + ".txt").exists();
    }
}