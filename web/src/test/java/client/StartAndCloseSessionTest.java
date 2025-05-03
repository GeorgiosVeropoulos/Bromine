package client;

import elements.WebDriver;
import org.junit.jupiter.api.Test;
import sleeper.Sleep;
import testbase.TestBase;
import static Constants.Constants.TWO_SECONDS;

public class StartAndCloseSessionTest extends TestBase {


    @Test
    public void startAndCloseSessionTest() {
        System.out.println("Hello world!");
        Sleep.For(TWO_SECONDS);
        WebDriver.get().open("https://www.georgeveropoulos.com/");
        Sleep.For(TWO_SECONDS);
    }
}
