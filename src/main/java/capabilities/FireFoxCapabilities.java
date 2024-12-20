package capabilities;

import json.JsonArrayBuilder;
import json.JsonBuilder;
import json.JsonObjectBuilder;

import java.util.Map;

public class FireFoxCapabilities extends Capabilities {

    private JsonBuilder jsonBuilder = new JsonBuilder();
    private JsonArrayBuilder firstMatchArray;
    private JsonObjectBuilder objectBuilder;

    private JsonObjectBuilder firefoxOptionsObject;

    private JsonArrayBuilder argsArray;
    private JsonObjectBuilder prefsMap;

    public FireFoxCapabilities() {
        JsonObjectBuilder capabilities = jsonBuilder.addNestedObject("capabilities");
        firstMatchArray = capabilities.addArray("firstMatch");
        objectBuilder = firstMatchArray.addNestedObject().addKeyValue("browserName", "firefox");
        firefoxOptionsObject = objectBuilder.addNestedObject("moz:firefoxOptions");
    }

    // Add Firefox command-line arguments (similar to Chrome)
    public FireFoxCapabilities addArguments(String... arguments) {
        argsArray = firefoxOptionsObject.addArray("args");
        for (String arg : arguments) {
            argsArray.addValue(arg);
        }
        argsArray.build();
        return this;
    }

    // Add Firefox preferences
    public FireFoxCapabilities addPrefs(Map<String, Object> prefs) {
        prefsMap = objectBuilder.addNestedObject("moz:firefoxOptions").addNestedObject("prefs");
        for (Map.Entry<String, Object> entry : prefs.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            prefsMap.addKeyValue(key, value);
        }
        return this;
    }

    // Add extra capabilities (if needed, similar to Chrome's extra capabilities)
    public FireFoxCapabilities addExtra(String key, Object value) {
        if (value instanceof String[]) {
            JsonArrayBuilder array = new JsonArrayBuilder();
            for (String v : (String[]) value) {
                array.addValue(v);
            }
            firefoxOptionsObject.addKeyValue(key, array);
        } else {
            firefoxOptionsObject.addKeyValue(key, value);
        }
        return this;
    }

    // Build the final JSON representation of the capabilities
    public String build() {
        return jsonBuilder.build();
    }
}

