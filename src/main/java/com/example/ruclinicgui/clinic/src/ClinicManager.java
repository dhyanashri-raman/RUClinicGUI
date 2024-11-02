package com.example.ruclinicgui.clinic.src;

import com.example.ruclinicgui.clinic.src.util.*;

import java.io.File;
import java.util.Scanner;

public class ClinicManager {
    List<Appointment> appts = new List <Appointment>();
    List <Provider> providers = new List <Provider>();
    CircularLinkedList technicians = new CircularLinkedList();
    Node pointer;
    List<Appointment> imagingAppts = new List<Appointment>();
    Sort sort = new Sort();
    ListMethods methods = new ListMethods();

    public void run() {
        loadProviders();
        printProviders();
        System.out.println("Clinic Manager is running...");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine().trim();
        String [] splittedInput = input.split(",");
        System.out.println();
        while(true) {
            if(input.isEmpty() || splittedInput[0] == null) {
                input = in.nextLine();
                splittedInput = input.split(",");
            }
            if(splittedInput[0].equals("Q")) {
                System.out.println("Scheduler terminated.");
                return;
            }
            if (splittedInput.length == 7 || splittedInput.length==1 || splittedInput.length == 6 ) {
                if (!splittedInput[0].isEmpty()) {
                    String command = splittedInput[0];
                    switch (command) {
                        case "D":
                            scheduleDocAppt(splittedInput);
                            break;
                        case "T":
                            scheduleImaging(splittedInput);
                            break;
                        case "C":
                            if(splittedInput.length==1){
                                System.out.println("Missing Data Tokens");
                                break;
                            }
                            cancel(splittedInput);
                            break;
                        case "R":
                            reschedule(splittedInput);
                            break;
                        case "PA":
                            if (appts.size() == 0) {
                                System.out.println("The schedule calendar is empty.");
                            } else {
                                methods.printByAppointment(appts);
                            }
                            break;
                        case "PP":
                            if (appts.size() == 0) {
                                System.out.println("The schedule calendar is empty.");
                            } else {
                                methods.printByPatient(appts);
                            }
                            break;
                        case "PL":
                            if (appts.size() == 0) {
                                System.out.println("The schedule calendar is empty.");
                            } else {
                                methods.printByLocation(appts);
                            }
                            break;
                        case "PS":
                            if (appts.size() == 0) {
                                System.out.println("The schedule calendar is empty.");
                            } else {
                                methods.printAllCharge(appts);
                            }
                            break;
                        case "PO":
                            if (appts.size() == 0) {
                                System.out.println("The schedule calendar is empty.");
                            } else {
                                methods.printOfficeAppointments(appts);
                            }
                            break;
                        case "PI":
                            if (appts.size() == 0) {
                                System.out.println("The schedule calendar is empty.");
                            } else {
                                methods.printImagingAppointments(appts);
                            }
                            break;
                        case "PC":
                            if (appts.size() == 0) {
                                System.out.println("The schedule calendar is empty.");
                            } else {
                                methods.printProviderCharges(appts, technicians);
                            }
                            break;
                        default:
                            if (!command.isEmpty() && Character.isLowerCase(command.charAt(0))) {
                                System.out.println("Invalid command!");
                            } else if (appts.size() == 0) {
                                System.out.println("The schedule calendar is empty.");
                            } else {
                                return;
                            }
                            break;
                    }
                    input = in.nextLine();
                    splittedInput = input.split(",");
                }
            }
            else {
                System.out.println("Missing data tokens.");
                input = in.nextLine();
                splittedInput = input.split(",");
            }
        }
    }

    public void loadProviders() {
        String fileName = "src/main/java/com/example/ruclinicgui/clinic/src/providers.txt";
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("File not found: " + fileName);
            return;
        }

        try {
            Scanner scanner = new Scanner(file);  // Using Scanner to read the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Split the line into individual fields
                String[] splittedLine = line.split("  ");
                // Use .equals() to compare strings, not ==
                if (splittedLine[0].equals("D")) {
                    Profile profile = new Profile(splittedLine[1], splittedLine[2], stringToDate(splittedLine[3]));
                    Specialty specialty = setSpecialty(splittedLine[5]);
                    Doctor doctor = new Doctor(profile, setLocation(splittedLine[4]), specialty, splittedLine[6]);
                    providers.add(doctor);
                } else if (splittedLine[0].equals("T")) {
                    Profile profile = new Profile(splittedLine[1], splittedLine[2], stringToDate(splittedLine[3]));
                    Location location = setLocation(splittedLine[4]);
                    int rate = Integer.parseInt(splittedLine[5]);
                    Technician technician = new Technician(profile, location, rate);
                    providers.add(technician);
                    technicians.addTechnician(technician);
                }
            }
            pointer = technicians.getHead();
            scanner.close();  // Don't forget to close the Scanner
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Providers loaded to the list.");
    }

    public void printProviders() {
        sort.sortByProvider(providers); // check if this is the right syntax
        for (int i = 0; i<providers.size(); i++) {
            System.out.println(providers.get(i).toString());
        }
        technicians.display();
    }

    public Specialty setSpecialty(String input) {
        Specialty specialty;
        if (input.equals("FAMILY")) {
            return Specialty.Family;
        }
        else if (input.equals("PEDIATRICIAN")) {
            return Specialty.Pediatrician;
        }
        else if (input.equals("ALLERGIST")) {
            return Specialty.Allergist;
        }
        return null;
    }

    public Location setLocation(String input) {
        Location location;
        if (input.equals("BRIDGEWATER")) {
            return Location.Bridgewater;
        }
        else if (input.equals("CLARK")) {
            return Location.Clark;
        }
        else if (input.equals("PRINCETON")) {
            return Location.Princeton;
        }
        else if (input.equals("PISCATAWAY")) {
            return Location.Piscataway;
        }
        else if (input.equals("MORRISTOWN")) {
            return Location.Morristown;
        }
        else if (input.equals("EDISON")) {
            return Location.Edison;
        }
        return null;
    }

    public void scheduleImaging(String[] input)
    {
        if(input.length < 7) {
            System.out.println("Missing data tokens.");
            return;
        }
        //T,9/30/2024,1,John,Doe,12/13/1989,xray
        if(!checkApptDate(input[1])) {
            return;
        }
        Date apptDate = stringToDate(input[1]);
        Timeslot timeslot = new Timeslot();
        timeslot.setTimeslot(input[2]);
        if(!timeslot.setTimeslot(input[2])) {
            System.out.println(input[2] + " is not a valid time slot.");
            return;
        }
        Date dob = stringToDate(input[5]);
        if(!checkDOB(dob)){
            return;
        }
        Profile profile = new Profile(input[3], input[4], dob);
        Person patient = new Person(profile);
        if(!isValidImaging(input[6])) {
            System.out.println(input[6] + " - imaging service not provided");
            return;
        }
        int index = methods.identifyImagingAppt2(imagingAppts, patient.getProfile(), apptDate, timeslot);
        if (index != -1) {
            System.out.println(appts.get(index).getProfile().toString() + " has an existing appointment at the same timeslot.");
            return;
        }
        Radiology room = setRadioRoom(input[6]);
        Technician technician = techAvailable(appts, apptDate, timeslot, room);
        if(technician == null) {
            System.out.println("Cannot find an available technician at all locations for " + room.toString() + " at slot " + input[2]);
            return;
        }
        Imaging newImageAppt = new Imaging(apptDate, timeslot, patient, technician, room);

        appts.add(newImageAppt);
        imagingAppts.add(newImageAppt);
        System.out.println(input[1] + " " + timeslot.toString() + " " + profile.toString() + " " + technician.toString() + " " + room.toString() + " booked.");
    }

    public void scheduleDocAppt(String [] input) {
        if(input.length < 7) {
            System.out.println("Missing data tokens.");
            return;
        }
        if (!checkApptDate(input[1])) {
            return;
        }
        Date date = stringToDate(input[1]);
        Timeslot slot = new Timeslot();
        slot.setTimeslot(input[2]);
        if (!slot.setTimeslot(input[2])) {
            System.out.println(input[2] + " is not a valid time slot.");
            return;
        }
        Date dob = stringToDate(input[5]);
        if (!checkDOB(dob)) {
            return;
        }
        Profile profile = new Profile(input[3], input[4], stringToDate(input[5]));
        Person patient = new Person(profile);
        int matchingDoctorIndex = methods.getDoctorFromNPI(providers, input[6]);
        if (matchingDoctorIndex==-1) {
            System.out.println(input[6] + " - provider doesn't exist.");
            return;
        }
        Doctor doctor = (Doctor) providers.get(matchingDoctorIndex);
        if (methods.identifyAppointment(appts, profile, date, slot)!=-1) {
            System.out.println(appts.get(methods.timeslotTaken(appts, doctor, slot, date)).getProfile().toString() + " has an existing appointment at the same timeslot.");
            return;
        }
        if (methods.timeslotTaken(appts, doctor, slot, date) != -1) {
            System.out.println(doctor.toString() + " is not available at slot " + input[2] + ".");
            return;
        }
        Appointment newAppt = new Appointment(date, slot, patient, doctor);
        appts.add(newAppt);
        System.out.println(input[1] + " " + slot.toString() + " " + profile.toString() + " " + doctor.toString() + " booked.");
    }

    // given: clinic.src.Appointment date, timeslot, first name, last name, date of birth (date, timeslot and profile)
    public void cancel(String [] input) {
        if (input.length < 6) {
            System.out.println("Missing data tokens.");
            return;
        }
        Date date = stringToDate(input[1]);
        Timeslot slot = new Timeslot();
        if (!slot.setTimeslot(input[2])) {
            System.out.println(input[2] + " is not a valid time slot.");
            return;
        }
        Profile profile = new Profile(input[3], input[4], stringToDate(input[5]));
        int inptApp = methods.identifyAppointment(appts, profile, date, slot);
        if (inptApp!=-1)
        {
            Appointment currApp = appts.get(inptApp);
            Appointment appointment = new Appointment(currApp.getDate(), currApp.getTimeslot(), currApp.getProfile(), currApp.getProvider());
            appts.remove(appointment);
            System.out.println(date.toString() + " " + slot.toString() + " " + profile.toString() + " - appointment has been canceled.");
            return;
        }
        System.out.println(date.toString() + " " + slot.toString() + " " + profile.toString() + " - appointment does not exist.");
    }

    // Reschedule an appointment given: R, clinic.src.util.Date, Timeslot1, First name, Last name, DOB, Timeslot2
    public void reschedule(String[] input) {
        if (input.length < 7) {
            System.out.println("Missing data tokens.");
            return;
        }
        Date date = stringToDate(input[1]);
        Timeslot timeslot1 = new Timeslot();
        timeslot1.setTimeslot(input[2]);
        Timeslot timeslot2 = new Timeslot();
        timeslot2.setTimeslot(input[6]);
        if (!timeslot2.setTimeslot(input[6])) {
            System.out.println(input[6] + " is not a valid timeslot.");
            return;
        }
        String firstName = input[3];
        String lastName = input[4];
        Date dob = stringToDate(input[5]);
        Profile profile = new Profile(firstName, lastName, dob);

        int apptIndex = methods.identifyAppointment(appts, profile, date, timeslot1);

        if (apptIndex == -1) {
            System.out.println(input[1] + " " + timeslot1.toString() + " " + firstName + " " + lastName + " " + dob.toString() + " does not exist.");
            return;
        }

        int apptIndex2 = methods.identifyAppointment(appts, profile, date, timeslot2);
        if (apptIndex2 !=-1) {
            Appointment appointment = appts.get(apptIndex2);
            System.out.println(profile.toString() + " has an existing appointment at " + appointment.getDate().toString() + " " + timeslot2.toString());
            return;
        }

        Appointment appointment = appts.get(apptIndex);
        Provider provider = (Provider) appointment.getProvider();

        // [PATEL, BRIDGEWATER, Somerset 08807, FAMILY] is not available at slot 1.
        if (methods.timeslotTaken(appts, provider, timeslot2, date) != -1) {
            System.out.println(provider.toString() + " is not available at slot " + input[2]);
            return;
        }

        Appointment newAppt = appts.get(apptIndex);
        newAppt.setTimeslot(timeslot2);

        System.out.println("Rescheduled to " + input[1] + " " + timeslot2.toString() + " " + firstName + " " + lastName + " " + dob.toString() + " " + newAppt.getProvider().toString());
    }

    public boolean checkDOB(Date dob)
    {
        if(!dob.isValidDate()) {
            System.out.println("Patient dob: " + dob.toString() + " is not a valid calendar date");
            return false;
        }
        else if(dob.isToday() || dob.isFutureDate()) {
            System.out.println("Patient dob: " + dob.toString() + " is today or a date after today.");
            return false;
        }
        else {
            return true;
        }
    }

    public boolean checkApptDate(String input) {
        Date date = stringToDate(input);
        if (!date.isValidDate()) {
            System.out.println("clinic.src.Appointment date: " + input + " is not a valid calendar date");
            return false;
        }
        else if(date.isBeforeToday() || date.isToday()) {
            System.out.println("clinic.src.Appointment date: " + input + " is today or a date before today.");
            return false;
        }
        else if (date.onWeekend()) {
            System.out.println("clinic.src.Appointment date: " + input + " is Saturday or Sunday.");
            return false;
        }
        else if(!date.isWithinSixMonths()) {
            System.out.println("clinic.src.Appointment date: " + input + " is not within six months.");
            return false;
        }
        else {
            return true;
        }
    }

    public Date stringToDate(String date) {
        String[] dateString = date.split("/");

        if (dateString.length != 3) {
            throw new IllegalArgumentException("Invalid date format. Expected format: MM/DD/YYYY");
        }
        int month = Integer.parseInt(dateString[0]);
        int day = Integer.parseInt(dateString[1]);
        int year = Integer.parseInt(dateString[2]);

        Date dateObject = new Date(year, month, day);

        if (dateObject == null) {
            return null;
        }
        else {
            return dateObject;
        }
    }

    public boolean isValidImaging(String input) {
        input.toLowerCase();
        if(input.equals("xray") || input.equals("catscan") || input.equals("ultrasound"))
        {
            return true;
        }
        return false;
    }

    public Radiology setRadioRoom(String input) {
        input.toLowerCase();
        if(input.equals("xray")) {
            return Radiology.XRAY;
        }
        else if(input.equals("catscan")) {
            return Radiology.CATSCAN;
        }
        else if(input.equals("ultrasound")) {
            return Radiology.ULTRASOUND;
        }
        return null;
    }

    public Technician techAvailable(List<Appointment> imaging, Date date, Timeslot timeslot, Radiology room) {
        boolean isFirstFree = true;
        for(int i = 0; i<appts.size(); i++){
            if(imaging.size() != 0){
                isFirstFree = false;
            }
        }
        if(isFirstFree){
            Technician firstTech = pointer.getTechnician();
            //pointer = pointer.getNext();
            return firstTech;
        }
        Node start = pointer;
        do {
            Technician currentTech = pointer.getTechnician();
            int techAvailable = methods.identifyImagingAppt(imaging, currentTech, date, timeslot);
            boolean roomFree = methods.isRoomFree(imaging, currentTech, date, timeslot, room);

            if (techAvailable == -1 && roomFree) {
                Technician selectedTech = currentTech;
                pointer = pointer.getNext();
                return selectedTech;
            }
            pointer = pointer.getNext();
        } while (pointer != start);
        return null;
    }
}