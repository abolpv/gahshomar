package io.github.abolpv.gahshomar.holiday;

import io.github.abolpv.gahshomar.PersianDate;

import java.util.Objects;

/**
 * Represents an occasion or event (not necessarily a holiday).
 * Examples: International Women's Day, Mother's Day, etc.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class Occasion {
    
    private final PersianDate date;
    private final String name;
    private final String description;
    private final OccasionType type;
    private final boolean recurring;
    
    public Occasion(PersianDate date, String name, String description, OccasionType type, boolean recurring) {
        this.date = Objects.requireNonNull(date);
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.type = type;
        this.recurring = recurring;
    }
    
    public Occasion(PersianDate date, String name, OccasionType type) {
        this(date, name, null, type, true);
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
    
    public OccasionType getType() {
        return type;
    }
    
    public boolean isRecurring() {
        return recurring;
    }
    
    /**
     * Gets the next occurrence of this occasion after today.
     */
    public PersianDate getNextOccurrence() {
        if (!recurring) {
            return date;
        }
        
        PersianDate today = PersianDate.now();
        PersianDate thisYear = PersianDate.of(today.getYear(), date.getMonthValue(), date.getDayOfMonth());
        
        if (thisYear.isAfter(today) || thisYear.isEqual(today)) {
            return thisYear;
        } else {
            return PersianDate.of(today.getYear() + 1, date.getMonthValue(), date.getDayOfMonth());
        }
    }
    
    /**
     * Gets days until next occurrence.
     */
    public long daysUntilNext() {
        return Math.abs(getNextOccurrence().daysUntil(PersianDate.now()));
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Occasion occasion = (Occasion) obj;
        return date.equals(occasion.date) && name.equals(occasion.name);
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
     * Type of occasion.
     */
    public enum OccasionType {
        NATIONAL("ملی"),
        INTERNATIONAL("بین‌المللی"),
        RELIGIOUS("مذهبی"),
        CULTURAL("فرهنگی"),
        MEMORIAL("بزرگداشت"),
        AWARENESS("آگاهی‌بخشی"),
        OTHER("سایر");
        
        private final String persianName;
        
        OccasionType(String persianName) {
            this.persianName = persianName;
        }
        
        public String getPersianName() {
            return persianName;
        }
    }
}
