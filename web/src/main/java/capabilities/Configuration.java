package capabilities;


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

    @Getter @Setter
    private static String jsonConfig;
    @Getter @Setter
    private static String driverUrl;

    @Getter @Setter
    private static BrowserType browserType;

    @Getter @Setter
    private static Path driverPath;





}
