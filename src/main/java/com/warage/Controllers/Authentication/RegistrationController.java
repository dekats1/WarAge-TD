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

public class RegistrationController {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private TextField emailField;

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registrationButton;

    @FXML
    private Button rulesButton;

    @FXML
    private CheckBox rulesCheckBox;

    @FXML
    private PasswordField verifyPasswordField;

    public void initialize() {
        TimeChecker.setBackgroundImage(backgroundImage);
        loginButton.setOnAction(event -> {
            Model.getInstance().getViewFactory().showLoginWindow();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });

        rulesButton.setOnAction(event -> {
           Model.getInstance().getViewFactory().showUserAgreementWindow();
        });
    }

}
