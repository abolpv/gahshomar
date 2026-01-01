package io.github.abolpv.gahshomar.holiday;

import io.github.abolpv.gahshomar.PersianDate;

import java.util.List;
import java.util.Optional;

/**
 * Interface for holiday providers.
 * Allows custom holiday implementations for different regions/organizations.
 *
 * @author Abolfazl Azizi
 * @since 1.0.0
 */
public interface HolidayProvider {
    
    /**
     * Gets all holidays for a year.
     */
    List<Holiday> getHolidays(int year);
    
    /**
     * Gets official holidays for a year.
     */
    List<Holiday> getOfficialHolidays(int year);
    
    /**
     * Checks if a date is a holiday.
     */
    boolean isHoliday(PersianDate date);
    
    /**
     * Checks if a date is an official holiday.
     */
    boolean isOfficialHoliday(PersianDate date);
    
    /**
     * Gets the holiday for a specific date.
     */
    Optional<Holiday> getHoliday(PersianDate date);
    
    /**
     * Gets holidays for a specific month.
     */
    List<Holiday> getHolidaysInMonth(int year, int month);
    
    /**
     * Gets the name of this provider.
     */
    String getName();
    
    /**
     * Gets the description of this provider.
     */
    default String getDescription() {
        return getName();
    }
}
