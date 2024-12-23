package download;

import exceptions.FailedToDownloadDriverException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
public class Download {



    public static void file(String urlString, Path targetPath) {
        log.debug("Downloading chromedriver from: {}", urlString);
        try (InputStream in = new URL(urlString).openStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FailedToDownloadDriverException(e);
        }
        log.debug("Download completed: {}", targetPath);
    }
}
