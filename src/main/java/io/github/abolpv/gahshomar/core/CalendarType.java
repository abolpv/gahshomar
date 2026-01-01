package io.github.abolpv.gahshomar.core;

/**
 * Enum representing supported calendar types.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public enum CalendarType {
    
    PERSIAN("Persian", "شمسی", "Jalali/Shamsi Solar Calendar"),
    HIJRI("Hijri", "قمری", "Islamic Lunar Calendar"),
    GREGORIAN("Gregorian", "میلادی", "Gregorian Solar Calendar");
    
    private final String englishName;
    private final String persianName;
    private final String description;
    
    CalendarType(String englishName, String persianName, String description) {
        this.englishName = englishName;
        this.persianName = persianName;
        this.description = description;
    }
    
    public String getEnglishName() {
        return englishName;
    }
    
    public String getPersianName() {
        return persianName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isSolar() {
        return this == PERSIAN || this == GREGORIAN;
    }
    
    public boolean isLunar() {
        return this == HIJRI;
    }
    
    @Override
    public String toString() {
        return persianName;
    }
}
