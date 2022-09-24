package das.tools.notifier.notify.exceptions;

public class WrongRequestParameterException extends Exception {
    public WrongRequestParameterException() {
        super();
    }

    public WrongRequestParameterException(String message) {
        super(message);
    }

    public WrongRequestParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongRequestParameterException(Throwable cause) {
        super(cause);
    }

    protected WrongRequestParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
