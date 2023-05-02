package iznimke;

public class PrevisokaPlacaException extends RuntimeException{

    public PrevisokaPlacaException() {
    }

    public PrevisokaPlacaException(String message) {
        super(message);
    }

    public PrevisokaPlacaException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrevisokaPlacaException(Throwable cause) {
        super(cause);
    }

    public PrevisokaPlacaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
