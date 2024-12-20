package testing;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CDPTest {




    @Test
    public void cdpTest() {

//        Launcher launcher = new Launcher();
//        try (SessionFactory factory = launcher.launch();
//             Session session = factory.create()) {
//
//            // Navigate to a webpage
//            session.navigate("https://www.georgeveropoulos.com");
//
//            session.waitDocumentReady();
//
//            // Define the XPath expression
//            String xpath = "//*[@id='info']";
//
//            // Check if the element exists by evaluating XPath in JavaScript
//            byte[] screenshot = session.captureScreenshot();
//            saveScreenshotToFile(screenshot, "target/screenshot.png");
//            boolean elementExists = (boolean) session.evaluate(
//                    "document.evaluate(\"" + xpath + "\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue !== null");
//
//
//            if (elementExists) {
//
//                // Perform a click on the element located by XPath
//                session.evaluate("document.evaluate(\"" + xpath + "\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.click();");
//                System.out.println("Clicked on the element with XPath: " + xpath);
//            } else {
//                System.out.println("Element with XPath '" + xpath + "' does not exist.");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // Close the launcher after the session
//            launcher.kill();
//        }
    }

    private void saveScreenshotToFile(byte[] screenshot, String filePath) throws IOException {
        Path path = Paths.get(filePath);

        // Ensure the target directory exists
        Files.createDirectories(path.getParent());

        // Write the screenshot byte array to the file
        Files.write(path, screenshot);
        System.out.println("Screenshot saved at: " + path.toAbsolutePath());
    }
}
