package com.warage.UI;

import com.warage.Model.Model;
import com.warage.UI.Achievements.AchievementWindowUI;
import com.warage.Views.TimeChecker;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.function.Consumer;

public class MenuUI {

    private AnchorPane root;

    public MenuUI(Consumer<Node> achievementWindow, Runnable toCareerMap, Runnable infinityGame) {
        root = new AnchorPane();
        root.getStyleClass().add("root");
        root.getStylesheets().add(getClass().getResource("/Styles/Menu.css").toExternalForm());

        // Background Image
        ImageView backgroundImage = new ImageView();
        backgroundImage.setFitHeight(800.0);
        backgroundImage.setFitWidth(1600.0);
        backgroundImage.setLayoutX(-1.0);
        backgroundImage.setPickOnBounds(true);
        boolean isNight = TimeChecker.setBackgroundImageOnMain(backgroundImage);
        AnchorPane.setTopAnchor(backgroundImage, 0.0);
        AnchorPane.setBottomAnchor(backgroundImage, 0.0);
        AnchorPane.setLeftAnchor(backgroundImage, 0.0);
        AnchorPane.setRightAnchor(backgroundImage, 0.0);
        root.getChildren().add(backgroundImage);

        ImageView creditsImage = new ImageView();
        creditsImage.setFitHeight(150.0);
        creditsImage.setFitWidth(350.0);
        creditsImage.setLayoutX(1200);
        creditsImage.setPickOnBounds(true);
        creditsImage.setImage(new Image(getClass().getResourceAsStream("/Image/Credits.png")));

        Label creditsLabel = new Label("$"+ Model.getInstance().getPlayer().getMoney());
        creditsLabel.setLayoutX(1310);
        creditsLabel.setLayoutY(35);
        creditsLabel.setPrefWidth(180.0);
        creditsLabel.setPrefHeight(80.0);
        creditsLabel.getStyleClass().add("money-value-label");

        root.getChildren().addAll(creditsImage, creditsLabel);

        // Start Game
        ImageView playImage = new ImageView();
        playImage.setFitHeight(127.0);
        playImage.setFitWidth(127.0);
        playImage.setLayoutX(692.0);
        playImage.setLayoutY(629.0);
        playImage.setPickOnBounds(true);
        playImage.setPreserveRatio(true);
        playImage.setImage(new Image(getClass().getResource("/Image/play.png").toExternalForm()));
        root.getChildren().add(playImage);

        Button startGameButton = new Button();
        startGameButton.setLayoutX(689.0);
        startGameButton.setLayoutY(628.0);
        startGameButton.setMaxHeight(202.0);
        startGameButton.setMaxWidth(221.0);
        startGameButton.setPrefHeight(127.0);
        startGameButton.setPrefWidth(135.0);
        startGameButton.setMnemonicParsing(false);
        startGameButton.getStyleClass().add("round-button");
        root.getChildren().add(startGameButton);

        Label startGameLabel = new Label("ИГРАТЬ");
        startGameLabel.setLayoutX(677.0);
        startGameLabel.setLayoutY(731.0);
        startGameLabel.setMinWidth(96.0);
        startGameLabel.setPrefHeight(40.0);
        startGameLabel.setPrefWidth(96.0);
        startGameLabel.getStyleClass().add("round-button-label");
        root.getChildren().add(startGameLabel);

        startGameButton.setOnAction(e -> {
           infinityGame.run();
        });

        // Achievement Button
        ImageView achievementImage = new ImageView();
        achievementImage.setFitHeight(160.0);
        achievementImage.setFitWidth(160.0);
        achievementImage.setLayoutX(210.0);
        achievementImage.setLayoutY(620.0);
        achievementImage.setPickOnBounds(true);
        achievementImage.setPreserveRatio(true);
        achievementImage.setImage(new Image(getClass().getResource("/Image/Achievements.png").toExternalForm()));
        root.getChildren().add(achievementImage);

        Button awardsButton = new Button();
        awardsButton.setLayoutX(222.0);
        awardsButton.setLayoutY(635.0);
        awardsButton.setMaxHeight(202.0);
        awardsButton.setMaxWidth(221.0);
        awardsButton.setPrefHeight(127.0);
        awardsButton.setPrefWidth(135.0);
        awardsButton.setMnemonicParsing(false);
        awardsButton.getStyleClass().add("round-button");
        root.getChildren().add(awardsButton);

        awardsButton.setOnAction(event -> {
            AchievementWindowUI achievementWindowUI = new AchievementWindowUI();
            achievementWindow.accept(achievementWindowUI.getRoot());
        });

        // Company Button
        Button companyButton = new Button();
        companyButton.setLayoutX(910.0);
        companyButton.setLayoutY(634.0);
        companyButton.setMaxHeight(202.0);
        companyButton.setMaxWidth(221.0);
        companyButton.setPrefHeight(127.0);
        companyButton.setPrefWidth(135.0);
        companyButton.setMnemonicParsing(false);
        companyButton.getStyleClass().add("round-button");
        root.getChildren().add(companyButton);
        companyButton.setOnAction(event -> toCareerMap.run());

        // Shop Button
        Button shopButton = new Button();
        shopButton.setLayoutX(1158.0);
        shopButton.setLayoutY(631.0);
        shopButton.setMaxHeight(202.0);
        shopButton.setMaxWidth(221.0);
        shopButton.setPrefHeight(127.0);
        shopButton.setPrefWidth(135.0);
        shopButton.setMnemonicParsing(false);
        shopButton.getStyleClass().add("round-button");
        root.getChildren().add(shopButton);

        // Heroes Button
        ImageView heroeImage = new ImageView();
        heroeImage.setFitHeight(180.0);
        heroeImage.setFitWidth(180.0);
        heroeImage.setLayoutX(447.0);
        heroeImage.setLayoutY(590.0);
        heroeImage.setPickOnBounds(true);
        heroeImage.setPreserveRatio(true);
        heroeImage.setImage(new Image(getClass().getResource("/Image/hero.png").toExternalForm()));
        root.getChildren().add(heroeImage);

        Button heroesButton = new Button();
        heroesButton.setLayoutX(467.0);
        heroesButton.setLayoutY(633.0);
        heroesButton.setMaxHeight(202.0);
        heroesButton.setMaxWidth(221.0);
        heroesButton.setPrefHeight(127.0);
        heroesButton.setPrefWidth(135.0);
        heroesButton.setMnemonicParsing(false);
        heroesButton.getStyleClass().add("round-button");
        root.getChildren().add(heroesButton);

        // Gift Button (transparent)
        Button giftButton = new Button();
        giftButton.setLayoutX(857.0);
        giftButton.setLayoutY(351.0);
        giftButton.setMaxHeight(202.0);
        giftButton.setMaxWidth(166.0);
        giftButton.setPrefHeight(141.0);
        giftButton.setPrefWidth(166.0);
        giftButton.setMnemonicParsing(false);
        giftButton.getStyleClass().add("round-transparent-button");
        root.getChildren().add(giftButton);

        // Label for Awards Button
        Label awardsLabel = new Label("ДОСТИЖЕНИЯ");
        awardsLabel.setLayoutX(171.0);
        awardsLabel.setLayoutY(729.0);
        awardsLabel.setMinWidth(96.0);
        awardsLabel.setPrefHeight(52.0);
        awardsLabel.setPrefWidth(242.0);
        awardsLabel.getStyleClass().add("round-button-label");
        root.getChildren().add(awardsLabel);

        // Label for Company Button
        Label companyLabel = new Label("КАРЬЕРА");
        companyLabel.setLayoutX(902.0);
        companyLabel.setLayoutY(725.0);
        companyLabel.setMinWidth(96.0);
        companyLabel.setPrefHeight(40.0);
        companyLabel.setPrefWidth(96.0);
        companyLabel.getStyleClass().add("round-button-label");
        root.getChildren().add(companyLabel);

        // Label for Heroes Button
        Label heroesLabel = new Label("ГЕРОИ");
        heroesLabel.setLayoutX(453.0);
        heroesLabel.setLayoutY(727.0);
        heroesLabel.setMinWidth(96.0);
        heroesLabel.setPrefHeight(40.0);
        heroesLabel.setPrefWidth(96.0);
        heroesLabel.getStyleClass().add("round-button-label");
        root.getChildren().add(heroesLabel);

        // Label for Shop Button
        Label shopLabel = new Label("МАГАЗИН");
        shopLabel.setLayoutX(1151.0);
        shopLabel.setLayoutY(720.0);
        shopLabel.setMinWidth(96.0);
        shopLabel.setPrefHeight(40.0);
        shopLabel.setPrefWidth(96.0);
        shopLabel.getStyleClass().add("round-button-label");
        root.getChildren().add(shopLabel);

        if (isNight) {
            awardsButton.getStyleClass().setAll("round-button", "night-mode");
            heroesButton.getStyleClass().setAll("round-button", "night-mode");
            companyButton.getStyleClass().setAll("round-button", "night-mode");
            shopButton.getStyleClass().setAll("round-button", "night-mode");
            startGameButton.getStyleClass().setAll("round-button", "night-mode");

            shopLabel.getStyleClass().setAll("round-button-label", "night-mode");
            startGameLabel.getStyleClass().setAll("round-button-label", "night-mode");
            heroesLabel.getStyleClass().setAll("round-button-label", "night-mode");
            awardsLabel.getStyleClass().setAll("round-button-label", "night-mode");
            companyLabel.getStyleClass().setAll("round-button-label", "night-mode");

            creditsLabel.getStyleClass().setAll("money-value-label", "night-mode");
        }
    }

    public Pane getRoot() {
        return root;
    }
}
