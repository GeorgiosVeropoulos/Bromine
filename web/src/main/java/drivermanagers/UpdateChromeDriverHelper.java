package drivermanagers;

import chrome.Chrome;
import chrome.Downloader;
import chrome.Version;
import chrome.enums.SupportedBinaries;
import chrome.enums.SupportedChannels;
import chrome.jsons.LastKnownGoodVersionsWithDownloads;
import org.bromine.utils.files.FileLoader;
import net.GetJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bromine.utils.platform.Platform;
import org.bromine.utils.files.Extract;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Scanner;
import java.util.Set;

// Update Chrome based on what platform we are using.
// this needs to be moved to its own module probably.
// clean up on code is needed.

// Check this https://github.com/GoogleChromeLabs/chrome-for-testing
public class UpdateChromeDriverHelper extends UpdateDriverHelper {

    private static final URL chromedriverResource;
    private static final Logger log = LoggerFactory.getLogger(UpdateChromeDriverHelper.class);

    static {
        if (Platform.isWindows()) {
            chromedriverResource = FileLoader.getURLunderTargetClasses("drivers", "chromedriver.exe");
        } else if (Platform.isLinux()) {
            chromedriverResource = FileLoader.getURLunderTargetClasses("drivers", "chromedriver");
        } else {
            throw new UnsupportedOperationException("Unsupported OS for ChromeDriver");
        }
    }

    public static void checkChromeVersionIsUpdated() {
        log.info("inside checkChromeVersionIsUpdated");
        LastKnownGoodVersionsWithDownloads lastKnownGoodVersionsWithDownloads = GetJson.getLastKnownGoodVersionsWithDownloadJson();
        Version version1 = lastKnownGoodVersionsWithDownloads.getChannels().getStable().getVersion();
        Version chromeDriver = getChromedriverVersion();
        Version installedChromeVersion = Chrome.getChromeDetails(); //        getInstalledChromeVersion();
        if (installedChromeVersion.toString().isEmpty() || (!installedChromeVersion.getMajor().equals(chromeDriver.getMajor()) && version1.getMajor().equals(installedChromeVersion.getMajor()))) {
            updateChromedriver();
            log.info("update chrome Driver");
        }
        log.info("Exit checkChromeVersionIsUpdated");
    }



    private static Version getChromedriverVersion() {
        Process process;
        try {
            if (chromedriverResource == null) {
                return new Version("0.0.0.0");
            }
            process = Runtime.getRuntime().exec(
                    new File(chromedriverResource.toURI()).getAbsolutePath() + " --version"
            );
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try (Scanner scanner = new Scanner(process.getInputStream())) {
            if (scanner.hasNextLine()) {
                // Example output: "ChromeDriver 139.0.7258.66 (commit-hash...)"
                String line = scanner.nextLine();
                String versionString = line.split(" ")[1]; // -> "139.0.7258.66"
                return new Version(versionString);
            }
        }

        throw new IllegalStateException("Unable to determine installed Chromedriver version.");
    }

    private static void updateChromedriver() {
        String driverFileName = "";

        // Determine the platform and file name based on OS
        if (Platform.isWindows()) {
            driverFileName = "chromedriver.exe";
        } else if (Platform.isMac()) {
            driverFileName = "chromedriver";
        } else if (Platform.isLinux()) {
            driverFileName = "chromedriver";
        } else {
            throw new UnsupportedOperationException("Unsupported OS for ChromeDriver update");
        }

        // Step 1: Download the zip file
//        Download.file(downloadUrl, zipFilePath);

        Path download = Downloader.builderFor().lastKnownGoodVersions()
                .withBinary(SupportedBinaries.CHROMEDRIVER)
                .withChannel(SupportedChannels.STABLE)
                .downloadTo(DRIVERS_PACKAGE)
                .execute();

        // Step 2: Unzip and replace the ChromeDriver binary
        Extract.unzip(download, download.getParent());

        // Step 3: Set the executable permission for the binary
        setExecutablePermission(DRIVERS_PACKAGE.resolve(driverFileName));

        // Step 4: Delete the zip file after unzipping
//        try {
//            Files.deleteIfExists(zipFilePath);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to delete zip file: " + zipFilePath, e);
//        }

        System.out.println("Chromedriver updated successfully.");
    }

    /**
     * Set executable permissions for the file based on the OS.
     */
    private static void setExecutablePermission(Path filePath) {
        try {
            if (Platform.isLinux() || Platform.isMac()) {
                // Linux or macOS: Set the executable permission
                Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
                Files.setPosixFilePermissions(filePath, permissions);
            } else {
                // Windows: No additional permissions needed
                System.out.println("No additional permissions set for Windows.");
            }
        } catch (UnsupportedOperationException e) {
            // If the OS doesn't support PosixFilePermissions (Windows or some other OS)
            log.info("Unsupported operation for setting file permissions.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to set executable permission for " + filePath, e);
        }
    }
}
