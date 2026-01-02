# Changelog

All notable changes to Gahshomar will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [1.0.0] - 2025-01-02

### 🎉 Initial Release

#### Added
- **PersianDate** - Immutable Persian/Jalali/Shamsi date with 80+ APIs
- **HijriDate** - Immutable Hijri/Islamic lunar date
- **PersianDateTime** & **HijriDateTime** - Date with time support
- **ZonedPersianDateTime** - Timezone-aware Persian datetime
- **DateConverter** - Bidirectional Persian ↔ Gregorian ↔ Hijri conversion
- **PersianNumbers** - Convert digits (۱۲۳ ↔ 123) and numbers to words
- **DateFormatter** - Custom pattern formatting with Persian digit support
- **DateParser** - Auto-detecting date parser
- **RelativeTime** - Human-readable relative dates (امروز، دیروز، فردا)
- **Age** - Age calculation with Persian/English formatting
- **DateRange** - Date range operations with Stream API
- **DateStream** - Fluent date stream utilities
- **Period** - ISO-8601 compatible periods
- **PersianHolidays** - Iranian solar holidays
- **HijriHolidays** - Islamic lunar holidays
- **IranTimeZone** - Asia/Tehran timezone utilities
- **DSTRule** - Daylight Saving Time handling

#### Technical
- Zero external dependencies
- Java 17+ compatible
- 133 unit tests (100% passing)
- 645+ public APIs