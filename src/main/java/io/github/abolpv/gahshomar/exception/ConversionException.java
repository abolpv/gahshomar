package io.github.abolpv.gahshomar.exception;

/**
 * Thrown when a date conversion fails.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public class ConversionException extends DateException {
    
    public ConversionException(String message) {
        super(message);
    }
    
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
