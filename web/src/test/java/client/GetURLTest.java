package client;

import elements.WebDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import testbase.TestBase;

public class GetURLTest extends TestBase {

    @Test @Tag("parallel") @Tag("test1")
    public void getURLTest() {

//        Sleeper.sleep(FIVE_SECONDS);
        WebDriver.get().open("https://www.georgeveropoulos.com");
//        WebDriverContainer.get().close();

        Assertions.assertTrue(WebDriver.get().navigation().getCurrentUrl().contains("https://www.georgeveropoulos.com"));
//        WebDriverContainer.get().switchTo().newTab();
//        WebDriverContainer.get().open("https://www.georgeveropoulos.com");
//        WebDriverContainer.get().close();
//        System.out.println("");
//        WebDriverContainer.get().switchTo().newTab();
//        WebDriverContainer.get().open("https://www.georgeveropoulos.com");
//        WebDriverContainer.get().close();
//        System.out.println("");

    }

}
