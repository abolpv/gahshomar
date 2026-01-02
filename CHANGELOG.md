# Changelog

All notable changes to Gahshomar will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.2] - 2025-01-02

### Fixed
- Simplified JitPack configuration for successful builds
- JitPack now correctly builds with Java 17

## [1.0.1] - 2025-01-02

### Fixed
- JitPack build configuration updates

## [1.0.0] - 2025-01-02

### 🎉 Initial Release

#### Core Features
- **PersianDate** - Immutable Persian/Jalali/Shamsi date with 80+ APIs
- **HijriDate** - Immutable Hijri/Islamic lunar date
- **PersianDateTime** & **HijriDateTime** - Date with time support
- **ZonedPersianDateTime** - Timezone-aware Persian datetime

#### Conversion
- **DateConverter** - Bidirectional Persian ↔ Gregorian ↔ Hijri conversion
- Accurate leap year calculation using 33-year cycle

#### Formatting
- **DateFormatter** - Custom pattern formatting with Persian digit support
- **DateParser** - Auto-detecting date parser (supports both Western and Persian digits)
- **PersianNumbers** - Convert digits (۱۲۳ ↔ 123) and numbers to words
- **RelativeTime** - Human-readable relative dates (امروز، دیروز، فردا)

#### Date Operations
- **Age** - Age calculation with Persian/English formatting
- **DateRange** - Date range operations with Stream API
- **DateStream** - Fluent date stream utilities
- **Period** - ISO-8601 compatible periods

#### Holidays
- **PersianHolidays** - Iranian solar holidays (Nowruz, etc.)
- **HijriHolidays** - Islamic lunar holidays (Ramadan, Eid, etc.)

#### Timezone
- **IranTimeZone** - Asia/Tehran timezone utilities
- **DSTRule** - Daylight Saving Time handling for Iran

#### Technical
- Zero external dependencies
- Java 17+ compatible
- 133 unit tests (100% passing)
- 645+ public APIs
- 36 source files
