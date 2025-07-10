module WarAge.TD {
    requires com.almasb.fxgl.all; // Keep this if you are using FXGL
    requires com.fasterxml.jackson.databind;
    requires java.net.http;

    // Add this line for the Jackson JavaTime module
    requires com.fasterxml.jackson.datatype.jsr310;

    // Open packages for FXML
    opens com.warage to javafx.fxml;
    opens com.warage.UI to javafx.fxml;
    opens com.warage.UI.Authentication to javafx.fxml;

    // Open your Model package to Jackson Databind for serialization/deserialization
    opens com.warage.Model to com.fasterxml.jackson.databind; // This is crucial for PlayerProfile
    opens com.warage.Views to javafx.fxml, javafx.graphics; // Keep as is

    // Exports
    exports com.warage;
    exports com.warage.UI;
    exports com.warage.UI.Authentication;
    // You might also need to export com.warage.Model if it's used directly by other modules or if FXGL needs to access it
    // exports com.warage.Model; // Consider adding this if you get "package not visible" errors for Model objects later
}