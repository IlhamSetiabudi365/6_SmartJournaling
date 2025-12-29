import java.io.File;
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
        String path = "./journals/"+ user.getEmail() + "/" + date + ".txt";
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
    }

    public void viewJournal(LocalDate date) {
    }

    public void editJournal(LocalDate date) {
    }
}