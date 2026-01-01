package io.github.abolpv.gahshomar.util;

import io.github.abolpv.gahshomar.exception.InvalidDateException;

/**
 * Utility class for precondition checks.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class Preconditions {
    
    private Preconditions() {
        // Utility class
    }
    
    /**
     * Ensures expression is true.
     */
    public static void checkArgument(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Ensures value is not null.
     */
    public static <T> T checkNotNull(T reference, String parameterName) {
        if (reference == null) {
            throw new NullPointerException(parameterName + " cannot be null");
        }
        return reference;
    }
    
    /**
     * Validates Persian date components.
     */
    public static void checkPersianDate(int year, int month, int day) {
        if (year < 1 || year > 9999) {
            throw new InvalidDateException("Year must be between 1 and 9999: " + year);
        }
        if (month < 1 || month > 12) {
            throw new InvalidDateException("Month must be between 1 and 12: " + month);
        }
        
        int maxDay = getPersianMonthLength(year, month);
        if (day < 1 || day > maxDay) {
            throw new InvalidDateException(year, month, day);
        }
    }
    
    /**
     * Validates Hijri date components.
     */
    public static void checkHijriDate(int year, int month, int day) {
        if (year < 1 || year > 9999) {
            throw new InvalidDateException("Year must be between 1 and 9999: " + year);
        }
        if (month < 1 || month > 12) {
            throw new InvalidDateException("Month must be between 1 and 12: " + month);
        }
        
        int maxDay = getHijriMonthLength(year, month);
        if (day < 1 || day > maxDay) {
            throw new InvalidDateException(year, month, day);
        }
    }
    
    /**
     * Gets the length of a Persian month.
     */
    public static int getPersianMonthLength(int year, int month) {
        if (month <= 6) {
            return 31;
        } else if (month <= 11) {
            return 30;
        } else {
            return isPersianLeapYear(year) ? 30 : 29;
        }
    }
    
    /**
     * Gets the length of a Hijri month.
     */
    public static int getHijriMonthLength(int year, int month) {
        // Odd months have 30 days, even months have 29 days
        // Except month 12 in leap years has 30 days
        if (month % 2 == 1) {
            return 30;
        } else if (month == 12 && isHijriLeapYear(year)) {
            return 30;
        } else {
            return 29;
        }
    }
    
    /**
     * Checks if a Persian year is a leap year.
     * Uses the 2820-year cycle algorithm.
     */
    public static boolean isPersianLeapYear(int year) {
        // Leap years in a 2820-year cycle
        // This is an approximation using the 33-year sub-cycle
        int[] leapYears = {1, 5, 9, 13, 17, 22, 26, 30};
        int mod = year % 33;
        if (mod == 0) mod = 33;
        
        for (int leap : leapYears) {
            if (mod == leap) return true;
        }
        return false;
    }
    
    /**
     * Checks if a Hijri year is a leap year.
     * Uses the 30-year cycle: years 2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29 are leap.
     */
    public static boolean isHijriLeapYear(int year) {
        int[] leapYears = {2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29};
        int mod = year % 30;
        if (mod == 0) mod = 30;
        
        for (int leap : leapYears) {
            if (mod == leap) return true;
        }
        return false;
    }
}
