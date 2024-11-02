package com.example.ruclinicgui.clinic.src;
import com.example.ruclinicgui.clinic.src.util.Date;

/**
 * The clinic.src.Profile class represents a patient's profile, including their first name,
 * last name, and date of birth. It implements the Comparable interface to allow
 * profiles to be compared based on their attributes.
 * @author Nithya Konduru, Dhyanashri Raman
 */
public class Profile implements Comparable<Profile> {
    private String fname;
    private String lname;
    private Date dob;

    /**
     * Constructor: Initializes a clinic.src.Profile object with the specified first name,
     * last name, and date of birth.
     *
     * @param fname The first name of the patient.
     * @param lname The last name of the patient.
     * @param dob   The date of birth of the patient.
     */
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     * Method: Validates the date of birth to ensure it is not invalid, today, or a future date.
     *
     * @return true if the date of birth is valid; false otherwise.
     */
    public boolean dobValid() {
        if (!this.dob.isValidDate()) {
            return false;
        } else if (this.dob.isToday()) {
            return false;
        } else if (this.dob.isFutureDate()) {
            return false;
        }
        return true;
    }

    /**
     * Method: Retrieves the first name of the patient.
     *
     * @return The first name of the patient.
     */
    public String getFirstName() {
        return this.fname;
    }

    /**
     * Method: Retrieves the last name of the patient.
     *
     * @return The last name of the patient.
     */
    public String getLastName() {
        return this.lname;
    }

    /**
     * Method: Retrieves the date of birth of the patient.
     *
     * @return The date of birth of the patient.
     */
    public Date getDob() {
        return this.dob;
    }

    /**
     * Method: Compares this clinic.src.Profile object to another object for equality.
     *
     * @param patientProfile The object to be compared with this clinic.src.Profile.
     * @return true if the profiles are equal; false otherwise.
     */
    @Override
    public boolean equals(Object patientProfile) {
        if (patientProfile instanceof Profile) {
            Profile profile = (Profile) patientProfile;
            return (profile.fname.equalsIgnoreCase(this.fname)
                    && profile.lname.equalsIgnoreCase(this.lname)
                    && profile.dob.equals(this.dob));
        }
        return false;
    }

    /**
     * Method: Compares this clinic.src.Profile object with another clinic.src.Profile object for order.
     *
     * @param patientProfile The clinic.src.Profile object to compare to.
     * @return A negative integer, zero, or a positive integer as this clinic.src.Profile
     *         is less than, equal to, or greater than the specified clinic.src.Profile.
     */
    @Override
    public int compareTo(Profile patientProfile) {
        int lnameComparison = this.lname.compareTo(patientProfile.lname);
        if (lnameComparison < 0) {
            return -1;
        } else if (lnameComparison > 0) {
            return 1;
        }
        int fnameComparison = this.fname.compareTo(patientProfile.fname);
        if (fnameComparison < 0) {
            return -1;
        } else if (fnameComparison > 0) {
            return 1;
        }
        int dobComparison = this.dob.compareTo(patientProfile.dob);
        if (dobComparison < 0) {
            return -1;
        } else if (dobComparison > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * Method: Returns a string representation of the clinic.src.Profile.
     *
     * @return A string containing the first name, last name, and date of birth.
     */
    @Override
    public String toString() {
        return fname + " " + lname + " " + dob.toString();
    }
}