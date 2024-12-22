package json;

import elements.HandleExceptions;

import java.util.*;

public final class JsonParser {

    public static Map<String, Object> parse(String jsonString) {
        jsonString = jsonString.trim();
        if (jsonString.startsWith("{")) {
            return parseObject(jsonString);
        } else if (jsonString.startsWith("[")) {
            throw new IllegalArgumentException("Input is an array, not an object.");
        }
        throw new IllegalArgumentException("Invalid JSON string.");
    }

    private static Map<String, Object> parseObject(String jsonString) {
        Map<String, Object> map = new HashMap<>();
        jsonString = jsonString.substring(1, jsonString.length() - 1).trim(); // Remove outer braces
        String[] keyValuePairs = splitJson(jsonString);

        for (String pair : keyValuePairs) {
            String[] keyValue = splitKeyValue(pair);
            String key = keyValue[0].trim().replace("\"", ""); // Remove quotes from key
            Object value = parseValue(keyValue[1].trim());
            map.put(key, value);
        }
        return map;
    }

    private static Object parseValue(String value) {
        if (value.startsWith("\"")) {
            return value.substring(1, value.length() - 1); // String
        } else if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value); // Boolean
        } else if (value.equals("null")) {
            return null; // Null
        } else if (value.startsWith("{")) {
            return parseObject(value); // Nested object
        } else if (value.startsWith("[")) {
            return parseArray(value); // Array
        } else {
            return parseNumber(value); // Number
        }
    }

    private static List<Object> parseArray(String jsonString) {
        List<Object> list = new ArrayList<>();
        jsonString = jsonString.substring(1, jsonString.length() - 1).trim(); // Remove outer brackets
        String[] values = splitJson(jsonString);

        for (String value : values) {
            list.add(parseValue(value.trim()));
        }
        return list;
    }

    private static String[] splitJson(String jsonString) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        int braceCount = 0;
        boolean inString = false;

        for (char ch : jsonString.toCharArray()) {
            if (ch == '\"') {
                inString = !inString; // Toggle string status
            }

            if (!inString) {
                if (ch == '{' || ch == '[') {
                    braceCount++;
                } else if (ch == '}' || ch == ']') {
                    braceCount--;
                } else if (ch == ',' && braceCount == 0) {
                    // When braceCount is 0, we can split here
                    parts.add(currentPart.toString().trim());
                    currentPart.setLength(0);
                    continue;
                }
            }

            currentPart.append(ch);
        }

        // Add the last part if there's any remaining
        if (currentPart.length() > 0) {
            parts.add(currentPart.toString().trim());
        }

        return parts.toArray(new String[0]);
    }

    // Split the key-value pair correctly, ensuring we find the colon correctly
    private static String[] splitKeyValue(String pair) {
        int colonIndex = findFirstColonOutsideQuotes(pair);
        if (colonIndex == -1) {
            throw new IllegalArgumentException("Invalid key-value pair: " + pair);
        }
        return new String[]{pair.substring(0, colonIndex).trim(), pair.substring(colonIndex + 1).trim()};
    }

    private static int findFirstColonOutsideQuotes(String str) {
        boolean inQuotes = false;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '\"') {
                inQuotes = !inQuotes; // Toggle quotes
            }
            if (ch == ':' && !inQuotes) {
                return i; // Return the index of the first colon outside quotes
            }
        }
        return -1; // Not found
    }

    private static Object parseNumber(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e2) {
                throw new IllegalArgumentException("Invalid number format: " + value);
            }
        }
    }


    public static Object findValueByKey(Map<String, Object> jsonMap, String key) {
        Stack<Map<String, Object>> stack = new Stack<>();
        stack.push(jsonMap);

        while (!stack.isEmpty()) {
            Map<String, Object> currentMap = stack.pop();

            for (Map.Entry<String, Object> entry : currentMap.entrySet()) {
                if (entry.getKey().equals(key)) {
                    return entry.getValue(); // Key found
                }

                // If the value is a nested Map, push it onto the stack
                if (entry.getValue() instanceof Map) {
                    stack.push((Map<String, Object>) entry.getValue());
                }

                // If the value is a List, process it
                if (entry.getValue() instanceof List) {
                    List<Object> list = (List<Object>) entry.getValue();
                    for (Object item : list) {
                        if (item instanceof Map) {
                            stack.push((Map<String, Object>) item);
                        }
                    }
                }
            }
        }
        return null; // Key not found
    }

    private static Object findInList(List<Object> jsonList, String key) {
        for (Object item : jsonList) {
            if (item instanceof Map) {
                Object found = findValueByKey((Map<String, Object>) item, key);
                if (found != null) {
                    return found;
                }
            }
        }
        return null; // Key not found in the list
    }
}
