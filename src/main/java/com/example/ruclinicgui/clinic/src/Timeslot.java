package com.example.ruclinicgui.clinic.src;

/**
 * The clinic.src.Timeslot class represents a specific time slot for appointments,
 * using hours and minutes. This class allows setting times based on predefined
 * slots and can compare and check equality between time slots.
 */
public class Timeslot implements Comparable<Timeslot> {
    private int hour;
    private int minute;

    /**
     * Default constructor for creating an empty Timeslot object.
     */
    public Timeslot() {
    }

    /**
     * Method: Sets the timeslot based on a predefined slot number.
     *
     * @param slot The slot number as a string (e.g., "1" for 9:00 AM).
     * @return true if the slot is successfully set, false if the slot is invalid.
     */
    public boolean setTimeslot(String slot) {
        switch (slot) {
            case "9:00 AM" -> { this.hour = 9; this.minute = 0; return true; }
            case "9:30 AM" -> { this.hour = 9; this.minute = 30; return true; }
            case "10:00 AM" -> { this.hour = 10; this.minute = 0; return true; }
            case "10:30 AM" -> { this.hour = 10; this.minute = 30; return true; }
            case "11:00 AM" -> { this.hour = 11; this.minute = 0; return true; }
            case "11:30 AM" -> { this.hour = 11; this.minute = 30; return true; }
            case "2:00 PM" -> { this.hour = 14; this.minute = 0; return true; }
            case "2:30 PM" -> { this.hour = 14; this.minute = 30; return true; }
            case "3:00 PM" -> { this.hour = 15; this.minute = 0; return true; }
            case "3:30 PM" -> { this.hour = 15; this.minute = 30; return true; }
            case "4:00 PM" -> { this.hour = 16; this.minute = 0; return true; }
            case "4:30 PM" -> { this.hour = 16; this.minute = 30; return true; }
            default -> { return false; }
        }
    }

    /**
     * Compares this timeslot to another timeslot based on the hour and minute.
     *
     * @param slot The timeslot to compare with.
     * @return     A negative integer, zero, or a positive integer as this timeslot is earlier than, equal to, or later than the specified timeslot.
     */
    @Override
    public int compareTo(Timeslot slot) {
        if (this.hour > slot.hour) {
            return 1;
        } else if (this.hour < slot.hour) {
            return -1;
        }
        if (this.minute > slot.minute) {
            return 1;
        } else if (this.minute < slot.minute) {
            return -1;
        }
        return 0;
    }

    /**
     * Checks if this timeslot is equal to another object.
     *
     * @param object The object to compare with.
     * @return       True if the object is a timeslot with the same hour and minute; otherwise false.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Timeslot) {
            Timeslot slot = (Timeslot) object;
            return this.hour == slot.hour && this.minute == slot.minute;
        }
        return false;
    }

    /**
     * Returns a string representation of the timeslot in a 12-hour format (e.g., 9:00 AM).
     *
     * @return A string representing the timeslot.
     */
    @Override
    public String toString() {
        String minuteString = String.format("%02d", this.minute);
        String period = (this.hour < 12) ? "AM" : "PM";

        // Convert the hour to 12-hour format
        int displayHour = this.hour % 12;
        if (displayHour == 0) {
            displayHour = 12; // 0 hours should be displayed as 12 AM/PM
        }

        return displayHour + ":" + minuteString + " " + period;
    }
}