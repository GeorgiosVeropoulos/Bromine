package zip;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class ZipHelper {


    public static void unzip(Path source, Path target, String fileName) {
        try (FileInputStream fis = new FileInputStream(source.toFile());
             ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                // Extract entry to target path
                Path newFile = target.resolve(Paths.get(entry.getName()).getFileName());

                if (entry.isDirectory()) {
                    // Create directory
                    Files.createDirectories(newFile);
                } else {
                    // Extract file
                    Files.copy(zis, newFile, StandardCopyOption.REPLACE_EXISTING);
                    log.info("Extracted: {}", newFile);
                }

                // Close the current zip entry
                zis.closeEntry();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Zip file not found: " + source, e);
        } catch (IOException e) {
            throw new RuntimeException("Error while extracting " + fileName, e);
        }
    }
}
