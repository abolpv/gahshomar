<p align="center">
  <img src="assets/logo.svg" alt="Gahshomar Logo" width="120" height="120">
</p>

<h1 align="center">Gahshomar</h1>
<h3 align="center">گاه‌شمار</h3>

<p align="center">
  <strong>A powerful, pure Java library for Persian (Jalali), Hijri, and Gregorian date conversion</strong>
</p>

<p align="center">
  <a href="#features">Features</a> •
  <a href="#installation">Installation</a> •
  <a href="#quick-start">Quick Start</a> •
  <a href="#api-reference">API Reference</a> •
  <a href="#contributing">Contributing</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17+">
  <img src="https://img.shields.io/badge/License-Apache%202.0-blue?style=for-the-badge" alt="License">
  <img src="https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge" alt="Build">
  <img src="https://jitpack.io/v/abolpv/gahshomar.svg?style=for-the-badge" alt="JitPack">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Zero-Dependencies-success?style=flat-square" alt="Zero Dependencies">
  <img src="https://img.shields.io/badge/100%25-Pure%20Java-orange?style=flat-square" alt="Pure Java">
  <img src="https://img.shields.io/badge/Kotlin-Compatible-7F52FF?style=flat-square" alt="Kotlin Compatible">
</p>

---

## Why Gahshomar?

**Gahshomar** (گاه‌شمار - Persian for "calendar") is a comprehensive date library designed specifically for Persian/Iranian developers. It provides:

- 🗓️ **Complete Calendar Support** - Persian (Jalali/Shamsi), Hijri (Islamic), and Gregorian
- 🔄 **Accurate Conversions** - Based on proven astronomical algorithms
- 🎯 **100+ APIs** - Everything you need for date manipulation
- ⚡ **Zero Dependencies** - Pure Java, no external libraries
- 🇮🇷 **Persian-First Design** - Full Persian language support
- 📱 **Kotlin Compatible** - Works seamlessly with Kotlin projects

---

## Features

| Feature | Description |
|---------|-------------|
| 🔄 **Date Conversion** | Convert between Persian, Hijri, and Gregorian calendars |
| 📅 **Leap Year** | Accurate leap year calculation for all calendars |
| ➕ **Date Arithmetic** | Add/subtract days, months, years |
| 📊 **Date Comparison** | Compare dates, check ranges |
| 🎂 **Age Calculation** | Calculate age with years, months, days |
| 📝 **Formatting** | Format dates with Persian/English patterns |
| 🔢 **Persian Numbers** | Convert to/from Persian digits (۱۲۳) |
| ⏰ **Relative Time** | "امروز", "دیروز", "۳ روز پیش" |
| 🎉 **Holidays** | Iranian official holidays (solar + lunar) |
| 📆 **Date Range** | Work with date ranges, count workdays |
| 🌙 **Hijri Support** | Full Islamic calendar support |
| 🔍 **Validation** | Comprehensive date validation |

---

## Installation

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.abolpv</groupId>
        <artifactId>gahshomar</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.abolpv:gahshomar:1.0.0'
}
```

### Gradle Kotlin DSL

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.abolpv:gahshomar:1.0.0")
}
```

---

## Quick Start

### Create a Persian Date

```java
import io.github.abolpv.gahshomar.PersianDate;

// From year, month, day
PersianDate date = PersianDate.of(1403, 10, 15);

// Today
PersianDate today = PersianDate.now();

// From Gregorian
PersianDate fromGregorian = PersianDate.from(LocalDate.of(2025, 1, 4));

// Parse from string
PersianDate parsed = PersianDate.parse("1403/10/15");
PersianDate parsedPersian = PersianDate.parse("۱۴۰۳/۱۰/۱۵");
```

### Convert Between Calendars

```java
PersianDate persian = PersianDate.of(1403, 10, 15);

// To Gregorian
LocalDate gregorian = persian.toGregorian();  // 2025-01-04

// To Hijri
HijriDate hijri = persian.toHijri();

// From Gregorian to Persian
PersianDate fromGregorian = PersianDate.from(LocalDate.now());
```

### Date Arithmetic

```java
PersianDate date = PersianDate.of(1403, 10, 15);

// Add/Subtract
PersianDate nextWeek = date.plusDays(7);
PersianDate nextMonth = date.plusMonths(1);
PersianDate lastYear = date.minusYears(1);

// Difference
long daysBetween = date.daysUntil(otherDate);
long monthsBetween = date.monthsUntil(otherDate);
```

### Get Date Information

```java
PersianDate date = PersianDate.of(1403, 10, 15);

int year = date.getYear();              // 1403
int month = date.getMonthValue();       // 10
int day = date.getDayOfMonth();         // 15
int dayOfYear = date.getDayOfYear();    // 289

String monthName = date.getMonthName();      // دی
String dayName = date.getDayOfWeekName();    // شنبه
Season season = date.getSeason();            // WINTER

boolean isLeap = date.isLeapYear();          // true
int monthLength = date.lengthOfMonth();      // 30
```

### Format Dates

```java
PersianDate date = PersianDate.of(1403, 10, 15);

// Default format
date.format();                           // 1403/10/15

// Custom patterns
date.format("yyyy-MM-dd");               // 1403-10-15
date.format("d MMMM yyyy");              // 15 دی 1403
date.format("EEEE d MMMM yyyy");         // شنبه 15 دی 1403

// With Persian digits
date.format("yyyy/MM/dd", true);         // ۱۴۰۳/۱۰/۱۵

// Relative time
date.toRelative();                       // امروز / دیروز / ۳ روز پیش
```

### Calculate Age

```java
PersianDate birthDate = PersianDate.of(1370, 5, 15);

// Full age
Age age = birthDate.getAge();
age.getYears();                          // 33
age.getMonths();                         // 5
age.getDays();                           // 10
age.toPersianString();                   // ۳۳ سال و ۵ ماه و ۱۰ روز

// Simple
int years = birthDate.getAgeInYears();   // 33

// Birthday
boolean isBirthday = birthDate.isBirthdayToday();
long daysUntil = birthDate.daysUntilNextBirthday();
```

### Special Dates

```java
PersianDate date = PersianDate.of(1403, 10, 15);

// Start/End of month
PersianDate firstOfMonth = date.atStartOfMonth();
PersianDate lastOfMonth = date.atEndOfMonth();

// Start/End of year
PersianDate nowruz = date.atStartOfYear();
PersianDate endOfYear = date.atEndOfYear();

// Start/End of week (Saturday-Friday)
PersianDate saturday = date.atStartOfWeek();
PersianDate friday = date.atEndOfWeek();

// Nowruz
PersianDate nowruz1404 = PersianDate.nowruz(1404);
```

### Workdays

```java
PersianDate date = PersianDate.of(1403, 10, 15);

boolean isWorkday = date.isWorkday();
boolean isWeekend = date.isWeekend();        // Friday

PersianDate nextWorkday = date.nextWorkday();
long workdays = date.workdaysUntil(otherDate);
```

### Holidays

```java
PersianHolidays holidays = PersianHolidays.of(1403);

// All holidays
List<Holiday> all = holidays.getAll();

// Check if holiday
boolean isHoliday = holidays.isHoliday(date);
boolean isOfficial = holidays.isOfficialHoliday(date);

// Get holiday info
Optional<Holiday> holiday = holidays.getHoliday(date);

// Nowruz holidays
List<Holiday> nowruz = holidays.getNowruz();
```

### Date Range

```java
PersianDate start = PersianDate.of(1403, 10, 1);
PersianDate end = PersianDate.of(1403, 10, 31);

DateRange range = DateRange.between(start, end);

long days = range.getDays();                     // 31
boolean contains = range.contains(someDate);
long workdays = range.countWorkdays();

// Iterate over range
for (PersianDate date : range) {
    System.out.println(date);
}

// Stream
range.stream()
     .filter(PersianDate::isWorkday)
     .forEach(System.out::println);
```

### Hijri Calendar

```java
// Create Hijri date
HijriDate hijri = HijriDate.of(1446, 6, 15);
HijriDate today = HijriDate.now();

// Convert
LocalDate gregorian = hijri.toGregorian();
PersianDate persian = hijri.toPersian();

// Special dates
HijriDate ramadan = HijriDate.startOfRamadan(1446);
HijriDate eidFitr = HijriDate.eidAlFitr(1446);
HijriDate eidAdha = HijriDate.eidAlAdha(1446);

// Check
boolean isRamadan = hijri.isRamadan();
boolean isSacred = hijri.isSacredMonth();
```

### Persian Numbers

```java
// To Persian digits
PersianNumbers.toPersian(123);           // ۱۲۳
PersianNumbers.toPersian("1403/10/15");  // ۱۴۰۳/۱۰/۱۵

// To Western digits
PersianNumbers.toWestern("۱۲۳");         // 123

// Parse
int num = PersianNumbers.parseInt("۱۲۳");  // 123

// To words
PersianNumbers.toWords(23);              // بیست و سه
PersianNumbers.toOrdinal(1);             // اول
```

---

## API Reference

### PersianDate

| Method | Description |
|--------|-------------|
| `of(year, month, day)` | Create from components |
| `now()` | Current date |
| `from(LocalDate)` | From Gregorian |
| `parse(String)` | Parse date string |
| `toGregorian()` | Convert to Gregorian |
| `toHijri()` | Convert to Hijri |
| `plusDays/Months/Years(n)` | Add time |
| `minusDays/Months/Years(n)` | Subtract time |
| `daysUntil(date)` | Days between dates |
| `getAge()` | Calculate age |
| `format(pattern)` | Format date |
| `toRelative()` | Relative time string |
| `isLeapYear()` | Check leap year |
| `isWorkday()` | Check if workday |

### HijriDate

| Method | Description |
|--------|-------------|
| `of(year, month, day)` | Create from components |
| `now()` | Current date |
| `toGregorian()` | Convert to Gregorian |
| `toPersian()` | Convert to Persian |
| `startOfRamadan(year)` | First of Ramadan |
| `eidAlFitr(year)` | Eid al-Fitr date |
| `eidAlAdha(year)` | Eid al-Adha date |
| `isRamadan()` | Check if Ramadan |

---

## Requirements

- **Java 17** or higher
- No external dependencies

---

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

```bash
# Clone
git clone https://github.com/abolpv/gahshomar.git
cd gahshomar

# Build
mvn clean compile

# Test
mvn test
```

---

## License

This project is licensed under the **Apache License 2.0**.

---

## Author

<p align="center">
  <strong>Abolfazl Azizi</strong>
  <br>
  <a href="https://github.com/abolpv">GitHub</a>
</p>

---

<p align="center">
  Made with ❤️ for Persian developers
  <br><br>
  ⭐ Star this repo if you find it useful!
</p>
