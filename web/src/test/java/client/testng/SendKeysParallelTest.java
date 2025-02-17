package client.testng;

import elements.Locator;
import elements.WebDriver;
import elements.WebElement;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.SauceLogin;
import testbase.TestBaseTestNG;

import static elements.WebElementsFactory.$;

public class SendKeysParallelTest extends TestBaseTestNG {

//    ThreadLocal<SauceLogin> sauceLogin = new ThreadLocal<>();
    SauceLogin sauceLogin;


    @BeforeMethod(alwaysRun = true)
    public void before() {
//        sauceLogin.set(new SauceLogin());
        sauceLogin = new SauceLogin();
    }

    @Test
    public void sendKeysTest() {
        WebDriver.get().open("https://www.saucedemo.com/v1/");
//        sauceLogin.get().username.sendKeys("Hello World");
//        Assertions.assertEquals("Hello World", sauceLogin.get().username.getAttribute("value"));
        sauceLogin.username.sendKeys("Hello World");
        Assertions.assertEquals("Hello World", sauceLogin.username.getAttribute("value"));
    }

    @Test
    public void sendKeysTest2() {
        WebDriver.get().open("https://www.saucedemo.com/v1/");
//        sauceLogin.get().username.sendKeys("Hello World");
//        Assertions.assertEquals("Hello World", sauceLogin.get().username.getAttribute("value"));
        sauceLogin.username.sendKeys("Hello World");
        Assertions.assertEquals("Hello World", sauceLogin.username.getAttribute("value"));
    }
}
