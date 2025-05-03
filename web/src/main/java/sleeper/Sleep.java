package sleeper;


import java.time.Duration;

public class Sleep {

    public static void For(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted Exception would be thrown!");
        }
    }

    public static void ForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted Exception would be thrown!");
        }
    }

}
