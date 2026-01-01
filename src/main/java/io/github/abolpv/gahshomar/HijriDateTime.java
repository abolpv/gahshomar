package io.github.abolpv.gahshomar;

import io.github.abolpv.gahshomar.format.PersianNumbers;
import io.github.abolpv.gahshomar.temporal.*;

import java.io.Serializable;
import java.time.*;
import java.util.Objects;

/**
 * A date-time in the Hijri (Islamic/Lunar) calendar system.
 * This class is immutable and thread-safe.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class HijriDateTime implements Comparable<HijriDateTime>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final HijriDate date;
    private final int hour;
    private final int minute;
    private final int second;
    private final int nano;
    
    private HijriDateTime(HijriDate date, int hour, int minute, int second, int nano) {
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.nano = nano;
    }
    
    // ==================== Factory Methods ====================
    
    public static HijriDateTime of(int year, int month, int day, int hour, int minute) {
        return of(year, month, day, hour, minute, 0, 0);
    }
    
    public static HijriDateTime of(int year, int month, int day, int hour, int minute, int second) {
        return of(year, month, day, hour, minute, second, 0);
    }
    
    public static HijriDateTime of(int year, int month, int day, int hour, int minute, int second, int nano) {
        validateTime(hour, minute, second, nano);
        HijriDate date = HijriDate.of(year, month, day);
        return new HijriDateTime(date, hour, minute, second, nano);
    }
    
    public static HijriDateTime of(HijriDate date, LocalTime time) {
        Objects.requireNonNull(date, "date cannot be null");
        Objects.requireNonNull(time, "time cannot be null");
        return new HijriDateTime(date, time.getHour(), time.getMinute(), 
                                 time.getSecond(), time.getNano());
    }
    
    public static HijriDateTime of(HijriDate date) {
        Objects.requireNonNull(date, "date cannot be null");
        return new HijriDateTime(date, 0, 0, 0, 0);
    }
    
    public static HijriDateTime now() {
        return now(ZoneId.systemDefault());
    }
    
    public static HijriDateTime now(ZoneId zone) {
        LocalDateTime ldt = LocalDateTime.now(zone);
        return from(ldt);
    }
    
    public static HijriDateTime from(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime, "dateTime cannot be null");
        HijriDate date = HijriDate.from(dateTime.toLocalDate());
        return new HijriDateTime(date, dateTime.getHour(), dateTime.getMinute(),
                                 dateTime.getSecond(), dateTime.getNano());
    }
    
    public static HijriDateTime from(Instant instant, ZoneId zone) {
        return from(LocalDateTime.ofInstant(instant, zone));
    }
    
    public static HijriDateTime ofEpochMilli(long epochMilli) {
        return from(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }
    
    // ==================== Getters ====================
    
    public HijriDate getDate() {
        return date;
    }
    
    public int getYear() {
        return date.getYear();
    }
    
    public int getMonthValue() {
        return date.getMonthValue();
    }
    
    public HijriMonth getMonth() {
        return date.getMonth();
    }
    
    public int getDayOfMonth() {
        return date.getDayOfMonth();
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
    
    public LocalDateTime toGregorian() {
        LocalDate gregorianDate = date.toGregorian();
        return LocalDateTime.of(gregorianDate, toLocalTime());
    }
    
    public PersianDateTime toPersian() {
        return PersianDateTime.of(date.toPersian(), toLocalTime());
    }
    
    public Instant toInstant(ZoneId zone) {
        return toGregorian().atZone(zone).toInstant();
    }
    
    public long toEpochMilli(ZoneId zone) {
        return toInstant(zone).toEpochMilli();
    }
    
    public HijriDate toDate() {
        return date;
    }
    
    // ==================== Arithmetic ====================
    
    public HijriDateTime plusYears(long years) {
        return new HijriDateTime(date.plusYears(years), hour, minute, second, nano);
    }
    
    public HijriDateTime plusMonths(long months) {
        return new HijriDateTime(date.plusMonths(months), hour, minute, second, nano);
    }
    
    public HijriDateTime plusDays(long days) {
        return new HijriDateTime(date.plusDays(days), hour, minute, second, nano);
    }
    
    public HijriDateTime plusHours(long hours) {
        LocalDateTime ldt = toGregorian().plusHours(hours);
        return from(ldt);
    }
    
    public HijriDateTime plusMinutes(long minutes) {
        LocalDateTime ldt = toGregorian().plusMinutes(minutes);
        return from(ldt);
    }
    
    public HijriDateTime plusSeconds(long seconds) {
        LocalDateTime ldt = toGregorian().plusSeconds(seconds);
        return from(ldt);
    }
    
    public HijriDateTime minusYears(long years) {
        return plusYears(-years);
    }
    
    public HijriDateTime minusMonths(long months) {
        return plusMonths(-months);
    }
    
    public HijriDateTime minusDays(long days) {
        return plusDays(-days);
    }
    
    public HijriDateTime minusHours(long hours) {
        return plusHours(-hours);
    }
    
    public HijriDateTime minusMinutes(long minutes) {
        return plusMinutes(-minutes);
    }
    
    public HijriDateTime minusSeconds(long seconds) {
        return plusSeconds(-seconds);
    }
    
    // ==================== With Methods ====================
    
    public HijriDateTime withYear(int year) {
        return new HijriDateTime(date.withYear(year), hour, minute, second, nano);
    }
    
    public HijriDateTime withMonth(int month) {
        return new HijriDateTime(date.withMonth(month), hour, minute, second, nano);
    }
    
    public HijriDateTime withDayOfMonth(int day) {
        return new HijriDateTime(date.withDayOfMonth(day), hour, minute, second, nano);
    }
    
    public HijriDateTime withHour(int hour) {
        validateTime(hour, minute, second, nano);
        return new HijriDateTime(date, hour, minute, second, nano);
    }
    
    public HijriDateTime withMinute(int minute) {
        validateTime(hour, minute, second, nano);
        return new HijriDateTime(date, hour, minute, second, nano);
    }
    
    public HijriDateTime withSecond(int second) {
        validateTime(hour, minute, second, nano);
        return new HijriDateTime(date, hour, minute, second, nano);
    }
    
    public HijriDateTime atStartOfDay() {
        return new HijriDateTime(date, 0, 0, 0, 0);
    }
    
    public HijriDateTime atEndOfDay() {
        return new HijriDateTime(date, 23, 59, 59, 999999999);
    }
    
    // ==================== Comparison ====================
    
    public boolean isBefore(HijriDateTime other) {
        return compareTo(other) < 0;
    }
    
    public boolean isAfter(HijriDateTime other) {
        return compareTo(other) > 0;
    }
    
    public boolean isEqual(HijriDateTime other) {
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
    public int compareTo(HijriDateTime other) {
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
        HijriDateTime other = (HijriDateTime) obj;
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
