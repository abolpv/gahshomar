package io.github.abolpv.gahshomar;

import io.github.abolpv.gahshomar.core.JalaliAlgorithm;
import io.github.abolpv.gahshomar.exception.InvalidDateException;
import io.github.abolpv.gahshomar.format.PersianNumbers;
import io.github.abolpv.gahshomar.temporal.*;

import java.io.Serializable;
import java.time.*;
import java.util.Objects;

/**
 * A date-time in the Persian (Jalali/Shamsi) calendar system.
 * This class is immutable and thread-safe.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class PersianDateTime implements Comparable<PersianDateTime>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final PersianDate date;
    private final int hour;
    private final int minute;
    private final int second;
    private final int nano;
    
    private PersianDateTime(PersianDate date, int hour, int minute, int second, int nano) {
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.nano = nano;
    }
    
    // ==================== Factory Methods ====================
    
    /**
     * Creates a PersianDateTime from date and time components.
     */
    public static PersianDateTime of(int year, int month, int day, int hour, int minute) {
        return of(year, month, day, hour, minute, 0, 0);
    }
    
    /**
     * Creates a PersianDateTime from date and time components.
     */
    public static PersianDateTime of(int year, int month, int day, int hour, int minute, int second) {
        return of(year, month, day, hour, minute, second, 0);
    }
    
    /**
     * Creates a PersianDateTime from date and time components.
     */
    public static PersianDateTime of(int year, int month, int day, int hour, int minute, int second, int nano) {
        validateTime(hour, minute, second, nano);
        PersianDate date = PersianDate.of(year, month, day);
        return new PersianDateTime(date, hour, minute, second, nano);
    }
    
    /**
     * Creates a PersianDateTime from a PersianDate and LocalTime.
     */
    public static PersianDateTime of(PersianDate date, LocalTime time) {
        Objects.requireNonNull(date, "date cannot be null");
        Objects.requireNonNull(time, "time cannot be null");
        return new PersianDateTime(date, time.getHour(), time.getMinute(), 
                                   time.getSecond(), time.getNano());
    }
    
    /**
     * Creates a PersianDateTime from a PersianDate at midnight.
     */
    public static PersianDateTime of(PersianDate date) {
        Objects.requireNonNull(date, "date cannot be null");
        return new PersianDateTime(date, 0, 0, 0, 0);
    }
    
    /**
     * Creates a PersianDateTime for the current date and time.
     */
    public static PersianDateTime now() {
        return now(ZoneId.systemDefault());
    }
    
    /**
     * Creates a PersianDateTime for the current date and time in a zone.
     */
    public static PersianDateTime now(ZoneId zone) {
        LocalDateTime ldt = LocalDateTime.now(zone);
        return from(ldt);
    }
    
    /**
     * Creates a PersianDateTime from a LocalDateTime.
     */
    public static PersianDateTime from(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime, "dateTime cannot be null");
        PersianDate date = PersianDate.from(dateTime.toLocalDate());
        return new PersianDateTime(date, dateTime.getHour(), dateTime.getMinute(),
                                   dateTime.getSecond(), dateTime.getNano());
    }
    
    /**
     * Creates a PersianDateTime from an Instant.
     */
    public static PersianDateTime from(Instant instant, ZoneId zone) {
        return from(LocalDateTime.ofInstant(instant, zone));
    }
    
    /**
     * Creates a PersianDateTime from epoch milliseconds.
     */
    public static PersianDateTime ofEpochMilli(long epochMilli) {
        return from(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }
    
    // ==================== Getters ====================
    
    public PersianDate getDate() {
        return date;
    }
    
    public int getYear() {
        return date.getYear();
    }
    
    public int getMonthValue() {
        return date.getMonthValue();
    }
    
    public PersianMonth getMonth() {
        return date.getMonth();
    }
    
    public int getDayOfMonth() {
        return date.getDayOfMonth();
    }
    
    public int getDayOfYear() {
        return date.getDayOfYear();
    }
    
    public PersianDayOfWeek getDayOfWeek() {
        return date.getDayOfWeek();
    }
    
    public int getHour() {
        return hour;
    }
    
    public int getMinute() {
        return minute;
    }
    
    public int getSecond() {
        return second;
    }
    
    public int getNano() {
        return nano;
    }
    
    public LocalTime toLocalTime() {
        return LocalTime.of(hour, minute, second, nano);
    }
    
    // ==================== Conversion ====================
    
    /**
     * Converts to LocalDateTime.
     */
    public LocalDateTime toGregorian() {
        LocalDate gregorianDate = date.toGregorian();
        return LocalDateTime.of(gregorianDate, toLocalTime());
    }
    
    /**
     * Converts to Instant.
     */
    public Instant toInstant(ZoneId zone) {
        return toGregorian().atZone(zone).toInstant();
    }
    
    /**
     * Converts to epoch milliseconds.
     */
    public long toEpochMilli(ZoneId zone) {
        return toInstant(zone).toEpochMilli();
    }
    
    /**
     * Gets just the date part.
     */
    public PersianDate toDate() {
        return date;
    }
    
    // ==================== Arithmetic ====================
    
    public PersianDateTime plusYears(long years) {
        return new PersianDateTime(date.plusYears(years), hour, minute, second, nano);
    }
    
    public PersianDateTime plusMonths(long months) {
        return new PersianDateTime(date.plusMonths(months), hour, minute, second, nano);
    }
    
    public PersianDateTime plusDays(long days) {
        return new PersianDateTime(date.plusDays(days), hour, minute, second, nano);
    }
    
    public PersianDateTime plusHours(long hours) {
        LocalDateTime ldt = toGregorian().plusHours(hours);
        return from(ldt);
    }
    
    public PersianDateTime plusMinutes(long minutes) {
        LocalDateTime ldt = toGregorian().plusMinutes(minutes);
        return from(ldt);
    }
    
    public PersianDateTime plusSeconds(long seconds) {
        LocalDateTime ldt = toGregorian().plusSeconds(seconds);
        return from(ldt);
    }
    
    public PersianDateTime minusYears(long years) {
        return plusYears(-years);
    }
    
    public PersianDateTime minusMonths(long months) {
        return plusMonths(-months);
    }
    
    public PersianDateTime minusDays(long days) {
        return plusDays(-days);
    }
    
    public PersianDateTime minusHours(long hours) {
        return plusHours(-hours);
    }
    
    public PersianDateTime minusMinutes(long minutes) {
        return plusMinutes(-minutes);
    }
    
    public PersianDateTime minusSeconds(long seconds) {
        return plusSeconds(-seconds);
    }
    
    // ==================== With Methods ====================
    
    public PersianDateTime withYear(int year) {
        return new PersianDateTime(date.withYear(year), hour, minute, second, nano);
    }
    
    public PersianDateTime withMonth(int month) {
        return new PersianDateTime(date.withMonth(month), hour, minute, second, nano);
    }
    
    public PersianDateTime withDayOfMonth(int day) {
        return new PersianDateTime(date.withDayOfMonth(day), hour, minute, second, nano);
    }
    
    public PersianDateTime withHour(int hour) {
        validateTime(hour, minute, second, nano);
        return new PersianDateTime(date, hour, minute, second, nano);
    }
    
    public PersianDateTime withMinute(int minute) {
        validateTime(hour, minute, second, nano);
        return new PersianDateTime(date, hour, minute, second, nano);
    }
    
    public PersianDateTime withSecond(int second) {
        validateTime(hour, minute, second, nano);
        return new PersianDateTime(date, hour, minute, second, nano);
    }
    
    public PersianDateTime atStartOfDay() {
        return new PersianDateTime(date, 0, 0, 0, 0);
    }
    
    public PersianDateTime atEndOfDay() {
        return new PersianDateTime(date, 23, 59, 59, 999999999);
    }
    
    // ==================== Comparison ====================
    
    public boolean isBefore(PersianDateTime other) {
        return compareTo(other) < 0;
    }
    
    public boolean isAfter(PersianDateTime other) {
        return compareTo(other) > 0;
    }
    
    public boolean isEqual(PersianDateTime other) {
        return compareTo(other) == 0;
    }
    
    // ==================== Formatting ====================
    
    public String format() {
        return format("yyyy/MM/dd HH:mm:ss");
    }
    
    public String format(String pattern) {
        return format(pattern, false);
    }
    
    public String format(String pattern, boolean persianDigits) {
        String result = date.format(pattern, false)
            .replace("HH", String.format("%02d", hour))
            .replace("H", String.valueOf(hour))
            .replace("mm", String.format("%02d", minute))
            .replace("m", String.valueOf(minute))
            .replace("ss", String.format("%02d", second))
            .replace("s", String.valueOf(second));
        
        return persianDigits ? PersianNumbers.toPersian(result) : result;
    }
    
    // ==================== Validation ====================
    
    private static void validateTime(int hour, int minute, int second, int nano) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be 0-23: " + hour);
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be 0-59: " + minute);
        }
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException("Second must be 0-59: " + second);
        }
        if (nano < 0 || nano > 999999999) {
            throw new IllegalArgumentException("Nano must be 0-999999999: " + nano);
        }
    }
    
    // ==================== Object Methods ====================
    
    @Override
    public int compareTo(PersianDateTime other) {
        int cmp = date.compareTo(other.date);
        if (cmp != 0) return cmp;
        cmp = Integer.compare(hour, other.hour);
        if (cmp != 0) return cmp;
        cmp = Integer.compare(minute, other.minute);
        if (cmp != 0) return cmp;
        cmp = Integer.compare(second, other.second);
        if (cmp != 0) return cmp;
        return Integer.compare(nano, other.nano);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PersianDateTime other = (PersianDateTime) obj;
        return hour == other.hour && minute == other.minute && 
               second == other.second && nano == other.nano &&
               date.equals(other.date);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(date, hour, minute, second, nano);
    }
    
    @Override
    public String toString() {
        return format();
    }
}
