package io.github.abolpv.gahshomar.range;

import io.github.abolpv.gahshomar.PersianDate;
import io.github.abolpv.gahshomar.temporal.PersianDayOfWeek;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Utility class for creating streams of Persian dates.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class DateStream {
    
    private DateStream() {
        // Utility class
    }
    
    /**
     * Creates an infinite stream of dates starting from the given date.
     */
    public static Stream<PersianDate> from(PersianDate start) {
        return Stream.iterate(start, d -> d.plusDays(1));
    }
    
    /**
     * Creates a stream of dates from start to end (exclusive).
     */
    public static Stream<PersianDate> range(PersianDate start, PersianDate endExclusive) {
        long days = Math.abs(endExclusive.toEpochDay() - start.toEpochDay());
        return Stream.iterate(start, d -> d.plusDays(1)).limit(days);
    }
    
    /**
     * Creates a stream of dates from start to end (inclusive).
     */
    public static Stream<PersianDate> rangeClosed(PersianDate start, PersianDate endInclusive) {
        return range(start, endInclusive.plusDays(1));
    }
    
    /**
     * Creates a stream of all dates in a month.
     */
    public static Stream<PersianDate> ofMonth(int year, int month) {
        PersianDate start = PersianDate.of(year, month, 1);
        return Stream.iterate(start, d -> d.plusDays(1))
                     .limit(start.lengthOfMonth());
    }
    
    /**
     * Creates a stream of all dates in a year.
     */
    public static Stream<PersianDate> ofYear(int year) {
        PersianDate start = PersianDate.of(year, 1, 1);
        return Stream.iterate(start, d -> d.plusDays(1))
                     .limit(start.lengthOfYear());
    }
    
    /**
     * Creates a stream of all Fridays in a date range.
     */
    public static Stream<PersianDate> fridays(PersianDate start, PersianDate end) {
        return range(start, end.plusDays(1))
                .filter(d -> d.getDayOfWeek() == PersianDayOfWeek.JOMEH);
    }
    
    /**
     * Creates a stream of all workdays in a date range.
     */
    public static Stream<PersianDate> workdays(PersianDate start, PersianDate end) {
        return range(start, end.plusDays(1))
                .filter(PersianDate::isWorkday);
    }
    
    /**
     * Creates a stream of all weekends in a date range.
     */
    public static Stream<PersianDate> weekends(PersianDate start, PersianDate end) {
        return range(start, end.plusDays(1))
                .filter(PersianDate::isWeekend);
    }
    
    /**
     * Creates a stream of first days of each month in a year.
     */
    public static Stream<PersianDate> firstDaysOfMonths(int year) {
        return Stream.iterate(1, m -> m + 1)
                     .limit(12)
                     .map(m -> PersianDate.of(year, m, 1));
    }
    
    /**
     * Creates a stream of last days of each month in a year.
     */
    public static Stream<PersianDate> lastDaysOfMonths(int year) {
        return Stream.iterate(1, m -> m + 1)
                     .limit(12)
                     .map(m -> PersianDate.of(year, m, 1).atEndOfMonth());
    }
    
    /**
     * Creates a stream of dates with step (every n days).
     */
    public static Stream<PersianDate> step(PersianDate start, PersianDate end, int stepDays) {
        if (stepDays <= 0) {
            throw new IllegalArgumentException("stepDays must be positive");
        }
        return Stream.iterate(start, d -> !d.isAfter(end), d -> d.plusDays(stepDays));
    }
    
    /**
     * Creates a stream of dates for a specific day of week.
     */
    public static Stream<PersianDate> dayOfWeek(PersianDate start, PersianDate end, PersianDayOfWeek dayOfWeek) {
        return range(start, end.plusDays(1))
                .filter(d -> d.getDayOfWeek() == dayOfWeek);
    }
    
    /**
     * Creates a stream of the nth day of each month.
     */
    public static Stream<PersianDate> nthDayOfMonths(int year, int dayOfMonth) {
        return Stream.iterate(1, m -> m + 1)
                     .limit(12)
                     .filter(m -> PersianDate.isValid(year, m, dayOfMonth))
                     .map(m -> PersianDate.of(year, m, dayOfMonth));
    }
    
    /**
     * Creates a stream of years between two dates.
     */
    public static Stream<Integer> years(int startYear, int endYear) {
        return Stream.iterate(startYear, y -> y + 1)
                     .limit(endYear - startYear + 1);
    }
    
    /**
     * Creates a stream of leap years in a range.
     */
    public static Stream<Integer> leapYears(int startYear, int endYear) {
        return years(startYear, endYear)
                .filter(PersianDate::isLeapYear);
    }
}
