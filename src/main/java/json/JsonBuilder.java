package json;

import java.util.HashMap;
import java.util.Map;

public class JsonBuilder {
    private final Map<String, Object> jsonMap ;

    public JsonBuilder() {
        this.jsonMap = new HashMap<>();
    }

    public JsonBuilder addKeyValue(String key, Object value) {
        jsonMap.put(key, value);
        return this;
    }

    public JsonObjectBuilder addNestedObject(String key) {
        JsonObjectBuilder nestedObject = new JsonObjectBuilder();
        jsonMap.put(key, nestedObject);
        return nestedObject;
    }

    public JsonArrayBuilder addArray(String key) {
        JsonArrayBuilder arrayBuilder = new JsonArrayBuilder();
        jsonMap.put(key, arrayBuilder);
        return arrayBuilder;
    }

    //Creates the final String JSON that we want to send.
    public String build() {
        return buildJson(jsonMap);
    }

    private String buildJson(Map<String, Object> map) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                jsonBuilder.append(", ");
            }
            jsonBuilder.append("\"").append(entry.getKey()).append("\": ");
            if (entry.getValue() instanceof Number) {
                jsonBuilder.append(entry.getValue());
            } else {
                jsonBuilder.append(valueToJson(entry.getValue()));
            }

            first = false;
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    private String valueToJson(Object value) {
        if (value instanceof String) {
            return "\"" + value + "\"";
        } else if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof JsonObjectBuilder) {
            return ((JsonObjectBuilder) value).build(); // Call build() on JsonObjectBuilder
        } else if (value instanceof JsonArrayBuilder) {
            return ((JsonArrayBuilder) value).build(); // Call build() on JsonArrayBuilder
        }
        return "null"; // for null values
    }
}
