package com.example.ruclinicgui.clinic.src;

/**
 * Represents a doctor who is a provider at the clinic.
 * Each doctor has a specialty and a National Provider Identifier (NPI).
 */
public class Doctor extends Provider {
    private Specialty specialty;
    private String npi;

    /**
     * Default constructor for creating a Doctor object with no initialization.
     */
    public Doctor() {
        super();
    }

    /**
     * Constructs a Doctor object with a profile, location, specialty, and NPI.
     *
     * @param profile    The profile of the doctor.
     * @param location   The location of the doctor.
     * @param specialty  The specialty of the doctor.
     * @param npi        The National Provider Identifier (NPI) of the doctor.
     */
    public Doctor(Profile profile, Location location, Specialty specialty, String npi) {
        super(profile, location);
        this.specialty = specialty;
        this.npi = npi;
    }

    /**
     * Returns the specialty of the doctor.
     *
     * @return The specialty of the doctor.
     */
    public Specialty getSpecialty() {
        return specialty;
    }

    /**
     * Returns the National Provider Identifier (NPI) of the doctor.
     *
     * @return The NPI of the doctor.
     */
    public String getNPI() {
        return npi;
    }

    /**
     * Returns the charge rate for the doctor's specialty.
     *
     * @return The charge rate for the doctor's specialty.
     */
    @Override
    public int rate() {
        return this.specialty.getCharge();
    }

    /**
     * Compares this doctor to another doctor based on their profiles.
     *
     * @param doc The doctor to compare with.
     * @return    A negative integer, zero, or a positive integer as this doctor is less than, equal to, or greater than the specified doctor.
     */
    public int compareTo(Doctor doc) {
        if (doc instanceof Doctor) {
            Doctor doctor = (Doctor) doc;
            return this.profile.compareTo(doctor.getProfile());
        }
        throw new ClassCastException("Cannot compare non-Doctor objects.");
    }

    /**
     * Compares this doctor to another object for equality.
     *
     * @param object The object to compare with.
     * @return       True if the object is a doctor with the same profile, NPI, and specialty; otherwise false.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Doctor) {
            Doctor doctor = (Doctor) object;
            return this.profile.equals(doctor.getProfile()) &&
                    this.npi.equals(doctor.getNPI()) &&
                    this.specialty.equals(doctor.getSpecialty());
        }
        return false;
    }

    /**
     * Returns a string representation of the doctor's details.
     *
     * @return A string representing the doctor.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}