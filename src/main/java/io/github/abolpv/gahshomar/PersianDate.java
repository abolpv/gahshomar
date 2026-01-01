package io.github.abolpv.gahshomar;

import io.github.abolpv.gahshomar.core.JalaliAlgorithm;
import io.github.abolpv.gahshomar.core.HijriAlgorithm;
import io.github.abolpv.gahshomar.exception.InvalidDateException;
import io.github.abolpv.gahshomar.format.PersianNumbers;
import io.github.abolpv.gahshomar.temporal.*;

import java.io.Serializable;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

/**
 * A date in the Persian (Jalali/Shamsi) calendar system.
 * This class is immutable and thread-safe.
 *
 * <p>Example usage:</p>
 * <pre>
 * PersianDate date = PersianDate.of(1403, 10, 15);
 * PersianDate today = PersianDate.now();
 * LocalDate gregorian = date.toGregorian();
 * </pre>
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class PersianDate implements Comparable<PersianDate>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final int year;
    private final int month;
    private final int day;
    
    // Cached values
    private transient Integer dayOfYear;
    private transient Integer dayOfWeek;
    
    /**
     * Private constructor. Use factory methods instead.
     */
    private PersianDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
    
    // ==================== Factory Methods ====================
    
    /**
     * Creates a PersianDate from year, month, and day.
     *
     * @param year  the year (1-9999)
     * @param month the month (1-12)
     * @param day   the day of month (1-31)
     * @return the Persian date
     * @throws InvalidDateException if the date is invalid
     */
    public static PersianDate of(int year, int month, int day) {
        validate(year, month, day);
        return new PersianDate(year, month, day);
    }
    
    /**
     * Creates a PersianDate for today in the default timezone.
     */
    public static PersianDate now() {
        return from(LocalDate.now());
    }
    
    /**
     * Creates a PersianDate for today in the specified timezone.
     */
    public static PersianDate now(ZoneId zone) {
        return from(LocalDate.now(zone));
    }
    
    /**
     * Creates a PersianDate from a Gregorian LocalDate.
     */
    public static PersianDate from(LocalDate gregorianDate) {
        Objects.requireNonNull(gregorianDate, "gregorianDate cannot be null");
        int[] persian = JalaliAlgorithm.gregorianToPersian(
            gregorianDate.getYear(),
            gregorianDate.getMonthValue(),
            gregorianDate.getDayOfMonth()
        );
        return new PersianDate(persian[0], persian[1], persian[2]);
    }
    
    /**
     * Creates a PersianDate from an Instant.
     */
    public static PersianDate from(Instant instant) {
        return from(instant, ZoneId.systemDefault());
    }
    
    /**
     * Creates a PersianDate from an Instant in the specified timezone.
     */
    public static PersianDate from(Instant instant, ZoneId zone) {
        Objects.requireNonNull(instant, "instant cannot be null");
        Objects.requireNonNull(zone, "zone cannot be null");
        return from(instant.atZone(zone).toLocalDate());
    }
    
    /**
     * Creates a PersianDate from epoch milliseconds.
     */
    public static PersianDate ofEpochMilli(long epochMilli) {
        return from(Instant.ofEpochMilli(epochMilli));
    }
    
    /**
     * Creates a PersianDate from epoch days.
     */
    public static PersianDate ofEpochDay(long epochDay) {
        return from(LocalDate.ofEpochDay(epochDay));
    }
    
    /**
     * Parses a date string in the format "yyyy/MM/dd" or "yyyy-MM-dd".
     * Also accepts Persian digits.
     */
    public static PersianDate parse(String text) {
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
     * Tries to parse a date string, returning Optional.empty() on failure.
     */
    public static Optional<PersianDate> tryParse(String text) {
        try {
            return Optional.of(parse(text));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    // ==================== Getters ====================
    
    /**
     * Gets the year.
     */
    public int getYear() {
        return year;
    }
    
    /**
     * Gets the month (1-12).
     */
    public int getMonthValue() {
        return month;
    }
    
    /**
     * Gets the month as PersianMonth enum.
     */
    public PersianMonth getMonth() {
        return PersianMonth.of(month);
    }
    
    /**
     * Gets the day of month (1-31).
     */
    public int getDayOfMonth() {
        return day;
    }
    
    /**
     * Gets the day of year (1-366).
     */
    public int getDayOfYear() {
        if (dayOfYear == null) {
            dayOfYear = JalaliAlgorithm.getDayOfYear(year, month, day);
        }
        return dayOfYear;
    }
    
    /**
     * Gets the day of week as PersianDayOfWeek.
     */
    public PersianDayOfWeek getDayOfWeek() {
        if (dayOfWeek == null) {
            dayOfWeek = JalaliAlgorithm.getDayOfWeek(year, month, day);
        }
        return PersianDayOfWeek.of(dayOfWeek);
    }
    
    /**
     * Gets the day of week as ISO DayOfWeek.
     */
    public DayOfWeek getIsoDayOfWeek() {
        return getDayOfWeek().toIsoDayOfWeek();
    }
    
    /**
     * Gets the Persian name of the month.
     */
    public String getMonthName() {
        return getMonth().getPersianName();
    }
    
    /**
     * Gets the Persian name of the day of week.
     */
    public String getDayOfWeekName() {
        return getDayOfWeek().getPersianName();
    }
    
    /**
     * Gets the quarter of the year (1-4).
     */
    public int getQuarter() {
        return getMonth().getQuarter();
    }
    
    /**
     * Gets the season.
     */
    public Season getSeason() {
        return getMonth().getSeason();
    }
    
    /**
     * Gets the length of this month.
     */
    public int lengthOfMonth() {
        return JalaliAlgorithm.getDaysInMonth(year, month);
    }
    
    /**
     * Gets the length of this year.
     */
    public int lengthOfYear() {
        return JalaliAlgorithm.getDaysInYear(year);
    }
    
    /**
     * Checks if this year is a leap year.
     */
    public boolean isLeapYear() {
        return JalaliAlgorithm.isLeapYear(year);
    }
    
    // ==================== Conversion Methods ====================
    
    /**
     * Converts to Gregorian LocalDate.
     */
    public LocalDate toGregorian() {
        int[] gregorian = JalaliAlgorithm.persianToGregorian(year, month, day);
        return LocalDate.of(gregorian[0], gregorian[1], gregorian[2]);
    }
    
    /**
     * Converts to HijriDate.
     */
    public HijriDate toHijri() {
        int[] hijri = HijriAlgorithm.persianToHijri(year, month, day);
        return HijriDate.of(hijri[0], hijri[1], hijri[2]);
    }
    
    /**
     * Converts to epoch milliseconds (at start of day in default timezone).
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
    
    /**
     * Converts to Instant (at start of day in default timezone).
     */
    public Instant toInstant() {
        return toGregorian().atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
    
    /**
     * Converts to Instant in the specified timezone.
     */
    public Instant toInstant(ZoneId zone) {
        return toGregorian().atStartOfDay(zone).toInstant();
    }
    
    // ==================== Arithmetic Operations ====================
    
    /**
     * Returns a copy with the specified number of days added.
     */
    public PersianDate plusDays(long days) {
        if (days == 0) return this;
        return from(toGregorian().plusDays(days));
    }
    
    /**
     * Returns a copy with the specified number of weeks added.
     */
    public PersianDate plusWeeks(long weeks) {
        return plusDays(weeks * 7);
    }
    
    /**
     * Returns a copy with the specified number of months added.
     */
    public PersianDate plusMonths(long months) {
        if (months == 0) return this;
        
        long totalMonths = year * 12L + month - 1 + months;
        int newYear = (int) Math.floorDiv(totalMonths, 12);
        int newMonth = (int) Math.floorMod(totalMonths, 12) + 1;
        int newDay = Math.min(day, JalaliAlgorithm.getDaysInMonth(newYear, newMonth));
        
        return of(newYear, newMonth, newDay);
    }
    
    /**
     * Returns a copy with the specified number of years added.
     */
    public PersianDate plusYears(long years) {
        if (years == 0) return this;
        
        int newYear = (int) (year + years);
        int newDay = day;
        
        // Handle Feb 30 -> Feb 29 for non-leap years
        if (month == 12 && day == 30 && !JalaliAlgorithm.isLeapYear(newYear)) {
            newDay = 29;
        }
        
        return of(newYear, month, newDay);
    }
    
    /**
     * Returns a copy with the specified number of days subtracted.
     */
    public PersianDate minusDays(long days) {
        return plusDays(-days);
    }
    
    /**
     * Returns a copy with the specified number of weeks subtracted.
     */
    public PersianDate minusWeeks(long weeks) {
        return plusWeeks(-weeks);
    }
    
    /**
     * Returns a copy with the specified number of months subtracted.
     */
    public PersianDate minusMonths(long months) {
        return plusMonths(-months);
    }
    
    /**
     * Returns a copy with the specified number of years subtracted.
     */
    public PersianDate minusYears(long years) {
        return plusYears(-years);
    }
    
    // ==================== Comparison Methods ====================
    
    /**
     * Checks if this date is before the specified date.
     */
    public boolean isBefore(PersianDate other) {
        return compareTo(other) < 0;
    }
    
    /**
     * Checks if this date is after the specified date.
     */
    public boolean isAfter(PersianDate other) {
        return compareTo(other) > 0;
    }
    
    /**
     * Checks if this date equals the specified date.
     */
    public boolean isEqual(PersianDate other) {
        return compareTo(other) == 0;
    }
    
    /**
     * Checks if this date is between two dates (inclusive).
     */
    public boolean isBetween(PersianDate start, PersianDate end) {
        return !isBefore(start) && !isAfter(end);
    }
    
    /**
     * Calculates the number of days until another date.
     */
    public long daysUntil(PersianDate other) {
        return toEpochDay() - other.toEpochDay();
    }
    
    /**
     * Calculates the number of months until another date.
     */
    public long monthsUntil(PersianDate other) {
        long months = (other.year - year) * 12L + (other.month - month);
        if (other.day < day) {
            months--;
        }
        return months;
    }
    
    /**
     * Calculates the number of years until another date.
     */
    public long yearsUntil(PersianDate other) {
        return monthsUntil(other) / 12;
    }
    
    /**
     * Calculates the age from this date (birthday) to today.
     */
    public Age getAge() {
        return getAge(PersianDate.now());
    }
    
    /**
     * Calculates the age from this date to the specified date.
     */
    public Age getAge(PersianDate asOf) {
        int years = asOf.year - this.year;
        int months = asOf.month - this.month;
        int days = asOf.day - this.day;
        
        if (days < 0) {
            months--;
            days += JalaliAlgorithm.getDaysInMonth(asOf.year, asOf.month == 1 ? 12 : asOf.month - 1);
        }
        
        if (months < 0) {
            years--;
            months += 12;
        }
        
        return new Age(Math.max(0, years), Math.max(0, months), Math.max(0, days));
    }
    
    /**
     * Gets the age in years only.
     */
    public int getAgeInYears() {
        return getAge().getYears();
    }
    
    /**
     * Checks if today is this person's birthday.
     */
    public boolean isBirthdayToday() {
        PersianDate today = PersianDate.now();
        return this.month == today.month && this.day == today.day;
    }
    
    /**
     * Gets the next birthday after today.
     */
    public PersianDate nextBirthday() {
        PersianDate today = PersianDate.now();
        PersianDate thisYearBirthday = PersianDate.of(today.year, this.month, 
            Math.min(this.day, JalaliAlgorithm.getDaysInMonth(today.year, this.month)));
        
        if (thisYearBirthday.isAfter(today)) {
            return thisYearBirthday;
        } else {
            int nextYear = today.year + 1;
            return PersianDate.of(nextYear, this.month,
                Math.min(this.day, JalaliAlgorithm.getDaysInMonth(nextYear, this.month)));
        }
    }
    
    /**
     * Gets days until next birthday.
     */
    public long daysUntilNextBirthday() {
        return Math.abs(nextBirthday().daysUntil(PersianDate.now()));
    }
    
    // ==================== With Methods ====================
    
    /**
     * Returns a copy with the year changed.
     */
    public PersianDate withYear(int year) {
        if (this.year == year) return this;
        int newDay = Math.min(day, JalaliAlgorithm.getDaysInMonth(year, month));
        return of(year, month, newDay);
    }
    
    /**
     * Returns a copy with the month changed.
     */
    public PersianDate withMonth(int month) {
        if (this.month == month) return this;
        int newDay = Math.min(day, JalaliAlgorithm.getDaysInMonth(year, month));
        return of(year, month, newDay);
    }
    
    /**
     * Returns a copy with the day of month changed.
     */
    public PersianDate withDayOfMonth(int day) {
        if (this.day == day) return this;
        return of(year, month, day);
    }
    
    /**
     * Returns the first day of this month.
     */
    public PersianDate atStartOfMonth() {
        return withDayOfMonth(1);
    }
    
    /**
     * Returns the last day of this month.
     */
    public PersianDate atEndOfMonth() {
        return withDayOfMonth(lengthOfMonth());
    }
    
    /**
     * Returns the first day of this year.
     */
    public PersianDate atStartOfYear() {
        return of(year, 1, 1);
    }
    
    /**
     * Returns the last day of this year.
     */
    public PersianDate atEndOfYear() {
        return of(year, 12, isLeapYear() ? 30 : 29);
    }
    
    /**
     * Returns the start of this week (Saturday).
     */
    public PersianDate atStartOfWeek() {
        int currentDay = getDayOfWeek().getValue();
        return minusDays(currentDay - 1);
    }
    
    /**
     * Returns the end of this week (Friday).
     */
    public PersianDate atEndOfWeek() {
        int currentDay = getDayOfWeek().getValue();
        return plusDays(7 - currentDay);
    }
    
    /**
     * Returns the start of this quarter.
     */
    public PersianDate atStartOfQuarter() {
        int quarterMonth = ((month - 1) / 3) * 3 + 1;
        return of(year, quarterMonth, 1);
    }
    
    /**
     * Returns the end of this quarter.
     */
    public PersianDate atEndOfQuarter() {
        int quarterMonth = ((month - 1) / 3 + 1) * 3;
        return of(year, quarterMonth, JalaliAlgorithm.getDaysInMonth(year, quarterMonth));
    }
    
    /**
     * Returns the Nowruz (first day) of the specified year.
     */
    public static PersianDate nowruz(int year) {
        return of(year, 1, 1);
    }
    
    /**
     * Returns the next Nowruz after this date.
     */
    public PersianDate nextNowruz() {
        if (month == 1 && day == 1) {
            return of(year + 1, 1, 1);
        }
        return of(year + 1, 1, 1);
    }
    
    // ==================== Stream Methods ====================
    
    /**
     * Returns a stream of dates from this date until the end date (exclusive).
     */
    public Stream<PersianDate> datesUntil(PersianDate endExclusive) {
        long days = Math.abs(endExclusive.toEpochDay() - toEpochDay());
        return Stream.iterate(this, d -> d.plusDays(1)).limit(days);
    }
    
    /**
     * Returns a stream of all dates in this month.
     */
    public Stream<PersianDate> datesOfMonth() {
        return Stream.iterate(atStartOfMonth(), d -> d.plusDays(1))
                     .limit(lengthOfMonth());
    }
    
    /**
     * Returns a stream of all dates in this year.
     */
    public Stream<PersianDate> datesOfYear() {
        return Stream.iterate(atStartOfYear(), d -> d.plusDays(1))
                     .limit(lengthOfYear());
    }
    
    // ==================== Workday Methods ====================
    
    /**
     * Checks if this is a workday (not Friday).
     */
    public boolean isWorkday() {
        return getDayOfWeek().isWorkday();
    }
    
    /**
     * Checks if this is a weekend (Friday).
     */
    public boolean isWeekend() {
        return getDayOfWeek().isWeekend();
    }
    
    /**
     * Returns the next workday.
     */
    public PersianDate nextWorkday() {
        PersianDate next = plusDays(1);
        while (!next.isWorkday()) {
            next = next.plusDays(1);
        }
        return next;
    }
    
    /**
     * Returns the previous workday.
     */
    public PersianDate previousWorkday() {
        PersianDate prev = minusDays(1);
        while (!prev.isWorkday()) {
            prev = prev.minusDays(1);
        }
        return prev;
    }
    
    /**
     * Counts workdays until another date.
     */
    public long workdaysUntil(PersianDate other) {
        return datesUntil(other).filter(PersianDate::isWorkday).count();
    }
    
    // ==================== Format Methods ====================
    
    /**
     * Formats the date using the default pattern (yyyy/MM/dd).
     */
    public String format() {
        return format("yyyy/MM/dd");
    }
    
    /**
     * Formats the date using the specified pattern.
     * Supported patterns: yyyy, yy, MM, M, dd, d, EEEE, EEE, MMMM, MMM
     */
    public String format(String pattern) {
        return format(pattern, false);
    }
    
    /**
     * Formats the date with optional Persian digits.
     */
    public String format(String pattern, boolean persianDigits) {
        String result = pattern
            .replace("yyyy", String.format("%04d", year))
            .replace("yy", String.format("%02d", year % 100))
            .replace("MMMM", getMonthName())
            .replace("MMM", getMonthName().substring(0, Math.min(3, getMonthName().length())))
            .replace("MM", String.format("%02d", month))
            .replace("M", String.valueOf(month))
            .replace("EEEE", getDayOfWeekName())
            .replace("EEE", getDayOfWeekName().substring(0, Math.min(2, getDayOfWeekName().length())))
            .replace("dd", String.format("%02d", day))
            .replace("d", String.valueOf(day));
        
        return persianDigits ? PersianNumbers.toPersian(result) : result;
    }
    
    /**
     * Returns a relative time string (e.g., "امروز", "دیروز", "۳ روز پیش").
     */
    public String toRelative() {
        PersianDate today = PersianDate.now();
        long daysDiff = today.toEpochDay() - toEpochDay();
        
        if (daysDiff == 0) return "امروز";
        if (daysDiff == 1) return "دیروز";
        if (daysDiff == -1) return "فردا";
        if (daysDiff == 2) return "پریروز";
        if (daysDiff == -2) return "پس‌فردا";
        
        if (daysDiff > 0 && daysDiff < 7) {
            return PersianNumbers.toPersian(daysDiff) + " روز پیش";
        }
        if (daysDiff < 0 && daysDiff > -7) {
            return PersianNumbers.toPersian(-daysDiff) + " روز دیگر";
        }
        
        if (daysDiff > 0 && daysDiff < 30) {
            long weeks = daysDiff / 7;
            return PersianNumbers.toPersian(weeks) + " هفته پیش";
        }
        if (daysDiff < 0 && daysDiff > -30) {
            long weeks = -daysDiff / 7;
            return PersianNumbers.toPersian(weeks) + " هفته دیگر";
        }
        
        if (daysDiff > 0 && daysDiff < 365) {
            long months = daysDiff / 30;
            return PersianNumbers.toPersian(months) + " ماه پیش";
        }
        if (daysDiff < 0 && daysDiff > -365) {
            long months = -daysDiff / 30;
            return PersianNumbers.toPersian(months) + " ماه دیگر";
        }
        
        if (daysDiff > 0) {
            long years = daysDiff / 365;
            return PersianNumbers.toPersian(years) + " سال پیش";
        } else {
            long years = -daysDiff / 365;
            return PersianNumbers.toPersian(years) + " سال دیگر";
        }
    }
    
    // ==================== Validation ====================
    
    /**
     * Validates if the given date components form a valid Persian date.
     */
    public static boolean isValid(int year, int month, int day) {
        if (year < 1 || year > 9999) return false;
        if (month < 1 || month > 12) return false;
        if (day < 1) return false;
        return day <= JalaliAlgorithm.getDaysInMonth(year, month);
    }
    
    /**
     * Checks if the given year is a leap year.
     */
    public static boolean isLeapYear(int year) {
        return JalaliAlgorithm.isLeapYear(year);
    }
    
    private static void validate(int year, int month, int day) {
        if (!isValid(year, month, day)) {
            throw new InvalidDateException(year, month, day);
        }
    }
    
    // ==================== Object Methods ====================
    
    @Override
    public int compareTo(PersianDate other) {
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
        PersianDate other = (PersianDate) obj;
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
