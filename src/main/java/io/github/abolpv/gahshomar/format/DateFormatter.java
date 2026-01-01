package io.github.abolpv.gahshomar.format;

import io.github.abolpv.gahshomar.PersianDate;
import io.github.abolpv.gahshomar.temporal.PersianMonth;
import io.github.abolpv.gahshomar.temporal.PersianDayOfWeek;

import java.util.Objects;

/**
 * Formatter for Persian dates with customizable patterns.
 * Thread-safe and immutable.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class DateFormatter {
    
    private final String pattern;
    private final boolean usePersianDigits;
    
    private DateFormatter(String pattern, boolean usePersianDigits) {
        this.pattern = Objects.requireNonNull(pattern, "pattern cannot be null");
        this.usePersianDigits = usePersianDigits;
    }
    
    /**
     * Creates a formatter with the given pattern.
     */
    public static DateFormatter ofPattern(String pattern) {
        return new DateFormatter(pattern, false);
    }
    
    /**
     * Creates a formatter with the given pattern and Persian digits option.
     */
    public static DateFormatter ofPattern(String pattern, boolean usePersianDigits) {
        return new DateFormatter(pattern, usePersianDigits);
    }
    
    /**
     * Creates an ISO date formatter (yyyy/MM/dd).
     */
    public static DateFormatter isoDate() {
        return ofPattern(FormatPattern.ISO_DATE);
    }
    
    /**
     * Creates a long date formatter (d MMMM yyyy).
     */
    public static DateFormatter longDate() {
        return ofPattern(FormatPattern.LONG_DATE);
    }
    
    /**
     * Creates a full date formatter (EEEE d MMMM yyyy).
     */
    public static DateFormatter fullDate() {
        return ofPattern(FormatPattern.FULL_DATE);
    }
    
    /**
     * Formats the given Persian date.
     */
    public String format(PersianDate date) {
        Objects.requireNonNull(date, "date cannot be null");
        
        String result = pattern;
        
        // Year
        result = result.replace("yyyy", String.format("%04d", date.getYear()));
        result = result.replace("yy", String.format("%02d", date.getYear() % 100));
        
        // Month
        result = result.replace("MMMM", date.getMonthName());
        result = result.replace("MMM", abbreviate(date.getMonthName(), 3));
        result = result.replace("MM", String.format("%02d", date.getMonthValue()));
        result = result.replace("M", String.valueOf(date.getMonthValue()));
        
        // Day
        result = result.replace("EEEE", date.getDayOfWeekName());
        result = result.replace("EEE", abbreviate(date.getDayOfWeekName(), 2));
        result = result.replace("dd", String.format("%02d", date.getDayOfMonth()));
        result = result.replace("d", String.valueOf(date.getDayOfMonth()));
        
        if (usePersianDigits) {
            result = PersianNumbers.toPersian(result);
        }
        
        return result;
    }
    
    /**
     * Formats with the specified Persian digits option.
     */
    public String format(PersianDate date, boolean persianDigits) {
        String result = format(date);
        if (persianDigits && !usePersianDigits) {
            result = PersianNumbers.toPersian(result);
        }
        return result;
    }
    
    /**
     * Gets the pattern.
     */
    public String getPattern() {
        return pattern;
    }
    
    /**
     * Returns whether Persian digits are used.
     */
    public boolean usesPersianDigits() {
        return usePersianDigits;
    }
    
    /**
     * Returns a new formatter with Persian digits enabled.
     */
    public DateFormatter withPersianDigits() {
        return new DateFormatter(pattern, true);
    }
    
    /**
     * Returns a new formatter with Persian digits disabled.
     */
    public DateFormatter withWesternDigits() {
        return new DateFormatter(pattern, false);
    }
    
    private String abbreviate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength);
    }
    
    @Override
    public String toString() {
        return "DateFormatter[pattern=" + pattern + ", persianDigits=" + usePersianDigits + "]";
    }
}
