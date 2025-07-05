package com.warage.Controllers;

import com.warage.Model.Model;
import com.warage.Views.TimeChecker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private Button exitButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button startGameButton;

    @FXML
    private ImageView backgroundImage;

    public void initialize() {
        TimeChecker.setBackgroundImage(backgroundImage);
        startGameButton.setOnAction(event -> {
            Model.getInstance().getViewFactory().showMenuWindow();
            Stage stage = (Stage) startGameButton.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });

        settingsButton.setOnAction(event -> {
            Model.getInstance().getViewFactory().showSettingsWindow();
        });

        exitButton.setOnAction(event -> {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
    }
}

