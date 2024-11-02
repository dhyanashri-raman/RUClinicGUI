/* @@author Dhyanashri Konduru*/
/* @@author Nithya Konduru */

package com.example.ruclinicgui.clinic.src.util;

import com.example.ruclinicgui.clinic.src.*;

import java.text.DecimalFormat;

public class ListMethods<E> extends List{
    Sort sort = new Sort();
    int NOT_FOUND = -1;
    public ListMethods()
    {
        super();
    }

    public int timeslotTakenByPatient(List<E> objects, Profile profile, Date date, Timeslot timeslot) {
        for (int i = 0; i<objects.size(); i++) {
            E obj = objects.get(i);
            if(obj instanceof Appointment)
            {
                if (((Appointment) obj).getProfile().equals(profile) && ((Appointment) obj).getTimeslot().equals(timeslot) && ((Appointment) obj).getDate().equals(date)) {
                    return i;
                }
            }
        }
        return NOT_FOUND;
    }
    public int dateExists (List <E> objects, Date date)
    {
        for(int i = 0; i < objects.size(); i++)
        {
            E obj = objects.get(i);
            if(obj instanceof Appointment)
            {
                if(((Appointment) obj).getDate().equals(date))
                    return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Prints all appointments ordered by date, time, and provider.
     */
    public String printByAppointment(List<E> objects) {
        StringBuilder output = new StringBuilder();
        output.append("\n** List of appointments ordered by date/time/provider.\n");
        sort.sortByAppointment(objects);
        String appointmentsOutput = printAppointments(objects);
        output.append(appointmentsOutput);
        output.append("** end of list **\n");
        return output.toString();
    }


    /**
     * Prints all appointments in the list.
     */
    private String printAppointments(List<E> objects) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < objects.size(); i++) {
            E obj = objects.get(i);
            if (obj instanceof Appointment) {
                output.append(formatAppointment((Appointment) obj)).append("\n");
            }
        }
        return output.toString();
    }


    /**
     * Formats an appointment for display.
     * @param app The appointment to format.
     * @return A formatted string representing the appointment.
     */
    private String formatAppointment(Appointment app) {
        return String.format("%s %s %s %s %s %s",
                app.getDate(),
                app.getTimeslot().toString(),
                app.getProfile().getProfile().getFirstName(),
                app.getProfile().getProfile().getLastName(),
                app.getProfile().getProfile().getDob(),
                app.getProvider().toString().toUpperCase());
    }

    private String formatImagingAppointments(Imaging imaging) {
        return String.format("%s %s %s %s %s %s",
                imaging.getDate(),
                imaging.getTimeslot().toString(),
                imaging.getProfile().getProfile().getFirstName(),
                imaging.getProfile().getProfile().getLastName(),
                imaging.getProfile().getProfile().getDob(),
                imaging.getProvider().toString().toUpperCase());
    }

    public String printOfficeAppointments(List<E> objects) {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append("** List of office appointments ordered by county/date/time.\n");

        sort.sortByLocation(objects);
        result.append(printOfficeAppts(objects));

        result.append("** end of list **\n");
        return result.toString();
    }

    private String printOfficeAppts(List<E> objects) {
        StringBuilder appointments = new StringBuilder();

        for (int i = 0; i < objects.size(); i++) {
            E obj = objects.get(i);
            if (obj instanceof Appointment) {
                if (((Appointment) obj).getProvider() instanceof Doctor) {
                    appointments.append(formatAppointment((Appointment) obj)).append("\n");
                }
            }
        }
        return appointments.toString();
    }

    public String printImagingAppointments(List<E> objects) {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append("** List of radiology appointments ordered by county/date/time.\n");
        sort.sortByLocation(objects);
        result.append(printImagingAppts(objects));
        result.append("** end of list **\n");
        return result.toString();
    }

    private String printImagingAppts(List<E> objects) {
        StringBuilder appointments = new StringBuilder();
        for (int i = 0; i < objects.size(); i++) {
            E obj = objects.get(i);
            if (obj instanceof Imaging) {
                appointments.append(formatImagingAppointments((Imaging) obj)).append("\n");
            }
        }
        return appointments.toString();
    }

    /**
     * Prints all appointments ordered by patient, date, and time.
     */
    public String printByPatient(List<E> objects) {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append("** Appointments ordered by patient/date/time **\n");

        sort.sortByPatient(objects);
        result.append(printAppointments(objects));

        result.append("** end of list **\n");
        return result.toString();
    }

    /**
     * Prints all appointments ordered by location, date, and time.
     */
    public String printByLocation(List<E> objects) {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append("** Appointments ordered by county/date/time **\n");

        sort.sortByLocation(objects);
        result.append(printAppointments(objects));

        result.append("** end of list **\n");
        return result.toString();
    }
    /**
     * Prints all charges for appointments, ordered by patient.
     */
    public String printAllCharge(List<E> objects) {
        if (objects.size() == 0) {
            return "\nThere are no appointments in the system.\n";
        }

        StringBuilder result = new StringBuilder();
        result.append("\n** Billing statement ordered by patient **\n");

        sort.sortByProfile(objects);
        DecimalFormat formatDec = new DecimalFormat("$#,##0.00");
        int counter = 1;
        Profile currentProfile = null;
        int currentCharge = 0;

        for (int i = 0; i < objects.size(); i++) {
            E obj = objects.get(i);
            int charge = 0;
            Profile profile = null;

            if (obj instanceof Appointment) {
                profile = ((Appointment) obj).getProfile().getProfile();
                if (((Appointment) obj).getProvider() instanceof Doctor) {
                    charge = ((Doctor) ((Appointment) obj).getProvider()).getSpecialty().getCharge();
                } else if (((Appointment) obj).getProvider() instanceof Technician) {
                    charge = ((Technician) ((Appointment) obj).getProvider()).rate();
                }
            }

            if (currentProfile == null || !currentProfile.equals(profile)) {
                if (currentProfile != null) {
                    result.append(String.format("(%d) %s [amount due: %s]%n",
                            counter++,
                            currentProfile.toString(),
                            formatDec.format(currentCharge)));
                }
                currentProfile = profile;
                currentCharge = charge;
            } else {
                currentCharge += charge;
            }
        }

        if (currentProfile != null) {
            result.append(String.format("(%d) %s [amount due: %s]%n",
                    counter,
                    currentProfile.toString(),
                    formatDec.format(currentCharge)));
        }
        result.append("** end of list **\n");

        return result.toString();
    }

    public String printProviderCharges(List<E> objects, CircularLinkedList techsCLL) {
        StringBuilder result = new StringBuilder();
        List<Provider> providers = new List<Provider>();
        result.append("\n** Credit amount ordered by provider. **\n");

        Node start = techsCLL.getHead();
        Provider currProvider = null;
        Node curr = start;

        do {
            providers.add(curr.getTechnician());
            curr = curr.next;
        } while (curr != start);

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof Appointment && ((Appointment) objects.get(i)).getProvider() instanceof Doctor) {
                providers.add(((Appointment) objects.get(i)).getProvider());
            }
        }

        sort.sortByProviderForPrint(providers);
        int charge;
        int counter = 1;
        for (int j = 0; j < providers.size(); j++) {
            charge = 0;
            currProvider = providers.get(j);

            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i) instanceof Imaging && currProvider instanceof Technician) {
                    if (((Imaging) objects.get(i)).getProvider().equals(currProvider)) {
                        charge += ((Imaging) objects.get(i)).getProvider().rate();
                    }
                } else if (objects.get(i) instanceof Appointment && currProvider instanceof Doctor) {
                    if (((Appointment) objects.get(i)).getProvider().equals(currProvider)) {
                        charge += (((Appointment) objects.get(i)).getProvider().rate());
                    }
                }
            }
            if(charge != 0){
                result.append("(").append(counter).append(") ")
                        .append(currProvider.getProfile().toString())
                        .append(" [credit amount: $").append(charge).append(".00] \n");
                counter++;
            }

        }

        result.append("** end of list **\n");

        return result.toString();
    }

    public int getDoctorFromNPI(List<E> objects, String npi) {
        for (int i = 0; i<objects.size(); i++) {
            E obj = objects.get(i);
            if(objects.get(i) instanceof Doctor) {
                if (((Doctor) objects.get(i)).getNPI().equals(npi)) {
                    return i;
                }
            }
        }
        return NOT_FOUND;
    }
    public int getTechnicianFromRate(List<E> objects, int rate) {
        for (int i = 0; i < size(); i++) {
            if(objects.get(i) instanceof Technician) {
                if (((Technician) objects.get(i)).rate()==(rate)) {
                    return i;
                }
            }
        }
        return NOT_FOUND;
    }
    public int identifyAppointment(List<E> objects, Profile profile, Date date, Timeslot timeslot) {
        for (int i = 0; i<objects.size(); i++) {
            if (objects.get(i) instanceof Appointment) {
                if (((Appointment) objects.get(i)).getProfile().getProfile().equals(profile)) {
                    if (((Appointment) objects.get(i)).getDate().equals(date)) {
                        if (((Appointment) objects.get(i)).getTimeslot().equals(timeslot)) {
                            return i;
                        }
                    }
                }
            }
        }
        return NOT_FOUND;
    }

    public int identifyImagingAppt(List<E> objects, Technician tech, Date date, Timeslot timeslot) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof Imaging) {
                Imaging imaging = (Imaging) objects.get(i);
                if (imaging.getProvider().getProfile().equals(tech.getProfile()) &&
                        imaging.getDate().equals(date) &&
                        imaging.getTimeslot().equals(timeslot)) {
                    return i; // clinic.src.Technician is NOT available (appointment found)
                }
            }
        }
        return NOT_FOUND; // clinic.src.Technician is available (no conflicting appointment found)
    }

    public int identifyImagingAppt2(List<E> imagingAppts, Profile profile, Date apptDate, Timeslot timeslot)
    {
        for (int i = 0; i < imagingAppts.size(); i++) {
            if (imagingAppts.get(i) instanceof Imaging) {
                Imaging imaging = (Imaging) imagingAppts.get(i);
                if (imaging.getProfile().getProfile().equals(profile) &&
                        imaging.getDate().equals(apptDate) &&
                        imaging.getTimeslot().equals(timeslot)) {
                    return i; // clinic.src.Technician is NOT available (appointment found)
                }
            }
        }
        return NOT_FOUND;
    }

    public boolean isRoomFree(List<E> objects, Technician tech, Date date, Timeslot timeslot, Radiology room) {
        Location location = tech.getLocation();
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof Imaging) {
                Imaging imaging = (Imaging) objects.get(i);
                if (imaging.getProvider().getLocation().equals(location) &&
                        imaging.getDate().equals(date) &&
                        imaging.getTimeslot().equals(timeslot) &&
                        imaging.getRoom().equals(room)) {
                    return false; // Room is NOT free
                }
            }
        }
        return true; // Room is free
    }

    public int timeslotTaken(List<E> objects, Provider provider, Timeslot timeslot, Date date) {
        for (int i = 0; i < objects.size(); i++) {
            if(objects.get(i) instanceof Appointment) {
                if (((Appointment) objects.get(i)).getProvider().equals(provider) && ((Appointment) objects.get(i)).getDate().equals(date) && ((Appointment) objects.get(i)).getTimeslot().equals(timeslot)) {
                    return i;
                }
            }
        }
        return NOT_FOUND;
    }

}
