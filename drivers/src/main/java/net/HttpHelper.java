package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class HttpHelper {




    protected static String get(String urlToGoTo) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(urlToGoTo);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Java Chrome Fetcher");

            int status = connection.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + status);
            }

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine).append("\n");
                }
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch JSON", e);
        }

        return response.toString();
    }

    public static void downloadTo(String urlToGoTo, Path destination) {
        try {
            URL url = new URL(urlToGoTo);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Java Chrome Fetcher");

            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + status);
            }

            // Create directories if not exist
            Files.createDirectories(destination.getParent());

            // Stream content directly to file
            try (InputStream in = connection.getInputStream()) {
                Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to download file", e);
        }
    }
}
