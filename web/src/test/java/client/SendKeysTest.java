package client;

import elements.ExpectedResult;
import elements.Locator;
import elements.WebElement;
import elements.WebDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.SauceLogin;
import testbase.TestBase;


public class SendKeysTest extends TestBase {


    SauceLogin sauceLogin;

    @BeforeEach
    public void before() {
        sauceLogin = new SauceLogin();
    }


    @Test @Tag("coverage")
    public void sendKeysTest() {
        WebDriver.get().open("https://www.saucedemo.com/v1/");
        sauceLogin.username.sendKeys("Hello World");
        Assertions.assertEquals("Hello World", sauceLogin.username.getAttribute("value"));

    }

    @Test @Tag("coverage") @Tag("test1")
    public void sendKeysTest2() {
        WebDriver.get().open("https://www.saucedemo.com/v1/");
        sauceLogin.username.sendKeys("Hello World");
        Assertions.assertEquals("Hello World", sauceLogin.username.getAttribute("value"));

    }
}
