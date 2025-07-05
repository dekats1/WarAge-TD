package com.warage.Controllers.Authentication;

import com.warage.Model.Model;
import com.warage.Views.TimeChecker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registrationButton;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private ImageView backgroundImage;

    public void initialize() {
        TimeChecker.setBackgroundImage(backgroundImage);

        registrationButton.setOnAction(event -> {
            Model.getInstance().getViewFactory().showRegistrationWindow();
            Stage stage = (Stage) registrationButton.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
    }

}
