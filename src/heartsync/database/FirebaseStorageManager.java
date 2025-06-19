package heartsync.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class FirebaseStorageManager {
    private static final String STORAGE_URL = "https://firebasestorage.googleapis.com/v0/b/heartsync-96435.appspot.com/o/";
    private static final String STORAGE_TOKEN = "?alt=media";

    public static String uploadProfilePicture(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        // Generate a unique filename
        String fileName = "profile_pictures/" + UUID.randomUUID().toString() + "_" + file.getName();
        String encodedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");

        // Create upload URL
        URL url = new URL(STORAGE_URL + encodedFileName);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/octet-stream");

        // Read and upload file
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                conn.getOutputStream().write(buffer, 0, bytesRead);
            }
        }

        // Get response
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to upload file. Response code: " + responseCode);
        }

        // Return the download URL
        return STORAGE_URL + encodedFileName + STORAGE_TOKEN;
    }

    public static void deleteProfilePicture(String downloadUrl) throws IOException {
        if (downloadUrl == null || !downloadUrl.startsWith(STORAGE_URL)) {
            return;
        }

        // Extract file path from download URL
        String filePath = downloadUrl.substring(STORAGE_URL.length());
        filePath = filePath.substring(0, filePath.indexOf(STORAGE_TOKEN));
        
        // Create delete URL
        URL url = new URL(STORAGE_URL + filePath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to delete file. Response code: " + responseCode);
        }
    }
} 