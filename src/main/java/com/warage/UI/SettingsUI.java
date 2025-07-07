package com.warage.UI;

import com.warage.Model.Model;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SettingsUI {

    private AnchorPane root;

    public SettingsUI(Runnable showMainMenuUi) {
        root = new AnchorPane();
        root.setPrefHeight(699.0);
        root.setPrefWidth(599.0);
        root.getStylesheets().add(getClass().getResource("/Styles/Settings.css").toExternalForm());

        // Title Label "Настройки"
        Label titleLabel = new Label("Настройки");
        titleLabel.setLayoutX(204.0);
        titleLabel.setLayoutY(80.0);
        titleLabel.getStyleClass().add("title-label");
        root.getChildren().add(titleLabel);

        // Music Label
        Label musicLabel = new Label("Музыка:");
        musicLabel.setLayoutX(77.0);
        musicLabel.setLayoutY(191.0);
        musicLabel.getStyleClass().add("setting-label");
        root.getChildren().add(musicLabel);

        // Music Slider
        Slider musicSlider = new Slider();
        musicSlider.setLayoutX(190.0);
        musicSlider.setLayoutY(191.0);
        musicSlider.setMax(100);
        musicSlider.setMin(0);
        musicSlider.setValue(50);
        root.getChildren().add(musicSlider);

        // Effects Label
        Label effectsLabel = new Label("Эффекты:");
        effectsLabel.setLayoutX(73.0);
        effectsLabel.setLayoutY(261.0);
        effectsLabel.getStyleClass().add("setting-label");
        root.getChildren().add(effectsLabel);

        // Effects Slider
        Slider effectsSlider = new Slider();
        effectsSlider.setLayoutX(190.0);
        effectsSlider.setLayoutY(261.0);
        effectsSlider.setMax(100);
        effectsSlider.setMin(0);
        effectsSlider.setValue(50);
        root.getChildren().add(effectsSlider);

        // Characters Label
        Label charactersLabel = new Label("Персонажи:");
        charactersLabel.setLayoutX(64.0);
        charactersLabel.setLayoutY(326.0);
        charactersLabel.getStyleClass().add("setting-label");
        root.getChildren().add(charactersLabel);

        // Characters Slider
        Slider charactersSlider = new Slider();
        charactersSlider.setLayoutX(190.0);
        charactersSlider.setLayoutY(326.0);
        charactersSlider.setMax(100);
        charactersSlider.setMin(0);
        charactersSlider.setValue(50);
        root.getChildren().add(charactersSlider);

        // Apply Button
        Button applyButton = new Button("Применить");
        applyButton.setLayoutX(216.0);
        applyButton.setLayoutY(591.0);
        applyButton.setPrefHeight(59.0);
        applyButton.setPrefWidth(167.0);
        applyButton.getStyleClass().add("action-button");
        root.getChildren().add(applyButton);

        // Close Settings Button
        Button closeSettingsButton = new Button("X");
        closeSettingsButton.setLayoutX(14.0);
        closeSettingsButton.setLayoutY(14.0);
        closeSettingsButton.setPrefHeight(43.0);
        closeSettingsButton.setPrefWidth(37.0);
        closeSettingsButton.getStyleClass().add("action-button");
        root.getChildren().add(closeSettingsButton);

        // Music Value Label
        Label musicValueLabel = new Label("100");
        musicValueLabel.setLayoutX(410.0);
        musicValueLabel.setLayoutY(194.0);
        musicValueLabel.getStyleClass().add("value-label");
        root.getChildren().add(musicValueLabel);

        // Effects Value Label
        Label effectsValueLabel = new Label("100");
        effectsValueLabel.setLayoutX(410.0);
        effectsValueLabel.setLayoutY(264.0);
        effectsValueLabel.getStyleClass().add("value-label");
        root.getChildren().add(effectsValueLabel);

        // Characters Value Label
        Label charactersValueLabel = new Label("100");
        charactersValueLabel.setLayoutX(410.0);
        charactersValueLabel.setLayoutY(329.0);
        charactersValueLabel.getStyleClass().add("value-label");
        root.getChildren().add(charactersValueLabel);
        musicValueLabel.textProperty().bind(musicSlider.valueProperty().asString("%.0f"));
        effectsValueLabel.textProperty().bind(effectsSlider.valueProperty().asString("%.0f"));
        charactersValueLabel.textProperty().bind(charactersSlider.valueProperty().asString("%.0f"));
    }

    public Pane getRoot(){
        return root;
    }
}


