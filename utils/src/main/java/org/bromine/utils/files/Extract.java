package org.bromine.utils.files;

import lombok.extern.slf4j.Slf4j;
import org.bromine.utils.tar.TarHeader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * A utility class for extracting files from various compressed formats.
 * This class supports extraction from ZIP and TAR.GZ files.
 */
@Slf4j
public class Extract {


    public static void unzip(Path source, Path target) {
        String fileName = "";
        try (FileInputStream fis = new FileInputStream(source.toFile());
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                fileName = entry.getName();
                // Extract entry to target path
                Path newFile = target.resolve(Paths.get(entry.getName()).getFileName());

                if (entry.isDirectory()) {
                    // Create directory
                    Files.createDirectories(newFile);
                } else {
                    // Extract file
                    Files.copy(zis, newFile, StandardCopyOption.REPLACE_EXISTING);
                    log.debug("Extracted: {}", newFile);
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

    public static void file(Path source, Path target) {
        String fileName = source.getFileName().toString(); // Get the file name as a string
        if (fileName.endsWith(".zip")) {
            unzip(source, target);
            return;
        }
        if (fileName.endsWith(".gzip")
                || fileName.endsWith(".tar.gz")) {
            tarGz(source, target);
            return;
        }
        // will maybe need to handle more files in the future.
    }


    public static void tarGz(Path source, Path target) {
        try (InputStream fileInputStream = Files.newInputStream(source);
             GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(gzipInputStream)) {

            byte[] header = new byte[512];
            byte[] buffer = new byte[8192]; // Reusable buffer for file data

            while (bufferedInputStream.read(header) == 512) {
                TarHeader tarHeader = new TarHeader(header);
                String name = tarHeader.getName();
                long size = tarHeader.getSize();

                if (name.isEmpty()) break; // End of archive

                Path outputPath = target.resolve(name);

                if (name.endsWith("/")) {
                    if (!Files.exists(outputPath)) {
                        Files.createDirectories(outputPath); // Create directory if it doesn't exist
                    }
                } else {
                    Files.createDirectories(outputPath.getParent()); // Ensure parent directories exist
                    try (OutputStream outputStream = Files.newOutputStream(outputPath)) {
                        long remaining = size;
                        while (remaining > 0) {
                            int read = bufferedInputStream.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                            if (read == -1) break; // End of stream
                            outputStream.write(buffer, 0, read);
                            remaining -= read;
                        }
                    }
                }

                // Skip padding to align to 512-byte blocks
                long skipBytes = (512 - (size % 512)) % 512;
                if (skipBytes > 0) {
                    bufferedInputStream.skipNBytes(skipBytes);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract tar.gz file: " + source, e);
        }
    }
}
