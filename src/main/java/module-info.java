module WarAge.TD {
    requires com.almasb.fxgl.all;

    opens com.warage to javafx.fxml;
    opens com.warage.UI to javafx.fxml;
    opens com.warage.UI.Authentication to javafx.fxml;

    exports com.warage;
    exports com.warage.UI;
    exports com.warage.UI.Authentication;

    opens com.warage.Model to javafx.fxml;
    opens com.warage.Views to javafx.fxml, javafx.graphics;


}