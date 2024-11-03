/**
 * The ClinicManagerController class handles the GUI operations for a medical clinic management system.
 * It manages appointments, providers, technicians, and various scheduling operations.
 * This controller implements the JavaFX Initializable interface to set up the initial GUI state.
 * @author Nithya Konduru, Dhyanashri Raman
 */

package com.example.ruclinicgui;

import com.example.ruclinicgui.clinic.src.*;
import com.example.ruclinicgui.clinic.src.util.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;


public class ClinicManagerController implements Initializable {
    List<Appointment> appts = new List <>();
    List <Provider> providers = new List<>();
    CircularLinkedList technicians = new CircularLinkedList();
    Node pointer;
    List<Appointment> imagingAppts = new List<>();
    Sort sort = new Sort();
    ListMethods methods = new ListMethods();

    /**
     * Initializes the controller.
     * Sets up all GUI components and initial state.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseTimeslot.getItems().addAll(times);
        appointmentDatePickerR.setEditable(false);
        appointmentDatePicker.setEditable(false);
        dobDatePicker.setEditable(false);
        dobDatePickerR.setEditable(false);
        oldTimeslot.getItems().addAll(times);
        newTimeslot.getItems().addAll(times);
        outputArea.setEditable(false);
        outputAreaR.setEditable(false);
        initializeToggleButtons();
        updateProviderList();
        chooseOne.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            updateProviderList();
        });
        cityColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().name()));
        countyColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getCounty()));
        zipColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getZip()));
        clinicLocations.getItems().addAll(Location.values());
    }

    @FXML
    private DatePicker appointmentDatePicker;

    /**
     * Retrieves the selected date from the appointment date picker.
     * Parses and validates the date format.
     * @return Date object representing the selected date, or null if invalid
     */
    @FXML
    private Date getDateSelected() {
        String date = "";
        if(appointmentDatePicker.getValue() != null){
             date = appointmentDatePicker.getValue().toString();
        }
        if (appointmentDatePicker.getValue() == null || date.isEmpty()) {
            return null;
        }
        String[] dateParts = appointmentDatePicker.getEditor().getText().split("/");

        if (dateParts.length != 3) {
            return null;
        }
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        Date selectedDate = new Date(year, month, day);
        if (!selectedDate.isValidDate()) {
            return null;
        }
        return selectedDate;
    }

    /**
     * Retrieves the selected date from the rescheduling date picker.
     * Parses and validates the date format.
     * @return Date object representing the selected date, or null if invalid
     */
    @FXML
    private Date getDateSelectedR() {
        String date = "";
        if(appointmentDatePickerR.getValue() != null){
            date = appointmentDatePickerR.getValue().toString();
        }
        if (date == null || date.isEmpty()) {
            return null;
        }
        String[] dateParts = appointmentDatePickerR.getEditor().getText().split("/");

        if (dateParts.length != 3) {
            return null;
        }
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        Date selectedDate = new Date(year, month, day);
        if (!selectedDate.isValidDate()) {
            return null;
        }
        return selectedDate;
    }

    /**
     * Validates an appointment date string against business rules.
     * Checks if date is valid, not in past, not on weekend, and within 6 months.
     * @param input the date string to validate
     * @return true if date is valid according to all rules, false otherwise
     */
    public boolean checkApptDate(String input) {
        Date date = stringToDate(input);
        if (!date.isValidDate()) {
            showAlert("Invalid Date", "Appointment date: " + input + " is not a valid calendar date.", Alert.AlertType.ERROR);
            return false;
        }
        else if (date.isBeforeToday() || date.isToday()) {
            showAlert("Invalid Appointment Date", "Appointment date: " + input + " is today or a date before today.", Alert.AlertType.ERROR);
            return false;
        }
        else if (date.onWeekend()) {
            showAlert("Weekend Appointment", "Appointment date: " + input + " is Saturday or Sunday.", Alert.AlertType.ERROR);
            return false;
        }
        else if (!date.isWithinSixMonths()) {
            showAlert("Out of Range", "Appointment date: " + input + " is not within six months.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Displays an alert dialog with specified parameters.
     * @param title the title of the alert
     * @param message the content message
     * @param alertType the type of alert to display
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private TextArea outputArea;

    @FXML
    private RadioButton option1;

    @FXML
    private RadioButton option2;

    @FXML
    private ToggleGroup chooseOne;

    /**
     * Initializes the toggle buttons by setting their toggle group.
     * This allows for a mutually exclusive selection between option1 and option2.
     */
    @FXML
    public void initializeToggleButtons() {
        option1.setToggleGroup(chooseOne);
        option2.setToggleGroup(chooseOne);
    }

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    /**
     * Creates a Person object from the patient input fields.
     * Combines first name, last name and date of birth.
     * @return Person object with patient details, or null if invalid input
     */
    @FXML
    private Person getPatient() {
        String selectedDateText = "";
        if(dobDatePicker.getEditor().getText() != null) {
            selectedDateText = dobDatePicker.getEditor().getText();
        }
        if (selectedDateText == null || selectedDateText.isEmpty()) {
            return null;
        }
        String[] dateParts = selectedDateText.split("/");
        if (dateParts.length != 3) {
            return null;
        }
        String firstName = fname.getText();
        String lastName = lname.getText();
        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            return null;
        }
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        Date date = new Date(year, month, day);
        Profile patientProfile = new Profile(fname.getText().trim(), lname.getText().trim(), date);
        return new Person(patientProfile);
    }

    /**
     * Creates a Person object from the rescheduling patient input fields.
     * @return Person object with patient details, or null if invalid input
     */
    @FXML
    private Person getPatientR() {
        String selectedDateText = "";
        if(dobDatePickerR.getEditor().getText() != null){
            selectedDateText = dobDatePickerR.getEditor().getText();
        }
        if (selectedDateText == null || selectedDateText.isEmpty()) {
            return null;
        }
        String[] dateParts = selectedDateText.split("/");
        if (dateParts.length != 3) {
            return null;
        }
        String firstName = fname.getText();
        String lastName = lname.getText();
        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            return null;
        }
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        Date date = new Date(year, month, day);
        if (!checkDOB(date)) {
            return null;
        }
        Profile patientProfile = new Profile(fnameR.getText(), lnameR.getText(), date);
        return new Person(patientProfile);
    }

    /**
     * Retrieves the selected timeslot from the choice box.
     * @return Timeslot object representing the selected time, or null if none selected
     */
    @FXML
    private Timeslot getTimeslot() {
        String slotString = chooseTimeslot.getValue();
        if (slotString == null || slotString.isEmpty()) {
            return null;
        }
        Timeslot slot = new Timeslot();
        slot.setTimeslot(slotString);
        return slot;
    }

    /**
     * Retrieves the selected old timeslot for rescheduling.
     * @return Timeslot object representing the old time, or null if none selected
     */
    @FXML
    private Timeslot getOldTimeslot() {
        String slotString = oldTimeslot.getValue();
        if (slotString == null || slotString.isEmpty()) {
            return null;
        }
        Timeslot slot = new Timeslot();
        slot.setTimeslot(slotString);
        return slot;
    }

    /**
     * Retrieves the selected new timeslot for rescheduling.
     * @return Timeslot object representing the new time, or null if none selected
     */
    @FXML
    private Timeslot getNewTimeslot() {
        String slotString = newTimeslot.getValue();
        if (slotString == null || slotString.isEmpty()) {
            return null;
        }
        Timeslot slot = new Timeslot();
        slot.setTimeslot(slotString);
        return slot;
    }

    /**
     * Finds and returns the selected provider from the providers list.
     * @return Provider object matching the selected provider string, or null if none found
     */
    @FXML
    private Provider getProvider() {
        String selectedProvider = chooseProvider.getValue();
        Provider output = null;
        for (Provider provider : providers) {
            if (provider.toString().equals(selectedProvider)) {
                output = provider;
            }
        }
        return output;
    }

    /**
     * Converts a string input to corresponding Radiology enum value.
     * @param input string representing the radiology type
     * @return Radiology enum value, or null if invalid input
     */
    public Radiology setRadioRoom(String input) {
        String lowerCase = input.toLowerCase();
        if(lowerCase.equals("xray")) {
            return Radiology.XRAY;
        }
        else if(lowerCase.equals("catscan")) {
            return Radiology.CATSCAN;
        }
        else if(lowerCase.equals("ultrasound")) {
            return Radiology.ULTRASOUND;
        }
        return null;
    }

    /**
     * Finds an available technician for an imaging appointment.
     * Checks scheduling conflicts and room availability.
     * @param imaging list of existing imaging appointments
     * @param date requested appointment date
     * @param timeslot requested timeslot
     * @param room requested radiology room
     * @return available Technician object, or null if none available
     */
    public Technician techAvailable(List<Appointment> imaging, Date date, Timeslot timeslot, Radiology room) {
        boolean isFirstFree = true;
        for(int i = 0; i<appts.size(); i++){
            if(imaging.size() != 0){
                isFirstFree = false;
            }
        }
        if(isFirstFree){
            Technician firstTech = pointer.getTechnician();
            return firstTech;
        }
        Node start = pointer;
        do {
            if (pointer!=null) {
                Technician currentTech = pointer.getTechnician();
                int techAvailable = methods.identifyImagingAppt(imaging, currentTech, date, timeslot);
                boolean roomFree = methods.isRoomFree(imaging, currentTech, date, timeslot, room);
                if (techAvailable == -1 && roomFree) {
                    Technician selectedTech = currentTech;
                    pointer = pointer.getNext();
                    return selectedTech;
                }
                pointer = pointer.getNext();
            }
        } while (pointer != start);
        return null;
    }

    /**
     * Schedules a new imaging appointment.
     * Validates all inputs and checks for conflicts before scheduling.
     */
    @FXML
    private void scheduleImaging() {
        StringBuilder missingFields = new StringBuilder();
        if (getDateSelected() == null) {
            missingFields.append("• Appointment Date\n");
        }
        if (getTimeslot() == null) {
            missingFields.append("• Timeslot\n");
        }
        if (getPatient() == null) {
            missingFields.append("• Patient Details\n");
        }
        if (chooseProvider.getValue() == null) missingFields.append("• Imaging Type\n");
        if (!missingFields.isEmpty()) {
            showAlertForSchedule("Missing Information", "Please fill out the following fields:\n" + missingFields.toString());
        }
        Date date = getDateSelected();
        Timeslot slot = getTimeslot();
        Person patient = getPatient();
        String imagingType = chooseProvider.getValue();
        boolean apptDateValid = true;
        boolean dobValid = true;
        if (date != null) {
            apptDateValid = checkApptDate(date.toString());
        }
        if (patient != null) {
            dobValid = checkDOB(patient.getProfile().getDob());
        }
        if (!apptDateValid || !dobValid) {
            return;
        }
        Radiology room = null;
        if(imagingType != null){
            room = setRadioRoom(imagingType);
        }
        if (room == null) {
            showAlertForSchedule("Invalid Imaging Type", imagingType + " is not a valid imaging service.");
            return;
        }
        int index = -3;
        if(patient != null && slot != null && date != null){
            index = methods.identifyImagingAppt2(imagingAppts, patient.getProfile(), date, slot);
        }
        if (patient != null && index != -1 && date != null) {
            showAlertForSchedule("Duplicate Appointment", patient.getProfile().toString() + " has an existing appointment at the same time.");
            return;
        }
        Technician technician = null;
        if(room != null && slot != null && date != null){
            technician = techAvailable(imagingAppts, date, slot, room);
        }
        if (technician == null && slot != null && date != null) {
            showAlertForSchedule("No Technician Available", "No available technician for " + imagingType + " at " + slot.toString());
            return;
        }
        if(!missingFields.isEmpty() && !loadProvidersButton.isDisabled())
        {
            showAlert("Load Provider's Error", "The providers have not been loaded.", Alert.AlertType.WARNING);
        }

        if(patient != null && room != null && slot != null && date != null){
            Imaging newImageAppt = new Imaging(date, slot, patient, technician, room);
            appts.add(newImageAppt);
            imagingAppts.add(newImageAppt);
            outputArea.appendText(date.toString() + " " + slot.toString() + " " + patient.getProfile().toString()
                    + " with " + technician.toString() + " in " + room.toString() + " booked.\n");
        }

    }

    /**
     * Schedules a new regular appointment.
     * Validates all inputs and checks for conflicts before scheduling.
     */
    @FXML
    private void schedule() {
        StringBuilder missingFields = new StringBuilder();
        if (getDateSelected() == null) {
            missingFields.append("• Appointment Date\n");
        }
        if (getTimeslot() == null) {
            missingFields.append("• Timeslot\n");
        }
        if (getPatient() == null) {
            missingFields.append("• Patient Details\n");
        }
        if (getProvider() == null) {
            missingFields.append("• Provider\n");
        }
        if (!missingFields.isEmpty()) {
            showAlertForSchedule("Missing Information", "Please fill out the following fields:\n" + missingFields.toString());
        }
        String selectedDateText = appointmentDatePicker.getEditor().getText();
        String formattedDate;
        if (selectedDateText != null && !selectedDateText.isEmpty()) {
            formattedDate = selectedDateText;
        } else {
            return;
        }
        Date date = getDateSelected();
        Timeslot slot = getTimeslot();
        Person patient = getPatient();
        Provider provider = getProvider();
        boolean apptDateValid = true;
        boolean dobValid = true;
        if (formattedDate !=null) {
            apptDateValid = checkApptDate(formattedDate);
        }
        if (patient != null) {
            dobValid = checkDOB(patient.getProfile().getDob());
        }
        if (patient!=null && provider instanceof Doctor) {
            Doctor doctor = (Doctor) provider;
            if (methods.identifyAppointment(appts, patient.getProfile(), date, slot) != -1) {
                showAlertForSchedule("Duplicate Appointment", patient.getProfile().toString() + " has an existing appointment at the same time.");
                return;
            }
            if (methods.timeslotTaken(appts, doctor, slot, date) != -1 ) {
                showAlertForSchedule("Timeslot Unavailable", doctor.toString() + " is not available at " + slot.toString() + ".");
                return;
            }
        }
        if(!missingFields.isEmpty() && !loadProvidersButton.isDisabled())
        {
            showAlert("Load Provider's Error", "The providers have not been loaded.", Alert.AlertType.WARNING);
        }
        if (missingFields.isEmpty() && apptDateValid && slot != null && slot.setTimeslot(slot.toString())
                && patient != null && dobValid && provider instanceof Doctor) {
            Appointment newAppt = new Appointment(date, slot, patient, provider);
            appts.add(newAppt);
            outputArea.appendText(formattedDate + " " + slot.toString() + " " + patient.getProfile().toString() + " " + provider.toString() + " booked.\n");
        }
    }

    /**
     * Retrieves the selected radiology room.
     * @return Radiology enum value representing selected room, or null if invalid
     */
    @FXML
    private Radiology getRoom() {
        String roomString = chooseProvider.getValue();
        if (roomString.equals("XRAY")) {
            return Radiology.XRAY;
        }
        if (roomString.equals("CATSCAN")) {
            return Radiology.CATSCAN;
        }
        if (roomString.equals("ULTRASOUND")) {
            return Radiology.ULTRASOUND;
        }
        return null;
    }

    /**
     * Cancels an existing appointment.
     * Validates inputs and removes appointment if found.
     */
    @FXML
    protected void cancel() {
        StringBuilder missingFields = new StringBuilder();

        if (getDateSelected() == null) {
            missingFields.append("• Appointment Date\n");
        }
        if (getTimeslot() == null) {
            missingFields.append("• Timeslot\n");
        }
        if (getPatient() == null) {
            missingFields.append("• Patient Details\n");
        }
        if (!missingFields.isEmpty()) {
            showAlertForSchedule("Missing Information", "Please fill out the following fields:\n" + missingFields.toString());
            return;
        }
        Date date = getDateSelected();
        Timeslot slot = getTimeslot();
        Person patient = getPatient();
        int inptApp = methods.identifyAppointment(appts, patient.getProfile(), date, slot);
        if (inptApp!=-1)
        {
            Appointment currApp = appts.get(inptApp);
            Appointment appointment = new Appointment(currApp.getDate(), currApp.getTimeslot(), currApp.getProfile(), currApp.getProvider());
            appts.remove(appointment);
            outputArea.appendText(date.toString() + " " + slot.toString() + " " + patient.getProfile().toString() + " - appointment has been canceled.\n");
            return;
        }
        outputArea.appendText(date.toString() + " " + slot.toString() + " " + patient.getProfile().toString() + " - appointment does not exist.\n");
    }

    /**
     * Event handler for cancel button click.
     * Calls the cancel method.
     */
    @FXML
    protected void onCancelClick() {
        cancel();
    }

    @FXML
    private DatePicker appointmentDatePickerR;

    @FXML
    private TextField fnameR;

    @FXML
    private TextField lnameR;

    @FXML
    private DatePicker dobDatePickerR;

    @FXML
    private ChoiceBox<String> oldTimeslot;

    @FXML
    private ChoiceBox<String> newTimeslot;

    @FXML
    private TextArea outputAreaR;

    /**
     * Event handler for reschedule button click.
     * Calls the reschedule method.
     */
    @FXML
    protected void onRescheduleClick() {
        reschedule();
    }

    /**
     * Reschedules an existing appointment to a new timeslot.
     * Validates all inputs and checks for conflicts.
     */
    @FXML
    protected void reschedule() {
        StringBuilder missingFields = new StringBuilder();
        if (getDateSelectedR() == null) {
            missingFields.append("• Appointment Date\n");
        }
        if (getOldTimeslot() == null) {
            missingFields.append("• Old Timeslot\n");
        }
        if (getNewTimeslot() == null) {
            missingFields.append("• New Timeslot\n");
        }
        if (getPatient() == null) {
            missingFields.append("• Patient Details\n");
        }
        if (!missingFields.isEmpty()) {
            showAlertForSchedule("Missing Information", "Please fill out the following fields:\n" + missingFields.toString());
        }
        String selectedDateText = appointmentDatePickerR.getEditor().getText();
        String formattedDate;
        if (selectedDateText != null && !selectedDateText.isEmpty()) {
            formattedDate = selectedDateText;
        } else {
            return;
        }
        Date date = getDateSelectedR();
        Timeslot oldSlot = getOldTimeslot();
        Timeslot newSlot = getNewTimeslot();
        Person patient = getPatientR();
        checkApptDate(formattedDate);
        if (patient != null) {
            checkDOB(patient.getProfile().getDob());
        }
        int apptIndex = methods.identifyAppointment(appts, patient.getProfile(), date, oldSlot);

        if (apptIndex == -1) {
            outputAreaR.appendText(formattedDate + " " + oldSlot.toString() + " " + patient.getProfile().getFirstName() + " " + patient.getProfile().getLastName() + " " + patient.getProfile().getDob().toString() + " does not exist.");
            return;
        }
        int apptIndex2 = methods.identifyAppointment(appts, patient.getProfile(), date, newSlot);
        if (apptIndex2 !=-1) {
            Appointment appointment = appts.get(apptIndex2);
            outputAreaR.appendText("\n" + patient.getProfile().toString() + " has an existing appointment at " + appointment.getDate().toString() + " " + newSlot.toString() + "\n");
            return;
        }
        Appointment appointment = appts.get(apptIndex);
        Provider provider = (Provider) appointment.getProvider();
        if (methods.timeslotTaken(appts, provider, newSlot, date) != -1) {
            outputAreaR.appendText("\n" + provider.toString() + " is not available at " + newTimeslot.getValue() + "\n");
            return;
        }

        Appointment newAppt = appts.get(apptIndex);
        newAppt.setTimeslot(newSlot);

        outputAreaR.appendText("\nRescheduled to " + formattedDate + " " + newSlot.toString() + " " + patient.getProfile().getFirstName() + " " + patient.getProfile().getLastName() + " " + patient.getProfile().getDob().toString() + " " + newAppt.getProvider().toString() + "\n");
    }

    /**
     * Displays an alert specific to scheduling operations.
     * @param title the title of the alert
     * @param message the content message
     */
    private void showAlertForSchedule(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Validates a date of birth.
     * Checks if date is valid and not in future.
     * @param dob the date to validate
     * @return true if date is valid, false otherwise
     */
    public boolean checkDOB(Date dob) {
        if (!dob.isValidDate()) {
            showAlertDOB("Invalid Date", "Patient DOB " + dob.toString() + " is not a valid calendar date.");
            return false;
        } else if (dob.isToday() || dob.isFutureDate()) {
            showAlertDOB("Invalid Date", "Patient DOB " + dob.toString() + " is today or a future date.");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Displays an error alert dialog with a specified title and message.
     * This method is specifically used to notify users of issues related to
     * the date of birth (DOB) input.
     * @param title   the title of the alert dialog
     * @param message the message content of the alert dialog
     */
    private void showAlertDOB(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private ChoiceBox<String> chooseTimeslot;

    @FXML
    private ChoiceBox<String> chooseProvider;

    @FXML
    private Button loadProvidersButton;

    /**
     * Gets the selected appointment type from radio buttons.
     * @param radioGroup the toggle group containing the radio buttons
     * @return String representing selected type ("D" for doctor, "T" for technician)
     */
    public String getTypeOfAppointment(ToggleGroup radioGroup) {
        Toggle selectedToggle =radioGroup.getSelectedToggle();
        if (selectedToggle instanceof RadioButton) {
            RadioButton selectedRadioButton = (RadioButton) selectedToggle;
            if (selectedRadioButton == option1) {
                return "D";
            } else if (selectedRadioButton == option2) {
                return "T";
            }
        }
        return "No option is selected";
    }

    private final String[] times = {"9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM"};

    @FXML
    private Text providerText;

    /**
     * Updates the provider list based on selected appointment type.
     * Filters providers based on whether doctor or technician is selected.
     */
    private void updateProviderList(){
        if(option1.isSelected())
        {
            providerText.setText("Provider: ");
            chooseProvider.getItems().clear();
            for(Provider provider : providers){
                if(provider instanceof Doctor){
                    chooseProvider.getItems().add(provider.toString());
                }
            }
        }
        else if(option2.isSelected()) {
            providerText.setText("Room: ");
            chooseProvider.getItems().clear();
            chooseProvider.getItems().addAll("XRAY", "CATSCAN", "ULTRASOUND");
        }
    }

    @FXML
    private TextArea printOutput;

    /**
     * Handles the event when the "Print by Appointment" button is clicked.
     * Appends the appointments, sorted by appointment details, to the output area.
     */
    @FXML
    protected void onPAClick() {
        printOutput.appendText(methods.printByAppointment(appts));
    }

    /**
     * Handles the event when the "Print by Charges" button is clicked.
     * Checks if there are any appointments in the system.
     * If there are, appends provider charges to the output area; otherwise, displays a message indicating no appointments.
     */
    @FXML
    protected void onPCClick() {
        if(appts.size() == 0){
            printOutput.appendText("\nThere are no appointments in the system." + "\n");
        }
        else{
            printOutput.appendText(methods.printProviderCharges(appts, technicians));
        }
    }

    /**
     * Handles the event when the "Print Imaging Appointments" button is clicked.
     * Appends imaging appointments to the output area.
     */
    @FXML
    protected void onPIClick() {
        printOutput.appendText(methods.printImagingAppointments(appts));
    }

    /**
     * Handles the event when the "Print by Location" button is clicked.
     * Appends appointments sorted by location to the output area.
     */
    @FXML
    protected void onPLClick() {
        printOutput.appendText(methods.printByLocation(appts));
    }

    /**
     * Handles the event when the "Print Office Appointments" button is clicked.
     * Appends office appointments to the output area.
     */
    @FXML
    protected void onPOClick() {
        printOutput.appendText(methods.printOfficeAppointments(appts));
    }

    /**
     * Handles the event when the "Print by Patient" button is clicked.
     * Appends appointments sorted by patient to the output area.
     */
    @FXML
    protected void onPPClick() {
        printOutput.appendText(methods.printByPatient(appts));
    }

    /**
     * Handles the event when the "Print All Charges" button is clicked.
     * Appends all charge details for appointments to the output area.
     */
    @FXML
    protected void onPSClick() {
        printOutput.appendText(methods.printAllCharge(appts));
    }

    /**
     * Handles the event when the "Load Providers" button is clicked.
     * Opens a file chooser dialog to allow the user to select a provider file in .txt format.
     * If a valid text file is selected, initializes toggle buttons, loads providers from the file,
     * and prints the provider information. Disables the "Load Providers" button upon successful load.
     * If an invalid file type is selected, displays an error alert prompting the user to select a valid text file.
     */
    @FXML
    protected void onLoadProvidersClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Provider File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(loadProvidersButton.getScene().getWindow());
        if (file != null) {
            if (file.getName().endsWith(".txt")) {
                initializeToggleButtons();
                loadProviders(file);
                printProviders();

                loadProvidersButton.setDisable(true);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid File Type");
                alert.setHeaderText(null);
                alert.setContentText("Please select a valid text file (.txt).");
                alert.showAndWait();
            }
        }
    }

    /**
     * Handles the action when the schedule button is clicked.
     * Depending on the type of appointment selected, it schedules a doctor
     * appointment or an imaging appointment. If no type is selected,
     * it shows an alert indicating that a selection is required.
     */
    @FXML
    protected void onScheduleClick() {
        if(getTypeOfAppointment(chooseOne).equals("D")) {
            schedule();
        }
        else if(getTypeOfAppointment(chooseOne).equals("T")){
            scheduleImaging();
        }
        else if (chooseOne.getSelectedToggle() == null) {
            showAlertForToggle("Selection Required", "Please select a type of appointment to continue");
        }
    }

    /**
     * Displays an alert dialog with a specified title and message.
     * This method is used to notify users of important information or errors.
     *
     * @param title   the title of the alert dialog
     * @param message the message content of the alert dialog
     */
    private void showAlertForToggle(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Handles the action when the clear button is clicked.
     * It clears all input fields and resets the appointment details.
     */
    @FXML
    protected void onClearClick() {
        clear();
    }

    /**
     * Clears all input fields related to the appointment scheduling.
     * This includes resetting the date pickers and clearing text fields.
     */
    @FXML
    private void clear() {
        appointmentDatePicker.setValue(null);
        chooseOne.selectToggle(null);
        dobDatePicker.setValue(null);
        fname.setText("");
        lname.setText("");
        dobDatePicker.setValue(null);
        chooseTimeslot.setValue(null);
        chooseProvider.setValue(null);
    }

    @FXML
    private TableView<Location> clinicLocations;

    @FXML
    private TableColumn<Location, String> cityColumn;

    @FXML
    private TableColumn<Location, String> countyColumn;

    @FXML
    private TableColumn<Location, String> zipColumn;

    /**
     * Loads provider data from a text file.
     * Parses file and creates appropriate Provider objects.
     * @param file the file containing provider data
     */
    public void loadProviders(File file) {
        if (!file.exists()) {
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] splittedLine = line.split("  ");
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
            for (Provider provider : providers) {
                if (getTypeOfAppointment(chooseOne).equals("D")) {
                    if (provider instanceof Doctor) {
                        chooseProvider.getItems().add(provider.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        outputArea.appendText("Providers loaded to the list." + "\n");
    }

    /**
     * Prints all providers in sorted order.
     * Displays both doctors and technicians.
     */
    public void printProviders() {
        sort.sortByProvider(providers);
        for (int i = 0; i<providers.size(); i++) {
            outputArea.appendText(providers.get(i).toString() + "\n");
        }
        outputArea.appendText(technicians.display() + "\n");
    }

    /**
     * Converts a date string to Date object.
     * @param date string in format MM/DD/YYYY
     * @return Date object representing the input string
     */
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

    /**
     * Converts a string to corresponding Specialty enum value.
     * @param input string representing specialty
     * @return Specialty enum value, or null if invalid input
     */
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

    /**
     * Converts a string to corresponding Location enum value.
     * @param input string representing location
     * @return Location enum value, or null if invalid input
     */
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
}