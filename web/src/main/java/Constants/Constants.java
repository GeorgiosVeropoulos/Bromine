package Constants;

import java.time.Duration;

public final class Constants {


    public static Duration ONE_SECOND = Duration.ofSeconds(1);
    public static Duration TWO_SECONDS = Duration.ofSeconds(2);
    public static Duration FIVE_SECONDS = Duration.ofSeconds(5);
    public static final String VALUE = "value";

    public static final String OS = System.getProperty("os.name");

    public static final String OS_LOWERCASE = OS.toLowerCase();
}
