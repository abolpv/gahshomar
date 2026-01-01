package io.github.abolpv.gahshomar.exception;

/**
 * Thrown when a date string cannot be parsed.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public class ParseException extends DateException {
    
    private final String input;
    private final String pattern;
    
    public ParseException(String input) {
        super("Cannot parse date: " + input);
        this.input = input;
        this.pattern = null;
    }
    
    public ParseException(String input, String pattern) {
        super(String.format("Cannot parse '%s' with pattern '%s'", input, pattern));
        this.input = input;
        this.pattern = pattern;
    }
    
    public String getInput() {
        return input;
    }
    
    public String getPattern() {
        return pattern;
    }
}
