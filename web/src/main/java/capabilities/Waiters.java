package capabilities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.PackagePrivate;

import java.time.Duration;


@Getter
@Setter
@PackagePrivate
public final class Waiters {


    private Waiters() {
    }

    private static class WaitersHolder {
        private static final Waiters INSTANCE = new Waiters();
    }

    static Waiters getInstance() {
        return WaitersHolder.INSTANCE;
    }

    private Duration timeout = Duration.ofSeconds(20);

    private Duration polling = Duration.ofMillis(500);
}
