package com.warage.UI.UpdateBranch;

import com.warage.Model.Tower;
import com.warage.Model.TowerUpgrade;
import com.warage.Service.UpgradeTowerApi;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import lombok.Data;
import java.util.Comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class UpdateBranchUI {
    private List<TowerUpgrade> allTowerUpgrades = new ArrayList<>();
    private List<TowerUpgrade> filteredUpgrades = new ArrayList<>();
    private AnchorPane root;
    private Tower pickedTower;
    private UpgradeTowerApi upgradeTowerApi = new UpgradeTowerApi();
    private VBox firstBox = new VBox(5);
    private VBox secondBox = new VBox(5);
    private VBox thirdBox = new VBox(5);

    public UpdateBranchUI() {
        upgradeTowerApi.getAllTowerUpgrades().thenAccept(towerUpgrades->{
            allTowerUpgrades.addAll(towerUpgrades);
        });

        root = new AnchorPane();
        root.setPrefHeight(607.0);
        root.setPrefWidth(390.0);
        root.getStyleClass().add("update-branch-root");
        root.getStylesheets().add(getClass().getResource("/Styles/UpdateBranch.css").toExternalForm());

        populateUpgradeBoxes();

    }

    public void setTower(Tower tower) {
        this.pickedTower = tower;
        filteredUpgrades = allTowerUpgrades.stream().filter(towerUpgrade -> towerUpgrade.getTower().getTowerID().equals(tower.getTowerID())).collect(Collectors.toList());
        updateUI();
    }

    private void updateUI() {
        root.getChildren().clear();
        ImageView towerImage = new ImageView(new Image(getClass().getResource(pickedTower.getAssetPath()).toExternalForm()));
        towerImage.getStyleClass().add("tower-image");
        towerImage.setFitWidth(100);
        towerImage.setFitHeight(100);
        towerImage.setLayoutX(145);
        towerImage.setLayoutY(50);


        String boxStyle = "-fx-background-color: rgba(0,0,0,0.3); -fx-padding: 5;";
        firstBox.setStyle(boxStyle);
        secondBox.setStyle(boxStyle);
        thirdBox.setStyle(boxStyle);

        // Позиционируем VBox под стрелками
        firstBox.setLayoutX(50);
        firstBox.setLayoutY(200);

        secondBox.setLayoutX(175);
        secondBox.setLayoutY(200);

        thirdBox.setLayoutX(300);
        thirdBox.setLayoutY(200);

        createArrows(towerImage);
        populateUpgradeBoxes();

        root.getChildren().addAll(towerImage, firstBox, secondBox, thirdBox);
    }

    private void populateUpgradeBoxes() {


        Map<Integer, List<TowerUpgrade>> upgradesByPath = filteredUpgrades.stream()
                .collect(Collectors.groupingBy(TowerUpgrade::getUpgradePath));

        for (int path = 1; path <= 3; path++) {
            VBox currentBox = path == 1 ? firstBox : path == 2 ? secondBox : thirdBox;
            currentBox.getChildren().clear();

            List<TowerUpgrade> pathUpgrades = upgradesByPath.getOrDefault(path, Collections.emptyList())
                    .stream()
                    .sorted(Comparator.comparingInt(TowerUpgrade::getUpgradeLevel))
                    .collect(Collectors.toList());

            for (TowerUpgrade upgrade : pathUpgrades) {
                AnchorPane upgradePane = createUpgradePane(upgrade);
                currentBox.getChildren().add(upgradePane);
            }
        }
    }

    private AnchorPane createUpgradePane(TowerUpgrade upgrade) {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(100, 60);
        pane.setStyle("-fx-background-color: #34495e; -fx-border-color: #2c3e50;");

        // Уровень прокачки
        Label levelLabel = new Label("Ур. " + upgrade.getUpgradeLevel());
        levelLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        AnchorPane.setTopAnchor(levelLabel, 5.0);
        AnchorPane.setLeftAnchor(levelLabel, 5.0);

        // Стоимость
        Label costLabel = new Label(upgrade.getCost() + " золота");
        costLabel.setStyle("-fx-text-fill: #f1c40f;");
        AnchorPane.setTopAnchor(costLabel, 25.0);
        AnchorPane.setLeftAnchor(costLabel, 5.0);

        // Кнопка улучшения
        Button upgradeBtn = new Button("↑");
        upgradeBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        upgradeBtn.setPrefSize(20, 20);
        AnchorPane.setBottomAnchor(upgradeBtn, 5.0);
        AnchorPane.setRightAnchor(upgradeBtn, 5.0);

        upgradeBtn.setOnAction(e -> {
            // Логика улучшения башни
            System.out.println("Улучшение " + upgrade.getUpgradePath() +
                    " пути до уровня " + (upgrade.getUpgradeLevel() + 1));
        });

        pane.getChildren().addAll(levelLabel, costLabel, upgradeBtn);
        return pane;
    }

    private void createArrows(ImageView towerImage) {
        double centerX = towerImage.getLayoutX() + towerImage.getFitWidth() / 2;
        double startY = towerImage.getLayoutY() + towerImage.getFitHeight();
        double arrowLength = 100;

        double[] angles = {-25, 0, 25};

        for (int i = 0; i < 3; i++) {
            double angleRad = Math.toRadians(angles[i]);

            double endX = centerX + arrowLength * Math.sin(angleRad);
            double endY = startY + arrowLength * Math.cos(angleRad);

            // Линия стрелки
            Line line = new Line();
            line.getStyleClass().add("upgrade-arrow");
            line.setStartX(centerX);
            line.setStartY(startY);
            line.setEndX(endX);
            line.setEndY(endY);

            double headSize = 12;

            Polygon arrowHead = new Polygon();
            arrowHead.getStyleClass().add("arrow-head");


            arrowHead.getPoints().addAll(
                    endX, endY,

                    endX - headSize * Math.cos(angleRad),
                    endY + headSize * Math.sin(angleRad),

                    endX + headSize * Math.cos(angleRad),
                    endY - headSize * Math.sin(angleRad)
            );

            root.getChildren().addAll(line, arrowHead);
        }
    }
}