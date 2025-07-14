package com.warage.UI.InterfaceUI;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Data;




@Data
public class PauseMenuUI {
    private AnchorPane root;
    public PauseMenuUI(Runnable exitToMenu, Runnable restartGame, Runnable continueGame) {
        root = new AnchorPane();
        root.setPrefWidth(1600);
        root.setPrefHeight(800);
        root.getStylesheets().add(getClass().getResource("/Styles/InterfaceStyles/PauseMenu.css").toExternalForm());
        root.setBackground(new Background(new BackgroundFill(Color.rgb(0,0,0,0.7), null, null)));

        // ПАУЗА ТЕКСТ
        Label pauseLabel = new Label("ПАУЗА");
        pauseLabel.getStyleClass().add("pauseLabel");

        ImageView exitGameImage = new ImageView(new Image(getClass().getResource("/Image/InterfaceIcons/exitRedImage.png").toExternalForm()));
        exitGameImage.setFitHeight(40);
        exitGameImage.setFitWidth(40);
        ImageView restartGameImage = new ImageView(new Image(getClass().getResource("/Image/InterfaceIcons/restart.png").toExternalForm()));
        restartGameImage.setFitHeight(40);
        restartGameImage.setFitWidth(40);
        ImageView continueGameImage = new ImageView(new Image(getClass().getResource("/Image/InterfaceIcons/continue.png").toExternalForm()));
        continueGameImage.setFitHeight(40);
        continueGameImage.setFitWidth(40);

        // Кнопки
        Button exitButton = new Button();
        exitButton.getStyleClass().addAll("menu-button", "exitButton");
        exitButton.setGraphic(exitGameImage);
        exitButton.setOnAction(e -> exitToMenu.run());

        Button restartButton = new Button();
        restartButton.getStyleClass().addAll("menu-button", "restartButton");
        restartButton.setGraphic(restartGameImage);
        restartButton.setOnAction(e -> restartGame.run());


        Button continueButton = new Button();
        continueButton.getStyleClass().addAll("menu-button", "continueButton");
        continueButton.setGraphic(continueGameImage);
        continueButton.setOnAction(e -> continueGame.run());



        // Горизонтальный контейнер для кнопок
        HBox buttonsContainer = new HBox( exitButton, restartButton, continueButton);
        buttonsContainer.getStyleClass().add("buttons-container");

        // Главный вертикальный контейнер
        VBox mainContainer = new VBox(pauseLabel, buttonsContainer);
        mainContainer.getStyleClass().add("main-container");
        mainContainer.setSpacing(40);

        //
        AnchorPane.setTopAnchor(mainContainer, (root.getPrefHeight() - 200) / 2);
        AnchorPane.setLeftAnchor(mainContainer, (root.getPrefWidth() - 700) / 2);

        root.getChildren().add(mainContainer);
    }

}
