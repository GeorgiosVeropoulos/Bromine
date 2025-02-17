package testbase;

import capabilities.*;
import elements.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class all Tests should extend.
 * It starts and closes a Session.
 */

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public class TestBase {
    protected static String DRIVER_URL;

    static {
        String envUrl = System.getenv("DRIVER_URL");

        // Check if envUrl is set, otherwise default to localhost with port
        if (envUrl != null) {
            // Append :51325 if not already included
            DRIVER_URL = envUrl.contains(":") ? envUrl : envUrl + ":9615";
        } else {
            // Default to localhost with port
            DRIVER_URL = "http://localhost:9615";
        }
    }

    public TestBase() {
        System.setProperty("webdriver.chrome.logfile", "chromedriver.log");
        System.setProperty("webdriver.chrome.verboseLogging", "true");
        Configuration.setDriverUrl(DRIVER_URL);
        Configuration.network()
                .setReadTimeout(Duration.ofSeconds(45))
                .setConnectionTimeout(Duration.ofSeconds(45));
    }

    @BeforeEach
    public void beforeEach() {
//        Configuration.setDriverUrl(DRIVER_URL);
//        log.info("DRIVER URL WAS {}", DRIVER_URL);
//        new ChromeDriver();
//        new ChromeDriver(new ChromeCapabilities().build());
        new ChromeDriver(chromeCapabilities());

//        new GeckoDriver();
        //        Sleeper.sleep(TWO_SECONDS);

        WebDriver.get().window().maximize();
        WebDriver.get().timeouts().set().implicitWait(Duration.ofSeconds(20));
        WebDriver.get().timeouts().set().pageLoad(Duration.ofSeconds(20));
        WebDriver.get().timeouts().set().scriptWait(Duration.ofSeconds(20));


    }


    @AfterEach
    public void afterEach() {
        if (WebDriver.get() != null) {
            WebDriver.get().quit();
        }
//        DriverClient.closeSession();
    }

    @AfterAll
    public static void afterAll() {
//        WebDriverContainer.get().kill();
    }



    public ChromeCapabilities chromeCapabilities() {
        Map<String,Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);  // Disable notifications popup
        prefs.put("credentials_enable_service", false);  // Disable password manager popup
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("download.default_directory", "/path/to/download/folder");  // Set default download directory
        prefs.put("download.prompt_for_download", false);  // Auto download without confirmation
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);  // Enable Safe Browsing
//        prefs.put("platformName", "LINUX");
        return new ChromeCapabilities().addArguments(
                "--start-maximized",  // Starts Chrome maximized
                "--disable-infobars",  // Disables the info bar
                "--disable-notifications",  // Disables browser notifications
//                "--headless",
                "--incognito",  // Opens Chrome in incognito mode
                "--disable-gpu",  // Disables GPU hardware acceleration (useful for headless mode)
                "--no-sandbox",  // Disables the sandbox (may help with certain CI environments)
                "--disable-dev-shm-usage"  // Addresses issues with /dev/shm size on certain systems
        )
                .addPrefs(prefs)
                .addExtra("excludeSwitches", new String[]{"enable-automation"}).build();
    }

    public String firefoxCapabilities() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("dom.webnotifications.enabled", false);  // Disable notifications popup
        prefs.put("browser.download.dir", "/path/to/download/folder");  // Set default download directory
        prefs.put("browser.download.folderList", 2);  // Use custom download location (2 = custom, 1 = desktop)
        prefs.put("browser.download.useDownloadDir", true);
        prefs.put("browser.helperApps.neverAsk.saveToDisk", "application/pdf");  // Automatically download PDFs without confirmation
        prefs.put("pdfjs.disabled", true);  // Disable the built-in PDF viewer

        return new FireFoxCapabilities()
                .addArguments(
                        "-private",  // Open Firefox in private browsing mode
                        "--width=1080",  // Set window width
                        "--height=480"  // Set window height
                )
                .addPrefs(prefs)
                .addExtra("excludeSwitches", new String[]{"enable-automation"})  // If there are any additional capabilities to set
                .build();
    }
}
