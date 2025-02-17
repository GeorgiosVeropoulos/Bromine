package testbase;

import capabilities.ChromeCapabilities;
import capabilities.Configuration;
import elements.ChromeDriver;
import elements.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class TestBaseTestNG {
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

    public TestBaseTestNG() {
        System.setProperty("webdriver.chrome.logfile", "chromedriver.log");
        System.setProperty("webdriver.chrome.verboseLogging", "true");
        Configuration.setDriverUrl(DRIVER_URL);
        Configuration.network()
                .setReadTimeout(Duration.ofSeconds(45))
                .setConnectionTimeout(Duration.ofSeconds(45));
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeEachTestRun() {
        new ChromeDriver(chromeCapabilities());
        WebDriver.get().window().maximize();
        WebDriver.get().timeouts().set().implicitWait(Duration.ofSeconds(20));
        WebDriver.get().timeouts().set().pageLoad(Duration.ofSeconds(20));
        WebDriver.get().timeouts().set().scriptWait(Duration.ofSeconds(20));
    }

    @AfterMethod(alwaysRun = true)
    public void afterEachTestRun() {
        if (WebDriver.get() != null) {
            WebDriver.get().quit();
        }
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
}
