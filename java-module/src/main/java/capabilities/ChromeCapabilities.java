package capabilities;

import json.JsonArrayBuilder;
import json.JsonBuilder;
import json.JsonObjectBuilder;

import java.util.Map;

public class ChromeCapabilities extends Capabilities {

    private JsonBuilder jsonBuilder = new JsonBuilder();
    private JsonArrayBuilder firstMatchArray;
    private JsonObjectBuilder objectBuilder;

    private JsonObjectBuilder chromeOptionsObject;

    private JsonArrayBuilder argsArray;
    private JsonObjectBuilder prefsMap;

    public ChromeCapabilities() {
        JsonObjectBuilder capabilities = jsonBuilder.addNestedObject("capabilities");
        firstMatchArray = capabilities.addArray("firstMatch");
        objectBuilder = firstMatchArray.addNestedObject().addKeyValue("browserName", "chrome");
        chromeOptionsObject = objectBuilder.addNestedObject("goog:chromeOptions");
    }


    public ChromeCapabilities addArguments(String... arguments) {

//        JsonObjectBuilder objectBuilder = firstMatchArray.addNestedObject().addKeyValue("browserName", "chrome");
        argsArray = chromeOptionsObject.addArray("args");
        for (String arg : arguments) {
            argsArray.addValue(arg);
        }
        argsArray.build();
        return this;
    }

    public ChromeCapabilities addPrefs(Map<String, Object> prefs) {
        prefsMap = chromeOptionsObject.addNestedObject("prefs");
        for (Map.Entry<String, Object> entry : prefs.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            prefsMap.addKeyValue(key, value);
        }
        return this;
    }


    /**
     * Can be used to set extra or experimental features.
     * @link <a href="https://gist.github.com/dodying/34ea4760a699b47825a766051f47d43b">Chromium command line switches</a>
     */
    public ChromeCapabilities addExtra(String key, Object value) {
        if (value instanceof String[]) {
            JsonArrayBuilder array = new JsonArrayBuilder();
            for (String v : (String[]) value) {
                array.addValue(v);
            }
            chromeOptionsObject.addKeyValue(key, array);
        } else {
            chromeOptionsObject.addKeyValue(key, value);
        }

        return this;
    }

    public String build() {
        return jsonBuilder.build();
    }
}
