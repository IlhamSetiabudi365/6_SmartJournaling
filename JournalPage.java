import java.io.*;
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

        //calculate and diplay dates from 4 days ago up to and including today
        System.out.println("=== Journal Dates ===");
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
        int choice = sc.nextInt();
        LocalDate chosenDate = today.minusDays(5-choice);
        showDateActions(chosenDate);
    }
    
    //helper method to check if a file exists for that date
    public boolean doesJournalExist(LocalDate date){
        String username = user.getDisplayName();
        String path = "user_journal/" + username + "/" + date + ".txt";
        File file = new File(path);
        return file.exists();
    }
    
    public void showDateActions(LocalDate chosenDate){
        Scanner sc = new Scanner(System.in);

        if (doesJournalExist(chosenDate)){
            //if a journal exists(true), user can VIEW or EDIT
            System.out.println("\nJournal found for " + chosenDate);
            System.out.println("1. View Entry");
            System.out.println("2. Edit Entry");
            System.out.print("Choice: ");
            int choice = sc.nextInt();
        
            if (choice == 1) viewJournal(chosenDate);
            else if (choice == 2) editJournal(chosenDate);
        }else{
            //if a journal doesn't exist(false), user can only CREATE
            System.out.println("\nNo journal found for " + chosenDate);
            System.out.println("1. Create New Entry");
            System.out.print("Choice: ");
            int choice = sc.nextInt();
        
            if (choice == 1) createJournal(chosenDate);
        }
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

         String directoryPath = directoryPath();
        String filePath = directoryPath + "/" + date + ".txt";


       try {
        BufferedReader inputStream = new BufferedReader (new FileReader(filePath));
        System.out.println("----------------------------------------");
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }

        System.out.println("----------------------------------------");
         System.out.println("Do you want to delete all the journal content?\n1. Yes\n2. No");
        inputStream.close();
        } catch (FileNotFoundException e) {
        System.out.println("File was not found");  
        } catch (IOException e) {
        System.out.println("Problem reading the file");
        }

    }


    public void editJournal(LocalDate date) {
        try {
            Scanner scan = new Scanner(System.in);
            // Get the directory path and ensure it exists
            String directoryPath = directoryPath();
            
            String filePath = directoryPath + "/" + date + ".txt";
            System.out.println("----------------------------------------");
            PrintWriter writer = new PrintWriter(new FileWriter(new File(filePath), true));
            String input = scan.nextLine();
            writer.println(input);
            scan.close();
            System.out.println("----------------------------------------");
            System.out.println("Do you want to delete all the journal content?\n1. Yes\n2. No");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error editing journal: " + e.getMessage());
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
}