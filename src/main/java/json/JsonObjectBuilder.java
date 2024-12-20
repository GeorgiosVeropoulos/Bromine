package json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JsonObjectBuilder {
    private final Map<String, Object> jsonMap;

    public JsonObjectBuilder() {
        this.jsonMap = new HashMap<>();
    }

    public JsonObjectBuilder addKeyValue(String key, Object value) {
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

    public String build() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            if (!first) {
                jsonBuilder.append(", ");
            }
            jsonBuilder.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof JsonObjectBuilder) {
                jsonBuilder.append(((JsonObjectBuilder) entry.getValue()).build());
            } else if (entry.getValue() instanceof JsonArrayBuilder) {
                jsonBuilder.append(((JsonArrayBuilder) entry.getValue()).build());
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
        }
        return "null"; // for null values
    }
}

