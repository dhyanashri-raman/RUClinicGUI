package com.example.ruclinicgui.clinic.src.util;


import com.example.ruclinicgui.clinic.src.Appointment;
import com.example.ruclinicgui.clinic.src.Provider;

/**
 * This class provides sorting functionality for various types of lists,
 * specifically focusing on sorting appointments and providers.
 * @author Nithya Konduru, Dhyanashri Raman
 * @param <E> The type of elements to be sorted, expected to be a subtype of Appointment.
 */
public class Sort<E> {
    /**
     * Swaps two appointments in the list at specified indices.
     *
     * @param appointments The list of appointments.
     * @param i The index of the first appointment to swap.
     * @param j The index of the second appointment to swap.
     */

    private void swapAppointments(List<E> appointments, int i, int j) {
        E temp = appointments.get(i);
        E second = appointments.get(j);
        if (temp instanceof Appointment && second instanceof Appointment) {
            appointments.set(i, second);
            appointments.set(j, temp);
        }
    }

    /**
     * Sorts a list of appointments in ascending order based on their attributes.
     *
     * @param list The list of appointments to be sorted.
     */
    public void sortByAppointment(List<E> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                E obj = list.get(j);
                E obj2 = list.get(j+1);
                if (obj instanceof Appointment && obj2 instanceof Appointment) {
                    if (((Appointment) obj).compareByAppointment((Appointment) obj2) > 0) {
                        swapAppointments(list, j, j + 1);
                    }
                }
            }
        }
    }

    /**
     * Sorts a list of providers in ascending order based on their last names.
     *
     * @param list The list of providers to be sorted.
     */
    public void sortByProvider(List<Provider> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                String lastName1 = list.get(j).getProfile().getLastName();
                String lastName2 = list.get(j + 1).getProfile().getLastName();
                if (lastName1.compareTo(lastName2) > 0) {
                    Provider temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    /**
     * Sorts a list of providers for printing purposes,
     * first by last name, then by first name, and finally by date of birth.
     *
     * @param list The list of providers to be sorted.
     */
    public void sortByProviderForPrint(List<Provider> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                String lastName1 = list.get(j).getProfile().getLastName();
                String lastName2 = list.get(j + 1).getProfile().getLastName();
                String firstName1 = list.get(j).getProfile().getFirstName();
                String firstName2 = list.get(j + 1).getProfile().getFirstName();
                Date dob1 = list.get(j).getProfile().getDob();
                Date dob2 = list.get(j + 1).getProfile().getDob();
                if (lastName1.compareTo(lastName2) > 0) {
                    Provider temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
                if (lastName1.equals(lastName2)) {
                    if (firstName1.compareTo(firstName2) > 0) {
                        Provider temp = list.get(j);
                        list.set(j, list.get(j + 1));
                        list.set(j + 1, temp);
                    }
                    else if (firstName1.equals(firstName2)) {
                        if (dob1.compareTo(dob2) > 0) {
                            Provider temp = list.get(j);
                            list.set(j, list.get(j + 1));
                            list.set(j + 1, temp);
                        }
                    }

                }
            }
        }
    }

    /**
     * Sorts a list of appointments in ascending order based on the patients associated with them.
     *
     * @param list The list of appointments to be sorted.
     */
    public void sortByPatient(List<E> list)
    {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                E obj = list.get(j);
                E obj2 = list.get(j+1);
                if (obj instanceof Appointment && obj2 instanceof Appointment) {
                    if (((Appointment) obj).compareByPatient((Appointment) obj2) > 0) {
                        swapAppointments(list, j, j + 1);
                    }
                }
            }
        }
    }

    /**
     * Sorts a list of appointments in ascending order based on their locations.
     *
     * @param list The list of appointments to be sorted.
     */
    public void sortByLocation(List<E> list)
    {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                E obj = list.get(j);
                E obj2 = list.get(j+1);
                if (obj instanceof Appointment && obj2 instanceof Appointment) {
                        if (((Appointment) obj).compareByLocation((Appointment) obj2) > 0) {
                            swapAppointments(list, j, j + 1);
                        }
                }
            }
        }
    }

    /**
     * Sorts a list of appointments in ascending order based on their profiles.
     *
     * @param list The list of appointments to be sorted.
     */
    public void sortByProfile(List<E> list)
    {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                E obj = list.get(j);
                E obj2 = list.get(j+1);
                if (obj instanceof Appointment && obj2 instanceof Appointment) {
                    if (((Appointment) obj).getProfile().compareTo(((Appointment) obj2).getProfile()) < 0) {
                        swapAppointments(list, j, j + 1);
                    }
                }
            }
        }
    }
}
