package sleeper;


import java.time.Duration;
import java.util.logging.Logger;

public class Sleeper {

    public static void sleepInSeconds(int seconds) {
        try {
            Thread.sleep(seconds *  1000);
        } catch (InterruptedException e) {
            //nothing
        }
    }


    public static void sleep(Duration duration) {
        try {
            Sleep.sleep.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted Exception would be thrown!");
        }
    }
}
