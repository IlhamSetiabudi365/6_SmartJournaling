import java.io.*; // Import any standard libraries you need

public class WeatherRecorder {

    private static final String BASE_URL = "https://api.data.gov.my/weather/forecast/?contains=";
    private static final String END_URL = "@location__location_name&sort=date&limit=1";

    public WeatherResult getWeather(String city) {
        String dynamicUrl = BASE_URL + city.replace(" ", "%20") + END_URL;
        API api = new API();
        
        try {
            String jsonResponse = api.get(dynamicUrl);

            // Updated smart extraction (handles spaces)
            String location = extractValue(jsonResponse, "\"location_name\"");
            String weather = extractValue(jsonResponse, "\"summary_forecast\"");

            return new WeatherResult(location, weather);

        } catch (Exception e) {
            return new WeatherResult("Unknown", "Offline");
        }
    }

    private String extractValue(String json, String key) {
        int keyIndex = json.indexOf(key);
        if (keyIndex != -1) {
            int searchStart = keyIndex + key.length();
            int valueStartIndex = json.indexOf("\"", searchStart);
            if (valueStartIndex != -1) {
                valueStartIndex++; // skip opening quote
                int valueEndIndex = json.indexOf("\"", valueStartIndex);
                if (valueEndIndex != -1) {
                    return json.substring(valueStartIndex, valueEndIndex);
                }
            }
        }
        return "Not Found";
    }
}

class WeatherResult {
    public String location;
    public String weather;

    public WeatherResult(String location, String weather) {
        this.location = location;
        this.weather = weather;
    }
}