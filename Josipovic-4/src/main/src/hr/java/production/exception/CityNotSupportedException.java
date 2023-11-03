package hr.java.production.exception;

public class CityNotSupportedException extends Exception{
    public CityNotSupportedException() {
    }

    public CityNotSupportedException(String message) {
        super(message);
    }

    public CityNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CityNotSupportedException(Throwable cause) {
        super(cause);
    }
}
