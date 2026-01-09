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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class JournalPage{
    private User user;

    public JournalPage(User user) {
        new JFXPanel(); // Initializes the toolkit
        Platform.setImplicitExit(false); // <--- ADD THIS LINE
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
    // Run file reading off-thread
    new Thread(() -> {
        try {
            Path path = Path.of(directoryPath(), date.toString() + ".txt");
            if (!Files.exists(path)) {
                showError("File not found for: " + date);
                return;
            }
            String content = Files.readString(path);

            Platform.runLater(() -> {
                Stage stage = new Stage();
                // Ensure the window stays on top of the console if possible
                stage.setAlwaysOnTop(true); 
                
                TextArea textArea = new TextArea(content);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                Button closeBtn = new Button("Close");
                closeBtn.setOnAction(e -> stage.close());

                VBox layout = new VBox(10, textArea, closeBtn);
                layout.setPadding(new javafx.geometry.Insets(15));
                
                stage.setScene(new Scene(layout, 400, 300));
                stage.setTitle("View: " + date);
                stage.show();
            });
        } catch (IOException e) {
            showError("Error reading journal: " + e.getMessage());
        }
    }).start();
}

// Helper to show errors on the UI thread
private void showError(String message) {
    Platform.runLater(() -> {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    });
}


    public void editJournal(LocalDate date) {
        
        
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

    public boolean doesJournalExist(LocalDate date) {
        return new File(directoryPath() + "/" + date + ".txt").exists();
    }

}