package org.bromine.utils.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * Utility class to create HttpURLConnection instances.
 * This class is designed to simplify the process of setting up HTTP requests.
 * It allows you to specify the HTTP method, URL, headers, and body.
 */
public class HttpUtil {

    private final HttpMethod method;
    private String url;
    private String body;
    private final Map<String, String> headers = new HashMap<>();

    private HttpUtil(HttpMethod method) {
        this.method = method;
    }

    public static HttpUtil with(HttpMethod method) {
        return new HttpUtil(method);
    }

    public HttpUtil forUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpUtil withBody(String body) {
        this.body = body;
        return this;
    }

    public HttpUtil withProperty(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public HttpUtil withProperties(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public HttpURLConnection getConnection() throws HttpRequestException {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

            connection.setRequestMethod(method.getMethod());
            connection.setDoInput(true);

            // Set headers
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // Write body if present
            if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
                connection.setDoOutput(true);
                if (body != null && !body.isEmpty()) {
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = body.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                }
            }

            return connection;

        } catch (IOException e) {
            throw new HttpRequestException("Failed to establish HTTP connection: " + e.getMessage(), e);
        }
    }

}