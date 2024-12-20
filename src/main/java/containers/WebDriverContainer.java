package containers;

import capabilities.Configuration;
import elements.ChromeDriver;
import elements.GeckoDriver;
import elements.WebDriver;

/**
 * WebDriverContainer handles the initiation of the specified WebDriver
 * based on the {@link capabilities.BrowserType} set in {@link Configuration}
 * <p>Usage of this class isn't mandatory but it does enable threadSafe usage of WebDrivers</p>
 *
 * @see capabilities.BrowserType
 * @see Configuration
 */
@Deprecated(forRemoval = true, since = "WebDriver.get() should be used now")
public class WebDriverContainer {


    private static final ThreadLocal<WebDriver> webDrivers = new ThreadLocal<>();


    /**
     * Intantiate a WebDriver based on BrowserType
     */
    public static void set() {
        System.out.println(Configuration.getBrowserType());
        switch (Configuration.getBrowserType()) {
            case FIREFOX -> webDrivers.set(new GeckoDriver());
            case CHROME -> webDrivers.set(new ChromeDriver());
            //add more later...
        }
    }

    public static WebDriver get() {
        return  webDrivers.get();
    }
}
