package client;

import elements.*;
import exceptions.NoSuchFrameException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sleeper.Sleeper;
import testbase.TestBase;

import static Constants.Constants.FIVE_SECONDS;
import static Constants.Constants.TWO_SECONDS;
import static elements.WebElementsFactory.$;

public class IframesTest extends TestBase {


    WebElement firstIframe = $(Locator.id("iframeResult"));
    WebElement h2inFirstIframe = $(Locator.xpath("//h2"));
    WebElement iframeInsideFirst = $(Locator.xpath("//iframe[@title = 'Iframe Example']"));

    WebElement h1inSecondIFrame = $(Locator.xpath("//h1"));



    @Test
    public void switchIframeTest() {
        WebDriver.get().open("https://seleniumbase.io/w3schools/iframes");
        Wait wait = new Wait();
//        WebDriver.get().switchTo().frame();
        wait.forResult(ExpectedResult.frameAvailableAndSwitchToIt(firstIframe));
        Assertions.assertEquals("HTML Iframes (nested iframes)", h2inFirstIframe.getText());

        WebDriver.get().switchTo().frame(iframeInsideFirst);
        Assertions.assertEquals("This page is displayed in an iframe.", h1inSecondIFrame.getText());

        WebDriver.get().switchTo().defaultContent();
        Assertions.assertTrue($(Locator.xpath("//span[@id='framesize']")).getText().contains("Result Size:"));
    }


    @Test @Tag("parallel")
    public void switchToNonExistingFrame() {
        WebDriver.get().open("https://seleniumbase.io/w3schools/iframes");
        WebDriver.get().timeouts().set().implicitWait(FIVE_SECONDS);
//        DriverClient.Contexts.switchToFrame($(By.xpath("invalid")));
        Assertions.assertThrows(NoSuchFrameException.class, () -> WebDriver.get().switchTo().frame(2));

    }

    @Test @Tag("parallel")
    public void switchToFrameByIndex() {
        WebDriver.get().open("https://seleniumbase.io/w3schools/iframes");
        WebDriver.get().timeouts().set().implicitWait(FIVE_SECONDS);
        Sleeper.sleep(TWO_SECONDS);
        WebDriver.get().switchTo().frame(0);
        Assertions.assertEquals("HTML Iframes (nested iframes)", h2inFirstIframe.getText());
    }
}
