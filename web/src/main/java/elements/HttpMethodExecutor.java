package elements;

import capabilities.Configuration;
import json.JsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.bromine.utils.net.HttpMethod;
import org.bromine.utils.net.HttpResponseHandler;
import org.bromine.utils.net.HttpUtil;
import org.bromine.utils.net.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
            log.error("START DIDN'T HAPPEN LOOSE YOUR SHIT CABRON");
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
                log.debug("Delete call successfully.");
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
    private static Response doRequest(HttpMethod requestMethod, String endPoint, String bodyToSend) throws IOException {
        HttpURLConnection connection = HttpUtil.with(requestMethod)
                .forUrl(Configuration.getDriverUrl()  + endPoint)
                .withBody(bodyToSend)
                .withProperty("Content-Type", "application/json; charset=UTF-8")
                .getConnection();
        return HttpResponseHandler.handleResponse(connection);
    }

}
