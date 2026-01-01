package io.github.abolpv.gahshomar;

import io.github.abolpv.gahshomar.core.*;
import io.github.abolpv.gahshomar.format.*;
import io.github.abolpv.gahshomar.holiday.*;
import io.github.abolpv.gahshomar.range.*;
import io.github.abolpv.gahshomar.range.Period;
import io.github.abolpv.gahshomar.temporal.*;
import io.github.abolpv.gahshomar.util.*;
import io.github.abolpv.gahshomar.zone.*;

import org.junit.jupiter.api.*;

import java.time.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Additional comprehensive tests for Gahshomar library.
 */
class AdditionalTests {
    
    // ==================== PersianDateTime Tests ====================
    
    @Nested
    @DisplayName("PersianDateTime")
    class PersianDateTimeTests {
        
        @Test
        @DisplayName("Should create PersianDateTime")
        void shouldCreatePersianDateTime() {
            PersianDateTime dt = PersianDateTime.of(1403, 10, 15, 14, 30);
            
            assertEquals(1403, dt.getYear());
            assertEquals(10, dt.getMonthValue());
            assertEquals(15, dt.getDayOfMonth());
            assertEquals(14, dt.getHour());
            assertEquals(30, dt.getMinute());
        }
        
        @Test
        @DisplayName("Should create now")
        void shouldCreateNow() {
            PersianDateTime dt = PersianDateTime.now();
            assertNotNull(dt);
        }
        
        @Test
        @DisplayName("Should convert to Gregorian")
        void shouldConvertToGregorian() {
            PersianDateTime dt = PersianDateTime.of(1403, 10, 15, 14, 30, 45);
            LocalDateTime gregorian = dt.toGregorian();
            
            assertEquals(14, gregorian.getHour());
            assertEquals(30, gregorian.getMinute());
            assertEquals(45, gregorian.getSecond());
        }
        
        @Test
        @DisplayName("Should add hours")
        void shouldAddHours() {
            PersianDateTime dt = PersianDateTime.of(1403, 10, 15, 22, 0);
            PersianDateTime result = dt.plusHours(5);
            
            assertEquals(16, result.getDayOfMonth());
            assertEquals(3, result.getHour());
        }
        
        @Test
        @DisplayName("Should format with time")
        void shouldFormatWithTime() {
            PersianDateTime dt = PersianDateTime.of(1403, 10, 15, 14, 30, 45);
            String formatted = dt.format("yyyy/MM/dd HH:mm:ss");
            
            assertEquals("1403/10/15 14:30:45", formatted);
        }
    }
    
    // ==================== HijriDateTime Tests ====================
    
    @Nested
    @DisplayName("HijriDateTime")
    class HijriDateTimeTests {
        
        @Test
        @DisplayName("Should create HijriDateTime")
        void shouldCreateHijriDateTime() {
            HijriDateTime dt = HijriDateTime.of(1446, 6, 15, 10, 30);
            
            assertEquals(1446, dt.getYear());
            assertEquals(6, dt.getMonthValue());
            assertEquals(10, dt.getHour());
        }
        
        @Test
        @DisplayName("Should convert to Persian")
        void shouldConvertToPersian() {
            HijriDateTime hijri = HijriDateTime.of(1446, 6, 15, 10, 30);
            PersianDateTime persian = hijri.toPersian();
            
            assertNotNull(persian);
            assertEquals(10, persian.getHour());
            assertEquals(30, persian.getMinute());
        }
    }
    
    // ==================== DateConverter Tests ====================
    
    @Nested
    @DisplayName("DateConverter")
    class DateConverterTests {
        
        @Test
        @DisplayName("Should convert Persian to Gregorian")
        void shouldConvertPersianToGregorian() {
            LocalDate result = DateConverter.persianToGregorian(1403, 10, 15);
            
            assertEquals(2025, result.getYear());
            assertEquals(1, result.getMonthValue());
            assertEquals(4, result.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should convert via Julian Day")
        void shouldConvertViaJulianDay() {
            long jd = DateConverter.persianToJulianDay(1403, 1, 1);
            int[] persian = DateConverter.julianDayToPersian(jd);
            
            assertEquals(1403, persian[0]);
            assertEquals(1, persian[1]);
            assertEquals(1, persian[2]);
        }
        
        @Test
        @DisplayName("Should calculate days between")
        void shouldCalculateDaysBetween() {
            long days = DateConverter.daysBetweenPersian(1403, 1, 1, 1403, 1, 11);
            assertEquals(10, days);
        }
    }
    
    // ==================== LeapYearRule Tests ====================
    
    @Nested
    @DisplayName("LeapYearRule")
    class LeapYearRuleTests {
        
        @Test
        @DisplayName("Should check all calendar leap years")
        void shouldCheckAllCalendarLeapYears() {
            assertTrue(LeapYearRule.isPersianLeapYear(1403));
            assertTrue(LeapYearRule.isHijriLeapYear(2));
            assertTrue(LeapYearRule.isGregorianLeapYear(2024));
        }
        
        @Test
        @DisplayName("Should get year lengths")
        void shouldGetYearLengths() {
            assertEquals(366, LeapYearRule.getPersianYearLength(1403));
            assertEquals(365, LeapYearRule.getPersianYearLength(1402));
            assertEquals(355, LeapYearRule.getHijriYearLength(2));
            assertEquals(354, LeapYearRule.getHijriYearLength(1));
        }
        
        @Test
        @DisplayName("Should get next leap year")
        void shouldGetNextLeapYear() {
            int next = LeapYearRule.getNextPersianLeapYear(1402);
            assertEquals(1403, next);
        }
    }
    
    // ==================== DateFormatter Tests ====================
    
    @Nested
    @DisplayName("DateFormatter")
    class DateFormatterTests {
        
        @Test
        @DisplayName("Should create with pattern")
        void shouldCreateWithPattern() {
            DateFormatter formatter = DateFormatter.ofPattern("yyyy-MM-dd");
            PersianDate date = PersianDate.of(1403, 10, 15);
            
            assertEquals("1403-10-15", formatter.format(date));
        }
        
        @Test
        @DisplayName("Should use Persian digits")
        void shouldUsePersianDigits() {
            DateFormatter formatter = DateFormatter.ofPattern("yyyy/MM/dd", true);
            PersianDate date = PersianDate.of(1403, 10, 15);
            
            assertEquals("۱۴۰۳/۱۰/۱۵", formatter.format(date));
        }
        
        @Test
        @DisplayName("Should use predefined formatters")
        void shouldUsePredefinedFormatters() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            
            assertNotNull(DateFormatter.isoDate().format(date));
            assertNotNull(DateFormatter.longDate().format(date));
            assertNotNull(DateFormatter.fullDate().format(date));
        }
    }
    
    // ==================== DateParser Tests ====================
    
    @Nested
    @DisplayName("DateParser")
    class DateParserTests {
        
        @Test
        @DisplayName("Should parse various formats")
        void shouldParseVariousFormats() {
            assertEquals(1403, DateParser.parse("1403/10/15").getYear());
            assertEquals(1403, DateParser.parse("1403-10-15").getYear());
            assertEquals(1403, DateParser.parse("۱۴۰۳/۱۰/۱۵").getYear());
        }
        
        @Test
        @DisplayName("Should try parse safely")
        void shouldTryParseSafely() {
            assertTrue(DateParser.tryParse("1403/10/15").isPresent());
            assertTrue(DateParser.tryParse("invalid").isEmpty());
        }
    }
    
    // ==================== RelativeTime Tests ====================
    
    @Nested
    @DisplayName("RelativeTime")
    class RelativeTimeTests {
        
        @Test
        @DisplayName("Should format relative dates")
        void shouldFormatRelativeDates() {
            PersianDate today = PersianDate.now();
            
            assertEquals("امروز", RelativeTime.format(today));
            assertEquals("دیروز", RelativeTime.format(today.minusDays(1)));
            assertEquals("فردا", RelativeTime.format(today.plusDays(1)));
        }
        
        @Test
        @DisplayName("Should format English")
        void shouldFormatEnglish() {
            PersianDate today = PersianDate.now();
            
            assertEquals("today", RelativeTime.formatEnglish(today));
            assertEquals("yesterday", RelativeTime.formatEnglish(today.minusDays(1)));
        }
    }
    
    // ==================== Period Tests ====================
    
    @Nested
    @DisplayName("Period")
    class PeriodTests {
        
        @Test
        @DisplayName("Should create period")
        void shouldCreatePeriod() {
            Period period = Period.of(2, 3, 15);
            
            assertEquals(2, period.getYears());
            assertEquals(3, period.getMonths());
            assertEquals(15, period.getDays());
        }
        
        @Test
        @DisplayName("Should calculate between dates")
        void shouldCalculateBetweenDates() {
            PersianDate start = PersianDate.of(1403, 1, 1);
            PersianDate end = PersianDate.of(1405, 4, 16);
            
            Period period = Period.between(start, end);
            
            assertEquals(2, period.getYears());
            assertEquals(3, period.getMonths());
            assertEquals(15, period.getDays());
        }
        
        @Test
        @DisplayName("Should format to Persian")
        void shouldFormatToPersian() {
            Period period = Period.of(2, 3, 15);
            String formatted = period.toPersianString();
            
            assertTrue(formatted.contains("سال"));
            assertTrue(formatted.contains("ماه"));
            assertTrue(formatted.contains("روز"));
        }
        
        @Test
        @DisplayName("Should add to date")
        void shouldAddToDate() {
            Period period = Period.ofMonths(2);
            PersianDate date = PersianDate.of(1403, 1, 15);
            PersianDate result = period.addTo(date);
            
            assertEquals(3, result.getMonthValue());
        }
    }
    
    // ==================== DateStream Tests ====================
    
    @Nested
    @DisplayName("DateStream")
    class DateStreamTests {
        
        @Test
        @DisplayName("Should create range stream")
        void shouldCreateRangeStream() {
            PersianDate start = PersianDate.of(1403, 1, 1);
            PersianDate end = PersianDate.of(1403, 1, 10);
            
            long count = DateStream.range(start, end).count();
            assertEquals(9, count);
        }
        
        @Test
        @DisplayName("Should stream month")
        void shouldStreamMonth() {
            long count = DateStream.ofMonth(1403, 1).count();
            assertEquals(31, count);
        }
        
        @Test
        @DisplayName("Should stream workdays")
        void shouldStreamWorkdays() {
            PersianDate start = PersianDate.of(1403, 10, 15);
            PersianDate end = PersianDate.of(1403, 10, 21);
            
            long workdays = DateStream.workdays(start, end).count();
            assertEquals(6, workdays); // 7 days - 1 Friday
        }
        
        @Test
        @DisplayName("Should stream leap years")
        void shouldStreamLeapYears() {
            long count = DateStream.leapYears(1400, 1410)
                .count();
            assertTrue(count > 0);
        }
    }
    
    // ==================== HijriHolidays Tests ====================
    
    @Nested
    @DisplayName("HijriHolidays")
    class HijriHolidaysTests {
        
        @Test
        @DisplayName("Should get Hijri holidays")
        void shouldGetHijriHolidays() {
            HijriHolidays holidays = HijriHolidays.of(1446);
            List<HijriHolidays.HijriHoliday> all = holidays.getAll();
            
            assertFalse(all.isEmpty());
        }
        
        @Test
        @DisplayName("Should get Ramadan holidays")
        void shouldGetRamadanHolidays() {
            HijriHolidays holidays = HijriHolidays.of(1446);
            List<HijriHolidays.HijriHoliday> ramadan = holidays.getRamadanHolidays();
            
            assertFalse(ramadan.isEmpty());
        }
    }
    
    // ==================== DateUtils Tests ====================
    
    @Nested
    @DisplayName("DateUtils")
    class DateUtilsTests {
        
        @Test
        @DisplayName("Should validate dates")
        void shouldValidateDates() {
            assertTrue(DateUtils.isValidPersianDate(1403, 10, 15));
            assertFalse(DateUtils.isValidPersianDate(1403, 13, 1));
        }
        
        @Test
        @DisplayName("Should get week of year")
        void shouldGetWeekOfYear() {
            PersianDate date = PersianDate.of(1403, 1, 15);
            int week = DateUtils.getWeekOfYear(date);
            assertTrue(week >= 1 && week <= 53);
        }
        
        @Test
        @DisplayName("Should get quarter dates")
        void shouldGetQuarterDates() {
            PersianDate first = DateUtils.getFirstDayOfQuarter(1403, 2);
            PersianDate last = DateUtils.getLastDayOfQuarter(1403, 2);
            
            assertEquals(4, first.getMonthValue());
            assertEquals(6, last.getMonthValue());
        }
        
        @Test
        @DisplayName("Should min/max dates")
        void shouldMinMaxDates() {
            PersianDate a = PersianDate.of(1403, 1, 1);
            PersianDate b = PersianDate.of(1403, 12, 29);
            
            assertEquals(a, DateUtils.min(a, b));
            assertEquals(b, DateUtils.max(a, b));
        }
    }
    
    // ==================== IranTimeZone Tests ====================
    
    @Nested
    @DisplayName("IranTimeZone")
    class IranTimeZoneTests {
        
        @Test
        @DisplayName("Should get Iran zone")
        void shouldGetIranZone() {
            assertNotNull(IranTimeZone.IRAN);
            assertEquals("Asia/Tehran", IranTimeZone.IRAN_ZONE_ID);
        }
        
        @Test
        @DisplayName("Should get current time")
        void shouldGetCurrentTime() {
            ZonedDateTime now = IranTimeZone.now();
            assertNotNull(now);
            assertEquals(IranTimeZone.IRAN, now.getZone());
        }
        
        @Test
        @DisplayName("Should check DST")
        void shouldCheckDST() {
            // Just verify it doesn't throw
            boolean isDST = IranTimeZone.isDSTActive();
            assertNotNull(Boolean.valueOf(isDST));
        }
    }
    
    // ==================== ZonedPersianDateTime Tests ====================
    
    @Nested
    @DisplayName("ZonedPersianDateTime")
    class ZonedPersianDateTimeTests {
        
        @Test
        @DisplayName("Should create zoned date time")
        void shouldCreateZonedDateTime() {
            ZonedPersianDateTime zdt = ZonedPersianDateTime.of(
                1403, 10, 15, 14, 30, IranTimeZone.IRAN);
            
            assertEquals(1403, zdt.getYear());
            assertEquals(14, zdt.getHour());
        }
        
        @Test
        @DisplayName("Should convert zones")
        void shouldConvertZones() {
            ZonedPersianDateTime tehran = ZonedPersianDateTime.nowInIran();
            ZonedPersianDateTime utc = tehran.withZoneSameInstant(ZoneOffset.UTC);
            
            assertEquals(tehran.toInstant(), utc.toInstant());
        }
        
        @Test
        @DisplayName("Should check DST")
        void shouldCheckDST() {
            ZonedPersianDateTime zdt = ZonedPersianDateTime.nowInIran();
            // Just verify it doesn't throw
            assertNotNull(Boolean.valueOf(zdt.isDaylightSavingTime()));
        }
    }
    
    // ==================== DSTRule Tests ====================
    
    @Nested
    @DisplayName("DSTRule")
    class DSTRuleTests {
        
        @Test
        @DisplayName("Should check DST active")
        void shouldCheckDSTActive() {
            // Just verify it doesn't throw
            assertNotNull(Boolean.valueOf(DSTRule.isDSTActiveInIran()));
        }
        
        @Test
        @DisplayName("Should get offsets")
        void shouldGetOffsets() {
            assertEquals(ZoneOffset.ofHoursMinutes(3, 30), DSTRule.getIranStandardOffset());
            assertEquals(ZoneOffset.ofHoursMinutes(4, 30), DSTRule.getIranDSTOffset());
        }
        
        @Test
        @DisplayName("Should get DST info")
        void shouldGetDSTInfo() {
            PersianDate date = PersianDate.now();
            String info = DSTRule.getDSTInfo(date);
            
            assertNotNull(info);
            assertTrue(info.contains("UTC"));
        }
    }
    
    // ==================== CalendarType Tests ====================
    
    @Nested
    @DisplayName("CalendarType")
    class CalendarTypeTests {
        
        @Test
        @DisplayName("Should have all types")
        void shouldHaveAllTypes() {
            assertEquals(3, CalendarType.values().length);
        }
        
        @Test
        @DisplayName("Should check solar/lunar")
        void shouldCheckSolarLunar() {
            assertTrue(CalendarType.PERSIAN.isSolar());
            assertTrue(CalendarType.GREGORIAN.isSolar());
            assertTrue(CalendarType.HIJRI.isLunar());
        }
    }
    
    // ==================== Occasion Tests ====================
    
    @Nested
    @DisplayName("Occasion")
    class OccasionTests {
        
        @Test
        @DisplayName("Should create occasion")
        void shouldCreateOccasion() {
            PersianDate date = PersianDate.of(1403, 1, 1);
            Occasion occasion = new Occasion(date, "نوروز", Occasion.OccasionType.NATIONAL);
            
            assertEquals("نوروز", occasion.getName());
            assertTrue(occasion.isRecurring());
        }
        
        @Test
        @DisplayName("Should get next occurrence")
        void shouldGetNextOccurrence() {
            PersianDate date = PersianDate.of(1403, 1, 1);
            Occasion occasion = new Occasion(date, "نوروز", Occasion.OccasionType.NATIONAL);
            
            PersianDate next = occasion.getNextOccurrence();
            assertNotNull(next);
            assertEquals(1, next.getMonthValue());
            assertEquals(1, next.getDayOfMonth());
        }
    }
    
    // ==================== Age Extended Tests ====================
    
    @Nested
    @DisplayName("Age Extended")
    class AgeExtendedTests {
        
        @Test
        @DisplayName("Should format English")
        void shouldFormatEnglish() {
            Age age = Age.of(33, 5, 10);
            String english = age.toEnglishString();
            
            assertTrue(english.contains("33"));
            assertTrue(english.contains("years"));
        }
        
        @Test
        @DisplayName("Should get totals")
        void shouldGetTotals() {
            Age age = Age.of(2, 6, 15);
            
            assertEquals(30, age.getTotalMonths());
            assertTrue(age.getTotalDays() > 0);
        }
    }
    
    // ==================== Edge Case Tests ====================
    
    @Nested
    @DisplayName("Edge Cases Extended")
    class EdgeCasesExtendedTests {
        
        @Test
        @DisplayName("Should handle century boundaries")
        void shouldHandleCenturyBoundaries() {
            PersianDate date = PersianDate.of(1399, 12, 29);
            PersianDate next = date.plusDays(1);
            
            assertEquals(1400, next.getYear());
        }
        
        @Test
        @DisplayName("Should handle very old dates")
        void shouldHandleVeryOldDates() {
            PersianDate date = PersianDate.of(100, 1, 1);
            assertNotNull(date.toGregorian());
        }
        
        @Test
        @DisplayName("Should handle future dates")
        void shouldHandleFutureDates() {
            PersianDate date = PersianDate.of(2000, 12, 29);
            assertNotNull(date.toGregorian());
        }
    }
}
