package exceptions;

/**
 * Parent to all exceptions thrown by WebDriver.
 * a generic runtime exception indicating something went wrong with a WebDriver action.
 * Methods that indicate throwing this method means that it can potentially throw any of its children.
 */
public class WebDriverException extends RuntimeException {


    public WebDriverException() {
    }


    public WebDriverException(String message) {
        super(message);
    }

    public WebDriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebDriverException(Throwable cause) {
        super(cause);
    }

    protected WebDriverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
