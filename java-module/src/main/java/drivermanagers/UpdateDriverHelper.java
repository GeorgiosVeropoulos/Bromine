package drivermanagers;


import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class UpdateDriverHelper {

    protected static final Logger log = Logger.getLogger(UpdateDriverHelper.class.getName());
    protected static final Path DRIVERS_PACKAGE;

    //if the drivers package doesn't exist we create it so we can put all downloaded browsers there.
    static {
        try {
            URL driversUrl = UpdateDriverHelper.class.getClassLoader().getResource("drivers/");
            Path driversPath;
            if (driversUrl == null) {
                // Fallback: Create the directory in your desired location
                driversPath = Paths.get("target/classes/drivers");
            } else {
                // If the URL is valid, ensure the directory exists
                driversPath = Paths.get(driversUrl.toURI());
            }
            if (!Files.exists(driversPath)) {
                Files.createDirectories(driversPath);
                log.info("Created drivers directory: " + driversPath.toAbsolutePath());
            }

            DRIVERS_PACKAGE = Paths.get(driversUrl.toURI());
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve drivers directory path", e);
        }
    }




}
