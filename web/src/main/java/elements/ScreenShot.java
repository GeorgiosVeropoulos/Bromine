package elements;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class ScreenShot {

    private static Path savePath = Paths.get(System.getProperty("user.dir"), "target", "screenshots");
    private static String format = "png";

    public static class config {

        public static void setSavePath(@NonNull Path path) {
            Objects.requireNonNull(path, "save path can't be null");
            savePath = path;
        }

        public static void setImageFormat(@NonNull String imageFormat) {
            Objects.requireNonNull(imageFormat, "Image format can't be null");
            format = imageFormat;
        }
    }

    @CheckReturnValue
    @CheckForNull
    public static Path takeScreenShot() {
        return getScreenShot(EndPoints.buildEndpoint(EndPoints.TAKE_SCREENSHOT));
    }

    /**
     * Take a screenshot of the specified WebElement.
     * @param webElement we want to take a screenshot.
     * @return the Path the screenshot was saved or null if we can't determine the WebElement exists.
     */
    @CheckReturnValue
    @CheckForNull
    public static Path takeScreenShot(WebElement webElement) {
        SearchContext context = webElement.getSearchContext();
        if (context == null) {
            //element couldn't be found this method will return null
            return null;
        }
        return getScreenShot(EndPoints.buildEndpoint(EndPoints.TAKE_ELEMENT_SCREENSHOT, context.elementId()));
    }

    private static Path getScreenShot(String endpoint) {
        Response response = HttpMethodExecutor.doGetRequest(endpoint);
        Path saveLocation = null;
        try {
            saveLocation = writeImage(decodeBase64ToByteArray(response.getString("value")), (UUID.randomUUID()) + "." + format);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return saveLocation;
    }


    private static byte[] decodeBase64ToByteArray(String base64Image) {
       return Base64.getDecoder().decode(base64Image);
    }

    private static Path writeImage(byte[] imgData, String imgName) throws IOException {
        Path targetDir = Paths.get(savePath.toString(), imgName);
        //Ensure all dirs are created.
        Files.createDirectories(savePath);
        // Create a file output stream to write the image file
        try (FileOutputStream fos = new FileOutputStream(targetDir.toString())) {
            fos.write(imgData);
        }
        return targetDir;
    }

}
