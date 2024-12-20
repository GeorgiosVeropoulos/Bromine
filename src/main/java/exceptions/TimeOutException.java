package exceptions;

public class TimeOutException extends RuntimeException {

    public TimeOutException(String format) {
        super(format);
    }
}
