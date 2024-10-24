module com.example.ruclinicgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.ruclinicgui to javafx.fxml;
    exports com.example.ruclinicgui;
}