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
        int option = sc.nextInt();
        LocalDate chosenDate = today.minusDays(5-option);
    }

    public void createJournal(LocalDate date) {
    }

    public void viewJournal(LocalDate date) {
    }

    public void editJournal(LocalDate date) {
    }
}