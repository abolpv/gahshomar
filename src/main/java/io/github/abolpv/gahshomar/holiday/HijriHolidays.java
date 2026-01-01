package io.github.abolpv.gahshomar.holiday;

import io.github.abolpv.gahshomar.HijriDate;
import io.github.abolpv.gahshomar.holiday.Holiday.HolidayType;

import java.util.*;

/**
 * Provider for Hijri (Islamic) holidays.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class HijriHolidays {
    
    private final int hijriYear;
    private List<HijriHoliday> cachedHolidays;
    
    private HijriHolidays(int hijriYear) {
        this.hijriYear = hijriYear;
    }
    
    /**
     * Creates a HijriHolidays instance for the specified Hijri year.
     */
    public static HijriHolidays of(int hijriYear) {
        return new HijriHolidays(hijriYear);
    }
    
    /**
     * Gets all Hijri holidays for this year.
     */
    public List<HijriHoliday> getAll() {
        if (cachedHolidays == null) {
            cachedHolidays = createHolidays();
        }
        return Collections.unmodifiableList(cachedHolidays);
    }
    
    private List<HijriHoliday> createHolidays() {
        List<HijriHoliday> holidays = new ArrayList<>();
        
        // Muharram
        holidays.add(new HijriHoliday(hijriYear, 1, 1, "رأس السنة الهجرية", "Islamic New Year", HolidayType.RELIGIOUS, false));
        holidays.add(new HijriHoliday(hijriYear, 1, 9, "تاسوعا", "Tasua", HolidayType.RELIGIOUS, true));
        holidays.add(new HijriHoliday(hijriYear, 1, 10, "عاشورا", "Ashura", HolidayType.RELIGIOUS, true));
        
        // Safar
        holidays.add(new HijriHoliday(hijriYear, 2, 20, "اربعین", "Arbaeen", HolidayType.RELIGIOUS, true));
        holidays.add(new HijriHoliday(hijriYear, 2, 28, "رحلت پیامبر", "Prophet's Demise", HolidayType.RELIGIOUS, true));
        
        // Rabi al-Awwal
        holidays.add(new HijriHoliday(hijriYear, 3, 12, "میلاد النبی", "Prophet's Birthday (Sunni)", HolidayType.RELIGIOUS, false));
        holidays.add(new HijriHoliday(hijriYear, 3, 17, "میلاد پیامبر و امام صادق", "Prophet's Birthday (Shia)", HolidayType.RELIGIOUS, true));
        
        // Rajab
        holidays.add(new HijriHoliday(hijriYear, 7, 13, "میلاد امام علی", "Birth of Imam Ali", HolidayType.RELIGIOUS, true));
        holidays.add(new HijriHoliday(hijriYear, 7, 27, "مبعث", "Isra and Mi'raj", HolidayType.RELIGIOUS, true));
        
        // Sha'ban
        holidays.add(new HijriHoliday(hijriYear, 8, 15, "نیمه شعبان", "Mid-Sha'ban", HolidayType.RELIGIOUS, true));
        
        // Ramadan
        holidays.add(new HijriHoliday(hijriYear, 9, 1, "آغاز ماه رمضان", "Start of Ramadan", HolidayType.RELIGIOUS, false));
        holidays.add(new HijriHoliday(hijriYear, 9, 21, "شهادت امام علی", "Martyrdom of Imam Ali", HolidayType.RELIGIOUS, true));
        holidays.add(new HijriHoliday(hijriYear, 9, 27, "شب قدر", "Laylat al-Qadr", HolidayType.RELIGIOUS, false));
        
        // Shawwal
        holidays.add(new HijriHoliday(hijriYear, 10, 1, "عید فطر", "Eid al-Fitr", HolidayType.RELIGIOUS, true));
        holidays.add(new HijriHoliday(hijriYear, 10, 2, "عید فطر", "Eid al-Fitr (Day 2)", HolidayType.RELIGIOUS, true));
        holidays.add(new HijriHoliday(hijriYear, 10, 25, "شهادت امام صادق", "Martyrdom of Imam Sadiq", HolidayType.RELIGIOUS, true));
        
        // Dhu al-Hijjah
        holidays.add(new HijriHoliday(hijriYear, 12, 9, "روز عرفه", "Day of Arafah", HolidayType.RELIGIOUS, false));
        holidays.add(new HijriHoliday(hijriYear, 12, 10, "عید قربان", "Eid al-Adha", HolidayType.RELIGIOUS, true));
        holidays.add(new HijriHoliday(hijriYear, 12, 18, "عید غدیر", "Eid al-Ghadir", HolidayType.RELIGIOUS, true));
        
        return holidays;
    }
    
    /**
     * Gets only official holidays (recognized in Iran).
     */
    public List<HijriHoliday> getOfficialHolidays() {
        return getAll().stream()
            .filter(HijriHoliday::isOfficial)
            .toList();
    }
    
    /**
     * Checks if a Hijri date is a holiday.
     */
    public boolean isHoliday(HijriDate date) {
        return getAll().stream()
            .anyMatch(h -> h.getMonth() == date.getMonthValue() && h.getDay() == date.getDayOfMonth());
    }
    
    /**
     * Gets the holiday for a specific Hijri date.
     */
    public Optional<HijriHoliday> getHoliday(HijriDate date) {
        return getAll().stream()
            .filter(h -> h.getMonth() == date.getMonthValue() && h.getDay() == date.getDayOfMonth())
            .findFirst();
    }
    
    /**
     * Gets holidays for a specific Hijri month.
     */
    public List<HijriHoliday> getHolidaysInMonth(int month) {
        return getAll().stream()
            .filter(h -> h.getMonth() == month)
            .toList();
    }
    
    /**
     * Gets Ramadan holidays.
     */
    public List<HijriHoliday> getRamadanHolidays() {
        return getHolidaysInMonth(9);
    }
    
    /**
     * Represents a Hijri holiday.
     */
    public static class HijriHoliday {
        private final int year;
        private final int month;
        private final int day;
        private final String persianName;
        private final String englishName;
        private final HolidayType type;
        private final boolean official;
        
        public HijriHoliday(int year, int month, int day, String persianName, 
                          String englishName, HolidayType type, boolean official) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.persianName = persianName;
            this.englishName = englishName;
            this.type = type;
            this.official = official;
        }
        
        public int getYear() { return year; }
        public int getMonth() { return month; }
        public int getDay() { return day; }
        public String getPersianName() { return persianName; }
        public String getEnglishName() { return englishName; }
        public HolidayType getType() { return type; }
        public boolean isOfficial() { return official; }
        
        public HijriDate toHijriDate() {
            return HijriDate.of(year, month, day);
        }
        
        @Override
        public String toString() {
            return persianName + " (" + year + "/" + month + "/" + day + ")";
        }
    }
}
