package com.example.ruclinicgui.clinic.src;

import com.example.ruclinicgui.clinic.src.util.Date;

/* @@author Dhyanashri Konduru*/
/* @@author Nithya Konduru */
/**
 * Represents an appointment between a patient and a provider, scheduled on a specific date and timeslot.
 * The appointment can be compared based on various factors such as date, timeslot, patient, provider, and location.
 */
public class Appointment implements Comparable<Appointment> {
        protected Date date;
        protected Timeslot timeslot;
        protected Person patient;
        protected Person provider;

        /**
         * Constructor to create a new appointment with a specified date, timeslot, patient, and provider.
         *
         * @param date      The date of the appointment.
         * @param timeslot  The timeslot of the appointment.
         * @param patient   The patient attending the appointment.
         * @param provider  The provider for the appointment.
         */
        public Appointment(Date date, Timeslot timeslot, Person patient, Person provider) {
                this.date = date;
                this.timeslot = timeslot;
                this.patient = patient;
                this.provider = provider;
        }

        /**
         * Compares this appointment with another appointment based on the date and timeslot.
         *
         * @param appt The appointment to compare with.
         * @return     An integer representing the comparison result. Returns 0 if both date and timeslot are equal.
         */
        @Override
        public int compareTo(Appointment appt) {
                int dateComparison = this.date.compareTo(appt.date);
                int timeslotComparison = this.timeslot.compareTo(appt.timeslot);
                if (dateComparison == 0) {
                        return timeslotComparison;
                }
                return dateComparison;
        }

        /**
         * Compares this appointment with another appointment based on the location (county), date, and timeslot.
         *
         * @param appt The appointment to compare with.
         * @return     An integer representing the comparison result based on location, date, and timeslot.
         */
        public int compareByLocation(Appointment appt) {
                int locationComparison = this.provider.getLocation().getCounty().compareTo(appt.provider.getLocation().getCounty());
                if (locationComparison != 0) {
                        return locationComparison;
                }
                int dateComparison = this.date.compareTo(appt.date);
                if (dateComparison != 0) {
                        return dateComparison;
                }
                return this.timeslot.compareTo(appt.timeslot);
        }

        /**
         * Compares this appointment with another appointment based on patient information,
         * including last name, first name, date of birth, and appointment date and time.
         *
         * @param appt The appointment to compare with.
         * @return     An integer representing the comparison result based on patient information.
         */
        public int compareByPatient(Appointment appt) {
                int lastNameComparison = this.patient.getProfile().getLastName().compareTo(appt.patient.getProfile().getLastName());
                if (lastNameComparison == 0) {
                        int firstNameComparison = this.patient.getProfile().getFirstName().compareTo(appt.patient.getProfile().getFirstName());
                        if (firstNameComparison == 0) {
                                int dobComparison = this.patient.getProfile().getDob().compareTo(appt.patient.getProfile().getDob());
                                if (dobComparison == 0) {
                                        int dateComparison = this.date.compareTo(appt.date);
                                        if (dateComparison == 0) {
                                                return this.timeslot.compareTo(appt.timeslot);
                                        }
                                        return dateComparison;
                                }
                                return dobComparison;
                        }
                        return firstNameComparison;
                }
                return lastNameComparison;
        }

        /**
         * Compares this appointment with another appointment based on the date and provider information.
         *
         * @param appt The appointment to compare with.
         * @return     An integer representing the comparison result based on the date and provider information.
         */
        public int compareByAppointment(Appointment appt) {
                int dateComparison = this.date.compareTo(appt.date);
                if (dateComparison == 0) {
                        int timeslotComparison = this.timeslot.compareTo(appt.timeslot);
                        if(this.timeslot.compareTo(appt.timeslot) == 0)
                        {
                                int providerComparison = this.provider.compareTo(appt.provider);
                                return providerComparison;
                        }
                        return timeslotComparison;
                }
                return dateComparison;
        }

        /**
         * Checks if this appointment is equal to another object.
         *
         * @param object The object to compare with.
         * @return       True if the object is an Appointment and all fields (date, timeslot, patient, and provider) are equal.
         */
        @Override
        public boolean equals(Object object) {
                if (object instanceof Appointment) {
                        Appointment appt = (Appointment) object;
                        return (this.date != null ? this.date.equals(appt.date) : appt.date == null)
                                && (this.timeslot != null ? this.timeslot.equals(appt.timeslot) : appt.timeslot == null)
                                && (this.patient != null ? this.patient.equals(appt.patient) : appt.patient == null)
                                && (this.provider != null ? this.provider.equals(appt.provider) : appt.provider == null);
                }
                return false;
        }

        /**
         * Returns a string representation of the appointment details.
         *
         * @return A formatted string with date, timeslot, patient information, and provider details.
         */
        @Override
        public String toString() {
                // Example format: "10/30/2024 4:00 PM Jane Doe 5/1/1996 [RACHAEL LIM 11/30/1975, BRIDGEWATER, Somerset 08807][PEDIATRICIAN, #23] booked."
                return this.provider.toString();
        }

        /**
         * Sets the timeslot for this appointment.
         *
         * @param timeslot The timeslot to set.
         */
        public void setTimeslot(Timeslot timeslot) {
                this.timeslot = timeslot;
        }

        /**
         * Gets the date of the appointment.
         *
         * @return The date of the appointment.
         */
        public Date getDate() {
                return this.date;
        }

        /**
         * Gets the timeslot of the appointment.
         *
         * @return The timeslot of the appointment.
         */
        public Timeslot getTimeslot() {
                return this.timeslot;
        }

        /**
         * Gets the patient profile associated with the appointment.
         *
         * @return The patient's profile.
         */
        public Person getProfile() {
                return this.patient;
        }

        /**
         * Gets the provider associated with the appointment, either a Technician or a Doctor.
         *
         * @return The provider as a Technician or Doctor, or null if neither is assigned.
         */
        public Provider getProvider() {
                if (this.provider instanceof Technician) {
                        return (Technician) this.provider;
                }
                if (this.provider instanceof Doctor) {
                        return (Doctor) this.provider;
                }
                return null;
        }
}