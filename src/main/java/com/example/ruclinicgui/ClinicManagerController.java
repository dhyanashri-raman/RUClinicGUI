package com.example.ruclinicgui;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class ClinicManagerController {

        @FXML
        private RadioButton option1;  // Corresponds to fx:id="option1" in FXML

        @FXML
        private RadioButton option2;  // Corresponds to fx:id="option2" in FXML

        @FXML
        private ToggleGroup choiceGroup;  // Corresponds to fx:id="choiceGroup" in FXML

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

}
