package json;

import java.util.ArrayList;
import java.util.List;

public class JsonArrayBuilder {
    private final List<Object> jsonArray;

    public JsonArrayBuilder() {
        this.jsonArray = new ArrayList<>();
    }

    public JsonArrayBuilder addValue(Object value) {
        jsonArray.add(value);
        return this;
    }

    public JsonObjectBuilder addNestedObject() {
        JsonObjectBuilder nestedObject = new JsonObjectBuilder();
        jsonArray.add(nestedObject);
        return nestedObject;
    }

    public String build() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        boolean first = true;
        for (Object value : jsonArray) {
            if (!first) {
                jsonBuilder.append(", ");
            }
            jsonBuilder.append(valueToJson(value));
            first = false;
        }
        jsonBuilder.append("]");
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