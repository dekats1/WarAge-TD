package com.warage.UI;

import com.warage.Views.TimeChecker;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MainMenuUI {
    private AnchorPane root;

    public MainMenuUI(Runnable showGameUI, Runnable showSettings, Runnable backToLoginUI) {
        // Initialize the root AnchorPane
        root = new AnchorPane();
        root.getStyleClass().add("root");
        root.getStylesheets().add(getClass().getResource("/Styles/MainMenu.css").toExternalForm());

        // Background Image
        ImageView backgroundImage = new ImageView();
        backgroundImage.setFitHeight(800.0);
        backgroundImage.setFitWidth(1600.0);
        backgroundImage.setLayoutX(-1.0);
        backgroundImage.setPickOnBounds(true);
        TimeChecker.setBackgroundImage(backgroundImage);
        AnchorPane.setTopAnchor(backgroundImage, 0.0);
        AnchorPane.setBottomAnchor(backgroundImage, 0.0);
        AnchorPane.setLeftAnchor(backgroundImage, 0.0);
        AnchorPane.setRightAnchor(backgroundImage, 0.0);
        root.getChildren().add(backgroundImage);

        // Start Game Button
        Button startGameButton = new Button("Начать");
        startGameButton.setLayoutX(694.0);
        startGameButton.setLayoutY(511.0);
        startGameButton.setMnemonicParsing(false);
        startGameButton.getStyleClass().add("menu-button");
        startGameButton.setOnAction(e->showGameUI.run());
        root.getChildren().add(startGameButton);

        // Settings Button
        Button settingsButton = new Button("Настройки");
        settingsButton.setLayoutX(694.0);
        settingsButton.setLayoutY(565.0);
        settingsButton.setMnemonicParsing(false);
        settingsButton.getStyleClass().add("menu-button");
        settingsButton.setOnAction(e->showSettings.run());
        root.getChildren().add(settingsButton);

        // Exit Button
        Button exitButton = new Button("Выход");
        exitButton.setLayoutX(694.0);
        exitButton.setLayoutY(618.0);
        exitButton.setMnemonicParsing(false);
        exitButton.getStyleClass().add("menu-button");
        exitButton.setOnAction(e->backToLoginUI.run());
        root.getChildren().add(exitButton);

        // WarAge Logo
        ImageView warAgeLogo = new ImageView();
        warAgeLogo.setFitHeight(508.0);
        warAgeLogo.setFitWidth(539.0);
        warAgeLogo.setLayoutX(525.0);
        warAgeLogo.setLayoutY(17.0);
        warAgeLogo.setPickOnBounds(true);
        warAgeLogo.setPreserveRatio(true);
        warAgeLogo.setImage(new Image(getClass().getResource("/Image/WarAge.png").toExternalForm()));
        root.getChildren().add(warAgeLogo);
    }

    public Pane getRoot(){
        return root;
    }
}

