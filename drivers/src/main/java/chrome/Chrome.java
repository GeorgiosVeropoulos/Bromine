package chrome;

import org.bromine.utils.platform.Platform;

import java.io.IOException;
import java.util.Scanner;

public class Chrome {




    protected static Process getChromeProcess() {
        Process process;
        try {
            process = switch (Platform.getOperatingSystem()) {
                case WINDOWS -> Runtime.getRuntime().exec(new String[]{
                        "cmd.exe", "/c", "reg", "query", "HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon", "/v", "version"});
                case LINUX -> Runtime.getRuntime().exec("/opt/google/chrome/google-chrome --version");
                case MAC -> Runtime.getRuntime().exec("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome --version");
                case NOT_SUPPORTED -> throw new UnsupportedOperationException("NOT SUPPORTED OS!");
            };

        } catch (IOException platformException) {
            throw new RuntimeException("Chrome Prosses wasn't able to be found", platformException.getCause());
        }
        return process;
    }

    public static Version getChromeDetails() {
        Process process = getChromeProcess();
        String version = null;

        try (Scanner scanner = new Scanner(process.getInputStream())) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();
                if (Platform.isWindows() && line.startsWith("version")) {
                    // Windows: Extract the version from the line
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 3) {
                        version = parts[2];
                        break;
                    }
                } else if (!Platform.isWindows() && line.toLowerCase().contains("chrome")) {
                    // Linux/macOS
                    version = line.replaceAll("[^\\d.]", "").trim();
                    break;
                }
            }
        }

        if (version == null) {
            throw new IllegalStateException("Unable to determine installed Chrome version.");
        }

        // Split the version and print parts
        return new Version(version);

//        return version;
    }
}
