package testbase;

import capabilities.BrowserType;
import capabilities.ChromeCapabilities;
import capabilities.Configuration;
import capabilities.FireFoxCapabilities;
import elements.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import sleeper.Sleeper;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static Constants.Constants.TWO_SECONDS;

/**
 * Base class all Tests should extend.
 * It starts and closes a Session.
 */

@Execution(ExecutionMode.CONCURRENT)
public class TestBase {
protected static final String DRIVER_URL = "http://localhost:51325";

    public TestBase() {
        String json = chromeCapabilities();
//        String json = firefoxCapabilities();
        Configuration.setGridUrl(DRIVER_URL);
        Configuration.setBrowserType(BrowserType.CHROME);
        Configuration.setJsonConfig(json);
//        ScreenShot.config.setImageFormat("png");
        ScreenShot.config.setSavePath(Paths.get(System.getProperty("user.dir"), "target", "screenshots").toString());

    }

    @BeforeEach
    public void beforeEach() {
//        WebDriverContainer.set();
        new ChromeDriver();

//        new GeckoDriver();
        //        Sleeper.sleep(TWO_SECONDS);

        WebDriver.get().window().maximize();
        WebDriver.get().timeouts().set().implicitWait(Duration.ofSeconds(20));
        WebDriver.get().timeouts().set().pageLoad(Duration.ofSeconds(20));
        WebDriver.get().timeouts().set().scriptWait(Duration.ofSeconds(20));


//        WebDriverContainer.get().window().maximize();

//        DriverClient.startSession();
//        DriverClient.setImplicitWait(FIVE_SECONDS);
    }


    @AfterEach
    public void afterEach() throws IOException {
        WebDriver.get().quit();
//        DriverClient.closeSession();
    }

    @AfterAll
    public static void afterAll() {
//        WebDriverContainer.get().kill();
    }



    public String chromeCapabilities() {
        Map<String,Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);  // Disable notifications popup
        prefs.put("credentials_enable_service", false);  // Disable password manager popup
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("download.default_directory", "/path/to/download/folder");  // Set default download directory
        prefs.put("download.prompt_for_download", false);  // Auto download without confirmation
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);  // Enable Safe Browsing
        return new ChromeCapabilities().addArguments(
                "--start-maximized",  // Starts Chrome maximized
                "--disable-infobars",  // Disables the info bar
                "--disable-notifications",  // Disables browser notifications
                "--incognito",  // Opens Chrome in incognito mode
                "--disable-gpu",  // Disables GPU hardware acceleration (useful for headless mode)
                "--no-sandbox",  // Disables the sandbox (may help with certain CI environments)
                "--disable-dev-shm-usage"  // Addresses issues with /dev/shm size on certain systems
        )
                .addPrefs(prefs)
                .addExtra("excludeSwitches", new String[]{"enable-automation"})
                .build();
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
