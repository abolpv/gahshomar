package io.github.abolpv.gahshomar;

import io.github.abolpv.gahshomar.exception.InvalidDateException;
import io.github.abolpv.gahshomar.temporal.*;
import io.github.abolpv.gahshomar.range.DateRange;
import io.github.abolpv.gahshomar.holiday.PersianHolidays;
import io.github.abolpv.gahshomar.holiday.Holiday;
import io.github.abolpv.gahshomar.format.PersianNumbers;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Gahshomar library.
 *
 * @author Abolfazl Azizi
 */
class GahshomarTest {
    
    // ==================== PersianDate Creation Tests ====================
    
    @Nested
    @DisplayName("PersianDate Creation")
    class PersianDateCreationTests {
        
        @Test
        @DisplayName("Should create valid Persian date")
        void shouldCreateValidPersianDate() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            
            assertEquals(1403, date.getYear());
            assertEquals(10, date.getMonthValue());
            assertEquals(15, date.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should create date for today")
        void shouldCreateDateForToday() {
            PersianDate today = PersianDate.now();
            assertNotNull(today);
        }
        
        @Test
        @DisplayName("Should throw exception for invalid month")
        void shouldThrowExceptionForInvalidMonth() {
            assertThrows(InvalidDateException.class, () -> 
                PersianDate.of(1403, 13, 1));
            assertThrows(InvalidDateException.class, () -> 
                PersianDate.of(1403, 0, 1));
        }
        
        @Test
        @DisplayName("Should throw exception for invalid day")
        void shouldThrowExceptionForInvalidDay() {
            // Month 7-11 have 30 days
            assertThrows(InvalidDateException.class, () -> 
                PersianDate.of(1403, 7, 31));
            
            // Esfand in non-leap year has 29 days
            assertThrows(InvalidDateException.class, () -> 
                PersianDate.of(1402, 12, 30));
        }
        
        @Test
        @DisplayName("Should allow 30 days in Esfand for leap year")
        void shouldAllow30DaysInEsfandForLeapYear() {
            // 1403 is a leap year
            PersianDate date = PersianDate.of(1403, 12, 30);
            assertEquals(30, date.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should parse date string")
        void shouldParseDateString() {
            PersianDate date = PersianDate.parse("1403/10/15");
            assertEquals(1403, date.getYear());
            assertEquals(10, date.getMonthValue());
            assertEquals(15, date.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should parse date with Persian digits")
        void shouldParseDateWithPersianDigits() {
            PersianDate date = PersianDate.parse("۱۴۰۳/۱۰/۱۵");
            assertEquals(1403, date.getYear());
            assertEquals(10, date.getMonthValue());
            assertEquals(15, date.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should parse date with dash separator")
        void shouldParseDateWithDashSeparator() {
            PersianDate date = PersianDate.parse("1403-10-15");
            assertEquals(1403, date.getYear());
        }
        
        @Test
        @DisplayName("tryParse should return empty for invalid date")
        void tryParseShouldReturnEmptyForInvalidDate() {
            Optional<PersianDate> result = PersianDate.tryParse("invalid");
            assertTrue(result.isEmpty());
        }
    }
    
    // ==================== Conversion Tests ====================
    
    @Nested
    @DisplayName("Date Conversions")
    class ConversionTests {
        
        @ParameterizedTest
        @DisplayName("Persian to Gregorian conversion")
        @CsvSource({
            "1403, 10, 15, 2025, 1, 3",
            "1403, 1, 1, 2024, 3, 19",
            "1402, 12, 29, 2024, 3, 18",
            "1400, 1, 1, 2021, 3, 20"
        })
        void shouldConvertPersianToGregorian(int pYear, int pMonth, int pDay, 
                                             int gYear, int gMonth, int gDay) {
            PersianDate persian = PersianDate.of(pYear, pMonth, pDay);
            LocalDate gregorian = persian.toGregorian();
            
            assertEquals(gYear, gregorian.getYear());
            assertEquals(gMonth, gregorian.getMonthValue());
            assertEquals(gDay, gregorian.getDayOfMonth());
        }
        
        @ParameterizedTest
        @DisplayName("Gregorian to Persian conversion")
        @CsvSource({
            "2025, 1, 3, 1403, 10, 15",
            "2024, 3, 19, 1403, 1, 1",
            "2024, 3, 18, 1402, 12, 29"
        })
        void shouldConvertGregorianToPersian(int gYear, int gMonth, int gDay,
                                             int pYear, int pMonth, int pDay) {
            LocalDate gregorian = LocalDate.of(gYear, gMonth, gDay);
            PersianDate persian = PersianDate.from(gregorian);
            
            assertEquals(pYear, persian.getYear());
            assertEquals(pMonth, persian.getMonthValue());
            assertEquals(pDay, persian.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Round-trip conversion should be consistent")
        void roundTripConversionShouldBeConsistent() {
            PersianDate original = PersianDate.of(1403, 10, 15);
            LocalDate gregorian = original.toGregorian();
            PersianDate converted = PersianDate.from(gregorian);
            
            assertEquals(original, converted);
        }
        
        @Test
        @DisplayName("Should convert to Hijri")
        void shouldConvertToHijri() {
            PersianDate persian = PersianDate.of(1403, 10, 15);
            HijriDate hijri = persian.toHijri();
            
            assertNotNull(hijri);
            assertTrue(hijri.getYear() > 0);
        }
    }
    
    // ==================== Leap Year Tests ====================
    
    @Nested
    @DisplayName("Leap Year")
    class LeapYearTests {
        
        @ParameterizedTest
        @DisplayName("Should correctly identify leap years")
        @CsvSource({
            "1403, true",
            "1404, true",
            "1402, false",
            "1401, false",
            "1399, true",
            "1400, false"
        })
        void shouldIdentifyLeapYears(int year, boolean expected) {
            assertEquals(expected, PersianDate.isLeapYear(year));
        }
        
        @Test
        @DisplayName("Leap year should have 366 days")
        void leapYearShouldHave366Days() {
            PersianDate leapYear = PersianDate.of(1403, 1, 1);
            assertEquals(366, leapYear.lengthOfYear());
        }
        
        @Test
        @DisplayName("Non-leap year should have 365 days")
        void nonLeapYearShouldHave365Days() {
            PersianDate nonLeapYear = PersianDate.of(1402, 1, 1);
            assertEquals(365, nonLeapYear.lengthOfYear());
        }
    }
    
    // ==================== Arithmetic Tests ====================
    
    @Nested
    @DisplayName("Date Arithmetic")
    class ArithmeticTests {
        
        @Test
        @DisplayName("Should add days correctly")
        void shouldAddDaysCorrectly() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.plusDays(10);
            
            assertEquals(25, result.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should handle month overflow when adding days")
        void shouldHandleMonthOverflow() {
            PersianDate date = PersianDate.of(1403, 10, 25);
            PersianDate result = date.plusDays(10);
            
            assertEquals(11, result.getMonthValue());
            assertEquals(5, result.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should add months correctly")
        void shouldAddMonthsCorrectly() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.plusMonths(3);
            
            assertEquals(1404, result.getYear());
            assertEquals(1, result.getMonthValue());
        }
        
        @Test
        @DisplayName("Should add years correctly")
        void shouldAddYearsCorrectly() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.plusYears(5);
            
            assertEquals(1408, result.getYear());
            assertEquals(10, result.getMonthValue());
        }
        
        @Test
        @DisplayName("Should subtract days correctly")
        void shouldSubtractDaysCorrectly() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.minusDays(20);
            
            assertEquals(9, result.getMonthValue());
            assertEquals(25, result.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should handle day adjustment for shorter month")
        void shouldHandleDayAdjustmentForShorterMonth() {
            // Bahman has 30 days, adding 1 month goes to Esfand (29/30 days)
            PersianDate date = PersianDate.of(1402, 11, 30); // Non-leap year
            PersianDate result = date.plusMonths(1);
            
            assertEquals(12, result.getMonthValue());
            assertEquals(29, result.getDayOfMonth()); // Adjusted from 30 to 29
        }
    }
    
    // ==================== Comparison Tests ====================
    
    @Nested
    @DisplayName("Date Comparison")
    class ComparisonTests {
        
        @Test
        @DisplayName("Should compare dates correctly")
        void shouldCompareDatesCorrectly() {
            PersianDate date1 = PersianDate.of(1403, 10, 15);
            PersianDate date2 = PersianDate.of(1403, 10, 20);
            PersianDate date3 = PersianDate.of(1403, 10, 15);
            
            assertTrue(date1.isBefore(date2));
            assertTrue(date2.isAfter(date1));
            assertTrue(date1.isEqual(date3));
        }
        
        @Test
        @DisplayName("Should check if date is between")
        void shouldCheckIfDateIsBetween() {
            PersianDate start = PersianDate.of(1403, 10, 1);
            PersianDate end = PersianDate.of(1403, 10, 31);
            PersianDate middle = PersianDate.of(1403, 10, 15);
            PersianDate outside = PersianDate.of(1403, 11, 1);
            
            assertTrue(middle.isBetween(start, end));
            assertFalse(outside.isBetween(start, end));
        }
        
        @Test
        @DisplayName("Should calculate days until")
        void shouldCalculateDaysUntil() {
            PersianDate date1 = PersianDate.of(1403, 10, 1);
            PersianDate date2 = PersianDate.of(1403, 10, 11);
            
            assertEquals(-10, date1.daysUntil(date2));
        }
    }
    
    // ==================== Getters Tests ====================
    
    @Nested
    @DisplayName("Date Information")
    class DateInfoTests {
        
        @Test
        @DisplayName("Should get month name")
        void shouldGetMonthName() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            assertEquals("دی", date.getMonthName());
        }
        
        @Test
        @DisplayName("Should get day of week")
        void shouldGetDayOfWeek() {
            // 1403/10/15 is Friday (January 3, 2025)
            PersianDate date = PersianDate.of(1403, 10, 15);
            assertEquals(PersianDayOfWeek.JOMEH, date.getDayOfWeek());
            
            // 1403/10/16 is Saturday (January 4, 2025)
            PersianDate saturday = PersianDate.of(1403, 10, 16);
            assertEquals(PersianDayOfWeek.SHANBE, saturday.getDayOfWeek());
        }
        
        @Test
        @DisplayName("Should get day of year")
        void shouldGetDayOfYear() {
            PersianDate date = PersianDate.of(1403, 1, 15);
            assertEquals(15, date.getDayOfYear());
            
            PersianDate date2 = PersianDate.of(1403, 2, 1);
            assertEquals(32, date2.getDayOfYear()); // 31 + 1
        }
        
        @Test
        @DisplayName("Should get quarter")
        void shouldGetQuarter() {
            assertEquals(1, PersianDate.of(1403, 1, 1).getQuarter());
            assertEquals(1, PersianDate.of(1403, 3, 31).getQuarter());
            assertEquals(2, PersianDate.of(1403, 4, 1).getQuarter());
            assertEquals(4, PersianDate.of(1403, 12, 1).getQuarter());
        }
        
        @Test
        @DisplayName("Should get season")
        void shouldGetSeason() {
            assertEquals(Season.SPRING, PersianDate.of(1403, 1, 1).getSeason());
            assertEquals(Season.SUMMER, PersianDate.of(1403, 4, 1).getSeason());
            assertEquals(Season.AUTUMN, PersianDate.of(1403, 7, 1).getSeason());
            assertEquals(Season.WINTER, PersianDate.of(1403, 10, 1).getSeason());
        }
        
        @Test
        @DisplayName("Should get month length")
        void shouldGetMonthLength() {
            assertEquals(31, PersianDate.of(1403, 1, 1).lengthOfMonth());
            assertEquals(31, PersianDate.of(1403, 6, 1).lengthOfMonth());
            assertEquals(30, PersianDate.of(1403, 7, 1).lengthOfMonth());
            assertEquals(30, PersianDate.of(1403, 12, 1).lengthOfMonth()); // Leap year
            assertEquals(29, PersianDate.of(1402, 12, 1).lengthOfMonth()); // Non-leap
        }
    }
    
    // ==================== Format Tests ====================
    
    @Nested
    @DisplayName("Formatting")
    class FormatTests {
        
        @Test
        @DisplayName("Should format with default pattern")
        void shouldFormatWithDefaultPattern() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            assertEquals("1403/10/15", date.format());
        }
        
        @Test
        @DisplayName("Should format with custom pattern")
        void shouldFormatWithCustomPattern() {
            PersianDate date = PersianDate.of(1403, 10, 5);
            assertEquals("1403-10-05", date.format("yyyy-MM-dd"));
            assertEquals("05 دی 1403", date.format("dd MMMM yyyy"));
        }
        
        @Test
        @DisplayName("Should format with Persian digits")
        void shouldFormatWithPersianDigits() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            assertEquals("۱۴۰۳/۱۰/۱۵", date.format("yyyy/MM/dd", true));
        }
        
        @Test
        @DisplayName("Should return relative time")
        void shouldReturnRelativeTime() {
            PersianDate today = PersianDate.now();
            assertEquals("امروز", today.toRelative());
            
            PersianDate yesterday = today.minusDays(1);
            assertEquals("دیروز", yesterday.toRelative());
            
            PersianDate tomorrow = today.plusDays(1);
            assertEquals("فردا", tomorrow.toRelative());
        }
    }
    
    // ==================== Age Tests ====================
    
    @Nested
    @DisplayName("Age Calculation")
    class AgeTests {
        
        @Test
        @DisplayName("Should calculate age correctly")
        void shouldCalculateAgeCorrectly() {
            PersianDate birthDate = PersianDate.of(1370, 5, 15);
            PersianDate asOf = PersianDate.of(1403, 10, 15);
            
            Age age = birthDate.getAge(asOf);
            
            assertEquals(33, age.getYears());
        }
        
        @Test
        @DisplayName("Should return zero age for same date")
        void shouldReturnZeroAgeForSameDate() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            Age age = date.getAge(date);
            
            assertTrue(age.isZero());
        }
        
        @Test
        @DisplayName("Age should format to Persian string")
        void ageShouldFormatToPersianString() {
            Age age = Age.of(33, 5, 10);
            String formatted = age.toPersianString();
            
            assertTrue(formatted.contains("۳۳"));
            assertTrue(formatted.contains("سال"));
        }
    }
    
    // ==================== With Methods Tests ====================
    
    @Nested
    @DisplayName("With Methods")
    class WithMethodsTests {
        
        @Test
        @DisplayName("Should change year")
        void shouldChangeYear() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.withYear(1404);
            
            assertEquals(1404, result.getYear());
            assertEquals(10, result.getMonthValue());
            assertEquals(15, result.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should get start of month")
        void shouldGetStartOfMonth() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.atStartOfMonth();
            
            assertEquals(1, result.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should get end of month")
        void shouldGetEndOfMonth() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate result = date.atEndOfMonth();
            
            assertEquals(30, result.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should get start of week (Saturday)")
        void shouldGetStartOfWeek() {
            // 1403/10/16 is Saturday - start of its own week
            PersianDate date = PersianDate.of(1403, 10, 16);
            PersianDate startOfWeek = date.atStartOfWeek();
            
            assertEquals(PersianDayOfWeek.SHANBE, startOfWeek.getDayOfWeek());
            assertEquals(16, startOfWeek.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should get end of week (Friday)")
        void shouldGetEndOfWeek() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            PersianDate endOfWeek = date.atEndOfWeek();
            
            assertEquals(PersianDayOfWeek.JOMEH, endOfWeek.getDayOfWeek());
        }
    }
    
    // ==================== Workday Tests ====================
    
    @Nested
    @DisplayName("Workday Methods")
    class WorkdayTests {
        
        @Test
        @DisplayName("Friday should be weekend")
        void fridayShouldBeWeekend() {
            // 1403/10/21 is Friday
            PersianDate friday = PersianDate.of(1403, 10, 21);
            
            assertTrue(friday.isWeekend());
            assertFalse(friday.isWorkday());
        }
        
        @Test
        @DisplayName("Saturday should be workday")
        void saturdayShouldBeWorkday() {
            // 1403/10/16 is Saturday
            PersianDate saturday = PersianDate.of(1403, 10, 16);
            
            assertTrue(saturday.isWorkday());
            assertFalse(saturday.isWeekend());
        }
        
        @Test
        @DisplayName("Should get next workday")
        void shouldGetNextWorkday() {
            // Thursday 1403/10/21 -> Next workday is Saturday 1403/10/23
            PersianDate thursday = PersianDate.of(1403, 10, 21);
            PersianDate nextWorkday = thursday.nextWorkday();
            
            assertEquals(PersianDayOfWeek.SHANBE, nextWorkday.getDayOfWeek());
        }
    }
    
    // ==================== HijriDate Tests ====================
    
    @Nested
    @DisplayName("HijriDate")
    class HijriDateTests {
        
        @Test
        @DisplayName("Should create valid Hijri date")
        void shouldCreateValidHijriDate() {
            HijriDate date = HijriDate.of(1446, 6, 15);
            
            assertEquals(1446, date.getYear());
            assertEquals(6, date.getMonthValue());
            assertEquals(15, date.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should convert Hijri to Persian")
        void shouldConvertHijriToPersian() {
            HijriDate hijri = HijriDate.of(1446, 6, 15);
            PersianDate persian = hijri.toPersian();
            
            assertNotNull(persian);
        }
        
        @Test
        @DisplayName("Should check Hijri leap year")
        void shouldCheckHijriLeapYear() {
            assertTrue(HijriDate.isLeapYear(2));
            assertTrue(HijriDate.isLeapYear(5));
            assertFalse(HijriDate.isLeapYear(1));
        }
        
        @Test
        @DisplayName("Should get start of Ramadan")
        void shouldGetStartOfRamadan() {
            HijriDate ramadan = HijriDate.startOfRamadan(1446);
            
            assertEquals(9, ramadan.getMonthValue());
            assertEquals(1, ramadan.getDayOfMonth());
        }
    }
    
    // ==================== DateRange Tests ====================
    
    @Nested
    @DisplayName("DateRange")
    class DateRangeTests {
        
        @Test
        @DisplayName("Should create date range")
        void shouldCreateDateRange() {
            PersianDate start = PersianDate.of(1403, 10, 1);
            PersianDate end = PersianDate.of(1403, 10, 10);
            
            DateRange range = DateRange.between(start, end);
            
            assertEquals(10, range.getDays());
        }
        
        @Test
        @DisplayName("Should check if range contains date")
        void shouldCheckIfRangeContainsDate() {
            DateRange range = DateRange.between(
                PersianDate.of(1403, 10, 1),
                PersianDate.of(1403, 10, 31)
            );
            
            assertTrue(range.contains(PersianDate.of(1403, 10, 15)));
            assertFalse(range.contains(PersianDate.of(1403, 11, 1)));
        }
        
        @Test
        @DisplayName("Should count workdays in range")
        void shouldCountWorkdaysInRange() {
            DateRange range = DateRange.between(
                PersianDate.of(1403, 10, 15),
                PersianDate.of(1403, 10, 21)
            );
            
            // 7 days, 1 Friday = 6 workdays
            assertEquals(6, range.countWorkdays());
        }
        
        @Test
        @DisplayName("Should create range for month")
        void shouldCreateRangeForMonth() {
            DateRange range = DateRange.ofMonth(1403, 10);
            
            assertEquals(1, range.getStart().getDayOfMonth());
            assertEquals(30, range.getEnd().getDayOfMonth());
        }
    }
    
    // ==================== Holiday Tests ====================
    
    @Nested
    @DisplayName("Holidays")
    class HolidayTests {
        
        @Test
        @DisplayName("Should get holidays for year")
        void shouldGetHolidaysForYear() {
            PersianHolidays holidays = PersianHolidays.of(1403);
            List<Holiday> all = holidays.getAll();
            
            assertFalse(all.isEmpty());
        }
        
        @Test
        @DisplayName("Should check if date is holiday")
        void shouldCheckIfDateIsHoliday() {
            PersianHolidays holidays = PersianHolidays.of(1403);
            
            // Nowruz
            assertTrue(holidays.isHoliday(PersianDate.of(1403, 1, 1)));
            assertTrue(holidays.isOfficialHoliday(PersianDate.of(1403, 1, 1)));
        }
        
        @Test
        @DisplayName("Should get Nowruz holidays")
        void shouldGetNowruzHolidays() {
            PersianHolidays holidays = PersianHolidays.of(1403);
            List<Holiday> nowruz = holidays.getNowruz();
            
            assertFalse(nowruz.isEmpty());
            assertTrue(nowruz.stream().anyMatch(h -> h.getName().contains("نوروز")));
        }
    }
    
    // ==================== PersianNumbers Tests ====================
    
    @Nested
    @DisplayName("Persian Numbers")
    class PersianNumbersTests {
        
        @Test
        @DisplayName("Should convert to Persian digits")
        void shouldConvertToPersianDigits() {
            assertEquals("۱۲۳", PersianNumbers.toPersian(123));
            assertEquals("۱۴۰۳/۱۰/۱۵", PersianNumbers.toPersian("1403/10/15"));
        }
        
        @Test
        @DisplayName("Should convert to Western digits")
        void shouldConvertToWesternDigits() {
            assertEquals("123", PersianNumbers.toWestern("۱۲۳"));
            assertEquals("1403/10/15", PersianNumbers.toWestern("۱۴۰۳/۱۰/۱۵"));
        }
        
        @Test
        @DisplayName("Should parse Persian number")
        void shouldParsePersianNumber() {
            assertEquals(123, PersianNumbers.parseInt("۱۲۳"));
        }
        
        @Test
        @DisplayName("Should convert to words")
        void shouldConvertToWords() {
            assertEquals("صفر", PersianNumbers.toWords(0));
            assertEquals("یک", PersianNumbers.toWords(1));
            assertEquals("بیست و سه", PersianNumbers.toWords(23));
        }
    }
    
    // ==================== Stream Tests ====================
    
    @Nested
    @DisplayName("Stream Operations")
    class StreamTests {
        
        @Test
        @DisplayName("Should stream dates until end")
        void shouldStreamDatesUntilEnd() {
            PersianDate start = PersianDate.of(1403, 10, 1);
            PersianDate end = PersianDate.of(1403, 10, 5);
            
            long count = start.datesUntil(end).count();
            assertEquals(4, count);
        }
        
        @Test
        @DisplayName("Should stream dates of month")
        void shouldStreamDatesOfMonth() {
            PersianDate date = PersianDate.of(1403, 10, 15);
            
            long count = date.datesOfMonth().count();
            assertEquals(30, count);
        }
    }
    
    // ==================== Edge Cases Tests ====================
    
    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Should handle year boundaries")
        void shouldHandleYearBoundaries() {
            PersianDate lastDay = PersianDate.of(1402, 12, 29);
            PersianDate nextDay = lastDay.plusDays(1);
            
            assertEquals(1403, nextDay.getYear());
            assertEquals(1, nextDay.getMonthValue());
            assertEquals(1, nextDay.getDayOfMonth());
        }
        
        @Test
        @DisplayName("Should handle leap year boundary")
        void shouldHandleLeapYearBoundary() {
            // 1403 is leap year, Esfand has 30 days
            PersianDate lastDay = PersianDate.of(1403, 12, 30);
            PersianDate nextDay = lastDay.plusDays(1);
            
            assertEquals(1404, nextDay.getYear());
            assertEquals(1, nextDay.getMonthValue());
        }
        
        @Test
        @DisplayName("Should handle first day of first year")
        void shouldHandleFirstDayOfFirstYear() {
            PersianDate date = PersianDate.of(1, 1, 1);
            assertNotNull(date);
        }
        
        @Test
        @DisplayName("Should handle equal dates in comparison")
        void shouldHandleEqualDatesInComparison() {
            PersianDate date1 = PersianDate.of(1403, 10, 15);
            PersianDate date2 = PersianDate.of(1403, 10, 15);
            
            assertEquals(0, date1.compareTo(date2));
            assertTrue(date1.isEqual(date2));
            assertEquals(date1.hashCode(), date2.hashCode());
        }
    }
    
    // ==================== PersianMonth Tests ====================
    
    @Nested
    @DisplayName("PersianMonth Enum")
    class PersianMonthTests {
        
        @Test
        @DisplayName("Should get month by value")
        void shouldGetMonthByValue() {
            assertEquals(PersianMonth.FARVARDIN, PersianMonth.of(1));
            assertEquals(PersianMonth.ESFAND, PersianMonth.of(12));
        }
        
        @Test
        @DisplayName("Should get month length")
        void shouldGetMonthLength() {
            assertEquals(31, PersianMonth.FARVARDIN.getDefaultLength());
            assertEquals(30, PersianMonth.MEHR.getDefaultLength());
            assertEquals(29, PersianMonth.ESFAND.getDefaultLength());
            assertEquals(30, PersianMonth.ESFAND.length(true)); // Leap year
        }
        
        @Test
        @DisplayName("Should get season for month")
        void shouldGetSeasonForMonth() {
            assertEquals(Season.SPRING, PersianMonth.FARVARDIN.getSeason());
            assertEquals(Season.WINTER, PersianMonth.DEY.getSeason());
        }
    }
    
    // ==================== PersianDayOfWeek Tests ====================
    
    @Nested
    @DisplayName("PersianDayOfWeek Enum")
    class PersianDayOfWeekTests {
        
        @Test
        @DisplayName("Should convert to ISO DayOfWeek")
        void shouldConvertToIsoDayOfWeek() {
            assertEquals(DayOfWeek.SATURDAY, PersianDayOfWeek.SHANBE.toIsoDayOfWeek());
            assertEquals(DayOfWeek.FRIDAY, PersianDayOfWeek.JOMEH.toIsoDayOfWeek());
        }
        
        @Test
        @DisplayName("Should create from ISO DayOfWeek")
        void shouldCreateFromIsoDayOfWeek() {
            assertEquals(PersianDayOfWeek.SHANBE, PersianDayOfWeek.from(DayOfWeek.SATURDAY));
            assertEquals(PersianDayOfWeek.JOMEH, PersianDayOfWeek.from(DayOfWeek.FRIDAY));
        }
        
        @Test
        @DisplayName("Should identify weekend")
        void shouldIdentifyWeekend() {
            assertTrue(PersianDayOfWeek.JOMEH.isWeekend());
            assertFalse(PersianDayOfWeek.SHANBE.isWeekend());
        }
    }
}
