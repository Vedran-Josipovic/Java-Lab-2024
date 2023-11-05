package hr.java.production.exception;

/**
 * Represents a custom exception thrown when a city is not supported.
 * Extends the {@code Exception} class, meaning it's a checked exception.
 *
 * <p><b>Note:</b> Throw this exception when a city is not found in the supported cities list.</p>
 */
public class CityNotSupportedException extends Exception {
    /**
     * Constructs a new CityNotSupportedException with {@code null} as its detail message.
     */
    public CityNotSupportedException() {
    }

    /**
     * Constructs a new CityNotSupportedException with the specified message.
     *
     * @param message The message is saved for later retrieval by the {@code Throwable.getMessage()} method.
     */
    public CityNotSupportedException(String message) {
        super(message);
    }

    /**
     * Constructs a new CityNotSupportedException with the specified message and cause.
     *
     * @param message The message (which is saved for later retrieval by the {@code Throwable.getMessage()} method).
     * @param cause   The cause (which is saved for later retrieval by the {@code Throwable.getCause()} method).
     *                (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public CityNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new CityNotSupportedException with the specified cause.
     *
     * @param cause The cause (which is saved for later retrieval by the {@code Throwable.getCause()} method).
     *              (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public CityNotSupportedException(Throwable cause) {
        super(cause);
    }
}
