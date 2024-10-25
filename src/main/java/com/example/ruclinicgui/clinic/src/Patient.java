package com.example.ruclinicgui.clinic.src;

/**
 * Represents a patient at the clinic, inheriting from the Person class.
 * A patient includes visit information in addition to their profile.
 */
public class Patient extends Person {
    private Visit visit;

    /**
     * Constructs a Patient object with a specific visit.
     *
     * @param visit The visit associated with the patient.
     */
    public Patient(Visit visit) {
        this.visit = visit;
    }

    /**
     * Compares this patient with another person based on their profiles.
     *
     * @param person The person to compare with.
     * @return       A negative integer, zero, or a positive integer as this patient is less than, equal to, or greater than the specified person.
     * @throws ClassCastException if the provided object is not a Patient.
     */
    @Override
    public int compareTo(Person person) {
        if (person instanceof Patient) {
            Patient patient = (Patient) person;
            return this.profile.compareTo(patient.profile);
        }
        throw new ClassCastException("Cannot compare non-Patient objects.");
    }

    /**
     * Checks if this patient is equal to another object.
     *
     * @param object The object to compare with.
     * @return       True if the object is a patient and their profiles are equal; otherwise false.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Patient) {
            Patient patient = (Patient) object;
            return this.profile.equals(patient.profile);
        }
        return false;
    }

    /**
     * Returns a string representation of the patient's profile.
     *
     * @return A string representing the patient.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}