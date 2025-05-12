package org.bromine.utils.net;

import json.JsonParser;

import java.util.HashMap;
import java.util.Map;


/**
 * Response is a helper class to handle the response from HTTP requests.
 * It extends HashMap to provide additional functionality for parsing and retrieving values.
 * It is designed to work with JSON data and provides methods to extract values in various formats.
 */
@SuppressWarnings("unchecked")
public class Response extends HashMap<String, Object> {

    // Constructor to directly load from a JSON string
    public Response(String jsonString) {
        super(JsonParser.parse(jsonString));
    }

    public Map<String, Object> getInnerHashMap(String key) {
        Object value = this.get(key);
        if (value instanceof Map<?, ?>) {
            return (Map<String, Object>) value;
        }
        throw new IllegalStateException("Value is not a Map: " + key);
    }

    public Map<String, String> getMapFor(String key) {
        Object value = this.get(key);
        if (value instanceof Map<?, ?> map) {
            Map<String, String> stringMap = new HashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object k = entry.getKey();
                Object v = entry.getValue();
                if (k != null && v != null) {
                    stringMap.put(k.toString(), v.toString());
                }
            }
            return stringMap;
        }
        throw new IllegalStateException("Value is not a Map: " + key);
    }

    public String getString(String key) {
        Object value = this.get(key);
        return value != null ? value.toString() : null;
    }

    public Integer getInt(String key) {
        Object value = this.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        try {
            return value != null ? Integer.parseInt(value.toString()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Boolean getBoolean(String key) {
        Object value = this.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return value != null && Boolean.parseBoolean(value.toString());
    }

}
