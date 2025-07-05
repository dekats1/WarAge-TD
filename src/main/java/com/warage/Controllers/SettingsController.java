package com.warage.Controllers;

import com.warage.Model.Model;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private Button applyButton;

    @FXML
    private Slider charactersSlider;

    @FXML
    private Label charactersValueLabel;

    @FXML
    private Button closeSettingsButton;

    @FXML
    private Slider effectsSlider;

    @FXML
    private Label effectsValueLabel;

    @FXML
    private Slider musicSlider;

    @FXML
    private Label musicValueLabel;

    public void initialize() {
        musicValueLabel.setText(String.valueOf(Math.round(musicSlider.getValue())));
        effectsValueLabel.setText(String.valueOf(Math.round(effectsSlider.getValue())));
        charactersValueLabel.setText(String.valueOf(Math.round(charactersSlider.getValue())));

        musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
           musicValueLabel.setText(String.valueOf(Math.round(musicSlider.getValue())));
        });

        effectsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            effectsValueLabel.setText(String.valueOf(Math.round(effectsSlider.getValue())));
        });

        charactersSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            charactersValueLabel.setText(String.valueOf(Math.round(charactersSlider.getValue())));
        });

        closeSettingsButton.setOnAction(actionEvent ->{
            Stage stage = (Stage) closeSettingsButton.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
    }

}
