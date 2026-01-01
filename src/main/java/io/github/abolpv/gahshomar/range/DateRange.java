package io.github.abolpv.gahshomar.range;

import io.github.abolpv.gahshomar.PersianDate;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents a range of Persian dates.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class DateRange implements Iterable<PersianDate> {
    
    private final PersianDate start;
    private final PersianDate end;
    
    private DateRange(PersianDate start, PersianDate end) {
        this.start = start;
        this.end = end;
    }
    
    /**
     * Creates a date range between two dates (inclusive).
     */
    public static DateRange between(PersianDate start, PersianDate end) {
        Objects.requireNonNull(start, "start cannot be null");
        Objects.requireNonNull(end, "end cannot be null");
        
        if (start.isAfter(end)) {
            return new DateRange(end, start);
        }
        return new DateRange(start, end);
    }
    
    /**
     * Creates a date range for a specific month.
     */
    public static DateRange ofMonth(int year, int month) {
        PersianDate start = PersianDate.of(year, month, 1);
        return new DateRange(start, start.atEndOfMonth());
    }
    
    /**
     * Creates a date range for a specific year.
     */
    public static DateRange ofYear(int year) {
        return new DateRange(
            PersianDate.of(year, 1, 1),
            PersianDate.of(year, 12, PersianDate.isLeapYear(year) ? 30 : 29)
        );
    }
    
    /**
     * Creates a date range for a week containing the given date.
     */
    public static DateRange weekOf(PersianDate date) {
        return new DateRange(date.atStartOfWeek(), date.atEndOfWeek());
    }
    
    /**
     * Gets the start date.
     */
    public PersianDate getStart() {
        return start;
    }
    
    /**
     * Gets the end date.
     */
    public PersianDate getEnd() {
        return end;
    }
    
    /**
     * Gets the number of days in this range (inclusive).
     */
    public long getDays() {
        return Math.abs(end.toEpochDay() - start.toEpochDay()) + 1;
    }
    
    /**
     * Checks if this range contains the given date.
     */
    public boolean contains(PersianDate date) {
        return date.isBetween(start, end);
    }
    
    /**
     * Checks if this range overlaps with another range.
     */
    public boolean overlaps(DateRange other) {
        return !start.isAfter(other.end) && !end.isBefore(other.start);
    }
    
    /**
     * Checks if this range is empty (start equals end).
     */
    public boolean isSingleDay() {
        return start.isEqual(end);
    }
    
    /**
     * Returns the intersection of this range with another.
     */
    public DateRange intersection(DateRange other) {
        if (!overlaps(other)) {
            return null;
        }
        PersianDate newStart = start.isAfter(other.start) ? start : other.start;
        PersianDate newEnd = end.isBefore(other.end) ? end : other.end;
        return new DateRange(newStart, newEnd);
    }
    
    /**
     * Returns a stream of all dates in this range.
     */
    public Stream<PersianDate> stream() {
        return Stream.iterate(start, d -> !d.isAfter(end), d -> d.plusDays(1));
    }
    
    /**
     * Counts workdays in this range.
     */
    public long countWorkdays() {
        return stream().filter(PersianDate::isWorkday).count();
    }
    
    /**
     * Counts weekends in this range.
     */
    public long countWeekends() {
        return stream().filter(PersianDate::isWeekend).count();
    }
    
    @Override
    public Iterator<PersianDate> iterator() {
        return stream().iterator();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DateRange other = (DateRange) obj;
        return start.equals(other.start) && end.equals(other.end);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
    
    @Override
    public String toString() {
        return start.format() + " - " + end.format();
    }
}
