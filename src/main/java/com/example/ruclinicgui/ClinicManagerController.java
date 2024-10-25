package com.example.ruclinicgui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ClinicManagerController implements Initializable {

    @FXML
    private RadioButton option1;  // Corresponds to fx:id="option1" in FXML

    @FXML
    private RadioButton option2;  // Corresponds to fx:id="option2" in FXML

    @FXML
    private ToggleGroup choiceGroup;  // Corresponds to fx:id="choiceGroup" in FXML

    @FXML
    private ChoiceBox<String> chooseTimeslot;

    @FXML
    private Text timeslot;

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
}