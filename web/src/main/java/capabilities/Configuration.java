package capabilities;


import elements.HttpMethodExecutor;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

/**
 * This class handles the entire configuration of the execution.
 * From browser capabilities to anything else.
 * The idea is to give a centralized class to control everything.
 * It is recommended to set values to this class inside constructors or very early during the execution flow.
 */
public class Configuration {

    @Getter @Setter @Deprecated(forRemoval = true, since = "Capabilities will be added for each BrowserType that can be passed on the constructor")
    private static String jsonConfig;
    @Getter @Setter
    private static String driverUrl;

    @Getter @Setter @Deprecated(forRemoval = true, since = "Browsers should set this")
    private static BrowserType browserType;

    @Getter @Setter
    private static Path driverPath;


    public static NetworkSettings network() {
        return new NetworkSettings();
    }





}
