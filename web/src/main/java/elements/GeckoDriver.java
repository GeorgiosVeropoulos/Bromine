package elements;

import capabilities.BrowserType;
import capabilities.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.bromine.utils.files.FileLoader;
import org.bromine.utils.platform.Platform;
import sleeper.Sleep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class GeckoDriver extends WebDriver {

    protected static final ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<>();

    // Static block to ensure process starts only if Gecko is the selected browser
    static {
        Configuration.setBrowserType(BrowserType.FIREFOX);
        if (isGeckoSelected()) {
            runAndShutDownDriver(GeckoDriver::startGeckoProcess);
        }
    }

    // Method to check if Gecko is the selected browser
    private static boolean isGeckoSelected() {
        String browser = Configuration.getBrowserType().name();  // Assuming this method exists
        return BrowserType.FIREFOX.name().equalsIgnoreCase(browser);
    }

    // Constructor: start session only if Gecko is selected
    public GeckoDriver() {
        if (!Configuration.getJsonConfig().contains("firefox")) {
            throw new UnsupportedOperationException("GeckoDriver cannot be initialized. Gecko is not the selected browser.\n" +
                    "Make sure you have selected FireFox in the Configuration Browser Type, your capabilities also match and geckodriver.exe exists in your resources package!");
        }
        Configuration.setBrowserType(BrowserType.FIREFOX);
        if (isGeckoSelected()) {
            // Start a new session for each GeckoDriver instance
            DriverClient.startSession(Configuration.getJsonConfig());
            map.put(Thread.currentThread().getId(), DriverClient.sessionId());
        }
        set(this);
    }

    // Method to start the ChromeDriver process
    private static synchronized void startGeckoProcess() {

        if (process == null || !process.isAlive()) {
            String resourcePath;

            String path = Configuration.getDriverPath() != null ? String.valueOf(Configuration.getDriverPath()) : "drivers";
            if (Platform.isWindows()) {
                resourcePath = "/geckodriver.exe";
            } else if (Platform.isLinux()) {
                resourcePath = "geckodriver"; // On Linux or macOS, use the plain executable
            } else {
                throw new UnsupportedOperationException("Unsupported OS for GeckoDriver initialization");
            }

            // Get the resource URL
            String p = "";
            if (Configuration.getDriverPath() == null) {
                URL url  = FileLoader.getURLunderTargetClasses(path, resourcePath);
                p = url.getPath();
            } else {
                if (!Files.exists(Paths.get(path, resourcePath))) {
                    throw new RuntimeException("FilePath " + path + " doesn't contain geckodriver");
                }
                p = resourcePath;
            }

            Path driverPath = Paths.get(p);
            if (!Files.isExecutable(driverPath)) {
                try {
                    Files.setPosixFilePermissions(driverPath, PosixFilePermissions.fromString("rwxr-xr-x"));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to set executable permissions for geckodriver: " + driverPath, e);
                }
            }

            // Get the path from the URL
            String chromedriverPath = p;

            // Adjust path for Windows if necessary
            if (chromedriverPath.startsWith("/") && Platform.isWindows()) {
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
                    Sleep.For(Duration.ofMillis(100));
                }
//                Sleeper.sleepInSeconds(5);
            } catch (IOException e) {
                throw new RuntimeException("Failed to start GeckoDriver" + e.getMessage(), e);
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
            log.info("Gecko process was alive");
        }

    }


    @Override
    public void quit() {
        super.quit();
        map.remove(Thread.currentThread().getId());
    }


    /**
     * Method to clean up the chromeDriver after all @Tests finish
     * Should be used in AfterAll or AfterSuite type of methods in either JUnit or TestNG.
     *
     */
    private synchronized void kill() {
        if (!map.isEmpty()) {
            System.out.println("KILL SKIPPED");
            return;
        }
        System.out.println("KILL ACTUALLY CALLED CODE");
        try {
            if (Platform.isWindows()) {
                // If the OS is Windows, use taskkill
                String killCommand = "taskkill /F /IM " + "chromedriver.exe";
                executeCommand(killCommand);
            } else {
                // If the OS is Unix-based (Linux or Mac), use pkill
                String killCommand = "pkill -f " + "chromedriver.exe";
                executeCommand(killCommand);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void executeCommand(String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        System.out.println("Command executed: " + command);
    }

}
