package com.example.ruclinicgui.clinic.src;

/**
 * The clinic.src.Technician class represents a technician at the clinic.
 * Technicians have a rate per visit and can be compared based on their profiles and rates.
 * @author Nithya Konduru, Dhyanashri Raman
 */
public class Technician extends Provider {
    private int ratePerVisit;

    /**
     * Default constructor for creating an empty Technician object.
     */
    public Technician() {
        super();
    }

    /**
     * Constructs a Technician object with a specific profile, location, and rate per visit.
     *
     * @param profile      The profile of the technician.
     * @param location     The location of the technician.
     * @param ratePerVisit The rate per visit for the technician.
     */
    public Technician(Profile profile, Location location, int ratePerVisit) {
        super(profile, location);
        this.ratePerVisit = ratePerVisit;
    }

    /**
     * Method: Retrieves the profile of the technician.
     *
     * @return The profile of the technician.
     */
    @Override
    public Profile getProfile() {
        return this.profile;
    }

    /**
     * Method: Sets the rate per visit for the technician.
     *
     * @param ratePerVisit The rate per visit to set for the technician.
     */
    public void setRatePerVisit(int ratePerVisit) {
        this.ratePerVisit = ratePerVisit;
    }

    /**
     * Method: Retrieves the rate per visit for the technician.
     *
     * @return The rate per visit for the technician.
     */
    @Override
    public int rate() {
        return this.ratePerVisit;
    }

    /**
     * Compares this technician to another technician based on their profiles and rates per visit.
     *
     * @param tech The technician to compare with.
     * @return     A negative integer, zero, or a positive integer as this technician's profile or rate is less than, equal to, or greater than the specified technician.
     * @throws ClassCastException if the provided object is not a Technician.
     */
    public int compareTo(Technician tech) {
        if (tech instanceof Technician) {
            Technician technician = (Technician) tech;

            int profileComparison = this.getProfile().compareTo(technician.getProfile());
            if (profileComparison == 0) {
                return Integer.compare(this.ratePerVisit, technician.rate());
            }
            return profileComparison;
        }
        throw new ClassCastException("Cannot compare non-Technician objects.");
    }

    /**
     * Checks if this technician is equal to another object based on their profiles, locations, and rates.
     *
     * @param object The object to compare with.
     * @return       True if the object is a technician with the same profile, location, and rate; otherwise false.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Technician) {
            Technician technician = (Technician) object;
            return this.getProfile().equals(technician.getProfile()) &&
                    this.getLocation().equals(technician.getLocation()) &&
                    this.ratePerVisit == technician.rate();
        }
        return false;
    }

    /**
     * Returns a string representation of the technician.
     *
     * @return A string representing the technician.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}