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
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @FXML
    private DatePicker appointmentDatePicker;

    public ClinicManagerController() throws IOException {
    }

    @FXML
    private Date getDateSelected() {
        if (appointmentDatePicker.getValue() == null) {
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

    @FXML
    private Date getDateSelectedR() {
        if (appointmentDatePickerR.getValue() == null) {
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

    public boolean checkApptDate(String input) {
        Date date = stringToDate(input);

        // Check if the date is valid
        if (!date.isValidDate()) {
            showAlert("Invalid Date", "Appointment date: " + input + " is not a valid calendar date.", Alert.AlertType.ERROR);
            return false;
        }
        else if (date.isBeforeToday() || date.isToday()) {
            showAlert("Invalid Appointment Date", "Appointment date: " + input + " is today or a date before today.", Alert.AlertType.WARNING);
            return false;
        }
        else if (date.onWeekend()) {
            showAlert("Weekend Appointment", "Appointment date: " + input + " is Saturday or Sunday.", Alert.AlertType.WARNING);
            return false;
        }
        else if (!date.isWithinSixMonths()) {
            showAlert("Out of Range", "Appointment date: " + input + " is not within six months.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: Remove if you want a header
        alert.setContentText(message);
        alert.showAndWait(); // Shows the alert and waits for the user to close it
    }

    @FXML
    private TextArea outputArea;

    @FXML
    private RadioButton option1;  // Corresponds to fx:id="option1" in FXML

    @FXML
    private RadioButton option2;  // Corresponds to fx:id="option2" in FXML

    @FXML
    private ToggleGroup chooseOne;  // Corresponds to fx:id="choiceGroup" in FXML

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

    @FXML
    private Person getPatient() {
        String selectedDateText = dobDatePicker.getEditor().getText();
        if (selectedDateText == null || selectedDateText.isEmpty()) {
            return null;
        }
        String[] dateParts = selectedDateText.split("/");
        if (dateParts.length != 3) {
            return null;
        }
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        Date date = new Date(year, month, day);
        if (!checkDOB(date)) {
            return null;
        }
        Profile patientProfile = new Profile(fname.getText(), lname.getText(), date);
        return new Person(patientProfile);
    }

    @FXML
    private Person getPatientR() {
        String selectedDateText = dobDatePickerR.getEditor().getText();
        if (selectedDateText == null || selectedDateText.isEmpty()) {
            return null;
        }
        String[] dateParts = selectedDateText.split("/");
        if (dateParts.length != 3) {
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
        checkApptDate(formattedDate);
        if (patient != null) {
            checkDOB(patient.getProfile().getDob());
        }
        if (patient!=null && provider instanceof Doctor) {
            Doctor doctor = (Doctor) provider;
            if (methods.identifyAppointment(appts, patient.getProfile(), date, slot) != -1) {
                showAlertForSchedule("Duplicate Appointment", patient.getProfile().toString() + " already has an appointment at this time.");
                return;
            }
            if (methods.timeslotTaken(appts, doctor, slot, date) != -1 ) {
                showAlertForSchedule("Timeslot Unavailable", doctor.toString() + " is not available at " + slot.toString() + ".");
                return;
            }
        }
        if (missingFields.length() == 0 && checkApptDate(formattedDate) && slot != null && slot.setTimeslot(slot.toString())
                && patient != null && checkDOB(patient.getProfile().getDob()) && provider instanceof Doctor) {
            Appointment newAppt = new Appointment(date, slot, patient, provider);
            appts.add(newAppt);
            outputArea.appendText(formattedDate + " " + slot.toString() + " " + patient.getProfile().toString() + " " + provider.toString() + " booked.\n");
        }
    }


//    @FXML
//    private void schedule() {
//        StringBuilder missingFields = new StringBuilder();
//        if (getDateSelected() == null) {
//            missingFields.append("• Appointment Date\n");
//        }
//        if (getTimeslot() == null) {
//            missingFields.append("• Timeslot\n");
//        }
//        if (getPatient() == null) {
//            missingFields.append("• Patient Details\n");
//        }
//        if (getProvider() == null) {
//            missingFields.append("• Provider\n");
//        }
//        if (!missingFields.isEmpty()) {
//            showAlertForSchedule("Missing Information", "Please fill out the following fields:\n" + missingFields.toString());
//        }
//        String selectedDateText = appointmentDatePicker.getEditor().getText();
//        String formattedDate;
//        if (selectedDateText != null && !selectedDateText.isEmpty()) {
//            formattedDate = selectedDateText;
//        } else {
//            return;
//        }
//        if (!checkApptDate(formattedDate)) {
//            return;
//        }
//        Date date = getDateSelected();
//        Timeslot slot = getTimeslot();
//        Person patient = getPatient();
//        Provider provider = getProvider();
//        if (!checkApptDate(formattedDate)) {
//            return;
//        }
//        if (patient != null && !checkDOB(patient.getProfile().getDob())) {
//            return;
//        }
//        if (patient!=null && provider instanceof Doctor) {
//            Doctor doctor = (Doctor) provider;
//            if (methods.identifyAppointment(appts, patient.getProfile(), date, slot) != -1) {
//                showAlertForSchedule("Duplicate Appointment", patient.getProfile().toString() + " already has an appointment at this time.");
//                return;
//            }
//            if (methods.timeslotTaken(appts, doctor, slot, date) != -1) {
//                showAlertForSchedule("Timeslot Unavailable", doctor.toString() + " is not available at " + slot.toString() + ".");
//                return;
//            }
//        } else {
//            showAlertForSchedule("Invalid Provider", "Selected provider is not a doctor.");
//            return;
//        }
//        Appointment newAppt = new Appointment(date, slot, patient, provider);
//        appts.add(newAppt);
//        outputArea.appendText(formattedDate + " " + slot.toString() + " " + patient.getProfile().toString() + " " + provider.toString() + " booked.\n");
//    }

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

        if (!slot.setTimeslot(slot.toString())) {
            showAlertForSchedule("Invalid Timeslot", slot.toString() + " is not a valid timeslot.");
            return;
        }

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

    @FXML
    protected void onRescheduleClick() {
        reschedule();
    }

    @FXML
    protected void reschedule() {
        StringBuilder missingFields = new StringBuilder();
        // Check for missing fields and build the message
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
            outputAreaR.appendText(formattedDate + " " + oldSlot.toString() + " " + patient.getProfile().getFirstName() + " " + patient.getProfile().getLastName() + " " + patient.getProfile().getDob().toString() + " does not exist.\n");
            return;
        }

        int apptIndex2 = methods.identifyAppointment(appts, patient.getProfile(), date, newSlot);
        if (apptIndex2 !=-1) {
            Appointment appointment = appts.get(apptIndex2);
            outputAreaR.appendText(patient.getProfile().toString() + " has an existing appointment at " + appointment.getDate().toString() + " " + newSlot.toString() + "\n");
            return;
        }

        Appointment appointment = appts.get(apptIndex);
        Provider provider = (Provider) appointment.getProvider();

        // [PATEL, BRIDGEWATER, Somerset 08807, FAMILY] is not available at slot 1.
        if (methods.timeslotTaken(appts, provider, newSlot, date) != -1) {
            outputAreaR.appendText(provider.toString() + " is not available at " + newTimeslot.getValue() + "\n");
            return;
        }

        Appointment newAppt = appts.get(apptIndex);
        newAppt.setTimeslot(newSlot);

        outputAreaR.appendText("Rescheduled to " + formattedDate + " " + newSlot.toString() + " " + patient.getProfile().getFirstName() + " " + patient.getProfile().getLastName() + " " + patient.getProfile().getDob().toString() + " " + newAppt.getProvider().toString() + "\n");
    }



    private void showAlertForSchedule(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change the type to ERROR, WARNING, etc. as needed
        alert.setTitle(title);
        alert.setHeaderText(null); // You can set a header text if needed
        alert.setContentText(message);
        alert.showAndWait(); // Show the dialog and wait for the user to close it
    }

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

    private void showAlertDOB(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header
        alert.setContentText(message);
        alert.showAndWait(); // Display the alert and wait for the user to close it
    }

    @FXML
    private ChoiceBox<String> chooseTimeslot;

    @FXML
    private ChoiceBox<String> chooseProvider;

    @FXML
    private Button loadProvidersButton;

    public String getTypeOfAppointment(ToggleGroup radioGroup) {
        // Get the selected toggle from the group and cast it to a RadioButton
        Toggle selectedToggle =radioGroup.getSelectedToggle();
        if (selectedToggle instanceof RadioButton) {
            RadioButton selectedRadioButton = (RadioButton) selectedToggle;
            // Check which RadioButton is selected and return the corresponding string
            if (selectedRadioButton == option1) {
                return "D";
            } else if (selectedRadioButton == option2) {
                return "T";
            }
        }
        return "No option is selected";  // Return this if no RadioButton is selected
    }


    private final String[] times = {"9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseTimeslot.getItems().addAll(times);
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
    private Text providerText;

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

    @FXML
    private Button PAButton;

    @FXML
    protected void onPAClick() {
        printOutput.appendText(methods.printByAppointment(appts));
    }

    @FXML
    private Button PCButton;

    @FXML
    protected void onPCClick() {
        printOutput.appendText(methods.printProviderCharges(appts, technicians));
    }

    @FXML
    private Button PIButton;

    @FXML
    protected void onPIClick() {
        printOutput.appendText(methods.printImagingAppointments(appts));
    }

    @FXML
    private Button PLButton;

    @FXML
    protected void onPLClick() {
        printOutput.appendText(methods.printByLocation(appts));
    }

    @FXML
    private Button POButton;

    @FXML
    protected void onPOClick() {
        printOutput.appendText(methods.printOfficeAppointments(appts));
    }

    @FXML
    private Button PPButton;

    @FXML
    protected void onPPClick() {
        printOutput.appendText(methods.printByPatient(appts));
    }

    @FXML
    private Button PSButton;

    @FXML
    protected void onPSClick() {
        printOutput.appendText(methods.printAllCharge(appts));
    }

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

    @FXML
    protected void onScheduleClick() {
        schedule();
    }

    @FXML
    protected void onClearClick() {
        clear();
    }

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



    public void loadProviders(File file) {
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            return;
        }

        try (Scanner scanner = new Scanner(file)) { // Using try-with-resources to automatically close the scanner
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

    public void printProviders() {
        sort.sortByProvider(providers); // check if this is the right syntax
        for (int i = 0; i<providers.size(); i++) {
            outputArea.appendText(providers.get(i).toString() + "\n");
        }
        outputArea.appendText(technicians.display() + "\n");
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
}