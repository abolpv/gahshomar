package io.github.abolpv.gahshomar.temporal;

import io.github.abolpv.gahshomar.format.PersianNumbers;
import java.util.Objects;

/**
 * Represents an age as years, months, and days.
 * Immutable and thread-safe.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class Age {
    
    private final int years;
    private final int months;
    private final int days;
    
    /**
     * Creates a new Age instance.
     */
    public Age(int years, int months, int days) {
        if (years < 0 || months < 0 || days < 0) {
            throw new IllegalArgumentException("Age components cannot be negative");
        }
        this.years = years;
        this.months = months;
        this.days = days;
    }
    
    /**
     * Creates an Age of zero.
     */
    public static Age zero() {
        return new Age(0, 0, 0);
    }
    
    /**
     * Creates an Age from years only.
     */
    public static Age ofYears(int years) {
        return new Age(years, 0, 0);
    }
    
    /**
     * Creates an Age from years and months.
     */
    public static Age of(int years, int months) {
        return new Age(years, months, 0);
    }
    
    /**
     * Creates an Age from years, months, and days.
     */
    public static Age of(int years, int months, int days) {
        return new Age(years, months, days);
    }
    
    /**
     * Gets the years component.
     */
    public int getYears() {
        return years;
    }
    
    /**
     * Gets the months component (0-11).
     */
    public int getMonths() {
        return months;
    }
    
    /**
     * Gets the days component (0-30).
     */
    public int getDays() {
        return days;
    }
    
    /**
     * Gets total months (years * 12 + months).
     */
    public int getTotalMonths() {
        return years * 12 + months;
    }
    
    /**
     * Gets approximate total days.
     */
    public int getTotalDays() {
        return years * 365 + months * 30 + days;
    }
    
    /**
     * Checks if this age is zero.
     */
    public boolean isZero() {
        return years == 0 && months == 0 && days == 0;
    }
    
    /**
     * Returns Persian formatted string.
     * Example: "۳۳ سال و ۵ ماه و ۱۰ روز"
     */
    public String toPersianString() {
        StringBuilder sb = new StringBuilder();
        
        if (years > 0) {
            sb.append(PersianNumbers.toPersian(years)).append(" سال");
        }
        
        if (months > 0) {
            if (sb.length() > 0) sb.append(" و ");
            sb.append(PersianNumbers.toPersian(months)).append(" ماه");
        }
        
        if (days > 0 || sb.length() == 0) {
            if (sb.length() > 0) sb.append(" و ");
            sb.append(PersianNumbers.toPersian(days)).append(" روز");
        }
        
        return sb.toString();
    }
    
    /**
     * Returns English formatted string.
     * Example: "33 years, 5 months, and 10 days"
     */
    public String toEnglishString() {
        StringBuilder sb = new StringBuilder();
        
        if (years > 0) {
            sb.append(years).append(years == 1 ? " year" : " years");
        }
        
        if (months > 0) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(months).append(months == 1 ? " month" : " months");
        }
        
        if (days > 0 || sb.length() == 0) {
            if (sb.length() > 0) sb.append(", and ");
            sb.append(days).append(days == 1 ? " day" : " days");
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return toPersianString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Age age = (Age) o;
        return years == age.years && months == age.months && days == age.days;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(years, months, days);
    }
}
