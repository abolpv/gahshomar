package io.github.abolpv.gahshomar.temporal;

import java.time.DayOfWeek;

/**
 * Enum representing days of the week in Persian calendar.
 * In Iran, the week starts on Saturday (Shanbe).
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public enum PersianDayOfWeek {
    
    SHANBE(1, "شنبه", "Shanbe", DayOfWeek.SATURDAY, false),
    YEKSHANBE(2, "یکشنبه", "Yekshanbe", DayOfWeek.SUNDAY, false),
    DOSHANBE(3, "دوشنبه", "Doshanbe", DayOfWeek.MONDAY, false),
    SESHANBE(4, "سه‌شنبه", "Seshanbe", DayOfWeek.TUESDAY, false),
    CHAHARSHANBE(5, "چهارشنبه", "Chaharshanbe", DayOfWeek.WEDNESDAY, false),
    PANJSHANBE(6, "پنج‌شنبه", "Panjshanbe", DayOfWeek.THURSDAY, false),
    JOMEH(7, "جمعه", "Jomeh", DayOfWeek.FRIDAY, true);
    
    private final int value;
    private final String persianName;
    private final String englishName;
    private final DayOfWeek isoDayOfWeek;
    private final boolean weekend;
    
    PersianDayOfWeek(int value, String persianName, String englishName, 
                     DayOfWeek isoDayOfWeek, boolean weekend) {
        this.value = value;
        this.persianName = persianName;
        this.englishName = englishName;
        this.isoDayOfWeek = isoDayOfWeek;
        this.weekend = weekend;
    }
    
    /**
     * Gets the day number (1=Saturday, 7=Friday).
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Gets the Persian name of the day.
     */
    public String getPersianName() {
        return persianName;
    }
    
    /**
     * Gets the English transliteration of the day name.
     */
    public String getEnglishName() {
        return englishName;
    }
    
    /**
     * Gets the corresponding ISO DayOfWeek.
     */
    public DayOfWeek toIsoDayOfWeek() {
        return isoDayOfWeek;
    }
    
    /**
     * Checks if this is a weekend day (Friday in Iran).
     */
    public boolean isWeekend() {
        return weekend;
    }
    
    /**
     * Checks if this is a workday.
     */
    public boolean isWorkday() {
        return !weekend;
    }
    
    /**
     * Gets PersianDayOfWeek from value (1-7).
     */
    public static PersianDayOfWeek of(int day) {
        if (day < 1 || day > 7) {
            throw new IllegalArgumentException("Invalid day of week: " + day);
        }
        return values()[day - 1];
    }
    
    /**
     * Gets PersianDayOfWeek from ISO DayOfWeek.
     */
    public static PersianDayOfWeek from(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case SATURDAY -> SHANBE;
            case SUNDAY -> YEKSHANBE;
            case MONDAY -> DOSHANBE;
            case TUESDAY -> SESHANBE;
            case WEDNESDAY -> CHAHARSHANBE;
            case THURSDAY -> PANJSHANBE;
            case FRIDAY -> JOMEH;
        };
    }
    
    /**
     * Returns the next day of week.
     */
    public PersianDayOfWeek plus(int days) {
        int newValue = ((this.value - 1 + days) % 7 + 7) % 7 + 1;
        return of(newValue);
    }
    
    /**
     * Returns the previous day of week.
     */
    public PersianDayOfWeek minus(int days) {
        return plus(-days);
    }
    
    @Override
    public String toString() {
        return persianName;
    }
}
