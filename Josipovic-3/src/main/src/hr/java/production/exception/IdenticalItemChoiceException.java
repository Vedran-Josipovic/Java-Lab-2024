package hr.java.production.exception;

public class IdenticalItemChoiceException extends Exception {
    public IdenticalItemChoiceException() {
    }

    public IdenticalItemChoiceException(String message) {
        super(message);
    }

    public IdenticalItemChoiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdenticalItemChoiceException(Throwable cause) {
        super(cause);
    }
}
