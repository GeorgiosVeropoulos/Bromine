package exceptions;

public class FailedToDownloadDriverException extends RuntimeException{


    public FailedToDownloadDriverException() {
    }

    public FailedToDownloadDriverException(String message) {
        super(message);
    }

    public FailedToDownloadDriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToDownloadDriverException(Throwable cause) {
        super(cause);
    }

    protected FailedToDownloadDriverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
