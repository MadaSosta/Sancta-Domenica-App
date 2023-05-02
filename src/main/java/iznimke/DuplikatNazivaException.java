package iznimke;

public class DuplikatNazivaException extends Exception{

    public DuplikatNazivaException() {
    }

    public DuplikatNazivaException(String message) {
        super(message);
    }

    public DuplikatNazivaException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplikatNazivaException(Throwable cause) {
        super(cause);
    }

    public DuplikatNazivaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
