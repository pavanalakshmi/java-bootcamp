package exception_handling.trading.exception;

public class InvalidThresholdValueException extends RuntimeException{
    public InvalidThresholdValueException(String message) {
        super(message);
    }
}
