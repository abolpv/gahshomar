package io.github.abolpv.gahshomar.core;

import java.time.LocalDate;

/**
 * Unified date converter between Persian, Hijri, and Gregorian calendars.
 * Provides a single entry point for all calendar conversions.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class DateConverter {
    
    private DateConverter() {
        // Utility class
    }
    
    // ==================== Persian to Others ====================
    
    /**
     * Converts Persian date to Gregorian.
     */
    public static LocalDate persianToGregorian(int year, int month, int day) {
        int[] g = JalaliAlgorithm.persianToGregorian(year, month, day);
        return LocalDate.of(g[0], g[1], g[2]);
    }
    
    /**
     * Converts Persian date to Hijri.
     * Returns array of [year, month, day].
     */
    public static int[] persianToHijri(int year, int month, int day) {
        return HijriAlgorithm.persianToHijri(year, month, day);
    }
    
    // ==================== Gregorian to Others ====================
    
    /**
     * Converts Gregorian date to Persian.
     * Returns array of [year, month, day].
     */
    public static int[] gregorianToPersian(int year, int month, int day) {
        return JalaliAlgorithm.gregorianToPersian(year, month, day);
    }
    
    /**
     * Converts Gregorian date to Persian.
     */
    public static int[] gregorianToPersian(LocalDate date) {
        return gregorianToPersian(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }
    
    /**
     * Converts Gregorian date to Hijri.
     * Returns array of [year, month, day].
     */
    public static int[] gregorianToHijri(int year, int month, int day) {
        return HijriAlgorithm.gregorianToHijri(year, month, day);
    }
    
    /**
     * Converts Gregorian date to Hijri.
     */
    public static int[] gregorianToHijri(LocalDate date) {
        return gregorianToHijri(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }
    
    // ==================== Hijri to Others ====================
    
    /**
     * Converts Hijri date to Gregorian.
     */
    public static LocalDate hijriToGregorian(int year, int month, int day) {
        int[] g = HijriAlgorithm.hijriToGregorian(year, month, day);
        return LocalDate.of(g[0], g[1], g[2]);
    }
    
    /**
     * Converts Hijri date to Persian.
     * Returns array of [year, month, day].
     */
    public static int[] hijriToPersian(int year, int month, int day) {
        return HijriAlgorithm.hijriToPersian(year, month, day);
    }
    
    // ==================== Julian Day Conversions ====================
    
    /**
     * Converts Persian date to Julian Day Number.
     */
    public static long persianToJulianDay(int year, int month, int day) {
        return JalaliAlgorithm.persianToJulianDay(year, month, day);
    }
    
    /**
     * Converts Julian Day Number to Persian date.
     */
    public static int[] julianDayToPersian(long julianDay) {
        return JalaliAlgorithm.julianDayToPersian(julianDay);
    }
    
    /**
     * Converts Gregorian date to Julian Day Number.
     */
    public static long gregorianToJulianDay(int year, int month, int day) {
        return JalaliAlgorithm.gregorianToJulianDay(year, month, day);
    }
    
    /**
     * Converts Julian Day Number to Gregorian date.
     */
    public static int[] julianDayToGregorian(long julianDay) {
        return JalaliAlgorithm.julianDayToGregorian(julianDay);
    }
    
    /**
     * Converts Hijri date to Julian Day Number.
     */
    public static long hijriToJulianDay(int year, int month, int day) {
        return HijriAlgorithm.hijriToJulianDay(year, month, day);
    }
    
    /**
     * Converts Julian Day Number to Hijri date.
     */
    public static int[] julianDayToHijri(long julianDay) {
        return HijriAlgorithm.julianDayToHijri(julianDay);
    }
    
    // ==================== Utility Methods ====================
    
    /**
     * Calculates days between two Persian dates.
     */
    public static long daysBetweenPersian(int y1, int m1, int d1, int y2, int m2, int d2) {
        long jd1 = persianToJulianDay(y1, m1, d1);
        long jd2 = persianToJulianDay(y2, m2, d2);
        return jd2 - jd1;
    }
    
    /**
     * Adds days to a Persian date.
     * Returns array of [year, month, day].
     */
    public static int[] addDaysToPersian(int year, int month, int day, long days) {
        long jd = persianToJulianDay(year, month, day) + days;
        return julianDayToPersian(jd);
    }
    
    /**
     * Gets the day of week for a Persian date (1=Saturday, 7=Friday).
     */
    public static int getPersianDayOfWeek(int year, int month, int day) {
        return JalaliAlgorithm.getDayOfWeek(year, month, day);
    }
    
    /**
     * Gets the day of week for a Hijri date (1=Saturday, 7=Friday).
     */
    public static int getHijriDayOfWeek(int year, int month, int day) {
        return HijriAlgorithm.getDayOfWeek(year, month, day);
    }
}
