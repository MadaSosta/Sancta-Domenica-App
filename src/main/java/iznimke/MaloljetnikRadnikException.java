package iznimke;

public class MaloljetnikRadnikException extends RuntimeException{

    public MaloljetnikRadnikException() {
    }

    public MaloljetnikRadnikException(String message) {
        super(message);
    }

    public MaloljetnikRadnikException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaloljetnikRadnikException(Throwable cause) {
        super(cause);
    }

    public MaloljetnikRadnikException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
