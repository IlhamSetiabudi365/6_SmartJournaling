import java.util.Map;

public class SentimentAnalyzer {
    // The AI Model URL
    private static final String MODEL_URL = "https://router.huggingface.co/hf-inference/models/distilbert/distilbert-base-uncased-finetuned-sst-2-english";

    public String analyze(String journalText) {
        API api = new API();
        
        // 1. LOAD THE TOKEN (This was missing before!)
        Map<String, String> env = EnvLoader.loadEnv(".env");
        String token = env.get("BEARER_TOKEN");

        if (token == null || token.isEmpty()) {
            System.out.println("[Error] BEARER_TOKEN not found in .env file.");
            return "UNKNOWN";
        }

        // 2. Prepare JSON
        String safeText = journalText.replace("\\", "\\\\")   // Escape backslashes first!
                                     .replace("\"", "\\\"")   // Escape quotes
                                     .replace("\r", "")       // Remove weird Windows returns
                                     .replace("\n", "\\n");   // Turn real newlines into \n text
        String jsonBody = "{\"inputs\": \"" + safeText + "\"}";

        System.out.println("Analyzing mood...");
        
        try {
            // 3. Send to AI
            String response = api.post(MODEL_URL, token, jsonBody);

            // 4. Extract Result (Simple Parsing)
            if (response != null && response.contains("label")) {
                // Find the start of the label
                int labelStartIndex = response.indexOf("\"label\":\"") + 9;
                // Find the end of the label
                int labelEndIndex = response.indexOf("\"", labelStartIndex);
                
                // Return "POSITIVE" or "NEGATIVE"
                return response.substring(labelStartIndex, labelEndIndex);
            }
        } catch (Exception e) {
            System.out.println("Sentiment API Error: " + e.getMessage());
        }
        
        return "UNKNOWN";
    }
}