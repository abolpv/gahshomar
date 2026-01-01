package io.github.abolpv.gahshomar.zone;

import io.github.abolpv.gahshomar.PersianDate;

import java.time.*;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for Daylight Saving Time rules and calculations.
 * Primarily focused on Iran's DST rules.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class DSTRule {
    
    private DSTRule() {
        // Utility class
    }
    
    /**
     * Checks if DST is currently active in Iran.
     */
    public static boolean isDSTActiveInIran() {
        return isDSTActive(IranTimeZone.IRAN, Instant.now());
    }
    
    /**
     * Checks if DST is active at a given instant in a zone.
     */
    public static boolean isDSTActive(ZoneId zone, Instant instant) {
        return zone.getRules().isDaylightSavings(instant);
    }
    
    /**
     * Checks if DST is active on a given Persian date in Iran.
     */
    public static boolean isDSTActive(PersianDate date) {
        LocalDate gregorian = date.toGregorian();
        Instant instant = gregorian.atStartOfDay(IranTimeZone.IRAN).toInstant();
        return isDSTActive(IranTimeZone.IRAN, instant);
    }
    
    /**
     * Gets the DST offset amount for Iran (1 hour).
     */
    public static Duration getDSTOffset() {
        return Duration.ofHours(1);
    }
    
    /**
     * Gets the current offset for a zone.
     */
    public static ZoneOffset getCurrentOffset(ZoneId zone) {
        return zone.getRules().getOffset(Instant.now());
    }
    
    /**
     * Gets the standard offset for Iran (UTC+3:30).
     */
    public static ZoneOffset getIranStandardOffset() {
        return IranTimeZone.IRST_OFFSET;
    }
    
    /**
     * Gets the DST offset for Iran (UTC+4:30).
     */
    public static ZoneOffset getIranDSTOffset() {
        return IranTimeZone.IRDT_OFFSET;
    }
    
    /**
     * Gets the next DST transition for a zone.
     */
    public static ZoneOffsetTransition getNextTransition(ZoneId zone) {
        return zone.getRules().nextTransition(Instant.now());
    }
    
    /**
     * Gets the next DST transition for Iran.
     */
    public static ZoneOffsetTransition getNextIranTransition() {
        return getNextTransition(IranTimeZone.IRAN);
    }
    
    /**
     * Gets all DST transitions in a year for Iran.
     */
    public static List<DSTTransitionInfo> getTransitionsInYear(int gregorianYear) {
        List<DSTTransitionInfo> transitions = new ArrayList<>();
        ZoneRules rules = IranTimeZone.IRAN.getRules();
        
        Instant startOfYear = LocalDate.of(gregorianYear, 1, 1)
                                       .atStartOfDay(IranTimeZone.IRAN)
                                       .toInstant();
        Instant endOfYear = LocalDate.of(gregorianYear, 12, 31)
                                     .atTime(23, 59, 59)
                                     .atZone(IranTimeZone.IRAN)
                                     .toInstant();
        
        ZoneOffsetTransition transition = rules.nextTransition(startOfYear.minusSeconds(1));
        
        while (transition != null && transition.getInstant().isBefore(endOfYear)) {
            transitions.add(new DSTTransitionInfo(
                transition.getInstant(),
                transition.getOffsetBefore(),
                transition.getOffsetAfter(),
                transition.isGap()
            ));
            transition = rules.nextTransition(transition.getInstant());
        }
        
        return transitions;
    }
    
    /**
     * Checks if a local time is in the DST gap (doesn't exist).
     */
    public static boolean isInGap(LocalDateTime dateTime, ZoneId zone) {
        try {
            ZonedDateTime.of(dateTime, zone);
            return false;
        } catch (DateTimeException e) {
            return true;
        }
    }
    
    /**
     * Checks if a local time is in the DST overlap (exists twice).
     */
    public static boolean isInOverlap(LocalDateTime dateTime, ZoneId zone) {
        ZoneRules rules = zone.getRules();
        ZoneOffsetTransition transition = rules.getTransition(dateTime);
        return transition != null && transition.isOverlap();
    }
    
    /**
     * Gets DST info string for a date.
     */
    public static String getDSTInfo(PersianDate date) {
        boolean isDST = isDSTActive(date);
        ZoneOffset offset = isDST ? getIranDSTOffset() : getIranStandardOffset();
        String offsetStr = offset.toString();
        
        if (isDST) {
            return "ساعت تابستانی فعال (UTC" + offsetStr + ")";
        } else {
            return "ساعت زمستانی (UTC" + offsetStr + ")";
        }
    }
    
    /**
     * Represents information about a DST transition.
     */
    public static class DSTTransitionInfo {
        private final Instant instant;
        private final ZoneOffset offsetBefore;
        private final ZoneOffset offsetAfter;
        private final boolean isGap;
        
        public DSTTransitionInfo(Instant instant, ZoneOffset offsetBefore, 
                                ZoneOffset offsetAfter, boolean isGap) {
            this.instant = instant;
            this.offsetBefore = offsetBefore;
            this.offsetAfter = offsetAfter;
            this.isGap = isGap;
        }
        
        public Instant getInstant() { return instant; }
        public ZoneOffset getOffsetBefore() { return offsetBefore; }
        public ZoneOffset getOffsetAfter() { return offsetAfter; }
        public boolean isGap() { return isGap; }
        public boolean isOverlap() { return !isGap; }
        
        public ZonedDateTime getZonedDateTime() {
            return instant.atZone(IranTimeZone.IRAN);
        }
        
        public PersianDate toPersianDate() {
            return PersianDate.from(instant);
        }
        
        @Override
        public String toString() {
            String type = isGap ? "Gap (spring forward)" : "Overlap (fall back)";
            return String.format("%s at %s: %s -> %s", 
                type, instant, offsetBefore, offsetAfter);
        }
    }
}
