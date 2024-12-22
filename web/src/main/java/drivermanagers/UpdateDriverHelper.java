package drivermanagers;


import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Slf4j
public class UpdateDriverHelper {

    protected static final Path DRIVERS_PACKAGE;

    //if the drivers package doesn't exist we create it so we can put all downloaded browsers there.
    static {
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
