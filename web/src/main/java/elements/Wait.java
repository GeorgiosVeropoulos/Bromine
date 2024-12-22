package elements;

import exceptions.TimeOutException;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * The Wait class provides a way to explicitly wait for certain conditions to be true or false
 * <p>The polling time by default is 500ms</p>
 * <p>The timeout duration by default is 20 seconds</p>
 *
 * <h4>Furthermore the class will:</h4>
 * <ul>
 * <li>Handle the threadsafety</li>
 * <li>Mofidies the usage of implicit waits to not overlap during its execution.</li>
 *
 * </ul>
 */
@ThreadSafe
public class Wait {

    private final ArrayList<Class<? extends RuntimeException>> exceptions;
    protected Duration polling;
    protected Duration timeout;

    protected Clock clock;

    public Wait(Duration timeout, Duration polling, Clock clock) {
        this.timeout = timeout;
        this.polling = polling;
        this.clock = clock;
        exceptions = new ArrayList<>();
    }

    public Wait(Duration timeout, Duration polling) {
        this(timeout, polling, Clock.systemDefaultZone());
    }

    public Wait() {
        this(WebDriver.get().timeouts().get().implicitWait().isZero() ? Duration.ofSeconds(20) : WebDriver.get().timeouts().get().implicitWait(),
                Duration.ofMillis(500));
    }


    /**
     *
     * @param condition
     * @return
     * @param <T>
     */
    @CheckReturnValue
    public <T> T forResult(Supplier<T> condition) {
        Instant start = clock.instant().plus(timeout);
        // Save the current implicit wait and set it to zero for explicit waiting
        Duration implicitWait = WebDriver.get().timeouts().get().implicitWait();
        WebDriver.get().timeouts().set().implicitWait(Duration.ZERO);

        try {
            while (true) {

                try {
                    T result = condition.get();
                    // Check the condition
                    if (result != null && (Boolean.class != result.getClass() || Boolean.TRUE.equals(result))) {
                        return result;
                    }

                } catch (RuntimeException exception) {
                    // Ignore exceptions to retry condition
                    handleExceptions(exception);
                }

                // Timeout check
                if (start.isBefore(clock.instant())) {
                    return handleTimeout(condition);
                }

                // Polling delay
                System.out.println("Waiting " + polling + "ms before re-checking condition");
                sleep(polling);
            }
        } finally {
            // Restore the original implicit wait
            WebDriver.get().timeouts().set().implicitWait(implicitWait);
        }
    }


    private void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted Exception would be thrown!");
        }
    }

    @SafeVarargs
    public final Wait ignoreExceptions(Class<? extends RuntimeException>... exceptions) {
        this.exceptions.addAll(List.of(exceptions));
        return this;
    }

    private void handleExceptions(RuntimeException exception) throws RuntimeException {
        if (exceptions.isEmpty()) {
            throw exception;
        }
        if (!exceptions.contains(exception.getClass())) {
            throw exception;
        }
    }

    // Updated handleTimeout method to safely attempt condition.get()
    private <T> T handleTimeout(Supplier<T> condition) {
        if (exceptions.contains(TimeOutException.class)) {
            T result = null;
            try {
                result = condition.get();
                // If condition is Boolean, return false; otherwise, return null
                if (result instanceof Boolean) {
                    return (T) Boolean.FALSE;
                }
                return result;
            } catch (RuntimeException e) {
                // Check if the exception should be ignored
                handleExceptions(e);
                return condition.get();
            }
        } else {
            throw new TimeOutException(String.format(
                    "Condition wasn't met after a timeout of %s with polling every %s", timeout, polling));
        }
    }

}
