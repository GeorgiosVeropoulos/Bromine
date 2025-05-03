package elements;

import capabilities.Configuration;
import exceptions.TimeOutException;
import exceptions.WebDriverException;
import lombok.extern.slf4j.Slf4j;
import org.bromine.annotations.ThreadSafe;
import org.bromine.annotations.UnderDevelopment;
import org.slf4j.helpers.CheckReturnValue;

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
 *
 * <p>The goal of this class is to limit or eliminate the usage of try catch blocks by its users.
 * It should be noted that this class is still underdevelopment and could change in the future.</p>
 */
@Slf4j
@ThreadSafe
@UnderDevelopment
public class Wait {

    private final ArrayList<Class<? extends WebDriverException>> exceptions; // List of exceptions to ignore
    protected Duration polling; // Polling time
    protected Duration timeout; // Timeout duration

    protected Clock clock; // Clock instance to get the current time

    public Wait(Duration timeout, Duration polling, Clock clock) {
        this.timeout = timeout;
        this.polling = polling;
        this.clock = clock;
        exceptions = new ArrayList<>();
    }

    public Wait(Duration timeout, Duration polling) {
        this(timeout, polling, Clock.systemDefaultZone());
    }

    /**
     * With default timeout and polling
     */
    public Wait() {
        this(Configuration.waiters().getTimeout(), Configuration.waiters().getPolling(), Clock.systemDefaultZone());
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
                    System.out.println("Condition was called with result: " + result);
                    // Check the condition
                    if (ExpectedResult.lastException.get() != null)
                        handleExceptions(ExpectedResult.lastException.get());
                    if (result != null && (Boolean.class != result.getClass() || Boolean.TRUE.equals(result))) {
                        return result;
                    }

                } catch (WebDriverException exception) {
                    // Ignore exceptions to retry condition
                    System.out.println("Was this ever Called?");
                    handleExceptions(exception);
                }

                // Timeout check
                if (start.isBefore(clock.instant())) {
                    return handleTimeout(condition);
                }

                // Polling delay
                log.info("Waiting " + polling + "ms before re-checking condition");
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
    public final Wait ignoreExceptions(Class<? extends WebDriverException>... exceptions) {
        this.exceptions.addAll(List.of(exceptions));
        return this;
    }

    private void handleExceptions(WebDriverException exception) throws WebDriverException {
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
            } catch (WebDriverException e) {
                // Check if the exception should be ignored
                handleExceptions(e);
                return condition.get();
            }
        } else {
            TimeOutException exception = new TimeOutException(String.format(
                    "Condition wasn't met after a timeout of %sms with polling every %sms", timeout.toMillis(), polling.toMillis()));
            if (ExpectedResult.lastException.get() != null) {
                exception.initCause(ExpectedResult.lastException.get()); // Set the last Exception as the cause
            }
            throw exception;
        }
    }

}
