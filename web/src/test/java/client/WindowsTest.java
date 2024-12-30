package client;

import elements.ChromeDriver;
import elements.WebDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testbase.TestBase;

public class WindowsTest extends TestBase {


    @Test
    public void closingLastWindowReturnsNullTest() {
        String handle = WebDriver.get().window().getWindowHandle();
        Assertions.assertNotNull(handle);
        WebDriver.get().close();
        handle = WebDriver.get().window().getWindowHandle();
        Assertions.assertNull(handle);
    }

    @Test
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
