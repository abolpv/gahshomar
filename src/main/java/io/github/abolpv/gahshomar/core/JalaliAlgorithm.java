package io.github.abolpv.gahshomar.core;

/**
 * Core algorithm for Persian (Jalali/Shamsi) calendar conversions.
 * Based on the algorithm by Kazimierz M. Borkowski.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class JalaliAlgorithm {
    
    // Persian calendar epoch in Julian Day Number (March 22, 622 CE)
    private static final int PERSIAN_EPOCH = 1948320;
    
    // Gregorian calendar epoch in Julian Day Number
    private static final int GREGORIAN_EPOCH = 1721426;
    
    private JalaliAlgorithm() {
        // Utility class
    }
    
    /**
     * Converts Persian date to Julian Day Number.
     */
    public static long persianToJulianDay(int year, int month, int day) {
        int epbase = year - (year >= 0 ? 474 : 473);
        int epyear = 474 + mod(epbase, 2820);
        
        return day
            + (month <= 7 ? (month - 1) * 31 : (month - 1) * 30 + 6)
            + (long) Math.floor((epyear * 682 - 110) / 2816.0)
            + (epyear - 1) * 365
            + (long) Math.floor(epbase / 2820.0) * 1029983
            + PERSIAN_EPOCH - 1;
    }
    
    /**
     * Converts Julian Day Number to Persian date.
     * Returns array of [year, month, day].
     */
    public static int[] julianDayToPersian(long julianDay) {
        long depoch = julianDay - persianToJulianDay(475, 1, 1);
        long cycle = (long) Math.floor(depoch / 1029983.0);
        long cyear = mod(depoch, 1029983);
        
        long ycycle;
        if (cyear == 1029982) {
            ycycle = 2820;
        } else {
            long aux1 = (long) Math.floor(cyear / 366.0);
            long aux2 = mod(cyear, 366);
            ycycle = (long) Math.floor((2134 * aux1 + 2816 * aux2 + 2815) / 1028522.0) + aux1 + 1;
        }
        
        int year = (int) (ycycle + 2820 * cycle + 474);
        if (year <= 0) {
            year--;
        }
        
        long yday = julianDay - persianToJulianDay(year, 1, 1) + 1;
        int month = (int) (yday <= 186 ? Math.ceil(yday / 31.0) : Math.ceil((yday - 6) / 30.0));
        int day = (int) (julianDay - persianToJulianDay(year, month, 1) + 1);
        
        return new int[]{year, month, day};
    }
    
    /**
     * Converts Gregorian date to Julian Day Number.
     */
    public static long gregorianToJulianDay(int year, int month, int day) {
        int a = (14 - month) / 12;
        int y = year + 4800 - a;
        int m = month + 12 * a - 3;
        
        return day + (153 * m + 2) / 5 + 365L * y + y / 4 - y / 100 + y / 400 - 32045;
    }
    
    /**
     * Converts Julian Day Number to Gregorian date.
     * Returns array of [year, month, day].
     */
    public static int[] julianDayToGregorian(long julianDay) {
        long a = julianDay + 32044;
        long b = (4 * a + 3) / 146097;
        long c = a - (146097 * b) / 4;
        long d = (4 * c + 3) / 1461;
        long e = c - (1461 * d) / 4;
        long m = (5 * e + 2) / 153;
        
        int day = (int) (e - (153 * m + 2) / 5 + 1);
        int month = (int) (m + 3 - 12 * (m / 10));
        int year = (int) (100 * b + d - 4800 + m / 10);
        
        return new int[]{year, month, day};
    }
    
    /**
     * Converts Persian date to Gregorian date.
     * Returns array of [year, month, day].
     */
    public static int[] persianToGregorian(int year, int month, int day) {
        long jd = persianToJulianDay(year, month, day);
        return julianDayToGregorian(jd);
    }
    
    /**
     * Converts Gregorian date to Persian date.
     * Returns array of [year, month, day].
     */
    public static int[] gregorianToPersian(int year, int month, int day) {
        long jd = gregorianToJulianDay(year, month, day);
        return julianDayToPersian(jd);
    }
    
    /**
     * Checks if a Persian year is a leap year.
     * Uses the 33-year cycle where leap years are at positions: 1, 5, 9, 13, 17, 22, 26, 30
     */
    public static boolean isLeapYear(int year) {
        int[] leapYearsIn33 = {1, 5, 9, 13, 17, 22, 26, 30};
        int mod = year % 33;
        if (mod <= 0) mod += 33;
        for (int leap : leapYearsIn33) {
            if (mod == leap) return true;
        }
        return false;
    }
    
    /**
     * Gets the number of days in a Persian year.
     */
    public static int getDaysInYear(int year) {
        return isLeapYear(year) ? 366 : 365;
    }
    
    /**
     * Gets the number of days in a Persian month.
     */
    public static int getDaysInMonth(int year, int month) {
        if (month <= 6) {
            return 31;
        } else if (month <= 11) {
            return 30;
        } else {
            return isLeapYear(year) ? 30 : 29;
        }
    }
    
    /**
     * Calculates day of year (1-366).
     */
    public static int getDayOfYear(int year, int month, int day) {
        if (month <= 6) {
            return (month - 1) * 31 + day;
        } else {
            return 186 + (month - 7) * 30 + day;
        }
    }
    
    /**
     * Gets the day of week (1=Saturday, 7=Friday).
     * Uses Gregorian conversion for accuracy.
     */
    public static int getDayOfWeek(int year, int month, int day) {
        // Convert to Gregorian and get ISO day of week
        int[] g = persianToGregorian(year, month, day);
        java.time.LocalDate ld = java.time.LocalDate.of(g[0], g[1], g[2]);
        int isoDow = ld.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday
        
        // Convert ISO (1=Mon..7=Sun) to Persian (1=Sat..7=Fri)
        // Saturday=6 in ISO -> 1 in Persian
        // Sunday=7 in ISO -> 2 in Persian
        // Monday=1 in ISO -> 3 in Persian
        // ...
        // Friday=5 in ISO -> 7 in Persian
        int persianDow = (isoDow + 2) % 7;
        return persianDow == 0 ? 7 : persianDow;
    }
    
    /**
     * Modulo operation that handles negative numbers correctly.
     */
    private static long mod(long a, long b) {
        return ((a % b) + b) % b;
    }
    
    private static int mod(int a, int b) {
        return ((a % b) + b) % b;
    }
}
