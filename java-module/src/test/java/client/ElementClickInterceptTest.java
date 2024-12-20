package client;

import elements.Locator;
import elements.WebElement;
import elements.WebDriver;
import exceptions.ElementClickInterceptedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sleeper.Sleeper;
import testbase.TestBase;

import static Constants.Constants.FIVE_SECONDS;
import static elements.WebElementsFactory.$;

public class ElementClickInterceptTest extends TestBase {


    WebElement toBeIntercepted = $(Locator.xpath("//div//time"));

    @Test @Tag("parallel")
    public void checkInterceptTest() {
        WebDriver.get().open("https://www.rollingstone.com/tv/tv-reviews/shining-girls-review-elisabeth-moss-1339682/");
        Sleeper.sleep(FIVE_SECONDS);
        Assertions.assertThrows(ElementClickInterceptedException.class, toBeIntercepted::click);

    }
}
