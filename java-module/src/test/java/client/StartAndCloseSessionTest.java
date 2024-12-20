package client;

import elements.WebDriver;
import org.junit.jupiter.api.Test;
import sleeper.Sleeper;
import testbase.TestBase;
import static Constants.Constants.TWO_SECONDS;

public class StartAndCloseSessionTest extends TestBase {


    @Test
    public void startAndCloseSessionTest() {
        System.out.println("Hello world!");
        Sleeper.sleep(TWO_SECONDS);
        WebDriver.get().open("https://www.georgeveropoulos.com/");
        Sleeper.sleep(TWO_SECONDS);
    }
}
