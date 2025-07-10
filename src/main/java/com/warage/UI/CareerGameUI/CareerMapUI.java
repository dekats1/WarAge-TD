package com.warage.UI.CareerGameUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class CareerMapUI {
    private AnchorPane root;

    public CareerMapUI() {
        root = new AnchorPane();
        root.setPrefSize(600, 400);
        root.getStylesheets().add(getClass().getResource("/Styles/CareerMap.css").toExternalForm());

        // Задний фон — изображение карты
        ImageView background = new ImageView(new Image(getClass().getResource("/Image/CareerMap.png").toExternalForm()));
        background.setFitWidth(1600);
        background.setFitHeight(800);
        background.setPickOnBounds(true);
        AnchorPane.setTopAnchor(background, 0.0);
        AnchorPane.setBottomAnchor(background, 0.0);
        AnchorPane.setLeftAnchor(background, 0.0);
        AnchorPane.setRightAnchor(background, 0.0);
        root.getChildren().add(background);

        int[][] positions = {
                {850, 750}, {890, 710}, {835, 682}, {770, 665}, {700, 650}, {600, 645},
                {500, 630}, {400, 620}, {283, 261}, {421, 152}, {388, 172}, {358, 187},
                {323, 204}, {290, 223}, {277, 117}, {252, 146}, {290, 155}, {330, 66},
                {316, 102}, {375, 46}
        };

        for (int i = 0; i < positions.length; i++) {
            Button levelBtn = new Button(String.valueOf(i + 1));
            levelBtn.setLayoutX(positions[i][0]);
            levelBtn.setLayoutY(positions[i][1]);
            levelBtn.setPrefSize(36, 31);
            levelBtn.getStyleClass().add("button-level");
            root.getChildren().add(levelBtn);
        }
    }

    public AnchorPane getRoot() {return root;}
}
