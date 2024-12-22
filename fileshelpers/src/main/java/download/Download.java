package download;

import exceptions.FailedToDownloadDriverException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class Download {

    private static final Logger log = Logger.getLogger(Download.class.getName());


    public static void file(String urlString, Path targetPath) {
        log.fine("Downloading chromedriver from: " + urlString);
        try (InputStream in = new URL(urlString).openStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FailedToDownloadDriverException(e);
        }
        log.fine("Download completed: " + targetPath);
    }
}
