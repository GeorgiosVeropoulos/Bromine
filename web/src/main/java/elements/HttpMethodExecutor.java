package elements;

import capabilities.Configuration;
import enums.HttpMethod;
import json.JsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * !!!UNDER CONSTRUCTION!!!
 */
@Slf4j
public class HttpMethodExecutor {



    protected static Response START(String bodyToSend) {
        try {
            return doRequest(HttpMethod.POST, "/session", bodyToSend);
        } catch (IOException e) {
//            throw new WebDriverException("EXCEPTION IN CREATING SESSION");
//            do nothing for now.
        }
        return new Response("{}");
    }

    protected static Response doPostRequest(String endPoint, String bodyToSend) {
        try {
            return doRequest(HttpMethod.POST, "/session/" + DriverClient.sessionId() + endPoint, bodyToSend);
        } catch (IOException e) {
            throw new RuntimeException("Exception found when trying to execute Post Request\n" +
                    "End point: "+ endPoint + "\n" +
                    "with body: " + bodyToSend + "\n" + e.getMessage());
        }
    }

    protected static Response doGetRequest(String endPoint) {
        try {
            return doRequest(HttpMethod.GET, "/session/" + DriverClient.sessionId() + endPoint, null);
        } catch (IOException e) {
            throw new RuntimeException("Exception found when trying to execute GET Request\n" +
                    "End point: "+ endPoint + "\n" + e.getMessage());
        }
    }


    protected static void doDeleteRequest(String endPoint) {
        try {
            URL url = new URL(Configuration.getDriverUrl() + "/session/" + DriverClient.sessionId() + endPoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.DELETE.getMethod());

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Delete call successfully.");
                connection.getResponseMessage();

            } else {
                System.err.println("Failed to Delete call. Response code: " + responseCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * HTTP request helper for GET AND POST REQUESTS
     * @param requestMethod the type we want.
     * @param endPoint the end point we aim to hit MUST START WITH /
     * @param bodyToSend the json body we send {@link JsonBuilder#build()}
     * @return a {@link Response} with the acquired data in a form of a {@code Map<String, Object}
     * @throws IOException for any exception happening.
     */
    private static Response doRequest(HttpMethod requestMethod, String endPoint, String bodyToSend) throws IOException{

        String URL = Configuration.getDriverUrl() + endPoint;
        log.info("Will do Request on {}", URL);
        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(Configuration.network().getConnectionTimeout());
        connection.setReadTimeout(Configuration.network().getReadTimeout());
        connection.setRequestMethod(requestMethod.getMethod());
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);

        if (bodyToSend != null) {
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = bodyToSend.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        int responseCode = connection.getResponseCode();
        log.info("HTTP Response Code: {}", responseCode);

        if (responseCode >= 200 && responseCode < 300) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                log.info("Response Body: {}", response.toString());
                return new Response(response.toString());
            }
        } else {
            // Error response: read from ErrorStream
            log.error("Error during session creation: HTTP Response Code {}", responseCode);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    errorResponse.append(responseLine.trim());
                }
                log.error("Error Response Body: {}", errorResponse.toString());
                return new Response(errorResponse.toString());
            }
        }
    }

    private static HttpURLConnection getHttpURLConnection(HttpMethod requestMethod, String endPoint, String bodyToSend) throws IOException {
        URL url = new URL(Configuration.getDriverUrl()  + endPoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod.getMethod());
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);

        if (bodyToSend != null) {
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = bodyToSend.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }
        return connection;
    }

}
