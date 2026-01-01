package io.github.abolpv.gahshomar.zone;

import io.github.abolpv.gahshomar.PersianDate;
import io.github.abolpv.gahshomar.PersianDateTime;
import io.github.abolpv.gahshomar.format.PersianNumbers;

import java.io.Serializable;
import java.time.*;
import java.util.Objects;

/**
 * A date-time with a time-zone in the Persian calendar system.
 * This class is immutable and thread-safe.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class ZonedPersianDateTime implements Comparable<ZonedPersianDateTime>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final PersianDateTime dateTime;
    private final ZoneId zone;
    private final ZoneOffset offset;
    
    private ZonedPersianDateTime(PersianDateTime dateTime, ZoneId zone, ZoneOffset offset) {
        this.dateTime = dateTime;
        this.zone = zone;
        this.offset = offset;
    }
    
    // ==================== Factory Methods ====================
    
    /**
     * Creates a ZonedPersianDateTime from components.
     */
    public static ZonedPersianDateTime of(int year, int month, int day, int hour, int minute, ZoneId zone) {
        return of(year, month, day, hour, minute, 0, zone);
    }
    
    /**
     * Creates a ZonedPersianDateTime from components.
     */
    public static ZonedPersianDateTime of(int year, int month, int day, int hour, int minute, int second, ZoneId zone) {
        PersianDateTime dt = PersianDateTime.of(year, month, day, hour, minute, second);
        ZoneOffset offset = zone.getRules().getOffset(dt.toGregorian().atZone(zone).toInstant());
        return new ZonedPersianDateTime(dt, zone, offset);
    }
    
    /**
     * Creates a ZonedPersianDateTime from a PersianDateTime and zone.
     */
    public static ZonedPersianDateTime of(PersianDateTime dateTime, ZoneId zone) {
        Objects.requireNonNull(dateTime, "dateTime cannot be null");
        Objects.requireNonNull(zone, "zone cannot be null");
        ZoneOffset offset = zone.getRules().getOffset(dateTime.toGregorian().atZone(zone).toInstant());
        return new ZonedPersianDateTime(dateTime, zone, offset);
    }
    
    /**
     * Creates a ZonedPersianDateTime from a PersianDate and zone (at midnight).
     */
    public static ZonedPersianDateTime of(PersianDate date, ZoneId zone) {
        return of(PersianDateTime.of(date), zone);
    }
    
    /**
     * Creates a ZonedPersianDateTime for now in the system timezone.
     */
    public static ZonedPersianDateTime now() {
        return now(ZoneId.systemDefault());
    }
    
    /**
     * Creates a ZonedPersianDateTime for now in the specified timezone.
     */
    public static ZonedPersianDateTime now(ZoneId zone) {
        ZonedDateTime zdt = ZonedDateTime.now(zone);
        PersianDateTime pdt = PersianDateTime.from(zdt.toLocalDateTime());
        return new ZonedPersianDateTime(pdt, zone, zdt.getOffset());
    }
    
    /**
     * Creates a ZonedPersianDateTime for now in Iran timezone.
     */
    public static ZonedPersianDateTime nowInIran() {
        return now(IranTimeZone.IRAN);
    }
    
    /**
     * Creates a ZonedPersianDateTime from an Instant.
     */
    public static ZonedPersianDateTime ofInstant(Instant instant, ZoneId zone) {
        ZonedDateTime zdt = instant.atZone(zone);
        PersianDateTime pdt = PersianDateTime.from(zdt.toLocalDateTime());
        return new ZonedPersianDateTime(pdt, zone, zdt.getOffset());
    }
    
    /**
     * Creates a ZonedPersianDateTime from a ZonedDateTime.
     */
    public static ZonedPersianDateTime from(ZonedDateTime zonedDateTime) {
        Objects.requireNonNull(zonedDateTime, "zonedDateTime cannot be null");
        PersianDateTime pdt = PersianDateTime.from(zonedDateTime.toLocalDateTime());
        return new ZonedPersianDateTime(pdt, zonedDateTime.getZone(), zonedDateTime.getOffset());
    }
    
    // ==================== Getters ====================
    
    public PersianDateTime getDateTime() {
        return dateTime;
    }
    
    public PersianDate getDate() {
        return dateTime.getDate();
    }
    
    public int getYear() {
        return dateTime.getYear();
    }
    
    public int getMonthValue() {
        return dateTime.getMonthValue();
    }
    
    public int getDayOfMonth() {
        return dateTime.getDayOfMonth();
    }
    
    public int getHour() {
        return dateTime.getHour();
    }
    
    public int getMinute() {
        return dateTime.getMinute();
    }
    
    public int getSecond() {
        return dateTime.getSecond();
    }
    
    public ZoneId getZone() {
        return zone;
    }
    
    public ZoneOffset getOffset() {
        return offset;
    }
    
    // ==================== Conversion ====================
    
    /**
     * Converts to ZonedDateTime.
     */
    public ZonedDateTime toZonedDateTime() {
        return dateTime.toGregorian().atZone(zone);
    }
    
    /**
     * Converts to Instant.
     */
    public Instant toInstant() {
        return toZonedDateTime().toInstant();
    }
    
    /**
     * Converts to epoch milliseconds.
     */
    public long toEpochMilli() {
        return toInstant().toEpochMilli();
    }
    
    /**
     * Converts to local PersianDateTime (drops zone info).
     */
    public PersianDateTime toLocalDateTime() {
        return dateTime;
    }
    
    // ==================== Zone Operations ====================
    
    /**
     * Returns a copy with a different zone, keeping the same instant.
     */
    public ZonedPersianDateTime withZoneSameInstant(ZoneId newZone) {
        return ofInstant(toInstant(), newZone);
    }
    
    /**
     * Returns a copy with a different zone, keeping the same local time.
     */
    public ZonedPersianDateTime withZoneSameLocal(ZoneId newZone) {
        return of(dateTime, newZone);
    }
    
    /**
     * Checks if DST is in effect.
     */
    public boolean isDaylightSavingTime() {
        return zone.getRules().isDaylightSavings(toInstant());
    }
    
    // ==================== Arithmetic ====================
    
    public ZonedPersianDateTime plusYears(long years) {
        return of(dateTime.plusYears(years), zone);
    }
    
    public ZonedPersianDateTime plusMonths(long months) {
        return of(dateTime.plusMonths(months), zone);
    }
    
    public ZonedPersianDateTime plusDays(long days) {
        return of(dateTime.plusDays(days), zone);
    }
    
    public ZonedPersianDateTime plusHours(long hours) {
        return ofInstant(toInstant().plusSeconds(hours * 3600), zone);
    }
    
    public ZonedPersianDateTime plusMinutes(long minutes) {
        return ofInstant(toInstant().plusSeconds(minutes * 60), zone);
    }
    
    public ZonedPersianDateTime plusSeconds(long seconds) {
        return ofInstant(toInstant().plusSeconds(seconds), zone);
    }
    
    public ZonedPersianDateTime minusYears(long years) {
        return plusYears(-years);
    }
    
    public ZonedPersianDateTime minusMonths(long months) {
        return plusMonths(-months);
    }
    
    public ZonedPersianDateTime minusDays(long days) {
        return plusDays(-days);
    }
    
    public ZonedPersianDateTime minusHours(long hours) {
        return plusHours(-hours);
    }
    
    public ZonedPersianDateTime minusMinutes(long minutes) {
        return plusMinutes(-minutes);
    }
    
    public ZonedPersianDateTime minusSeconds(long seconds) {
        return plusSeconds(-seconds);
    }
    
    // ==================== Comparison ====================
    
    public boolean isBefore(ZonedPersianDateTime other) {
        return toInstant().isBefore(other.toInstant());
    }
    
    public boolean isAfter(ZonedPersianDateTime other) {
        return toInstant().isAfter(other.toInstant());
    }
    
    public boolean isEqual(ZonedPersianDateTime other) {
        return toInstant().equals(other.toInstant());
    }
    
    // ==================== Formatting ====================
    
    public String format() {
        return format("yyyy/MM/dd HH:mm:ss Z");
    }
    
    public String format(String pattern) {
        return format(pattern, false);
    }
    
    public String format(String pattern, boolean persianDigits) {
        String result = dateTime.format(pattern, false)
            .replace("Z", offset.toString())
            .replace("z", zone.getId());
        
        return persianDigits ? PersianNumbers.toPersian(result) : result;
    }
    
    // ==================== Object Methods ====================
    
    @Override
    public int compareTo(ZonedPersianDateTime other) {
        return toInstant().compareTo(other.toInstant());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ZonedPersianDateTime other = (ZonedPersianDateTime) obj;
        return dateTime.equals(other.dateTime) && zone.equals(other.zone);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(dateTime, zone);
    }
    
    @Override
    public String toString() {
        return format();
    }
}
