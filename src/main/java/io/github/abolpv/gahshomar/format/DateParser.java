package io.github.abolpv.gahshomar.format;

import io.github.abolpv.gahshomar.PersianDate;
import io.github.abolpv.gahshomar.exception.ParseException;
import io.github.abolpv.gahshomar.temporal.PersianMonth;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for Persian date strings.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class DateParser {
    
    // Common date patterns
    private static final Pattern NUMERIC_SLASH = Pattern.compile("^(\\d{2,4})/(\\d{1,2})/(\\d{1,2})$");
    private static final Pattern NUMERIC_DASH = Pattern.compile("^(\\d{2,4})-(\\d{1,2})-(\\d{1,2})$");
    private static final Pattern NUMERIC_DOT = Pattern.compile("^(\\d{2,4})\\.(\\d{1,2})\\.(\\d{1,2})$");
    private static final Pattern COMPACT = Pattern.compile("^(\\d{4})(\\d{2})(\\d{2})$");
    
    private DateParser() {
        // Utility class
    }
    
    /**
     * Parses a date string automatically detecting the format.
     */
    public static PersianDate parse(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new ParseException("Date string cannot be null or empty");
        }
        
        // Normalize Persian/Arabic digits to Western
        String normalized = PersianNumbers.toWestern(text.trim());
        
        // Try different patterns
        Optional<PersianDate> result;
        
        result = tryParseNumeric(normalized, NUMERIC_SLASH);
        if (result.isPresent()) return result.get();
        
        result = tryParseNumeric(normalized, NUMERIC_DASH);
        if (result.isPresent()) return result.get();
        
        result = tryParseNumeric(normalized, NUMERIC_DOT);
        if (result.isPresent()) return result.get();
        
        result = tryParseCompact(normalized);
        if (result.isPresent()) return result.get();
        
        result = tryParsePersianText(normalized);
        if (result.isPresent()) return result.get();
        
        throw new ParseException(text);
    }
    
    /**
     * Parses a date string with a specific pattern.
     */
    public static PersianDate parse(String text, String pattern) {
        if (text == null || text.trim().isEmpty()) {
            throw new ParseException("Date string cannot be null or empty");
        }
        
        String normalized = PersianNumbers.toWestern(text.trim());
        
        // Simple pattern matching
        try {
            String regex = patternToRegex(pattern);
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(normalized);
            
            if (m.matches()) {
                int year = 0, month = 0, day = 0;
                
                // Extract based on pattern
                if (pattern.contains("yyyy")) {
                    year = extractYear(m, pattern, "yyyy");
                } else if (pattern.contains("yy")) {
                    int yy = extractYear(m, pattern, "yy");
                    year = yy < 50 ? 1400 + yy : 1300 + yy;
                }
                
                if (pattern.contains("MM") || pattern.contains("M")) {
                    month = extractMonth(m, pattern);
                }
                
                if (pattern.contains("dd") || pattern.contains("d")) {
                    day = extractDay(m, pattern);
                }
                
                return PersianDate.of(year, month, day);
            }
        } catch (Exception e) {
            // Fall through to exception
        }
        
        throw new ParseException(text, pattern);
    }
    
    /**
     * Tries to parse a date string, returning Optional.empty() on failure.
     */
    public static Optional<PersianDate> tryParse(String text) {
        try {
            return Optional.of(parse(text));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Tries to parse a date string with a pattern.
     */
    public static Optional<PersianDate> tryParse(String text, String pattern) {
        try {
            return Optional.of(parse(text, pattern));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    private static Optional<PersianDate> tryParseNumeric(String text, Pattern pattern) {
        Matcher m = pattern.matcher(text);
        if (m.matches()) {
            try {
                int year = Integer.parseInt(m.group(1));
                int month = Integer.parseInt(m.group(2));
                int day = Integer.parseInt(m.group(3));
                
                // Handle 2-digit years
                if (year < 100) {
                    year = year < 50 ? 1400 + year : 1300 + year;
                }
                
                return Optional.of(PersianDate.of(year, month, day));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
    
    private static Optional<PersianDate> tryParseCompact(String text) {
        Matcher m = COMPACT.matcher(text);
        if (m.matches()) {
            try {
                int year = Integer.parseInt(m.group(1));
                int month = Integer.parseInt(m.group(2));
                int day = Integer.parseInt(m.group(3));
                return Optional.of(PersianDate.of(year, month, day));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
    
    private static Optional<PersianDate> tryParsePersianText(String text) {
        // Try to parse text like "15 دی 1403"
        for (PersianMonth month : PersianMonth.values()) {
            if (text.contains(month.getPersianName())) {
                try {
                    String[] parts = text.split("\\s+");
                    int day = 0, year = 0;
                    
                    for (String part : parts) {
                        if (part.matches("\\d{4}")) {
                            year = Integer.parseInt(part);
                        } else if (part.matches("\\d{1,2}")) {
                            day = Integer.parseInt(part);
                        }
                    }
                    
                    if (year > 0 && day > 0) {
                        return Optional.of(PersianDate.of(year, month.getValue(), day));
                    }
                } catch (Exception e) {
                    // Continue trying
                }
            }
        }
        return Optional.empty();
    }
    
    private static String patternToRegex(String pattern) {
        return pattern
            .replace("yyyy", "(\\d{4})")
            .replace("yy", "(\\d{2})")
            .replace("MM", "(\\d{2})")
            .replace("M", "(\\d{1,2})")
            .replace("dd", "(\\d{2})")
            .replace("d", "(\\d{1,2})")
            .replace("MMMM", "([\\u0600-\\u06FF]+)")
            .replace("EEEE", "([\\u0600-\\u06FF]+)");
    }
    
    private static int extractYear(Matcher m, String pattern, String yearPattern) {
        int idx = pattern.indexOf(yearPattern);
        // Simple extraction - in real implementation would need proper group tracking
        return Integer.parseInt(m.group(1));
    }
    
    private static int extractMonth(Matcher m, String pattern) {
        return Integer.parseInt(m.group(2));
    }
    
    private static int extractDay(Matcher m, String pattern) {
        return Integer.parseInt(m.group(3));
    }
}
