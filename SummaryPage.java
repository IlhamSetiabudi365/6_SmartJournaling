import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class SummaryPage {
    private User user;

    public SummaryPage(User user) {
        this.user = user;
    }

    public void showSummary() {
        System.out.println("\n========================================");
        System.out.println("          WEEKLY JOURNAL SUMMARY        ");
        System.out.println("========================================");
        System.out.printf("%-12s | %-15s | %-10s%n", "DATE", "WEATHER", "MOOD");
        System.out.println("----------------------------------------");

        LocalDate today = LocalDate.now();

        // Loop back 7 days
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String summaryRow = getSummaryForDate(date);
            System.out.println(summaryRow);
        }
        
        System.out.println("========================================");
        System.out.println("Press Enter to return to menu...");
        new Scanner(System.in).nextLine();
        return;
    }

    private String getSummaryForDate(LocalDate date) {
        String directoryPath = "user_journal/" + user.getDisplayName();
        File file = new File(directoryPath + "/" + date + ".txt");

        if (!file.exists()) {
            return String.format("%-12s | %-15s | %-10s", date, "No Entry", "-");
        }

        String weather = "Unknown";
        String mood = "Unknown";

        try {
            // Read all lines to find the "Weather:" and "MOOD:" keys
            List<String> lines = Files.readAllLines(file.toPath());
            
            for (String line : lines) {
                if (line.startsWith("Weather: ")) {
                    weather = line.replace("Weather: ", "").trim();
                    // Shorten weather if it's too long for the table
                    if (weather.length() > 15) weather = weather.substring(0, 12) + "...";
                }
                if (line.startsWith("MOOD: ")) {
                    mood = line.replace("MOOD: ", "").trim();
                }
            }
        } catch (Exception e) {
            weather = "Error";
        }

        return String.format("%-12s | %-15s | %-10s", date, weather, mood);
    }
}