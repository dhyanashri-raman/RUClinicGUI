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
        output.append("** end of list **");
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

    public void printOfficeAppointments(List <E> objects) {
        System.out.println();
        System.out.println("** clinic.src.util.List of office appointments ordered by county/date/time.");
        sort.sortByLocation(objects);
        printOfficeAppts(objects);
        System.out.println("** end of list **");
    }

    private void printOfficeAppts(List <E> objects) {
        for (int i =0; i < objects.size(); i++) {
            E obj = objects.get(i);
            if (obj instanceof Appointment) {
                if (((Appointment) obj).getProvider() instanceof Doctor) {
                    System.out.println(formatAppointment((Appointment) obj));
                }
            }
        }
    }

    public void printImagingAppointments(List <E> objects) {
        System.out.println();
        System.out.println("** List of radiology appointments ordered by county/date/time.");
        sort.sortByLocation(objects);
        printImagingAppts(objects);
        System.out.println("** end of list **");
    }

    private void printImagingAppts(List <E> objects) {
        for (int i = 0; i<objects.size(); i++) {
            E obj = objects.get(i);
            if (obj instanceof Imaging) {
                System.out.println(formatImagingAppointments((Imaging) obj));
            }
        }
    }

    /**
     * Prints all appointments ordered by patient, date, and time.
     */
    public void printByPatient(List <E> objects) {
        System.out.println();
        System.out.println("** Appointments ordered by patient/date/time **");
        sort.sortByPatient(objects);
        printAppointments(objects);
        System.out.println("** end of list **");
    }

    /**
     * Prints all appointments ordered by location, date, and time.
     */
    public void printByLocation(List <E> objects) {
        System.out.println();
        System.out.println("** Appointments ordered by county/date/time.");
        sort.sortByLocation(objects);
        printAppointments(objects);
        System.out.println("** end of list **");
    }

    /**
     * Prints all charges for appointments, ordered by patient.
     */
    public void printAllCharge(List <E> objects) {
        if (objects.size() == 0) {
            System.out.println("There are no appointments in the system.");
            return;
        }
        System.out.println("** Billing statement ordered by patient **");
        sort.sortByProfile(objects);
        DecimalFormat formatDec = new DecimalFormat("$#,##0.00");
        int counter = 1;
        Profile currentProfile = null;
        int currentCharge = 0;

        for (int i = 0; i < objects.size(); i++) {
            E obj = objects.get(i);
            int charge=0;
            Profile profile=null;
            if (obj instanceof Appointment) {
                profile = ((Appointment) obj).getProfile().getProfile();
                if (((Appointment) obj).getProvider() instanceof Doctor) {
                    charge = ((Doctor)((Appointment) obj).getProvider()).getSpecialty().getCharge();
                }
                else if (((Appointment) obj).getProvider() instanceof Technician) {
                    charge = ((Technician) ((Appointment) obj).getProvider()).rate();
                }
            }

            if (currentProfile == null || !currentProfile.equals(profile)) {
                if (currentProfile != null) {
                    System.out.printf("(%d) %s [amount due: %s]%n",
                            counter++,
                            currentProfile.toString(),
                            formatDec.format(currentCharge));
                }
                currentProfile = profile;
                currentCharge = charge;
            } else {
                currentCharge += charge;
            }
        }
        if (currentProfile != null) {
            System.out.printf("(%d) %s [amount due: %s]%n",
                    counter,
                    currentProfile.toString(),
                    formatDec.format(currentCharge));
        }
        System.out.println("** end of list **");
    }

    public void printProviderCharges(List <E> objects, CircularLinkedList techsCLL) {
        List<Provider> providers = new List<Provider>();
        System.out.println("\n** Credit amount ordered by provider. **");
        Node start = techsCLL.getHead();
        Provider currProvider = null;
        Node curr = start;
        do{
            providers.add(curr.getTechnician());
            curr = curr.next;
        }while(curr != start);
        for(int i = 0; i<objects.size(); i++) {
            if(objects.get(i) instanceof Appointment && ((Appointment) objects.get(i)).getProvider() instanceof Doctor) {
                providers.add(((Appointment) objects.get(i)).getProvider());
            }
        }
        sort.sortByProviderForPrint(providers);
        int counter = 0;
        int charge;
        for(int j = 0; j<providers.size(); j++){
            charge = 0;
            currProvider = providers.get(j);
            for(int i = 0; i<objects.size(); i++) {
                if(objects.get(i) instanceof Imaging && currProvider instanceof Technician) {
                    if(((Imaging) objects.get(i)).getProvider().equals(currProvider)) {
                        charge += ((Imaging) objects.get(i)).getProvider().rate();
                    }
                }
                else if(objects.get(i) instanceof Appointment && currProvider instanceof Doctor) {
                    if(((Appointment) objects.get(i)).getProvider().equals(currProvider)) {
                        charge += (((Appointment) objects.get(i)).getProvider().rate());
                    }
                }
            }
            counter++;
            System.out.println("(" + counter + ") " + currProvider.getProfile().toString() + " [credit amount: $" + charge + ".00] ");
        }
        System.out.println("** end of list **\n");
    }

    // REMEMBER TO DO PS COMMAND - ask dhyana about this

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
