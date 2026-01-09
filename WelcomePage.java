import java.time.LocalTime;
import java.util.Scanner;

public class WelcomePage {
    private static User user;

    public WelcomePage(User user) {
        this.user = user;
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        welcomeMessage();
        
        // Welcome Page
        System.out.println("Welcome");
        System.out.println("================================");
        System.out.println("===        Smart Journal      ===");
        System.out.println("================================");
        System.out.println("by : AkuSukaMakanBakso");
        System.out.println();
        
        // Menu Page
        System.out.println("Menu:");
        System.out.println("1. Journal Page         (press 1)");
        System.out.println("2. Sentiment Analysis  (press 2)");
        System.out.println("3. Summary Page        (press 3)");
        System.out.println("0. Exit                (press 0)");

        System.out.print(">> ");
        int choice = sc.nextLine();
        
        switch(choice){
            case 1 -> {
                //link to journal page
            }case 2 -> {
                //??
            }case 3 -> {
                //??
            }case 0 -> {
                //exit ide
            }default ->{
                //error message
            }
        }

    }

    public static void welcomeMessage(){
            LocalTime timeNow = LocalTime.now();
            if (timeNow.isAfter(LocalTime.of(0, 0))&& timeNow.isBefore(LocalTime.of(11, 59))){
                System.out.println("Good Morning" + user.getDisplayName());
            } else if(timeNow.isAfter(LocalTime.of(12, 0))&& timeNow.isBefore(LocalTime.of(16, 59))){
                System.out.println("Good Afternoon" + user.getDisplayName());
            } else if (timeNow.isAfter(LocalTime.of(17, 0))&& timeNow.isBefore(LocalTime.of(23, 59))){
                System.out.println("Good Evening" + user.getDisplayName());
            }
        }
}
