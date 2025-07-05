module WarAge.TD {
    requires com.almasb.fxgl.all;

    opens com.warage to javafx.fxml;
    opens com.warage.Controllers to javafx.fxml;
    opens com.warage.Controllers.Authentication to javafx.fxml;

    exports com.warage;
    exports com.warage.Controllers;
    exports com.warage.Controllers.Authentication;

    opens com.warage.Model to javafx.fxml;
    opens com.warage.Views to javafx.fxml, javafx.graphics;


}