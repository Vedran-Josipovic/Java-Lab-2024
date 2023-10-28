package hr.java.production.exception;

public class IdenticalCategoryInputException extends RuntimeException{
    public IdenticalCategoryInputException() {
    }

    public IdenticalCategoryInputException(String message) {
        super(message);
    }

    public IdenticalCategoryInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdenticalCategoryInputException(Throwable cause) {
        super(cause);
    }
}
