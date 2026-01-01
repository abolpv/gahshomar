package io.github.abolpv.gahshomar.holiday;

import io.github.abolpv.gahshomar.PersianDate;
import io.github.abolpv.gahshomar.HijriDate;
import io.github.abolpv.gahshomar.holiday.Holiday.HolidayType;

import java.util.*;

/**
 * Provider for Persian (Iranian) holidays.
 * Includes both solar (Shamsi) and lunar (Hijri) holidays.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class PersianHolidays {
    
    private final int year;
    private List<Holiday> cachedHolidays;
    
    private PersianHolidays(int year) {
        this.year = year;
    }
    
    /**
     * Creates a PersianHolidays instance for the specified year.
     */
    public static PersianHolidays of(int year) {
        return new PersianHolidays(year);
    }
    
    /**
     * Gets all holidays for this year.
     */
    public List<Holiday> getAll() {
        if (cachedHolidays == null) {
            cachedHolidays = new ArrayList<>();
            cachedHolidays.addAll(getSolarHolidays());
            cachedHolidays.addAll(getLunarHolidays());
            cachedHolidays.sort(Comparator.comparing(Holiday::getDate));
        }
        return Collections.unmodifiableList(cachedHolidays);
    }
    
    /**
     * Gets solar (Shamsi) holidays.
     */
    public List<Holiday> getSolarHolidays() {
        List<Holiday> holidays = new ArrayList<>();
        
        // Nowruz holidays (1-4 Farvardin)
        holidays.add(new Holiday(PersianDate.of(year, 1, 1), "نوروز", "آغاز سال نو", true, HolidayType.NATIONAL));
        holidays.add(new Holiday(PersianDate.of(year, 1, 2), "تعطیلات نوروز", null, true, HolidayType.NATIONAL));
        holidays.add(new Holiday(PersianDate.of(year, 1, 3), "تعطیلات نوروز", null, true, HolidayType.NATIONAL));
        holidays.add(new Holiday(PersianDate.of(year, 1, 4), "تعطیلات نوروز", null, true, HolidayType.NATIONAL));
        
        // 12 Farvardin - Islamic Republic Day
        holidays.add(new Holiday(PersianDate.of(year, 1, 12), "روز جمهوری اسلامی", null, true, HolidayType.NATIONAL));
        
        // 13 Farvardin - Nature Day (Sizdah Bedar)
        holidays.add(new Holiday(PersianDate.of(year, 1, 13), "روز طبیعت", "سیزده‌بدر", true, HolidayType.NATIONAL));
        
        // 14 Khordad - Demise of Imam Khomeini
        holidays.add(new Holiday(PersianDate.of(year, 3, 14), "رحلت امام خمینی", null, true, HolidayType.NATIONAL));
        
        // 15 Khordad - 15 Khordad Uprising
        holidays.add(new Holiday(PersianDate.of(year, 3, 15), "قیام ۱۵ خرداد", null, true, HolidayType.NATIONAL));
        
        // 22 Bahman - Islamic Revolution Day
        holidays.add(new Holiday(PersianDate.of(year, 11, 22), "پیروزی انقلاب اسلامی", null, true, HolidayType.NATIONAL));
        
        // 29 Esfand - Nationalization of Oil Industry
        holidays.add(new Holiday(PersianDate.of(year, 12, 29), "ملی شدن صنعت نفت", null, true, HolidayType.NATIONAL));
        
        // Yalda Night (30 Azar)
        holidays.add(new Holiday(PersianDate.of(year, 9, 30), "شب یلدا", "بلندترین شب سال", false, HolidayType.NATIONAL));
        
        return holidays;
    }
    
    /**
     * Gets lunar (Hijri) holidays converted to Persian dates.
     * Note: These dates are approximate and may vary by 1-2 days.
     */
    public List<Holiday> getLunarHolidays() {
        List<Holiday> holidays = new ArrayList<>();
        
        // Get the approximate Hijri year for this Persian year
        PersianDate startOfYear = PersianDate.of(year, 1, 1);
        HijriDate hijriStart = startOfYear.toHijri();
        int hijriYear = hijriStart.getYear();
        
        // Add lunar holidays for both possible Hijri years in this Persian year
        addLunarHolidaysForYear(holidays, hijriYear);
        addLunarHolidaysForYear(holidays, hijriYear + 1);
        
        // Filter to only include holidays that fall in this Persian year
        holidays.removeIf(h -> h.getDate().getYear() != year);
        
        return holidays;
    }
    
    private void addLunarHolidaysForYear(List<Holiday> holidays, int hijriYear) {
        // Tasua and Ashura (9-10 Muharram)
        addHijriHoliday(holidays, hijriYear, 1, 9, "تاسوعای حسینی", true, HolidayType.RELIGIOUS);
        addHijriHoliday(holidays, hijriYear, 1, 10, "عاشورای حسینی", true, HolidayType.RELIGIOUS);
        
        // Arbaeen (20 Safar)
        addHijriHoliday(holidays, hijriYear, 2, 20, "اربعین حسینی", true, HolidayType.RELIGIOUS);
        
        // Demise of Prophet & Martyrdom of Imam Hassan (28 Safar)
        addHijriHoliday(holidays, hijriYear, 2, 28, "رحلت پیامبر و شهادت امام حسن", true, HolidayType.RELIGIOUS);
        
        // Martyrdom of Imam Reza (last day of Safar)
        addHijriHoliday(holidays, hijriYear, 2, 29, "شهادت امام رضا", true, HolidayType.RELIGIOUS);
        
        // Birthday of Prophet & Imam Sadiq (17 Rabi al-Awwal)
        addHijriHoliday(holidays, hijriYear, 3, 17, "میلاد پیامبر و امام صادق", true, HolidayType.RELIGIOUS);
        
        // Martyrdom of Fatimah (3 Jumada al-Thani)
        addHijriHoliday(holidays, hijriYear, 6, 3, "شهادت حضرت فاطمه", true, HolidayType.RELIGIOUS);
        
        // Birthday of Imam Ali (13 Rajab)
        addHijriHoliday(holidays, hijriYear, 7, 13, "میلاد امام علی", true, HolidayType.RELIGIOUS);
        
        // Mab'ath (27 Rajab)
        addHijriHoliday(holidays, hijriYear, 7, 27, "مبعث پیامبر", true, HolidayType.RELIGIOUS);
        
        // Birthday of Imam Mahdi (15 Sha'ban)
        addHijriHoliday(holidays, hijriYear, 8, 15, "میلاد امام زمان", true, HolidayType.RELIGIOUS);
        
        // Martyrdom of Imam Ali (21 Ramadan)
        addHijriHoliday(holidays, hijriYear, 9, 21, "شهادت امام علی", true, HolidayType.RELIGIOUS);
        
        // Eid al-Fitr (1-2 Shawwal)
        addHijriHoliday(holidays, hijriYear, 10, 1, "عید فطر", true, HolidayType.RELIGIOUS);
        addHijriHoliday(holidays, hijriYear, 10, 2, "تعطیلات عید فطر", true, HolidayType.RELIGIOUS);
        
        // Martyrdom of Imam Sadiq (25 Shawwal)
        addHijriHoliday(holidays, hijriYear, 10, 25, "شهادت امام صادق", true, HolidayType.RELIGIOUS);
        
        // Eid al-Adha (10 Dhu al-Hijjah)
        addHijriHoliday(holidays, hijriYear, 12, 10, "عید قربان", true, HolidayType.RELIGIOUS);
        
        // Eid al-Ghadir (18 Dhu al-Hijjah)
        addHijriHoliday(holidays, hijriYear, 12, 18, "عید غدیر", true, HolidayType.RELIGIOUS);
    }
    
    private void addHijriHoliday(List<Holiday> holidays, int hijriYear, int hijriMonth, int hijriDay, 
                                  String name, boolean official, HolidayType type) {
        try {
            HijriDate hijriDate = HijriDate.of(hijriYear, hijriMonth, hijriDay);
            PersianDate persianDate = hijriDate.toPersian();
            holidays.add(new Holiday(persianDate, name, null, official, type));
        } catch (Exception e) {
            // Invalid date, skip
        }
    }
    
    /**
     * Gets Nowruz holidays.
     */
    public List<Holiday> getNowruz() {
        return getAll().stream()
            .filter(h -> h.getDate().getMonthValue() == 1 && h.getDate().getDayOfMonth() <= 13)
            .toList();
    }
    
    /**
     * Gets only official holidays.
     */
    public List<Holiday> getOfficialHolidays() {
        return getAll().stream()
            .filter(Holiday::isOfficial)
            .toList();
    }
    
    /**
     * Checks if the given date is a holiday.
     */
    public boolean isHoliday(PersianDate date) {
        return getAll().stream().anyMatch(h -> h.getDate().equals(date));
    }
    
    /**
     * Checks if the given date is an official holiday.
     */
    public boolean isOfficialHoliday(PersianDate date) {
        return getAll().stream()
            .anyMatch(h -> h.getDate().equals(date) && h.isOfficial());
    }
    
    /**
     * Gets the holiday for a specific date, if any.
     */
    public Optional<Holiday> getHoliday(PersianDate date) {
        return getAll().stream()
            .filter(h -> h.getDate().equals(date))
            .findFirst();
    }
    
    /**
     * Gets holidays for a specific month.
     */
    public List<Holiday> getHolidaysInMonth(int month) {
        return getAll().stream()
            .filter(h -> h.getDate().getMonthValue() == month)
            .toList();
    }
}
