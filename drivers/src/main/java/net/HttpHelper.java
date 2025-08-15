package net;

import lombok.extern.slf4j.Slf4j;
import org.bromine.utils.net.HttpMethod;
import org.bromine.utils.net.HttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


@Slf4j
public class HttpHelper {




    public static String get(String urlToGoTo) {
        StringBuilder response = new StringBuilder();

        try {
            HttpURLConnection connection = HttpUtil.with(HttpMethod.GET)
                    .forUrl(urlToGoTo)
                    .withProperty("User-Agent", "Java Chrome Fetcher")
                    .getConnection();
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
            log.error("Failed to fetch JSON from {}", urlToGoTo);
            throw new RuntimeException("Failed to fetch JSON", e);
        }

        return response.toString();
    }

    public static void downloadTo(String urlToGoTo, Path destination) {
        try {
            HttpURLConnection connection = HttpUtil.with(HttpMethod.GET)
                    .forUrl(urlToGoTo)
                    .withProperty("User-Agent", "Java Chrome Fetcher")
                    .getConnection();
            // Create directories if not exist
            Files.createDirectories(destination.getParent());

            // Stream content directly to file
            try (InputStream in = connection.getInputStream()) {
                Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            }

            connection.disconnect();
        } catch (IOException e) {
            log.error("Failed to download file from {}", urlToGoTo);
            throw new RuntimeException("Failed to download file", e);
        }
    }
}
