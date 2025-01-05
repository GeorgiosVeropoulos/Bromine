package client;

import elements.WebDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import testbase.TestBase;

public class WindowsTest extends TestBase {


    @Test @Tag("coverage")
    public void closingLastWindowReturnsNullTest() {
        String handle = WebDriver.get().window().getWindowHandle();
        Assertions.assertNotNull(handle);
        WebDriver.get().close();
        handle = WebDriver.get().window().getWindowHandle();
        Assertions.assertNull(handle);
    }

    @Test @Tag("coverage")
    public void switchToNewTabHandleTest() {
        WebDriver.get().open("https://www.google.com");
        String handle = WebDriver.get().window().getWindowHandle();
        Assertions.assertNotNull(handle);
        WebDriver.get().switchTo().newTab();
        WebDriver.get().open("https://www.google.com");
        String handle1 = WebDriver.get().window().getWindowHandle();
        WebDriver.get().close();
        Assertions.assertEquals(handle, WebDriver.get().window().getWindowHandle());
        Assertions.assertNotEquals(handle, handle1);

    }
}
