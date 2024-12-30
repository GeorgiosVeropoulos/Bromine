package capabilities;

import lombok.Getter;
import lombok.experimental.PackagePrivate;

import java.time.Duration;

@Getter
@PackagePrivate
public class NetworkSettings {

    private int readTimeout = 60_000;
    private int connectionTimeout = 60_000;

    public NetworkSettings setReadTimeout(Duration duration) {
        readTimeout = getTimeout(duration);
        return this;
    }

    public NetworkSettings setConnectionTimeout(Duration duration) {
        connectionTimeout = getTimeout(duration);
        return this;
    }


    private int getTimeout(Duration duration) {
        long millis = duration.toMillis();  // Get the total milliseconds (long)
        int valueToReturn;
        // Check if the value is within the int range
        if (millis > Integer.MAX_VALUE) {
            // Option 1: Clamp to max int value (handle overflow)
            valueToReturn = Integer.MAX_VALUE;
        } else if (millis < Integer.MIN_VALUE) {
            // Option 2: Clamp to min int value (handle overflow)
            valueToReturn = Integer.MIN_VALUE;
        } else {
            // Option 3: Safe cast to int
            valueToReturn = (int) millis;
        }
        return valueToReturn;
    }

}
