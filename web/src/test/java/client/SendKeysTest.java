package client;

import elements.Locator;
import elements.WebElement;
import elements.WebDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import testbase.TestBase;

import static elements.WebElementsFactory.$;

public class SendKeysTest extends TestBase {


    WebElement username = $(Locator.id("user-name"));


    @Test @Tag("coverage")
    public void sendKeysTest() {
        WebDriver.get().open("https://www.saucedemo.com/v1/");

//        username.click();?
        username.sendKeys("Hello World");

        Assertions.assertEquals("Hello World", username.getAttribute("value"));

    }
}
