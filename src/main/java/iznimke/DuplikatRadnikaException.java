package iznimke;

public class DuplikatRadnikaException extends Exception{

    public DuplikatRadnikaException() {
    }

    public DuplikatRadnikaException(String message) {
        super(message);
    }

    public DuplikatRadnikaException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplikatRadnikaException(Throwable cause) {
        super(cause);
    }

    public DuplikatRadnikaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
