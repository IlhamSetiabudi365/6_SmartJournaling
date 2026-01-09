import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {

    /**
     * Loads environment variables from a .env file into a Map.
     * Each line should be in KEY=VALUE format.
     * Lines starting with '#' or empty lines are ignored.
     * 
     * @param filePath the path to the .env file
     * @return a Map containing the environment variables as key-value pairs
     */
    public static Map<String, String> loadEnv(String filePath) {
        Map<String, String> env = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                // Skip empty lines or comments
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                // Split on the first '=' only
                String[] parts = line.split("=", 2);

                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    // Optionally, remove quotes from value (if you want to support that)
                    if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                        value = value.substring(1, value.length() - 1);
                    }

                    env.put(key, value);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load .env file: " + e.getMessage());
        }
        
        return env;
    }
}
