package client;

import elements.WebDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sleeper.Sleeper;
import testbase.TestBase;

import java.io.IOException;

import static Constants.Constants.FIVE_SECONDS;

public class GetURLTest extends TestBase {

    @Test @Tag("parallel")
    public void getURLTest() {

//        Sleeper.sleep(FIVE_SECONDS);
        WebDriver.get().open("https://www.georgeveropoulos.com");
//        WebDriverContainer.get().close();

//        Assertions.assertTrue(WebDriverContainer.get().navigation().getCurrentUrl().contains("https://www.georgeveropoulos.com"));
//        WebDriverContainer.get().switchTo().newTab();
//        WebDriverContainer.get().open("https://www.georgeveropoulos.com");
//        WebDriverContainer.get().close();
//        System.out.println("");
//        WebDriverContainer.get().switchTo().newTab();
//        WebDriverContainer.get().open("https://www.georgeveropoulos.com");
//        WebDriverContainer.get().close();
        System.out.println("");

    }

}
