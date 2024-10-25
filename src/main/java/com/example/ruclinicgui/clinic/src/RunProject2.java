package com.example.ruclinicgui.clinic.src;

/**
 * The clinic.src.RunProject2 class serves as the entry point for running the Clinic Manager system.
 * It contains the main method which starts the execution of the Clinic Manager.
 */
public class RunProject2 {

    /**
     * Main method: Starts the Clinic Manager system by calling the run() method of ClinicManager.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        new ClinicManager().run();
    }
}