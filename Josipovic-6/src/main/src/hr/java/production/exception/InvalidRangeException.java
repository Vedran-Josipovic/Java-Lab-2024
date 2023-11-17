package hr.java.production.exception;

/**
 * Represents a custom exception thrown when a number is outside the permitted range.
 * Extends the {@code RuntimeException} class, meaning it's an unchecked exception.
 *
 * <p><b>Note:</b> Throw this exception when a number is outside of the permitted range.</p>
 */
public class InvalidRangeException extends RuntimeException {
    /**
     * Constructs a new InvalidRangeException with {@code null} as its detail message.
     */
    public InvalidRangeException() {
        super();
    }

    /**
     * Constructs a new InvalidRangeException with the specified message.
     *
     * @param message The message is saved for later retrieval by the {@code Throwable.getMessage()} method.
     */
    public InvalidRangeException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidRangeException with the specified message and cause.
     *
     * @param message The message (which is saved for later retrieval by the {@code Throwable.getMessage()} method).
     * @param cause   The cause (which is saved for later retrieval by the {@code Throwable.getCause()} method).
     *                (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public InvalidRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new InvalidRangeException with the specified cause.
     *
     * @param cause The cause (which is saved for later retrieval by the {@code Throwable.getCause()} method).
     *              (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public InvalidRangeException(Throwable cause) {
        super(cause);
    }
}
