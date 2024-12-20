package capabilities;


import elements.ScreenShot;

/**
 * This class handles the entire configuration of the execution.
 * From browser capabilities to anything else.
 */
public class Configuration {

    public static String jsonConfig;
    private static String gridUrl;

    private static BrowserType type;

    public static void setJsonConfig(String config) {
        jsonConfig = config;
    }

    public static String getJsonConfig() {
        return jsonConfig;
    }

    public static void setGridUrl(String url) {
        gridUrl = url;
    }

    public static String getGridUrl() {
        return gridUrl;
    }

    public static void setBrowserType(BrowserType browserType) {
        type = browserType;
    }

    public static BrowserType getBrowserType() {
        return type;
    }





}
