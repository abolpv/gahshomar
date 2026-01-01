package io.github.abolpv.gahshomar.temporal;

/**
 * Enum representing Hijri (Islamic/Lunar) months.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public enum HijriMonth {
    
    MUHARRAM(1, "محرم", "Muharram", 30),
    SAFAR(2, "صفر", "Safar", 29),
    RABI_AL_AWWAL(3, "ربیع‌الاول", "Rabi' al-Awwal", 30),
    RABI_AL_THANI(4, "ربیع‌الثانی", "Rabi' al-Thani", 29),
    JUMADA_AL_AWWAL(5, "جمادی‌الاول", "Jumada al-Awwal", 30),
    JUMADA_AL_THANI(6, "جمادی‌الثانی", "Jumada al-Thani", 29),
    RAJAB(7, "رجب", "Rajab", 30),
    SHABAN(8, "شعبان", "Sha'ban", 29),
    RAMADAN(9, "رمضان", "Ramadan", 30),
    SHAWWAL(10, "شوال", "Shawwal", 29),
    DHU_AL_QADAH(11, "ذی‌القعده", "Dhu al-Qi'dah", 30),
    DHU_AL_HIJJAH(12, "ذی‌الحجه", "Dhu al-Hijjah", 29); // 30 in leap year
    
    private final int value;
    private final String arabicName;
    private final String englishName;
    private final int defaultLength;
    
    HijriMonth(int value, String arabicName, String englishName, int defaultLength) {
        this.value = value;
        this.arabicName = arabicName;
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
     * Gets the Arabic/Persian name of the month.
     */
    public String getArabicName() {
        return arabicName;
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
        if (this == DHU_AL_HIJJAH && leapYear) {
            return 30;
        }
        return defaultLength;
    }
    
    /**
     * Gets the HijriMonth from the month number (1-12).
     */
    public static HijriMonth of(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        return values()[month - 1];
    }
    
    /**
     * Checks if this is a sacred month in Islam.
     */
    public boolean isSacredMonth() {
        return this == MUHARRAM || this == RAJAB || 
               this == DHU_AL_QADAH || this == DHU_AL_HIJJAH;
    }
    
    @Override
    public String toString() {
        return arabicName;
    }
}
