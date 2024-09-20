package jdbc.optimistic_locking.exception;

public class SQLIntegrityConstraintViolationException extends RuntimeException{
    public SQLIntegrityConstraintViolationException(String message) {
        super(message);
    }
}
