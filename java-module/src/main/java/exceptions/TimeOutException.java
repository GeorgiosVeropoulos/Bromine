package exceptions;

public class TimeOutException extends WebDriverException {

    public TimeOutException(String format) {
        super(format);
    }
}
