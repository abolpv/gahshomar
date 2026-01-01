package io.github.abolpv.gahshomar.exception;

/**
 * Base exception for all Gahshomar date-related errors.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public class DateException extends RuntimeException {
    
    public DateException(String message) {
        super(message);
    }
    
    public DateException(String message, Throwable cause) {
        super(message, cause);
    }
}
