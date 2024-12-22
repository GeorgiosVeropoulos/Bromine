package sleeper;


import java.time.Duration;

public interface Sleep {

    Sleep sleep = duration -> Thread.sleep(duration.toMillis());

    void sleep(Duration duration) throws InterruptedException;
}
