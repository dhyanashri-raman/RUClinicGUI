package com.example.ruclinicgui;

import com.example.ruclinicgui.clinic.src.*;
import com.example.ruclinicgui.clinic.src.util.*;
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

    List<Appointment> appts = new List <Appointment>();
    List <Provider> providers = new List <Provider>();
    CircularLinkedList technicians = new CircularLinkedList();
    Node pointer;
    List<Appointment> imagingAppts = new List<Appointment>();
    Sort sort = new Sort();
    ListMethods methods = new ListMethods();

    @FXML
    private RadioButton option1;  // Corresponds to fx:id="option1" in FXML

    @FXML
    private RadioButton option2;  // Corresponds to fx:id="option2" in FXML

    @FXML
    private ToggleGroup choiceGroup;  // Corresponds to fx:id="choiceGroup" in FXML

    @FXML
    private ChoiceBox<String> chooseTimeslot;

    @FXML
    private ChoiceBox<Provider> chooseProvider;

    @FXML
    private Text timeslot;

    @FXML
    private Button loadProvidersButton;

    // This method is called when a RadioButton is selected
    @FXML
    private void handleSelection() {
        // Get the selected RadioButton from the ToggleGroup
        RadioButton selectedRadioButton = (RadioButton) choiceGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            // Retrieve the text of the selected RadioButton
            String selectedOption = selectedRadioButton.getText();
            System.out.println("Selected option: " + selectedOption);
            // Additional logic can be implemented based on the selection
        }
    }

    private final String[] times = {"9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseTimeslot.getItems().addAll(times);
    }

    @FXML
    protected void onLoadProvidersClick() {
        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Provider File");

        // Set extension filters to show .txt files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(loadProvidersButton.getScene().getWindow());

        if (file != null) {
            // Validate the file extension
            if (file.getName().endsWith(".txt")) {
                System.out.println("File selected: " + file.getAbsolutePath());
                timeslot.setText("Selected file: " + file.getName());

                // Call the loadProviders method with the selected file
                loadProviders(file);
                printProviders();

                loadProvidersButton.setDisable(true);
            } else {
                // Invalid file type
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid File Type");
                alert.setHeaderText(null);
                alert.setContentText("Please select a valid text file (.txt).");
                alert.showAndWait();
            }
        } else {
            timeslot.setText("File selection canceled.");
        }
    }

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

            // Populate the ChoiceBox with provider names
            chooseProvider.getItems().clear(); // Clear existing items
            //chooseProvider.getItems().addAll(providers); // Add all providers to the ChoiceBox

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