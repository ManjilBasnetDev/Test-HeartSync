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
    private static final String FIREBASE_AUTH = "YOUR_FIREBASE_AUTH"; // Replace with your Firebase auth token
    private static final Gson gson = new Gson();
    private static final HttpClient client = HttpClient.newHttpClient();

    // Generic GET
    public static <T> T get(String path, Class<T> clazz) throws IOException {
        String url = DATABASE_URL + path + ".json";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            try (InputStream in = conn.getInputStream()) {
                String json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                return gson.fromJson(json, clazz);
            }
        }
        return null;
    }

    // Generic GET for TypeToken (for lists/maps)
    public static <T> T get(String path, Type typeOfT) throws IOException {
        String url = DATABASE_URL + path + ".json";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            try (InputStream in = conn.getInputStream()) {
                String json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                return gson.fromJson(json, typeOfT);
            }
        }
        return null;
    }

    // PUT (create/replace at path)
    public static void put(String path, Object data) throws IOException {
        String url = DATABASE_URL + path + ".json";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        String json = gson.toJson(data);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        conn.getInputStream().close();
    }

    // POST (push new child, returns key)
    public static String post(String path, Object data) throws IOException {
        String url = DATABASE_URL + path + ".json";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
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
        String url = DATABASE_URL + path + ".json";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("PATCH");
        conn.setDoOutput(true);
        String json = gson.toJson(data);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        conn.getInputStream().close();
    }

    // DELETE
    public static void delete(String path) throws IOException {
        String url = DATABASE_URL + path + ".json";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("DELETE");
        conn.getInputStream().close();
    }

    // Helper for /users/{userId}
    public static String getUserPath(String userId) {
        return "users/" + userId;
    }
    // Helper for /chats/{chatId}
    public static String getChatPath(String chatId) {
        return "chats/" + chatId + "/messages";
    }
    // Helper for /matches/{userId}/{otherUserId}
    public static String getMatchPath(String userId, String otherUserId) {
        return "matches/" + userId + "/" + otherUserId;
    }

    public static <T> void set(String path, T data) {
        try {
            String json = gson.toJson(data);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DATABASE_URL + "/" + path + ".json"))
                .header("Authorization", "Bearer " + FIREBASE_AUTH)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set data: " + e.getMessage());
        }
    }
}
