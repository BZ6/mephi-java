import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Locale;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class WeatherApp {

    private static final String API_URL = "https://api.weather.yandex.ru/v2/forecast";
    private static final double SAINT_PETERSBURG_LAT = 59.94;
    private static final double SAINT_PETERSBURG_LON = 30.31;
    private static final int FORECAST_DAYS = 3;
    private static final String API_KEY_FILE = "config.properties";
    private static final String API_KEY_PROPERTY = "api.key";

    public static void main(String[] args) {
        String apiKey = loadApiKey(API_KEY_FILE);
        if (apiKey == null) {
            System.out.println("Failed to load API key. Exiting.");
            System.exit(1);
        }

        try {
            String responseBody = fetchWeather(apiKey);
            JsonObject weather = new Gson().fromJson(responseBody, JsonObject.class);

            System.out.println("=== Full JSON response ===");
            System.out.println(responseBody);
            System.out.println();

            double currentTemperature = weather.getAsJsonObject("fact").get("temp").getAsDouble();
            System.out.println("Current temperature: " + currentTemperature + " °C");

            JsonArray forecasts = weather.getAsJsonArray("forecasts");
            int dayCount = Math.min(FORECAST_DAYS, forecasts.size());

            double sum = 0;
            for (int i = 0; i < dayCount; i++) {
                double dayTemperature = forecasts.get(i).getAsJsonObject()
                        .getAsJsonObject("parts")
                        .getAsJsonObject("day")
                        .get("temp_avg")
                        .getAsDouble();
                System.out.println("Day " + (i + 1) + ": day temperature " + dayTemperature + " °C");
                sum += dayTemperature;
            }

            double average = sum / dayCount;
            System.out.println("Average day temperature for " + dayCount + " days: "
                    + String.format(Locale.US, "%.1f", average) + " °C");

        } catch (IOException | InterruptedException e) {
            System.out.println("Failed to execute request: " + e.getMessage());
            System.exit(1);
        }

        System.exit(0);
    }

    private static String loadApiKey(String fileName) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(fileName)) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Failed to read " + fileName + ": " + e.getMessage());
            return null;
        }

        String key = properties.getProperty(API_KEY_PROPERTY);
        if (key == null || key.isBlank()) {
            System.out.println(API_KEY_PROPERTY + " is not set in " + fileName);
            return null;
        }
        return key.trim();
    }

    private static String fetchWeather(String apiKey) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder(URI.create(buildRequestUri()))
                .timeout(Duration.ofSeconds(10))
                .header("X-Yandex-Weather-Key", apiKey)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("server returned status " + response.statusCode()
                    + ": " + response.body());
        }
        return response.body();
    }

    private static String buildRequestUri() {
        return API_URL
                + "?lat=" + SAINT_PETERSBURG_LAT
                + "&lon=" + SAINT_PETERSBURG_LON
                + "&limit=" + FORECAST_DAYS
                + "&lang=ru_RU&hours=false";
    }
}
