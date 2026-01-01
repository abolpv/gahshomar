package io.github.abolpv.gahshomar.holiday;

import io.github.abolpv.gahshomar.PersianDate;

import java.util.Objects;

/**
 * Represents a holiday with a date and description.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class Holiday {
    
    private final PersianDate date;
    private final String name;
    private final String description;
    private final boolean official;
    private final HolidayType type;
    
    public Holiday(PersianDate date, String name, String description, boolean official, HolidayType type) {
        this.date = Objects.requireNonNull(date);
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.official = official;
        this.type = type;
    }
    
    public Holiday(PersianDate date, String name, boolean official) {
        this(date, name, null, official, HolidayType.OTHER);
    }
    
    public PersianDate getDate() {
        return date;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isOfficial() {
        return official;
    }
    
    public HolidayType getType() {
        return type;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Holiday holiday = (Holiday) obj;
        return date.equals(holiday.date) && name.equals(holiday.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(date, name);
    }
    
    @Override
    public String toString() {
        return name + " (" + date.format() + ")";
    }
    
    /**
     * Type of holiday.
     */
    public enum HolidayType {
        NATIONAL,
        RELIGIOUS,
        INTERNATIONAL,
        OTHER
    }
}
