package io.github.abolpv.gahshomar.range;

import io.github.abolpv.gahshomar.PersianDate;
import io.github.abolpv.gahshomar.format.PersianNumbers;

import java.io.Serializable;
import java.util.Objects;

/**
 * A date-based amount of time in the Persian calendar.
 * Models a quantity of time in terms of years, months, and days.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class Period implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final Period ZERO = new Period(0, 0, 0);
    
    private final int years;
    private final int months;
    private final int days;
    
    private Period(int years, int months, int days) {
        this.years = years;
        this.months = months;
        this.days = days;
    }
    
    // ==================== Factory Methods ====================
    
    /**
     * Creates a Period from years, months, and days.
     */
    public static Period of(int years, int months, int days) {
        return new Period(years, months, days);
    }
    
    /**
     * Creates a Period of years.
     */
    public static Period ofYears(int years) {
        return new Period(years, 0, 0);
    }
    
    /**
     * Creates a Period of months.
     */
    public static Period ofMonths(int months) {
        return new Period(0, months, 0);
    }
    
    /**
     * Creates a Period of days.
     */
    public static Period ofDays(int days) {
        return new Period(0, 0, days);
    }
    
    /**
     * Creates a Period of weeks (as days).
     */
    public static Period ofWeeks(int weeks) {
        return new Period(0, 0, weeks * 7);
    }
    
    /**
     * Calculates the Period between two Persian dates.
     */
    public static Period between(PersianDate startInclusive, PersianDate endExclusive) {
        int years = endExclusive.getYear() - startInclusive.getYear();
        int months = endExclusive.getMonthValue() - startInclusive.getMonthValue();
        int days = endExclusive.getDayOfMonth() - startInclusive.getDayOfMonth();
        
        if (days < 0) {
            months--;
            days += startInclusive.lengthOfMonth();
        }
        
        if (months < 0) {
            years--;
            months += 12;
        }
        
        return new Period(years, months, days);
    }
    
    /**
     * Parses a string like "P2Y3M4D" or "2 years, 3 months, 4 days".
     */
    public static Period parse(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
        
        text = text.trim().toUpperCase();
        
        // ISO-8601 format: P2Y3M4D
        if (text.startsWith("P")) {
            int years = 0, months = 0, days = 0;
            text = text.substring(1);
            
            int yIdx = text.indexOf('Y');
            if (yIdx >= 0) {
                years = Integer.parseInt(text.substring(0, yIdx));
                text = text.substring(yIdx + 1);
            }
            
            int mIdx = text.indexOf('M');
            if (mIdx >= 0) {
                months = Integer.parseInt(text.substring(0, mIdx));
                text = text.substring(mIdx + 1);
            }
            
            int dIdx = text.indexOf('D');
            if (dIdx >= 0) {
                days = Integer.parseInt(text.substring(0, dIdx));
            }
            
            return new Period(years, months, days);
        }
        
        throw new IllegalArgumentException("Cannot parse period: " + text);
    }
    
    // ==================== Getters ====================
    
    public int getYears() {
        return years;
    }
    
    public int getMonths() {
        return months;
    }
    
    public int getDays() {
        return days;
    }
    
    /**
     * Gets total months (years * 12 + months).
     */
    public long toTotalMonths() {
        return years * 12L + months;
    }
    
    /**
     * Checks if this period is zero.
     */
    public boolean isZero() {
        return years == 0 && months == 0 && days == 0;
    }
    
    /**
     * Checks if this period is negative.
     */
    public boolean isNegative() {
        return years < 0 || months < 0 || days < 0;
    }
    
    // ==================== Operations ====================
    
    /**
     * Returns a copy with the specified years added.
     */
    public Period plusYears(int years) {
        return new Period(this.years + years, months, days);
    }
    
    /**
     * Returns a copy with the specified months added.
     */
    public Period plusMonths(int months) {
        return new Period(years, this.months + months, days);
    }
    
    /**
     * Returns a copy with the specified days added.
     */
    public Period plusDays(int days) {
        return new Period(years, months, this.days + days);
    }
    
    /**
     * Returns a copy with the specified years subtracted.
     */
    public Period minusYears(int years) {
        return plusYears(-years);
    }
    
    /**
     * Returns a copy with the specified months subtracted.
     */
    public Period minusMonths(int months) {
        return plusMonths(-months);
    }
    
    /**
     * Returns a copy with the specified days subtracted.
     */
    public Period minusDays(int days) {
        return plusDays(-days);
    }
    
    /**
     * Returns a copy multiplied by the scalar.
     */
    public Period multipliedBy(int scalar) {
        return new Period(years * scalar, months * scalar, days * scalar);
    }
    
    /**
     * Returns a copy with the years, months, and days negated.
     */
    public Period negated() {
        return multipliedBy(-1);
    }
    
    /**
     * Returns a normalized period.
     */
    public Period normalized() {
        long totalMonths = toTotalMonths();
        int newYears = (int) (totalMonths / 12);
        int newMonths = (int) (totalMonths % 12);
        return new Period(newYears, newMonths, days);
    }
    
    /**
     * Adds this period to a Persian date.
     */
    public PersianDate addTo(PersianDate date) {
        return date.plusYears(years).plusMonths(months).plusDays(days);
    }
    
    /**
     * Subtracts this period from a Persian date.
     */
    public PersianDate subtractFrom(PersianDate date) {
        return date.minusYears(years).minusMonths(months).minusDays(days);
    }
    
    // ==================== Formatting ====================
    
    /**
     * Returns ISO-8601 format (P2Y3M4D).
     */
    public String toIsoString() {
        StringBuilder sb = new StringBuilder("P");
        if (years != 0) sb.append(years).append("Y");
        if (months != 0) sb.append(months).append("M");
        if (days != 0) sb.append(days).append("D");
        if (sb.length() == 1) sb.append("0D");
        return sb.toString();
    }
    
    /**
     * Returns Persian formatted string.
     */
    public String toPersianString() {
        StringBuilder sb = new StringBuilder();
        
        if (years != 0) {
            sb.append(PersianNumbers.toPersian(Math.abs(years))).append(" سال");
        }
        if (months != 0) {
            if (sb.length() > 0) sb.append(" و ");
            sb.append(PersianNumbers.toPersian(Math.abs(months))).append(" ماه");
        }
        if (days != 0 || sb.length() == 0) {
            if (sb.length() > 0) sb.append(" و ");
            sb.append(PersianNumbers.toPersian(Math.abs(days))).append(" روز");
        }
        
        return sb.toString();
    }
    
    // ==================== Object Methods ====================
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Period period = (Period) obj;
        return years == period.years && months == period.months && days == period.days;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(years, months, days);
    }
    
    @Override
    public String toString() {
        return toIsoString();
    }
}
