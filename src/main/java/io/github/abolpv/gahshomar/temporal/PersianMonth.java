package io.github.abolpv.gahshomar.temporal;

/**
 * Enum representing Persian (Jalali/Shamsi) months.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public enum PersianMonth {
    
    FARVARDIN(1, "فروردین", "Farvardin", 31),
    ORDIBEHESHT(2, "اردیبهشت", "Ordibehesht", 31),
    KHORDAD(3, "خرداد", "Khordad", 31),
    TIR(4, "تیر", "Tir", 31),
    MORDAD(5, "مرداد", "Mordad", 31),
    SHAHRIVAR(6, "شهریور", "Shahrivar", 31),
    MEHR(7, "مهر", "Mehr", 30),
    ABAN(8, "آبان", "Aban", 30),
    AZAR(9, "آذر", "Azar", 30),
    DEY(10, "دی", "Dey", 30),
    BAHMAN(11, "بهمن", "Bahman", 30),
    ESFAND(12, "اسفند", "Esfand", 29); // 30 in leap year
    
    private final int value;
    private final String persianName;
    private final String englishName;
    private final int defaultLength;
    
    PersianMonth(int value, String persianName, String englishName, int defaultLength) {
        this.value = value;
        this.persianName = persianName;
        this.englishName = englishName;
        this.defaultLength = defaultLength;
    }
    
    /**
     * Gets the month number (1-12).
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Gets the Persian name of the month.
     */
    public String getPersianName() {
        return persianName;
    }
    
    /**
     * Gets the English transliteration of the month name.
     */
    public String getEnglishName() {
        return englishName;
    }
    
    /**
     * Gets the default number of days in this month (non-leap year).
     */
    public int getDefaultLength() {
        return defaultLength;
    }
    
    /**
     * Gets the number of days in this month for the given year.
     */
    public int length(boolean leapYear) {
        if (this == ESFAND && leapYear) {
            return 30;
        }
        return defaultLength;
    }
    
    /**
     * Gets the PersianMonth from the month number (1-12).
     */
    public static PersianMonth of(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        return values()[month - 1];
    }
    
    /**
     * Gets the first day of year for this month (1-based).
     */
    public int getFirstDayOfYear() {
        int day = 1;
        for (int i = 0; i < value - 1; i++) {
            day += values()[i].defaultLength;
        }
        return day;
    }
    
    /**
     * Gets the quarter of the year (1-4).
     */
    public int getQuarter() {
        return (value - 1) / 3 + 1;
    }
    
    /**
     * Returns the season for this month.
     */
    public Season getSeason() {
        return switch (this) {
            case FARVARDIN, ORDIBEHESHT, KHORDAD -> Season.SPRING;
            case TIR, MORDAD, SHAHRIVAR -> Season.SUMMER;
            case MEHR, ABAN, AZAR -> Season.AUTUMN;
            case DEY, BAHMAN, ESFAND -> Season.WINTER;
        };
    }
    
    @Override
    public String toString() {
        return persianName;
    }
}
