package io.github.abolpv.gahshomar.core;

/**
 * Core algorithm for Hijri (Islamic/Lunar) calendar conversions.
 * Uses the tabular Islamic calendar algorithm.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class HijriAlgorithm {
    
    // Hijri calendar epoch in Julian Day Number (July 16, 622 CE)
    private static final int HIJRI_EPOCH = 1948439;
    
    // Average length of a Hijri month
    private static final double HIJRI_MONTH_LENGTH = 29.530588853;
    
    private HijriAlgorithm() {
        // Utility class
    }
    
    /**
     * Converts Hijri date to Julian Day Number.
     */
    public static long hijriToJulianDay(int year, int month, int day) {
        return day
            + (int) Math.ceil(29.5 * (month - 1) + 0.99)
            + (year - 1) * 354
            + (int) Math.floor((3 + 11 * year) / 30.0)
            + HIJRI_EPOCH - 385;
    }
    
    /**
     * Converts Julian Day Number to Hijri date.
     * Returns array of [year, month, day].
     */
    public static int[] julianDayToHijri(long julianDay) {
        long l = julianDay - HIJRI_EPOCH + 10632;
        long n = (long) Math.floor((l - 1) / 10631.0);
        l = l - 10631 * n + 354;
        
        long j = ((long) Math.floor((10985 - l) / 5316.0)) 
               * ((long) Math.floor((50 * l) / 17719.0))
               + ((long) Math.floor(l / 5670.0)) 
               * ((long) Math.floor((43 * l) / 15238.0));
        
        l = l - ((long) Math.floor((30 - j) / 15.0)) 
              * ((long) Math.floor((17719 * j) / 50.0))
              - ((long) Math.floor(j / 16.0)) 
              * ((long) Math.floor((15238 * j) / 43.0)) + 29;
        
        int month = (int) Math.floor((24 * l) / 709.0);
        int day = (int) (l - Math.floor((709 * month) / 24.0));
        int year = (int) (30 * n + j - 30);
        
        return new int[]{year, month, day};
    }
    
    /**
     * Converts Hijri date to Gregorian date.
     * Returns array of [year, month, day].
     */
    public static int[] hijriToGregorian(int year, int month, int day) {
        long jd = hijriToJulianDay(year, month, day);
        return JalaliAlgorithm.julianDayToGregorian(jd);
    }
    
    /**
     * Converts Gregorian date to Hijri date.
     * Returns array of [year, month, day].
     */
    public static int[] gregorianToHijri(int year, int month, int day) {
        long jd = JalaliAlgorithm.gregorianToJulianDay(year, month, day);
        return julianDayToHijri(jd);
    }
    
    /**
     * Converts Hijri date to Persian date.
     * Returns array of [year, month, day].
     */
    public static int[] hijriToPersian(int year, int month, int day) {
        long jd = hijriToJulianDay(year, month, day);
        return JalaliAlgorithm.julianDayToPersian(jd);
    }
    
    /**
     * Converts Persian date to Hijri date.
     * Returns array of [year, month, day].
     */
    public static int[] persianToHijri(int pYear, int pMonth, int pDay) {
        long jd = JalaliAlgorithm.persianToJulianDay(pYear, pMonth, pDay);
        return julianDayToHijri(jd);
    }
    
    /**
     * Checks if a Hijri year is a leap year.
     * Uses the 30-year cycle: years 2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29 are leap.
     */
    public static boolean isLeapYear(int year) {
        return (11 * year + 14) % 30 < 11;
    }
    
    /**
     * Gets the number of days in a Hijri year.
     */
    public static int getDaysInYear(int year) {
        return isLeapYear(year) ? 355 : 354;
    }
    
    /**
     * Gets the number of days in a Hijri month.
     */
    public static int getDaysInMonth(int year, int month) {
        // Odd months have 30 days, even months have 29 days
        // Except month 12 in leap years has 30 days
        if (month % 2 == 1) {
            return 30;
        } else if (month == 12 && isLeapYear(year)) {
            return 30;
        } else {
            return 29;
        }
    }
    
    /**
     * Calculates day of year (1-355).
     */
    public static int getDayOfYear(int year, int month, int day) {
        int dayOfYear = day;
        for (int m = 1; m < month; m++) {
            dayOfYear += getDaysInMonth(year, m);
        }
        return dayOfYear;
    }
    
    /**
     * Gets the day of week (1=Saturday, 7=Friday).
     */
    public static int getDayOfWeek(int year, int month, int day) {
        long jd = hijriToJulianDay(year, month, day);
        int dow = (int) ((jd + 2) % 7);  // 0=Friday
        return dow == 0 ? 7 : dow;  // Convert to 1-7 where 1=Saturday
    }
}
