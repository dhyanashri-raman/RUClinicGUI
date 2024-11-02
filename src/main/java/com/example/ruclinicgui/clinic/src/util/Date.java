package com.example.ruclinicgui.clinic.src.util;

import java.util.Calendar;

/**
 * The clinic.src.util.Date class represents a specific date with year, month, and day.
 * It provides methods to validate the date, check its properties,
 * and compare it with other dates. This class implements the Comparable
 * interface to allow for date comparisons.
 * @author Nithya Konduru, Dhyanashri Raman
 */
public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;

    /**
     * Constructor to initialize a clinic.src.util.Date object with a specific year, month, and day.
     *
     * @param year  The year of the date
     * @param month The month of the date (1-12)
     * @param day   The day of the month
     */
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Checks if the date is valid based on several criteria including
     * if the date is not today, is not before today, is within six months,
     * and is not on a weekend.
     *
     * @return true if the date is valid, false otherwise
     */
    public boolean isValid() {
        return isValidDate() && !isToday() && !isBeforeToday() && isWithinSixMonths() && !onWeekend();
    }

    /**
     * Validates the date by checking the day, month, and year.
     * Ensures that the month is between 1 and 12, and the day is valid
     * for the given month and year (considering leap years).
     *
     * @return true if the date is valid, false otherwise
     */
    public boolean isValidDate() {
        if (this.year < 1 || this.month < 1 || this.month > 12 || this.day < 1) {
            return false;
        }
        else {
            if (this.month == 2 && isLeapYear() && this.day > 29) {
                return false;
            } else if (this.month == 2 && !isLeapYear() && this.day > 28) {
                return false;
            }
            if (this.day > 31 && (this.month == 1 || this.month == 3 || this.month == 5 || this.month == 7
                    || this.month == 8 || this.month == 10 || this.month == 12)) {
                return false;
            }
            if (this.day > 30 && (this.month == 4 || this.month == 6 || this.month == 9 || this.month == 11)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the current year is a leap year.
     * A year is a leap year if it is divisible by 4,
     * but not divisible by 100 unless it is also divisible by 400.
     *
     * @return true if the year is a leap year, false otherwise
     */
    public boolean isLeapYear() {
        if (this.year % QUADRENNIAL == 0) {
            if (this.year % CENTENNIAL == 0) {
                if (this.year % QUATERCENTENNIAL == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks if the date is today's date.
     *
     * @return true if the date is today, false otherwise
     */
    public boolean isToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        return (this.year == calendar.get(Calendar.YEAR)
                && this.month == (calendar.get(Calendar.MONTH) + 1)
                && this.day == calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Checks if the date is before today.
     *
     * @return true if the date is before today, false otherwise
     */
    public boolean isBeforeToday() {
        Calendar today = Calendar.getInstance();
        today.setLenient(false);
        if (this.year < today.get(Calendar.YEAR)) {
            return true;
        } else if (this.year == today.get(Calendar.YEAR)) {
            if (this.month < today.get(Calendar.MONTH) + 1) {
                return true;
            } else if (this.month == today.get(Calendar.MONTH) + 1) {
                return this.day < today.get(Calendar.DAY_OF_MONTH);
            }
        }
        return false;
    }

    /**
     * Checks if the date falls on a weekend (Saturday or Sunday).
     *
     * @return true if the date is on a weekend, false otherwise
     */
    public boolean onWeekend() {
        Calendar appointmentDate = new Calendar.Builder()
                .setDate(this.year, this.month - 1, this.day)
                .build();
        int dayofweek = appointmentDate.get(Calendar.DAY_OF_WEEK);
        return (dayofweek == Calendar.SATURDAY || dayofweek == Calendar.SUNDAY);
    }

    /**
     * Checks if the date is within six months from the current date.
     *
     * @return true if the date is within six months, false otherwise
     */
    public boolean isWithinSixMonths() {
        Calendar today = Calendar.getInstance();
        today.setLenient(false);
        Calendar appointmentDate = new Calendar.Builder()
                .setDate(this.year, this.month - 1, this.day)
                .build();
        Calendar sixMonthsLater = Calendar.getInstance();
        sixMonthsLater.setLenient(false);
        sixMonthsLater.add(Calendar.MONTH, 6);
        return !appointmentDate.before(today) && !appointmentDate.after(sixMonthsLater);
    }

    /**
     * Checks if the date is a future date compared to the current date.
     *
     * @return true if the date is in the future, false otherwise
     */
    public boolean isFutureDate() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setLenient(false);
        Calendar date = new Calendar.Builder()
                .setDate(this.year, this.month - 1, this.day)
                .build();
        return date.after(currentDate);
    }

    @Override
    /**
     * Compares this date to another date.
     * Returns a negative integer, zero, or a positive integer
     * as this date is less than, equal to, or greater than the specified date.
     *
     * @param o The date to compare with
     * @return comparison result as an integer
     */
    public int compareTo(Date o) {
        int yearComparison = this.year - o.year;
        int monthComparison = this.month - o.month;
        int dayComparison = this.day - o.day;
        if (yearComparison > 0) {
            return 1;
        } else if (yearComparison < 0) {
            return -1;
        }
        if (monthComparison > 0) {
            return 1;
        } else if (monthComparison < 0) {
            return -1;
        }
        if (dayComparison > 0) {
            return 1;
        } else if (dayComparison < 0) {
            return -1;
        }
        return 0;
    }

    @Override
    /**
     * Checks if this date is equal to another object.
     * Two dates are considered equal if they have the same year, month, and day.
     *
     * @param o The object to compare with
     * @return true if the dates are equal, false otherwise
     */
    public boolean equals(Object o) {
        if (o instanceof Date) {
            Date dateEx = (Date) o;
            return this.year == dateEx.year
                    && this.month == dateEx.month
                    && this.day == dateEx.day;
        }
        return false;
    }

    @Override
    /**
     * Returns a string representation of the date in MM/DD/YYYY format.
     *
     * @return A string representing the date
     */
    public String toString() {
        return this.month + "/" + this.day + "/" + this.year;
    }

    /**
     * Gets the year of the date.
     *
     * @return The year of the date
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Gets the month of the date.
     *
     * @return The month of the date
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Gets the day of the date.
     *
     * @return The day of the date
     */
    public int getDay() {
        return this.day;
    }
}