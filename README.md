<div align="center">
  <img src="assets/logo.svg" alt="Gahshomar Logo" width="180" height="180">
  
  # گاه‌شمار | Gahshomar
  
  **A Modern Persian (Jalali/Shamsi) & Hijri Calendar Library for Java**

  [![Java Version](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
  [![License](https://img.shields.io/badge/License-Apache%202.0-blue?style=for-the-badge)](LICENSE)
  [![Tests](https://img.shields.io/badge/Tests-133%20Passing-brightgreen?style=for-the-badge)](src/test)
  [![Zero Dependencies](https://img.shields.io/badge/Dependencies-Zero-purple?style=for-the-badge)]()
  
  [![Build Status](https://github.com/abolpv/gahshomar/actions/workflows/maven.yml/badge.svg)](https://github.com/abolpv/gahshomar/actions)
  [![CodeQL](https://github.com/abolpv/gahshomar/actions/workflows/codeql.yml/badge.svg)](https://github.com/abolpv/gahshomar/actions/workflows/codeql.yml)
  [![JitPack](https://jitpack.io/v/abolpv/gahshomar.svg)](https://jitpack.io/#abolpv/gahshomar)
  [![GitHub release](https://img.shields.io/github/v/release/abolpv/gahshomar)](https://github.com/abolpv/gahshomar/releases)

  [English](#-features) | [فارسی](#-ویژگیها)

</div>

---

## ✨ Why Gahshomar?

| Feature | Gahshomar | Others |
|---------|-----------|--------|
| 🚫 Zero Dependencies | ✅ | ❌ Usually require ICU4J |
| ☀️ Persian Calendar | ✅ Full support | ✅ |
| 🌙 Hijri Calendar | ✅ Full support | ⚠️ Limited |
| 🔄 Bidirectional Conversion | ✅ Persian↔Gregorian↔Hijri | ⚠️ Usually one-way |
| 🔢 Persian Numbers | ✅ Digits + Words + Ordinal | ❌ |
| 🎊 Iranian Holidays | ✅ Solar + Lunar | ❌ |
| ⏰ Relative Time | ✅ امروز، دیروز، فردا | ❌ |
| 📊 Stream API | ✅ DateStream, DateRange | ❌ |
| 🇮🇷 Iran Timezone + DST | ✅ Built-in | ❌ |

---

## 🌟 Features

- ☀️ **Persian Calendar** - Full Jalali/Shamsi calendar support with accurate leap year calculation (33-year cycle)
- 🌙 **Hijri Calendar** - Complete Islamic lunar calendar with 30-year cycle
- 🔄 **Bidirectional Conversion** - Persian ↔ Gregorian ↔ Hijri seamless conversions
- 📅 **DateTime Support** - Date and time handling with timezone awareness
- 🇮🇷 **Iran Timezone** - Built-in Asia/Tehran support with DST handling
- 🎊 **Holidays** - Iranian solar & Islamic lunar holidays
- 🔢 **Persian Numbers** - Convert digits (۱۲۳ ↔ 123) and numbers to words
- ⏰ **Relative Time** - Human-readable relative dates (امروز، دیروز، فردا)
- 📊 **Date Ranges** - Range operations with Stream API support
- 🚫 **Zero Dependencies** - Pure Java, no external libraries required
- ✅ **Well Tested** - 133+ unit tests with comprehensive coverage

---

## 📦 Installation

### Maven (JitPack) - Recommended

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.abolpv</groupId>
    <artifactId>gahshomar</artifactId>
    <version>v1.0.2</version>
</dependency>
```

### Gradle (JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.abolpv:gahshomar:v1.0.2'
}
```

### Gradle Kotlin DSL

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.abolpv:gahshomar:v1.0.2")
}
```

### Manual

Download the JAR from [Releases](https://github.com/abolpv/gahshomar/releases) and add it to your classpath.

---

## 🚀 Quick Start

### Creating Dates

```java
import io.github.abolpv.gahshomar.*;

// Current date
PersianDate today = PersianDate.now();

// Specific date
PersianDate date = PersianDate.of(1403, 10, 15);

// Parse from string (supports Persian digits!)
PersianDate parsed = PersianDate.parse("1403/10/15");
PersianDate parsedPersian = PersianDate.parse("۱۴۰۳/۱۰/۱۵");
```

### Date Conversions

```java
// Persian to Gregorian
LocalDate gregorian = PersianDate.of(1403, 1, 1).toGregorian();
// Result: 2024-03-19

// Gregorian to Persian
PersianDate persian = PersianDate.from(LocalDate.of(2025, 1, 3));
// Result: 1403/10/15

// Persian to Hijri
HijriDate hijri = PersianDate.of(1403, 10, 15).toHijri();

// Hijri to Persian
PersianDate fromHijri = HijriDate.of(1446, 7, 3).toPersian();
```

### Date Arithmetic

```java
PersianDate date = PersianDate.of(1403, 10, 15);

// Add/subtract
PersianDate future = date.plusDays(30);
PersianDate past = date.minusMonths(2);
PersianDate nextYear = date.plusYears(1);

// Navigation
PersianDate startOfMonth = date.atStartOfMonth();
PersianDate endOfMonth = date.atEndOfMonth();
PersianDate startOfWeek = date.atStartOfWeek();  // Saturday
PersianDate endOfWeek = date.atEndOfWeek();      // Friday
```

### Formatting

```java
PersianDate date = PersianDate.of(1403, 10, 15);

// Default format
date.format();                    // "1403/10/15"

// Custom patterns
date.format("yyyy-MM-dd");        // "1403-10-15"
date.format("dd MMMM yyyy");      // "15 دی 1403"
date.format("EEEE dd MMMM");      // "جمعه 15 دی"

// Persian digits
date.format("yyyy/MM/dd", true);  // "۱۴۰۳/۱۰/۱۵"

// Relative time
date.toRelative();                // "امروز" / "دیروز" / "فردا" / "۳ روز پیش"
```

### Date Information

```java
PersianDate date = PersianDate.of(1403, 10, 15);

date.getMonthName();       // "دی"
date.getDayOfWeek();       // PersianDayOfWeek.JOMEH (جمعه)
date.getDayOfYear();       // 289
date.getQuarter();         // 4
date.getSeason();          // Season.WINTER (زمستان)
date.lengthOfMonth();      // 30
date.lengthOfYear();       // 366 (leap year)
date.isLeapYear();         // true
date.isWeekend();          // true (Friday)
date.isWorkday();          // false
```

### Age Calculation

```java
PersianDate birthDate = PersianDate.of(1370, 5, 15);
Age age = birthDate.getAge();

age.getYears();           // 33
age.getMonths();          // 5
age.getDays();            // 0
age.toPersianString();    // "۳۳ سال و ۵ ماه"
age.toEnglishString();    // "33 years, 5 months"
```

### Persian Numbers

```java
import io.github.abolpv.gahshomar.format.PersianNumbers;

// Convert digits
PersianNumbers.toPersian(1403);          // "۱۴۰۳"
PersianNumbers.toPersian("1403/10/15");  // "۱۴۰۳/۱۰/۱۵"
PersianNumbers.toWestern("۱۴۰۳");        // "1403"

// Number to words
PersianNumbers.toWords(23);              // "بیست و سه"
PersianNumbers.toWords(1403);            // "یک هزار و چهارصد و سه"

// Ordinal numbers
PersianNumbers.toOrdinal(1);             // "اول"
PersianNumbers.toOrdinal(15);            // "پانزدهم"
```

### DateTime with Time

```java
// Create with time
PersianDateTime dt = PersianDateTime.of(1403, 10, 15, 14, 30, 45);

// Current datetime
PersianDateTime now = PersianDateTime.now();

// Time operations
dt.plusHours(5);
dt.minusMinutes(30);

// Format
dt.format("yyyy/MM/dd HH:mm:ss");  // "1403/10/15 14:30:45"

// Convert to Gregorian
LocalDateTime gregorian = dt.toGregorian();
```

### Timezone Support

```java
import io.github.abolpv.gahshomar.zone.*;

// Current time in Iran
ZonedDateTime iranNow = IranTimeZone.now();

// Zoned Persian DateTime
ZonedPersianDateTime zdt = ZonedPersianDateTime.of(
    1403, 10, 15, 14, 30, IranTimeZone.IRAN
);

// Convert between zones
ZonedPersianDateTime utc = zdt.withZoneSameInstant(ZoneOffset.UTC);

// DST info
DSTRule.isDST(PersianDate.now());          // Check if DST is active
DSTRule.getDSTInfo(PersianDate.now());     // Get DST description
```

### Holidays

```java
import io.github.abolpv.gahshomar.holiday.*;

// Persian holidays
PersianHolidays holidays = PersianHolidays.of(1403);

holidays.isHoliday(PersianDate.of(1403, 1, 1));      // true (Nowruz)
holidays.isOfficialHoliday(date);                    // Check official holidays
holidays.getHolidaysIn(PersianMonth.FARVARDIN);      // Get month's holidays
holidays.getNowruz();                                 // Get Nowruz holidays

// Hijri holidays  
HijriHolidays hijriHolidays = HijriHolidays.of(1446);
hijriHolidays.getAll();                              // All Islamic holidays
hijriHolidays.startOfRamadan();                      // First day of Ramadan
```

### Date Ranges

```java
import io.github.abolpv.gahshomar.range.*;

// Create range
DateRange range = DateRange.between(
    PersianDate.of(1403, 1, 1),
    PersianDate.of(1403, 1, 31)
);

range.getDays();                          // 31
range.contains(PersianDate.of(1403, 1, 15));  // true
range.countWorkdays();                    // Count working days
range.overlaps(anotherRange);             // Check overlap

// Month/Year ranges
DateRange month = DateRange.ofMonth(1403, 1);
DateRange year = DateRange.ofYear(1403);
```

### Date Streams

```java
import io.github.abolpv.gahshomar.range.DateStream;

// Stream of dates
DateStream.range(start, end)
    .filter(d -> d.isWorkday())
    .forEach(System.out::println);

// All days in a month
DateStream.ofMonth(1403, 1).count();      // 31

// Only workdays
DateStream.workdays(start, end);

// Only Fridays
DateStream.fridays(start, end);

// Leap years in range
DateStream.leapYears(1390, 1410);
```

### Periods

```java
import io.github.abolpv.gahshomar.range.Period;

// Create period
Period period = Period.of(2, 3, 15);      // 2 years, 3 months, 15 days

// Calculate period between dates
Period between = Period.between(date1, date2);

// Add period to date
PersianDate result = period.addTo(date);

// Format
period.toPersianString();   // "۲ سال و ۳ ماه و ۱۵ روز"
period.toString();          // "P2Y3M15D" (ISO-8601)
```

### Hijri Calendar

```java
// Create Hijri date
HijriDate hijri = HijriDate.of(1446, 9, 1);

// Ramadan utilities
HijriDate.startOfRamadan(1446);
HijriDate.isRamadan(hijri);

// Sacred months
hijri.getMonth().isSacredMonth();

// Convert
PersianDate persian = hijri.toPersian();
LocalDate gregorian = hijri.toGregorian();
```

---

## 📊 API Statistics

| Metric | Value |
|--------|-------|
| **Public APIs** | 645+ |
| **Unit Tests** | 133 |
| **Source Files** | 36 |
| **Dependencies** | Zero |
| **Min Java Version** | 17 |

---

## 📚 Package Structure

```
io.github.abolpv.gahshomar
├── PersianDate          // Main Persian date class
├── PersianDateTime      // Persian date with time
├── HijriDate            // Hijri/Islamic date
├── HijriDateTime        // Hijri date with time
├── core/
│   ├── JalaliAlgorithm  // Persian calendar algorithms
│   ├── HijriAlgorithm   // Hijri calendar algorithms
│   ├── DateConverter    // Conversion utilities
│   ├── LeapYearRule     // Leap year calculations
│   └── CalendarType     // Calendar type enum
├── temporal/
│   ├── PersianMonth     // Persian month enum
│   ├── HijriMonth       // Hijri month enum
│   ├── PersianDayOfWeek // Day of week enum
│   ├── Season           // Season enum
│   └── Age              // Age calculation
├── format/
│   ├── DateFormatter    // Pattern-based formatting
│   ├── DateParser       // Auto-detecting parser
│   ├── PersianNumbers   // Number conversion
│   ├── RelativeTime     // Relative time formatting
│   └── FormatPattern    // Predefined patterns
├── holiday/
│   ├── PersianHolidays  // Iranian holidays
│   ├── HijriHolidays    // Islamic holidays
│   ├── Holiday          // Holiday model
│   └── Occasion         // Non-holiday occasions
├── range/
│   ├── DateRange        // Date range operations
│   ├── DateStream       // Stream utilities
│   └── Period           // Date periods
├── zone/
│   ├── IranTimeZone     // Iran timezone utilities
│   ├── ZonedPersianDateTime // Timezone-aware datetime
│   └── DSTRule          // DST calculations
├── util/
│   ├── DateUtils        // Date utilities
│   └── Preconditions    // Validation helpers
└── exception/
    ├── DateException    // Base exception
    ├── InvalidDateException
    ├── ParseException
    └── ConversionException
```

---

## 🔧 Requirements

- **Java 17** or higher
- No external dependencies

---

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=GahshomarTest
```

---

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📞 Support

- 🐛 [Report Bug](https://github.com/abolpv/gahshomar/issues)
- 💡 [Request Feature](https://github.com/abolpv/gahshomar/issues)
- 📖 [Documentation](https://github.com/abolpv/gahshomar#readme)

---

## 👨‍💻 Author

**Abolfazl** - [abolpv](https://github.com/abolpv)

---

<a name="-ویژگیها"></a>

<div dir="rtl">

## 🌟 ویژگی‌ها

- ☀️ **تقویم شمسی** - پشتیبانی کامل از تقویم جلالی/شمسی با محاسبه دقیق سال کبیسه (چرخه ۳۳ ساله)
- 🌙 **تقویم هجری** - تقویم قمری اسلامی با چرخه ۳۰ ساله
- 🔄 **تبدیل دوطرفه** - تبدیل شمسی ↔ میلادی ↔ هجری
- 📅 **پشتیبانی از زمان** - مدیریت تاریخ و زمان با آگاهی از منطقه زمانی
- 🇮🇷 **منطقه زمانی ایران** - پشتیبانی داخلی از Asia/Tehran با مدیریت تغییر ساعت
- 🎊 **تعطیلات** - تعطیلات شمسی ایران و تعطیلات قمری اسلامی
- 🔢 **اعداد فارسی** - تبدیل ارقام (۱۲۳ ↔ 123) و اعداد به حروف
- ⏰ **زمان نسبی** - تاریخ‌های نسبی خوانا (امروز، دیروز، فردا)
- 📊 **بازه تاریخ** - عملیات بازه با پشتیبانی Stream API
- 🚫 **بدون وابستگی** - جاوای خالص، بدون کتابخانه خارجی
- ✅ **تست شده** - بیش از ۱۳۳ تست واحد

---

## 📦 نصب

### Maven (JitPack)

</div>

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.abolpv</groupId>
    <artifactId>gahshomar</artifactId>
    <version>v1.0.2</version>
</dependency>
```

<div dir="rtl">

### Gradle

</div>

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.abolpv:gahshomar:v1.0.2'
}
```

<div dir="rtl">

---

## 🚀 شروع سریع

### ایجاد تاریخ

</div>

```java
import io.github.abolpv.gahshomar.*;

// تاریخ امروز
PersianDate today = PersianDate.now();

// تاریخ مشخص
PersianDate date = PersianDate.of(1403, 10, 15);

// پارس از رشته (با پشتیبانی از اعداد فارسی!)
PersianDate parsed = PersianDate.parse("1403/10/15");
PersianDate parsedPersian = PersianDate.parse("۱۴۰۳/۱۰/۱۵");
```

<div dir="rtl">

### تبدیل تاریخ

</div>

```java
// شمسی به میلادی
LocalDate gregorian = PersianDate.of(1403, 1, 1).toGregorian();

// میلادی به شمسی
PersianDate persian = PersianDate.from(LocalDate.of(2025, 1, 3));

// شمسی به هجری
HijriDate hijri = PersianDate.of(1403, 10, 15).toHijri();
```

<div dir="rtl">

### محاسبات تاریخ

</div>

```java
PersianDate date = PersianDate.of(1403, 10, 15);

// اضافه/کم کردن
date.plusDays(30);      // ۳۰ روز بعد
date.minusMonths(2);    // ۲ ماه قبل
date.plusYears(1);      // یک سال بعد

// ناوبری
date.atStartOfMonth();  // اول ماه
date.atEndOfMonth();    // آخر ماه
date.atStartOfWeek();   // شنبه
date.atEndOfWeek();     // جمعه
```

<div dir="rtl">

### محاسبه سن

</div>

```java
PersianDate birthDate = PersianDate.of(1370, 5, 15);
Age age = birthDate.getAge();

age.toPersianString();  // "۳۳ سال و ۵ ماه"
```

<div dir="rtl">

### اعداد فارسی

</div>

```java
PersianNumbers.toPersian(1403);     // "۱۴۰۳"
PersianNumbers.toWestern("۱۴۰۳");   // "1403"
PersianNumbers.toWords(23);         // "بیست و سه"
PersianNumbers.toOrdinal(1);        // "اول"
```

---

<div align="center">

**Made with ❤️ by [Abolfazl](https://github.com/abolpv)**

</div>
