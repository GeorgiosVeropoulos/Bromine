package elements;

import capabilities.Configuration;
import enums.HttpMethod;
import exceptions.WebDriverException;
import json.JsonBuilder;

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
public class HttpMethodExecutor {



    protected static Response START(String bodyToSend) {
        try {
            return doRequest(HttpMethod.POST, "/session", bodyToSend);
        } catch (IOException e) {
            //do nothing for now.
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


    protected static String doDeleteRequest(String endPoint) {
        try {
            URL url = new URL(Configuration.getGridUrl() + "/session/" + DriverClient.sessionId() + endPoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.DELETE.getMethod());

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Delete call successfully.");
                return connection.getResponseMessage();

            } else {
                System.err.println("Failed to Delete call. Response code: " + responseCode);
            }
            return null;
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

        URL url = new URL(Configuration.getGridUrl()  + endPoint);
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


        int responseCode = connection.getResponseCode();

        if (responseCode >= 200 && responseCode < 300) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

//                System.out.println(response);
                return new Response(response.toString());
//                return JsonParser.parse(response.toString());
            }
        } else {
            // Error response: read from ErrorStream
            System.err.println("Error during session creation: HTTP Response Code " + responseCode);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    errorResponse.append(responseLine.trim());
                }
                System.err.println("Error response: " + errorResponse.toString());
                return new Response(errorResponse.toString());
//                return JsonParser.parse(errorResponse.toString());
            }
        }
    }

}
