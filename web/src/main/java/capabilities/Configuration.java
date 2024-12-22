package capabilities;


import elements.ScreenShot;
import lombok.Getter;
import lombok.Setter;

/**
 * This class handles the entire configuration of the execution.
 * From browser capabilities to anything else.
 */
public class Configuration {

    @Getter @Setter
    private static String jsonConfig;
    @Getter @Setter
    private static String gridUrl;

    @Getter @Setter
    private static BrowserType browserType;





}
