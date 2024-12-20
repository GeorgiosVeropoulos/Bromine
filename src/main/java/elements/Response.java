package elements;

import json.JsonParser;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to handle the response from HTTP requests.
 */
public class Response extends HashMap<String, Object> {

    // Constructor to directly load from a JSON string
    public Response(String jsonString) {
        super(JsonParser.parse(jsonString));
    }


    public Map<String, Object> getInnerHashMap(String key) {
        if (super.get(key) instanceof Map<?,?>)
            return (Map<String, Object>) super.getOrDefault(key, "all ok");
        return new HashMap<>();
    }

    public Map<String, String> getValueAsMap() {
        return (Map<String, String>) super.get("value");
    }




    // Additional helper methods to make it easier to work with JSON data
    public String getString(String key) {
        Object value = this.get(key);
        return value != null ? value.toString() : null;
    }

    public Integer getInt(String key) {
        Object value = this.get(key);
        return value instanceof Integer ? (Integer) value : null;
    }

    public Boolean getBoolean(String key) {
        Object value = this.get(key);
        return value instanceof Boolean ? (Boolean) value : null;
    }



    // More method will be added in the future...
}
