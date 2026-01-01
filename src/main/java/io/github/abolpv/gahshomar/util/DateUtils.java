package io.github.abolpv.gahshomar.util;

import io.github.abolpv.gahshomar.PersianDate;
import io.github.abolpv.gahshomar.HijriDate;
import io.github.abolpv.gahshomar.core.JalaliAlgorithm;
import io.github.abolpv.gahshomar.core.HijriAlgorithm;
import io.github.abolpv.gahshomar.temporal.PersianMonth;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for date operations.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class DateUtils {
    
    private DateUtils() {
        // Utility class
    }
    
    // ==================== Validation ====================
    
    /**
     * Checks if a Persian date is valid.
     */
    public static boolean isValidPersianDate(int year, int month, int day) {
        return PersianDate.isValid(year, month, day);
    }
    
    /**
     * Checks if a Hijri date is valid.
     */
    public static boolean isValidHijriDate(int year, int month, int day) {
        return HijriDate.isValid(year, month, day);
    }
    
    // ==================== Month Operations ====================
    
    /**
     * Gets the number of days in a Persian month.
     */
    public static int getPersianMonthLength(int year, int month) {
        return JalaliAlgorithm.getDaysInMonth(year, month);
    }
    
    /**
     * Gets the number of days in a Hijri month.
     */
    public static int getHijriMonthLength(int year, int month) {
        return HijriAlgorithm.getDaysInMonth(year, month);
    }
    
    /**
     * Gets the Persian month name.
     */
    public static String getPersianMonthName(int month) {
        return PersianMonth.of(month).getPersianName();
    }
    
    // ==================== Year Operations ====================
    
    /**
     * Gets the number of days in a Persian year.
     */
    public static int getPersianYearLength(int year) {
        return JalaliAlgorithm.getDaysInYear(year);
    }
    
    /**
     * Gets the number of days in a Hijri year.
     */
    public static int getHijriYearLength(int year) {
        return HijriAlgorithm.getDaysInYear(year);
    }
    
    /**
     * Gets the first day of a Persian year.
     */
    public static PersianDate getFirstDayOfYear(int year) {
        return PersianDate.of(year, 1, 1);
    }
    
    /**
     * Gets the last day of a Persian year.
     */
    public static PersianDate getLastDayOfYear(int year) {
        return PersianDate.of(year, 12, PersianDate.isLeapYear(year) ? 30 : 29);
    }
    
    // ==================== Day of Year Operations ====================
    
    /**
     * Gets the day of year for a Persian date.
     */
    public static int getDayOfYear(int year, int month, int day) {
        return JalaliAlgorithm.getDayOfYear(year, month, day);
    }
    
    /**
     * Gets a Persian date from day of year.
     */
    public static PersianDate fromDayOfYear(int year, int dayOfYear) {
        int month = 1;
        int remaining = dayOfYear;
        
        while (remaining > JalaliAlgorithm.getDaysInMonth(year, month)) {
            remaining -= JalaliAlgorithm.getDaysInMonth(year, month);
            month++;
        }
        
        return PersianDate.of(year, month, remaining);
    }
    
    // ==================== Comparison ====================
    
    /**
     * Returns the earlier of two Persian dates.
     */
    public static PersianDate min(PersianDate a, PersianDate b) {
        return a.isBefore(b) ? a : b;
    }
    
    /**
     * Returns the later of two Persian dates.
     */
    public static PersianDate max(PersianDate a, PersianDate b) {
        return a.isAfter(b) ? a : b;
    }
    
    /**
     * Clamps a date to be within a range.
     */
    public static PersianDate clamp(PersianDate date, PersianDate min, PersianDate max) {
        if (date.isBefore(min)) return min;
        if (date.isAfter(max)) return max;
        return date;
    }
    
    // ==================== List Operations ====================
    
    /**
     * Gets all dates in a month as a list.
     */
    public static List<PersianDate> getDatesInMonth(int year, int month) {
        List<PersianDate> dates = new ArrayList<>();
        int days = getPersianMonthLength(year, month);
        for (int d = 1; d <= days; d++) {
            dates.add(PersianDate.of(year, month, d));
        }
        return dates;
    }
    
    /**
     * Gets all Fridays in a month.
     */
    public static List<PersianDate> getFridaysInMonth(int year, int month) {
        return getDatesInMonth(year, month).stream()
            .filter(PersianDate::isWeekend)
            .toList();
    }
    
    /**
     * Gets all workdays in a month.
     */
    public static List<PersianDate> getWorkdaysInMonth(int year, int month) {
        return getDatesInMonth(year, month).stream()
            .filter(PersianDate::isWorkday)
            .toList();
    }
    
    // ==================== Conversion Shortcuts ====================
    
    /**
     * Converts today's date to Persian.
     */
    public static PersianDate today() {
        return PersianDate.now();
    }
    
    /**
     * Converts a Gregorian date string to Persian.
     */
    public static PersianDate fromGregorian(String isoDate) {
        return PersianDate.from(LocalDate.parse(isoDate));
    }
    
    /**
     * Converts Persian date to Gregorian string.
     */
    public static String toGregorianString(PersianDate date) {
        return date.toGregorian().toString();
    }
    
    // ==================== Week Operations ====================
    
    /**
     * Gets the week number of the year (1-53).
     */
    public static int getWeekOfYear(PersianDate date) {
        int dayOfYear = date.getDayOfYear();
        // Persian week starts on Saturday
        PersianDate firstDay = PersianDate.of(date.getYear(), 1, 1);
        int firstDayOfWeek = firstDay.getDayOfWeek().getValue();
        
        return (dayOfYear + firstDayOfWeek - 2) / 7 + 1;
    }
    
    /**
     * Gets all dates in a week containing the given date.
     */
    public static List<PersianDate> getWeekDates(PersianDate date) {
        List<PersianDate> dates = new ArrayList<>();
        PersianDate start = date.atStartOfWeek();
        for (int i = 0; i < 7; i++) {
            dates.add(start.plusDays(i));
        }
        return dates;
    }
    
    // ==================== Quarter Operations ====================
    
    /**
     * Gets the first day of a quarter.
     */
    public static PersianDate getFirstDayOfQuarter(int year, int quarter) {
        int month = (quarter - 1) * 3 + 1;
        return PersianDate.of(year, month, 1);
    }
    
    /**
     * Gets the last day of a quarter.
     */
    public static PersianDate getLastDayOfQuarter(int year, int quarter) {
        int month = quarter * 3;
        return PersianDate.of(year, month, JalaliAlgorithm.getDaysInMonth(year, month));
    }
}
