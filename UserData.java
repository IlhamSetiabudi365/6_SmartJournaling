// Only run this file to create UserData.txt
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class UserData {

    public static void main(String[] args) {
        createUserDataFile();
    }

    private static void createUserDataFile() {
        File file = new File("UserData.txt");

        if (file.exists()) {
            System.out.println("UserData.txt already exists. No changes made.");
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("UserData.txt"))) {
            
            writer.println("s100201@student.fop");
            writer.println("Foo Bar");
            writer.println("pw-Stud#1");
            writer.println();
            writer.println("s100202@student.fop");
            writer.println("John Doe");
            writer.println("pw-Stud#2");

            System.out.println("UserData.txt file created successfully.");

        } catch (IOException e) {
            System.out.println("Error creating UserData.txt: " + e.getMessage());
        }
    }   
}