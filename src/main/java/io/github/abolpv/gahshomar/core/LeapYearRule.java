package io.github.abolpv.gahshomar.core;

/**
 * Provides leap year calculation rules for different calendars.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class LeapYearRule {
    
    private LeapYearRule() {
        // Utility class
    }
    
    /**
     * Checks if a Persian year is a leap year using the 2820-year cycle.
     * This is the most accurate algorithm for Persian calendar.
     */
    public static boolean isPersianLeapYear(int year) {
        return JalaliAlgorithm.isLeapYear(year);
    }
    
    /**
     * Checks if a Hijri year is a leap year.
     * Uses the 30-year cycle where years 2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29 are leap.
     */
    public static boolean isHijriLeapYear(int year) {
        return HijriAlgorithm.isLeapYear(year);
    }
    
    /**
     * Checks if a Gregorian year is a leap year.
     */
    public static boolean isGregorianLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    
    /**
     * Gets the number of days in a Persian year.
     */
    public static int getPersianYearLength(int year) {
        return isPersianLeapYear(year) ? 366 : 365;
    }
    
    /**
     * Gets the number of days in a Hijri year.
     */
    public static int getHijriYearLength(int year) {
        return isHijriLeapYear(year) ? 355 : 354;
    }
    
    /**
     * Gets the number of days in a Gregorian year.
     */
    public static int getGregorianYearLength(int year) {
        return isGregorianLeapYear(year) ? 366 : 365;
    }
    
    /**
     * Gets leap years in a range of Persian years.
     */
    public static int[] getPersianLeapYearsInRange(int startYear, int endYear) {
        return java.util.stream.IntStream.rangeClosed(startYear, endYear)
            .filter(LeapYearRule::isPersianLeapYear)
            .toArray();
    }
    
    /**
     * Gets the next Persian leap year after the given year.
     */
    public static int getNextPersianLeapYear(int year) {
        int next = year + 1;
        while (!isPersianLeapYear(next)) {
            next++;
        }
        return next;
    }
    
    /**
     * Gets the previous Persian leap year before the given year.
     */
    public static int getPreviousPersianLeapYear(int year) {
        int prev = year - 1;
        while (!isPersianLeapYear(prev)) {
            prev--;
        }
        return prev;
    }
}
