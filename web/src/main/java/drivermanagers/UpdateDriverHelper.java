package drivermanagers;


import ch.qos.logback.classic.spi.PlatformInfo;
import elements.ChromeDriver;
import files.FileLoader;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class UpdateDriverHelper {

    protected static final Path DRIVERS_PACKAGE;

    static {
        try {
            URL driversUrl = FileLoader.getURLFromPath("drivers/");
            Path driversPath;

            if (driversUrl == null) {
                // Fallback: Create the directory in your desired location
                driversPath = Paths.get("target","classes", "drivers");
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
