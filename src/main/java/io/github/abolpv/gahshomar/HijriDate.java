package io.github.abolpv.gahshomar;

import io.github.abolpv.gahshomar.core.HijriAlgorithm;
import io.github.abolpv.gahshomar.core.JalaliAlgorithm;
import io.github.abolpv.gahshomar.exception.InvalidDateException;
import io.github.abolpv.gahshomar.format.PersianNumbers;
import io.github.abolpv.gahshomar.temporal.*;

import java.io.Serializable;
import java.time.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * A date in the Hijri (Islamic/Lunar) calendar system.
 * This class is immutable and thread-safe.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class HijriDate implements Comparable<HijriDate>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final int year;
    private final int month;
    private final int day;
    
    private HijriDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
    
    // ==================== Factory Methods ====================
    
    /**
     * Creates a HijriDate from year, month, and day.
     */
    public static HijriDate of(int year, int month, int day) {
        validate(year, month, day);
        return new HijriDate(year, month, day);
    }
    
    /**
     * Creates a HijriDate for today.
     */
    public static HijriDate now() {
        return from(LocalDate.now());
    }
    
    /**
     * Creates a HijriDate for today in the specified timezone.
     */
    public static HijriDate now(ZoneId zone) {
        return from(LocalDate.now(zone));
    }
    
    /**
     * Creates a HijriDate from a Gregorian LocalDate.
     */
    public static HijriDate from(LocalDate gregorianDate) {
        Objects.requireNonNull(gregorianDate, "gregorianDate cannot be null");
        int[] hijri = HijriAlgorithm.gregorianToHijri(
            gregorianDate.getYear(),
            gregorianDate.getMonthValue(),
            gregorianDate.getDayOfMonth()
        );
        return new HijriDate(hijri[0], hijri[1], hijri[2]);
    }
    
    /**
     * Creates a HijriDate from a PersianDate.
     */
    public static HijriDate from(PersianDate persianDate) {
        Objects.requireNonNull(persianDate, "persianDate cannot be null");
        int[] hijri = HijriAlgorithm.persianToHijri(
            persianDate.getYear(),
            persianDate.getMonthValue(),
            persianDate.getDayOfMonth()
        );
        return new HijriDate(hijri[0], hijri[1], hijri[2]);
    }
    
    /**
     * Creates a HijriDate from epoch milliseconds.
     */
    public static HijriDate ofEpochMilli(long epochMilli) {
        return from(LocalDate.ofEpochDay(epochMilli / 86400000L));
    }
    
    /**
     * Parses a date string in the format "yyyy/MM/dd".
     */
    public static HijriDate parse(String text) {
        Objects.requireNonNull(text, "text cannot be null");
        String normalized = PersianNumbers.toWestern(text.trim());
        
        String[] parts;
        if (normalized.contains("/")) {
            parts = normalized.split("/");
        } else if (normalized.contains("-")) {
            parts = normalized.split("-");
        } else {
            throw new InvalidDateException("Cannot parse date: " + text);
        }
        
        if (parts.length != 3) {
            throw new InvalidDateException("Cannot parse date: " + text);
        }
        
        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            return of(year, month, day);
        } catch (NumberFormatException e) {
            throw new InvalidDateException("Cannot parse date: " + text);
        }
    }
    
    /**
     * Tries to parse a date string.
     */
    public static Optional<HijriDate> tryParse(String text) {
        try {
            return Optional.of(parse(text));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    // ==================== Getters ====================
    
    public int getYear() {
        return year;
    }
    
    public int getMonthValue() {
        return month;
    }
    
    public HijriMonth getMonth() {
        return HijriMonth.of(month);
    }
    
    public int getDayOfMonth() {
        return day;
    }
    
    public int getDayOfYear() {
        return HijriAlgorithm.getDayOfYear(year, month, day);
    }
    
    public PersianDayOfWeek getDayOfWeek() {
        int dow = HijriAlgorithm.getDayOfWeek(year, month, day);
        return PersianDayOfWeek.of(dow);
    }
    
    public String getMonthName() {
        return getMonth().getArabicName();
    }
    
    public int lengthOfMonth() {
        return HijriAlgorithm.getDaysInMonth(year, month);
    }
    
    public int lengthOfYear() {
        return HijriAlgorithm.getDaysInYear(year);
    }
    
    public boolean isLeapYear() {
        return HijriAlgorithm.isLeapYear(year);
    }
    
    // ==================== Conversion Methods ====================
    
    /**
     * Converts to Gregorian LocalDate.
     */
    public LocalDate toGregorian() {
        int[] gregorian = HijriAlgorithm.hijriToGregorian(year, month, day);
        return LocalDate.of(gregorian[0], gregorian[1], gregorian[2]);
    }
    
    /**
     * Converts to PersianDate.
     */
    public PersianDate toPersian() {
        int[] persian = HijriAlgorithm.hijriToPersian(year, month, day);
        return PersianDate.of(persian[0], persian[1], persian[2]);
    }
    
    /**
     * Converts to epoch milliseconds.
     */
    public long toEpochMilli() {
        return toGregorian().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    /**
     * Converts to epoch day.
     */
    public long toEpochDay() {
        return toGregorian().toEpochDay();
    }
    
    // ==================== Arithmetic Operations ====================
    
    public HijriDate plusDays(long days) {
        if (days == 0) return this;
        return from(toGregorian().plusDays(days));
    }
    
    public HijriDate plusWeeks(long weeks) {
        return plusDays(weeks * 7);
    }
    
    public HijriDate plusMonths(long months) {
        if (months == 0) return this;
        
        long totalMonths = year * 12L + month - 1 + months;
        int newYear = (int) Math.floorDiv(totalMonths, 12);
        int newMonth = (int) Math.floorMod(totalMonths, 12) + 1;
        int newDay = Math.min(day, HijriAlgorithm.getDaysInMonth(newYear, newMonth));
        
        return of(newYear, newMonth, newDay);
    }
    
    public HijriDate plusYears(long years) {
        if (years == 0) return this;
        
        int newYear = (int) (year + years);
        int newDay = Math.min(day, HijriAlgorithm.getDaysInMonth(newYear, month));
        
        return of(newYear, month, newDay);
    }
    
    public HijriDate minusDays(long days) {
        return plusDays(-days);
    }
    
    public HijriDate minusWeeks(long weeks) {
        return plusWeeks(-weeks);
    }
    
    public HijriDate minusMonths(long months) {
        return plusMonths(-months);
    }
    
    public HijriDate minusYears(long years) {
        return plusYears(-years);
    }
    
    // ==================== Comparison Methods ====================
    
    public boolean isBefore(HijriDate other) {
        return compareTo(other) < 0;
    }
    
    public boolean isAfter(HijriDate other) {
        return compareTo(other) > 0;
    }
    
    public boolean isEqual(HijriDate other) {
        return compareTo(other) == 0;
    }
    
    public long daysUntil(HijriDate other) {
        return toEpochDay() - other.toEpochDay();
    }
    
    // ==================== Special Dates ====================
    
    /**
     * Returns the start of Ramadan for the given Hijri year.
     */
    public static HijriDate startOfRamadan(int year) {
        return of(year, 9, 1);
    }
    
    /**
     * Returns the end of Ramadan (last day) for the given Hijri year.
     */
    public static HijriDate endOfRamadan(int year) {
        return of(year, 9, HijriAlgorithm.getDaysInMonth(year, 9));
    }
    
    /**
     * Returns Eid al-Fitr for the given Hijri year.
     */
    public static HijriDate eidAlFitr(int year) {
        return of(year, 10, 1);
    }
    
    /**
     * Returns Eid al-Adha for the given Hijri year.
     */
    public static HijriDate eidAlAdha(int year) {
        return of(year, 12, 10);
    }
    
    /**
     * Checks if this date is in Ramadan.
     */
    public boolean isRamadan() {
        return month == 9;
    }
    
    /**
     * Checks if this month is a sacred month.
     */
    public boolean isSacredMonth() {
        return getMonth().isSacredMonth();
    }
    
    // ==================== With Methods ====================
    
    public HijriDate withYear(int year) {
        if (this.year == year) return this;
        int newDay = Math.min(day, HijriAlgorithm.getDaysInMonth(year, month));
        return of(year, month, newDay);
    }
    
    public HijriDate withMonth(int month) {
        if (this.month == month) return this;
        int newDay = Math.min(day, HijriAlgorithm.getDaysInMonth(year, month));
        return of(year, month, newDay);
    }
    
    public HijriDate withDayOfMonth(int day) {
        if (this.day == day) return this;
        return of(year, month, day);
    }
    
    public HijriDate atStartOfMonth() {
        return withDayOfMonth(1);
    }
    
    public HijriDate atEndOfMonth() {
        return withDayOfMonth(lengthOfMonth());
    }
    
    public HijriDate atStartOfYear() {
        return of(year, 1, 1);
    }
    
    public HijriDate atEndOfYear() {
        return of(year, 12, HijriAlgorithm.getDaysInMonth(year, 12));
    }
    
    // ==================== Format Methods ====================
    
    public String format() {
        return format("yyyy/MM/dd");
    }
    
    public String format(String pattern) {
        return format(pattern, false);
    }
    
    public String format(String pattern, boolean persianDigits) {
        String result = pattern
            .replace("yyyy", String.format("%04d", year))
            .replace("yy", String.format("%02d", year % 100))
            .replace("MMMM", getMonthName())
            .replace("MMM", getMonthName().substring(0, Math.min(3, getMonthName().length())))
            .replace("MM", String.format("%02d", month))
            .replace("M", String.valueOf(month))
            .replace("dd", String.format("%02d", day))
            .replace("d", String.valueOf(day));
        
        return persianDigits ? PersianNumbers.toPersian(result) : result;
    }
    
    // ==================== Validation ====================
    
    public static boolean isValid(int year, int month, int day) {
        if (year < 1 || year > 9999) return false;
        if (month < 1 || month > 12) return false;
        if (day < 1) return false;
        return day <= HijriAlgorithm.getDaysInMonth(year, month);
    }
    
    public static boolean isLeapYear(int year) {
        return HijriAlgorithm.isLeapYear(year);
    }
    
    private static void validate(int year, int month, int day) {
        if (!isValid(year, month, day)) {
            throw new InvalidDateException(year, month, day);
        }
    }
    
    // ==================== Object Methods ====================
    
    @Override
    public int compareTo(HijriDate other) {
        int cmp = Integer.compare(year, other.year);
        if (cmp != 0) return cmp;
        cmp = Integer.compare(month, other.month);
        if (cmp != 0) return cmp;
        return Integer.compare(day, other.day);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HijriDate other = (HijriDate) obj;
        return year == other.year && month == other.month && day == other.day;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }
    
    @Override
    public String toString() {
        return format();
    }
}
