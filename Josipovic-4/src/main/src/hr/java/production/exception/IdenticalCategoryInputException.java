package hr.java.production.exception;

/**
 * Represents a custom exception thrown when identical category input is encountered.
 * Extends the {@code RuntimeException} class, meaning it's an unchecked exception.
 *
 * <p><b>Note:</b> Throw this exception when a category input is identical to an existing one.</p>
 */
public class IdenticalCategoryInputException extends RuntimeException {
    /**
     * Constructs a new IdenticalCategoryInputException with {@code null} as its detail message.
     */
    public IdenticalCategoryInputException() {
    }

    /**
     * Constructs a new IdenticalCategoryInputException with the specified message.
     *
     * @param message The message is saved for later retrieval by the {@code Throwable.getMessage()} method.
     */
    public IdenticalCategoryInputException(String message) {
        super(message);
    }

    /**
     * Constructs a new IdenticalCategoryInputException with the specified message and cause.
     *
     * @param message The message (which is saved for later retrieval by the {@code Throwable.getMessage()} method).
     * @param cause   The cause (which is saved for later retrieval by the {@code Throwable.getCause()} method).
     *                (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public IdenticalCategoryInputException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new IdenticalCategoryInputException with the specified cause.
     *
     * @param cause The cause (which is saved for later retrieval by the {@code Throwable.getCause()} method).
     *              (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public IdenticalCategoryInputException(Throwable cause) {
        super(cause);
    }


}
