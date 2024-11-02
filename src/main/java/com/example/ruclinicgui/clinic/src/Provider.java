package com.example.ruclinicgui.clinic.src;

/**
 * The abstract clinic.src.Provider class represents a provider at the clinic, including their location.
 * Providers can either be doctors or technicians. This class extends the Person class.
 * @author Nithya Konduru, Dhyanashri Raman
 */
public abstract class Provider extends Person {
    private Location location;

    /**
     * Default constructor for creating an empty Provider object.
     */
    public Provider() {
        super();
    }

    /**
     * Constructs a Provider object with a specific profile and location.
     *
     * @param profile  The profile of the provider.
     * @param location The location of the provider.
     */
    public Provider(Profile profile, Location location) {
        super(profile);
        this.location = location;
    }

    /**
     * Method: Retrieves the location of the provider.
     *
     * @return The location of the provider.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Abstract method to retrieve the rate of the provider.
     * Must be implemented by subclasses.
     *
     * @return The rate of the provider.
     */
    public abstract int rate();

    /**
     * Compares this provider to another person based on their profiles.
     *
     * @param person The person to compare with.
     * @return       A negative integer, zero, or a positive integer as this provider is less than, equal to, or greater than the specified person.
     * @throws ClassCastException if the provided object is not a Provider.
     */
    @Override
    public int compareTo(Person person) {
        if (person instanceof Provider) {
            Provider provider = (Provider) person;
            return this.profile.compareTo(provider.profile);
        }
        throw new ClassCastException("Cannot compare non-Provider objects.");
    }

    /**
     * Checks if this provider is equal to another object based on their profiles.
     *
     * @param object The object to compare with.
     * @return       True if the object is a provider and their profiles are equal; otherwise false.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Provider) {
            Provider provider = (Provider) object;
            return this.profile.equals(provider.profile);
        }
        return false;
    }

    /**
     * Returns a string representation of the provider, including their profile, location, and specific details for doctors or technicians.
     *
     * @return A string representing the provider.
     * @throws ClassCastException if the object is not a valid provider.
     */
    @Override
    public String toString() {
        if (this instanceof Doctor) {
            return "[" + super.toString() + ", " + this.location + ", " + this.location.getCounty() + " " + this.location.getZip() + "]" + "[" + ((Doctor) this).getSpecialty().toString().toUpperCase() + ", #" + ((Doctor) this).getNPI() + "]";
        } else if (this instanceof Technician) {
            return "[" + super.toString() + ", " + this.location + ", " + this.location.getCounty() + " " + this.location.getZip() + "]" + "[rate: $" + this.rate() + "]";
        }
        throw new ClassCastException("Cannot perform toString() on a non-clinic.src.Provider object.");
    }
}