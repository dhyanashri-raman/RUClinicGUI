package com.example.ruclinicgui.clinic.src.util;


import com.example.ruclinicgui.clinic.src.Appointment;
import com.example.ruclinicgui.clinic.src.Provider;

public class Sort<E> {
    private void swapAppointments(List<E> appointments, int i, int j) {
        E temp = appointments.get(i);
        E second = appointments.get(j);
        if (temp instanceof Appointment && second instanceof Appointment) {
            appointments.set(i, second);
            appointments.set(j, temp);
        }
    }

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
