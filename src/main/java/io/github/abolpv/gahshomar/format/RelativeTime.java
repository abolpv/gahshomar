package io.github.abolpv.gahshomar.format;

import io.github.abolpv.gahshomar.PersianDate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Formats dates and times as relative strings (e.g., "۲ روز پیش").
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class RelativeTime {
    
    private RelativeTime() {
        // Utility class
    }
    
    /**
     * Formats a Persian date relative to today.
     */
    public static String format(PersianDate date) {
        return format(date, PersianDate.now());
    }
    
    /**
     * Formats a Persian date relative to a reference date.
     */
    public static String format(PersianDate date, PersianDate reference) {
        long daysDiff = reference.toEpochDay() - date.toEpochDay();
        
        // Today
        if (daysDiff == 0) return "امروز";
        
        // Yesterday/Tomorrow
        if (daysDiff == 1) return "دیروز";
        if (daysDiff == -1) return "فردا";
        
        // Day before yesterday / Day after tomorrow
        if (daysDiff == 2) return "پریروز";
        if (daysDiff == -2) return "پس‌فردا";
        
        // Past
        if (daysDiff > 0) {
            if (daysDiff < 7) {
                return PersianNumbers.toPersian(daysDiff) + " روز پیش";
            }
            if (daysDiff < 30) {
                long weeks = daysDiff / 7;
                return PersianNumbers.toPersian(weeks) + " هفته پیش";
            }
            if (daysDiff < 365) {
                long months = daysDiff / 30;
                return PersianNumbers.toPersian(months) + " ماه پیش";
            }
            long years = daysDiff / 365;
            return PersianNumbers.toPersian(years) + " سال پیش";
        }
        
        // Future
        daysDiff = -daysDiff;
        if (daysDiff < 7) {
            return PersianNumbers.toPersian(daysDiff) + " روز دیگر";
        }
        if (daysDiff < 30) {
            long weeks = daysDiff / 7;
            return PersianNumbers.toPersian(weeks) + " هفته دیگر";
        }
        if (daysDiff < 365) {
            long months = daysDiff / 30;
            return PersianNumbers.toPersian(months) + " ماه دیگر";
        }
        long years = daysDiff / 365;
        return PersianNumbers.toPersian(years) + " سال دیگر";
    }
    
    /**
     * Formats an instant relative to now.
     */
    public static String format(Instant instant) {
        return format(instant, Instant.now());
    }
    
    /**
     * Formats an instant relative to a reference instant.
     */
    public static String format(Instant instant, Instant reference) {
        long seconds = ChronoUnit.SECONDS.between(instant, reference);
        
        if (Math.abs(seconds) < 60) {
            return "همین الان";
        }
        
        long minutes = seconds / 60;
        if (Math.abs(minutes) < 60) {
            if (minutes > 0) {
                return PersianNumbers.toPersian(minutes) + " دقیقه پیش";
            } else {
                return PersianNumbers.toPersian(-minutes) + " دقیقه دیگر";
            }
        }
        
        long hours = minutes / 60;
        if (Math.abs(hours) < 24) {
            if (hours > 0) {
                return PersianNumbers.toPersian(hours) + " ساعت پیش";
            } else {
                return PersianNumbers.toPersian(-hours) + " ساعت دیگر";
            }
        }
        
        // Fall back to date-based formatting
        PersianDate date = PersianDate.from(instant);
        PersianDate refDate = PersianDate.from(reference);
        return format(date, refDate);
    }
    
    /**
     * Formats epoch milliseconds relative to now.
     */
    public static String format(long epochMilli) {
        return format(Instant.ofEpochMilli(epochMilli));
    }
    
    /**
     * Returns a short relative string (e.g., "۲ر" for 2 days ago).
     */
    public static String formatShort(PersianDate date) {
        PersianDate today = PersianDate.now();
        long daysDiff = today.toEpochDay() - date.toEpochDay();
        
        if (daysDiff == 0) return "امروز";
        if (daysDiff == 1) return "دیروز";
        if (daysDiff == -1) return "فردا";
        
        if (daysDiff > 0) {
            if (daysDiff < 7) return PersianNumbers.toPersian(daysDiff) + "ر";
            if (daysDiff < 30) return PersianNumbers.toPersian(daysDiff / 7) + "ه";
            if (daysDiff < 365) return PersianNumbers.toPersian(daysDiff / 30) + "م";
            return PersianNumbers.toPersian(daysDiff / 365) + "س";
        } else {
            daysDiff = -daysDiff;
            if (daysDiff < 7) return "+" + PersianNumbers.toPersian(daysDiff) + "ر";
            if (daysDiff < 30) return "+" + PersianNumbers.toPersian(daysDiff / 7) + "ه";
            if (daysDiff < 365) return "+" + PersianNumbers.toPersian(daysDiff / 30) + "م";
            return "+" + PersianNumbers.toPersian(daysDiff / 365) + "س";
        }
    }
    
    /**
     * Returns English relative string.
     */
    public static String formatEnglish(PersianDate date) {
        PersianDate today = PersianDate.now();
        long daysDiff = today.toEpochDay() - date.toEpochDay();
        
        if (daysDiff == 0) return "today";
        if (daysDiff == 1) return "yesterday";
        if (daysDiff == -1) return "tomorrow";
        
        if (daysDiff > 0) {
            if (daysDiff < 7) return daysDiff + " days ago";
            if (daysDiff < 30) return (daysDiff / 7) + " weeks ago";
            if (daysDiff < 365) return (daysDiff / 30) + " months ago";
            return (daysDiff / 365) + " years ago";
        } else {
            daysDiff = -daysDiff;
            if (daysDiff < 7) return "in " + daysDiff + " days";
            if (daysDiff < 30) return "in " + (daysDiff / 7) + " weeks";
            if (daysDiff < 365) return "in " + (daysDiff / 30) + " months";
            return "in " + (daysDiff / 365) + " years";
        }
    }
}
