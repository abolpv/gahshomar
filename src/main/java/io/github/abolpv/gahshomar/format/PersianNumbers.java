package io.github.abolpv.gahshomar.format;

/**
 * Utility class for converting between Persian and Arabic numerals.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public final class PersianNumbers {
    
    private static final char[] PERSIAN_DIGITS = {'۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹'};
    private static final char[] ARABIC_DIGITS = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
    
    private PersianNumbers() {
        // Utility class
    }
    
    /**
     * Converts a number to Persian digits.
     */
    public static String toPersian(int number) {
        return toPersian(String.valueOf(number));
    }
    
    /**
     * Converts a number to Persian digits.
     */
    public static String toPersian(long number) {
        return toPersian(String.valueOf(number));
    }
    
    /**
     * Converts a string containing digits to Persian digits.
     */
    public static String toPersian(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        StringBuilder result = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            if (c >= '0' && c <= '9') {
                result.append(PERSIAN_DIGITS[c - '0']);
            } else if (c >= '٠' && c <= '٩') {
                result.append(PERSIAN_DIGITS[c - '٠']);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    /**
     * Converts Persian/Arabic digits to Western digits.
     */
    public static String toWestern(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        StringBuilder result = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            if (c >= '۰' && c <= '۹') {
                result.append((char) ('0' + (c - '۰')));
            } else if (c >= '٠' && c <= '٩') {
                result.append((char) ('0' + (c - '٠')));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    /**
     * Parses a string that may contain Persian digits as an integer.
     */
    public static int parseInt(String text) {
        return Integer.parseInt(toWestern(text.trim()));
    }
    
    /**
     * Parses a string that may contain Persian digits as a long.
     */
    public static long parseLong(String text) {
        return Long.parseLong(toWestern(text.trim()));
    }
    
    /**
     * Checks if the string contains Persian digits.
     */
    public static boolean containsPersianDigits(String text) {
        if (text == null) return false;
        for (char c : text.toCharArray()) {
            if (c >= '۰' && c <= '۹') return true;
        }
        return false;
    }
    
    /**
     * Checks if the string contains Arabic digits.
     */
    public static boolean containsArabicDigits(String text) {
        if (text == null) return false;
        for (char c : text.toCharArray()) {
            if (c >= '٠' && c <= '٩') return true;
        }
        return false;
    }
    
    /**
     * Pads a number with leading zeros in Persian.
     */
    public static String padPersian(int number, int width) {
        String str = String.valueOf(number);
        while (str.length() < width) {
            str = "0" + str;
        }
        return toPersian(str);
    }
    
    /**
     * Converts number to Persian words.
     */
    public static String toWords(int number) {
        if (number == 0) return "صفر";
        if (number < 0) return "منفی " + toWords(-number);
        
        String[] ones = {"", "یک", "دو", "سه", "چهار", "پنج", "شش", "هفت", "هشت", "نه",
                        "ده", "یازده", "دوازده", "سیزده", "چهارده", "پانزده", "شانزده",
                        "هفده", "هجده", "نوزده"};
        String[] tens = {"", "", "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود"};
        String[] hundreds = {"", "یکصد", "دویست", "سیصد", "چهارصد", "پانصد", 
                            "ششصد", "هفتصد", "هشتصد", "نهصد"};
        
        StringBuilder result = new StringBuilder();
        
        if (number >= 1000) {
            int thousands = number / 1000;
            if (thousands == 1) {
                result.append("یکهزار");
            } else {
                result.append(toWords(thousands)).append(" هزار");
            }
            number %= 1000;
            if (number > 0) result.append(" و ");
        }
        
        if (number >= 100) {
            result.append(hundreds[number / 100]);
            number %= 100;
            if (number > 0) result.append(" و ");
        }
        
        if (number >= 20) {
            result.append(tens[number / 10]);
            number %= 10;
            if (number > 0) result.append(" و ");
        }
        
        if (number > 0) {
            result.append(ones[number]);
        }
        
        return result.toString();
    }
    
    /**
     * Converts ordinal number to Persian (e.g., 1 -> اول, 2 -> دوم).
     */
    public static String toOrdinal(int number) {
        return switch (number) {
            case 1 -> "اول";
            case 2 -> "دوم";
            case 3 -> "سوم";
            default -> toWords(number) + "م";
        };
    }
}
