package hr.java.production.exception;

/**
 * Represents a custom exception thrown when identical item choices are encountered.
 * Extends the {@code Exception} class, meaning it's a checked exception.
 *
 * <p><b>Note:</b> Throw this exception when an item choice is identical to an existing one.</p>
 */
public class IdenticalItemChoiceException extends Exception {
    /**
     * Constructs a new IdenticalItemChoiceException with {@code null} as its detail message.
     */
    public IdenticalItemChoiceException() {
        super();
    }

    /**
     * Constructs a new IdenticalItemChoiceException with the specified message.
     *
     * @param message The message is saved for later retrieval by the {@code Throwable.getMessage()} method.
     */
    public IdenticalItemChoiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new IdenticalItemChoiceException with the specified message and cause.
     *
     * @param message The message (which is saved for later retrieval by the {@code Throwable.getMessage()} method).
     * @param cause   The cause (which is saved for later retrieval by the {@code Throwable.getCause()} method).
     *                (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public IdenticalItemChoiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new IdenticalItemChoiceException with the specified cause.
     *
     * @param cause The cause (which is saved for later retrieval by the {@code Throwable.getCause()} method).
     *              (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public IdenticalItemChoiceException(Throwable cause) {
        super(cause);
    }
}
