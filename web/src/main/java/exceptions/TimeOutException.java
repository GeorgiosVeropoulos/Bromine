package exceptions;

public class TimeOutException extends WebDriverException {

    public TimeOutException(String format) {
        super(format);
    }

    public TimeOutException(String message, Throwable cause) {
        super(message, cause);
    }
}
