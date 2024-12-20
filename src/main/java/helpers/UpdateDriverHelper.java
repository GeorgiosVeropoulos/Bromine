package helpers;

import elements.WebDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
            } else {
                // If the URL is valid, ensure the directory exists
                driversPath = Paths.get(driversUrl.toURI());
            }
            if (!Files.exists(driversPath)) {
                Files.createDirectories(driversPath);
                System.out.println("Created drivers directory: " + driversPath.toAbsolutePath());
            }

            DRIVERS_PACKAGE = Paths.get(driversUrl.toURI());
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve drivers directory path", e);
        }
    }

    protected static void downloadFile(String urlString, Path targetPath) {
        System.out.println("Downloading chromedriver from: " + urlString);
        try (InputStream in = new URL(urlString).openStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Download completed: " + targetPath);
    }

    protected static void unzip(Path source, Path target, String fullNameofDriverExe) {
        try (FileInputStream fis = new FileInputStream(source.toFile());
             ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                // Only process chromedriver.exe (ignoring folder paths)
                if (entry.getName().contains(fullNameofDriverExe)) {
                    // Extract only the file name from the entry
                    Path newFile = target.resolve(Paths.get(entry.getName()).getFileName().toString());

                    // Check if the file exists
                    if (Files.exists(newFile)) {
                        System.out.println(fullNameofDriverExe + " already exists and will be replaced: " + newFile);
                        Files.copy(zis, newFile, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        System.out.println("Extracting " + fullNameofDriverExe + ": " + newFile);
                        Files.copy(zis, newFile);
                    }

                    // Close the current zip entry
                    zis.closeEntry();

                    // Break once chromedriver.exe is processed
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Zip file not found: " + source, e);
        } catch (IOException e) {
            throw new RuntimeException("Error while extracting " + fullNameofDriverExe, e);
        }

    }
}
