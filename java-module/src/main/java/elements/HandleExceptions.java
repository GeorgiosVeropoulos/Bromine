package elements;

import exceptions.*;
import json.JsonParser;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleExceptions {


    public static void handleResponse(Response response, String customMessage) throws RuntimeException {
        if (!(response.get("value") instanceof Map<?, ?>)) {
            return;
        }

        Map<String, Object> valueMap = (Map<String, Object>) response.get("value");
        if (valueMap == null) {
            return;
        }

        // Check if the response contains an error message
        if (valueMap.containsKey("error") && valueMap.get("error") instanceof String errorMessage) {

            // Optionally, get the stacktrace if it exists
//            String stackTrace = valueMap.containsKey("stacktrace") ? (String) valueMap.get("stacktrace") : null;

            // Throw exceptions based on the error message content
            switch (errorMessage) {
                case "no such element":
                    throw new NoSuchElementException(customMessage);
                case "no such window":
                    throw new NoSuchWindowException(customMessage);
                case "no such frame":
                    throw new NoSuchFrameException(customMessage);
                case "stale element reference":
                    throw new StaleElementReferenceException(customMessage);
                case "element click intercepted":
                    String wholeMessage = decodeUnicodeSequences((String) JsonParser.findValueByKey(valueMap, "message"));
                    String clickablePointText = "Element is not clickable at point " + extractCoordinates(wholeMessage);
                    String otherElementText = "Other element would receive the click: " + extractOtherElement(wholeMessage);
                    throw new ElementClickInterceptedException(customMessage + "\n" +clickablePointText + "\n" + otherElementText);
                    // Add more cases for different error messages as needed
                default:
                    throw new RuntimeException("An unexpected error occurred: " + errorMessage);
            }
        }
    }


    // Method to extract the coordinates
    private static String extractCoordinates(String text) {
        Pattern pattern = Pattern.compile("\\(\\d+,\\s*\\d+\\)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "Coordinates not found";
    }

    // Method to extract the other element
    private static String extractOtherElement(String text) {
        // Look for the "Other element would receive the click" section
        Pattern pattern = Pattern.compile("Other element would receive the click: <(.+?)>");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return "<" + matcher.group(1) + ">";
        }
        return "Other element not found";
    }

    private static String decodeUnicodeSequences(String text) {
        return text
                .replaceAll("\\\\u003C", "<")  // Decode \u003C to <
                .replaceAll("\\\\u003E", ">");  // Decode \u003E to >
    }

}
