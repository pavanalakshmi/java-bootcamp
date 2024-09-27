package exception_handling.trading.exception;

public class HitErrorsThresholdException extends RuntimeException{
    public HitErrorsThresholdException(String message) {
        super(message);
    }
}
