package io.github.abolpv.gahshomar.format;

/**
 * Predefined date format patterns for Persian dates.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class FormatPattern {
    
    private FormatPattern() {
        // Utility class
    }
    
    // ==================== Standard Patterns ====================
    
    /** yyyy/MM/dd - e.g., 1403/10/15 */
    public static final String ISO_DATE = "yyyy/MM/dd";
    
    /** yyyy-MM-dd - e.g., 1403-10-15 */
    public static final String ISO_DATE_DASH = "yyyy-MM-dd";
    
    /** yyyyMMdd - e.g., 14031015 */
    public static final String BASIC_DATE = "yyyyMMdd";
    
    /** yy/MM/dd - e.g., 03/10/15 */
    public static final String SHORT_DATE = "yy/MM/dd";
    
    // ==================== Long Patterns ====================
    
    /** d MMMM yyyy - e.g., 15 دی 1403 */
    public static final String LONG_DATE = "d MMMM yyyy";
    
    /** EEEE d MMMM yyyy - e.g., شنبه 15 دی 1403 */
    public static final String FULL_DATE = "EEEE d MMMM yyyy";
    
    /** d MMMM - e.g., 15 دی */
    public static final String DAY_MONTH = "d MMMM";
    
    /** MMMM yyyy - e.g., دی 1403 */
    public static final String MONTH_YEAR = "MMMM yyyy";
    
    // ==================== Time Patterns ====================
    
    /** HH:mm - e.g., 14:30 */
    public static final String TIME_SHORT = "HH:mm";
    
    /** HH:mm:ss - e.g., 14:30:45 */
    public static final String TIME_LONG = "HH:mm:ss";
    
    /** yyyy/MM/dd HH:mm - e.g., 1403/10/15 14:30 */
    public static final String DATE_TIME = "yyyy/MM/dd HH:mm";
    
    /** yyyy/MM/dd HH:mm:ss - e.g., 1403/10/15 14:30:45 */
    public static final String DATE_TIME_FULL = "yyyy/MM/dd HH:mm:ss";
    
    // ==================== Pattern Elements ====================
    
    /**
     * Pattern element descriptions:
     * 
     * yyyy - 4-digit year (1403)
     * yy   - 2-digit year (03)
     * MMMM - Full month name (فروردین)
     * MMM  - Abbreviated month name (فرو)
     * MM   - 2-digit month (01-12)
     * M    - Month (1-12)
     * dd   - 2-digit day (01-31)
     * d    - Day (1-31)
     * EEEE - Full day name (شنبه)
     * EEE  - Abbreviated day name (ش)
     * HH   - Hour 24h (00-23)
     * hh   - Hour 12h (01-12)
     * mm   - Minutes (00-59)
     * ss   - Seconds (00-59)
     * a    - AM/PM marker
     */
    public static String getPatternHelp() {
        return """
            Pattern Elements:
            yyyy - 4-digit year (1403)
            yy   - 2-digit year (03)
            MMMM - Full month name (فروردین)
            MMM  - Abbreviated month name (فرو)
            MM   - 2-digit month (01-12)
            M    - Month (1-12)
            dd   - 2-digit day (01-31)
            d    - Day (1-31)
            EEEE - Full day name (شنبه)
            EEE  - Abbreviated day name (ش)
            HH   - Hour 24h (00-23)
            mm   - Minutes (00-59)
            ss   - Seconds (00-59)
            """;
    }
}
