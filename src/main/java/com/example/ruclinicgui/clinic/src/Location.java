package com.example.ruclinicgui.clinic.src;

/**
 * The clinic.src.Location enum represents various geographical locations along with their corresponding
 * counties and ZIP codes. Each location is defined with a name, county, and ZIP code.
 */
public enum Location {
    Bridgewater("Somerset", "08888"),
    Edison("Middlesex", "08817"),
    Piscataway("Middlesex", "08817"),
    Princeton("Mercer", "08542"),
    Morristown("Morris", "07960"),
    Clark("Union", "07066");

    private final String county;
    private final String zip;

    /**
     * Constructor to initialize a clinic.src.Location enum instance with a specific county and ZIP code.
     *
     * @param county The county of the location.
     * @param zip    The ZIP code of the location.
     */
    Location(String county, String zip) {
        this.county = county;
        this.zip = zip;
    }

    /**
     * Retrieves the county of the location.
     *
     * @return The county as a String.
     */
    public String getCounty() {
        return county;
    }

    /**
     * Retrieves the ZIP code of the location.
     *
     * @return The ZIP code as a String.
     */
    public String getZip() {
        return this.zip;
    }
}