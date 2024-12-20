package helpers;

import elements.ChromeDriver;
import json.JsonParser;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Scanner;

// Check this https://github.com/GoogleChromeLabs/chrome-for-testing
public class UpdateChromeDriverHelper extends UpdateDriverHelper{

    private static final URL chromdriverExe = ChromeDriver.class.getClassLoader().getResource("drivers/chromedriver.exe");
    private static final String CHROMEDRIVER_URL = "https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/";
    private static final String LAST_KNOWN_GOOD_VERSIONS_URL = "https://googlechromelabs.github.io/chrome-for-testing/last-known-good-versions.json";
    public static void checkChromeVersionIsUpdated() {
        String version = fetchLastKnownGoodVersion(LAST_KNOWN_GOOD_VERSIONS_URL, "Stable");
        String chromeDriver = getChromedriverVersion();
        String installedChromeVersion = getInstalledChromeVersion();
        if (!chromeDriver.equals(installedChromeVersion) && version.contains(installedChromeVersion)) {
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
            if (chromdriverExe == null) {
                return "0";
            }
            process = Runtime.getRuntime().exec(new File(chromdriverExe.toURI()).getAbsolutePath() + " --version");
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
                        "google-chrome --version";
                process = Runtime.getRuntime().exec(command);
            } else {
                throw new UnsupportedOperationException("Unsupported OS");
            }

            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.contains("version")) {
                    version = line.split("\\s+")[line.split("\\s+").length - 1].split("\\.")[0]; // Get major version
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
        // Form the URL for downloading the specific ChromeDriver version
        String downloadUrl = CHROMEDRIVER_URL + chromeVersion + "/win64/chromedriver-win64.zip";
        // Download Chromedriver zip file
        Path zipFilePath = DRIVERS_PACKAGE.resolve("chromedriver.zip");

        // Step 1: Download the zip file
        downloadFile(downloadUrl, zipFilePath);
        // Step 2: Wait until the download completes (already ensured by blocking call above)
        // Step 3: Unzip and replace chromedriver.exe
        unzip(zipFilePath, DRIVERS_PACKAGE, "chromedriver.exe");
        // Step 4: Delete the zip file
        try {
            Files.deleteIfExists(zipFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Chromedriver updated successfully.");
    }



}
