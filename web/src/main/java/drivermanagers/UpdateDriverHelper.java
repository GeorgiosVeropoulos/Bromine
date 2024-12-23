package drivermanagers;


import ch.qos.logback.classic.spi.PlatformInfo;
import elements.ChromeDriver;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Slf4j
public class UpdateDriverHelper {

    protected static final Path DRIVERS_PACKAGE;
    protected static final Platform platform;


    protected enum Platform {
        WINDOWS,
        LINUX,
        MAC
    }

    //if the drivers package doesn't exist we create it so we can put all downloaded browsers there.
    static {
        String currentOS = System.getProperty("os.name").toLowerCase();
        if (currentOS.contains("win")) {
            platform = Platform.WINDOWS;
        } else if (currentOS.contains("nix") || currentOS.contains("nux")) {
            platform = Platform.LINUX;
        } else {
            throw new UnsupportedOperationException("Unsupported OS");
        }

        try {
            URL driversUrl = UpdateDriverHelper.class.getClassLoader().getResource("drivers/");
            Path driversPath;

            if (driversUrl == null) {
                // Fallback: Create the directory in your desired location
                driversPath = Paths.get("target/classes/drivers");
                Files.createDirectories(driversPath);
                log.info("Created fallback drivers directory: {}", driversPath.toAbsolutePath());
            } else {
                // If the URL is valid, ensure the directory exists
                driversPath = Paths.get(driversUrl.toURI());
            }

            DRIVERS_PACKAGE = driversPath;
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve drivers directory path", e);
        }
    }





}
