package elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ScreenShot {

    private static String savePath = Paths.get(System.getProperty("user.dir"), "target", "screenshots").toString();
    private static String format = "png";

    public static class config {

        public static void setSavePath(String path) {
            if (path == null) {
                throw new IllegalArgumentException("save path can't be null");
            }
            savePath = path;
        }

        public static void setImageFormat(String imageFormat) {
            if (imageFormat == null) {
                throw new IllegalArgumentException("Image format can't be null");
            }
            format = imageFormat;
        }
    }

    public static String takeScreenShot() {
        Response response = HttpMethodExecutor.doGetRequest(EndPoints.TAKE_SCREENSHOT);
        String saveLocation = null;
        try {
            saveLocation =  writeImage(decodeBase64ToByteArray(response.getString("value")), (System.currentTimeMillis()) + "." + format);
        } catch (IOException e) {

        }
        return saveLocation;
    }

    public static String takeScreenShot(WebElement element) {
        SearchContext context = element.getSearchContext();
        if (context == null) {
            //element couldn't be found this method will return null
            return null;
        }
        Response response = HttpMethodExecutor.doGetRequest(EndPoints.buildEndpoint(EndPoints.TAKE_ELEMENT_SCREENSHOT, context.elementId()));
        String saveLocation = null;
        try {
            saveLocation = writeImage(decodeBase64ToByteArray(response.getString("value")), (System.currentTimeMillis()) + "." + format);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return saveLocation;
    }


    private static byte[] decodeBase64ToByteArray(String base64Image) {
       return Base64.getDecoder().decode(base64Image);
    }

    private static String writeImage(byte[] imgData, String imgName) throws IOException {
        String targetDir = Paths.get(savePath, imgName).toString();
        //Ensure all dirs are created.
        Files.createDirectories(Path.of(savePath));
        // Create a file output stream to write the image file
        try (FileOutputStream fos = new FileOutputStream(targetDir)) {
            fos.write(imgData);
        }
        return targetDir;
    }

}
