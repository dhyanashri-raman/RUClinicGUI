package com.example.ruclinicgui.clinic.src;

/**
 * The clinic.src.Specialty enum defines different medical specialties, each
 * associated with a specific charge amount. It provides a way to
 * categorize providers based on their specialization.
 * @author Nithya Konduru, Dhyanashri Raman
 */
public enum Specialty {
    Family(250),
    Pediatrician(300),
    Allergist(350);

    private final int charge;

    /**
     * Constructor: Initializes the clinic.src.Specialty enum with a specified
     * charge amount.
     *
     * @param charge The charge amount for the specialty.
     */
    Specialty(int charge) {
        this.charge = charge;
    }

    /**
     * Method: Gets the charge amount associated with the specialty.
     *
     * @return The charge amount for the specialty.
     */
    public int getCharge() {
        return charge;
    }
}