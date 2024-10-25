package com.example.ruclinicgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClinicManagerMain {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}