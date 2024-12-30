package elements;

import capabilities.BrowserType;
import capabilities.Configuration;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class GeckoDriver extends WebDriver {

    protected static final ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<>();

    // Static block to ensure process starts only if Gecko is the selected browser
    static {
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
        if (isGeckoSelected()) {
            // Start a new session for each GeckoDriver instance
//            DriverClient.startSession();
            map.put(Thread.currentThread().getId(), DriverClient.sessionId());
        }
        set(this);
    }

    // Method to start the ChromeDriver process
    private static synchronized void startGeckoProcess() {
        if (process == null || !process.isAlive()) {
            String chromedriverPath = DriverClient.class.getClassLoader().getResource("drivers/geckodriver.exe").getPath();
            if (chromedriverPath.startsWith("/")) {
                chromedriverPath = chromedriverPath.substring(1); // Remove leading slash for Windows
            }
            String url = Configuration.getDriverUrl();
            String port = url.substring(url.lastIndexOf(':') + 1);
            ProcessBuilder processBuilder = new ProcessBuilder(chromedriverPath, "--port=" + port);

            try {
                process = processBuilder.start();
            } catch (IOException e) {
                throw new RuntimeException("Failed to start GeckoDriver", e);
            }
        } else {
            System.out.println("Gecko Driver process is already running.");
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
            if (os.contains("win")) {
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
