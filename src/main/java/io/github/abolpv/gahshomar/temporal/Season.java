package io.github.abolpv.gahshomar.temporal;

/**
 * Enum representing seasons of the year.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public enum Season {
    
    SPRING("بهار", "Spring", 1),
    SUMMER("تابستان", "Summer", 2),
    AUTUMN("پاییز", "Autumn", 3),
    WINTER("زمستان", "Winter", 4);
    
    private final String persianName;
    private final String englishName;
    private final int value;
    
    Season(String persianName, String englishName, int value) {
        this.persianName = persianName;
        this.englishName = englishName;
        this.value = value;
    }
    
    /**
     * Gets the Persian name of the season.
     */
    public String getPersianName() {
        return persianName;
    }
    
    /**
     * Gets the English name of the season.
     */
    public String getEnglishName() {
        return englishName;
    }
    
    /**
     * Gets the season number (1-4).
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Gets the Season from value (1-4).
     */
    public static Season of(int value) {
        if (value < 1 || value > 4) {
            throw new IllegalArgumentException("Invalid season value: " + value);
        }
        return values()[value - 1];
    }
    
    /**
     * Gets the Season from Persian month.
     */
    public static Season fromPersianMonth(int month) {
        return switch ((month - 1) / 3) {
            case 0 -> SPRING;
            case 1 -> SUMMER;
            case 2 -> AUTUMN;
            case 3 -> WINTER;
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
    }
    
    /**
     * Gets the first month of this season (1-12).
     */
    public int getFirstMonth() {
        return (value - 1) * 3 + 1;
    }
    
    /**
     * Gets the last month of this season (1-12).
     */
    public int getLastMonth() {
        return value * 3;
    }
    
    /**
     * Returns the next season.
     */
    public Season next() {
        return values()[(ordinal() + 1) % 4];
    }
    
    /**
     * Returns the previous season.
     */
    public Season previous() {
        return values()[(ordinal() + 3) % 4];
    }
    
    @Override
    public String toString() {
        return persianName;
    }
}
