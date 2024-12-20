package elements;

import annotations.AffectedBy;
import capabilities.BrowserType;
import capabilities.Configuration;
import helpers.UpdateChromeDriverHelper;

import javax.annotation.concurrent.ThreadSafe;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
@AffectedBy(clazz = UpdateChromeDriverHelper.class)
public class ChromeDriver extends WebDriver {

    protected static final ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<>();


    // Static block to ensure process starts only if Chrome is the selected browser
    // Also checks if the current chromedriver is using the most updated version.
    static {
        if (isChromeSelected()) {
            UpdateChromeDriverHelper.checkChromeVersionIsUpdated();
            runAndShutDownDriver(ChromeDriver::startChromeProcess);
        }
    }

    // Method to check if Chrome is the selected browser
    private static boolean isChromeSelected() {
        String browser = Configuration.getBrowserType().name();  // Assuming this method exists
        return BrowserType.CHROME.name().equalsIgnoreCase(browser);
    }

    // Constructor: start session only if Chrome is selected
    public ChromeDriver() {
        if (!Configuration.getJsonConfig().contains("chrome")) {
            throw new UnsupportedOperationException("ChromeDriver cannot be initialized. Chrome is not the selected browser.\n" +
                    "Make sure you have selected Chrome in the Configuration Browser Type, your capabilities also match and chromedriver.exe exists in your resources package!");
        }
        if (isChromeSelected()) {
            // Start a new session for each ChromeDriver instance
            DriverClient.startSession();
            map.put(Thread.currentThread().getId(), DriverClient.sessionId());
        }
        set(this);
    }


    // Method to start the ChromeDriver process
    private static synchronized void startChromeProcess() {

        if (process == null || !process.isAlive()) {
            String chromedriverPath = DriverClient.class.getClassLoader().getResource("drivers/chromedriver.exe").getPath();
            if (chromedriverPath.startsWith("/")) {
                chromedriverPath = chromedriverPath.substring(1); // Remove leading slash for Windows
            }
            String url = Configuration.getGridUrl();
            String port = url.substring(url.lastIndexOf(':') + 1);
            ProcessBuilder processBuilder = new ProcessBuilder(chromedriverPath, "--port=" + port);

            try {
                process = processBuilder.start();
            } catch (IOException e) {
                throw new RuntimeException("Failed to start ChromeDriver", e);
            }
        }
        if (process.isAlive()) {
            System.out.println("Chrome process was alive");
        }
    }



    @Override
    public void quit() {
        super.quit();
        map.remove(Thread.currentThread().getId());
    }

}
