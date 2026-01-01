package io.github.abolpv.gahshomar.zone;

import java.time.*;
import java.time.zone.ZoneRules;

/**
 * Utility class for Iran timezone operations.
 * Iran Standard Time (IRST) is UTC+3:30.
 * Iran Daylight Time (IRDT) is UTC+4:30.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class IranTimeZone {
    
    /**
     * Iran timezone ID.
     */
    public static final String IRAN_ZONE_ID = "Asia/Tehran";
    
    /**
     * Iran timezone.
     */
    public static final ZoneId IRAN = ZoneId.of(IRAN_ZONE_ID);
    
    /**
     * Iran standard time offset (UTC+3:30).
     */
    public static final ZoneOffset IRST_OFFSET = ZoneOffset.ofHoursMinutes(3, 30);
    
    /**
     * Iran daylight saving time offset (UTC+4:30).
     */
    public static final ZoneOffset IRDT_OFFSET = ZoneOffset.ofHoursMinutes(4, 30);
    
    private IranTimeZone() {
        // Utility class
    }
    
    /**
     * Gets the current time in Iran.
     */
    public static ZonedDateTime now() {
        return ZonedDateTime.now(IRAN);
    }
    
    /**
     * Gets the current local time in Iran.
     */
    public static LocalTime currentTime() {
        return LocalTime.now(IRAN);
    }
    
    /**
     * Gets the current local date in Iran.
     */
    public static LocalDate currentDate() {
        return LocalDate.now(IRAN);
    }
    
    /**
     * Gets the current instant in Iran timezone.
     */
    public static Instant currentInstant() {
        return Instant.now();
    }
    
    /**
     * Checks if DST is currently in effect in Iran.
     */
    public static boolean isDSTActive() {
        return isDSTActive(Instant.now());
    }
    
    /**
     * Checks if DST is in effect at the given instant.
     */
    public static boolean isDSTActive(Instant instant) {
        ZoneRules rules = IRAN.getRules();
        return rules.isDaylightSavings(instant);
    }
    
    /**
     * Checks if DST is in effect at the given date-time.
     */
    public static boolean isDSTActive(LocalDateTime dateTime) {
        ZonedDateTime zdt = dateTime.atZone(IRAN);
        return isDSTActive(zdt.toInstant());
    }
    
    /**
     * Gets the current offset for Iran.
     */
    public static ZoneOffset getCurrentOffset() {
        return getOffset(Instant.now());
    }
    
    /**
     * Gets the offset at the given instant.
     */
    public static ZoneOffset getOffset(Instant instant) {
        return IRAN.getRules().getOffset(instant);
    }
    
    /**
     * Converts a UTC instant to Iran time.
     */
    public static ZonedDateTime toIranTime(Instant instant) {
        return instant.atZone(IRAN);
    }
    
    /**
     * Converts Iran time to UTC.
     */
    public static Instant toUTC(ZonedDateTime iranTime) {
        return iranTime.toInstant();
    }
    
    /**
     * Converts a local date-time in Iran to instant.
     */
    public static Instant toInstant(LocalDateTime iranDateTime) {
        return iranDateTime.atZone(IRAN).toInstant();
    }
    
    /**
     * Converts epoch milliseconds to Iran time.
     */
    public static ZonedDateTime fromEpochMilli(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli).atZone(IRAN);
    }
    
    /**
     * Gets the epoch milliseconds for a local date-time in Iran.
     */
    public static long toEpochMilli(LocalDateTime iranDateTime) {
        return toInstant(iranDateTime).toEpochMilli();
    }
    
    /**
     * Gets the difference in hours between Iran and UTC.
     */
    public static double getHoursFromUTC() {
        ZoneOffset offset = getCurrentOffset();
        return offset.getTotalSeconds() / 3600.0;
    }
    
    /**
     * Converts from another timezone to Iran.
     */
    public static ZonedDateTime convertToIran(ZonedDateTime other) {
        return other.withZoneSameInstant(IRAN);
    }
    
    /**
     * Converts from Iran to another timezone.
     */
    public static ZonedDateTime convertFromIran(ZonedDateTime iranTime, ZoneId targetZone) {
        return iranTime.withZoneSameInstant(targetZone);
    }
    
    /**
     * Gets next DST transition from now.
     * Returns null if Iran no longer uses DST.
     */
    public static ZonedDateTime getNextDSTTransition() {
        ZoneRules rules = IRAN.getRules();
        var transition = rules.nextTransition(Instant.now());
        if (transition != null) {
            return transition.getInstant().atZone(IRAN);
        }
        return null;
    }
    
    /**
     * Formats current Iran time as string.
     */
    public static String formatCurrentTime() {
        return now().toLocalTime().toString();
    }
    
    /**
     * Formats current Iran date-time as string.
     */
    public static String formatCurrentDateTime() {
        return now().toLocalDateTime().toString();
    }
}
