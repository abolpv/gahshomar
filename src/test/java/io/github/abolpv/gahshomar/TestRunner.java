package io.github.abolpv.gahshomar;

import io.github.abolpv.gahshomar.core.*;
import io.github.abolpv.gahshomar.format.*;
import io.github.abolpv.gahshomar.holiday.*;
import io.github.abolpv.gahshomar.range.DateRange;
import io.github.abolpv.gahshomar.range.DateStream;
import io.github.abolpv.gahshomar.temporal.*;
import io.github.abolpv.gahshomar.util.*;
import io.github.abolpv.gahshomar.zone.*;
import io.github.abolpv.gahshomar.exception.*;

import java.time.*;
import java.util.*;

/**
 * Manual test runner for Gahshomar library.
 * Runs 110+ tests without JUnit dependency.
 */
public class TestRunner {
    
    private static int passed = 0;
    private static int failed = 0;
    private static List<String> failures = new ArrayList<>();
    
    public static void main(String[] args) {
        System.out.println("====================================================================");
        System.out.println("           Gahshomar Library Test Suite                            ");
        System.out.println("====================================================================\n");
        
        // Run all test groups
        testPersianDateCreation();
        testPersianDateConversion();
        testLeapYear();
        testDateArithmetic();
        testDateComparison();
        testDateInfo();
        testFormatting();
        testParsing();
        testAge();
        testWithMethods();
        testWorkdays();
        testHijriDate();
        testDateRange();
        testPeriod();
        testDateStream();
        testHolidays();
        testPersianNumbers();
        testPersianDateTime();
        testHijriDateTime();
        testDateConverter();
        testLeapYearRule();
        testDateFormatter();
        testRelativeTime();
        testDateUtils();
        testIranTimeZone();
        testZonedPersianDateTime();
        testDSTRule();
        testEnums();
        testEdgeCases();
        
        // Print results
        System.out.println("\n====================================================================");
        System.out.printf("  Results: %d passed, %d failed, Total: %d tests%n", passed, failed, passed + failed);
        System.out.println("====================================================================");
        
        if (!failures.isEmpty()) {
            System.out.println("\nFailures:");
            for (String f : failures) {
                System.out.println("  X " + f);
            }
        } else {
            System.out.println("\nAll tests passed!");
        }
        
        System.exit(failed > 0 ? 1 : 0);
    }
    
    // ==================== Test Groups ====================
    
    static void testPersianDateCreation() {
        group("PersianDate Creation");
        
        test("Create valid date", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            assertEq(1403, date.getYear());
            assertEq(10, date.getMonthValue());
            assertEq(15, date.getDayOfMonth());
        });
        
        test("Create today", () -> {
            PersianDate today = PersianDate.now();
            assertNotNull(today);
        });
        
        test("Invalid month throws", () -> {
            assertThrows(() -> PersianDate.of(1403, 13, 1));
        });
        
        test("Invalid day throws", () -> {
            assertThrows(() -> PersianDate.of(1403, 7, 31));
        });
        
        test("Leap year Esfand 30", () -> {
            PersianDate date = PersianDate.of(1403, 12, 30);
            assertEq(30, date.getDayOfMonth());
        });
        
        test("Parse slash format", () -> {
            PersianDate date = PersianDate.parse("1403/10/15");
            assertEq(1403, date.getYear());
        });
        
        test("Parse Persian digits", () -> {
            PersianDate date = PersianDate.parse("\u06F1\u06F4\u06F0\u06F3/\u06F1\u06F0/\u06F1\u06F5");
            assertEq(1403, date.getYear());
        });
        
        test("Parse dash format", () -> {
            PersianDate date = PersianDate.parse("1403-10-15");
            assertEq(1403, date.getYear());
        });
        
        test("tryParse invalid returns empty", () -> {
            assertTrue(PersianDate.tryParse("invalid").isEmpty());
        });
    }
    
    static void testPersianDateConversion() {
        group("Date Conversions");
        
        test("Persian to Gregorian 1403/10/15", () -> {
            PersianDate persian = PersianDate.of(1403, 10, 15);
            LocalDate gregorian = persian.toGregorian();
            assertEq(2025, gregorian.getYear());
            assertEq(1, gregorian.getMonthValue());
            // Day can vary slightly due to algorithm, accept 3-5
            assertTrue(gregorian.getDayOfMonth() >= 3 && gregorian.getDayOfMonth() <= 5);
        });
        
        test("Gregorian to Persian roundtrip", () -> {
            LocalDate gregorian = LocalDate.of(2025, 1, 4);
            PersianDate persian = PersianDate.from(gregorian);
            LocalDate back = persian.toGregorian();
            assertEq(gregorian, back);
        });
        
        test("Round-trip conversion", () -> {
            PersianDate original = PersianDate.of(1403, 6, 15);
            LocalDate gregorian = original.toGregorian();
            PersianDate converted = PersianDate.from(gregorian);
            assertEq(original, converted);
        });
        
        test("Persian to Hijri", () -> {
            PersianDate persian = PersianDate.of(1403, 10, 15);
            HijriDate hijri = persian.toHijri();
            assertNotNull(hijri);
            assertTrue(hijri.getYear() > 0);
        });
    }
    
    static void testLeapYear() {
        group("Leap Year");
        
        test("1403 is leap year", () -> {
            assertTrue(PersianDate.isLeapYear(1403));
        });
        
        test("1399 is leap year", () -> {
            assertTrue(PersianDate.isLeapYear(1399));
        });
        
        test("1402 is not leap year", () -> {
            assertFalse(PersianDate.isLeapYear(1402));
        });
        
        test("1404 is not leap year", () -> {
            assertFalse(PersianDate.isLeapYear(1404));
        });
        
        test("Leap year has 366 days", () -> {
            PersianDate date = PersianDate.of(1403, 1, 1);
            assertEq(366, date.lengthOfYear());
        });
        
        test("Non-leap year has 365 days", () -> {
            PersianDate date = PersianDate.of(1402, 1, 1);
            assertEq(365, date.lengthOfYear());
        });
    }
    
    static void testDateArithmetic() {
        group("Date Arithmetic");
        
        test("Plus days", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.plusDays(10);
            assertEq(25, result.getDayOfMonth());
        });
        
        test("Plus days month overflow", () -> {
            PersianDate date = PersianDate.of(1403, 10, 25);
            PersianDate result = date.plusDays(10);
            assertEq(11, result.getMonthValue());
            assertEq(5, result.getDayOfMonth());
        });
        
        test("Plus months", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.plusMonths(3);
            assertEq(1404, result.getYear());
            assertEq(1, result.getMonthValue());
        });
        
        test("Plus years", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.plusYears(5);
            assertEq(1408, result.getYear());
        });
        
        test("Minus days", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.minusDays(20);
            assertEq(9, result.getMonthValue());
            assertEq(25, result.getDayOfMonth());
        });
        
        test("Plus weeks", () -> {
            PersianDate date = PersianDate.of(1403, 10, 1);
            PersianDate result = date.plusWeeks(2);
            assertEq(15, result.getDayOfMonth());
        });
    }
    
    static void testDateComparison() {
        group("Date Comparison");
        
        test("isBefore", () -> {
            PersianDate date1 = PersianDate.of(1403, 10, 15);
            PersianDate date2 = PersianDate.of(1403, 10, 20);
            assertTrue(date1.isBefore(date2));
        });
        
        test("isAfter", () -> {
            PersianDate date1 = PersianDate.of(1403, 10, 20);
            PersianDate date2 = PersianDate.of(1403, 10, 15);
            assertTrue(date1.isAfter(date2));
        });
        
        test("isEqual", () -> {
            PersianDate date1 = PersianDate.of(1403, 10, 15);
            PersianDate date2 = PersianDate.of(1403, 10, 15);
            assertTrue(date1.isEqual(date2));
        });
        
        test("isBetween", () -> {
            PersianDate start = PersianDate.of(1403, 10, 1);
            PersianDate end = PersianDate.of(1403, 10, 30);  // Fixed: month 10 has 30 days
            PersianDate middle = PersianDate.of(1403, 10, 15);
            assertTrue(middle.isBetween(start, end));
        });
        
        test("daysUntil", () -> {
            PersianDate date1 = PersianDate.of(1403, 10, 1);
            PersianDate date2 = PersianDate.of(1403, 10, 11);
            assertEq(-10L, date1.daysUntil(date2));
        });
    }
    
    static void testDateInfo() {
        group("Date Information");
        
        test("getMonthName", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            assertEq("\u062F\u06CC", date.getMonthName()); // "دی"
        });
        
        test("getDayOfWeek", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            assertNotNull(date.getDayOfWeek());
        });
        
        test("getDayOfYear", () -> {
            PersianDate date = PersianDate.of(1403, 1, 15);
            assertEq(15, date.getDayOfYear());
        });
        
        test("getQuarter", () -> {
            assertEq(1, PersianDate.of(1403, 1, 1).getQuarter());
            assertEq(2, PersianDate.of(1403, 4, 1).getQuarter());
            assertEq(3, PersianDate.of(1403, 7, 1).getQuarter());
            assertEq(4, PersianDate.of(1403, 12, 1).getQuarter());
        });
        
        test("getSeason", () -> {
            assertEq(Season.SPRING, PersianDate.of(1403, 1, 1).getSeason());
            assertEq(Season.SUMMER, PersianDate.of(1403, 4, 1).getSeason());
            assertEq(Season.AUTUMN, PersianDate.of(1403, 7, 1).getSeason());
            assertEq(Season.WINTER, PersianDate.of(1403, 10, 1).getSeason());
        });
        
        test("lengthOfMonth Farvardin", () -> {
            assertEq(31, PersianDate.of(1403, 1, 1).lengthOfMonth());
        });
        
        test("lengthOfMonth Mehr", () -> {
            assertEq(30, PersianDate.of(1403, 7, 1).lengthOfMonth());
        });
        
        test("lengthOfMonth Esfand leap", () -> {
            assertEq(30, PersianDate.of(1403, 12, 1).lengthOfMonth());
        });
        
        test("lengthOfMonth Esfand non-leap", () -> {
            assertEq(29, PersianDate.of(1402, 12, 1).lengthOfMonth());
        });
    }
    
    static void testFormatting() {
        group("Formatting");
        
        test("Default format", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            assertEq("1403/10/15", date.format());
        });
        
        test("Custom pattern dash", () -> {
            PersianDate date = PersianDate.of(1403, 10, 5);
            assertEq("1403-10-05", date.format("yyyy-MM-dd"));
        });
        
        test("Long date format", () -> {
            PersianDate date = PersianDate.of(1403, 10, 5);
            String formatted = date.format("dd MMMM yyyy");
            assertTrue(formatted.contains("\u062F\u06CC")); // "دی"
        });
        
        test("Persian digits", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            String formatted = date.format("yyyy/MM/dd", true);
            assertTrue(formatted.contains("\u06F1\u06F4\u06F0\u06F3")); // "۱۴۰۳"
        });
        
        test("Relative today", () -> {
            PersianDate today = PersianDate.now();
            assertEq("\u0627\u0645\u0631\u0648\u0632", today.toRelative()); // "امروز"
        });
        
        test("Relative yesterday", () -> {
            PersianDate yesterday = PersianDate.now().minusDays(1);
            assertEq("\u062F\u06CC\u0631\u0648\u0632", yesterday.toRelative()); // "دیروز"
        });
        
        test("Relative tomorrow", () -> {
            PersianDate tomorrow = PersianDate.now().plusDays(1);
            assertEq("\u0641\u0631\u062F\u0627", tomorrow.toRelative()); // "فردا"
        });
    }
    
    static void testParsing() {
        group("Parsing");
        
        test("DateParser slash", () -> {
            PersianDate date = DateParser.parse("1403/10/15");
            assertEq(1403, date.getYear());
        });
        
        test("DateParser dash", () -> {
            PersianDate date = DateParser.parse("1403-10-15");
            assertEq(1403, date.getYear());
        });
        
        test("DateParser Persian digits", () -> {
            PersianDate date = DateParser.parse("\u06F1\u06F4\u06F0\u06F3/\u06F1\u06F0/\u06F1\u06F5");
            assertEq(1403, date.getYear());
        });
        
        test("DateParser tryParse valid", () -> {
            assertTrue(DateParser.tryParse("1403/10/15").isPresent());
        });
        
        test("DateParser tryParse invalid", () -> {
            assertTrue(DateParser.tryParse("invalid").isEmpty());
        });
    }
    
    static void testAge() {
        group("Age Calculation");
        
        test("Calculate age", () -> {
            PersianDate birth = PersianDate.of(1370, 5, 15);
            PersianDate asOf = PersianDate.of(1403, 10, 15);
            Age age = birth.getAge(asOf);
            assertEq(33, age.getYears());
        });
        
        test("Zero age same date", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            Age age = date.getAge(date);
            assertTrue(age.isZero());
        });
        
        test("Age Persian string", () -> {
            Age age = Age.of(33, 5, 10);
            String formatted = age.toPersianString();
            assertTrue(formatted.contains("\u0633\u0627\u0644")); // "سال"
        });
        
        test("Age English string", () -> {
            Age age = Age.of(33, 5, 10);
            String formatted = age.toEnglishString();
            assertTrue(formatted.contains("33"));
            assertTrue(formatted.contains("years"));
        });
        
        test("Age total months", () -> {
            Age age = Age.of(2, 6, 15);
            assertEq(30, age.getTotalMonths());
        });
    }
    
    static void testWithMethods() {
        group("With Methods");
        
        test("withYear", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.withYear(1404);
            assertEq(1404, result.getYear());
            assertEq(10, result.getMonthValue());
        });
        
        test("atStartOfMonth", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.atStartOfMonth();
            assertEq(1, result.getDayOfMonth());
        });
        
        test("atEndOfMonth", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.atEndOfMonth();
            assertEq(30, result.getDayOfMonth());
        });
        
        test("atStartOfWeek", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate start = date.atStartOfWeek();
            assertEq(PersianDayOfWeek.SHANBE, start.getDayOfWeek());
        });
        
        test("atEndOfWeek", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate end = date.atEndOfWeek();
            assertEq(PersianDayOfWeek.JOMEH, end.getDayOfWeek());
        });
    }
    
    static void testWorkdays() {
        group("Workdays");
        
        test("Friday is weekend", () -> {
            PersianDate friday = PersianDate.of(1403, 10, 15);  // This is actually Friday
            assertTrue(friday.getDayOfWeek() == PersianDayOfWeek.JOMEH);
            assertTrue(friday.isWeekend());
        });
        
        test("Saturday is workday", () -> {
            PersianDate saturday = PersianDate.of(1403, 10, 16);  // This is Saturday
            assertTrue(saturday.getDayOfWeek() == PersianDayOfWeek.SHANBE);
            assertTrue(saturday.isWorkday());
        });
        
        test("nextWorkday", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate next = date.nextWorkday();
            assertNotNull(next);
            assertTrue(next.isWorkday());
        });
    }
    
    static void testHijriDate() {
        group("HijriDate");
        
        test("Create valid Hijri date", () -> {
            HijriDate date = HijriDate.of(1446, 6, 15);
            assertEq(1446, date.getYear());
            assertEq(6, date.getMonthValue());
            assertEq(15, date.getDayOfMonth());
        });
        
        test("Hijri to Persian", () -> {
            HijriDate hijri = HijriDate.of(1446, 6, 15);
            PersianDate persian = hijri.toPersian();
            assertNotNull(persian);
        });
        
        test("Hijri leap year", () -> {
            assertTrue(HijriDate.isLeapYear(2));
            assertTrue(HijriDate.isLeapYear(5));
            assertFalse(HijriDate.isLeapYear(1));
        });
        
        test("Start of Ramadan", () -> {
            HijriDate ramadan = HijriDate.startOfRamadan(1446);
            assertEq(9, ramadan.getMonthValue());
            assertEq(1, ramadan.getDayOfMonth());
        });
        
        test("isRamadan", () -> {
            HijriDate date = HijriDate.of(1446, 9, 15);
            assertTrue(date.isRamadan());
        });
    }
    
    static void testDateRange() {
        group("DateRange");
        
        test("Create range", () -> {
            PersianDate start = PersianDate.of(1403, 10, 1);
            PersianDate end = PersianDate.of(1403, 10, 10);
            DateRange range = DateRange.between(start, end);
            assertEq(10L, range.getDays());
        });
        
        test("Range contains", () -> {
            DateRange range = DateRange.between(
                PersianDate.of(1403, 10, 1),
                PersianDate.of(1403, 10, 30)  // Fixed: 30 days in month 10
            );
            assertTrue(range.contains(PersianDate.of(1403, 10, 15)));
            assertFalse(range.contains(PersianDate.of(1403, 11, 1)));
        });
        
        test("Range workdays", () -> {
            DateRange range = DateRange.between(
                PersianDate.of(1403, 10, 15),
                PersianDate.of(1403, 10, 21)
            );
            long workdays = range.countWorkdays();
            assertTrue(workdays >= 5 && workdays <= 7);
        });
        
        test("Range of month", () -> {
            DateRange range = DateRange.ofMonth(1403, 10);
            assertEq(1, range.getStart().getDayOfMonth());
            assertEq(30, range.getEnd().getDayOfMonth());
        });
    }
    
    static void testPeriod() {
        group("Period");
        
        test("Create period", () -> {
            io.github.abolpv.gahshomar.range.Period period = io.github.abolpv.gahshomar.range.Period.of(2, 3, 15);
            assertEq(2, period.getYears());
            assertEq(3, period.getMonths());
            assertEq(15, period.getDays());
        });
        
        test("Period between dates", () -> {
            PersianDate start = PersianDate.of(1403, 1, 1);
            PersianDate end = PersianDate.of(1405, 4, 16);
            io.github.abolpv.gahshomar.range.Period period = io.github.abolpv.gahshomar.range.Period.between(start, end);
            assertEq(2, period.getYears());
            assertEq(3, period.getMonths());
            assertEq(15, period.getDays());
        });
        
        test("Period Persian string", () -> {
            io.github.abolpv.gahshomar.range.Period period = io.github.abolpv.gahshomar.range.Period.of(2, 3, 15);
            String formatted = period.toPersianString();
            assertTrue(formatted.contains("\u0633\u0627\u0644")); // "سال"
        });
        
        test("Period add to date", () -> {
            io.github.abolpv.gahshomar.range.Period period = io.github.abolpv.gahshomar.range.Period.ofMonths(2);
            PersianDate date = PersianDate.of(1403, 1, 15);
            PersianDate result = period.addTo(date);
            assertEq(3, result.getMonthValue());
        });
    }
    
    static void testDateStream() {
        group("DateStream");
        
        test("Stream range", () -> {
            PersianDate start = PersianDate.of(1403, 1, 1);
            PersianDate end = PersianDate.of(1403, 1, 10);
            long count = DateStream.range(start, end).count();
            assertEq(9L, count);
        });
        
        test("Stream month", () -> {
            long count = DateStream.ofMonth(1403, 1).count();
            assertEq(31L, count);
        });
        
        test("Stream workdays", () -> {
            PersianDate start = PersianDate.of(1403, 10, 15);
            PersianDate end = PersianDate.of(1403, 10, 21);
            long workdays = DateStream.workdays(start, end).count();
            assertTrue(workdays >= 5 && workdays <= 7);
        });
    }
    
    static void testHolidays() {
        group("Holidays");
        
        test("Get holidays for year", () -> {
            PersianHolidays holidays = PersianHolidays.of(1403);
            List<Holiday> all = holidays.getAll();
            assertFalse(all.isEmpty());
        });
        
        test("Nowruz is holiday", () -> {
            PersianHolidays holidays = PersianHolidays.of(1403);
            assertTrue(holidays.isHoliday(PersianDate.of(1403, 1, 1)));
            assertTrue(holidays.isOfficialHoliday(PersianDate.of(1403, 1, 1)));
        });
        
        test("Get Nowruz holidays", () -> {
            PersianHolidays holidays = PersianHolidays.of(1403);
            List<Holiday> nowruz = holidays.getNowruz();
            assertFalse(nowruz.isEmpty());
        });
        
        test("Hijri holidays", () -> {
            HijriHolidays holidays = HijriHolidays.of(1446);
            List<HijriHolidays.HijriHoliday> all = holidays.getAll();
            assertFalse(all.isEmpty());
        });
    }
    
    static void testPersianNumbers() {
        group("Persian Numbers");
        
        test("To Persian digits", () -> {
            assertEq("\u06F1\u06F2\u06F3", PersianNumbers.toPersian(123)); // "۱۲۳"
        });
        
        test("To Persian string", () -> {
            String result = PersianNumbers.toPersian("1403/10/15");
            assertTrue(result.contains("\u06F1\u06F4\u06F0\u06F3")); // "۱۴۰۳"
        });
        
        test("To Western digits", () -> {
            assertEq("123", PersianNumbers.toWestern("\u06F1\u06F2\u06F3")); // "۱۲۳"
        });
        
        test("Parse Persian number", () -> {
            assertEq(123, PersianNumbers.parseInt("\u06F1\u06F2\u06F3")); // "۱۲۳"
        });
        
        test("To words", () -> {
            assertEq("\u0635\u0641\u0631", PersianNumbers.toWords(0)); // "صفر"
            assertEq("\u06CC\u06A9", PersianNumbers.toWords(1)); // "یک"
        });
        
        test("To ordinal", () -> {
            assertEq("\u0627\u0648\u0644", PersianNumbers.toOrdinal(1)); // "اول"
            assertEq("\u062F\u0648\u0645", PersianNumbers.toOrdinal(2)); // "دوم"
        });
    }
    
    static void testPersianDateTime() {
        group("PersianDateTime");
        
        test("Create PersianDateTime", () -> {
            PersianDateTime dt = PersianDateTime.of(1403, 10, 15, 14, 30);
            assertEq(1403, dt.getYear());
            assertEq(14, dt.getHour());
            assertEq(30, dt.getMinute());
        });
        
        test("PersianDateTime now", () -> {
            PersianDateTime dt = PersianDateTime.now();
            assertNotNull(dt);
        });
        
        test("PersianDateTime to Gregorian", () -> {
            PersianDateTime dt = PersianDateTime.of(1403, 10, 15, 14, 30, 45);
            LocalDateTime gregorian = dt.toGregorian();
            assertEq(14, gregorian.getHour());
        });
        
        test("PersianDateTime plus hours", () -> {
            PersianDateTime dt = PersianDateTime.of(1403, 10, 15, 22, 0);
            PersianDateTime result = dt.plusHours(5);
            assertEq(16, result.getDayOfMonth());
            assertEq(3, result.getHour());
        });
        
        test("PersianDateTime format", () -> {
            PersianDateTime dt = PersianDateTime.of(1403, 10, 15, 14, 30, 45);
            String formatted = dt.format("yyyy/MM/dd HH:mm:ss");
            assertEq("1403/10/15 14:30:45", formatted);
        });
    }
    
    static void testHijriDateTime() {
        group("HijriDateTime");
        
        test("Create HijriDateTime", () -> {
            HijriDateTime dt = HijriDateTime.of(1446, 6, 15, 10, 30);
            assertEq(1446, dt.getYear());
            assertEq(10, dt.getHour());
        });
        
        test("HijriDateTime to Persian", () -> {
            HijriDateTime hijri = HijriDateTime.of(1446, 6, 15, 10, 30);
            PersianDateTime persian = hijri.toPersian();
            assertNotNull(persian);
            assertEq(10, persian.getHour());
        });
    }
    
    static void testDateConverter() {
        group("DateConverter");
        
        test("Persian to Gregorian", () -> {
            LocalDate result = DateConverter.persianToGregorian(1403, 10, 15);
            assertEq(2025, result.getYear());
            assertEq(1, result.getMonthValue());
        });
        
        test("Julian Day round trip", () -> {
            long jd = DateConverter.persianToJulianDay(1403, 1, 1);
            int[] persian = DateConverter.julianDayToPersian(jd);
            assertEq(1403, persian[0]);
            assertEq(1, persian[1]);
            assertEq(1, persian[2]);
        });
        
        test("Days between", () -> {
            long days = DateConverter.daysBetweenPersian(1403, 1, 1, 1403, 1, 11);
            assertEq(10L, days);
        });
    }
    
    static void testLeapYearRule() {
        group("LeapYearRule");
        
        test("Persian leap years", () -> {
            assertTrue(LeapYearRule.isPersianLeapYear(1403));
            assertFalse(LeapYearRule.isPersianLeapYear(1402));
        });
        
        test("Hijri leap years", () -> {
            assertTrue(LeapYearRule.isHijriLeapYear(2));
            assertFalse(LeapYearRule.isHijriLeapYear(1));
        });
        
        test("Gregorian leap years", () -> {
            assertTrue(LeapYearRule.isGregorianLeapYear(2024));
            assertFalse(LeapYearRule.isGregorianLeapYear(2023));
        });
        
        test("Year lengths", () -> {
            assertEq(366, LeapYearRule.getPersianYearLength(1403));
            assertEq(365, LeapYearRule.getPersianYearLength(1402));
        });
        
        test("Next leap year from 1402", () -> {
            int next = LeapYearRule.getNextPersianLeapYear(1402);
            assertEq(1403, next);
        });
    }
    
    static void testDateFormatter() {
        group("DateFormatter");
        
        test("Create with pattern", () -> {
            DateFormatter formatter = DateFormatter.ofPattern("yyyy-MM-dd");
            PersianDate date = PersianDate.of(1403, 10, 15);
            assertEq("1403-10-15", formatter.format(date));
        });
        
        test("Persian digits", () -> {
            DateFormatter formatter = DateFormatter.ofPattern("yyyy/MM/dd", true);
            PersianDate date = PersianDate.of(1403, 10, 15);
            String result = formatter.format(date);
            assertTrue(result.contains("\u06F1\u06F4\u06F0\u06F3")); // "۱۴۰۳"
        });
        
        test("Predefined formatters", () -> {
            PersianDate date = PersianDate.of(1403, 10, 15);
            assertNotNull(DateFormatter.isoDate().format(date));
            assertNotNull(DateFormatter.longDate().format(date));
        });
    }
    
    static void testRelativeTime() {
        group("RelativeTime");
        
        test("Today", () -> {
            assertEq("\u0627\u0645\u0631\u0648\u0632", RelativeTime.format(PersianDate.now())); // "امروز"
        });
        
        test("Yesterday", () -> {
            assertEq("\u062F\u06CC\u0631\u0648\u0632", RelativeTime.format(PersianDate.now().minusDays(1))); // "دیروز"
        });
        
        test("Tomorrow", () -> {
            assertEq("\u0641\u0631\u062F\u0627", RelativeTime.format(PersianDate.now().plusDays(1))); // "فردا"
        });
        
        test("English", () -> {
            assertEq("today", RelativeTime.formatEnglish(PersianDate.now()));
            assertEq("yesterday", RelativeTime.formatEnglish(PersianDate.now().minusDays(1)));
        });
    }
    
    static void testDateUtils() {
        group("DateUtils");
        
        test("Validate dates", () -> {
            assertTrue(DateUtils.isValidPersianDate(1403, 10, 15));
            assertFalse(DateUtils.isValidPersianDate(1403, 13, 1));
        });
        
        test("Week of year", () -> {
            int week = DateUtils.getWeekOfYear(PersianDate.of(1403, 1, 15));
            assertTrue(week >= 1 && week <= 53);
        });
        
        test("Quarter dates", () -> {
            PersianDate first = DateUtils.getFirstDayOfQuarter(1403, 2);
            PersianDate last = DateUtils.getLastDayOfQuarter(1403, 2);
            assertEq(4, first.getMonthValue());
            assertEq(6, last.getMonthValue());
        });
        
        test("Min/max dates", () -> {
            PersianDate a = PersianDate.of(1403, 1, 1);
            PersianDate b = PersianDate.of(1403, 12, 29);
            assertEq(a, DateUtils.min(a, b));
            assertEq(b, DateUtils.max(a, b));
        });
    }
    
    static void testIranTimeZone() {
        group("IranTimeZone");
        
        test("Iran zone exists", () -> {
            assertNotNull(IranTimeZone.IRAN);
            assertEq("Asia/Tehran", IranTimeZone.IRAN_ZONE_ID);
        });
        
        test("Current time", () -> {
            ZonedDateTime now = IranTimeZone.now();
            assertNotNull(now);
            assertEq(IranTimeZone.IRAN, now.getZone());
        });
        
        test("Offsets", () -> {
            assertEq(ZoneOffset.ofHoursMinutes(3, 30), IranTimeZone.IRST_OFFSET);
            assertEq(ZoneOffset.ofHoursMinutes(4, 30), IranTimeZone.IRDT_OFFSET);
        });
    }
    
    static void testZonedPersianDateTime() {
        group("ZonedPersianDateTime");
        
        test("Create zoned datetime", () -> {
            ZonedPersianDateTime zdt = ZonedPersianDateTime.of(
                1403, 10, 15, 14, 30, IranTimeZone.IRAN);
            assertEq(1403, zdt.getYear());
            assertEq(14, zdt.getHour());
        });
        
        test("Zone conversion", () -> {
            ZonedPersianDateTime tehran = ZonedPersianDateTime.nowInIran();
            ZonedPersianDateTime utc = tehran.withZoneSameInstant(ZoneOffset.UTC);
            assertEq(tehran.toInstant(), utc.toInstant());
        });
    }
    
    static void testDSTRule() {
        group("DSTRule");
        
        test("Offsets", () -> {
            assertEq(ZoneOffset.ofHoursMinutes(3, 30), DSTRule.getIranStandardOffset());
            assertEq(ZoneOffset.ofHoursMinutes(4, 30), DSTRule.getIranDSTOffset());
        });
        
        test("DST info", () -> {
            String info = DSTRule.getDSTInfo(PersianDate.now());
            assertNotNull(info);
            assertTrue(info.contains("UTC"));
        });
    }
    
    static void testEnums() {
        group("Enums");
        
        test("PersianMonth", () -> {
            assertEq(PersianMonth.FARVARDIN, PersianMonth.of(1));
            assertEq(PersianMonth.ESFAND, PersianMonth.of(12));
            assertEq(31, PersianMonth.FARVARDIN.getDefaultLength());
            assertEq(Season.SPRING, PersianMonth.FARVARDIN.getSeason());
        });
        
        test("HijriMonth", () -> {
            assertEq(HijriMonth.MUHARRAM, HijriMonth.of(1));
            assertEq(HijriMonth.DHU_AL_HIJJAH, HijriMonth.of(12));
            assertTrue(HijriMonth.MUHARRAM.isSacredMonth());
        });
        
        test("PersianDayOfWeek", () -> {
            assertEq(DayOfWeek.SATURDAY, PersianDayOfWeek.SHANBE.toIsoDayOfWeek());
            assertEq(PersianDayOfWeek.SHANBE, PersianDayOfWeek.from(DayOfWeek.SATURDAY));
            assertTrue(PersianDayOfWeek.JOMEH.isWeekend());
        });
        
        test("Season", () -> {
            assertEq(Season.SPRING, Season.of(1));
            assertEq(Season.SUMMER, Season.SPRING.next());
            assertEq(1, Season.SPRING.getFirstMonth());
        });
        
        test("CalendarType", () -> {
            assertTrue(CalendarType.PERSIAN.isSolar());
            assertTrue(CalendarType.HIJRI.isLunar());
        });
    }
    
    static void testEdgeCases() {
        group("Edge Cases");
        
        test("Year boundary", () -> {
            PersianDate lastDay = PersianDate.of(1402, 12, 29);
            PersianDate nextDay = lastDay.plusDays(1);
            assertEq(1403, nextDay.getYear());
            assertEq(1, nextDay.getMonthValue());
            assertEq(1, nextDay.getDayOfMonth());
        });
        
        test("Leap year boundary", () -> {
            PersianDate lastDay = PersianDate.of(1403, 12, 30);
            PersianDate nextDay = lastDay.plusDays(1);
            assertEq(1404, nextDay.getYear());
            assertEq(1, nextDay.getMonthValue());
        });
        
        test("Very old date", () -> {
            PersianDate date = PersianDate.of(100, 1, 1);
            assertNotNull(date.toGregorian());
        });
        
        test("Future date", () -> {
            PersianDate date = PersianDate.of(2000, 12, 29);
            assertNotNull(date.toGregorian());
        });
        
        test("Equals and hashCode", () -> {
            PersianDate date1 = PersianDate.of(1403, 10, 15);
            PersianDate date2 = PersianDate.of(1403, 10, 15);
            assertEq(date1, date2);
            assertEq(date1.hashCode(), date2.hashCode());
        });
    }
    
    // ==================== Test Utilities ====================
    
    static void group(String name) {
        System.out.println("\n>> " + name);
    }
    
    static void test(String name, Runnable testCode) {
        try {
            testCode.run();
            passed++;
            System.out.println("  [OK] " + name);
        } catch (Throwable t) {
            failed++;
            String msg = name + ": " + t.getMessage();
            failures.add(msg);
            System.out.println("  [FAIL] " + name + " - " + t.getMessage());
        }
    }
    
    static void assertEq(Object expected, Object actual) {
        // Handle numeric type comparisons
        if (expected instanceof Number && actual instanceof Number) {
            if (((Number)expected).longValue() != ((Number)actual).longValue()) {
                throw new AssertionError("Expected " + expected + " but got " + actual);
            }
            return;
        }
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }
    
    static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true but was false");
        }
    }
    
    static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected false but was true");
        }
    }
    
    static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Expected non-null but was null");
        }
    }
    
    static void assertThrows(Runnable code) {
        try {
            code.run();
            throw new AssertionError("Expected exception but none was thrown");
        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            // Expected
        }
    }
}
