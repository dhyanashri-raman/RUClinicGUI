package com.example.ruclinicgui.clinic.src;

/**
 * Represents a visit associated with an appointment.
 * Each visit can reference the next visit in a linked list structure.
 * @author Nithya Konduru, Dhyanahsri
 */
public class Visit {
    private Appointment appointment;
    private Visit next;

    /**
     * Constructs a clinic.src.Visit with a specified appointment.
     *
     * @param appointment The appointment associated with this visit.
     */
    public Visit(Appointment appointment) {
        this.appointment = appointment;
        this.next = null;
    }

    /**
     * Adds a new visit to the next reference of this visit.
     *
     * @param visit The visit to be added.
     */
    public void add(Visit visit) {
        this.next = visit;
    }

    /**
     * Retrieves the appointment associated with this visit.
     *
     * @return The appointment of this visit.
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * Retrieves the next visit in the list.
     *
     * @return The next visit object, or null if there is none.
     */
    public Visit getVisit() {
        return next;
    }
}