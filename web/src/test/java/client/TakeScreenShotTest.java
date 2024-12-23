package client;

import elements.*;
import org.junit.jupiter.api.Test;
import testbase.TestBase;

import static elements.WebElementsFactory.$;

public class TakeScreenShotTest extends TestBase {

    WebElement info = $(Locator.xpath("//*[@id='info']"));

    @Test
    public void takeSreenShotTest() {

        System.out.println("HASH  " + 0x61c88647);
        WebDriver.get().open("https://www.georgeveropoulos.com/");
        System.out.println(ScreenShot.takeScreenShot(info));
//        ScreenShot.takeScreenShot();
    }
}
