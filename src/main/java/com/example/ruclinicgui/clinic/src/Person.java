package com.example.ruclinicgui.clinic.src;

/**
 * Represents a person in the clinic system, which could be a patient or a provider.
 * Each person has an associated profile, and this class implements comparison based on the profile.
 * @author Nithya Konduru, Dhyanashri Raman
 */
public class Person implements Comparable<Person> {

    protected Profile profile;

    /**
     * Default constructor for creating an empty Person object.
     */
    public Person() {
    }

    /**
     * Constructs a Person object with a specific profile.
     *
     * @param profile The profile associated with the person.
     */
    public Person(Profile profile) {
        this.profile = profile;
    }

    /**
     * Retrieves the profile of this person.
     *
     * @return The profile associated with the person.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Compares this person to another person based on their profiles.
     *
     * @param person The person to compare with.
     * @return       A negative integer, zero, or a positive integer as this person's profile is less than, equal to, or greater than the specified person's profile.
     */
    @Override
    public int compareTo(Person person) {
        return this.profile.compareTo(person.profile);
    }

    /**
     * Checks if this person is equal to another object based on their profiles.
     *
     * @param object The object to compare with.
     * @return       True if the object is a person and their profiles are equal; otherwise false.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Person) {
            Person person = (Person) object;
            return this.profile.equals(person.profile);
        }
        return false;
    }

    /**
     * Returns a string representation of the person's profile.
     *
     * @return A string representing the person.
     */
    @Override
    public String toString() {
        return this.profile.toString();
    }

    /**
     * Retrieves the Provider if this person is a Provider.
     *
     * @return The Provider object if this person is a Provider, or null if not.
     */
    public Provider getProvider() {
        if (this instanceof Provider) {
            return (Provider) this;
        }
        return null;
    }

    /**
     * Retrieves the location if this person is a Provider.
     *
     * @return The Location associated with the person if they are a Provider, or null if not.
     */
    public Location getLocation() {
        if (this instanceof Provider) {
            return ((Provider) this).getLocation();
        }
        return null;
    }
}