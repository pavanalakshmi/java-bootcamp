package exception_handling.trading.exception;

public class HitInsertErrorsThresholdException extends RuntimeException{
    public HitInsertErrorsThresholdException(String message) {
        super(message);
    }
}
