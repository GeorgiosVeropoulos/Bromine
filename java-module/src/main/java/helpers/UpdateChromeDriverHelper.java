package helpers;

import elements.ChromeDriver;
import json.JsonParser;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

// Check this https://github.com/GoogleChromeLabs/chrome-for-testing
public class UpdateChromeDriverHelper extends UpdateDriverHelper{

    private static final URL chromedriverResource;
    static {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            chromedriverResource = ChromeDriver.class.getClassLoader().getResource("drivers/chromedriver.exe");
        } else if (System.getProperty("os.name").toLowerCase().contains("nix") || System.getProperty("os.name").toLowerCase().contains("nux")) {
            chromedriverResource = ChromeDriver.class.getClassLoader().getResource("drivers/chromedriver");
        } else {
            throw new UnsupportedOperationException("Unsupported OS for ChromeDriver");
        }
    }

    private static final String CHROMEDRIVER_URL = "https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/";
    private static final String LAST_KNOWN_GOOD_VERSIONS_URL = "https://googlechromelabs.github.io/chrome-for-testing/last-known-good-versions.json";

    public static void checkChromeVersionIsUpdated() {
        String version = fetchLastKnownGoodVersion(LAST_KNOWN_GOOD_VERSIONS_URL, "Stable");
        String chromeDriver = getChromedriverVersion();
        String installedChromeVersion = getInstalledChromeVersion();
        boolean b = !installedChromeVersion.substring(0,3).equals(chromeDriver);
        if (!installedChromeVersion.substring(0,3).equals(chromeDriver) && version.contains(installedChromeVersion)) {
            updateChromedriver(version);
        }
    }



    private static String fetchLastKnownGoodVersion(String urlString, String build) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (response.toString().isEmpty() || response.toString().isBlank()) {
            throw new RuntimeException("No Version FOUND");
        }
        Map<String, Object> parsedJson = JsonParser.parse(response.toString());
        Map<String, Object> channels = (Map<String, Object>) parsedJson.get("channels");
        Map<String, Object> buildJson = (Map<String, Object>) channels.get(build);
        return buildJson.get("version").toString();
    }

    private static String getChromedriverVersion() {
        // Command to get Chromedriver version
        Process process;
        try {
            if (chromedriverResource == null) {
                return "0";
            }
            process = Runtime.getRuntime().exec(new File(chromedriverResource.toURI()).getAbsolutePath() + " --version");
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(process.getInputStream());
        if (scanner.hasNextLine()) {
            return scanner.nextLine().split(" ")[1].split("\\.")[0]; // Get major version
        }
        throw new IllegalStateException("Unable to determine installed Chromedriver version.");
    }


    private static String getInstalledChromeVersion() {
        String os = System.getProperty("os.name").toLowerCase();
        Process process = null;
        String version = null;

        try {
            if (os.contains("win")) {
                process = Runtime.getRuntime().exec("reg query \"HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon\" /v version");
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                String command = os.contains("mac") ?
                        "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome --version" :
                        "/opt/google/chrome/google-chrome --version";
                process = Runtime.getRuntime().exec(command);
            } else {
                throw new UnsupportedOperationException("Unsupported OS");
            }

            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (os.contains("win") && line.contains("version")) {
                    // Windows: Get the major version
                    version = line.split("\\s+")[line.split("\\s+").length - 1].split("\\.")[0];
                } else if ((os.contains("nix") || os.contains("nux") || os.contains("mac")) && line.toLowerCase().contains("chrome")) {
                    // Linux/macOS: Extract the first three numbers
                    version = line.replaceAll("[^\\d.]", "").trim(); // Remove non-numeric characters
                    String[] parts = version.split("\\.");
                    if (parts.length >= 3) {
                        version = parts[0] + "." + parts[1] + "." + parts[2];
                    }
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }

        if (version == null) {
            throw new IllegalStateException("Unable to determine installed Chrome version.");
        }

        return version;
    }


    private static void updateChromedriver(String chromeVersion) {
        String os = System.getProperty("os.name").toLowerCase();
        String platform = "";
        String driverFileName = "";

        // Determine the platform and file name based on OS
        if (os.contains("win")) {
            platform = "win64";
            driverFileName = "chromedriver.exe";
        } else if (os.contains("mac")) {
            platform = "mac64";
            driverFileName = "chromedriver";
        } else if (os.contains("nix") || os.contains("nux")) {
            platform = "linux64";
            driverFileName = "chromedriver";
        } else {
            throw new UnsupportedOperationException("Unsupported OS for ChromeDriver update");
        }

        // Form the URL for downloading the specific ChromeDriver version
        String downloadUrl = CHROMEDRIVER_URL + chromeVersion + "/" + platform + "/chromedriver-" + platform + ".zip";
        Path zipFilePath = DRIVERS_PACKAGE.resolve("chromedriver.zip");

        // Step 1: Download the zip file
        downloadFile(downloadUrl, zipFilePath);

        // Step 2: Unzip and replace the ChromeDriver binary
        unzip(zipFilePath, DRIVERS_PACKAGE, driverFileName);

        // Step 3: Set the executable permission for the binary
        setExecutablePermission(DRIVERS_PACKAGE.resolve(driverFileName));

        // Step 4: Delete the zip file after unzipping
        try {
            Files.deleteIfExists(zipFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete zip file: " + zipFilePath, e);
        }

        System.out.println("Chromedriver updated successfully.");
    }

    /**
     * Set executable permissions for the file based on the OS.
     */
    private static void setExecutablePermission(Path filePath) {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("nix") || System.getProperty("os.name").toLowerCase().contains("nux")) {
                // Linux or macOS: Set the executable permission
                Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
                Files.setPosixFilePermissions(filePath, permissions);
            } else {
                // Windows: No additional permissions needed
                System.out.println("No additional permissions set for Windows.");
            }
        } catch (UnsupportedOperationException e) {
            // If the OS doesn't support PosixFilePermissions (Windows or some other OS)
            System.out.println("Unsupported operation for setting permissions.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to set executable permission for " + filePath, e);
        }
    }




}
