package io.github.abolpv.gahshomar.exception;

/**
 * Thrown when an invalid date is provided.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public class InvalidDateException extends DateException {
    
    private final int year;
    private final int month;
    private final int day;
    
    public InvalidDateException(int year, int month, int day) {
        super(String.format("Invalid date: %d/%d/%d", year, month, day));
        this.year = year;
        this.month = month;
        this.day = day;
    }
    
    public InvalidDateException(String message) {
        super(message);
        this.year = 0;
        this.month = 0;
        this.day = 0;
    }
    
    public int getYear() {
        return year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public int getDay() {
        return day;
    }
}
