package elements;

import annotations.AffectedBy;
import capabilities.BrowserType;
import capabilities.ChromeCapabilities;
import capabilities.Configuration;
import drivermanagers.UpdateChromeDriverHelper;
import lombok.extern.slf4j.Slf4j;
import sleeper.Sleeper;

import javax.annotation.concurrent.ThreadSafe;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe
@AffectedBy(clazz = UpdateChromeDriverHelper.class)
@Slf4j
public class ChromeDriver extends WebDriver {

//    private static final Logger log = LoggerFactory.getLogger(ChromeDriver.class);
    protected static final ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<>();

    private static boolean initialized = false;
    private static final ReentrantLock lock = new ReentrantLock();


    private static void initialize() {
        if (!initialized) {
            lock.lock();
            try {
                if (!initialized) {  // Double-checked locking
                    log.info("Before check Chrome Version");
                    UpdateChromeDriverHelper.checkChromeVersionIsUpdated();
                    log.info("Before start chrome process");
                    runAndShutDownDriver(ChromeDriver::startChromeProcess);
                    initialized = true;
                }
            } finally {
                lock.unlock();
            }
        }
    }

    // Method to check if Chrome is the selected browser
    private static boolean isChromeSelected() {
        String browser = Configuration.getBrowserType().name();  // Assuming this method exists
        return BrowserType.CHROME.name().equalsIgnoreCase(browser);
    }

    /**
     * Will open a Chrome window with no capabilities set.
     */
    public ChromeDriver() {
        ChromeCapabilities defaultCapabilities = new ChromeCapabilities();
        new ChromeDriver(defaultCapabilities);
    }

    /**
     * Will open a Chrome window with the capabilities set.
     * @param chromeCapabilities to be used.
     */
    public ChromeDriver(ChromeCapabilities chromeCapabilities) {
        String chromeCapabilitiesAsString = chromeCapabilities.toString();
        if (!chromeCapabilitiesAsString.contains("chrome")) {
            throw new UnsupportedOperationException("ChromeDriver couldn't be created. Make sure your " +
                    "jsonConfig contains correct structure for Chrome.");
        }
        Configuration.setBrowserType(BrowserType.CHROME);
        initialize();
        DriverClient.startSession(chromeCapabilitiesAsString);
        map.put(Thread.currentThread().getId(), DriverClient.sessionId());
        set(this);
    }


    // Method to start the ChromeDriver process
    private static synchronized void startChromeProcess() {

        if (process == null || !process.isAlive()) {
            String resourcePath;

            String path = Configuration.getDriverPath() != null ? String.valueOf(Configuration.getDriverPath()) : "drivers";
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                resourcePath = path + "/chromedriver.exe";
            } else if (System.getProperty("os.name").toLowerCase().contains("nix") || System.getProperty("os.name").toLowerCase().contains("nux")) {
                resourcePath = path + "/chromedriver"; // On Linux or macOS, use the plain executable
            } else {
                throw new UnsupportedOperationException("Unsupported OS for ChromeDriver initialization");
            }

            // Get the resource URL
            String p = "";
            if (Configuration.getDriverPath() == null) {
                URL url = DriverClient.class.getClassLoader().getResource(resourcePath);
                if (url == null) {
                    throw new RuntimeException("Resource not found: " + resourcePath);
                }
                p = url.getPath();
            } else {
                if (!Files.exists(Paths.get(resourcePath))) {
                    throw new RuntimeException("FilePath " + path + " doesn't contain chromedriver");
                }
                p = resourcePath;
            }


            // Get the path from the URL
            String chromedriverPath = p;

            // Adjust path for Windows if necessary
            if (chromedriverPath.startsWith("/") && System.getProperty("os.name").toLowerCase().contains("win")) {
                chromedriverPath = chromedriverPath.substring(1); // Remove leading slash for Windows
            }

            String urlString = Configuration.getDriverUrl();
            String port = urlString.substring(urlString.lastIndexOf(':') + 1);
            ProcessBuilder processBuilder = new ProcessBuilder(chromedriverPath,
                    "--port=" + port,
                    "--host:0.0.0.0"
//                    "--verbose"
//                    "--log-level=ALL"
            );
            BufferedReader reader = null;
            try {
                process = processBuilder.start();
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // Flag to track if the process has started properly
                boolean processReady = false;

                // Loop to periodically check the process's output
                while (!processReady) {
                    // Check if there's any output from the process
                    if (reader.ready()) {
                        String line = reader.readLine();
                        if (line != null && line.contains("started")) { // Modify the condition to suit your needs
                            processReady = true;
                            System.out.println("Process is ready!");
                        }
                    }

                    // You can do other work here while waiting (if necessary)
                    // For example, check if the process is still running:
                    if (!process.isAlive()) {
                        System.out.println("Process has terminated unexpectedly.");
                        break;
                    }

                    // A small sleep to prevent a tight loop, allowing for CPU efficiency
                    Sleeper.sleep(Duration.ofMillis(500));
                }
//                Sleeper.sleepInSeconds(5);
            } catch (IOException e) {
                throw new RuntimeException("Failed to start ChromeDriver", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();  // Handle the exception if closing the reader fails
                    }
                }
            }
        }

        if (process.isAlive()) {
            log.info("Chrome process was alive");
        }


    }



    @Override
    public void quit() {
        super.quit();
        map.remove(Thread.currentThread().getId());
    }

}
