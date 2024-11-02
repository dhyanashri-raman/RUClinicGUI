package com.example.ruclinicgui.clinic.src;


import com.example.ruclinicgui.clinic.src.util.Date;

/**
 * Represents an imaging appointment at the clinic.
 * This appointment includes details such as the date, timeslot, patient, provider, and the radiology room.
 * @author Nithya Konduru, Dhyanashri Raman
 */
public class Imaging extends Appointment {
    private Radiology room;

    /**
     * Constructs an Imaging appointment with a specific date, timeslot, patient, provider, and radiology room.
     *
     * @param date      The date of the appointment.
     * @param timeslot  The timeslot of the appointment.
     * @param patient   The patient attending the appointment.
     * @param provider  The provider overseeing the appointment.
     * @param room      The radiology room where the appointment takes place.
     */
    public Imaging(Date date, Timeslot timeslot, Person patient, Person provider, Radiology room) {
        super(date, timeslot, patient, provider);
        this.room = room;
    }

    /**
     * Sets the radiology room for this imaging appointment.
     *
     * @param room The radiology room to set for the appointment.
     */
    public void setRoom(Radiology room) {
        this.room = room;
    }

    /**
     * Returns the radiology room of this imaging appointment.
     *
     * @return The radiology room.
     */
    public Radiology getRoom() {
        return room;
    }
}