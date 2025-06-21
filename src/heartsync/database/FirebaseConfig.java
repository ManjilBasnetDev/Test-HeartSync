package heartsync.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FirebaseConfig {
    private static final String DATABASE_URL = "https://heartsync-96435-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static final String JSON_EXT = ".json?auth=null"; // Allow unauthenticated access during development
    private static final Gson gson = new Gson();
    private static final HttpClient client = HttpClient.newHttpClient();

    public static String getUserPath(String userId) {
        return "users/" + userId;
    }

    public static <T> T get(String path, Type type) throws IOException {
        URL url = new URL(DATABASE_URL + path + JSON_EXT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                return gson.fromJson(response.toString(), type);
            }
        } else {
            throw new IOException("HTTP error code: " + responseCode);
        }
    }

    public static void put(String path, Object data) throws IOException {
        URL url = new URL(DATABASE_URL + path + JSON_EXT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonData = gson.toJson(data);
        System.out.println("Sending data to: " + url.toString());
        System.out.println("Data: " + jsonData);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            StringBuilder errorResponse = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    errorResponse.append(line);
                }
            }
            throw new IOException("Failed to write to database. Response code: " + responseCode + ", Error: " + errorResponse.toString());
        }
    }

    public static void delete(String path) throws IOException {
        URL url = new URL(DATABASE_URL + path + JSON_EXT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to delete. Response code: " + responseCode);
        }
    }

    // POST (push new child, returns key)
    public static String post(String path, Object data) throws IOException {
        String url = DATABASE_URL + path + JSON_EXT;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        String json = gson.toJson(data);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        try (InputStream in = conn.getInputStream()) {
            String resp = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> map = gson.fromJson(resp, Map.class);
            return map.get("name"); // Firebase returns {"name":"generatedKey"}
        }
    }

    // PATCH (update fields at path)
    public static void patch(String path, Object data) throws IOException {
        String url = DATABASE_URL + path + JSON_EXT;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        String json = gson.toJson(data);
        
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            return;
        }
        conn.getInputStream().close();
    }

    // Helper for /matches/{userId}/{otherUserId}
    public static String getMatchPath(String userId, String otherUserId) {
        return "matches/" + userId + "/" + otherUserId;
    }

    public static <T> void set(String path, T data) {
        try {
            String json = gson.toJson(data);
            String fullUrl = DATABASE_URL + path + JSON_EXT;
            System.out.println("Saving data to Firebase URL: " + fullUrl);
            System.out.println("Data being saved: " + json);
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Firebase response status: " + response.statusCode());
            System.out.println("Firebase response body: " + response.body());
            
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to set data: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set data: " + e.getMessage());
        }
    }

    // Delete all data from Firebase
    public static void deleteAllData() throws IOException {
        // Delete all user data nodes
        delete("user_details");
        delete("user_likes");
        delete("user_passes");
        delete("matches");
        delete("messages");
        delete("user_notifications");
        delete("user_reports");
        delete("user_contacts");
        System.out.println("All user data has been deleted from Firebase.");
    }
}
